package com.hanhe.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableScheduling
@SpringBootApplication
public class StudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyApplication.class, args);
    }

    @EnableAsync
    @Configuration
    public class TaskPoolConfig{

        @Bean("taskExecutor")
        public Executor taskExecutor(){
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(30);  // 核心线程数
            executor.setMaxPoolSize(200);  //最大线程数
            executor.setQueueCapacity(200); // 缓冲队列
            executor.setKeepAliveSeconds(10); // 允许线程空闲时间
            executor.setThreadNamePrefix("taskExecutor-"); //设置线程池前缀
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); //线程池对拒绝任务的处理策略
            return executor;
        }
    }

}

