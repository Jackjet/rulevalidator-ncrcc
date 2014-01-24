package com.yonyou.nc.codevalidator.resparser.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.resparser.IJavaResourceQueryFactory;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceType;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;

public class JavaResourceQueryFactory implements IJavaResourceQueryFactory {

	public static final IJavaResourceQueryFactory INSTANCE = new JavaResourceQueryFactory();

	@Override
	public ResourceType getType() {
		return ResourceType.JAVA;
	}

	@Override
	public List<JavaClassResource> getResource(JavaResourceQuery javaResourceQuery) {
		BusinessComponent businessComponent = javaResourceQuery.getBusinessComponent();
		JavaResPrivilege resPrivilege = javaResourceQuery.getResPrivilege();
		String resourcePath = getResourcePath(businessComponent, resPrivilege);
		File sourceFolder = new File(resourcePath);
		if(!sourceFolder.exists()) {
			return Collections.emptyList();
		}
		@SuppressWarnings("rawtypes")
		Collection javaFiles = FileUtils.listFiles(sourceFolder, new String[] { "java" }, true);
		List<JavaClassResource> result = new ArrayList<JavaClassResource>();
		for (Object javaFileObject : javaFiles) {
			File javaFile = (File) javaFileObject;
			JavaClassResource javaClassResource = new JavaClassResource();
			javaClassResource.setResourcePath(javaFile.getAbsolutePath());
			javaClassResource.setContainExtraWrapper(javaResourceQuery.isContainExtraWrapper());

			List<String> classNameFilterList = javaResourceQuery.getClassNameFilterList();
			if (classNameFilterList == null || classNameFilterList.size() == 0
					|| classNameFilterList.contains(javaClassResource.getJavaCodeClassName())) {
				result.add(javaClassResource);
			}
		}
		return result;
	}

	private String getResourcePath(BusinessComponent businessComponent, JavaResPrivilege resPrivilege) {
		switch (resPrivilege) {
		case SRC:
			return businessComponent.getCodePath();
		default:
			return businessComponent.getCodePath(resPrivilege.resFolder());
		}
	}

}
