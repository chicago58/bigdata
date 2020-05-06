package com.wolf.clazz

/**
  * @Description TODO
  * @Author wangqikang
  * @CreatedAt 2020-03-17 07:43
  **/
object SessionFactory {

  def apply(): Unit = {
    println(123)
  }

  def apply(name: String): Unit = {
    println(name)
  }

  def main(args: Array[String]): Unit = {
    SessionFactory("tom")

    val arr = Array(1, 2, 3, 4, 5)
    println(arr.toBuffer)

    val arr1 = new Array(6)
    println(arr1.toBuffer)

  }

}
