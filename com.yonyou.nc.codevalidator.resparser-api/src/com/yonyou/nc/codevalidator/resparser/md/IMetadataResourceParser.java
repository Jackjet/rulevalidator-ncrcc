package com.yonyou.nc.codevalidator.resparser.md;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;

/**
 * 元数据资源解析器，可以解析成两种文件格式bmf和bpf
 * @author mazhqa
 * @since V1.0
 */
public interface IMetadataResourceParser {
	
	String BMF_FILE_PREFIX = ".bmf";
	String BPF_FILE_PREFIX = ".bpf";
	
	/**
	 * 解析元数据bmf文件
	 * @param metadataFile
	 * @return
	 * @throws ResourceParserException
	 * @since V1.0
	 */
	IMetadataFile parseMetadataFile(String metadataFile) throws ResourceParserException;
	
	/**
	 * 解析元数据业务活动bpf文件
	 * @param metaProcessFile
	 * @return
	 * @throws ResourceParserException
	 * @since V2.1
	 */
	IMetaProcessFile parseMetaProcessFile(String metaProcessFile) throws ResourceParserException;

}
