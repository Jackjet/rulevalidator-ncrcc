package com.yonyou.nc.codevalidator.resparser.md;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;

/**
 * 元数据资源解析工具类， OSGi自动注入的服务
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
			throw new ResourceParserException("元数据文件解析器未找到，不能解析元数据!");
		}
		return metadataResourceParser.parseMetadataFile(metadataFile);
	}
	
	public static IMetaProcessFile parseMetaProcessFile(String metadataFile) throws ResourceParserException {
		if (metadataResourceParser == null) {
			throw new ResourceParserException("元数据文件解析器未找到，不能解析元数据!");
		}
		return metadataResourceParser.parseMetaProcessFile(metadataFile);
	}

}
