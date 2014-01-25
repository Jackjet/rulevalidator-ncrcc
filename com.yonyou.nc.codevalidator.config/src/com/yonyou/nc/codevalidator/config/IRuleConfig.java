package com.yonyou.nc.codevalidator.config;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.vo.IRuleCheckConfiguration;

/**
 * 规则配置相关接口，用于规则配置的存储和读取操作
 * @author mazhqa
 * @since V2.3
 */
public interface IRuleConfig {
	
	/**
	 * 将规则配置导出至输出流中
	 * 
	 * @param os
	 *            - 不会关闭此输出流，需要在外面关闭
	 * @param ruleCheckConfiguration
	 * @throws RuleBaseException
	 */
	void exportConfig(OutputStream os, IRuleCheckConfiguration ruleCheckConfiguration) throws RuleBaseException;

	/**
	 * 将输入流中的数据导入，返回规则配置
	 * 
	 * @param is
	 *            - 此输入流不会在此方法中被关闭
	 * @return
	 * @throws RuleBaseException
	 *             - 加载配置时可能会出现格式错误，也可能是文件刚新建读取错误
	 */
	IRuleCheckConfiguration loadConfiguration(InputStream is) throws RuleBaseException;

	/**
	 * 解析对应的业务组件下，并整理成执行的context
     * 
	 * @param businessComponent
	 * @return
	 * @throws RuleParserException - 规则 配置文件读取错误时抛出此异常
	 */
	IRuleCheckConfiguration parseRule(BusinessComponent businessComponent) throws RuleBaseException;
	
	/**
	 * 得到规则配置文件路径
	 * 
	 * @param businessComponent
	 * @return
	 */
	String getRuleConfigFilePath(BusinessComponent businessComponent);

	/**
	 * 得到规则配置文件相对于本工程project的路径
	 * 
	 * @return
	 */
	String getRuleConfigRelativePath();

	/**
	 * 在业务组件下，规则配置文件的名称，应该在业务组件/rulecase文件夹下
	 * 
	 * @return
	 */
	String getRuleConfigFileName();

	/**
	 * 配置文件夹初始化操作
	 * 
	 * @param configFolder
	 * @throws RuleBaseException
	 */
	void configFolderInitialize(File configFolder) throws RuleBaseException;
	
	/**
	 * 配置文件初始化操作，用于初始化文件是进行配置文件初始内容的写入
	 * @param configFile
	 * @throws RuleBaseException
	 */
	void configFileInitialize(File configFile) throws RuleBaseException;
}
