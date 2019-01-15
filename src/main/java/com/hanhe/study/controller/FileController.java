package com.hanhe.study.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Log4j2
@Controller
@RequestMapping("file")
public class FileController {
    Set<String> set = new HashSet<>();
    Set<String> readSet = new HashSet<>();
    Set<String> csvSet = new HashSet<>();

    @ResponseBody
    @GetMapping("parse")
    public String run() throws Exception{
        File file = new File("/downloaded_data.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        File outFile = new File("/var/root/Desktop/output.txt");
        FileWriter writer = new FileWriter(outFile);

        int line = 1;
        String tempString;

        while ((tempString = reader.readLine()) != null) {
            parse(tempString);
            line++;
        }

        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            String openid = iterator.next().toString();
            System.out.println(openid);
            writer.write(openid + "\n");
        }
        writer.write("end");
        writer.close();
        return "OK";
    }

    private void parse(String jsonStr){
        String[] strs = jsonStr.substring(1, jsonStr.length() - 1).split(",");

        for (String str : strs) {
            String[] strArr = str.split(":");
            if (strArr[0].equals("\"message\"")) {
                String arr[] = strArr[1].split("&");
                for (String string : arr) {
                    String ret[] = string.split("=");
                    if(ret[0].equals("openid")) {
                        set.add(ret[1]);
                        System.out.println(ret[1]);
                    }
                }
            }
        }
    }

    @ResponseBody
    @GetMapping("parseCSV")
    public String parseCSV() throws Exception{
        File file = new File("/var/root/Desktop/sqlResult_2212570.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int line = 1;
        String tempString;

        while ((tempString = reader.readLine()) != null) {
            parseCsvFile(tempString);
            line++;
        }

        readLogWechatId();

        int readLen = readSet.size();
        int csvLen  = csvSet.size();
        readSet.retainAll(csvSet);
        int retLen = readSet.size();

        double result = (double)retLen / (double)readLen;

        return "2019-01-14 :" +
                "<br/>  日活 ： " + readLen +
                "<br/> 新增用户 ： " + csvLen +
                "<br/> 新增用户活跃人数 : " + retLen +
                "<br/> 新增用户活跃占比 ： " + result;
    }

    private void parseCsvFile(String line){
        line = line.substring(1, line.length() - 1);
        System.out.println(line);
        csvSet.add(line);
    }

    private void readLogWechatId() throws Exception{
        File file = new File("/var/root/Desktop/output.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int line = 1;
        String tempString;

        while ((tempString = reader.readLine()) != null) {
            readSet.add(tempString);
            line++;
        }
    }
}


