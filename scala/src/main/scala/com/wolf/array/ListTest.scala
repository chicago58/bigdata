package com.wolf.array

/**
  * @Description TODO
  * @Author wangqikang
  * @CreatedAt 2020-03-16 21:14
  **/
object ListTest {

  def main(args: Array[String]): Unit = {

    val lst0 = List(1, 7, 9, 8, 0, 3, 5, 4, 6, 2)

    val lst1 = lst0.map(x => x * 2)
    println("lst1:" + lst1)

    val lst2 = lst0.filter(x => x % 2 == 0)
    println("lst2:" + lst2)

    val lst3 = lst0.sorted
    println("lst3:" + lst3)

    val lst4 = lst0.sortBy(x => x)
    println("lst4:" + lst4)

    val lst5 = lst0.sortWith((x, y) => x < y)
    println("lst5:" + lst5)

    val lst6 = lst3.reverse
    println("lst6:" + lst6)

    val iterator = lst0.grouped(4)
    val lst7 = iterator.toList
    println("lst7:" + lst7)
    val lst8 = lst7.flatten
    println("lst8:" + lst8)

    val lst9 = lst0.reduce(_ + _) // 按特定顺序操作，底层调用reduceLeft()
    println("lst9:" + lst9)
    val lst10 = lst0.fold(10)(_ + _) // 按特定顺序操作，有初始值，底层调用foldLeft()
    println("lst10:" + lst10)
    val lst11 = lst0.par.fold(10)(_ + _) // 非特定顺序
    println("lst11:" + lst11)

    val sum = lst0.par.sum
    println("sum:" + sum)
    val res1 = lst0.par.filter(_ % 2 == 0) // par 转换为并行化集合，由多线程并行处理，非特定顺序
    println("res1:" + res1.toBuffer)
    val res2 = lst0.par.reduce((x, y) => x + y)
    println("res2:" + res2)

    val arr = Array("a b c", "d e f", "h i j")
    val res = arr.flatMap(_.split(" "))
    println("res:" + res.toBuffer)
  }

}
