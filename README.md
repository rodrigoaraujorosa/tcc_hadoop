# Projetos de Trabalho de Conclusão de Curso
#### BIG DATA: Análise de viabilidade de emprego do Hadoop para processamento de logs

Projetos utilizados nos experimentos para o Trabalho de Conclusão de Curso em Sistemas de Informação da UNISINOS. Implementação de Jobs MapReduce utilizando o padrão de design do tipo Filtragem com o recurso Distributed Grep.

Foram criados dois projetos distintos, cada um implementando um job para os sistemas que foram estudados. Os jobs visam processar logs de sistemas transacionais, extrair dados relevantes, gravar em banco de dados e, por fim, gerar informação com valor para o negócio de uma empresa.

Cada projeto possui três classes:

1. Mapper (classe de mapeamento)
2. Driver (classe que implementa o método main. Configura as propriedades do Job)
3. Utilitária (classe contendo métodos auxiliares para tratamento dos dados de entrada e saída dos jobs)

Blibliotecas necessárias para a implementação de jobs MapReduce:

* hadoop-common-2.7.1.jar
* commons-cli-1.3.1.jar
* hadoop-mapreduce-client-core-2.7.1.jar
* hadoop-test-1.2.1.jar

A versão do Hadoop utilizada foi a 2.7.1.

Também foi utilzado o Sqoop para transferência de dados entre o HDFS e o Banco de Dados MySQL utilizado nos experimentos.
