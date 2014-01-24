package com.yonyou.nc.codevalidator.plugin.domain.dmm.other.debug;

import java.io.File;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.yonyou.nc.codevalidator.sdk.upm.UpmOperateException;

public class LaunchFileUtils {
	
	static final String clientPostfix = "_JStarter.launch";// NC Client
	static final String middleWarePostfix = "_Server.launch";// NC MiddleWare
	static final String vmAttibute="org.eclipse.jdt.launching.VM_ARGUMENTS";
	private LaunchFileUtils() {
	}

	public static LaunchFileVO loadAllLanchFile() {
		return null;

	}

	public static LaunchFileVO loadLanchFileByType(String type) {
		return null;
	}

	public static LaunchFileVO[] loadLanchFileByProject(String projectName,String workspace) throws UpmOperateException {
		LaunchFileVO[]vos=new LaunchFileVO[2];
		String clientFilePath = workspace + projectName
				+ LaunchFileUtils.clientPostfix;
		String middleWareFilePath = workspace + projectName
				+ LaunchFileUtils.middleWarePostfix;
		LaunchFileVO clientvo=LaunchFileUtils.loadLanchFileByFilePath(clientFilePath);
		clientvo.setProjectName(projectName);
		clientvo.setDebugName(projectName+"_JStarter");
		LaunchFileVO midllewarevo=LaunchFileUtils.loadLanchFileByFilePath(middleWareFilePath);
		midllewarevo.setProjectName(projectName);
		midllewarevo.setDebugName(projectName+"_Server");
		vos[0]=clientvo;
		vos[1]=midllewarevo;
		return vos;
	}

	public static LaunchFileVO loadLanchFileByFilePath(String filePath)
			throws UpmOperateException {
		LaunchFileVO lanchfilevo = new LaunchFileVO();
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			String type;
			String vm_arguments = "unSet";
			// ½âÎö.LaunchÎÄ¼þ
			document = saxReader.read(new File(filePath));
			if(document==null){
				return  lanchfilevo;
			}
			Node configurationNode = document
					.selectSingleNode("launchConfiguration");

			Element configurationElement = (Element) configurationNode;
			
			Attribute typeAttribute = configurationElement.attribute("type");
			type=typeAttribute==null?"unset":typeAttribute.getValue().trim();
			List<? extends Node> stringAttributNodes = configurationElement
					.selectNodes("stringAttribute");
			if(stringAttributNodes!=null){
				for(Node stringAttributNode : stringAttributNodes){
					Element stringAttributElement = (Element) stringAttributNode;
					Attribute att_key=stringAttributElement.attribute("key");
					String key=att_key==null?"unknown":att_key.getValue().trim();
					if(key.equals(LaunchFileUtils.vmAttibute)){
						Attribute att_value=	stringAttributElement.attribute("value");
						vm_arguments=att_value==null?"unSet":att_value.getValue().trim();
						break;
					}
				}
			}
			lanchfilevo.setType(type);
			lanchfilevo.setVm_arguments(vm_arguments);
			return lanchfilevo;
		} catch (DocumentException e) {
			throw new UpmOperateException(e);
		}

	}
}
