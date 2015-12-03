package com.tcc.bigdata.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * @author Rodrigo Rosa
 *
 * Classe que implementa o método main. Responsável pela parametrização e execução do Job MapReduce
 */
public class JobExtracaoSysBDriver {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		
		// Verifica se os parâmetros foram passados corretamente
		if (otherArgs.length != 3) {
			System.err.println("Usar: JobExtracaoSysB <regex> <dir_entrada> <dir_saida>");
			System.exit(2);
		}
		conf.set("mapregex", otherArgs[0]); // Define a expresão regular informada por parâmetro
		
		Job job = new Job(conf, "Extrair Info Log Sistema B");       // Cria um novo job mapreduce
		job.setJarByClass(JobExtracaoSysBDriver.class);              // Define a classe de execução
		job.setMapperClass(JobExtracaoSysBMapper.class);             // Define a classe Mapper
		job.setOutputKeyClass(NullWritable.class);                   // Define as chaves de saída como nulo
		job.setOutputValueClass(Text.class);                         // Define os valores de saída como texto
		job.setNumReduceTasks(0);                                    // Define como zero o número de reducers
		FileInputFormat.addInputPath(job, new Path(otherArgs[1]));   // Define o diretório de entrada do HDFS
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[2])); // Define o diretório de saída do HDFS
		System.exit(job.waitForCompletion(true) ? 0 : 1);            // Se o job completar com sucesso retorna true
	}
}
