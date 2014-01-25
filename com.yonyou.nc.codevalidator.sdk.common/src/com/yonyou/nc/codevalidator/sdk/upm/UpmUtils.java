package com.yonyou.nc.codevalidator.sdk.upm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * NC中的upm工具类
 * @author mazhqa
 * @since V1.0
 */
public final class UpmUtils {
	
	private UpmUtils() {
		
	}

	/**
	 * 加载upm文件
	 * 
	 * @param upmFilePath
	 * @return
	 * @throws DocumentException
	 * @since V1.0
	 * V2.0版本更新，读取interfaceName和implementationName时，不进行trim()操作
	 */
	public static UpmModuleVO loadUpmFile(String upmFilePath) throws UpmOperateException {
		UpmModuleVO upmModuleVO = new UpmModuleVO();
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(new File(upmFilePath));
			Node moduleNode = document.selectSingleNode("module");
			Element moduleElement = (Element) moduleNode;
			Attribute moduleNameAttribute = moduleElement.attribute("name");
			String moduleName = moduleNameAttribute == null ? "unknown" : moduleNameAttribute.getValue().trim();
			upmModuleVO.setModuleName(moduleName);

			List<? extends Node> publicNodeList = moduleNode.selectNodes("public");
			if (publicNodeList != null) {
				List<UpmComponentVO> upmComponentVoList = new ArrayList<UpmComponentVO>();
				for (Node publicNode : publicNodeList) {
					List<? extends Node> componentNodes = publicNode.selectNodes("component");
					if (componentNodes != null) {
						for (Node componentNode : componentNodes) {
							UpmComponentVO componentVo = getComponentVo(componentNode);
							upmComponentVoList.add(componentVo);
						}
					}
				}
				upmModuleVO.setPubComponentVoList(upmComponentVoList);
			}
			return upmModuleVO;
		} catch (DocumentException e) {
			throw new UpmOperateException(e);
		}
	}

	private static UpmComponentVO getComponentVo(Node componentNode) {
		UpmComponentVO upmComponentVO = new UpmComponentVO();
		Element componentElement = (Element) componentNode;

		if (componentElement.attribute("priority") != null) {
			upmComponentVO.setPriority(Integer.parseInt(componentElement.attributeValue("priority").trim()));
		}
		if (componentElement.attributeValue("accessProtected") != null) {
			upmComponentVO.setAccessProtected(Boolean
					.valueOf(componentElement.attributeValue("accessProtected").trim()));
		}
		if (componentElement.attributeValue("remote") != null) {
			upmComponentVO.setRemote(Boolean.valueOf(componentElement.attributeValue("remote").trim()));
		}
		if (componentElement.attributeValue("singleton") != null) {
			boolean singleton = Boolean.valueOf(componentElement.attributeValue("singleton").trim());
			upmComponentVO.setSingleton(singleton);
		}
		if (componentElement.attributeValue("cluster") != null) {
			String cluster = componentElement.attributeValue("cluster").trim();
			upmComponentVO.setCluster(cluster);
		}
		if (componentElement.attributeValue("name") != null) {
			String name = componentElement.attributeValue("name").trim();
			upmComponentVO.setName(name);
		}
		if (componentElement.attributeValue("tx") != null) {
			String tx = componentElement.attributeValue("tx").trim();
			upmComponentVO.setTx(tx);
		}
		if (componentElement.attributeValue("supportAlias") != null) {
			boolean supportAlias = Boolean.valueOf(componentElement.attributeValue("supportAlias").trim());
			upmComponentVO.setSupportAlias(supportAlias);
		}
		if (componentElement.selectSingleNode("interface") != null) {
			String interfaceName = componentElement.selectSingleNode("interface").getText().trim();
			upmComponentVO.setInterfaceName(interfaceName);
		}
		if (componentElement.selectSingleNode("implementation") != null) {
			String implementationName = componentElement.selectSingleNode("implementation").getText().trim();
			upmComponentVO.setImplementationName(implementationName);
		}
		return upmComponentVO;
	}

//	/**
//	 * Test in utils, ignore this.
//	 * 
//	 * @param args
//	 * @throws DocumentException
//	 * @throws UpmOperateException
//	 */
//	public static void main(String[] args) throws UpmOperateException {
//		UpmUtils.loadUpmFile("D:/viewroot/NC_6.3_UAP_NEW_dev_2/NC6_UAP_PVOB/NC_UAP_NEW/message/META-INF/addrgroup.upm");
//	}

}
