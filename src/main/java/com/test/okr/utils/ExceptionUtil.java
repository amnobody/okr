package com.test.okr.utils;

import java.io.*;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/04/24
 * @description 异常工具类
 */
public class ExceptionUtil {

    /**
     * 获取指定路径下的
     * @param e
     * @return
     */
    public static String getSimpleException(Exception e) {

        BufferedReader reader;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            reader = new BufferedReader(new StringReader(sw.toString()));
            StringBuffer buffer = new StringBuffer(e.getMessage());
            String temp;
            while ((temp = reader.readLine()) != null) {
                if (temp.contains("com.test")) {
                    buffer.append(System.lineSeparator()).append(temp);
                }
            }
            return buffer.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
