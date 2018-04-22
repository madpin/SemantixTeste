// Leio os 2 arquivos, e defino em variáveis diferentes
val rdd_aug = sc.textFile("/FileStore/tables/NASA_access_log_Aug95.gz")
val rdd_jul = sc.textFile("/FileStore/tables/NASA_access_log_Jul95.gz")

// Faço o union dos 2 RDD, em um unico.
val rdd = rdd_jul.union(rdd_aug)

val hosts = rdd.map(host => host.split(" ")(0).trim)

println("Count Distinct Hosts")

// Count distinct dos hosts parseados
println(hosts.distinct.count())

val REGEX = """^(\S+) (\S+) (\S+) \[([\w:/]+\s[+\-]\d{4})\] "(\S+) (\S+)(.*)" (\d{3}) (\S+)""".r

case class AccessRec( host: String, timeStamp: String, url:String, httpCode:Int, reqSize:String)

// Função para parsear cada linha do RDD, em um objeto AccessRec
def parseLine(log: String):
	AccessRec = {
		val found = REGEX.findFirstMatchIn(log) 
		if (found.isEmpty)
		{
			println("Erro na linha: " + log)
			AccessRec("Empty", "", "",  -1 ,  "-1" )
		}
		else 
		{
			AccessRec(
              found.get.group(1),
              found.get.group(4),
              found.get.group(6),
              found.get.group(8).toInt,
              found.get.group(9)
            )
		}
	}

val accessDf = rdd.map(parseLine).toDF()
// Exploração, para vermos o schema criado
accessDf.printSchema

println("Count de 404:")
// contando as linhas com httpCode igual a 404
accessDf.filter($"httpCode" === "404").count()

accessDf.filter($"httpCode" === "404").groupBy("host").count().sort($"count".desc).show(5)

accessDf.createOrReplaceTempView("nasa")

println("Lista com os erros 404 por dia:")
spark.sql("""
select 
 left(timeStamp,11) as data,
 count(*) as count
from nasa
where
  httpCode == "404"
group by
  left(timeStamp,11)
""").show()

println("Tamanho total de todas requisições no período:")

spark.sql("""
select 
 sum(reqSize) as reqSizeTotal
from nasa
""").show()