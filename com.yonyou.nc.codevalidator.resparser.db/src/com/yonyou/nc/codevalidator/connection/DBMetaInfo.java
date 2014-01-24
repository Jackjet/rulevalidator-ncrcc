/*
 * 创建日期 2005-8-22
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.yonyou.nc.codevalidator.connection;

import java.io.Serializable;

/**
 * @nopublish
 * @author hey
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class DBMetaInfo implements Serializable{

    /**
     * <code>serialVersionUID</code> 的注释
     */
    private static final long serialVersionUID = 1230267680871744361L;
    //数据库类型
    private int type;
    //数据库版本
    private int Version; 
    //数据库名称
    private String name;
    //是否支持批操作
    boolean isSupportBatch=true;
    //是否JDBC-ODBC桥
    boolean isODBC=false;
    //用户名
    String userName;
    //
    String catalog;
    
   
    
   
    /**
     * @param type
     * @param version
     * @param name
     * @param isSupportBatch
     * @param isODBC
     */
    public DBMetaInfo(int type, int version, String name, boolean isSupportBatch,
            boolean isODBC) {
        super();
        this.type = type;
        Version = version;
        this.name = name;
        this.isSupportBatch = isSupportBatch;
        this.isODBC = isODBC;
    }
    
    
    
    public String getCatalog() {
        return catalog;
    }
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public boolean isODBC() {
        return isODBC;
    }
    public void setODBC(boolean isODBC) {
        this.isODBC = isODBC;
    }
    public boolean isSupportBatch() {
        return isSupportBatch;
    }
    public void setSupportBatch(boolean isSupportBatch) {
        this.isSupportBatch = isSupportBatch;
    }
    /**
     * @return 返回 name。
     */
    public String getName() {
        return name;
    }
    /**
     * @param name 要设置的 name。
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return 返回 type。
     */
    public int getType() {
        return type;
    }
    /**
     * @param type 要设置的 type。
     */
    public void setType(int type) {
        this.type = type;
    }
    /**
     * @return 返回 version。
     */
    public int getVersion() {
        return Version;
    }
    /**
     * @param version 要设置的 version。
     */
    public void setVersion(int version) {
        Version = version;
    }
}
