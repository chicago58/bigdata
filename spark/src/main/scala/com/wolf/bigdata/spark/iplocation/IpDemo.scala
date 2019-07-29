package com.wolf.bigdata.spark.iplocation

import java.io.{BufferedReader, FileInputStream, InputStreamReader}

import scala.collection.mutable.ArrayBuffer

/**
  * @Description 计算ip归属地
  * @Author wangqikang
  * @Date 2019-07-22 22:58
  */
object IpDemo {

  /**
    * 将ip转换为Long
    *
    * @param ip
    * @return
    */
  def ipToLong(ip: String): Long = {
    val fragments = ip.split("[.]")
    var ipNum = 0L
    for (i <- 0 until fragments.length) {
      ipNum = fragments(i).toLong | ipNum << 8L
    }
    ipNum
  }

  /**
    * 读取文件内容返回字符数组
    *
    * @param path
    * @return
    */
  def readData(path: String) = {
    val br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))
    var s: String = null
    var flag = true
    val lines = new ArrayBuffer[String]()
    while (flag) {
      s = br.readLine()
      if (s != null) {
        lines += s
      } else {
        flag = false
      }
    }
    lines
  }

  /**
    * 使用二分查找ip归属地
    *
    * @param lines
    * @param ip
    * @return
    */
  def binarySearch(lines: ArrayBuffer[String], ip: Long): Int = {
    var low = 0
    var high = lines.length - 1
    while (low <= high) {
      val mid = (low + high) / 2
      val midSplited = lines(mid).split("\\|")
      if ((ip >= midSplited(2).toLong) && (ip <= midSplited(3).toLong)) {
        return mid
      } else if (ip < midSplited(2).toLong) {
        high = mid - 1
      } else {
        low = mid + 1
      }
    }
    -1
  }

  def main(args: Array[String]): Unit = {
    val ip = "120.55.185.61"
    // 将ip转换成Long
    val ipNum = ipToLong(ip)
    println(ipNum)

    // 读取文件内容保存到字符数组中
    val lines = readData("/Users/wangqikang/Work/code/bigdata/spark/src/main/resources/ip.txt")
    // 二分查找ip归属地
    val index = binarySearch(lines, ipNum)
    print(lines(index))
  }

}
