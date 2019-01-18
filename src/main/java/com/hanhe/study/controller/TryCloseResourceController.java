package com.hanhe.study.controller;

import java.io.FileInputStream;
import java.io.InputStream;

public class TryCloseResourceController {

    public Object closeResource(){
        try(
            InputStream inputStream = new FileInputStream("");
            FileInputStream fileInputStream = new FileInputStream("");
        ){

        }catch (Exception e){
            e.printStackTrace();
        }

        //TODO 如何知道资源是否被释放了
        return null;
    }
}
