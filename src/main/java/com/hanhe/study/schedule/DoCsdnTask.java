package com.hanhe.study.schedule;

import com.hanhe.study.asyncTask.DoSpiderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DoCsdnTask {
    @Autowired
    private DoSpiderTask spiderTask;

//    @Scheduled(cron = "0/20 * * * * ?")
    public void csdnTask(){
//        spiderTask.doCsdnTask();
        spiderTask.doBaiduTask();
    }
}
