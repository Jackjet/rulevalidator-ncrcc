package ncmdp.importattr.wizard;

import java.util.ArrayList;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Attribute;
import ncmdp.model.Entity;
import ncmdp.model.ValueObject;
import ncmdp.pdmxml.Field;
import ncmdp.views.NCMDPViewSheet;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class ImportAttrWizard extends Wizard {
	private class ImportAttrCommand extends Command{
		private Attribute[] attrs = null;
		private Attribute keyAttr = null;
		private Attribute oldKeyAttr = null;
		public ImportAttrCommand(Attribute[] attrs, Attribute keyAttr) {
			super(Messages.ImportAttrWizard_0);
			this.attrs = attrs;
			this.keyAttr = keyAttr;
		}

		@Override
		public void execute() {
			if(vo instanceof Entity && keyAttr != null){
				oldKeyAttr =((Entity)vo).getKeyAttribute();
			}
			redo();
		}

		@Override
		public void redo() {
			int count = attrs == null ? 0 : attrs.length;
			for (int i = 0; i < count; i++) {
				vo.addProp(attrs[i]);
			}
			if(vo instanceof Entity && keyAttr != null){
				((Entity)vo).setKeyAttribute(keyAttr);
			}
			NCMDPViewSheet.getNCMDPViewPage().getCellPropertiesView().getTv().refresh();
		}

		@Override
		public void undo() {
			int count = attrs == null ? 0 : attrs.length;
			for (int i = 0; i < count; i++) {
				vo.removeProp(attrs[i]);
			}
			if(vo instanceof Entity && keyAttr != null){
				((Entity)vo).setKeyAttribute(oldKeyAttr);
			}
			NCMDPViewSheet.getNCMDPViewPage().getCellPropertiesView().getTv().refresh();
		}
	}
	private SelImportSourceWizardPage selImportSourcePage = new SelImportSourceWizardPage(SelImportSourceWizardPage.class.getCanonicalName(), Messages.ImportAttrWizard_1, null);

	private SelPDMFilePathWizardPage selPdmFilePathPage = new SelPDMFilePathWizardPage(SelPDMFilePathWizardPage.class.getCanonicalName(), Messages.ImportAttrWizard_2, null);

	private SelDBTableWizardPage selDBTableWizardPage = new SelDBTableWizardPage(SelDBTableWizardPage.class.getCanonicalName(), Messages.ImportAttrWizard_3, null);
	
	private SelTableFromBillItem selTableFromBillItemWizardPage = new SelTableFromBillItem(SelTableFromBillItem.class.getCanonicalName(), Messages.ImportAttrWizard_4, null);

	private SelTableColumnWizardPage selTableColumnsPage = new SelTableColumnWizardPage(SelTableColumnWizardPage.class.getCanonicalName(), Messages.ImportAttrWizard_5, null);
	
	private ValueObject vo = null;
	public ImportAttrWizard(ValueObject vo) {
		super();
		this.vo = vo;
	}

	@Override
	public boolean performFinish() {
		IWizardPage currPage = this.getContainer().getCurrentPage();
		if (currPage instanceof SelTableColumnWizardPage) {
			SelTableColumnWizardPage page = (SelTableColumnWizardPage)currPage;
			Field[] fields = page.getSelectedFields();
			ArrayList<Attribute> al = new ArrayList<Attribute>();
			Attribute keyAttr = null;
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				//导入属性，只是从pdm或表中获得属性，然后导入，重新设置id
				Attribute attr = new Attribute();
				attr.setName(field.getName());
				attr.setDisplayName(field.getDisplayName());
				attr.setType(field.getModuleType());
				attr.setFieldName(field.getName());
				attr.setLength(field.getLength());
				attr.setPrecision(field.getPrecision());
				if(field.isKey())
					keyAttr = attr;
				al.add(attr);
			}
			if(al.size() > 0){
				ImportAttrCommand cmd = new ImportAttrCommand(al.toArray(new Attribute[0]), keyAttr);
				if(NCMDPEditor.getActiveMDPEditor() != null)
					NCMDPEditor.getActiveMDPEditor().executComand(cmd);
			}
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void addPages() {
		addPage(selImportSourcePage);//导入数据源
		addPage(selPdmFilePathPage);//输入pdm路径页面
		addPage(selDBTableWizardPage);//输入数据库属性的页面
		addPage(selTableFromBillItemWizardPage);//输入从bill表获得数据库属性的页面
		addPage(selTableColumnsPage);//选择属性的页面
	}

	@Override
	public boolean canFinish() {
		IWizardPage currPage = this.getContainer().getCurrentPage();
		if (currPage instanceof SelTableColumnWizardPage) {
			return true;
		}

		return false;
	}

}
