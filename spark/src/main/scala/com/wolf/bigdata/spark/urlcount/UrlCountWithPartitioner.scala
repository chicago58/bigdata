package com.wolf.bigdata.spark.urlcount

import java.net.URL

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


/**
  * @Description 统计每个学院访问url的前3名 [实现：根据域名重新分区后二次排序]
  * @Author wangqikang
  * @Date 2019-07-17 21:20
  */
object UrlCountWithPartitioner {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("UrlCountWithPartitioner").setMaster("local[2]")
    val sc = new SparkContext(conf)

    // 读取数据
    val lines: RDD[String] = sc.textFile(args(0))

    // 逐行切分数据，并按url计数
    val urlAndOne = lines.map(line => {
      val fields = line.split("\t")
      val url = fields(1)
      (url, 1)
    })

    // 按url聚合，统计每个url访问的次数并缓存
    val summedUrl = urlAndOne.reduceByKey(_ + _).cache()

    // 拼接数据(域名,(url,访问次数))，并按域名分组
    val rdd1 = summedUrl.map(t => {
      val host = new URL(t._1).getHost
      (host, (t._1, t._2))
    })

    // 统计所有域名
    val urls = rdd1.map(_._1).distinct().collect()
    // 按域名自定义分区器
    val partitioner = new HostPartitioner(urls)
    // 按照自定义的分区器重新分区
    val partitionedRdd = rdd1.partitionBy(partitioner)

    /**
      * 遍历每个分区，并使用scala list的排序方法二次排序
      */
    val result = partitionedRdd.mapPartitions(iter => {
      iter.toList.sortBy(_._2._2).reverse.take(3).iterator
    })

    result.saveAsTextFile("/Users/wangqikang/Work/code/bigdata/spark/src/main/resources/urlcount/output")

    sc.stop()
  }

}