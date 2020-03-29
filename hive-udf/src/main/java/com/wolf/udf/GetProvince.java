package com.wolf.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.HashMap;

public class GetProvince extends UDF {
    private static HashMap<String, String> provinceMap = new HashMap<String, String>();

    static {
        provinceMap.put("135", "北京");
        provinceMap.put("136", "上海");
        provinceMap.put("137", "深圳");
        provinceMap.put("138", "广州");
        provinceMap.put("139", "长沙");
    }

    public String evaluate(String phoneNbr) {
        String province = provinceMap.get(phoneNbr.substring(0, 3));
        if (province == null) {
            return "未知";
        } else {
            return province;
        }
    }

}
