package com.yonyou.nc.codevalidator.resparser.md;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;

/**
 * Ԫ������Դ���������࣬ OSGi�Զ�ע��ķ���
 * @author mazhqa
 * @since V1.0
 */
public class MetadataResourceParserUtils {

	private static IMetadataResourceParser metadataResourceParser;

	public void setMetadataResourceParser(IMetadataResourceParser metadataResourceParser) {
		MetadataResourceParserUtils.metadataResourceParser = metadataResourceParser;
	}

	public static IMetadataFile parseMetaDataFile(String metadataFile) throws ResourceParserException {
		if (metadataResourceParser == null) {
			throw new ResourceParserException("Ԫ�����ļ�������δ�ҵ������ܽ���Ԫ����!");
		}
		return metadataResourceParser.parseMetadataFile(metadataFile);
	}
	
	public static IMetaProcessFile parseMetaProcessFile(String metadataFile) throws ResourceParserException {
		if (metadataResourceParser == null) {
			throw new ResourceParserException("Ԫ�����ļ�������δ�ҵ������ܽ���Ԫ����!");
		}
		return metadataResourceParser.parseMetaProcessFile(metadataFile);
	}

}
