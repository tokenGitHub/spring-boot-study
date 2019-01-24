package com.hanhe.study.hodgepodge;

import java.util.Scanner;

public class Series {

    //   某种特殊的数列a1, a2, a3, ...的定义如下：a1 = 1, a2 = 2, ... , an = 2 * an − 1 + an - 2 (n > 2)。
    //   给出任意一个正整数k，求该数列的第k项模以32767的结果是多少？

    public static void main(String [] args){
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int a[] = new int[1000000];
        a[0] = 1; a[1] = 2;
        int len = 2;
        for(int i = 0; i < n;i++){
            int k = in.nextInt();

            if(a[k - 1] == 0){
                for(int j = len; j <= k ;j++){
                    a[j] = (a[j - 1] * 2 % 32767 + a[j - 2] % 32767) % 32767;
                    len++;
                }
            }
            System.out.println(a[k - 1]);
        }
    }
}
