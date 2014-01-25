package com.yonyou.nc.codevalidator.resparser.md;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.resparser.IMetaDataResourceQueryFactory;
import com.yonyou.nc.codevalidator.resparser.MetadataResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceType;
import com.yonyou.nc.codevalidator.resparser.resource.MetaResType;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.ExecuteUnitUtils;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 元数据资源查询工厂类, 支持两种类型的元数据：bmf和bpf
 * @author mazhqa
 * @since V1.0
 */
public class MetaDataResourceQueryFactory implements IMetaDataResourceQueryFactory {

	@Override
	public ResourceType getType() {
		return ResourceType.METADATA;
	}

	@Override
	public List<MetadataResource> getResource(MetadataResourceQuery resourceQuery) throws RuleBaseException {
		BusinessComponent businessComponent = resourceQuery.getBusinessComponent();
		String metadataPath = ExecuteUnitUtils.getMetadataFolderPath(businessComponent);
		MetaResType metaResType = resourceQuery.getMetaResType();
		File metadataFolder = new File(metadataPath);
		if(metadataFolder.exists()) {
			@SuppressWarnings("rawtypes")
			Collection metadataFiles = FileUtils.listFiles(metadataFolder, metaResType.getMetaFileSuffix(), true);
			List<MetadataResource> result = new ArrayList<MetadataResource>();
			for (Object metadataFileObject : metadataFiles) {
				File metadataFile = (File) metadataFileObject;
				MetadataResource metadataResource = new MetadataResource();
				metadataResource.setResourcePath(metadataFile.getAbsolutePath());
				result.add(metadataResource);
			}
			return result;
		} else {
			return Collections.emptyList();
		}
	}

}
