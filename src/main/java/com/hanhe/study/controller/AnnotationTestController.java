package com.hanhe.study.controller;

import com.hanhe.study.annotation.FirstAnnotationTest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("annotation")
public class AnnotationTestController {

    @ResponseBody
    @FirstAnnotationTest
    @GetMapping("methodAnnotation")
    public String run(){
        return "ok";
    }
}
