package com.wolf.basic

object Variable {

  def main(args: Array[String]): Unit = {
    // val定义变量，变量值不可变（相当于Java中final修饰变量）
    val i = 1
    println(i)

    // var定义变量，变量值可变（推荐使用val）
    var s = "hello"
    println(s)

    // Scala编译器会自动推断变量类型
    val str: String = "scala"
    println(str)
  }

}
