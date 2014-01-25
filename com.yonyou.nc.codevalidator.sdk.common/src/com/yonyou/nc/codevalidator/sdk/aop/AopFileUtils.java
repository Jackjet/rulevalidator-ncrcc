package com.yonyou.nc.codevalidator.sdk.aop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * NC中的aop工具类
 * @author mazhqa
 * @since V2.1
 */
public final class AopFileUtils {
	
	private AopFileUtils(){
		
	}
	
	/**
	 * 加载Aop文件
	 * @param aopFilePath
	 * @return
	 * @throws AopOperateException
	 */
	public static AopModuleVO loadAopFile(String aopFilePath) throws AopOperateException{
		AopModuleVO aopModuleVo = new AopModuleVO();
		SAXReader saxReader = new SAXReader();
		Document document;
		try{
			document = saxReader.read(new File(aopFilePath));
			Node moduleNode = document.selectSingleNode("module");
			Element moduleElement = (Element) moduleNode;
			String priorityString = moduleElement.attribute("priority").getValue().trim();
			aopModuleVo.setPriority(Integer.parseInt(priorityString));
			Node aopsNode = moduleElement.selectSingleNode("aops");
			List<? extends Node> aspectNodeList = aopsNode.selectNodes("aspect");
			if(aspectNodeList != null){
				List<AopAspectVO> aopAspectVoList = new ArrayList<AopAspectVO>();
				for (Node aspectNode : aspectNodeList) {
					AopAspectVO aopAspectVO = new AopAspectVO();
					Element aspectElement = (Element) aspectNode;
					String implemenationClass = aspectElement.attribute("class").getValue().toString();
					String componentInterface = aspectElement.attribute("component").getValue().toString();
					aopAspectVO.setImplemenationClass(implemenationClass);
					aopAspectVO.setComponentInterface(componentInterface);
					aopAspectVoList.add(aopAspectVO);
				}
				aopModuleVo.setAopAspectVoList(aopAspectVoList);
			}
			return aopModuleVo;
		}catch (DocumentException e) {
			throw new AopOperateException(e);
		}
	}

}
