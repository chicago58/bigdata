package com.wolf.bigdata.spark.customsort

/**
  * @Description
  * @Author wangqikang
  * @Date 2019-07-20 15:15
  */
/**
  * 方式一：自定义bean对象实现Ordered和Serializable接口
  *
  * @param faceValue 颜值
  * @param age       年龄
  */
//case class Girl(val faceValue: Int, val age: Int) extends Ordered[Girl] with Serializable {
//  override def compare(that: Girl): Int = {
//    if (this.faceValue == that.faceValue) {
//      that.age - this.age
//    } else {
//      this.faceValue - that.faceValue
//    }
//  }
//}

/**
  * 方式二：通过隐式转换实现排序
  * 自定义bean对象（封装排序字段）实现Serializable序列化接口，用于网络中传输数据
  *
  * @param faceValue 颜值
  * @param age       年龄
  */
case class Girl(faceValue: Int, age: Int) extends Serializable
