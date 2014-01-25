package com.yonyou.nc.codevalidator.resparser.lang;

public class I18nPropertyElement {

	private String simpValue;
	private String tradValue;
	private String englishValue;
	private String entryName;
	private String fileName;
	private String resId;

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getSimpValue() {
		return simpValue;
	}

	public void setSimpValue(String simpValue) {
		this.simpValue = simpValue;
	}

	public String getTradValue() {
		return tradValue;
	}

	public void setTradValue(String tradValue) {
		this.tradValue = tradValue;
	}

	public String getEnglishValue() {
		return englishValue;
	}

	public void setEnglishValue(String englishValue) {
		this.englishValue = englishValue;
	}

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String filePath) {
		this.entryName = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
