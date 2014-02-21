package com.web.pub.fw.editor;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

/**
 * 
 * 
 * @author:sunshine
 * @date:2014-2-11 обнГ1:40:48
 */
public class BigDecimalEditor extends PropertyEditorSupport {

	public BigDecimalEditor() {
	}

	/**
	 * Parse the Date from the given text, using the specified DateFormat.
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if ("null".equals(text) || null == text || "".equals(text)) {
			setValue(BigDecimal.ZERO);
		} else {
			setValue(text);
		}
		return;
	}
}
