package com.wolf.bigdata.spark.wordcount;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;

/**
 * @Description java实现wordcount功能，并提交到集群运行
 * @Author wangqikang
 * @Date 2019-07-15 23:14
 */
public class JavaWordCount {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("JavaWordCount").setMaster("local");
        // 创建 java spark context
        JavaSparkContext jsc = new JavaSparkContext(conf);
        // 读取数据
        JavaRDD<String> lines = jsc.textFile(args[0]);
        // 切分
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterable<String> call(String line) throws Exception {
                return Arrays.asList(line.split(" "));
            }
        });
        // 单词计数
        JavaPairRDD<String, Integer> wordAndOne = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String word) throws Exception {
                return new Tuple2<String, Integer>(word, 1);
            }
        });
        // 分组聚合
        JavaPairRDD<String, Integer> result = wordAndOne.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
        // 反转
        JavaPairRDD<Integer, String> swappedPair = result.mapToPair(new PairFunction<Tuple2<String, Integer>, Integer, String>() {
            @Override
            public Tuple2<Integer, String> call(Tuple2<String, Integer> tuple2) throws Exception {
                return new Tuple2<Integer, String>(tuple2._2, tuple2._1);
            }
        });
        // 排序并反转（只能按key排序）
        JavaPairRDD<String, Integer> finalResult = swappedPair.sortByKey(false).mapToPair(new PairFunction<Tuple2<Integer, String>, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(Tuple2<Integer, String> tuple2) throws Exception {
                // 交换 Tuple2 中元素的顺序
                return tuple2.swap();
            }
        });
        // 保存
        finalResult.saveAsTextFile(args[1]);
        // 释放资源
        jsc.stop();
    }
}
