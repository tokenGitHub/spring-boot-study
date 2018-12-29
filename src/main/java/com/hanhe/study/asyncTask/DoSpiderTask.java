package com.hanhe.study.asyncTask;

import com.hanhe.study.asyncTask.spiders.FirstSpider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DoSpiderTask {
    Random random = new Random();

    @Async("taskExecutor")
    public void doCsdnTask(){
        new FirstSpider("https://blog.csdn.net/qq_29215513" + random.nextDouble(), true);
    }


    @Async("taskExecutor")
    public void doBaiduTask(){
        new FirstSpider("https://news.baidu.com/", true);
    }
}
