package com.hanhe.study.controller;

import com.hanhe.study.annotation.Fruit;
import com.hanhe.study.annotation.FruitInfoUtil;
import com.hanhe.study.annotation.FruitName;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("annotation")
public class AnnotationTestController {

    @ResponseBody
    @FruitName
    @GetMapping("methodAnnotation")
    public String run(){
        return FruitInfoUtil.getFruitInfo(Fruit.class);
    }
}
