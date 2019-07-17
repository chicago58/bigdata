package com.wolf.bigdata.spark.urlcount

import java.net.URL

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @Description 统计每个学院访问url最多的前3个
  * @Author wangqikang
  * @Date 2019-07-17 08:37
  */
object UrlCount {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("UrlCount").setMaster("local[2]")
    val sc = new SparkContext(conf)

    // 一行一行读取数据
    val lines: RDD[String] = sc.textFile(args(0))

    // split
    val urlAndOne = lines.map(line => {
      val fields = line.split("\t")
      val url = fields(1)
      (url, 1)
    })

    // 聚合
    val summedUrl = urlAndOne.reduceByKey(_ + _)
    // 打印每个url访问了多少次
    //    println(summedUrl.collect().toBuffer)

    summedUrl.cache()

    val grouped = summedUrl.map(t => {
      // 获取主域名
      val host = new URL(t._1).getHost
      (host, t._1, t._2)
    }).groupBy(_._1)

    /**
      * 调用scala中list的排序算子，当数据量非常大时存在内存溢出的隐患
      */
    val result = grouped.mapValues(_.toList.sortBy(_._3).reverse.take(3))
    println(result.collect().toBuffer)

    sc.stop()
  }

}
