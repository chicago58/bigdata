package com.wolf.bigdata.spark.mobilelocation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @Description 基站下停留时间最长的手机号（降序）
  * @Author wangqikang
  * @Date 2019-07-16 7:30
  */
object MobileLocation {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("MobileLocation").setMaster("local[2]")
    val sc = new SparkContext(conf)
    // 手机号，时间，基站ID，类型
    val lines: RDD[String] = sc.textFile(args(0))
    // 切分
    //    lines.map(_.split(",")).map(arr => (arr(0), arr(1).toLong, arr(2), arr(3)))
    val splited = lines.map(line => {
      val fields = line.split(",")
      // 手机号
      val mobile = fields(0)
      // 基站ID
      val location = fields(2)
      // 类型
      val tp = fields(3)
      // 手机号进出基站的时间
      val time = if (tp == "1") -fields(1).toLong else fields(1).toLong
      // 拼接数据
      ((mobile, location), time)
    })
    // 分组聚合（手机号在基站下的停留时长）
    val reduced: RDD[((String, String), Long)] = splited.reduceByKey(_ + _)
    val locationMobileTime = reduced.map(x => {
      // 基站ID，手机号，停留时长
      (x._1._2, (x._1._1, x._2))
    })

    // 基站ID，x坐标，y坐标
    val locationInfo: RDD[String] = sc.textFile(args(1))
    // 整理基站数据
    val splitedLocationInfo = locationInfo.map(line => {
      val fields = line.split(",")
      val id = fields(0)
      val x = fields(1)
      val y = fields(2)
      (id, (x, y))
    })
    // join连接（基站ID，（（手机号，停留时长）,（x坐标，y坐标）））
    val joined: RDD[(String, ((String, Long), (String, String)))] = locationMobileTime.join(splitedLocationInfo)
    // 根据基站分组
    val groupedByMobile = joined.groupBy(_._1)
    println(groupedByMobile.collect().toBuffer)
    // 根据停留时长降序排序
    val result = groupedByMobile.mapValues(_.toList.sortBy(_._2._1._2).reverse.take(2))
    println(result.collect().toBuffer)
//    println(joined.collect().toBuffer)
    sc.stop()
  }
}
