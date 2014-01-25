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
//@RuleDefinition(catalog = CatalogEnum.OTHERCONFIGFILE, coder = "muxh", description = "调试项启动参数设置", 
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
//				errorList.append("工程["+vo.getProjectName()+"]调试项["+vo.getDebugName()+"]没有配置-Xmx -Xms --XX:MaxPermSize --XX:PermSize等参数\n");
//			}
//			if(argument.getXms()<512){
//				errorList.append("工程["+vo.getProjectName()+"]调试项["+vo.getDebugName()+"]-Xms参数值小于512m,当前值:"+argument.getXms()+"m\n");
//			}
//			if(argument.getXmx()<512){
//				errorList.append("工程["+vo.getProjectName()+"]调试项["+vo.getDebugName()+"]-Xmx参数值小于512m,当前值:"+argument.getXmx()+"m\n");
//			}
//			if(argument.getPermSize()<256){
//				errorList.append("工程["+vo.getProjectName()+"]调试项["+vo.getDebugName()+"]-XX:PermSize参数值小于256m,当前值:"+argument.getPermSize()+"m\n");
//			}
//			if(argument.getMaxPermSize()<512){
//				errorList.append("工程["+vo.getProjectName()+"]调试项["+vo.getDebugName()+"]-XX:MaxPermSize参数值小于512m,当前值:"+argument.getMaxPermSize()+"m\n");
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
