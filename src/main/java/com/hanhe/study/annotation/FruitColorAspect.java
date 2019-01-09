package com.hanhe.study.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class FruitColorAspect {

    @Around("execution(* com/hanhe/study/controller* (...))")
    public Object setName(ProceedingJoinPoint point){
        Object[] args = point.getArgs();
        System.out.println("Aspect");
        for(Object o : args){
            System.out.println(o.toString());
        }

        return "Aspect";
    }
}
