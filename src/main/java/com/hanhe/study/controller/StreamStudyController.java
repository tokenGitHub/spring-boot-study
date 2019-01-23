package com.hanhe.study.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Stream;

@Log4j
@Controller
@RequestMapping("stream")
public class StreamStudyController {
    private List<String> list = new ArrayList<>();
    private Map<Integer, String> map = new HashMap<>();

    private void listInit(){
        list.clear();
        list.add("aoe");
        list.add("iuv2");
        list.add("bpmf3");
        list.add("dtnl45");
        list.add("abcdefssg");
        list.add("hijknmasdfafl");
    }

    private void mapInit(){
        listInit();
        list.forEach(s -> map.put(s.length(), s));
    }

    @ResponseBody
    @GetMapping("removeIf")
    public Object removeIf(){
        listInit();
        list.removeIf(s -> s.contains("v"));
        list.removeIf( s -> s.length() > 4);
        list.stream().forEach(log::info);

        return list;
    }

    @ResponseBody
    @GetMapping("replaceAll")
    public Object replaceAll(){
        listInit();

        list.replaceAll( s -> {
            if(s.length() <=3 ) {
                return "hahaha";
            }
            return s;
        });

        list.sort(Comparator.comparing(String::length));
        return list;
    }

//  TODO 待研究
    @ResponseBody
    @GetMapping("spliterator")
    public Object spliterator(){
        Spliterator spliterator = list.spliterator();
        log.info(spliterator.characteristics());
        log.info(Spliterator.SUBSIZED);

        log.info(spliterator.trySplit());

        log.info(spliterator.estimateSize());

        log.info(spliterator.trySplit());
        return list;
    }


    @ResponseBody
    @GetMapping("map")
    public Object map(){
        mapInit();
//        map.put(3,  map.getOrDefault(3,  "defaultValue"));
//        map.put(23, map.getOrDefault(23, "defaultValue"));
        map.forEach((k, v) -> log.info("  key : " + k + " value : " + v));
        map.putIfAbsent(3 ,"absent");
        map.putIfAbsent(23 ,"absent");

        list.stream().map(String::length).forEach(log::info);

        return map;
    }

}
