package com.wolf.array

/**
  * @Description TODO
  * @Author wangqikang
  * @CreatedAt 2020-03-15 22:58
  **/
object ForArrayDemo {

  def main(args: Array[String]): Unit = {
    // 初始化一个数组
    val arr = Array(1, 2, 3, 4, 5, 6, 7, 8)

    // 增强for循环
    for (i <- arr)
      print(i + " ")
    println()

    for (i <- arr.indices.reverse) // indices底层是 0 until length
      print(arr(i) + " ")
    println()
  }

}
