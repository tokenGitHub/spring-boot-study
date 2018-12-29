package com.hanhe.study.asyncTask.spiders.jsoupSpider;

import org.jsoup.nodes.Document;


public class DataParser extends JsoupSpiderAbstract{
    private Document document;

    public DataParser(Document document){
        this.document = document;
    }

}
