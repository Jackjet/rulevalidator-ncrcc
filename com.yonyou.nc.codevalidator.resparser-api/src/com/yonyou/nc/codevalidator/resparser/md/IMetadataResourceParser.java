package com.yonyou.nc.codevalidator.resparser.md;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.bpf.IMetaProcessFile;

/**
 * Ԫ������Դ�����������Խ����������ļ���ʽbmf��bpf
 * @author mazhqa
 * @since V1.0
 */
public interface IMetadataResourceParser {
	
	String BMF_FILE_PREFIX = ".bmf";
	String BPF_FILE_PREFIX = ".bpf";
	
	/**
	 * ����Ԫ����bmf�ļ�
	 * @param metadataFile
	 * @return
	 * @throws ResourceParserException
	 * @since V1.0
	 */
	IMetadataFile parseMetadataFile(String metadataFile) throws ResourceParserException;
	
	/**
	 * ����Ԫ����ҵ��bpf�ļ�
	 * @param metaProcessFile
	 * @return
	 * @throws ResourceParserException
	 * @since V2.1
	 */
	IMetaProcessFile parseMetaProcessFile(String metaProcessFile) throws ResourceParserException;

}
