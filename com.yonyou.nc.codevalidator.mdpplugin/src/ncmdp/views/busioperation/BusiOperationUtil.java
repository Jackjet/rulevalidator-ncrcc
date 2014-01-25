package ncmdp.views.busioperation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ncmdp.cache.MDPCachePool;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Cell;
import ncmdp.model.JGraph;
import ncmdp.model.activity.BusiOperation;
import ncmdp.model.activity.OpInterface;
import ncmdp.model.activity.Operation;
import ncmdp.model.activity.RefBusiOperation;
import ncmdp.model.activity.RefOperation;
import ncmdp.serialize.JGraphSerializeTool;
import ncmdp.tool.basic.StringUtil;
import ncmdp.util.MDPLogger;

public class BusiOperationUtil {
	public static void fillOperationInfo(List inputElementList, boolean isInit) {
		// 填充信息
		JGraph currentJGraph = null;
		if (!isInit) {
			currentJGraph = NCMDPEditor.getActiveMDPEditor().getModel();
		}
		List removeElements = new ArrayList();
		for (Object obj : inputElementList) {
			RefOperation refOp = (RefOperation) obj;
			String opItfID = refOp.getOpItfID();
			if (currentJGraph == null) {
				File file = getReferencedCellSourceFile(opItfID);
				if (file != null) {
					currentJGraph = JGraphSerializeTool.loadFromFile(file);
				}
			}
			Cell referencedCell = currentJGraph.getCellByID(opItfID);
			OpInterface opItf = (OpInterface) referencedCell;
			// if (opItf == null) { throw new RuntimeException("找不到引用的组件，id：" +
			// opItfID); }
			if (opItf == null) {
				removeElements.add(obj);
				continue;
			}
			refOp.setOpItfName(opItf.getDisplayName());
			Operation operation = opItf.getOperationByID(refOp.getId());
			// if (operation == null) { throw new
			// RuntimeException("找不到引用的组件，id：" + opItfID); }
			if (operation == null) {
				removeElements.add(obj);
				continue;
			}
			refOp.setDisplayName(operation.getDisplayName() == null ? ""
					: operation.getDisplayName());
			refOp.setName(operation.getName());
		}
		inputElementList.removeAll(removeElements);
	}

	public static void fillBusiOperationInfo(List<RefBusiOperation> busiopers,
			boolean isInit) {
		// 填充信息
		JGraph currentJGraph = null;
		if (!isInit) {
			currentJGraph = NCMDPEditor.getActiveMDPEditor().getModel();
		}
		List removeElements = new ArrayList();
		List<BusiOperation> toefBusiOperation = null;// getAllToRefBusiOperation()
		for (RefBusiOperation refBusiOp : busiopers) {
			String busiActID = refBusiOp.getBusiActID();
			if (currentJGraph == null) {
				File file = getReferencedCellSourceFile(busiActID);
				if (file != null) {
					currentJGraph = JGraphSerializeTool.loadFromFile(file);
				}
			}
			if (toefBusiOperation == null) {
				toefBusiOperation = currentJGraph.getAllToRefBusiOperation();
			}
			BusiOperation busiOp = null;
			for (BusiOperation busiOptemp : toefBusiOperation) {
				if (busiOptemp.getId().equalsIgnoreCase(
						refBusiOp.getRealBusiOpid())) {
					busiOp = busiOptemp;
				}
			}
			// Cell referencedCell =
			// currentJGraph.getCellByID(refBusiOp.getRealBusiOpid());
			// BusiOperation busiOp = (BusiOperation) referencedCell;
			// if (opItf == null) { throw new RuntimeException("找不到引用的组件，id：" +
			// opItfID); }
			if (busiOp == null) {
				removeElements.add(refBusiOp);
				continue;
			}
			refBusiOp.setName(busiOp.getName());
			refBusiOp.setDisplayName(busiOp.getDisplayName());

		}
		busiopers.removeAll(removeElements);
	}

	private static File getReferencedCellSourceFile(String id) {
		String filePath = MDPCachePool.getInstance()
				.getFilePathByIDAndIndustry(id, null);
		MDPLogger.info(filePath);
		return StringUtil.isEmptyWithTrim(filePath) ? null : new File(filePath);
	}

	public static BusiOperation taransOpitf2BusiOperation(OpInterface opItf) {
		BusiOperation busiOp = new BusiOperation();
		busiOp.setId(opItf.getId());
		busiOp.setName(opItf.getName());
		busiOp.setDisplayName(opItf.getDisplayName());
		busiOp.setAuthorization(true);
		busiOp.setBusiActivity(false);
		return busiOp;
	}

	public static BusiOperation taransOperation2BusiOperation(
			Operation operation) {
		BusiOperation busiOp = new BusiOperation();
		busiOp.setId(operation.getId());
		busiOp.setName(operation.getName());
		busiOp.setDisplayName(operation.getDisplayName());
		busiOp.setAuthorization(true);
		busiOp.setBusiActivity(false);
		return busiOp;
	}
}
