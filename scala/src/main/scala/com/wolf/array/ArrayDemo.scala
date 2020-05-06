package com.wolf.array

import scala.collection.mutable.ArrayBuffer

/**
  * @Description TODO
  * @Author wangqikang
  * @CreatedAt 2020-03-15 22:45
  **/
object ArrayDemo {

  /**
    * 定长数组，相当于Java的普通数组
    * 变长数组，相当于Java的ArrayList
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {
    // 初始化一个长度为8的定长数组，所有元素均为0
    val arr1 = new Array[Int](8)
    // 打印数组，内容为数组的hashcode值
    println(arr1)
    // 打印数组缓冲，调用toBuffer()方法将数组转换成数组缓冲
    println(arr1.toBuffer)


    // 如果没有使用new，相当于调用了Array类伴生对象的apply()方法为数组赋值
    // 初始化一个长度为1的定长数组，元素为10
    val arr2 = Array[Int](10)
    println(arr2.toBuffer)

    // 定义一个长度为3的定长数组
    val arr3 = Array("hadoop", "storm", "spark")
    // 使用()访问元素
    println(arr3(2))

    // 使用数组缓冲需要导入scala.collection.mutable.ArrayBuffer包
    val ab = ArrayBuffer[Int]()
    // += 在数组缓冲尾部追加元素
    ab += 1
    println("变长数组追加一个元素：" + ab)

    // += 追加多个元素
    ab += (2, 3, 4, 5)
    println("变长数组追加多个元素：" + ab)

    // ++= 追加一个数组
    ab ++= Array(6, 7)
    println("变长数组追加一个数组：" + ab)

    // ++= 追加一个数组缓冲
    ab ++= ArrayBuffer(8, 9)
    println("变长数组追加一个数组缓冲：" + ab)

    // 在数组的某个位置插入元素
    ab.insert(0, -1, 0)
    println("变长数组在指定位置插入元素：" + ab)

    // 在数组的某个位置删除元素
    ab.remove(8, 2)
    println("变长数组在指定位置删除元素：" + ab)

  }

}
