package com.wolf.bigdata.spark.customsort

/**
  * @Description 自定义bean对象封装排序字段 [case class可以封装类，还不用new]
  * @Author wangqikang
  * @Date 2019-07-20 15:15
  */
/**
  * 方式一：继承Ordered、实现Serializable序列化接口
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
  * 方式二：实现Serializable序列化接口，用于网络传输数据 [隐式转换实现排序]
  *
  * @param faceValue 颜值
  * @param age       年龄
  */
case class Girl(faceValue: Int, age: Int) extends Serializable
