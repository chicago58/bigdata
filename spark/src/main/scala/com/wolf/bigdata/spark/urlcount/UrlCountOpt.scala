package com.wolf.bigdata.spark.urlcount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @Description 统计每个学院访问url的前3名 [实现：循环过滤出每个学院的数据，再二次排序（Spark的排序不会出现内存溢出）]
  * @Author wangqikang
  * @Date 2019-07-17 21:05
  */
object UrlCountOpt {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("UrlCountOpt").setMaster("local[2]")
    val sc = new SparkContext(conf)

    // 读取数据
    val lines: RDD[String] = sc.textFile(args(0))

    // 逐行切分数据，并按url计数
    val urlAndOne = lines.map(line => {
      val fields = line.split("\t")
      val url = fields(1)
      (url, 1)
    })

    // 按url聚合，统计每个url访问的次数
    val summedUrl = urlAndOne.reduceByKey(_ + _)

    val urls = Array("http://java.itcast.cn", "http://php.itcast.cn", "http://net.itcast.cn")

    /**
      * 先过滤出每个学院的数据，再调用RDD的排序方法（即使数据量大，也不会溢出，可放入磁盘）
      */
    for (u <- urls) {
      val insRdd = summedUrl.filter(t => {
        val url = t._1
        url.startsWith(u)
      })

      /**
        * 调用RDD的排序算子，内存不会溢出
        * 多次触发Action，会将任务提交多次
        */
      val result = insRdd.sortBy(_._2, false).take(3)
      println(result.toBuffer)
    }

    sc.stop()
  }

}
