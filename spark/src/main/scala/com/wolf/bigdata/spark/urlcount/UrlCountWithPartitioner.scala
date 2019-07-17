package com.wolf.bigdata.spark.urlcount

import java.net.URL

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

import scala.collection.mutable


/**
  * @Description
  * @Author wangqikang
  * @Date 2019-07-17 21:20
  */
object UrlCountWithPartitioner {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("UrlCountWithPartitioner").setMaster("local[2]")
    val sc = new SparkContext(conf)

    val lines: RDD[String] = sc.textFile(args(0))

    // split
    val urlAndOne = lines.map(line => {
      val fields = line.split("\t")
      val url = fields(1)
      (url, 1)
    })

    // 聚合
    val summedUrl = urlAndOne.reduceByKey(_ + _).cache()

    val rdd1 = summedUrl.map(t => {
      val host = new URL(t._1).getHost
      // （学院，（url，次数））
      (host, (t._1, t._2))
    })

    val urls = rdd1.map(_._1).distinct().collect()
    val partitioner = new HostPartitioner(urls)

    // 按照自定义的分区器重新分区
    val partitionedRdd = rdd1.partitionBy(partitioner)

    val result = partitionedRdd.mapPartitions(iter => {
      iter.toList.sortBy(_._2._2).reverse.take(3).iterator
    })

    result.saveAsTextFile("/")

    sc.stop()
  }

}

/**
  * 自定义分区器
  */
class HostPartitioner(urls: Array[String]) extends Partitioner {
  val rules = new mutable.HashMap[String, Int]()
  var index = 0
  for (url <- urls) {
    rules.put(url, index)
    index += 1
  }

  override def numPartitions: Int = urls.length

  override def getPartition(key: Any): Int = {
    val url = key.toString
    rules.getOrElse(url, 0)
  }
}