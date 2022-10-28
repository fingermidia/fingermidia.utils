/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fingermidia.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author dirceubelem
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //on class level
public @interface Column {

    public enum TYPE {

        COLUMN, KEY
    }

    TYPE type() default TYPE.COLUMN;

    String name();

    boolean mainOrder() default false;

    boolean showJSON() default true;

    String jsonName();

    String dateTimeFormat() default "";

    boolean dateTime() default false;

    boolean jsonData() default false;

    boolean search() default false;

}
