package com.hanhe.study.asyncTask.spiders.jsoupSpider;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public abstract class JsoupSpiderAbstract {
    protected Document document;

    public String getTitle(){
        return document.title();
    }

    public String url(){
        return document.location();
    }

    //获取对应 class 的所有 内容
    public List<String> geHtmlByClass(String clazz){
        Elements elements = document.getElementsByClass(clazz);
        List<String> list = new ArrayList<>();

        for(Element element : elements){
            list.add(element.html());
        }
        return list;
    }

    //获取对应 class 的所有 attr 值
    public List<String> geAttributeByClass(String clazz, String attr){
        Elements elements = document.getElementsByClass(clazz);
        List<String> list = new ArrayList<>();

        for(Element element : elements){
            list.add(element.attr(attr));
        }
        return list;
    }

    public String getHtmlById(String id){
        return document.getElementById(id).html();
    }

    public String getAttributeByIdAndKey(String id, String attr){
        return document.getElementById(id).attr(attr);
    }


}
