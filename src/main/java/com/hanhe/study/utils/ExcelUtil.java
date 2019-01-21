package com.hanhe.study.utils;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;

public class ExcelUtil {

    public static void outputDetailExcel(String columns[][], List data, HttpServletResponse response, String name, FilterInterface filter) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode(name + ".xls", "utf-8"));

            WritableWorkbook wwb = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet sheet = wwb.createSheet(name, 0);
            setColumn(columns[1], sheet);

            if(filter == null) {
                setData(data, columns[0], sheet);
            }else{
                setData(data, columns[0], sheet, filter);
            }
            wwb.write();
            wwb.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void setColumn(String [] column, WritableSheet sheet){
        int len = column.length;
        try {
            for (int i = 0; i < len; i++) {
                sheet.addCell((new Label(i, 0, column[i])));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void setData(List list, String column[], WritableSheet sheet){
        Object object = list.get(0);
        Class clazz = object.getClass();
        int len = column.length;

        try {
            for (int j = 0; j < len; j++) {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(column[j], clazz);
                Method method = propertyDescriptor.getReadMethod();

                for (int i = 0; i < list.size(); i++) {
                    Object item = list.get(i);
                    Object data = method.invoke(item);
                    sheet.addCell(new Label(j, i + 1,  data.toString()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void setData(List list, String column[], WritableSheet sheet, FilterInterface filter) throws Exception{
        Object object = list.get(0);
        Class clazz = object.getClass();
        int len = column.length;

        for (int j = 0; j < len; j++) {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(column[j], clazz);
            Method method = propertyDescriptor.getReadMethod();

            for (int i = 0; i < list.size(); i++) {
                Object item = list.get(i);
                Object data = method.invoke(item);
                sheet.addCell(new Label(j, i + 1, filter.filter(column[j], data)));
            }
        }
    }
}
