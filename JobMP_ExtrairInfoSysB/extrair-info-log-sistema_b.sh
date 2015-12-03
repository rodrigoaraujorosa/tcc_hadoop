#!/bin/sh

# Definir variáveis para os diretórios origem (servidor hadoop) e destino (HDFS)
DIR_LOG_HADOOP=/home/hadoop/bigdata/logs/sistema_b
DIR_INPUT_HDFS=/user/hadoop/input/sistema_b

# Declarando variáveis para os diretórios do HDFS e para o Job do Sqoop
DIR_INPUT=/user/hadoop/input/sistema_b; DIR_OUTPUT=/user/hadoop/output
BD_CONEXAO=jdbc:mysql://192.168.1.108:3306/tcc_hadoop; BD_USUARIO=root; BD_TABELA=sqoopinseredados


echo ''
echo '------------------------------------------------------------------------------------------------------------------------------------------------------'
echo '|                                               Iniciando Inclusão de Arquivos de Log do Sistema B no HDFS                                           |'
echo '------------------------------------------------------------------------------------------------------------------------------------------------------'
echo ''

# Lista os arquivos e copia para o HDFS
echo "$(date +%y/%m/%d" "%H:%M:%S) INFO Iniciando a cópia dos arquivos ($DIR_LOG_HADOOP):"
echo '------------------------------------------------------------------------------------------------------------------------------------------------------'
echo ''
ls -la $DIR_LOG_HADOOP
echo ''
hdfs dfs -put $DIR_LOG_HADOOP/requisicoes* $DIR_INPUT_HDFS

# Lista os arquivos copiados no HDFS
echo "$(date +%y/%m/%d" "%H:%M:%S) INFO Arquivos copiados para o HDFS ($DIR_INPUT_HDFS):"
echo '------------------------------------------------------------------------------------------------------------------------------------------------------'
echo ''
hdfs dfs -ls $DIR_INPUT_HDFS
echo ''

# Remove os arquivos do diretório de origem
echo "$(date +%y/%m/%d" "%H:%M:%S) INFO Removendo arquivos do diretório origem ($DIR_LOG_HADOOP)"
echo '------------------------------------------------------------------------------------------------------------------------------------------------------'
echo ''
rm -R $DIR_LOG_HADOOP/requisicoes*

echo ''
echo '------------------------------------------------------------------------------------------------------------------------------------------------------'
echo '|                                                  Iniciando Job MapReduce [Extrair Info Log Sistema B]                                              |'
echo '------------------------------------------------------------------------------------------------------------------------------------------------------'
echo ''

# Excluindo diretório output do HDFS
echo "$(date +%y/%m/%d" "%H:%M:%S) INFO Limpando o diretório de saída do HDFS ($DIR_OUTPUT)"
echo '------------------------------------------------------------------------------------------------------------------------------------------------------'
echo ''
hdfs dfs -rm -R -skipTrash $DIR_OUTPUT
echo ''

# Executando o Job MapReduce
echo "$(date +%y/%m/%d" "%H:%M:%S) INFO Executando Job Extrair Info Log Sistema B"
echo '------------------------------------------------------------------------------------------------------------------------------------------------------'
echo ''
$HADOOP_HOME/bin/hadoop jar JobExtracaoSysB.jar '.*#9007@.*?' $DIR_INPUT $DIR_OUTPUT
echo ''
echo "$(date +%y/%m/%d" "%H:%M:%S) INFO Job MapReduce - Diretório de entrada ($DIR_INPUT)"
echo '------------------------------------------------------------------------------------------------------------------------------------------------------'
echo ''
hdfs dfs -ls $DIR_INPUT
echo ''
echo "$(date +%y/%m/%d" "%H:%M:%S) INFO Job MapReduce - Diretório de saída ($DIR_OUTPUT)"
echo '------------------------------------------------------------------------------------------------------------------------------------------------------'
echo ''
hdfs dfs -cat $DIR_OUTPUT/part-*
echo ''
echo "$(date +%y/%m/%d" "%H:%M:%S) INFO Job MapReduce - Excluindo Arquivos do diretório de entrada ($DIR_INPUT)"
echo '------------------------------------------------------------------------------------------------------------------------------------------------------'
echo ''
hdfs dfs -rm -R -skipTrash $DIR_INPUT/requisicoes*
echo ''

# Executando o Job de exportação do Sqoop
echo '------------------------------------------------------------------------------------------------------------------------------------------------------'
echo '|                                                   Iniciando Job Sqoop [Carregar Banco de Dados MySQL]                                              |'
echo '------------------------------------------------------------------------------------------------------------------------------------------------------'
echo ''
sqoop export --connect $BD_CONEXAO --username $BD_USUARIO --table $BD_TABELA --export-dir $DIR_OUTPUT

