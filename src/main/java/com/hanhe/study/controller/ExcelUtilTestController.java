package com.hanhe.study.controller;

import com.hanhe.study.asyncTask.DoSpiderTask;
import com.hanhe.study.asyncTask.TestTask;
import com.hanhe.study.domain.ExcelTestDomain;
import com.hanhe.study.utils.ExcelUtil;
import com.hanhe.study.utils.FilterInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.Future;

@Slf4j
@Controller
@RequestMapping("test")
@AllArgsConstructor
public class ExcelUtilTestController {
    private TestTask task;
    private DoSpiderTask spiderTask;

    @GetMapping("getExcel")
    public void getTestExcel(HttpServletResponse response){
        List<ExcelTestDomain> list = new ArrayList<>();
        for(int i = 0 ;i < 10 ;i++){
            ExcelTestDomain testDomain = new ExcelTestDomain();
            testDomain.setId(i);
            testDomain.setAge(i * 10);
            testDomain.setName(randomName());
            testDomain.setCreateDate(new Date(System.currentTimeMillis()));
            testDomain.setStatus(i % 3);
            list.add(testDomain);
        }
        String columns[][] = {
            {"id", "age", "name", "createDate", "status"},  //第一行为属性名
            {"编号", "年龄", "姓名", "创建日期", "状态"}       //第二行为显示名  一一对应
        };
        ExcelUtil.outputDetailExcel(columns, list, response, "测试数据", (column, data) -> {
            if("status".equals(column)){
                switch (data.toString()){
                    case "0": return "小学";
                    case "1": return "初中";
                    case "2": return "高中";
                }
            }
            return data.toString();
        });
    }

    private String randomName(){
        Random random = new Random();
        int len = random.nextInt(15) + 5;
        String ret = "";
        for(int i = 0;i < len;i++){
            ret += (char)(random.nextInt(26) + 'A');
        }
        return ret;
    }

    @ResponseBody
    @GetMapping("async")
    public String testAsync() throws Exception{
        List<Future<Long>> taskFutures = new ArrayList<>();
        long start = System.currentTimeMillis();

        for(int i = 0 ;i < 500 ;i++) {
            taskFutures.add(task.doTask(i + "Thread  " )) ;
            log.info("  " +i);
        }

        task.taskReturnValueMange(taskFutures,start);
        return "ok";
    }

    @ResponseBody
    @GetMapping("toString")
    public String toStringTest() throws Exception{
        String ret = "";

        List<Object> list = new ArrayList<>();
        list.add(new String());
        list.add(new Integer(0));
        list.add(new Long(0));
        list.add(new ArrayList<>());
        list.add(new HashMap<>());
        list.add(new Object());
        list.add(new Hashtable<>());
        list.add(new ExcelTestDomain());

        for(int i = 0;i < list.size();i++){
            ret += FilterInterface.toString(list.get(i)) + " <br/> <br/>";
        }
        return ret;
    }

    @ResponseBody
    @GetMapping("doTask")
    public String doTask(){
        spiderTask.doBaiduTask();
//        Pattern pattern = Pattern.compile("http://(\\w+.)+");
//        Matcher matcher = pattern.matcher(string);
//        while (matcher.find()){
//            String subStr = matcher.group();
//            String e = matcher.group();
//            //截取出括号中的内容
//            String substring = e.substring(3, e.length()-1);
//            //字符串截取
//            CharSequence subSequence = string.subSequence(matcher.start(0), matcher.end(0));
//            System.out.println("开始位置:"+matcher.start(0)+" 结束位置:"+matcher.end(0));
//            System.out.println(subSequence.toString());
//            System.out.println(e);
//        }
        return " 这就是 return";
    }
}
