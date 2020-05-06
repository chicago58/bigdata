package com.wolf;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class SocketWordCount1 {
    private static String hostname = "localhost";
    private static char delimiter = '\n';


    public static void main(String[] args) throws Exception {
        //获取Flink运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //获取端口号（参数指定）
        int port;
        try {
            ParameterTool parameterTool = ParameterTool.fromArgs(args);
            port = parameterTool.getInt("port");
        } catch (Exception e) {
            System.err.println("No Port Set, Use Default Port 9000.");
            port = 9000;//设置默认端口号
        }

        //连接Socket数据源，获取输入的数据
        DataStreamSource<String> text = env.socketTextStream(hostname, port, delimiter);

        //汇总计算
        SingleOutputStreamOperator<WordWithCount1> wordCount = text.flatMap(new FlatMapFunction<String, WordWithCount1>() {
            public void flatMap(String line, Collector<WordWithCount1> collector) throws Exception {
                String[] words = line.split("\\s");
                for (String word : words) {
                    collector.collect(new WordWithCount1(word, 1L));
                }
            }
        }).keyBy("word") //分组
                .timeWindow(Time.seconds(2), Time.seconds(1)) //指定时间窗口大小为2秒，时间间隔为1秒
                .sum("count"); //聚合，可使用reduce或sum聚合
//                .reduce(new ReduceFunction<WordWithCount>() {
//                    public WordWithCount reduce(WordWithCount a, WordWithCount b) throws Exception {
//                        return new WordWithCount(a.word, a.count + b.count);
//                    }
//                });

        //打印到控制台，并设置并行度
        wordCount.print().setParallelism(1);

        env.execute("Socket Word Count");
    }
}
/**
 * 1、获取运行环境
 * 2、获取数据源
 * 3、算子操作
 * 4、存储数据或打印
 * 5、触发任务执行
 */
