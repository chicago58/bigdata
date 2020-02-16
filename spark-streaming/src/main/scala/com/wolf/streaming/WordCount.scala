package com.wolf.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("StreamingWordCount").setMaster("local[2]")

    // 创建StreamingContext并设置产生批次的间隔时间
    val ssc = new StreamingContext(conf, Seconds(5))

    // 从Socket端口创建RDD
    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("127.0.0.1", 8888);

    val words: DStream[String] = lines.flatMap(_.split(" "))

    val wordAndOne: DStream[(String, Int)] = words.map((_, 1))

    val result: DStream[(String, Int)] = wordAndOne.reduceByKey(_ + _)

    // 打印
    result.print()

    // 开启程序
    ssc.start()

    // 等待程序结束
    ssc.awaitTermination()

  }
}
