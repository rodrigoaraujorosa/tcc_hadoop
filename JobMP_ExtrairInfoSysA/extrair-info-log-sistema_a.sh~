#!/bin/sh

# Declarando variáveis para os diretórios do HDFS
DIR_OUTPUT=/user/hadoop/output
DIR_INPUT=/user/hadoop/input/sistema_a

# Declarando variáveis para o Job do Sqoop
BD_CONEXAO=jdbc:mysql://192.168.1.100:3306/tcc_hadoop
BD_USUARIO=root
BD_TABELA=resultado

echo '-------------------------------------------------------------------------------------------------------'
echo '|                      Iniciando Job MapReduce [Extrair Info Log Sistema A]                           |'
echo '-------------------------------------------------------------------------------------------------------'
echo ''
# Excluindo diretório output do HDFS
echo "Limpando o diretório de saída do HDFS ($DIR_OUTPUT)"
echo '-------------------------------------------------------------------------------------------------------'
echo ''
hdfs dfs -rm -R -skipTrash $DIR_OUTPUT
# Executando o Job MapReduce
$HADOOP_HOME/bin/hadoop jar JobExtracaoSysA.jar '.*senha.*?' $DIR_INPUT $DIR_OUTPUT

echo "Job MapReduce - Diretório de entrada ($DIR_INPUT)"
echo '-------------------------------------------------------------------------------------------------------'
echo ''
hdfs dfs -ls $DIR_INPUT
echo ''
echo "Job MapReduce - Diretório de saída ($DIR_OUTPUT)"
echo '-------------------------------------------------------------------------------------------------------'
hdfs dfs -cat $DIR_OUTPUT/part-*
echo ''

echo '-------------------------------------------------------------------------------------------------------'
echo '|                        Iniciando Job Sqoop [Carregar Banco de Dados MySQL]                          |'
echo '-------------------------------------------------------------------------------------------------------'
echo ''
sqoop export --connect $BD_CONEXAO --username $BD_USUARIO --table $BD_TABELA --export-dir $DIR_OUTPUT


