package ncmdp.views;

import java.util.ArrayList;
import java.util.List;

import ncmdp.common.MDPConstants;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Attribute;
import ncmdp.model.Cell;
import ncmdp.model.Connection;
import ncmdp.model.Enumerate;
import ncmdp.model.JGraph;
import ncmdp.model.Relation;
import ncmdp.model.Type;
import ncmdp.model.ValueObject;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPCommonUtil;
import ncmdp.util.MDPLogger;
import ncmdp.util.MDPUtil;
import ncmdp.util.ProjectUtil;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;

/**
 * cell属性修改
 * @author wangxmn
 *
 */
public class CellModifier implements ICellModifier {
	private class CellModifiCommand extends Command {
		private String property = ""; 

		private Object value = null;

		private Object oldValue = null;

		private Attribute attr = null;

		public CellModifiCommand(String property, Object value, Attribute attr) {
			super(Messages.CellModifier_1);
			this.property = property;
			this.value = value;
			this.attr = attr;
		}

		@Override
		public void execute() {
			oldValue = getValue(attr, property);
			String curIndustry = NCMDPEditor.getActiveMDPEditor().getModel()
					.getIndustry() == null ? "" : NCMDPEditor 
					.getActiveMDPEditor().getModel().getIndustry().getCode();
//			MDPExplorerTreeView view = MDPExplorerTreeView.getMDPExploerTreeView(null);
			if (!MDPConstants.BASE_INDUSTRY.equalsIgnoreCase(curIndustry)) {
				attr.setIndustryChanged(true);
//				MDPLogger.info("CellModifier属性所属的实体的id为："+attr.getClassID());
				Cell cell = attr.getParentCell();
				if(cell == null){
//					cell = view.findCellbyId(attr.getClassID());
					cell = ProjectUtil.findCellbyId(attr.getClassID());
					if(cell == null){
						MDPLogger.error("属性所属实体为空");
						return;
					}
				}
				cell.setIndustryChanged(true);
				cell.setModifyIndustry(curIndustry);
				// component
				NCMDPEditor.getActiveMDPEditor().getModel()
						.setIndustryChanged(true);
				NCMDPEditor.getActiveMDPEditor().getModel()
						.setModifyIndustry(curIndustry);
			}
			redo();
		}

		@Override
		public void redo() {
			modifyAttr(attr, property, value, true);
		}

		@Override
		public void undo() {
			modifyAttr(attr, property, oldValue, false);
		}

	}

	public static final String[] colNames = { Messages.CellModifier_3,
			Messages.CellModifier_4, Messages.CellModifier_5,
			Messages.CellModifier_6, Messages.CellModifier_7,
			Messages.CellModifier_8, Messages.CellModifier_9,
			Messages.CellModifier_10, Messages.CellModifier_11,
			Messages.CellModifier_12, Messages.CellModifier_13,
			Messages.CellModifier_14, Messages.CellModifier_15,
			Messages.CellModifier_16, Messages.CellModifier_17,
			Messages.CellModifier_18, Messages.CellModifier_19,
			Messages.CellModifier_20, Messages.CellModifier_21,
			Messages.CellModifier_22, Messages.CellModifier_23,
			Messages.CellModifier_24, Messages.CellModifier_25,
			Messages.CellModifier_26, Messages.CellModifier_27,
			Messages.CellModifier_28, Messages.CellModifier_29,
			Messages.CellModifier_30, Messages.CellModifier_31,
			Messages.CellModifier_32, Messages.CellModifier_33 };

	private CellPropertiesView view = null;

	public CellModifier(CellPropertiesView view) {
		super();
		this.view = view;
	}

	private TreeViewer getTreeViewer() {
		return getPropertiesView().getTv();
	}

	private CellPropertiesView getPropertiesView() {
		return view;
	}

	public boolean canModify(Object element, String property) {
		if (element instanceof ArrayList) {
			return false;
		} else if (element instanceof Attribute) {
			Attribute atr = (Attribute) element;
			JGraph graph = NCMDPEditor.getActiveMDPEditor().getModel();
			if (!MDPCommonUtil.attrCanModify(graph,
					graph.getCellByID(atr.getClassID()), atr)) {
				// 增量开发的源文件,只允许修改下列字段
				if (!(colNames[7].equals(property)/* 参照 */
						|| colNames[8].equals(property)/* 描述 */
						|| colNames[23].equals(property)/* 多语资源id */
						|| colNames[24].equals(property)/* 扩展标签 */
						|| colNames[28].equals(property) /* 使用权 */
				|| colNames[29].equals(property)/* 使用权组 */)) {
					return false;
				}
			}
			if (colNames[4].equals(property)) {
				Object o = getPropertiesView().getCellPart().getModel();
				if (o instanceof ValueObject) {
					ValueObject vo = (ValueObject) o;
					List<Connection> cons = vo.getSourceConnections();
					for (int i = 0; i < cons.size(); i++) {
						Connection con = cons.get(i);
						if (con instanceof Relation
								&& atr.equals(((Relation) con)
										.getSrcAttribute())) {
							return false;
						}
					}
				}
			} else if (colNames[28].equals(property)
					|| colNames[29].equals(property)) {
				if (NCMDPTool.isBaseType(atr.getType().getTypeId())) {
					return false;
				}
				return true;
			}
		}
		return true;
	}

	public Object getValue(Object element, String property) {
		if (element instanceof Attribute) {
			Attribute prop = (Attribute) element;
			if (colNames[1].equals(property)) {
				return prop.getName() == null ? "" : prop.getName(); 
			} else if (colNames[2].equals(property)) {
				return prop.getDisplayName() == null ? "" : prop 
						.getDisplayName();
			} else if (colNames[3].equals(property)) {
				String type = prop.getTypeStyle();
				return type;
			} else if (colNames[4].equals(property)) {
				return prop.getType();
			} else if (colNames[5].equals(property)) {
				return prop.getFieldName();
			} else if (colNames[6].equals(property)) {
				return prop.getFieldType();
			} else if (colNames[7].equals(property)) {
				return prop.getRefModuleName();
			} else if (colNames[8].equals(property)) {
				return prop.getDesc() == null ? "" : prop.getDesc(); 
			} else if (colNames[9].equals(property)) {
				return new Boolean(prop.isHide());
			} else if (colNames[10].equals(property)) {
				return new Boolean(prop.isNullable());
			} else if (colNames[11].equals(property)) {
				return new Boolean(prop.isReadOnly());
			} else if (colNames[12].equals(property)) {
				return prop.getAccessStrategy() == null ? "" : prop 
						.getAccessStrategy();
			} else if (colNames[13].equals(property)) {
				return prop.getVisibilityKind();
			} else if (colNames[14].equals(property)) {
				return prop.getDefaultValue() == null ? "" : prop 
						.getDefaultValue();
			} else if (colNames[15].equals(property)) {
				return prop.getMaxValue();
			} else if (colNames[16].equals(property)) {
				return prop.getMinValue();
			} else if (colNames[17].equals(property)) {
				return prop.getLength();
			} else if (colNames[18].equals(property)) {
				return prop.getPrecision();
			} else if (colNames[19].equals(property)) {
				return new Boolean(prop.isFixLen());
			} else if (colNames[20].equals(property)) {
				return new Boolean(prop.isCalculateAttr());
			} else if (colNames[21].equals(property)) {
				return new Boolean(prop.isActive());
			} else if (colNames[22].equals(property)) {
				return new Boolean(prop.isAuthorization());
			} else if (colNames[23].equals(property)) {
				return prop.getResid();
			} else if (colNames[24].equals(property)) {
				// 注：借用help字段来表示“扩展标签”
				return prop.getHelp();
			} else if (colNames[25].equals(property)) {
				return prop.getIsSequence();
			} else if (colNames[26].equals(property)) {
				return prop.getIsDynamicAttr();
			} else if (colNames[27].equals(property)) {
				return prop.getDynamicTable();
			} else if (colNames[28].equals(property)) {
				return new Boolean(prop.isAccessPower());
			} else if (colNames[29].equals(property)) {
				return prop.getAccessPowerGroup();
			} else if (colNames[30].equals(property)) {
				return new Boolean(prop.isForLocale());
			}
		}
		return ""; 
	}

	private boolean isOnlyName(TreeItem item, TreeItem selfItem, String name) {
		boolean isOnly = true;
		int count = item.getItemCount();
		for (int i = 0; i < count; i++) {
			TreeItem ti = item.getItem(i);
			Object o = ti.getData();
			if (o instanceof Attribute && !ti.equals(selfItem)) {
				Attribute attr = (Attribute) o;
				if (name != null && name.equals(attr.getName())) {
					isOnly = false;
					break;
				}
			} else if (ti.getItemCount() > 0) {
				isOnly = isOnlyName(ti, selfItem, name);
				if (!isOnly)
					break;
			}
		}
		return isOnly;
	}

	public void modify(Object element, String property, Object value) {
		TreeItem item = (TreeItem) element;
		Object o = item.getData();
		if (o instanceof Attribute) {
			Attribute prop = (Attribute) o;
			if (colNames[1].equals(property)) {
				String newName = (String) value;
				if (!isOnlyName(getTreeViewer().getTree().getTopItem(), item,
						newName)) {
					return;
				}
			} else if (colNames[4].equals(property)) {
				Type type = (Type) value;
				if ("BS000010000100001058".equalsIgnoreCase(type.getTypeId())) { 
					prop.setForLocale(true);
				}
			}
			Object old = getValue(prop, property);
			if (old != null && old.equals(value))
				return;
			CellModifiCommand cmd = new CellModifiCommand(property, value, prop);
			NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
			if (editor != null)
				editor.executComand(cmd);
		}
	}
	private boolean isEquals(Type oldtype, Type newtype) {
		if (oldtype == null && newtype == null) {
			return true;
		} else if (oldtype != null && newtype != null) {
			return oldtype.getTypeId().equals(newtype.getTypeId());
		} else {
			return false;
		}
	}

	private void modifyAttr(Attribute prop, String property, Object value,
			boolean isReDo) {

		if (colNames[1].equals(property)) {
			String newName = (String) value;
			prop.setName(newName);
		} else if (colNames[2].equals(property)) {
			prop.setDisplayName((String) value);
		} else if (colNames[3].equals(property)) {
			prop.setTypeStyle((String) value);
		} else if (colNames[4].equals(property)) {
			Type type = (Type) value;
			Type oldType = prop.getType();
			prop.setType(type);
			if (!isEquals(oldType, type)) {
				Cell cell = null;
				if (type != null) {
					cell = type.getCell();
				}
				if (cell != null && cell instanceof Enumerate) {
					Enumerate enumerate = (Enumerate) cell;
					Type retrType = enumerate.getReturnType();
					if (retrType != null && retrType.getDbType() != null) {
						prop.setFieldType(retrType.getDbType());
						prop.setLength(retrType.getLength());
						prop.setPrecision(retrType.getPrecise());
					}
				} else {
					prop.setFieldType(type.getDbType());
					if (!NCMDPTool.isBaseType(type.getTypeId())
							&& "REF".equalsIgnoreCase(prop.getTypeStyle())) { 
						prop.setLength("20"); 
					} else {
						prop.setLength(type.getLength());
					}

					prop.setPrecision(type.getPrecise());
				}
				if (type != null && !NCMDPTool.isBaseType(type.getTypeId())) {
					if (cell != null && cell instanceof Enumerate) {
						prop.setTypeStyle("SINGLE"); 
					} else {
						prop.setTypeStyle("REF"); 
						if (prop.isNullable()) {
							// 引用类型属性，如果可为空，则必须为varchar，这里先设置个默认值，然后保存时还会校验
							prop.setFieldType("varchar"); 
						}
					}
				}
			}
			getPropertiesView().updateCanzhaoCellEditor(prop);

		} else if (colNames[5].equals(property)) {
			prop.setFieldName((String) value);
			//
		} else if (colNames[6].equals(property)) {
			prop.setFieldType((String) value);
		} else if (colNames[7].equals(property)) {
			prop.setRefModuleName((String) value);
		} else if (colNames[8].equals(property)) {
			prop.setDesc((String) value);
			//
		} else if (colNames[9].equals(property)) {
			prop.setHide(((Boolean) value).booleanValue());
			//
		} else if (colNames[10].equals(property)) {
			prop.setNullable(((Boolean) value).booleanValue());
			//
		} else if (colNames[11].equals(property)) {
			prop.setReadOnly(((Boolean) value).booleanValue());
			//
		} else if (colNames[12].equals(property)) {
			prop.setAccessStrategy((String) value);
		} else if (colNames[13].equals(property)) {
			prop.setVisibilityKind((String) value);
		} else if (colNames[14].equals(property)) {
			prop.setDefaultValue((String) value);
		} else if (colNames[15].equals(property)) {
			prop.setMaxValue((String) value);
		} else if (colNames[16].equals(property)) {
			prop.setMinValue((String) value);
			//
		} else if (colNames[17].equals(property)) {
			prop.setLength((String) value);
			//
		} else if (colNames[18].equals(property)) {
			prop.setPrecision((String) value);
			//
		} else if (colNames[19].equals(property)) {
			prop.setFixLen(((Boolean) value).booleanValue());
			//
		} else if (colNames[20].equals(property)) {
			prop.setCalculateAttr(((Boolean) value).booleanValue());
			//
		} else if (colNames[21].equals(property)) {
			prop.setActive(((Boolean) value).booleanValue());
			//
		} else if (colNames[22].equals(property)) {
			prop.setAuthorization(((Boolean) value).booleanValue());
			//
		} else if (colNames[23].equals(property)) {
			prop.setResid((String) value);
		} else if (colNames[24].equals(property)) {
			// 注：借用help字段来表示“扩展标签”
			prop.setHelp((String) value);
		} else if (colNames[25].equals(property)) {
			prop.setIsSequence(((Boolean) value).booleanValue());
		} else if (colNames[26].equals(property)) {
			prop.setIsDynamicAttr(((Boolean) value).booleanValue());
		} else if (colNames[27].equals(property)) {
			prop.setDynamicTable((String) value);
		} else if (colNames[28].equals(property)) {
			prop.setAccessPower(((Boolean) value).booleanValue());
		} else if (colNames[29].equals(property)) {
			prop.setAccessPowerGroup((String) value);
		} else if (colNames[30].equals(property)) {
			prop.setForLocale((((Boolean) value).booleanValue()));
		}
		getTreeViewer().refresh(prop);
		((ValueObject) getPropertiesView().getCellPart().getModel())
				.firePropUpdate(prop);

		if (isReDo) {
			// 修改属性 ，需要调整行业设置
			JGraph graph = NCMDPEditor.getActiveMDPEditor().getModel();
			if (!MDPCommonUtil.attrCanModify(graph,
					graph.getCellByID(prop.getClassID()), prop)) {// 增量开发的
				if(MDPUtil.getDevVersionType()==null){
					return;
				}
				// 源文件中的属性,只允许修改属性模型的部分内容
				// 修改了之后，需要调整 行业相关信息
				graph.setIndustryChanged(true);
				graph.getCellByID(prop.getClassID()).setIndustryChanged(true);
				prop.setIndustryChanged(true);// 是否被修改了
				prop.setVersionType(MDPUtil.getDevVersionType());
			}
		}
	}
}
