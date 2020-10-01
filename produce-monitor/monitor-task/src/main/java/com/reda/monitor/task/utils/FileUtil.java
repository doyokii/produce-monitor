package com.reda.monitor.task.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author : Zhuang Jialong
 * @description : 文件处理工具类
 * @date : 2020/9/14 17:13
 * @Copyright: Copyright(c)2019 RedaFlight.com All Rights Reserved
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 输出本行内容及字符数
     * @param fileName
     * @param lineNumber
     * @return
     * @throws IOException
     */
    public static String readLineVarFile(String fileName, int lineNumber) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        String line = reader.readLine();
        if (lineNumber <= 0 || lineNumber > getTotalLines(fileName)) {
            System.out.println("不在文件的行数范围之内。");
        }
        int num = 0;
        while (line != null && line!= "") {
            if (lineNumber == ++num) {
                logger.debug("第" + lineNumber + "行: " + line + "     字符数为：" + line.length());
                return line;
            }
            line = reader.readLine();
        }
        reader.close();
        return null;
    }

    /**
     * 文件内容的总行数
     * @param fileName
     * @return
     * @throws IOException
     */
    public static int getTotalLines(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        LineNumberReader reader = new LineNumberReader(br);
        String s = reader.readLine();
        int lines = 0;
        while (s != null) {
            lines++;
            s = reader.readLine();
        }
        reader.close();
        br.close();
        return lines;
    }
}
