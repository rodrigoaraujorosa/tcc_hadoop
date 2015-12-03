package com.tcc.bigdata.hadoop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.Text;

/**
 * @author Rodrigo Rosa
 * 
 * Classe com utilitários para auxiliar a execução do Job.
 */
public class JobExtracaoSysBUtil {
	//                                  Expressão regular divide em grupos as informações do log nos seguinte formato
	//                                  (Data                 ) (Hora:Minuto:Segundo.miliseg   ) (Pri..) (Class)     (log xml)
	static final String REGEX_FORMAT = "(\\[\\d{4}\\d{2}\\d{2}) (\\d{2}:\\d{2}:\\d{2}.\\d{3}\\]) ([^ ]*) ([^ ]*) - < (.*)$";
	static final String REGEX_ESTABELECIMENTO = ".*<estabelecimento>(.*?)</estabelecimento>.*?"; // extrai o estabelecimento do log xml
	static final String REGEX_MENSAGEM = ".*<dadosGenericos>(.*?)</dadosGenericos>.*?";          // extrai a msg de erro do log xml      
	static final String REGEX_RESPOSTA = ".*<codigoResposta>(.*?)</codigoResposta>.*?";          // extrai o codigo de resposta do log xml
	static final int QUANTIDADE_GRUPOS = 5; // número de grupos de informação a serem extraídos
	
	private static String data = "";
	private static String hora = "";
	private static String prioridade;
	private static String classe;
	private static String xml;
	private static String estabelecimento = "";
	private static String operadora = "";
	private static String mensagem = "";
	private static String resposta = "";
	private static String sistema = "SISTEMA B"; // Correspondente Bancário
	private static String tipo = "MENU";         // Avisos e Falhas relacionadas ao menu do POS (5 tipos de erros)
	
	public static Text retornaRegistro(Text linha){
		
		Pattern p = Pattern.compile(REGEX_FORMAT);                      // Cria um padrão com a expressão regular que divide a linha em grupos
		Matcher m = p.matcher(linha.toString().replaceAll("\\s+"," ")); // Aplica o padrão sobre a linha e trata os espaços em excesso na linha

		if (m.matches() && m.groupCount() == QUANTIDADE_GRUPOS) { // Se a condição da RegEx for satisfeita

			data = m.group(1).replace("[", "");
			data = data.substring(0, 4)+"-"+data.substring(4, 6)+"-"+data.substring(6,8); // organiza a data no formato aaaa-mm-dd
			hora = m.group(2).replace("]", "").replace(".", ",");
			setPrioridade(m.group(3));
			setClasse(m.group(4));
			xml = m.group(5);
			
			p = Pattern.compile(REGEX_ESTABELECIMENTO);
			m = p.matcher(xml);
			if (m.matches()){
				estabelecimento = m.group(1);
			}

			p = Pattern.compile(REGEX_MENSAGEM);
			m = p.matcher(xml);
			if (m.matches()){
				mensagem = m.group(1);
				// Faz a limpeza dos dados. Limpa a mensagem de erro, retirando a tag de identificação
				if (mensagem.contains("#9007@")){
					mensagem = mensagem.substring(mensagem.indexOf("#9007@")+6).replace(",",".");					
				}
			}

			p = Pattern.compile(REGEX_RESPOSTA);
			m = p.matcher(xml);
			if (m.matches()){
				JobExtracaoSysBUtil.setResposta(m.group(1));
			}
		}
		//         Retorna a mensagem formatada para gravar no arquivo de saída do Hadoop
		//         Coloca no formato CSV para posterior carga no Banco de Dados via Sqoop
		return new Text(data + " " + hora.substring(0, 8) + "," + estabelecimento + "," + operadora + "," + mensagem + "," + sistema + "," + tipo + "," + resposta);
	}

	public static String getPrioridade() {
		return prioridade;
	}

	public static void setPrioridade(String prioridade) {
		JobExtracaoSysBUtil.prioridade = prioridade;
	}

	public static String getClasse() {
		return classe;
	}

	public static void setClasse(String classe) {
		JobExtracaoSysBUtil.classe = classe;
	}


	public static String getResposta() {
		return resposta;
	}

	public static void setResposta(String resposta) {
		JobExtracaoSysBUtil.resposta = resposta;
	}
	
}
