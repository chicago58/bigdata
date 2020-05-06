package com.wolf

import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

object SocketWordCount2 {

  def main(args: Array[String]): Unit = {
    //获取运行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //获取端口
    val port: Int = try {
      ParameterTool.fromArgs(args).getInt("port")
    } catch {
      case e: Exception => {
        System.err.println("No Port Set, Use Default Port 9000")
      }
        9000
    }

    //连接数据源
    val text = env.socketTextStream("localhost", port, '\n')

    //解析数据，分组，窗口计算，聚合求sum

    import org.apache.flink.api.scala._ //添加隐式转换，否则执行报错

//    val wordCount: DataStream[WordWithCount2] = text.flatMap(line => line.split("\\s")) //切分单词
//      .map(word => WordWithCount2(word, 1)) //单词计数
//      .keyBy("word") //分组
//      .timeWindow(Time.seconds(2), Time.seconds(1)) //指定窗口大小和间隔时间
//      .sum("count")
//    //      .reduce((a: WordWithCount, b: WordWithCount) => WordWithCount(a.word, a.count + b.count))
//
//
//    //打印到控制台，并设置并行度
//    wordCount.print().setParallelism(1)
//
//    //执行任务
//    env.execute("Socket Word Count")
  }
}

