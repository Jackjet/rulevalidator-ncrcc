package com.yonyou.nc.codevalidator.resparser.mdadaptor;

import ncmdp.model.JGraph;
import ncmdp.serialize.XMLSerialize;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;
import com.yonyou.nc.codevalidator.resparser.bpfadaptor.MetaProcessFileAdaptor;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataResourceParser;

/**
 * 基于元数据插件（NCMDP)的资源解析器
 * @author mazhqa
 * @since V1.0
 */
public class MetadataResourceParserImpl implements IMetadataResourceParser {

	@Override
	public IMetadataFile parseMetadataFile(String metadataFile) throws ResourceParserException {
		if(!metadataFile.endsWith(BMF_FILE_PREFIX)){
			throw new ResourceParserException(String.format("元数据文件格式错误:%s， 不能解析为元数据文件", metadataFile));
		}
		JGraph metaGraph = XMLSerialize.getInstance().paserXmlToMDP(metadataFile, false);
		return new MetadataFileAdaptor(metaGraph);
	}

	@Override
	public IMetaProcessFile parseMetaProcessFile(String metaProcessFile) throws ResourceParserException {
		if(!metaProcessFile.endsWith(BPF_FILE_PREFIX)){
			throw new ResourceParserException(String.format("元数据文件格式错误:%s，不能解析为业务操作文件", metaProcessFile));
		}
		JGraph metaGraph = XMLSerialize.getInstance().paserXmlToMDP(metaProcessFile, false);
		return new MetaProcessFileAdaptor(metaGraph);
	}

}
