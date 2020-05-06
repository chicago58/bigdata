package com.wolf.basic

object For {

  def main(args: Array[String]): Unit = {
    // for(i <- 表达式)
    // 1 to 10返回Range区间
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

    // 高级for循环（循环时指定条件）
    // 每个生成器都可以带一个条件，if前面没有分号
    for (i <- 1 to 3; j <- 1 to 3 if i != j) {
      print((10 * i + j) + " ")
    }
    println()

    // for推导式：若循环体以yield开始，则会构建一个集合，每次迭代生成集合中的一个元素
    val v = for (i <- 1 to 10) yield i * 10
    println(v)

    // 函数式编程
    println(1.to(10).map(_ * 2))

    // 获取数组索引
    val arrIndex = Array("aa", "bb", "cc", "dd")
    for (i <- arrIndex.indices) { // indices <=> 0 until length
      println(i + " " + arrIndex(i))
    }
  }

}
