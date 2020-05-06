package com.wolf.basic

object ConditionExpression {

  def main(args: Array[String]): Unit = {
    val x = 1

    val y = if (x > 0) 1 else -1
    println(y)

    // Scala支持混合类型表达式
    val z = if (x > 1) 1 else "error"
    println(z)

    // else缺失，相当于if (x > 2) 1 else ()
    val m = if (x > 2) 1
    println(m)

    // Scala中每个表达式都有值
    val n = if (x > 2) 1 else ()
    println(n)

    // if 和 else if
    val k = if (x < 0) 0
    else if (x >= 1) 1 else -1
    println(k)

    val k2 = if (x < 0) println(0) else if (x >= 1) println(1) else println(-1)
    println(k2)
  }

}
