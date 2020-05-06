package com.wolf.array

/**
  * @Description TODO
  * @Author wangqikang
  * @CreatedAt 2020-03-15 23:02
  **/
object YieldArrayDemo {

  def main(args: Array[String]): Unit = {
    // 定义一个数组
    val arr = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)

    // 将偶数取出乘以10后生成新的数组
    val res = for (e <- arr if e % 2 == 0)
      yield e * 10
    println(res.toBuffer)

    // filter实现
    val r = arr.filter(_ % 2 == 0).map(_ * 10)
    println(r.toBuffer)
  }

}
