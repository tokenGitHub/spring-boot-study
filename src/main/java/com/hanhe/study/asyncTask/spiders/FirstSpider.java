package com.hanhe.study.asyncTask.spiders;


import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class FirstSpider extends BreadthCrawler {

    public FirstSpider(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        if(crawlPath.indexOf("csdn") != -1)
            csdn();
        else{
            baidu(crawlPath);
        }
    }

    public void baidu(String url) {
        this.addSeed(url);

        this.addRegex("http://(\\w+.)+");

        try {
            start(3);
        }catch (Exception e){
            e.printStackTrace();
        }
        setThreads(1);
    }

    public void csdn(){
        Random random = new Random();
        //设置开始爬取的页面
        this.addSeed("https://blog.csdn.net/qq_29215513/article/list/1?" + random.nextDouble());
        this.addSeed("https://blog.csdn.net/qq_29215513/article/list/2?" + random.nextDouble());
//        this.addSeed("http://[a-z]+.+[a-z]+?");

        //设置爬取规则 使用正则表达式
        this.addRegex("https://blog.csdn.net/qq_29215513/article/details/\\d+");

        this.addRegex("-.*\\.(jpg|png|gif).*");
        try {
            start(3);
        }catch (Exception e){
            e.printStackTrace();
        }
        setThreads(1);
    }

    @Override
    public void visit(Page page, CrawlDatums crawlDatums) {
//        System.out.println(page.doc());

//        if(!page.url().matches("https://blog.csdn.net/\\w+\\d+/article/details/\\d+"))
        if(!page.url().matches("http://(\\w+.)+"))
            return;


        System.out.println(page.doc().title());
        try {
            Thread.sleep(1500);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
