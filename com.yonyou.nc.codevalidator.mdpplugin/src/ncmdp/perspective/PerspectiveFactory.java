package ncmdp.perspective;

import ncmdp.project.MDPExplorerTreeView;
import ncmdp.views.NCMDPViewSheet;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
/**
 * 创建uapMDP透视图
 * @author wangxmn
 *
 */
public class PerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		//资源管理器空间
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.2f, editorArea);
		//向空间中加入视图
		//其中MDPExplorerTreeView.class.getCanonicalName()为资源管理视图的id
		left.addView(MDPExplorerTreeView.class.getCanonicalName());

		//右侧属性视图空间
		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.8f, editorArea);
		right.addView(IPageLayout.ID_PROP_SHEET);
		
		//左侧资源管理器视图的outline视图空间
		IFolderLayout explorerTreeBottom = layout.createFolder("treebottom", IPageLayout.BOTTOM, 0.6f, MDPExplorerTreeView.class.getCanonicalName());
		explorerTreeBottom.addView(IPageLayout.ID_OUTLINE);

		//下层实体的属性视图空间
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.8f, editorArea);
		bottom.addView(NCMDPViewSheet.class.getCanonicalName());
		
//		layout.addActionSet("com.rational.clearcase.ActionSet");
		
	}

}
