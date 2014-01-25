package ncmdp.serialize;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Constant;
import ncmdp.model.JGraph;
import ncmdp.model.Service;
import ncmdp.model.activity.OpInterface;
import ncmdp.model.property.XMLElement;
import ncmdp.tool.NCMDPTool;
import ncmdp.tool.basic.StringUtil;
import ncmdp.ui.tree.mdptree.MDPFileTreeItem;
import ncmdp.util.MDPLogger;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;

public class ModuleSerialize {
	public static String genBpmFile(JGraph graph, IProject project,
			MDPFileTreeItem item) throws Exception {
		String upmFileName = "";
		List<OpInterface> bos = graph.getAllOpInterface();
		if (bos.size() > 0) {
			File metainfo = new File(project.getLocation().toFile(), "META-INF");
			String relatePath = item.getMDFileRelativePath();
			if (relatePath.toLowerCase().endsWith(Constant.MDFILE_BPF_SUFFIX)) {
				relatePath = relatePath.substring(0,
						relatePath.lastIndexOf(Constant.MDFILE_BPF_SUFFIX));
			}
			relatePath = relatePath.replace('\\', '/').replace('/', '_');
			if (relatePath.startsWith("_"))
				relatePath = relatePath.substring(1);
			File bpmf = new File(metainfo, relatePath + ".upm");
			upmFileName = bpmf.getAbsolutePath();
			File parent = bpmf.getParentFile();
			if (!parent.exists())
				parent.mkdirs();
			if (!bpmf.exists()) {
				bpmf.createNewFile();
			}
			if (!bpmf.canWrite()) {
				NCMDPTool.silentSetWriterable(bpmf.getAbsolutePath());
			}
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(bpmf);
				writeBpmFile(pw, bos, item.getModuleName());
			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
				throw e;
			} finally {
				if (pw != null)
					pw.close();
			}
		}
		return upmFileName;
	}

	private static void writeBpmFile(PrintWriter pw, List<OpInterface> bos,
			String moduleName) {
		pw.println("<?xml version='1.0' encoding='gb2312'?>");
		pw.println("<module name='" + moduleName + "'>");
		pw.println("\t<public>");
		for (int i = 0; i < bos.size(); i++) {
			OpInterface bo = bos.get(i);
			writeFile(pw, bo);
		}
		pw.println("\t</public>");
		pw.println("</module>");
	}

	public static void genBsmFile(JGraph graph, IProject project,
			MDPFileTreeItem item) throws Exception {
		List<Service> services = graph.getAllService();
		if (services.size() > 0) {
			File metainfo = new File(project.getLocation().toFile(), "META-INF");
			String relatePath = item.getMDFileRelativePath();
			if (relatePath.toLowerCase().endsWith(Constant.MDFILE_SUFFIX)) {
				relatePath = relatePath.substring(0,
						relatePath.lastIndexOf(Constant.MDFILE_SUFFIX));
			}
			relatePath = relatePath.replace('\\', '/').replace('/', '_');
			if (relatePath.startsWith("_"))
				relatePath = relatePath.substring(1);
			File bsmf = new File(metainfo, relatePath + ".usm");
			File parent = bsmf.getParentFile();
			if (!parent.exists())
				parent.mkdirs();
			if (!bsmf.canWrite()) {
				NCMDPTool.silentSetWriterable(bsmf.getAbsolutePath());
			}
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(bsmf);
				writeBsmFile(pw, services, item.getModuleName());
			} catch (Exception e) {
				MDPLogger.error(e.getMessage(), e);
				throw e;
			} finally {
				if (pw != null)
					pw.close();
			}
		}
	}

	private static void writeBsmFile(PrintWriter pw, List<Service> services,
			String moduleName) {
		pw.println("<?xml version='1.0' encoding='gb2312'?>");
		pw.println("<module name='" + moduleName + "'>");
		pw.println("\t<public>");
		for (int i = 0; i < services.size(); i++) {
			Service service = services.get(i);
			writeFile(pw, service);
		}
		pw.println("\t</public>");
		pw.println("</module>");
	}

	private static void writeFile(PrintWriter pw, OpInterface bo) {
		pw.print("\t\t<component ");
		pw.print("singleton='");
		pw.print(bo.isSingleton() ? "true" : "false");
		pw.print("' ");
		
		pw.print("remote='");
		pw.print(bo.isRemote() ? "true" : "false");
		pw.print("' ");
		pw.print("tx='");
		pw.print(bo.getTransProp());
		pw.println("'>");
		pw.print("\t\t\t<interface>");
		pw.print(bo.getFullClassName());
		String version = bo.getVersion();
		if (version != null && version.trim().length() > 0) {
			pw.print("@" + version);
		}
		pw.println("</interface>");
		pw.print("\t\t\t<implementation>");
		String curImpl = bo.getImplClsName();
		if (StringUtil.isEmptyWithTrim(curImpl)) {
			MessageDialog.openInformation(NCMDPEditor.getActiveMDPEditor()
					.getSite().getShell(), "提示",
					"注意：当前接口  " + bo.getDisplayName() + "没有指定默认实现类");
		}
		pw.print(curImpl);
		pw.println("</implementation>");
		ArrayList<XMLElement> al = bo.getAlPropertys();
		for (int i = 0; i < al.size(); i++) {
			pw.print(al.get(i).serializedXMLString("\t\t\t") + "\r\n");
		}
		pw.println("\t\t</component>");

	}

	private static File getBsmFilePath(IProject project, String fileName) {
		File metainfo = new File(project.getLocation().toFile(), "META-INF");
		File bsmFile = new File(metainfo, fileName + ".usm");
		return bsmFile;
	}
}
