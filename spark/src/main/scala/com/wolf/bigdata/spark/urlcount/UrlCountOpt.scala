package com.wolf.bigdata.spark.urlcount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @Description
  * @Author wangqikang
  * @Date 2019-07-17 21:05
  */
object UrlCountOpt {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("UrlCountOpt").setMaster("local[2]")
    val sc = new SparkContext(conf)

    val lines: RDD[String] = sc.textFile(args(0))

    // split
    val urlAndOne = lines.map(line => {
      val fields = line.split("\t")
      val url = fields(1)
      (url, 1)
    })

    // 聚合
    val summedUrl = urlAndOne.reduceByKey(_ + _)

    val urls = Array("http://java.itcast.cn", "http://php.itcast.cn", "http://net.itcast.cn")

    // 循环过滤
    for (u <- urls) {
      val insRdd = summedUrl.filter(t => {
        val url = t._1
        url.startsWith(u)
      })

      /**
        * 调用RDD的排序算子，不会内存溢出
        */
      val result = insRdd.sortBy(_._2, false).take(3)
      println(result.toBuffer)
    }

    sc.stop()

  }

}
