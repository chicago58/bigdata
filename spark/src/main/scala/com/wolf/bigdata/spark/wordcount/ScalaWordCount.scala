package com.wolf.bigdata.spark.wordcount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @Description scala实现wordcount功能，并提交到集群上运行
  * @Author wangqikang
  * @Date 2019-07-15 23:04
  */
object ScalaWordCount {

  /**
    * main() 作为程序执行的入口
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {
    // 创建 SparkConf 并设置 App 名称
    val conf = new SparkConf().setAppName("WordCount").setMaster("local")
    // 创建 SparkContext，该对象是通向集群的入口
    val sc = new SparkContext(conf)
    // 读取 hdfs 中的数据
    val lines: RDD[String] = sc.textFile(args(0))
    // 切分单词
    val words: RDD[String] = lines.flatMap(_.split(" "))
    // 将单词计数
    val wordAndOne: RDD[(String, Int)] = words.map((_, 1))
    // reduceByKey 分组聚合，在每个分区上先进行聚合，再进行全局聚合
    val result: RDD[(String, Int)] = wordAndOne.reduceByKey((x, y) => x + y)
    // 排序
    val finalResult: RDD[(String, Int)] = result.sortBy(_._2, false)
    // 将数据存储到 hdfs 中
    finalResult.saveAsTextFile(args(1))
    // 释放资源
    sc.stop()
  }

}
