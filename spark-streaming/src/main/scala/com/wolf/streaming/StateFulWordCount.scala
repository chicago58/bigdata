package com.wolf.streaming

import org.apache.spark.{HashPartitioner, SparkConf}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

object StateFulWordCount {

  /**
    * String：单词 hello
    * Seq[Int]：单词在当前批次出现的次数
    * Option[Int]：历史结果
    */
  val updateFunc = (iter: Iterator[(String, Seq[Int], Option[Int])]) => {
    //    iter.map(t => (t._1, t._2.sum + t._3.getOrElse(0)))

    // 模式匹配
    iter.map { case (x, y, z) => (x, y.sum + z.getOrElse(0)) }
  }


  def main(args: Array[String]): Unit = {
    LoggerLevels.setStreamingLogLevels()

    val conf = new SparkConf().setAppName("StateFulStreamingWordCount").setMaster("local[2]")

    // 创建StreamingContext并设置产生批次的间隔时间
    val ssc = new StreamingContext(conf, Seconds(5))
    // 设置CheckPoint目录
    ssc.checkpoint("/Users/wangqikang/wordcount")

    // 从Socket端口创建RDD
    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("127.0.0.1", 8888);

    val words: DStream[String] = lines.flatMap(_.split(" "))

    val wordAndOne: DStream[(String, Int)] = words.map((_, 1))

    // updateStateByKey()可以累加，但是需要传入自定义累加函数
    val result: DStream[(String, Int)] = wordAndOne.updateStateByKey(updateFunc, new HashPartitioner(ssc.sparkContext.defaultParallelism), true)

    // 打印
    result.print()

    // 开启程序
    ssc.start()

    // 等待程序结束
    ssc.awaitTermination()
  }
}
