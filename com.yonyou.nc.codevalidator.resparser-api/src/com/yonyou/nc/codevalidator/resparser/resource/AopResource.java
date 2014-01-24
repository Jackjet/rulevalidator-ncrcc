package com.yonyou.nc.codevalidator.resparser.resource;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ResourceType;
import com.yonyou.nc.codevalidator.sdk.aop.AopFileUtils;
import com.yonyou.nc.codevalidator.sdk.aop.AopModuleVO;
import com.yonyou.nc.codevalidator.sdk.aop.AopOperateException;

/**
 * aop资源类型
 * @author mazhqa
 * @since V2.1
 */
public class AopResource extends AbstractFileResource {

	@Override
	public ResourceType getResourceType() {
		return ResourceType.AOP;
	}
	
	public AopModuleVO getAopModuleVo() throws ResourceParserException {
		try {
			return AopFileUtils.loadAopFile(resourcePath);
		} catch (AopOperateException e) {
			throw new ResourceParserException(e);
		}
	}

	@Override
	public void accept(IResourceVisitor resourceVisitor) throws ResourceParserException {
		
	}

}
