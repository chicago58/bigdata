package com.wolf.bigdata.spark.customsort

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @Description spark实现自定义排序
  * @Author wangqikang
  * @Date 2019-07-18 08:48
  */
object CustomSort {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("CustomSort").setMaster("local[2]")
    val sc = new SparkContext(conf)

    val rdd1 = sc.parallelize(List(("yuihatano", 90, 28, 1), ("abgelababy", 90, 27, 2), ("jujingyi", 95, 22, 3)))
    // 方式二：使用隐式转换
    import OrderContext._
    // 方式一：自定义对象实现Ordered和Serializable接口来实现排序
    val rdd2 = rdd1.sortBy(x => Girl(x._2, x._3), false)
    println(rdd2.collect().toBuffer)

    sc.stop()
  }

}
