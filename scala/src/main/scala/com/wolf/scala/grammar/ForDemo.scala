package com.wolf.scala.grammar

object ForDemo {
  def main(args: Array[String]): Unit = {
    // for(i <- 表达式)
    for (i <- 1 to 10) {
      print(i + " ")
    }
    println()

    // for(i <- 数组)
    val arr = Array("a", "b", "c")
    for (i <- arr) {
      print(i + " ")
    }
    println()

    // 高级for循环，每个生成器表达式都可以带一个条件，if前面没有分号
    for (i <- 1 to 3; j <- 1 to 3 if i != j) {
      print((10 * i + j) + " ")
    }
    println()

    // for推导式，如果循环体以yield开始，则会构建一个集合，每次迭代生成集合中的一个值
    val v = for (i <- 1 to 10) yield i * 10
    println("v:" + v)
  }
}
