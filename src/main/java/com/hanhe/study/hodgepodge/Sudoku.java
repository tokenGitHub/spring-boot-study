package com.hanhe.study.hodgepodge;

public class Sudoku {
    public static void run(String args[]){
        int[][] sudoku = {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };

        System.out.println(check(sudoku));

    }

    public static boolean check(int[][] sudoku) {
        int [][] flag = new int[3][3];
        for(int i = 0 ; i < sudoku.length ; i++){
            int flag1 = 0,flag2 = 0;

            for(int j = 0;j<sudoku[i].length;j++){
                flag1 ^= (1 << sudoku[i][j]);
                flag2 ^= (1 << sudoku[j][i]);
                flag[i/3][j/3] ^= (1 << sudoku[i][j]);
            }
            if (flag1 != 0x3FE || flag2 != 0x3FE) return false;
        }

        for(int i = 0; i < 3;i++)
            for(int j = 0; j< 3;j++)
                if (flag[i][j] != 0x3FE) return false;
        return true;
    }
}


