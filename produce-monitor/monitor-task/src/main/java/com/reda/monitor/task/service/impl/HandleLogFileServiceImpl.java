package com.reda.monitor.task.service.impl;

import com.reda.monitor.task.config.ParseMappingConfig;
import com.reda.monitor.task.entity.MonitInformation;
import com.reda.monitor.task.mapper.MonitorMapper;
import com.reda.monitor.task.service.HandleLogFileService;
import com.reda.monitor.task.utils.PropertiesUtil;
import com.reda.monitor.task.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCommands;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.reda.monitor.task.utils.FileUtil.getTotalLines;
import static com.reda.monitor.task.utils.FileUtil.readLineVarFile;

/**
 * @author : Zhuang Jialong
 * @description : 解析日志文件
 * @date : 2020/9/30 下午 2:29
 * @Copyright: Copyright(c)2019 RedaFlight.com All Rights Reserved
 */
@Service
public class HandleLogFileServiceImpl implements HandleLogFileService {
    private static Logger logger = LoggerFactory.getLogger(HandleLogFileServiceImpl.class);

    private static final ConcurrentHashMap<String, String> logFilesMap = new ConcurrentHashMap<>();


    private static ConcurrentHashMap<String, Integer> fileLineNumMapping = new ConcurrentHashMap<>();
    private static JedisCommands jedisCommands;
    private static String netWorkIp;
    @Autowired
    private ParseMappingConfig parseMappingConfig;
    @Value("${file.local.path}")
    private String filePath;
    @Autowired
    private MonitorMapper monitorMapper;

    @Override
    public void parseLogFiles() {
        if (logFilesMap.size() == 0) {
            initConfiguration();
        }
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<MonitInformation> monitorInformations = new ArrayList<>();
        logFilesMap.keySet().stream().forEach(logType -> {
            String[] split = logFilesMap.get(logType).split(";");
            Arrays.asList(logFilesMap.get(logType).split(";")).stream().forEach(fileName -> {
//                String fileContent = readFilesByLine(filePath + netWorkIp  + "/" + "data" + "/" + today + "/" + fileName, fileName,today);
                // windows本地调试分割符 \\
                                String fileContent = readFilesByLine(filePath + netWorkIp  + "\\" + "data" + "\\" + today + "\\" + fileName, fileName,today);
                if (fileContent != null && fileContent != "") {
                    List<MonitInformation> entities = parseLogs(fileContent, logType);
                    monitorInformations.addAll(entities);
                    System.out.println("当前文件" + fileName + ",解析出实体个数" + entities.size());
                }
            });
        });
        if (monitorInformations.size() > 0) {

            monitorMapper.insert(monitorInformations);
        }else {
            logger.debug("未读出文件内容！");
        }
    }

    /**
     * 按行读取日志文件
     * @param filePath 文件全限定路径
     * @param fileName 文件名
     * @param date     当前日期
     * @return
     */
    private String readFilesByLine(String filePath, String fileName,String date) {
        StringBuilder stringBuilder = new StringBuilder();
        String fileKey = fileName + date;
        //判断是否存在当前日期的文件，如果不存在设置初始行数为1，给24小时+超时时间
        if(!jedisCommands.exists(fileKey)){
            jedisCommands.setex(fileKey,87400,"1");
        }

        Integer lineNum = Integer.valueOf(jedisCommands.get(fileName + date));

        try {
            Integer totalLine = getTotalLines(filePath);
            if (lineNum > totalLine) {
                 throw new RuntimeException("文件未新增。");
            }
            if (lineNum == 1) {
                for (int i = lineNum; i < totalLine + 1; i++) {
                    stringBuilder.append(readLineVarFile(filePath, i) + "\n");
                }
            } else {
                for (int i = lineNum; i < totalLine + 1; i++) {
                    stringBuilder.append(readLineVarFile(filePath, i) + "\n");
                }
            }
            Integer restTime = Integer.valueOf(String.valueOf(jedisCommands.ttl(fileKey)));
            jedisCommands.setex(fileKey,restTime,String.valueOf(totalLine+1));
            return stringBuilder.toString();
        } catch (Exception e) {
            logger.info("未读到文件内容，异常原因：" + e.getMessage());
            return null;
        }
    }

    /**
     * 解析读到的文件内容
     *
     * @param fileContent 文件内容
     * @param parseType   解析类型
     */
    private List<MonitInformation> parseLogs(String fileContent, String parseType) {
        if (fileContent == null || fileContent == "") {
            logger.info("file no content");
            return null;
        }
        String[] split = fileContent.split("\n");
        List<MonitInformation> monitInformationList = new ArrayList<>();
        Stream<String> stringStream = Arrays.asList(split).stream().map(lineContent -> lineContent.replaceAll(" +", "")).filter(lineContent -> lineContent != null || lineContent != "");

        Arrays.asList(split).stream().map(lineContent -> lineContent.replaceAll(" +", "")).filter(lineContent -> lineContent != null || lineContent != "").forEach(
                lineContent -> {
                    HashMap<String, String> fieldValue = new HashMap<>();
                    Arrays.asList(lineContent.split(",")).stream().forEach(f ->
                            fieldValue.put(f.substring(0, f.indexOf("=")), f.endsWith("=") == true ? "" : f.substring(f.indexOf("=") + 1))
                    );
                    MonitInformation monitInformation = new MonitInformation();
                    Arrays.asList(monitInformation.getClass().getDeclaredFields()).stream().forEach(field -> {
                        field.setAccessible(true);
                        String fieldName = field.getName();
                        String key = jedisCommands.get(parseType + "." + fieldName);

                        try {
                            if (field.getType() == (Date.class)) {
                                Date parse = new SimpleDateFormat("yyyy/MM/ddhh:mm:ss").parse(fieldValue.get(key));
                                field.set(monitInformation, parse);
                            } else {
                                field.set(monitInformation, fieldValue.get(key));
                            }
                        } catch (IllegalAccessException | ParseException e) {
                            logger.error("package field to entity failed ,field name is " + field.getName() + " , and parseType is " + parseType);
                        }
                    });
                    monitInformationList.add(monitInformation);
                });
        return monitInformationList;
    }

    /**
     * 初始化各种不同类型日志字段对应关系
     */
    private void initConfiguration() {
        jedisCommands = RedisUtils.getRedisCommands();
        //初始化文件名及日志类型
        Map<String, String> fileName2Type = PropertiesUtil.getProperty("application.properties", "localIp");
        try {
            netWorkIp = PropertiesUtil.getNetWorkIp();
            logger.info("current network ip is : "+netWorkIp);
            StringBuilder stringBuilder = new StringBuilder();
            //拼接日志文件名
            // service_172.31.0.1_nginx.log
            fileName2Type.keySet().stream()
                    .map(key -> fileName2Type.get(key) + ":" + key)
                    .map(key -> key.replaceAll("localIp.", netWorkIp + "_"))
                    .map(key -> key + ".log").collect(Collectors.toList())
                    .forEach(element -> {
                        String key = element.split(":")[0];
                        String value = element.split(":")[1];
                        if (logFilesMap.containsKey(key)) {
                            String originalValue = logFilesMap.get(key);
                            logFilesMap.put(key, originalValue + ";" + value);
                        } else {
                            logFilesMap.put(key, value);
                        }
                    });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        Environment parseEnvironment = parseMappingConfig.getEnvironment();
        //初始化日志文件及字段配置
        MonitInformation monitInformation = new MonitInformation();
        Field[] fields = monitInformation.getClass().getDeclaredFields();
        logFilesMap.keySet().forEach(parseType -> {
            Arrays.asList(fields).stream().forEach(field -> {
                field.setAccessible(true);
                String propertyValue = parseEnvironment.getProperty(parseType + "." + field.getName());
                jedisCommands.set(parseType + "." + field.getName(), propertyValue);
            });
        });
    }
}
