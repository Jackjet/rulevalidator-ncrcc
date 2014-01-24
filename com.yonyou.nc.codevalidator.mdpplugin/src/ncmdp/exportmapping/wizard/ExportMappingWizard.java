package ncmdp.exportmapping.wizard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import ncmdp.exporttofea.wizard.FeatureNamePage;
import ncmdp.model.Attribute;
import ncmdp.model.BusiItfAttr;
import ncmdp.model.BusinInterface;
import ncmdp.model.Cell;
import ncmdp.model.Feature;
import ncmdp.model.JGraph;
import ncmdp.model.Reference;
import ncmdp.model.ValueObject;
import ncmdp.serialize.FeatureConfig;
import ncmdp.util.MDPCommonUtil;
import ncmdp.util.MDPLogger;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.xml.sax.SAXException;
/**
 * 导出业务接口属性映射
 * @author wangxmn
 *
 */
public class ExportMappingWizard extends Wizard{

	private FeatureNamePage mappingNamePage = null;
	private MappingConfigPage mappingConfigPage = null;
	private ValueObject model;
	
	
	public ExportMappingWizard(ValueObject model){
		super();
		this.model = model;
	}
	
	@Override
	public boolean performFinish() {
		//业务接口
		List<BusinInterface> busiInters = mappingConfigPage.getSelection();
		if(busiInters==null||(busiInters!=null&&busiInters.isEmpty())){
			MessageDialog.openError(this.getShell(), "提示", "没有选中任何业务接口属性映射");
			return false;
		}
		File featureFile = MDPCommonUtil.getFeatureFile(model);
		Feature feature = createFeature(busiInters);
		boolean result = false;
		try {
			result = FeatureConfig.addBusiItf(feature, featureFile);
		} catch (ParserConfigurationException e) {
			MDPLogger.error(e.getMessage(), e);
			MessageDialog.openError(this.getShell(), "错误", "根据文件创建Doc错误！");
		} catch (SAXException e) {
			MDPLogger.error(e.getMessage(), e);
			MessageDialog.openError(this.getShell(), "错误", "文件解析错误！");
		} catch (IOException e) {
			MDPLogger.error(e.getMessage(), e);
			MessageDialog.openError(this.getShell(), "错误", "文件未找到！");
		} catch (TransformerException e) {
			MDPLogger.error(e.getMessage(), e);
			MessageDialog.openError(this.getShell(), "错误", "格式编辑出现问题！");
		}
		if(result){
			MessageDialog.openInformation(this.getShell(), "提示", "导出业务接口属性映射完成");
		}else{
			MessageDialog.openError(this.getShell(), "提示", "导出特性失败");
		}
		return true;
	}
	@Override
	public void addPages() {
		mappingNamePage = new FeatureNamePage("输入feature名称", 
				model.getGraph().getOwnModule());
		mappingConfigPage = new MappingConfigPage("选择feature", model);
		addPage(mappingNamePage);
		addPage(mappingConfigPage);
	}
	@Override
	public boolean canFinish() {
		if(this.getContainer().getCurrentPage()!= mappingConfigPage){
			return false;
		}
		return super.canFinish();
	}
	
	private Feature createFeature(List<BusinInterface> busiInters){
		//面板模型
		JGraph graph = model.getGraph();
		MDPLogger.info("start to create the features!------------------------------");
		if(graph==null){
			MDPLogger.info("JGraph is null!");
			return null;
		}
		List<Cell> cells = graph.getCells();
		if(cells.isEmpty()){
			MDPLogger.info("the cells are empty!");
			return null;
		}
		//参照
		List<Reference> refers = new ArrayList<Reference>();
		//业务接口属性与实体属性的映射关系
		Map<String,String> referAttrs = new HashMap<String,String>();
		//实体属性
		List<Attribute> attributes = new ArrayList<Attribute>();
		/**获得上述值**/
		Map<String,Reference> referMaps = new HashMap<String, Reference>();
		List<BusiItfAttr> busiAttrs = new ArrayList<BusiItfAttr>();
		Map<String, Attribute[]> mapping = model.getBusiattrAttrExtendMap();
		for(int i=0,j=graph.getCells().size();i<j;i++){
			Cell c = cells.get(i);
			if(c instanceof Reference){
				Reference r = (Reference) c;
				referMaps.put(r.getRefId(), r);
			}
		}
		for(int i=0,j=busiInters.size();i<j;i++){
			BusinInterface busiI = busiInters.get(i);
			Reference ref = referMaps.get(busiI.getId());
			if(ref!=null){
				refers.add(ref);
			}
			busiAttrs.addAll(busiI.getBusiItAttrs());//接口中的属性
		}
		for(int i=0,j=busiAttrs.size();i<j;i++){
			BusiItfAttr busiA = busiAttrs.get(i);
			Attribute[] as = mapping.get(busiA.getId());
			if(as[0]!=null){
				attributes.add(as[0]);
				referAttrs.put(as[0].getName(), busiA.getId());
			}
		}
		Feature feature = new Feature();
		feature.setName(mappingNamePage.getPropId());
		feature.setDisplayName(mappingNamePage.getPropName());
		feature.addBusinInterface(busiInters);
		feature.addRefer(refers);
		feature.addAttributes(attributes);
		feature.putBusiAndAttrMapping(referAttrs);
		return feature;
	}
}

