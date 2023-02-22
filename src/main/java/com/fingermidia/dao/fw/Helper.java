/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fingermidia.dao.fw;

import com.fingermidia.utils.DateTime;
import org.json.JSONObject;
import org.postgresql.util.PGobject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dirceubelem
 */
public class Helper {

    public static JSONObject createJSON(TOBase t) throws Exception {

        JSONObject j = new JSONObject();

        Class<?> c = t.getClass();

        DateTime d;
        String nomeJson;
        Object o;
        String v;
        for (Field f : c.getDeclaredFields()) {
            if (f.isAnnotationPresent(Column.class) && f.getAnnotation(Column.class).showJSON()) {

                if (f.getAnnotation(Column.class).dateTime()) {
                    d = new DateTime((Timestamp) runGetter(f, t));
                    nomeJson = f.getAnnotation(Column.class).jsonName();
                    j.put(nomeJson, d.toString(f.getAnnotation(Column.class).dateTimeFormat()));
                } else if (f.getType() == String.class) {
                    nomeJson = f.getAnnotation(Column.class).jsonName();
                    o = runGetter(f, t);
                    if (o != null) {
                        v = (String) o;
                        j.put(nomeJson, v.trim());
                    }
                } else {
                    nomeJson = f.getAnnotation(Column.class).jsonName();
                    j.put(nomeJson, runGetter(f, t));
                }

            }
        }

        return j;

    }

    public static Object runGetter(Field field, TOBase o) throws Exception {

        for (Method method : o.getClass().getMethods()) {

            // verificar funcionamento de propriedades boolean
            if ((method.getName().startsWith("get") || method.getName().startsWith("is"))
                    && (method.getName().length() == (field.getName().length() + 3)
                    || method.getName().length() == (field.getName().length() + 2))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    return method.invoke(o);
                }
            }
        }

        return null;
    }

    public static Object runSetter(String name, TOBase o, Object v) throws Exception {

        for (Method method : o.getClass().getMethods()) {

            // verificar funcionamento de propriedades boolean
            if ((method.getName().startsWith("set"))
                    && (method.getName().length() == (name.length() + 3))) {
                if (method.getName().toLowerCase().endsWith(name.toLowerCase())) {
                    if (v instanceof PGobject) {
                        PGobject pg = (PGobject) v;
                        return method.invoke(o, pg.getValue());
                    } else {
                        return method.invoke(o, v);
                    }
                }
            }
        }

        return null;
    }

    public static String obterNomeTabela(TOBase t) {

        Class<?> obj = (Class<?>) t.getClass();

        if (obj.isAnnotationPresent(Table.class)) {
            Annotation annotation = obj.getAnnotation(Table.class);
            Table tabela = (Table) annotation;
            return tabela.name();
        } else {
            return null;
        }

    }

    public static List<Field> obterColunas(TOBase t) throws Exception {

        List<Field> l = new ArrayList<>();

        Class<?> c = (Class<?>) t.getClass();

        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                l.add(field);
            }
        }

        return l;
    }

    public static String obterColunasString(TOBase t) throws Exception {

        StringBuilder ret = new StringBuilder();

        Class<?> c = t.getClass();

        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                if (ret.length() == 0) {
                    ret.append(field.getName());
                } else {
                    ret.append(", ").append(field.getName());
                }
            }
        }

        return ret.toString();
    }

    public static String obterColunasString(TOBase t, String alias) throws Exception {

        StringBuilder ret = new StringBuilder();

        Class<?> c = t.getClass();

        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                if (ret.length() == 0) {
                    ret.append(alias).append(".").append(field.getName()).append(" as ").append(alias).append("_").append(field.getName());
                } else {
                    ret.append(", ").append(alias).append(".").append(field.getName()).append(" as ").append(alias).append("_").append(field.getName());
                }
            }
        }

        return ret.toString();
    }

    public static String obterColunasString(TOBase t, String alias, String prename) throws Exception {

        StringBuilder ret = new StringBuilder();

        Class<?> c = t.getClass();

        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {

                String name = field.getName();
                String asName = prename + name;

                if (ret.length() == 0) {
                    ret.append(alias).append(".").append(name).append(" as ").append(asName);
                } else {
                    ret.append(", ").append(alias).append(".").append(name).append(" as ").append(asName);
                }
            }
        }

        return ret.toString();
    }

    public static List<Field> obterColunasSemChave(TOBase t) throws Exception {

        List<Field> l = new ArrayList<>();

        Class<?> c = t.getClass();

        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) && field.getAnnotation(Column.class).type() == Column.TYPE.COLUMN) {
                l.add(field);
            }
        }

        return l;
    }

    public static List<Field> obterColunasChave(TOBase t) throws Exception {

        List<Field> l = new ArrayList<>();

        Class<?> c = t.getClass();

        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) && field.getAnnotation(Column.class).type() == Column.TYPE.KEY) {
                l.add(field);
            }
        }

        return l;
    }

    public static List<Field> obterColunasOrdenacaoPrincipal(TOBase t) throws Exception {

        List<Field> l = new ArrayList<>();

        Class<?> c = t.getClass();

        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) && field.getAnnotation(Column.class).mainOrder()) {
                l.add(field);
            }
        }

        return l;
    }

    public static List<Field> obterColunasBusca(TOBase t) throws Exception {

        List<Field> l = new ArrayList<>();

        Class<?> c = t.getClass();

        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) && field.getAnnotation(Column.class).search()) {
                l.add(field);
            }
        }

        return l;
    }

}
