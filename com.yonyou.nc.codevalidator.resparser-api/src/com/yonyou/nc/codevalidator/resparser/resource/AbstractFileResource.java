package com.yonyou.nc.codevalidator.resparser.resource;

/**
 * File/Folder based abstract resource.
 * 
 * @author mazhqa
 * @since V1.0
 */
public abstract class AbstractFileResource implements IResource {

	protected String resourcePath;

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	@Override
	public String getResourcePath() {
		return resourcePath;
	}
	
	/**
	 * 获取资源文件名称
	 * 如：D:\\Develop\\GitRepo-nc\\RuleValidator\\test\\rulecheck\\METADATA\\alert\\alertregistry.bmf，获取完成后即为：alertregistry.bmf
	 * @return
	 */
	public String getResourceFileName(){
		if(resourcePath == null){
			return null;
		}
		return resourcePath.substring(resourcePath.lastIndexOf("\\") + 1);
	}

}
