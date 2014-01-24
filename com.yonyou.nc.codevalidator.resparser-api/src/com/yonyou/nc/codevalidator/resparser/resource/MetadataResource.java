package com.yonyou.nc.codevalidator.resparser.resource;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ResourceType;
import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.md.MetadataResourceParserUtils;

/**
 * 元数据资源，存在两种类型：bmf和bpf
 * @author mazhqa
 * @since V1.0
 */
public class MetadataResource extends AbstractFileResource {
	
	@Override
	public void accept(IResourceVisitor resourceVisitor) throws ResourceParserException {
		resourceVisitor.visit(this);
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.METADATA;
	}
	
	/**
	 * 得到当前元数据资源类型
	 * @return
	 */
	public MetaResType getMetaResType(){
		return resourcePath.endsWith(".bmf") ? MetaResType.BMF : MetaResType.BPF;
	}

	/**
	 * 对于bmf文件使用此方法得出其元数据文件对象
	 * @return
	 * @throws ResourceParserException - 资源格式不正确
	 */
	public IMetadataFile getMetadataFile() throws ResourceParserException {
		return MetadataResourceParserUtils.parseMetaDataFile(resourcePath);
	}
	
	/**
	 * 对于bpf文件使用此方法得出其业务操作文件对象
	 * @return
	 * @throws ResourceParserException - 资源格式不正确
	 */
	public IMetaProcessFile getMetaProcessFile() throws ResourceParserException {
		return MetadataResourceParserUtils.parseMetaProcessFile(resourcePath);
	}
}
