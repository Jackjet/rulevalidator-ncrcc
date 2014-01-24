package ncmdp.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ncmdp.cache.MDPCachePool;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Attribute;
import ncmdp.model.BusiItfImplConnection;
import ncmdp.model.BusinInterface;
import ncmdp.model.Connection;
import ncmdp.model.Feature;
import ncmdp.model.JGraph;
import ncmdp.model.Reference;
import ncmdp.model.ValueObject;
import ncmdp.serialize.JGraphSerializeTool;
import ncmdp.util.MDPLogger;
import ncmdp.views.NCMDPViewSheet;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;

public class MappingAction extends Action {

	private class AddMappingCommand extends Command {
		private ValueObject vo = null;
		private Feature feature = null;
		private Attribute[] attrs = null;
		private List<Connection> sourceConn = null;
		private List<BusinInterface> busiItfs = null;

		public AddMappingCommand(ValueObject vo, Feature attrs) {
			super();
			this.vo = vo;
			this.feature = attrs;
		}

		@Override
		public void execute() {
			redo();
		}

		@Override
		public void redo() {
			Map<String, String> mapping = feature.getBusiAndAttrMapping();
			Map<String, Attribute[]> maps = vo.getBusiattrAttrExtendMap();
			attrs = feature.getAttrbuteCopys();
			for(int i=0,j=attrs.length;i<j;i++){
				String busiItfAttrId = mapping.get(attrs[i].getName());
				if(busiItfAttrId!=null){
					if(maps.get(busiItfAttrId)==null){
						maps.put(busiItfAttrId, new Attribute[]{attrs[i],null});
					}
				}
				vo.addProp(attrs[i]);
			}
			List<String> ids = feature.getBusiItfIds();
			if(ids==null||ids.isEmpty()){
				MDPLogger.info("BusiInterface is empty");
			}
			Map<String, BusinInterface> busItfMap = new HashMap<String, BusinInterface>();
			busiItfs = new ArrayList<BusinInterface>();
			for(int i=0,j=ids.size();i<j;i++){
				String busiItfID = ids.get(i);
				BusinInterface itf  = getBusItf(busiItfID);
				busiItfs.add(itf);
				busItfMap.put(busiItfID, itf);
				vo.addBusiItf(itf); 
			}
			List<Reference> refs = feature.getRefers();
			for(int i=0,j=refs.size();i<j;i++){
				Reference ref = refs.get(i);
				String busiId = ref.getRefId();
				ref.setReferencedCell(busItfMap.get(busiId));
				vo.getGraph().addCell(ref);
			}
			sourceConn = new ArrayList<Connection>();
			for(int i=0,j=refs.size();i<j;i++){
				sourceConn.add(new BusiItfImplConnection(vo, refs.get(i)));
			}

			NCMDPViewSheet.getNCMDPViewPage().getCellPropertiesView().getTv()
					.refresh();
		}

		@Override
		public void undo() {
			//ɾ������
			if(attrs!=null&&attrs.length>0){
				for(int i=0,j=attrs.length;i<j;i++){
					vo.removeProp(attrs[i]);
				}
			}
			//ɾ������
			if(sourceConn!=null&&!sourceConn.isEmpty()){
				for(Connection conn:sourceConn){
					conn.disConnect();
				}
			}
			//ɾ��������ҵ��ӿ����Ե�ӳ��
			Collection<String> busiAttrIds = feature.getBusiAndAttrMapping().values();
			if(busiAttrIds!=null&&!busiAttrIds.isEmpty()){
				for(String id:busiAttrIds){
					vo.getBusiattrAttrExtendMap().remove(id);
				}
			}
			//ɾ��ҵ��ӿ�
			if(busiItfs!=null&&!busiItfs.isEmpty()){
				for(BusinInterface inter:busiItfs){
					vo.removeBusiItf(inter);
				}
			}
			List<Reference> refs = feature.getRefers();
			if(refs!=null&&!refs.isEmpty()){
				for(Reference ref:refs){
					vo.getGraph().removeCell(ref);
				}
			}
			NCMDPViewSheet.getNCMDPViewPage().getCellPropertiesView().getTv()
			.refresh();
		}
	}

	private Feature feature = null;

	public MappingAction(Feature feature) {
		super();
		this.feature = feature;
		setText(feature.getDisplayName());
	}

	@Override
	public void run() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if (editor != null) {
			List<?> list = editor.getGV().getSelectedEditParts();
			ValueObject vo = null;
			if (list.size() > 0) {
				EditPart ep = (EditPart) list.get(0);
				// ���ѡ�е�ʵ�������ģ��
				Object o = ep.getModel();
				if (o instanceof ValueObject) {
					vo = (ValueObject) o;
				}
			}
			if (vo != null) {
				AddMappingCommand cmd = new AddMappingCommand(vo, feature);
				editor.executComand(cmd);
			} else {
				MessageDialog.openConfirm(editor.getSite().getShell(),
						Messages.FeatureAction_2, Messages.FeatureAction_3);
			}
		}
	}
	
	private BusinInterface getBusItf(String busiItfID){
		File refFile = new File(MDPCachePool.getInstance().
				getFilePathByIDAndIndustry(busiItfID, null));
		if(!refFile.exists()){
			throw new RuntimeException("���õ�ҵ��ӿڲ����ڣ�ҵ��ӿ�id"+busiItfID);
		}
		JGraph refGraph = JGraphSerializeTool.loadFromFile(refFile,false);
		return (BusinInterface)refGraph.getCellByID(busiItfID);
	}

}
