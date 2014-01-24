package com.yonyou.nc.codevalidator.resparser.resource;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ResourceType;
import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.md.MetadataResourceParserUtils;

/**
 * Ԫ������Դ�������������ͣ�bmf��bpf
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
	 * �õ���ǰԪ������Դ����
	 * @return
	 */
	public MetaResType getMetaResType(){
		return resourcePath.endsWith(".bmf") ? MetaResType.BMF : MetaResType.BPF;
	}

	/**
	 * ����bmf�ļ�ʹ�ô˷����ó���Ԫ�����ļ�����
	 * @return
	 * @throws ResourceParserException - ��Դ��ʽ����ȷ
	 */
	public IMetadataFile getMetadataFile() throws ResourceParserException {
		return MetadataResourceParserUtils.parseMetaDataFile(resourcePath);
	}
	
	/**
	 * ����bpf�ļ�ʹ�ô˷����ó���ҵ������ļ�����
	 * @return
	 * @throws ResourceParserException - ��Դ��ʽ����ȷ
	 */
	public IMetaProcessFile getMetaProcessFile() throws ResourceParserException {
		return MetadataResourceParserUtils.parseMetaProcessFile(resourcePath);
	}
}
