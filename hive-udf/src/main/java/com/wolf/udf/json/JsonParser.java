package com.wolf.udf.json;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JsonParser extends UDF {

    public String evaluate(String jsonLine) {

        //Hive自带的Jackson解析工具
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            MovieRateBean bean = objectMapper.readValue(jsonLine, MovieRateBean.class);
            return bean.toString();
        } catch (Exception e) {

        }

        return "";
    }

    public static void main(String[] args) throws IOException {

        String str = "{\n" +
                "    \"movie\": \"1193\",\n" +
                "    \"rate\": \"5\",\n" +
                "    \"timestamp\": \"98765433456\",\n" +
                "    \"uid\": \"65432345676543\"\n" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        MovieRateBean movieRateBean = objectMapper.readValue(str, MovieRateBean.class);
        System.out.println(movieRateBean);
    }
    
}
