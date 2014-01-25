package ncmdp.perspective;

import ncmdp.project.MDPExplorerTreeView;
import ncmdp.views.NCMDPViewSheet;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
/**
 * ����uapMDP͸��ͼ
 * @author wangxmn
 *
 */
public class PerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		//��Դ�������ռ�
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.2f, editorArea);
		//��ռ��м�����ͼ
		//����MDPExplorerTreeView.class.getCanonicalName()Ϊ��Դ������ͼ��id
		left.addView(MDPExplorerTreeView.class.getCanonicalName());

		//�Ҳ�������ͼ�ռ�
		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.8f, editorArea);
		right.addView(IPageLayout.ID_PROP_SHEET);
		
		//�����Դ��������ͼ��outline��ͼ�ռ�
		IFolderLayout explorerTreeBottom = layout.createFolder("treebottom", IPageLayout.BOTTOM, 0.6f, MDPExplorerTreeView.class.getCanonicalName());
		explorerTreeBottom.addView(IPageLayout.ID_OUTLINE);

		//�²�ʵ���������ͼ�ռ�
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.8f, editorArea);
		bottom.addView(NCMDPViewSheet.class.getCanonicalName());
		
//		layout.addActionSet("com.rational.clearcase.ActionSet");
		
	}

}
