package com.fingermidia;

import com.fingermidia.dao.fw.Helper;
import com.fingermidia.utils.DateTime;
import com.fingermidia.utils.Guid;

public class Main {
    public static void main(String[] args) throws Exception {
//        DateTime now = DateTime.now();
//        now.addMonth(-1);
//        now.addMonth(1);
//        DateTime billingDate = new DateTime(now.getYear(), now.getMonth(), 1, 0, 0, 0);
//
//        DateTime due = billingDate.clone();
//        due.addDay(10);
//
//        DateTime lastDayMonth = billingDate.clone();
//        lastDayMonth.addMonth(1);
//        lastDayMonth.addDay(-1);

//        System.out.println(Guid.getString10());
        System.out.println(Helper.ofuscate("dirceubelem@gmail.com"));
    }
}
