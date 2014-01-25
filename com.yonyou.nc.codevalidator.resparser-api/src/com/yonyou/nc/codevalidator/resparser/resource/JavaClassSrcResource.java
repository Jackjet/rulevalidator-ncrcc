package com.yonyou.nc.codevalidator.resparser.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;

/**
 * iterator java class src resource from this.
 * 
 * @author mazhqa
 * 
 */
public class JavaClassSrcResource extends CompositeResource {

	public JavaClassSrcResource(BusinessComponent businessComponent) {
		super(businessComponent, businessComponent.getCodePath());
	}

	@Override
	public void accept(IResourceVisitor resourceVisitor) throws ResourceParserException {
		resourceVisitor.visit(this);
	}

	@Override
	public List<IResource> getSubResources() {
		List<IResource> result = new ArrayList<IResource>();
		File srcFolder = new File(resourcePath);
		if (new File(srcFolder, "public").exists()) {
			result.add(new JavaClassPublicResource(businessComponent));
		}
		if (new File(srcFolder, "client").exists()) {
			result.add(new JavaClassClientResource(businessComponent));
		}
		if (new File(srcFolder, "private").exists()) {
			result.add(new JavaClassPrivateResource(businessComponent));
		}
		return result;
	}
}
