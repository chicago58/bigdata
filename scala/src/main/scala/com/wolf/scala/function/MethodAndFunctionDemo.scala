package com.wolf.scala.function

object MethodAndFunctionDemo {
  // 定义一个方法，参数为一个函数
  def m1(f: (Int, Int) => Int): Int = {
    f(2, 6)
  }

  // 定义函数
  val f1 = (x: Int, y: Int) => x + y
  val f2 = (m: Int, n: Int) => m * n

  def main(args: Array[String]): Unit = {
    val r1 = m1(f1)
    println(r1) // 8

    val r2 = m1(f2)
    println(r2) // 12

    // 将方法转换成函数
    val func1 = m1 _
    println(func1)
  }
}
