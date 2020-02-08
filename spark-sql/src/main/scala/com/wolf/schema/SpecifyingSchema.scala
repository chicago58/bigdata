package com.wolf.schema

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @Description 通过StructType指定Schema
  * @Author wangqikang
  * @Date 2019-07-25 21:42
  */
object SpecifyingSchema {

  def main(args: Array[String]): Unit = {

    // 创建SparkConf()并设置AppName
    val conf = new SparkConf().setAppName("sql-2").setMaster("local[1]")

    // SQLContext依赖SparkContext
    val sc = new SparkContext(conf)

    // 创建SQLContext
    val sqlContext = new SQLContext(sc)

    // 读取数据创建RDD
    val lineRDD = sc.textFile("../bigdata/spark-sql/src/main/resources/person/").map(_.split(" "))

    // 通过StructType指定每个字段的Schema
    val schema = StructType(
      List(
        StructField("id", IntegerType, true),
        StructField("name", StringType, true),
        StructField("age", IntegerType, true)
      )
    )

    // 将RDD映射到RowRDD上
    val rowRDD = lineRDD.map(p => Row(p(0).toInt, p(1), p(2).toInt))

    // 将Schema信息应用到RowRDD上
    val personDataFrame = sqlContext.createDataFrame(rowRDD, schema)

    // 注册临时表
    personDataFrame.registerTempTable("t_person")

    // 执行sql
    val df = sqlContext.sql("select * from t_person order by age desc limit 3")

    // 将结果以JSON的形式写入指定位置
    df.write.json("../bigdata/spark-sql/src/main/resources/output-specifying")

    // 释放资源
    sc.stop()

  }

}
