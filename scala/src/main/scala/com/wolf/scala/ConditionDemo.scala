package com.wolf.scala

object ConditionDemo {
  def main(args: Array[String]): Unit = {
    val x = 1
    val y = if (x > 0) 1 else -1
    println("y:" + y) // 1

    // scala支持混合类型表达式
    val z = if (x > 1) 1 else "error"
    println("z:" + z) // error

    // else语句缺失，相当于 if (x > 2) 1 else ()
    val m = if (x > 2) 1
    println("m:" + m) // ()

    // scala中每个表达式都有值
    val k1 = if (x < 0) 0 else if (x >= 1) 1 else -1
    println("k1:" + k1) // 1

    val k2 = if (x < 0) println(0) else if (x >= 1) println(1) else println(-1)
    println("k2:" + k2) // ()
  }
}
