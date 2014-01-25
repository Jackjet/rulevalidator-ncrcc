//package com.yonyou.nc.codevalidator.plugin.domain.dmm.other.debug;
//
//import org.eclipse.core.runtime.Platform;
//
//import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
//import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
//import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
//import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
//import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
//import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
//import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
//import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;
//import com.yonyou.nc.codevalidator.sdk.upm.UpmOperateException;
//
//@RuleDefinition(catalog = CatalogEnum.OTHERCONFIGFILE, coder = "muxh", description = "������������������", 
//relatedIssueId = "817", subCatalog = SubCatalogEnum.OCF_UPM)
//public class TestCase00817 extends AbstractRuleDefinition{
//
//	private IRuleExecuteResult checkVmArgument(LaunchFileVO[] launchvos) {
//		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
//		if(launchvos==null||launchvos.length==0){
//			return null;
//		}
//		
//		for(LaunchFileVO vo:launchvos){
//			if(!vo.getType().equals("nc.uap.mde.launch")){
//				continue;
//			}
//			StringBuilder errorList = new StringBuilder();
//			VmArgumentVO argument=vo.famatVmArgument();
//			if(argument==null){
//				errorList.append("����["+vo.getProjectName()+"]������["+vo.getDebugName()+"]û������-Xmx -Xms --XX:MaxPermSize --XX:PermSize�Ȳ���\n");
//			}
//			if(argument.getXms()<512){
//				errorList.append("����["+vo.getProjectName()+"]������["+vo.getDebugName()+"]-Xms����ֵС��512m,��ǰֵ:"+argument.getXms()+"m\n");
//			}
//			if(argument.getXmx()<512){
//				errorList.append("����["+vo.getProjectName()+"]������["+vo.getDebugName()+"]-Xmx����ֵС��512m,��ǰֵ:"+argument.getXmx()+"m\n");
//			}
//			if(argument.getPermSize()<256){
//				errorList.append("����["+vo.getProjectName()+"]������["+vo.getDebugName()+"]-XX:PermSize����ֵС��256m,��ǰֵ:"+argument.getPermSize()+"m\n");
//			}
//			if(argument.getMaxPermSize()<512){
//				errorList.append("����["+vo.getProjectName()+"]������["+vo.getDebugName()+"]-XX:MaxPermSize����ֵС��512m,��ǰֵ:"+argument.getMaxPermSize()+"m\n");
//			}
//			if(errorList.length()>0){
//				result.addResultElement(vo.getProjectName(), errorList.toString());
//			}
//		}
//		
//		return result;	
//	}
//
//	private LaunchFileVO[] getLaunchVO(IRuleExecuteContext ruleExecContext) throws UpmOperateException {
//		String projectName=ruleExecContext.getBusinessComponent().getProjectName();
////		IWorkspace workspace=ResourcesPlugin.getWorkspace();
////		workspace.getRoot().getLocation();
//		String workSpacePath=Platform.getLocation().toString();
//	
//		String wokspacePath=workSpacePath+"/.metadata/.plugins/org.eclipse.debug.core/.launches/";//TODO
//		LaunchFileVO[]launchvos=LaunchFileUtils.loadLanchFileByProject(projectName, wokspacePath);
//		return launchvos;
//	}
//
//
//
//	@Override
//	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext)
//			throws RuleBaseException {
//		LaunchFileVO[]launchvos=this.getLaunchVO( ruleExecContext);
//		IRuleExecuteResult result=this.checkVmArgument(launchvos);
//		return result;
//		
//	}
//
//}
