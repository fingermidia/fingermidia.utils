/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fingermidia.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;

/**
 * @author Dirceu
 */
public class DateTime implements Comparable {

    private long millis = 0;

    public DateTime(DateTime d) {
        this.millis = d.getMillis();
    }

    private static String getZone() {
        return "America/Sao_Paulo";
    }

    private static String getGMT() {
        return "GMT-3";
    }

    public DateTime(String date, String format) throws ParseException {
        DateFormat fmt = new SimpleDateFormat(format);
        this.millis = fmt.parse(date).getTime();
    }

    public DateTime(int... v) throws ParseException {
        String date = v[2] + "/" + v[1] + "/" + v[0] + " " + v[3] + ":" + v[4] + ":" + v[5];
        String format = "dd/MM/yyyy HH:mm:ss";
        DateFormat fmt = new SimpleDateFormat(format);
        this.millis = fmt.parse(date).getTime();
    }

    public DateTime(int year, int month, int day) throws ParseException {
        String date = day + "/" + month + "/" + year;
        String format = "dd/MM/yyyy";
        DateFormat fmt = new SimpleDateFormat(format);
        this.millis = fmt.parse(date).getTime();
    }

    public DateTime(long millis) {
        this.millis = millis;
    }

    public DateTime(Timestamp value) {
        if (value != null) {
            this.millis = value.getTime();
        } else {
            this.millis = 0;
        }
    }

    public DateTime(Date value) {
        if (value != null) {
            this.millis = value.getTime();
        } else {
            this.millis = 0;
        }
    }

    public static DateTime now() {
        return new DateTime(getInstant().toEpochMilli());
    }

    public Date getDate() {
        if (this.millis != 0) {
            return new Date(this.millis);
        } else {
            return null;
        }
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public long getMillis() {
        return this.millis;
    }

    public Timestamp getTimestamp() {
        if (this.millis != 0) {
            return new Timestamp(this.millis);
        } else {
            return null;
        }
    }

    public String toString(String format) {
        if (this.millis != 0) {
            DateFormat fmt = new SimpleDateFormat(format);
            return fmt.format(getTimestamp());
        } else {
            return "";
        }
    }

    public String getWeekDay() {
        return this.toString("EEEE");
    }

    public String getWeekDaySimple() {
        String day = this.toString("E").toLowerCase();
        switch (day) {
            case "sun":
                return "Dom";
            case "mon":
                return "Seg";
            case "tue":
                return "Ter";
            case "wed":
                return "Qua";
            case "thu":
                return "Qui";
            case "fri":
                return "Sex";
            case "sat":
                return "Sab";
        }
        return day;
    }

    public int getWeekDayInt() {
        return this.getWeekDaySimple().equals("Dom") || this.getWeekDaySimple().equals("Sun") ? 1
                : this.getWeekDaySimple().equals("Seg") || this.getWeekDaySimple().equals("Mon") ? 2
                : this.getWeekDaySimple().equals("Ter") || this.getWeekDaySimple().equals("Tue") ? 3
                : this.getWeekDaySimple().equals("Qua") || this.getWeekDaySimple().equals("Wed") ? 4
                : this.getWeekDaySimple().equals("Qui") || this.getWeekDaySimple().equals("Thu") ? 5
                : this.getWeekDaySimple().equals("Sex") || this.getWeekDaySimple().equals("Fri") ? 6 : 7;
    }

    public int getDay() {
        return Integer.parseInt(this.toString("dd"));
    }

    public int getMonth() {
        return Integer.parseInt(this.toString("MM"));
    }

    public String getMonthStr() {
        String month = this.toString("MMMM").toLowerCase();
        switch (month) {
            case "january":
                return "Janeiro";
            case "february":
                return "Fevereiro";
            case "march":
                return "Março";
            case "april":
                return "Abril";
            case "may":
                return "Maio";
            case "june":
                return "Junho";
            case "july":
                return "Julho";
            case "august":
                return "Agosto";
            case "september":
                return "Setembro";
            case "october":
                return "Outubro";
            case "november":
                return "Novembro";
            case "december":
                return "Dezembro";
        }
        return month;
    }

    public int getYear() {
        return Integer.parseInt(this.toString("yyyy"));
    }

    public int getHour() {
        return Integer.parseInt(this.toString("HH"));
    }

    public int getMinute() {
        return Integer.parseInt(this.toString("mm"));
    }

    public int getSecond() {
        return Integer.parseInt(this.toString("ss"));
    }

    public void addSecond(long v) {
        this.millis += v * 1000;
    }

    public DateTime addCloneSecond(long v) {
        DateTime c = this.clone();
        c.addSecond(v);
        return c;
    }

    public void addMinute(long v) {
        addSecond(v * 60);
    }

    public DateTime addCloneMinute(long v) {
        DateTime c = this.clone();
        c.addMinute(v);
        return c;
    }

    public void addHour(long v) {
        addMinute(v * 60);
    }

    public DateTime addCloneHour(long v) {
        DateTime c = this.clone();
        c.addHour(v);
        return c;
    }

    public void addDay(long v) {
        addHour(v * 24);
    }

    public DateTime addCloneDay(long v) {
        DateTime c = this.clone();
        c.addDay(v);
        return c;
    }

    private static Instant getInstant() {
        System.setProperty("user.timezone", getZone());
        Instant now = Instant.now();
        now.atZone(ZoneId.of(getZone()));
        return now;
    }

    public void addMonth(int v) {
        if (v > 0) {
            int initDay = getDay();
            for (int i = 0; i < v; i++) {
                int month = getMonth();
                do {
                    addDay(1);
                } while (month == getMonth());
            }
            if (initDay < getDay()) {
                do {
                    addDay(1);
                } while (initDay != getDay());
            } else {
                do {
                    addDay(-1);
                } while (initDay != getDay());
            }
        } else {
            int initDay = getDay();
            for (int i = 0; i < (v * -1); i++) {
                int month = getMonth();
                do {
                    addDay(-1);
                } while (month == getMonth());
            }
            if (initDay < getDay()) {
                do {
                    addDay(1);
                } while (initDay != getDay());
            } else {
                do {
                    addDay(-1);
                } while (initDay != getDay());
            }
        }
    }

    public DateTime addCloneMonth(int v) {
        DateTime d = this.clone();
        d.addMonth(v);
        return d;
    }

    public void addYear(int v) {
        addDay(v * 365);
    }

    public DateTime addCloneYear(int v) {
        DateTime c = this.clone();
        c.addYear(v);
        return c;
    }

    public void addDateTime(DateTime v) {
        this.millis += v.getMillis();
    }

    public DateTime addCloneDateTime(DateTime v) {
        DateTime c = this.clone();
        c.addDateTime(v);
        return c;
    }

    public int compareTo(Object o) {
        DateTime d = (DateTime) o;
        if (this.millis == d.getMillis()) {
            return 0;
        } else if (this.millis > d.getMillis()) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public DateTime clone() {
        return new DateTime(this.getMillis());
    }

    public int[] array() {
        int[] v = new int[7];
        v[0] = this.getYear();
        v[1] = this.getMonth();
        v[2] = this.getDay();
        v[3] = this.getHour();
        v[4] = this.getMinute();
        v[5] = this.getSecond();
        v[6] = getWeekDayInt();
        return v;
    }

    @Override
    public String toString() {
        return toString("dd/MM/yyyy HH:mm:ss");
    }

    public static long diffDays(DateTime v1, DateTime v2) throws Exception {

        if (v1.getMillis() <= v2.getMillis()) {

            long diff = v2.getMillis() - v1.getMillis();
            diff = diff / 24 / 60 / 60 / 1000;
            return diff;

        } else {
            return diffDays(v2, v1);
        }

    }

    public static long diffHour(DateTime v1, DateTime v2) throws Exception {

        if (v1.getMillis() <= v2.getMillis()) {

            long diff = v2.getMillis() - v1.getMillis();
            diff = diff / 60 / 60 / 1000;
            return diff;

        } else {
            return diffHour(v2, v1);
        }

    }

    public static long diffMinutes(DateTime v1, DateTime v2) throws Exception {

        long diff = v2.getMillis() - v1.getMillis();
        diff = diff / 60 / 1000;
        return diff;

    }

    public static long diffWorkingDayInSeconds(DateTime v1, DateTime v2) throws Exception {

        if (v1.getMillis() <= v2.getMillis()) {

            if (v1.getWeekDayInt() == 1) {
                v1.addDay(1);
                int[] t1 = v1.array();
                t1[3] = 8;
                t1[4] = 0;
                t1[5] = 0;
                v1 = new DateTime(t1);
            }
            if (v2.getWeekDayInt() == 1) {
                v2.addDay(1);
                int[] t2 = v2.array();
                t2[3] = 8;
                t2[4] = 0;
                t2[5] = 0;
                v2 = new DateTime(t2);
            }
            if (v1.getWeekDayInt() == 7) {
                v1.addDay(2);
                int[] t1 = v1.array();
                t1[3] = 8;
                t1[4] = 0;
                t1[5] = 0;
                v1 = new DateTime(t1);
            }
            if (v2.getWeekDayInt() == 7) {
                v2.addDay(2);
                int[] t2 = v2.array();
                t2[3] = 8;
                t2[4] = 0;
                t2[5] = 0;
                v2 = new DateTime(t2);
            }

            if (v1.getDay() == v2.getDay() && v1.getMonth() == v2.getMonth() && v1.getYear() == v2.getYear()) {

                int[] t1 = v1.array();
                int[] t2 = v2.array();

                if (t1[3] > 18) {
                    t1[3] = 18;
                    t1[4] = 0;
                    t1[5] = 0;
                }

                if (t2[3] > 18) {
                    t2[3] = 18;
                    t2[4] = 0;
                    t2[5] = 0;
                }

                if (t1[3] < 8) {
                    t1[3] = 8;
                    t1[4] = 0;
                    t1[5] = 0;
                }

                if (t2[3] < 8) {
                    t2[3] = 8;
                    t2[4] = 0;
                    t2[5] = 0;
                }

                if (t1[3] > 12 && t1[3] < 14) {
                    t1[3] = 14;
                    t1[4] = 0;
                    t1[5] = 0;
                }

                if (t2[3] > 12 && t2[3] < 14) {
                    t2[3] = 14;
                    t2[4] = 0;
                    t2[5] = 0;
                }

                if (t1[3] < 14 && t2[3] >= 14) {
                    t1[3] += 2;
                }
                DateTime d1 = new DateTime(t1);
                DateTime d2 = new DateTime(t2);

                long total = (d2.getMillis() / 1000) - (d1.getMillis() / 1000);

                return total;

            } else {
                DateTime v = v1.clone();
                int[] t = v.array();
                if (t[3] == 8 && t[4] == 0 && t[5] == 0) {
                    v.addDay(1);
                    return diffWorkingDayInSeconds(v, v2) + 28800;
                } else {
                    if (t[3] < 14) {
                        t[3] += 2;
                    }
                    long diff = (((18 - t[3]) * 60) * 60) + (t[4] * 60) + (long) t[5];

                    t[3] = 8;
                    t[4] = 0;
                    t[5] = 0;

                    v = new DateTime(t);
                    v.addDay(1);

                    return diffWorkingDayInSeconds(v, v2) + diff;
                }
            }

        } else {
            return diffWorkingDayInSeconds(v2, v1);
        }

    }
}
