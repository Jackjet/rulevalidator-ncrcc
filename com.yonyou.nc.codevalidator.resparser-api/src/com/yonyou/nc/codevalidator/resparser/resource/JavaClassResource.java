package com.yonyou.nc.codevalidator.resparser.resource;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ResourceType;

/**
 * @author mazhqa
 *
 */
public class JavaClassResource extends AbstractFileResource {
	
	/**
	 * �Ƿ��������İ�װ��������src/public/�µĴ��뻹�ж����folder
	 * @since V2.3
	 */
	private boolean containExtraWrapper = false;

	public enum PRIVILEGE {
		PUBLIC, CLIENT, PRIVATE
	}

	public PRIVILEGE getCodePrivilege() {
		if (resourcePath.toLowerCase().contains("public")) {
			return PRIVILEGE.PUBLIC;
		} else if (resourcePath.toLowerCase().contains("client")) {
			return PRIVILEGE.CLIENT;
		}
		return PRIVILEGE.PRIVATE;
	}

	/**
	 * Convert resourcePath(like
	 * D:\\Documents\\UAP-NC-Doc\\src\\public\\nc\\ui\\test\\Java.java) to
	 * nc.ui.test.Java
	 * 
	 * @return
	 */
	public String getJavaCodeClassName() {
		String replaceDotStr = resourcePath.replace("\\", ".");
		String privilegeStr = replaceDotStr.substring(replaceDotStr.indexOf("src.") + 4);
		String javaStr = privilegeStr.substring(privilegeStr.indexOf(".") + 1);
		String result = javaStr.replace(".java", "");
		if(containExtraWrapper){
			result = result.substring(result.indexOf(".") + 1);
		}
		return result;
	}

	@Override
	public void accept(IResourceVisitor resourceVisitor) throws ResourceParserException {
		resourceVisitor.visit(this);
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.JAVA;
	}

	public boolean isContainExtraWrapper() {
		return containExtraWrapper;
	}

	public void setContainExtraWrapper(boolean containExtraWrapper) {
		this.containExtraWrapper = containExtraWrapper;
	}
	
	/**
	 * ȡjava�������һ��.���������
	 * @return
	 */
	public String getLastJavaClassName() {
		String fullname = getJavaCodeClassName();
		return fullname.substring(fullname.lastIndexOf('.') + 1, fullname.length());
	}
	
}
