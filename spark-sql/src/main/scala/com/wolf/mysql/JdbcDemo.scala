
package com.wolf.mysql

import java.util.Properties

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @Description
  * @Author wangqikang
  * @Date 2019-07-26 07:00
  */
object JdbcDemo {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("MySQL").setMaster("local[1]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    // 并行化方式创建RDD
    val personRDD = sc.parallelize(Array("1 tom 5", "2 jerry 3", "3 kitty 6")).map(_.split(" "))

    // 通过StructType指定表字段Schema
    val schema = StructType(
      List(
        StructField("id", IntegerType, true),
        StructField("name", StringType, true),
        StructField("age", IntegerType, true)
      )
    )

    // 将RDD映射到RowRDD
    val rowRDD = personRDD.map(p => Row(p(0).toInt, p(1).trim, p(2).toInt))

    // 将Schema信息应用到RowRDD上
    val personDataFrame = sqlContext.createDataFrame(rowRDD, schema)

    // 创建properties存储数据库相关属性
    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "root")

    // 将数据追加到数据库中
    personDataFrame.write.mode("append").jdbc("jdbc:mysql://127.0.0.1:3306/bigdata", "bigdata.person", prop)

    // 释放sc
    sc.stop()

  }

}
