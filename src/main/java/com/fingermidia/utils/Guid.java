/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fingermidia.utils;

import java.util.Random;

/**
 * @author Dirceu
 */
public class Guid {

    private static Guid guidFactory = new Guid();
    static Random random = new Random();

    public static void setGuidImpl(Guid factory) {
        guidFactory = factory;
    }

    public static String getString() {
        return guidFactory.getGuidString().toLowerCase();
    }

    protected String getGuidString() {
        long rand = (random.nextLong() & 0x7FFFFFFFFFFFFFFFL) |
                0x4000000000000000L;
        return Long.toString(rand, 32) +
                Long.toString(System.currentTimeMillis() & 0xFFFFFFFFFFFFFL, 32);
    }

    public static String getString10() {
        return guidFactory.getGuidString10().toUpperCase();
    }

    protected String getGuidString10() {
        long rand = (random.nextLong() & 0x7FFFFFFL) |
                0x4000000L;
        return Long.toString(rand, 32) +
                Long.toString(System.currentTimeMillis() & 0xFFFFL, 32);
    }
}

