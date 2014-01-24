package com.yonyou.nc.codevalidator.rule.annotation;

import java.util.ArrayList;
import java.util.List;

/**
 * �����ִ�н׶ζ���
 * 
 * @since 2.6
 * @author mazhqa
 */
public enum ExecutePeriod {

    /**
     * ����׶Σ���������Դʱ�ܹ���ִ̬�м�飬������Ҫ���������jar�������ݿ�
     */
    CHECKOUT("����׶�", 0, "CHECKOUT"),

    /**
     * ������ɽ׶Σ���ʱ����ʹ��IClassLoaderUtils�еķ���
     */
    COMPILE("������ɽ׶�", 1, "COMPILE"),

    /**
     * ����׶Σ���ʱ���Լ��������ݿ���صĹ���
     */
    DEPLOY("����׶�", 2, "DEPLOY");

    private int value;
    private String name;
    private String innerValue;

    private ExecutePeriod(String name, int value, String innerValue) {
    	this.name = name;
        this.value = value;
        this.innerValue = innerValue;
    }

    public String getName() {
		return name;
	}

    public static List<String> getAllExecutePeriods(){
    	List<String> result = new ArrayList<String>();
    	for (ExecutePeriod executePeriod : ExecutePeriod.values()) {
			result.add(executePeriod.getName());
		}
    	return result;
    }
    
    /**
     * ����ִ��ʱ�ڵ����ƻ��ö�ٶ������û��ƥ���Ԫ���򷵻�null
     * @param name
     * @return
     */
    public static ExecutePeriod getExecutePeriod(String name){
    	for (ExecutePeriod executePeriod : ExecutePeriod.values()) {
			if(executePeriod.name.equals(name) || executePeriod.innerValue.equals(name)){
				return executePeriod;
			}
		}
    	return null;
    }
    
    /**
     * �жϵ�ǰҪ���ִ�н׶��Ƿ������exePeriod�н���
     * @param exePeriod
     * @return
     */
    public boolean canExecuteInEnv(ExecutePeriod exePeriod) {
    	return this.value <= exePeriod.value;
    }
    
}
