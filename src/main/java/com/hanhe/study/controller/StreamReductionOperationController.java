package com.hanhe.study.controller;

import com.hanhe.study.domain.ExcelTestDomain;
import com.hanhe.study.utils.FilterInterface;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Log4j
@Controller
@RequestMapping("reduction")
public class StreamReductionOperationController {
    private List<ExcelTestDomain> list = new ArrayList<>();
    private Map<String, Integer> map = new HashMap<>();

    private void listInit(){
        for(int i = 0 ;i < 10 ;i++){
            ExcelTestDomain testDomain = new ExcelTestDomain();
            testDomain.setId(i);
            testDomain.setAge(i +10);
            testDomain.setName("name~~~" + i);
            testDomain.setCreateDate(new Date(System.currentTimeMillis()));
            testDomain.setStatus(i % 3);
            list.add(testDomain);
        }
    }

    private void mapInit(){
        listInit();
        list.forEach(s -> map.put(s.toString(), s.getId()));
    }

    @ResponseBody
    @GetMapping("reduce")
    public Object reduce() throws Exception{
        mapInit();
        Optional<ExcelTestDomain> optionalS = list.stream().reduce((s1, s2) -> s1.getId() > s2.getId() ? s1 : s2);
        log.info(optionalS.get());
        log.info(FilterInterface.toString(list.stream().max(Comparator.comparing(ExcelTestDomain::getAge))));
        log.info(list.stream().reduce(0, (sum, str) -> str.getAge() + sum, (a,b ) -> a + b));
        return map;
    }

    @ResponseBody
    @GetMapping("collect")
    public Object collect(){
        mapInit();

        map = list.stream().collect(Collectors.toMap(ExcelTestDomain::getName, ExcelTestDomain::getAge));

        Map map1 = list.stream().collect(
                Collectors.groupingBy(ExcelTestDomain::getStatus)
        );
        map1.forEach((k, v) -> log.info("map1    k : " + k + "  v : " + v));

        Map map2 = list.stream().collect(
                Collectors.groupingBy(ExcelTestDomain::getStatus, Collectors.counting())
        );

        map2.forEach((k, v) -> log.info("map2    k : " + k + "  v : " + v));

        Map map3 = list.stream().collect(
                Collectors.groupingBy(
                    ExcelTestDomain::getStatus, Collectors.mapping(
                            ExcelTestDomain::getStatus,Collectors.counting()
                    )
                )
        );

        map3.forEach((k, v) -> log.info("map3    k : " + k + "  v : " + v));

        return map;
    }
}