package ncmdp.cache;

import java.util.HashMap;

import ncmdp.model.Cell;
import ncmdp.model.JGraph;

/**
 * 对象缓存，在eclipse启动时加载各个文件的信息
 * @author wangxmn
 *
 */
public class MDPUICachePool {

	private HashMap<String, JGraph> compCache = new HashMap<String, JGraph>();
	private HashMap<String, Cell> cellCache = new HashMap<String, Cell>();
	
	
}
