package com.yonyou.nc.codevalidator.plugin.domain.mm.vo;

import java.io.Serializable;

/**
 * 单据模板表体VO，用来存放数据
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
public class MmBillTempletBodyVO implements Serializable {

	/**
	 * ID
	 */
	private static final long serialVersionUID = 632575717346311221L;

	public static final String PK_BILLTEMPLET_B = "pk_billtemplet_b";
	public static final String PK_BILLTEMPLET = "pk_billtemplet";
	public static final String ITEMKEY = "itemkey";
	public static final String POS = "pos";
	public static final String WIDTH = "width";
	public static final String SHOWORDER = "showorder";
	public static final String REFTYPE = "reftype";
	public static final String LOCKFLAG = "lockflag";
	public static final String DEFAULTSHOWNAME = "defaultshowname";
	public static final String EDITFLAG = "editflag";
	public static final String DATATYPE = "datatype";
	public static final String INPUTLENGTH = "inputlength";
	public static final String USERDEFINE3 = "userdefine3";
	public static final String SHOWFLAG = "showflag";
	public static final String USERDEFINE2 = "userdefine2";
	public static final String USERDEFINE1 = "userdefine1";
	public static final String TOTALFLAG = "totalflag";
	public static final String USEREDITFLAG = "usereditflag";
	public static final String LOADFORMULA = "usereditflag";
	public static final String EDITFORMULA = "usereditflag";
	public static final String IDCOLNAME = "idcolname";
	public static final String NULLFLAG = "nullflag";
	public static final String USERSHOWFLAG = "usershowflag";
	public static final String USERFLAG = "userflag";
	public static final String CARDFLAG = "cardflag";
	public static final String LISTFLAG = "listflag";
	public static final String TABLE_CODE = "table_code";
	public static final String TABLE_NAME = "table_name";
	public static final String FOREGROUND = "foreground";
	public static final String DEFAULTVALUE = "defaultvalue";
	public static final String PK_CORP = "pk_corp";
	public static final String ITEMTYPE = "itemtype";
	public static final String OPTIONS = "options";
	public static final String LEAFFLAG = "leafflag";
	public static final String USERDEFFLAG = "userdefflag";
	public static final String NEWLINEFLAG = "newlineflag";
	public static final String REVISEFLAG = "reviseflag";
	public static final String USERREVISEFLAG = "userreviseflag";
	
	public static final  String LISTSHOWFLAG ="listshowflag";

	public static final  String LISTSHOW ="listshow";

	public static final  String VALIDATEFORMULA ="validateformula";

	public static final  String RESID="resid";
	public static final  String RESID_TABNAME ="resid_tabname";

	public static final  String LIST ="list";

	public static final  String M_SHARETABLECODE="m_shareTableCode";

	public static final  String METADATAPROPERTY="metadataproperty";
	public static final  String METADATAPATH ="metadatapath";
	public static final  String METADATARELATION ="metadatarelation";
	// 是否卡片超连接
	public static final  String HYPERLINKFLAG ="hyperlinkflag";
	// 是否列表超链接
	public static final  String LISTHYPERLINKFLAG ="listHyperlinkflag";


	private String pk_billtemplet_b;
	private String pk_billtemplet;
	private String itemkey;
	private Integer pos;
	private Integer width;
	private Integer showorder;
	private String reftype;
	private Boolean lockflag;
	private String defaultshowname;
	private Boolean editflag;
	private Integer datatype;
	private Integer inputlength;
	private String userdefine3;
	private Boolean showflag;
	private String userdefine2;
	private String userdefine1;
	private Boolean totalflag;
	private Boolean usereditflag;
	private String loadformula;
	private String editformula;
	private String idcolname;
	private Boolean nullflag;
	private Boolean usershowflag;
	private Boolean userflag;
	private Boolean cardflag;
	private Boolean listflag;
	private String table_code;
	private String table_name;
	private Integer foreground;
	private String defaultvalue;
	private String pk_corp;
	private Integer itemtype;
	private String options;
	private String leafflag;
	private String userdefflag;
	private String newlineflag;
	private String reviseflag;
	private String userreviseflag;

	private Boolean listshowflag;

	private String listshow;

	private String validateformula;

	private String resid;
	private String resid_tabname;

	private boolean list;

	private String m_shareTableCode;

	private String metadataproperty;
	private String metadatapath;
	private String metadatarelation;
	// 是否卡片超连接
	private String hyperlinkflag;
	// 是否列表超链接
	private String listHyperlinkflag;

	public String getPk_billtemplet_b() {
		return pk_billtemplet_b;
	}

	public void setPk_billtemplet_b(String pk_billtemplet_b) {
		this.pk_billtemplet_b = pk_billtemplet_b;
	}

	public String getPk_billtemplet() {
		return pk_billtemplet;
	}

	public void setPk_billtemplet(String pk_billtemplet) {
		this.pk_billtemplet = pk_billtemplet;
	}

	public String getItemkey() {
		return itemkey;
	}

	public void setItemkey(String itemkey) {
		this.itemkey = itemkey;
	}

	public Integer getPos() {
		return pos;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getShoworder() {
		return showorder;
	}

	public void setShoworder(Integer showorder) {
		this.showorder = showorder;
	}

	public String getReftype() {
		return reftype;
	}

	public void setReftype(String reftype) {
		this.reftype = reftype;
	}

	public Boolean getLockflag() {
		return lockflag;
	}

	public void setLockflag(Boolean lockflag) {
		this.lockflag = lockflag;
	}

	public String getDefaultshowname() {
		return defaultshowname;
	}

	public void setDefaultshowname(String defaultshowname) {
		this.defaultshowname = defaultshowname;
	}

	public Boolean getEditflag() {
		return editflag;
	}

	public void setEditflag(Boolean editflag) {
		this.editflag = editflag;
	}

	public Integer getDatatype() {
		return datatype;
	}

	public void setDatatype(Integer datatype) {
		this.datatype = datatype;
	}

	public Integer getInputlength() {
		return inputlength;
	}

	public void setInputlength(Integer inputlength) {
		this.inputlength = inputlength;
	}

	public String getUserdefine3() {
		return userdefine3;
	}

	public void setUserdefine3(String userdefine3) {
		this.userdefine3 = userdefine3;
	}

	public Boolean getShowflag() {
		return showflag;
	}

	public void setShowflag(Boolean showflag) {
		this.showflag = showflag;
	}

	public String getUserdefine2() {
		return userdefine2;
	}

	public void setUserdefine2(String userdefine2) {
		this.userdefine2 = userdefine2;
	}

	public String getUserdefine1() {
		return userdefine1;
	}

	public void setUserdefine1(String userdefine1) {
		this.userdefine1 = userdefine1;
	}

	public Boolean getTotalflag() {
		return totalflag;
	}

	public void setTotalflag(Boolean totalflag) {
		this.totalflag = totalflag;
	}

	public Boolean getUsereditflag() {
		return usereditflag;
	}

	public void setUsereditflag(Boolean usereditflag) {
		this.usereditflag = usereditflag;
	}

	public String getLoadformula() {
		return loadformula;
	}

	public void setLoadformula(String loadformula) {
		this.loadformula = loadformula;
	}

	public String getEditformula() {
		return editformula;
	}

	public void setEditformula(String editformula) {
		this.editformula = editformula;
	}

	public String getIdcolname() {
		return idcolname;
	}

	public void setIdcolname(String idcolname) {
		this.idcolname = idcolname;
	}

	public Boolean getNullflag() {
		return nullflag;
	}

	public void setNullflag(Boolean nullflag) {
		this.nullflag = nullflag;
	}

	public Boolean getUsershowflag() {
		return usershowflag;
	}

	public void setUsershowflag(Boolean usershowflag) {
		this.usershowflag = usershowflag;
	}

	public Boolean getUserflag() {
		return userflag;
	}

	public void setUserflag(Boolean userflag) {
		this.userflag = userflag;
	}

	public Boolean getCardflag() {
		return cardflag;
	}

	public void setCardflag(Boolean cardflag) {
		this.cardflag = cardflag;
	}

	public Boolean getListflag() {
		return listflag;
	}

	public void setListflag(Boolean listflag) {
		this.listflag = listflag;
	}

	public String getTable_code() {
		return table_code;
	}

	public void setTable_code(String table_code) {
		this.table_code = table_code;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public Integer getForeground() {
		return foreground;
	}

	public void setForeground(Integer foreground) {
		this.foreground = foreground;
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public Integer getItemtype() {
		return itemtype;
	}

	public void setItemtype(Integer itemtype) {
		this.itemtype = itemtype;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getLeafflag() {
		return leafflag;
	}

	public void setLeafflag(String leafflag) {
		this.leafflag = leafflag;
	}

	public String getUserdefflag() {
		return userdefflag;
	}

	public void setUserdefflag(String userdefflag) {
		this.userdefflag = userdefflag;
	}

	public String getNewlineflag() {
		return newlineflag;
	}

	public void setNewlineflag(String newlineflag) {
		this.newlineflag = newlineflag;
	}

	public String getReviseflag() {
		return reviseflag;
	}

	public void setReviseflag(String reviseflag) {
		this.reviseflag = reviseflag;
	}

	public String getUserreviseflag() {
		return userreviseflag;
	}

	public void setUserreviseflag(String userreviseflag) {
		this.userreviseflag = userreviseflag;
	}

	public Boolean getListshowflag() {
		return listshowflag;
	}

	public void setListshowflag(Boolean listshowflag) {
		this.listshowflag = listshowflag;
	}

	public String getListshow() {
		return listshow;
	}

	public void setListshow(String listshow) {
		this.listshow = listshow;
	}

	public String getValidateformula() {
		return validateformula;
	}

	public void setValidateformula(String validateformula) {
		this.validateformula = validateformula;
	}

	public String getResid() {
		return resid;
	}

	public void setResid(String resid) {
		this.resid = resid;
	}

	public String getResid_tabname() {
		return resid_tabname;
	}

	public void setResid_tabname(String resid_tabname) {
		this.resid_tabname = resid_tabname;
	}

	public boolean isList() {
		return list;
	}

	public void setList(boolean list) {
		this.list = list;
	}

	public String getM_shareTableCode() {
		return m_shareTableCode;
	}

	public void setM_shareTableCode(String m_shareTableCode) {
		this.m_shareTableCode = m_shareTableCode;
	}

	public String getMetadataproperty() {
		return metadataproperty;
	}

	public void setMetadataproperty(String metadataproperty) {
		this.metadataproperty = metadataproperty;
	}

	public String getMetadatapath() {
		return metadatapath;
	}

	public void setMetadatapath(String metadatapath) {
		this.metadatapath = metadatapath;
	}

	public String getMetadatarelation() {
		return metadatarelation;
	}

	public void setMetadatarelation(String metadatarelation) {
		this.metadatarelation = metadatarelation;
	}

	public String getHyperlinkflag() {
		return hyperlinkflag;
	}

	public void setHyperlinkflag(String hyperlinkflag) {
		this.hyperlinkflag = hyperlinkflag;
	}

	public String getListHyperlinkflag() {
		return listHyperlinkflag;
	}

	public void setListHyperlinkflag(String listHyperlinkflag) {
		this.listHyperlinkflag = listHyperlinkflag;
	}

}
