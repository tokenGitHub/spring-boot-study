package com.hanhe.study.hodgepodge;

import java.util.LinkedList;
import java.util.Queue;

public class pathFinder {
    public static void run(String args[]) {
        String a = ".W.\n" +
                ".W.\n" +
                "...",

                b = ".W.\n" +
                        ".W.\n" +
                        "W..",

                c = "......\n" +
                        "......\n" +
                        "......\n" +
                        "......\n" +
                        "......\n" +
                        "......",

                d = "......\n" +
                        "......\n" +
                        "......\n" +
                        "......\n" +
                        ".....W\n" +
                        "....W.";

        System.out.println(pathFinder(a));
        System.out.println(pathFinder(b));
        System.out.println(pathFinder(c));
        System.out.println(pathFinder(d));
    }

    static char[][] arr;
    static int[][] visit;
    static Queue<String> queue = new LinkedList<String>();
    static boolean ret = false;

    static boolean pathFinder(String maze) {
        ret = false;
        String[] strings = maze.split("\n");
        arr = new char[strings.length][];

        for (int i = 0; i < strings.length; i++) {
            arr[i] = strings[i].toCharArray();
        }
        visit = new int[arr.length][arr[0].length];
        queue.clear();
        queue.offer(0 + "," + 0);
        bfs();

        return ret;
    }

    static void bfs() {
        while (!queue.isEmpty()) {
            String chars[] = queue.poll().split(",");
            int i = Integer.parseInt(chars[0]);
            int j = Integer.parseInt(chars[1]);

            if (visit[i][j] == 1 || arr[i][j] == 'W') continue;
            visit[i][j] = 1;
            if (i == arr.length - 1 && j == arr[0].length - 1) {
                ret = true;
                return;
            }
            //访问四个方向
            if (i + 1 < arr.length) queue.offer((i + 1) + "," + j);
            if (j + 1 < arr.length) queue.offer(i + "," + (j + 1));
            if (i - 1 >= 0) queue.offer((i - 1) + "," + j);
            if (j - 1 >= 0) queue.offer(i + "," + (j - 1));

        }
    }
}

