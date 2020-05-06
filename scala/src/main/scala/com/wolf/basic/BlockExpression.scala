package com.wolf.basic

object BlockExpression {

  def main(args: Array[String]): Unit = {
    val x = 0

    // Scala的块中可包含一系列表达式，最后一个表达式的值即是块的值
    val result = {
      if (x < 0) {
        -1
      } else if (x >= 1) {
        1
      } else {
        "error"
      }
    }

    println(result)
  }

}
