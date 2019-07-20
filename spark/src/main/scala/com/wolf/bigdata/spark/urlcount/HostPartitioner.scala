package com.wolf.bigdata.spark.urlcount

import org.apache.spark.Partitioner

import scala.collection.mutable

/**
  * @Description 自定义分区器
  * @Author wangqikang
  * @Date 2019-07-20 13:57
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
