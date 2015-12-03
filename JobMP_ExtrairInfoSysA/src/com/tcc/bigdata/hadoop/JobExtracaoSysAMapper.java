package com.tcc.bigdata.hadoop;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @author Rodrigo Rosa
 *
 * Classe de mapeamento (Mapper). 
 * Responsável por buscar nos arquivos apenas as linhas que contém erros.
 * Os erros são pesquisados por meio de expressão regular informada na chamada do job
 */
public class JobExtracaoSysAMapper extends Mapper<Object, Text, NullWritable, Text> {

	// Implementação do método map
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		
		String txt = value.toString(); // Pega o conteúdo de uma linha do arquivo
		String mapRegex = context.getConfiguration().get("mapregex"); // Pega a expressão regular de filtro
		
		Pattern p = Pattern.compile(mapRegex, Pattern.CASE_INSENSITIVE); // Aplica o padrão RegEx
		Matcher m = p.matcher(txt); // Extrai linha com base na expressão
		
		// Se encontrou algo escreve em um arquivo de saída (reduce)
		if (m.matches()) { 
			// Formata a linha de saída: <data_hora>,<cliente>,<operadora>,<mensagem>,<sistema>,<tipo>,<resposta>
			Text registro = JobExtracaoSysAUtil.retornaRegistro(value);
			if (!"00".equals(JobExtracaoSysAUtil.getResposta())) { // Verifica se realmente é erro
				context.write(NullWritable.get(), registro); // Escreve no arquivo de saída
			}
		}	
	}
}