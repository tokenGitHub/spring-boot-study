package com.hanhe.study.asyncTask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

@Slf4j
@Component
public class TestTask {
    @Async("taskExecutor")
    public Future<Long> doTask(String task) throws Exception{
        Random random = new Random();
        long start = System.currentTimeMillis();
        for(int i = 0 ;i< 10 ;i++){
            log.info(task);
            int time = random.nextInt(100);
            Thread.sleep(time);
        }
        return new AsyncResult<>(System.currentTimeMillis() - start);
    }

    @Async("taskExecutor")
    public void taskReturnValueMange(List<Future<Long>> futures, long start) throws Exception{
        long ret = 0;
        for (Future future : futures) {
            ret += Long.parseLong(future.get().toString());
        }
        log.info("" + ret);

        log.info("  " + (System.currentTimeMillis() - start) );
    }

}
