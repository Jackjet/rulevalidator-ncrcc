package nc.web.pub.fw.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * 
 * 
 * @author:sunshine
 * @date:2014-1-7 ÏÂÎç4:48:49
 */
public class PubAnnotationHandlerMapping extends
		DefaultAnnotationHandlerMapping {
	public static List<PubHandler> handlerList = new ArrayList<PubHandler>();

	@Override
	protected void registerHandler(String[] urlPaths, String beanName)
			throws BeansException, IllegalStateException {
		// TODO Auto-generated method stub
		super.registerHandler(urlPaths, beanName);
	}

	@Override
	protected void registerHandler(String urlPath, Object handler)
			throws BeansException, IllegalStateException {
		super.registerHandler(urlPath, handler);
	}

	@Override
	protected String[] determineUrlsForHandlerMethods(Class<?> handlerType,
			final boolean hasTypeLevelMapping) {
		RequestMapping hanlerMapping = AnnotationUtils.findAnnotation(
				handlerType, RequestMapping.class);
		String handleName = handlerType.getName();
		Method[] methods = handlerType.getMethods();
		List<PubMapping> mappingList = new ArrayList<PubMapping>();
		for (Method method : methods) {
			RequestMapping mapping = AnnotationUtils.findAnnotation(method,
					RequestMapping.class);
			if (mapping != null) {
				mappingList.add(new PubMapping(mapping.value(), mapping
						.method(), method.getReturnType(), method
						.getParameterTypes()));
			}
		}
		String hanlerUrl = "";
		if (hanlerMapping != null && hanlerMapping.value().length > 0) {
			hanlerUrl = hanlerMapping.value()[0].toString();
		}
		Collections.sort(mappingList);
		handlerList.add(new PubHandler(handleName, hanlerUrl, mappingList));
		return super.determineUrlsForHandlerMethods(handlerType,
				hasTypeLevelMapping);
	}
}
