package com.hanhe.study.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class ExcelTestDomain {
    private Integer id;
    private Integer age;
    private String name;
    private Date createDate;
    private Integer status;
}
