package com.hanhe.study.controller;

import com.hanhe.study.utils.FilterInterface;
import lombok.extern.log4j.Log4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Log4j
@Controller
@RequestMapping("reduction")
public class StreamReductionOperationController {
    private List<String> list = new ArrayList<>();
    private Map<Integer, String> map = new HashMap<>();

    private void listInit(){
        list.clear();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
    }

    private void mapInit(){
        listInit();
        list.forEach(s -> map.put(s.length(), s));
    }

    @ResponseBody
    @GetMapping("reduce")
    public Object reduce() throws Exception{
        mapInit();
        Optional<String> optionalS = list.stream().reduce((s1, s2) -> s1.length() > s2.length() ? s1 : s2);
        log.info(optionalS.get());
        log.info(FilterInterface.toString(list.stream().max(Comparator.comparing(String::length))));
        log.info(list.stream().reduce(0, (sum, str) -> str.length() + sum, (a,b ) -> a + b));
        return map;
    }

    @ResponseBody
    @GetMapping("collect")
    public Object collect(){
        mapInit();
        return map;
    }


}
