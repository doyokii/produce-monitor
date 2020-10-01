package com.reda.monitor.task.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author : Zhuang Jialong
 * @description :
 * @date : 2020/9/16 下午 5:58
 * @Copyright: Copyright(c)2019 RedaFlight.com All Rights Reserved
 */
public class PropertiesUtil {
    /**
     * 获取配置
     * @param propertyName 配置文件名
     * @param keyChar   需要的关系字
     */


    /**
     * 获取配置
     *
     * @param propertyName
     * @param keyChar
     * @return
     */
    public static Map<String, String> getProperty(String propertyName, String keyChar) {
        InputStream inputStream = null;
        HashMap<String, String> propertiesMap = new HashMap<>();
        try {
            inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertyName);
            Properties properties = new Properties();
//            Map<String, String> concernElements = new HashMap<>(8);
            properties.load(inputStream);
            properties.stringPropertyNames()
                    .stream()
                    .filter(key -> key.startsWith(keyChar))
                    .collect(Collectors.toList()).
                    forEach(propertiesKey -> {
                        propertiesMap.put(propertiesKey, properties.getProperty(propertiesKey));
                    });
            return propertiesMap;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据网卡获得IP地址
     *
     * @return
     * @throws SocketException
     */
    public static String getNetWorkIp() throws SocketException {
        String ip = "";
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            String name = intf.getName();
            if (!name.contains("docker") && !name.contains("lo")) {
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    //获得IP
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipaddress = inetAddress.getHostAddress().toString();
                        if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {

                            if (!"127.0.0.1".equals(ip)) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        }
        return ip;
    }
}
