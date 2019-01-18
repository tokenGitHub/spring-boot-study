package com.hanhe.study.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.time.LocalDate;
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
        File file = new File("/var/root/Desktop/2019-01-08SqlResult.csv");
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

        return "2019-01-08 :" +
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

    @ResponseBody
    @GetMapping("testBreakLabel")
    public String testBreakLabel(){
        int i = 0 ,j = 0;
        label : for(j =0 ;j < 10 ; j++) {
            for (i = 0; i < 10; i++) {
                if (i == 2)
                    break label;
                log.info(i + "   " + j);
            }
        }
        log.info(i + "   " + j + "  end");
        return "ok";
    }

    public static Set<String> getWechatIdSet(String fileName) throws Exception{
        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int line = 1;
        String tempString;
        Set<String> resultSet = new HashSet<>();
        while ((tempString = reader.readLine()) != null) {
            tempString = tempString.substring(1, tempString.length() - 1);
            log.info(tempString);
            resultSet.add(tempString);
            line++;
        }
        return resultSet;
    }

    @ResponseBody
    @GetMapping("getDateBeforeDays")
    public Object getDateBeforeDays(Integer year, Integer month, Integer day, Integer days){
        LocalDate localDate = LocalDate.of(year, month, day);
        return localDate.plusDays(-days);
    }

    public static Object jsonStrToArr(JSONObject jsonObject){
        String jsonStr = "{\n" +
                "  \"WAIT_BUYER_CONFIRM_GOODS\": 25178.90997505188,\n" +
                "  \"status_postSum\": 50,\n" +
                "  \"postSum\": 189,\n" +
                "  \"paymentSum\": 74406.93992424011,\n" +
                "  \"status_paymentSum\": 13365.149974822998,\n" +
                "  \"totalFeeSum\": 78553.49999046326,\n" +
                "  \"status_WAIT_BUYER_CONFIRM_GOODS\": 5053.479991912842,\n" +
                "  \"TRADE_REFUND\": 1644,\n" +
                "  \"WAIT_SELLER_SEND_GOODS\": 236,\n" +
                "  \"status_totalFeeSum\": 14290.299993515015,\n" +
                "  \"totalYouzan\": 885,\n" +
                "  \"status_TRADE_SUCCESS\": 7978.669982910156,\n" +
                "  \"TRADE_SUCCESS\": 47348.02994918823,\n" +
                "  \"totalUser\": 2305913,\n" +
                "  \"status_TRADE_REFUND\": 333\n" +
                "}";

        String  resultStr = "";
        resultStr += "<br/>超过预产期_订单完成 ：" + jsonObject.get("status_TRADE_SUCCESS") + ",";
        resultStr += "<br/>超过预产期_等待买家确认 ：" + jsonObject.get("status_WAIT_BUYER_CONFIRM_GOODS");
        resultStr += "<br/>超过预产期_待发货 ：" + jsonObject.get("status_WAIT_SELLER_SEND_GOODS");
        resultStr += "<br/>超过预产期_待付款 ：" + jsonObject.get("status_WAIT_BUYER_PAY");
        resultStr += "<br/>超过预产期_退款中 ：" + jsonObject.get("status_TRADE_REFUND");
        resultStr += "<br/>超过预产期_总金额 ：" + jsonObject.get("status_paymentSum");

        resultStr += "<br/>";

        resultStr += "<br/>订单完成 ：" + jsonObject.get("TRADE_SUCCESS");
        resultStr += "<br/>等待买家确认 ：" + jsonObject.get("WAIT_BUYER_CONFIRM_GOODS");
        resultStr += "<br/>待发货 ：" + jsonObject.get("WAIT_SELLER_SEND_GOODS");
        resultStr += "<br/>待付款 ：" + jsonObject.get("WAIT_BUYER_PAY");
        resultStr += "<br/>退款中 ：" + jsonObject.get("TRADE_REFUND");
        resultStr += "<br/>总金额 ：" + jsonObject.get("paymentSum");

        resultStr += "<br/>";

        resultStr += "<br/>当日超过预产期总用户数 ：" + jsonObject.get("totalUser");
        resultStr += "<br/>当日有赞下单总数 ：" + jsonObject.get("totalYouzan");
        resultStr += "<br/>超过预产期用户下单总数 ：" + jsonObject.get("status_count");

        return resultStr;
    }
}


