package com.wolf.bigdata.sparksql.schema

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @Description 通过反射推断Schema
  * @Author wangqikang
  * @Date 2019-07-25 21:30
  */
object InferringSchema {

  def main(args: Array[String]): Unit = {
    // 创建SparkConf()并设置AppName
    val conf = new SparkConf().setAppName("sql-1")

    // SQLContext依赖SparkContext
    val sc = new SparkContext(conf)

    // 创建SQLContext
    val sqlContext = new SQLContext(sc)

    // 读取文件创建RDD
    val lineRDD = sc.textFile(args(0)).map(_.split(" "))

    // 关联RDD和case class
    val personRDD = lineRDD.map(x => Person(x(0).toInt, x(1), x(2).toInt))

    // 导入隐式转换，否则无法将RDD转换成DataFrame
    import sqlContext.implicits._
    // 将RDD转换成DataFrame
    val personDF = personRDD.toDF()

    // 注册临时表
    personDF.registerTempTable("t_person")

    // 传入sql
    val df = sqlContext.sql("select * from t_person order by age desc limit 2")

    // 将结果以JSON形式存储到指定位置
    df.write.json(args(1))

    // 释放sc
    sc.stop()

  }

}
