package com.wolf.bigdata.spark.urlcount

import java.net.URL

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @Description 统计每个学院访问url的前3名 [实现：根据域名分组后二次排序（scala list中的排序方法可能导致内存溢出）]
  * @Author wangqikang
  * @Date 2019-07-17 08:37
  */
object UrlCount {

  def main(args: Array[String]): Unit = {
    /**
      * local:表示一个线程
      * local[2]:表示两个线程
      * local[*]:表示利用CPU的所有线程
      */
    val conf = new SparkConf().setAppName("UrlCount").setMaster("local[2]")
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
    // 打印每个url访问了多少次
    //    println(summedUrl.collect().toBuffer)

    // 将聚合后的数据缓存到内存中
    summedUrl.cache()

    // 拼接数据(域名,url,访问次数)，并按域名分组
    val grouped = summedUrl.map(t => {
      // 获取域名
      val host = new URL(t._1).getHost
      (host, t._1, t._2)
    }).groupBy(_._1)
    println(grouped.collect().toBuffer)

    /**
      * 按域名分组后二次排序：调用scala中list的排序方法
      * 缺陷：当数据量非常大时可能内存溢出
      * 分析：list集合排序必须将所有数据加载到内存。当数据量很大时，有内存溢出的隐患。
      *
      * mapValues:取出分组后的集合
      */
    val result = grouped.mapValues(_.toList.sortBy(_._3).reverse.take(3))
    println(result.collect().toBuffer)

    sc.stop()
  }

}
