package ncmdp.factory;

import java.util.ArrayList;
import java.util.List;

import ncmdp.model.AggregationRelation;
import ncmdp.model.BusiItfImplConnection;
import ncmdp.model.BusinInterface;
import ncmdp.model.ConvergeConnection;
import ncmdp.model.DependConnection;
import ncmdp.model.Entity;
import ncmdp.model.Enumerate;
import ncmdp.model.ExtendConnection;
import ncmdp.model.Note;
import ncmdp.model.NoteConnection;
import ncmdp.model.Relation;
import ncmdp.model.Service;
import ncmdp.model.ValueObject;
import ncmdp.model.activity.BusiActivity;
import ncmdp.model.activity.BusiOperation;
import ncmdp.model.activity.OpInterface;
import ncmdp.request.CtrlMouseUpRequest;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.swt.events.MouseEvent;

/**
 * 生成调色板程序
 * @author wangxmn
 *
 */
public class PaletteFactory {
	public static class TemplateCreationTool extends CreationTool {
		@Override
		protected boolean handleButtonUp(int button) {
			if (button == 1 && getCurrentInput().isControlKeyDown()) {
				setUnloadWhenFinished(false);
			} else {
				setUnloadWhenFinished(true);
			}
			boolean b = super.handleButtonUp(button);
			return b;
		}

	}

	public static class CustomConnectionCreationTool extends
			ConnectionCreationTool {

		@Override
		protected boolean handleButtonUp(int button) {
			if (button == 1) {
				setUnloadWhenFinished(false);
			} else {
				setState(STATE_TERMINAL);
				setUnloadWhenFinished(true);
			}
			return super.handleButtonUp(button);
		}

	}

	public static class CustomSelectionTool extends SelectionTool {

		@Override
		public void mouseUp(MouseEvent me, EditPartViewer viewer) {
			if (getCurrentInput().isAltKeyDown()) {
				super.getTargetEditPart().performRequest(
						new CtrlMouseUpRequest());
			} else {
				super.mouseUp(me, viewer);
			}
		}
	}

	public static PaletteRoot createPaletteRoot(boolean isBpfFileInput) {
		PaletteRoot paletteRoot = new PaletteRoot();
		paletteRoot.add(createBasePalette(paletteRoot));
		paletteRoot.add(createBusinessComponentPalette(isBpfFileInput));
		paletteRoot.add(createConnectionPalette(isBpfFileInput));
		return paletteRoot;
	}

	/**
	 * 调色板中的Palette
	 * @param root
	 * @return
	 */
	private static PaletteContainer createBasePalette(PaletteRoot root) {
		PaletteContainer container = new PaletteGroup("Base Palette");
		//选择工具箭头
		SelectionToolEntry entry = new SelectionToolEntry("select");
		//CustomSelectionTool作为事件源
		entry.setToolClass(CustomSelectionTool.class);
		container.add(entry);
		return container;
	}

	private static PaletteContainer createConnectionPalette(
			boolean isBpfFileInput) {
		PaletteDrawer drawer = new PaletteDrawer(Messages.PaletteFactory_2);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		//
		ToolEntry dependTE = new ConnectionCreationToolEntry(Messages.PaletteFactory_3,
				Messages.PaletteFactory_4, new CreationFactory() {
					public Object getNewObject() {
						return DependConnection.class;
					}
					public Object getObjectType() {
						return Integer.valueOf(1);
					}
				}, ImageFactory.getDependImgDescriptor(),
				ImageFactory.getDependImgDescriptor());
		dependTE.setToolClass(CustomConnectionCreationTool.class);
		entries.add(dependTE);
		/******************************************************************/
		if (!isBpfFileInput) {
			ToolEntry extendTE = new ConnectionCreationToolEntry(Messages.PaletteFactory_5,
					Messages.PaletteFactory_6, new CreationFactory() {
						public Object getNewObject() {
							return ExtendConnection.class;
						}
						public Object getObjectType() {
							return Integer.valueOf(1);
						}
					}, ImageFactory.getExtendImgDescriptor(),
					ImageFactory.getExtendImgDescriptor());
			extendTE.setToolClass(CustomConnectionCreationTool.class);
			entries.add(extendTE);
			//
			ToolEntry aggreTE = new ConnectionCreationToolEntry(Messages.PaletteFactory_7,
					Messages.PaletteFactory_8, new CreationFactory() {
						public Object getNewObject() {
							return AggregationRelation.class;
						}
						public Object getObjectType() {
							return Integer.valueOf(1);
						}
					}, ImageFactory.getAggregationImgDescriptor(),
					ImageFactory.getAggregationImgDescriptor());
			aggreTE.setToolClass(CustomConnectionCreationTool.class);
			entries.add(aggreTE);
			//
			ToolEntry relationTE = new ConnectionCreationToolEntry(Messages.PaletteFactory_9,
					Messages.PaletteFactory_10, new CreationFactory() {
						public Object getNewObject() {
							return Relation.class;
						}
						public Object getObjectType() {
							return Integer.valueOf(1);
						}
					}, ImageFactory.getRelationImgDescriptor(),
					ImageFactory.getRelationImgDescriptor());
			relationTE.setToolClass(CustomConnectionCreationTool.class);
			entries.add(relationTE);
			//
			ToolEntry busiItfconTE = new ConnectionCreationToolEntry(Messages.PaletteFactory_11,
					Messages.PaletteFactory_12, new CreationFactory() {
						public Object getNewObject() {
							return BusiItfImplConnection.class;
						}
						public Object getObjectType() {
							return Integer.valueOf(1);
						}
					}, ImageFactory.getBusiItfConImgDescriptor(),
					ImageFactory.getBusiItfConImgDescriptor());
			busiItfconTE.setToolClass(CustomConnectionCreationTool.class);
			entries.add(busiItfconTE);
		}
		/******************************************************************/
		else {
			ToolEntry converTE = new ConnectionCreationToolEntry(Messages.PaletteFactory_13,
					Messages.PaletteFactory_14, new CreationFactory() {
						public Object getNewObject() {
							return ConvergeConnection.class;
						}

						public Object getObjectType() {
							return Integer.valueOf(1);
						}
					}, ImageFactory.getConvergeImgDescriptor(),
					ImageFactory.getConvergeImgDescriptor());
			converTE.setToolClass(CustomConnectionCreationTool.class);
			entries.add(converTE);
			ToolEntry busiItfconTE = new ConnectionCreationToolEntry(Messages.PaletteFactory_15,
					Messages.PaletteFactory_16, new CreationFactory() {
						public Object getNewObject() {
							return BusiItfImplConnection.class;
						}

						public Object getObjectType() {
							return Integer.valueOf(1);
						}
					}, ImageFactory.getBusiItfConImgDescriptor(),
					ImageFactory.getBusiItfConImgDescriptor());
			busiItfconTE.setToolClass(CustomConnectionCreationTool.class);
			entries.add(busiItfconTE);
		}
		ToolEntry noteconTE = new ConnectionCreationToolEntry(Messages.PaletteFactory_17,
				Messages.PaletteFactory_18, new CreationFactory() {
					public Object getNewObject() {
						return NoteConnection.class;
					}

					public Object getObjectType() {
						return Integer.valueOf(1);
					}
				}, ImageFactory.getNoteConImgDescriptor(),
				ImageFactory.getNoteConImgDescriptor());
		noteconTE.setToolClass(CustomConnectionCreationTool.class);
		entries.add(noteconTE);
		drawer.addAll(entries);
		return drawer;

	}

	/**
	 * 调色版中的业务组件工具箱
	 * @param isBpfFileInput
	 * @return
	 */
	private static PaletteContainer createBusinessComponentPalette(
			boolean isBpfFileInput) {
		PaletteDrawer drawer = new PaletteDrawer(Messages.PaletteFactory_19);
		List<ToolEntry> entries = new ArrayList<ToolEntry>();
		if (isBpfFileInput) {
			//
			ToolEntry opItfTE = new CombinedTemplateCreationEntry(Messages.PaletteFactory_20,
					Messages.PaletteFactory_21, OpInterface.class, new SimpleFactory(
							OpInterface.class),
					ImageFactory.getOpItfImgDescriptor(),
					ImageFactory.getOpItfImgDescriptor());
			opItfTE.setToolClass(TemplateCreationTool.class);
			entries.add(opItfTE);
			//
			ToolEntry busiOperationTE = new CombinedTemplateCreationEntry(
					Messages.PaletteFactory_22, Messages.PaletteFactory_23, BusiOperation.class,
					new SimpleFactory(BusiOperation.class),
					ImageFactory.getBusiOperationDescriptor(),
					ImageFactory.getBusiOperationDescriptor());
			busiOperationTE.setToolClass(TemplateCreationTool.class);
			entries.add(busiOperationTE);
			//
			ToolEntry busiAcivityTE = new CombinedTemplateCreationEntry(Messages.PaletteFactory_24,
					Messages.PaletteFactory_25, BusiActivity.class, new SimpleFactory(
							BusiActivity.class),
					ImageFactory.getBusiActivityDescriptor(),
					ImageFactory.getBusiActivityDescriptor());
			busiAcivityTE.setToolClass(TemplateCreationTool.class);
			entries.add(busiAcivityTE);
			//
			ToolEntry serviceTE = new CombinedTemplateCreationEntry(Messages.PaletteFactory_26,
					Messages.PaletteFactory_27, Service.class,
					new SimpleFactory(Service.class),
					ImageFactory.getAbstractClassImgDescriptor(),
					ImageFactory.getAbstractClassImgDescriptor());
			serviceTE.setToolClass(TemplateCreationTool.class);
			entries.add(serviceTE);
			serviceTE.setVisible(false);// 抽象类暂时不放开 @2012-05-28
			//
			ToolEntry noteTE = new CombinedTemplateCreationEntry(Messages.PaletteFactory_28,
					Messages.PaletteFactory_29, Note.class, new SimpleFactory(Note.class),
					ImageFactory.getNoteImgDescriptor(),
					ImageFactory.getNoteImgDescriptor());
			noteTE.setToolClass(TemplateCreationTool.class);
			entries.add(noteTE);
		} else {
			//业务实体树
			//实体类型
			ToolEntry entityTE = new CombinedTemplateCreationEntry(Messages.PaletteFactory_30,
					Messages.PaletteFactory_31, Entity.class, new SimpleFactory(Entity.class),
					ImageFactory.getEntityImgDescriptor(),
					ImageFactory.getEntityImgDescriptor());
			entityTE.setToolClass(TemplateCreationTool.class);
			entries.add(entityTE);
			//值类型图标，不显示
			ToolEntry cellTE = new CombinedTemplateCreationEntry(Messages.PaletteFactory_32,
					Messages.PaletteFactory_33, ValueObject.class, new SimpleFactory(
							ValueObject.class),
					ImageFactory.getValueObjectImgDescriptor(),
					ImageFactory.getValueObjectImgDescriptor());
			cellTE.setToolClass(TemplateCreationTool.class);
			entries.add(cellTE);
			cellTE.setVisible(false);// 值类型暂时不放开 @2012-05-28
			//枚举类型
			ToolEntry enumTE = new CombinedTemplateCreationEntry(Messages.PaletteFactory_34,
					Messages.PaletteFactory_35, Enumerate.class, new SimpleFactory(
							Enumerate.class),
					ImageFactory.getEnumImgDescriptor(),
					ImageFactory.getEnumImgDescriptor());
			enumTE.setToolClass(TemplateCreationTool.class);
			entries.add(enumTE);
			//注释类型
			ToolEntry noteTE = new CombinedTemplateCreationEntry(Messages.PaletteFactory_36,
					Messages.PaletteFactory_37, Note.class, new SimpleFactory(Note.class),
					ImageFactory.getNoteImgDescriptor(),
					ImageFactory.getNoteImgDescriptor());
			noteTE.setToolClass(TemplateCreationTool.class);
			entries.add(noteTE);
			//业务接口类型
			ToolEntry busiItfTE = new CombinedTemplateCreationEntry(Messages.PaletteFactory_38,
					Messages.PaletteFactory_39, BusinInterface.class, new SimpleFactory(
							BusinInterface.class),
					ImageFactory.getBusiItfImgDescriptor(),
					ImageFactory.getBusiItfImgDescriptor());
			busiItfTE.setToolClass(TemplateCreationTool.class);
			entries.add(busiItfTE);
		}
		drawer.addAll(entries);
		return drawer;

	}
}
