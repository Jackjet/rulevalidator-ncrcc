package com.yonyou.nc.codevalidator.export.api;

import java.io.File;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IIdentifier;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 部分形式进行规则输出的持久化接口，以每个具体的执行单元和结果进行输出;
 * <P>
 * 此接口不能直接使用，适用于给其他人实现
 * 
 * @author mazhqa
 * @since V2.7
 */
public interface IPortionRulePersistence extends IIdentifier {

	/**
	 * 批量规则执行结果生成，进行保存操作，保存的格式不限
	 * 
	 * @param businessComponent
	 *            - 当前执行单元
	 * @param ruleResultList
	 *            - 执行单元上的规则结果列表
	 * @return 结果文件目录对应的文件夹 - 如果是IRuleFolderPersistence实现的话，如果不是，返回null（比如DAO)
	 * @throws RuleBaseException
	 */
	String batchExportResult(BusinessComponent businessComponent,
			List<IRuleExecuteResult> ruleResultList, RuleExportContext context)
			throws RuleBaseException;

	/**
	 * 结果文件夹初始化操作
	 * 
	 * @param resultFolder
	 * @throws RuleBaseException
	 */
	void resultFolderInitialize(File resultFolder) throws RuleBaseException;

	/**
	 * 存储输出结果的文件夹名称
	 * 
	 * @return
	 */
	String getResultFolderName();

	/**
	 * 是否需要输出至文件夹
	 * @return
	 */
	boolean needExportFolder();
}
