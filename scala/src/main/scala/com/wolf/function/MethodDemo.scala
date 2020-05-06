package com.wolf.function

object MethodDemo {

  def main(args: Array[String]): Unit = {

  }

  // 定义方法
  def m1(x: Int, y: Int): Int = x * y

  // 定义函数
  val f1 = (x: Int, y: Int) => x + y
  println(f1(1, 2))

  val arr = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  val res1 = arr.map((x: Int) => x * 10) // 函数式编程
  println("arr: " + arr.toBuffer)
  println("res1: " + res1.toBuffer)

  // 函数式编程
  val f2 = (x: Int) => x * x
  arr.map(f2)

}
