package com.wolf.logenhance;

import java.sql.*;
import java.util.HashMap;

public class DBLoader {
    public static void loadDB(HashMap<String, String> ruleMap) {
        Connection conn = null;
        Statement st = null;
        ResultSet res = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://");
            st = conn.createStatement();
            res = st.executeQuery("select url, content from urlcontent");
            while (res.next()) {
                ruleMap.put(res.getString(1), res.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
                if (st != null) {
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
