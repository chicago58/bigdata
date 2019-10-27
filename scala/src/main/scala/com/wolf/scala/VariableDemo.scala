package com.wolf.scala

object VariableDemo {
  def main(args: Array[String]): Unit = {
    // val定义变量，变量值不可变
    val i = 1
    //    i = 2 // 错误
    println("i: " + i)

    // var定义变量，变量值可变
    var j = "hello"
    j = "world"
    println("j: " + j)

    // scala编译器会自动推断变量类型
    val str: String = "hello world"
    println("str: " + str)
  }
}
