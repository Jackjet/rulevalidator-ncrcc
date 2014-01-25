package ncmdp.editor;

import ncmdp.actions.DeleteComponentAndTablesAction;
import ncmdp.actions.DeletePublishedMetaDataAction;
import ncmdp.actions.ExportToCodeAction;
import ncmdp.actions.ExportToFeatureAction;
import ncmdp.actions.ExportToImageAction;
import ncmdp.actions.ExportToPdmFileAction;
import ncmdp.actions.ExportToPdmFileActionAndOpen;
import ncmdp.actions.ExportToXSDFileAction;
import ncmdp.actions.ExporttoMappingAction;
import ncmdp.actions.FeatureAction;
import ncmdp.actions.GenModuleFileAction;
import ncmdp.actions.MappingAction;
import ncmdp.actions.MultiLangAction;
import ncmdp.actions.GenModuleFileAction.GenModuleFileOption;
import ncmdp.actions.GenSqlAction;
import ncmdp.actions.GenWSDLAction;
import ncmdp.actions.ImportAttrToCellAction;
import ncmdp.actions.ImportInterfaceAction;
import ncmdp.actions.PublishMetaDataAction;
import ncmdp.model.Cell;
import ncmdp.model.Entity;
import ncmdp.model.Feature;
import ncmdp.model.Service;
import ncmdp.model.ValueObject;
import ncmdp.model.activity.BusiActivity;
import ncmdp.model.activity.BusiOperation;
import ncmdp.model.activity.OpInterface;
import ncmdp.serialize.FeatureConfig;
import ncmdp.util.MDPLogger;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.StructuredSelection;

public class NCMDPEditorContextMenuProvider extends ContextMenuProvider {
	private ImportAttrToCellAction importAttrToCellAction = null;

	private PublishMetaDataAction publishMetaDataAction = null;

	private PublishMetaDataAction publishMetaDataActionAllLowVersion = null;

	private DeletePublishedMetaDataAction deletePublishedMetaDataAction = null;

	private DeleteComponentAndTablesAction deleteComponentAndTablesAction = null;

	private ExportToCodeAction exportToCodeAction = null;

	private ExportToXSDFileAction exportToXSDFileAction = null;

	private GenWSDLAction genWSDLAction = null;

	private ExportToImageAction exportToImageAction = null;

	private GenModuleFileAction genUPMModuleFileAction = null;

	private ExportToPdmFileAction exportToPdmFileAction = null;

	private ExportToPdmFileActionAndOpen exportToPdmFileActionAndOpen = null;

	private MultiLangAction multiAction = null;

	private ImportInterfaceAction importInterfaceAction = null;
	
	private ExportToFeatureAction exportToFeatureAction = null;
	
	private ExporttoMappingAction exportToMappingAction = null;

	private GenSqlAction genSqlAction1 = null;

	private GenSqlAction genSqlAction2 = null;

	private MenuManager sub = null;
	
	private MenuManager mapping = null;

	private NCMDPEditor editor;

	public NCMDPEditorContextMenuProvider(NCMDPEditor editor,
			EditPartViewer view) {
		super(view);
		this.editor = editor;
		initActions();
	}

	private void initActions() {
		importAttrToCellAction = new ImportAttrToCellAction();
		publishMetaDataAction = new PublishMetaDataAction(false);
		publishMetaDataActionAllLowVersion = new PublishMetaDataAction(true);
		deletePublishedMetaDataAction = new DeletePublishedMetaDataAction();
		deleteComponentAndTablesAction = new DeleteComponentAndTablesAction();
		exportToCodeAction = new ExportToCodeAction();
		exportToXSDFileAction = new ExportToXSDFileAction();
		genWSDLAction = new GenWSDLAction();
		exportToImageAction = new ExportToImageAction();
		genUPMModuleFileAction = new GenModuleFileAction(
				GenModuleFileOption.ONLY_UPM);
		exportToPdmFileAction = new ExportToPdmFileAction();
		exportToPdmFileActionAndOpen = new ExportToPdmFileActionAndOpen();
		multiAction = new MultiLangAction();
		importInterfaceAction = new ImportInterfaceAction();
		genSqlAction1 = new GenSqlAction(false);
		genSqlAction2 = new GenSqlAction(true);

		sub = new MenuManager(Messages.NCMDPEditorContextMenuProvider_0);
		mapping = new MenuManager("映射");
	}

	@Override
	public void buildContextMenu(IMenuManager mm) {
		StructuredSelection select = (StructuredSelection) getViewer()
				.getSelection();
		Object first = select.getFirstElement();
		Object model = null;
		if (first instanceof AbstractEditPart) {
			model = ((AbstractEditPart) first).getModel();
		}
		
		boolean isRefEditor = editor.isRefMDFileEditor();
		mm.add(importAttrToCellAction);
		mm.add(new Separator());
		mm.add(publishMetaDataAction);
		mm.add(publishMetaDataActionAllLowVersion);
		mm.add(deletePublishedMetaDataAction);
		mm.add(deleteComponentAndTablesAction);
		mm.add(new Separator());
		mm.add(exportToCodeAction);
		mm.add(genWSDLAction);
		mm.add(new Separator());
		mm.add(multiAction);
		mm.add(new Separator());
		mm.add(importInterfaceAction);
		mm.add(new Separator());
		mm.add(exportToPdmFileAction);
		mm.add(exportToPdmFileActionAndOpen);
		mm.add(new Separator());
		mm.add(genUPMModuleFileAction);
		mm.add(new Separator());
		mm.add(genSqlAction1);
		mm.add(genSqlAction2);
		mm.add(new Separator());
		
		//添加特性
		//导出特性
		if(model!=null&&model instanceof ValueObject){
			exportToFeatureAction = new ExportToFeatureAction((ValueObject)model);
			exportToMappingAction = new ExporttoMappingAction((ValueObject)model);
			mm.add(exportToFeatureAction);
			mm.add(exportToMappingAction);
			mm.add(new Separator());
		}
		//特性接口，isRefEditor表示是否为本mde工程下的内容，且bmf文件须放在
		//metadata文件夹下
		if (!isRefEditor && model != null && model instanceof ValueObject) {
			/** yuyoga 根据模块名去寻找相应的特性配置文件 */
			String moduleName = null;
			if (model instanceof Entity) {
				/** 获取模块名 */
				moduleName = ((Entity) model).getGraph().getOwnModule();
//				MDPLogger.info("moduleName is "+moduleName);
			}
			/** end */
			/**获得特性**/
			Feature[] features = FeatureConfig.getFeatures(moduleName);
			int count = features == null ? 0 : features.length;
			sub.removeAll();
			mapping.removeAll();
			for (int i = 0; i < count; i++) {
				if (features[i] == null) {
					sub.add(new Separator());
				} else {
					if(features[i].getBusiItfIds().isEmpty()){
						sub.add(new FeatureAction(features[i]));
					}else{
						mapping.add(new MappingAction(features[i]));
					}
					
				}
				
			}
			mm.add(sub);
			mm.add(mapping);
			mm.add(new Separator());
		}
		/**特性完毕**/
		//添加导出为图片popmenu
		mm.add(exportToImageAction);

		boolean sqlLimited = !isRefEditor && model != null
				&& model instanceof Cell && !(model instanceof BusiOperation)
				&& !(model instanceof BusiActivity)
				&& !(model instanceof OpInterface);
		importAttrToCellAction.setEnabled(!isRefEditor && model != null
				&& model instanceof ValueObject);
		publishMetaDataAction.setEnabled(!isRefEditor);
		publishMetaDataActionAllLowVersion.setEnabled(!isRefEditor);
		deletePublishedMetaDataAction.setEnabled(!isRefEditor);
		deleteComponentAndTablesAction.setEnabled(!isRefEditor);
		exportToCodeAction.setEnabled(!isRefEditor && model != null
				&& model instanceof Cell && !(model instanceof BusiOperation)
				&& !(model instanceof BusiActivity));
		exportToXSDFileAction.setEnabled(!isRefEditor && model != null
				&& model instanceof Cell);
		exportToPdmFileAction.setEnabled(sqlLimited);
		exportToPdmFileActionAndOpen.setEnabled(sqlLimited);
		genWSDLAction.setEnabled(!isRefEditor && model != null
				&& model instanceof Service);

		genUPMModuleFileAction.setEnabled(!isRefEditor && model != null
				&& model instanceof OpInterface);
		genSqlAction1.setEnabled(sqlLimited);
		genSqlAction2.setEnabled(sqlLimited);

		boolean isInterface = NCMDPEditor.getActiveMDPEditor().getModel()
				.isbizmodel();
		importInterfaceAction.setEnabled(isInterface);

	}

}
