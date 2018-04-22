# Teste Semantix

### #. Qual o objetivo do comando cache​ ​em Spark?

A proteção contra falhas do Spark, faz com que o estágio da computação seja mapeado.
Dessa forma, no caso de falha de um node, toda a computação feita por ele, será reprocessada por outro node.
Caso essa computação seja muito demorada, isso pode causar atraso nos processos, o cache, serve para guardar um estágio intermediário em disco, para caso haja falha de um job, ele possa ser recuperado a partir do ultimo cache.
Outra função do cache, é quando um mesmo RDD é utilizado por 2 braches, dessa forma explicitamos que não deverá ser recalculado o RDD do começo.


### #. O mesmo código implementado em Spark é normalmente mais rápido que a implementação equivalente em MapReduce. Por quê?

Os processos em MapReduce, são executados em disco, apesar do HDFS dar bastante agilidade aos processos, ainda assim gravação em disco é uma operação muito custosa.
O Spark por sua vez, trabalha com os dados em memória, muito mais "baratos" para o processo.
Algumas vezes falam de 100x mais rápido, esses são usos de casos de machine learning, onde existe multiplas iterações, para que o erro seja diminuido, e esses são os uso de casos onde o Spark mais se destaca.


### #. Qual é a função do SparkContext​?
SparkContext é um cliente disponivel em todas linguagens "spark", Java, Scala, Python e R. Ele que estabelece a comunicação com o ambiente Spark, através dele que podemos criar RDD, acumuladores e variaveis broadcast.
Dessa forma, ele age como o master da aplicação Spark.

### #. Explique com suas palavras o que é Resilient​ ​Distributed​ ​Datasets​ (RDD)

RDD, é a abstração Spark mais básica, é uma coleção de registros, que podem ou não estar distribuidos entre vários nodes. Essa coleção pode conter primitivos, ou valor de valores, como tuplas ou outros objetos.


### #. GroupByKey​ ​é menos eficiente que reduceByKey​ ​em grandes dataset. Por quê?
Os dois vão gerar a mesma resposta, mas o reduceByKey​ vai agrupa os dados por key antes do "shuffling", enquanto no GroupByKey​ ele faz primeiro o "shuffling" e depois os agrupa:

![GroupByKey​](https://databricks.gitbooks.io/databricks-spark-knowledge-base/content/images/group_by.png)
![reduceByKey​](https://databricks.gitbooks.io/databricks-spark-knowledge-base/content/images/reduce_by.png)

### #. Explique o que o código Scala abaixo faz.
```
val textFile = sc.textFile("hdfs://...")
val counts = textFile.flatMap(line => line.split(" "))
.map(word => (word, 1))
.reduceByKey(_ + _)
counts.saveAsTextFile("hdfs://...")
```

Resposta:

```
# Carrega o arquivo "hdfs://..." em um RDD
# sc representa o SparkContext da pergunta anterior
val textFile = sc.textFile("hdfs://...")

# line.split(" ") - Quebra a linha por espaços
val counts = textFile.flatMap(line => line.split(" "))
# Cria uma tupla, com a palavra como "chave", e 1 como valor
.map(word => (word, 1))
# Soma os "1" de cada uma das palavras
.reduceByKey(_ + _)

# Salva a resposta no hdfs, como um "dicionário" com a palavra, e a quantidade de vezezs que apareceu
counts.saveAsTextFile("hdfs://...")
```



|                host|           timeStamp|                 url|httpCode|reqSize|
|        199.72.81.55|01/Jul/1995:00:00...|    /history/apollo/|     200|   6245|
|unicomp6.unicomp.net|01/Jul/1995:00:00...| /shuttle/countdown/|     200|   3985|
|      199.120.110.21|01/Jul/1995:00:00...|/shuttle/missions...|     200|   4085|
|  burger.letters.com|01/Jul/1995:00:00...|/shuttle/countdow...|     304|      0|
|      199.120.110.21|01/Jul/1995:00:00...|/shuttle/missions...|     200|   4179|