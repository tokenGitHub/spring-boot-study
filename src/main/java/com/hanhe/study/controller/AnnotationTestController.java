package com.hanhe.study.controller;

import com.hanhe.study.annotation.FruitName;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("annotation")
public class AnnotationTestController {

    @ResponseBody
    @FruitName
    @GetMapping("methodAnnotation")
    public String run(){
        System.out.println(" running ...... ");
        return "running ";
    }
}
