package com.wolf.bigdata.spark.iplocation

import java.sql.{Connection, Date, DriverManager, PreparedStatement}

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @Description 广播变量实现统计ip归属地
  * @Author wangqikang
  * @Date 2019-07-29 22:37
  */
object IpLocation {

  /**
    * 将数据写入MySQL数据库
    */
  val dataToMySQL = (iterator: Iterator[(String, Int)]) => {
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sql = "insert into location_info (location, counts, access_date) values (?, ?, ?)"

    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata", "root", "1qaz@WSX")
      iterator.foreach(line => {
        ps = conn.prepareStatement(sql)
        ps.setString(1, line._1)
        ps.setInt(2, line._2)
        ps.setDate(3, new Date(System.currentTimeMillis()))
        ps.executeUpdate()
      })
    } catch {
      case e: Exception => println(e.printStackTrace())
    } finally {
      if (ps != null) {
        ps.close()
      }
      if (conn != null) {
        conn.close()
      }
    }
  }

  /**
    * 二分查找定位ip归属地
    *
    * @param lines
    * @param ip
    * @return
    */
  def binarySearch(lines: Array[(String, String, String)], ip: Long): Int = {
    var low = 0
    var high = lines.length - 1
    while (low <= high) {
      val mid = (low + high) / 2
      if ((ip >= lines(mid)._1.toLong) && (ip <= lines(mid)._2.toLong)) {
        return mid
      } else if (ip < lines(mid)._1.toLong) {
        high = mid - 1
      } else {
        low = mid + 1
      }
    }
    -1
  }

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

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("IpLocation")
    val sc = new SparkContext(conf)

    val ipRuleRdd = sc.textFile("/Users/wangqikang/Work/code/bigdata/spark/src/main/resources/ip.txt")
      .map(line => {
        val fields = line.split("\\|")
        val startNum = fields(2)
        val endNum = fields(3)
        val province = fields(6)
        (startNum, endNum, province)
      })

    // 全部ip映射规则保存到hdfs或其他文件系统
    val ipRuleArray = ipRuleRdd.collect()

    // 将变量广播到当前任务的Executor中
    val ipRuleBroadcast = sc.broadcast(ipRuleArray)

    // 加载要处理的数据
    val ipsRdd = sc.textFile("/Users/wangqikang/Work/code/bigdata/spark/src/main/resources/access.log")
      .map(line => {
        val fields = line.split("\\|")
        fields(1)
      })

    val result = ipsRdd.map(ip => {
      val ipNum = ipToLong(ip)
      val index = binarySearch(ipRuleBroadcast.value, ipNum)
      val info = ipRuleBroadcast.value(index)
      info
    })
      .map(t => (t._3, 1))
      .reduceByKey(_ + _)

    // 向MySQL写入数据
    result.foreachPartition(dataToMySQL(_))

    // 释放资源
    sc.stop()
  }

}
