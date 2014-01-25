package com.yonyou.nc.codevalidator.rule.annotation;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则的执行阶段定义
 * 
 * @since 2.6
 * @author mazhqa
 */
public enum ExecutePeriod {

    /**
     * 检出阶段，当存在资源时能够静态执行检查，而不需要依赖编译的jar包或数据库
     */
    CHECKOUT("检出阶段", 0, "CHECKOUT"),

    /**
     * 编译完成阶段，此时可以使用IClassLoaderUtils中的方法
     */
    COMPILE("编译完成阶段", 1, "COMPILE"),

    /**
     * 部署阶段，此时可以检查出与数据库相关的规则
     */
    DEPLOY("部署阶段", 2, "DEPLOY");

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
     * 根据执行时期的名称获得枚举对象，如果没有匹配的元素则返回null
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
     * 判断当前要求的执行阶段是否可以在exePeriod中进行
     * @param exePeriod
     * @return
     */
    public boolean canExecuteInEnv(ExecutePeriod exePeriod) {
    	return this.value <= exePeriod.value;
    }
    
}
