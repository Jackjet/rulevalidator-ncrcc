package com.yonyou.nc.codevalidator.plugin.domain.dmm.other.debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 *工作空间中调试项目信息  %workspace%\.metadata\.plugins\org.eclipse.debug.core\.launches
 */
public class LaunchFileVO {
	/**
	 * 启动参数
	 */
	private String vm_arguments;
	/**
	 * 类型 关注nc.uap.mde.launch类型
	 */
	private String type;
	/**
	 * 工程名
	 */
	private String projectName;
	/**
	 * 调试项名称
	 */
	private String debugName;
	
	public String getDebugName() {
		return debugName;
	}
	public void setDebugName(String debugName) {
		this.debugName = debugName;
	}
	public String getVm_arguments() {
		return vm_arguments;
	}
	public void setVm_arguments(String vm_arguments) {
		this.vm_arguments = vm_arguments;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * -Xms512m -Xmx512m -XX:PermSize=256m -XX:MaxPermSize=256m
	 * @return
	 */
	public VmArgumentVO famatVmArgument(){
		VmArgumentVO argumentVo=new VmArgumentVO();		
		if(this.vm_arguments==null||this.vm_arguments.length()==0){
			return argumentVo;
		}
		String[]lineArguments=this.vm_arguments.split("\\r\\n");
		List<String>argumentsList=new ArrayList<String>();		
		for(String line:lineArguments){
			argumentsList.addAll(Arrays.asList(line.split(" ")));
		}
		if(argumentsList==null||argumentsList.size()==0){
			return argumentVo;
		}
		for(String argument:argumentsList){
			if(argument.startsWith("-Xms")){
				String num=argument.substring(4,argument.length()-1);
				argumentVo.setXms(Double.valueOf(num));
			}
			if(argument.startsWith("-Xmx")){
				String num=argument.substring(4,argument.length()-1);
				argumentVo.setXmx(Double.valueOf(num));
			}
			if(argument.startsWith("-XX:PermSize")){
				String num=argument.substring(12,argument.length()-1);
				argumentVo.setPermSize(Double.valueOf(num));
			}
			if(argument.startsWith("-XX:MaxPermSize")){
				String num=argument.substring(16,argument.length()-1);
				argumentVo.setMaxPermSize(Double.valueOf(num));
			}
		}
		return argumentVo;
	}
}
