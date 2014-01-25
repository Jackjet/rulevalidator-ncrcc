package ncmdp.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public abstract class DependSearcherSorter {
	private boolean ignoreCircle = false;

	private static Stack<Object> stack = new Stack<Object>();

	List<Object> list = new ArrayList<Object>();

	HashMap<String, List<File>> hashmap = new HashMap<String, List<File>>();

	public DependSearcherSorter(boolean ignoreCircle) {
		super();
		this.ignoreCircle = ignoreCircle;
	}

	public DependSearcherSorter() {
		super();
	}

	public abstract String getIdentity(File obj);

	public abstract List<File> getDepend(File obj) throws Exception;

	public boolean ignoreCircle() {
		return ignoreCircle;
	}

	public List<File> getListDepends(File sourceFile) throws Exception {
		list.clear();
		filePathSet.clear();
		stack.clear();
		sortByDepend1(sourceFile);
		List<File> result = new ArrayList<File>();
		result.addAll(filePathSet);//�ռ����е�
		hashmap.clear();
		return result;
	}

	/**
	 * ��д�ռ��㷨 dingxm @2010-01-25 ��60s->5s FT -_-!��
	 * 
	 * @param obj
	 */
	private HashSet<File> filePathSet = new HashSet<File>();

	private void sortByDepend1(File sourceFile)throws Exception {
		String id = getIdentity(sourceFile);//����ļ��ľ���·��
		if (stack.contains(id)) {//��ʾ�Ѿ��ڴ�����ļ���
			if (!ignoreCircle()) {//��ʾ��֧��ѭ�����ã�������Ȼ������ѭ�����ã��򱨴�
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < stack.size(); i++) {
					sb.append(stack.get(i)).append(",  ");
				}
				sb.append(id);
				throw new Exception("������"+sourceFile.getName()+
						Messages.DependSearcherSorter_1 + sb.toString());
			} else {
				return;
			}
		}
		if (filePathSet.contains(sourceFile)) {//����ļ����а������ļ�
			return;
		} else {//����ļ����в��������ļ�
			filePathSet.add(sourceFile);
			stack.push(id);
			List<File> depends = getDepend(sourceFile);//��ȡ���ļ��������ļ�
			for (int i = 0; i < depends.size(); i++) {
				File dependObj = depends.get(i);
				String nchomePath = NCMDPTool.getNCHome();
				if (dependObj.getAbsoluteFile().getPath()
						.startsWith(nchomePath)) {
					continue;//���Ϊ�м���е�Ԫ�����ļ������账��
				}
				sortByDepend1(dependObj);
			}
			stack.pop();
		}
	}
}
