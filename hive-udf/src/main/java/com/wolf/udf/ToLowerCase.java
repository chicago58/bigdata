package com.wolf.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class ToLowerCase extends UDF {

    public String evaluate(String... inStr) {
        StringBuffer sb = new StringBuffer();
        for (String str : inStr) {
            sb.append(str.toLowerCase());
        }

        return sb.toString();
    }

    public int evaluate(int a, int b) {
        return a + b;
    }

    public String evaluate(String inStr) {
        if (inStr == null) {
            return null;
        }
        return inStr;
    }
}
