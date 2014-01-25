package com.yonyou.nc.codevalidator.sqltrans;

import java.util.Vector;

/**
 * 此处插入类型说明。 
 * 功能：表达一个表达式 
 * 创建日期：(2001-3-15 19:17:17)
 * 
 * @author：张 森
 */
public class Expresion_str {
    Expresion_str left;//其左边的常数或变量

    Expresion_str right;//其右边的常数或变量

    char operatie;//操作符

    Vector valoare;//代表一个常数

    //String valoarestr=null;//代表一个变量
    /**
     * Expresion 构造子注解。
     */
    public Expresion_str() {
        super();
    }
}