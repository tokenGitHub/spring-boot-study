package com.hanhe.study.hodgepodge;

import java.util.Scanner;

public class PasswordChecked {

//    1. 密码只能由大写字母，小写字母，数字构成；
//    2. 密码不能以数字开头；
//    3. 密码中至少出现大写字母，小写字母和数字这三种字符类型中的两种；
//    4. 密码长度至少为8

    public static void main(String [] args){
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        for(; s.hasNext();){
            String str = s.next();
            if(str.length() < 8 || digetChecked(str.charAt(0)) || types(str) < 2) {
                System.out.println("NO");
                continue;
            }
            System.out.println("YES");
        }
    }

    public static boolean digetChecked(char s){
        return s >= '0' && s <= '9';
    }

    public static int types(String s){
        int cnt = 0;
        if(s.split("\\d").length > 0){
            cnt ++;
        }
        if(s.split("[a-z]").length > 0) {
            cnt++;
        }
        if(s.split("[A-Z]").length > 0){
            cnt ++;
        }
        return cnt;
    }
}
