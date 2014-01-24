package ncmdp.exporttofea.wizard;

import java.io.File;
import java.util.List;

import ncmdp.model.Attribute;
import ncmdp.model.Feature;
import ncmdp.model.ValueObject;
import ncmdp.serialize.FeatureConfig;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPCommonUtil;
import ncmdp.util.MDPUtil;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
/**
 * 导出特性的向导式对话框
 * @author wangxmn
 *
 */
public class ExportFeatureWizard extends Wizard{

	private FeatureNamePage featureNamePage = null;
	private FeatureConfigPage featureConfigPage = null;
	private ValueObject model;
	private String prefix = "Features_";
	private String suffix = ".xml";
	
	public ExportFeatureWizard(ValueObject model){
		super();
		this.model = model;
	}
	
	@Override
	public boolean performFinish() {
		List<Attribute> exportAttrs = featureConfigPage.getSelection();
		if(exportAttrs==null){
			MessageDialog.openError(this.getShell(), "提示", "没有选中任何属性");
			return false;
		}
		Feature feature = getFeature();
		if(feature==null){
			return false;
		}
		String name = model.getGraph().getOwnModule();
		File featureFile = MDPCommonUtil.getFeatureFile(name);
		if(FeatureConfig.addFeature(featureFile, feature)){
			MessageDialog.openInformation(this.getShell(), "提示", "导出特性完成");
		}else{
			MessageDialog.openError(this.getShell(), "提示", "导出特性失败");
		}
		return true;
	}
	@Override
	public void addPages() {
		featureNamePage = new FeatureNamePage("输入feature名称", 
				model.getGraph().getOwnModule());
		featureConfigPage = new FeatureConfigPage("选择feature", model);
		addPage(featureNamePage);
		addPage(featureConfigPage);
	}
	@Override
	public boolean canFinish() {
		if(this.getContainer().getCurrentPage()!=featureConfigPage){
			return false;
		}
		return super.canFinish();
	}
	
//	public File getFeatureFile(String moduleName){
//		String ncHome = NCMDPTool.getNCHome();
//		String filename = prefix + moduleName + suffix;
//		return new File(ncHome, "/ierp/metadata/Features/" + filename);
//	}
	
	private Feature getFeature(){
		Feature feature = new Feature();
		List<Attribute> attrs = featureConfigPage.getSelection();
		String propId = featureNamePage.getPropId();
		if(propId==null||"".equals(propId)){
			MessageDialog.openError(this.getShell(), "警告", "特性id为空!");
			return null;
		}
		String propName = featureNamePage.getPropName();
		if(propName==null||"".equals(propName)){
			MessageDialog.openError(this.getShell(), "警告", "特性id为空!");
			return null;
		}
		feature.setName(propId);
		feature.setDisplayName(propName);
		feature.addAttributes(attrs);
		return feature;
	}

}
