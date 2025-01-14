package com.fingermidia;

import com.fingermidia.utils.DateTime;

public class Main {
    public static void main(String[] args) throws Exception {
        DateTime now = DateTime.now().addHour(-1);
        System.out.println(now);
    }
}
