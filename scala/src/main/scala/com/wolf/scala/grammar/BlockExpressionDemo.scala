package com.wolf.scala.grammar

object BlockExpressionDemo {
  def main(args: Array[String]): Unit = {
    val x = 0

    // scala的{}中可包含一系列表达式，最后一个表达式的值即是块的值
    val res = {
      if (x < 0) {
        -1
      } else if (x >= 1) {
        1
      } else {
        "error"
      }
    }

    println("res:" + res) // error
  }
}
