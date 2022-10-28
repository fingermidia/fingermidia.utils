/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fingermidia.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dirceubelem
 */
public class Cache {

    private static boolean active = true;
    private static long size = 0;
    private static ConcurrentHashMap<String, VOCache> cache = new ConcurrentHashMap<>();

    public static void removeCacheCandidate(String id) {

        List<String> keys = new ArrayList<>();

        for (ConcurrentHashMap.Entry<String, VOCache> entry : cache.entrySet()) {

            if (entry.getKey().contains(id)) {
                keys.add(entry.getKey());
            }

        }

        for (String item : keys) {
            cache.remove(item);
        }
    }

    public static long getSize() {
        return size;
    }

    public static void clear() {
        cache = new ConcurrentHashMap<>();
        size = 0;
    }

    public static boolean isActive() {
        return active;
    }

    public static void setActive(boolean active) {
        if (!active) {
            clear();
        }
        Cache.active = active;
    }

    public static void setCache(String service, String id, Object value, int time) {
        if (active) {

            long t = System.currentTimeMillis() + (time * 60000);

            VOCache v = new VOCache(t, value);

            if (cache.containsKey(service + id)) {
                size -= value.toString().length();
                cache.remove(service + id);
            }

            cache.put(service + id, v);
            size += value.toString().length();
        }
    }

    public static void removeCache(String service, String id) {
        if (active && cache.containsKey(service + id)) {
            cache.remove(service + id);
        }
    }

    public static void setCacheSeconds(String service, String id, Object value, int time) {
        if (active) {

            long t = System.currentTimeMillis() + (time * 1000);

            VOCache v = new VOCache(t, value);

            if (cache.containsKey(service + id)) {
                size -= value.toString().length();
                cache.remove(service + id);
            }

            cache.put(service + id, v);
            size += value.toString().length();
        }
    }

    public static long getTimeExpire(String service, String id) {
        if (active && cache.containsKey(service + id)) {

            VOCache v = cache.get(service + id);

            if (v.getExpire() >= System.currentTimeMillis()) {
                return (v.getExpire() - System.currentTimeMillis()) / 1000;
            } else {
                cache.remove(service + id);
                return 0;
            }

        } else {
            return 0;
        }
    }

    public static Object getCache(String service, String id) {
        if (active && cache.containsKey(service + id)) {

            VOCache v = cache.get(service + id);

            if (v.getExpire() >= System.currentTimeMillis()) {
                return v.getValue();
            } else {
                cache.remove(service + id);
                return null;
            }

        } else {
            return null;
        }
    }
}
