package com.yonyou.nc.codevalidator.rule.annotation;

/**
 * ��Ŀ����(��������)��
 * 0.�������JC_CODECRITERION("����淶"),JC_CODEDESIGN("������ƣ���ģʽ��"),JC_PERFORMISSUES
 * ("��������"),JC_CONCURRENISSUES("��������"),JC_PREBUILDCHECK("����ǰ���")
 * 1.Ԫ���ݹ���MD_BASESETTING("��������"), MD_FUNCCHECK("Ԫ���ݹ��ܼ��"), MD_EDITOR("Ԫ���ݱ༭��"),
 * MD_PUBLISH("Ԫ���ݷ���"), MD_CODEORSCRIPT("Ԫ���ݴ���&�ű�����")��
 * 2.UI�������ù���UF_MODELAPIORTEMPLATE("ģ��API&ģ��"), UF_LAYOUT("ҳ�沼��"),
 * UF_BTNOREVENT("��ť&�¼�"), UF_CONFIGFILE("�����ļ�")��
 * 3.������jar������JAR_VERSION("jar���汾"), JAR_FUNC("jar������"), JAR_DEPEND("jar������")��
 * 4.�������LANG_METADATA("Ԫ����"), LANG_BILLTEMP("����ģ��"), LANG_QUERYTEMP("��ѯģ��"),
 * LANG_PRINTTEMP("��ӡģ��"), LANG_CODE("�������", CatalogEnum.LANG),
 * LANG_CONFIG("���ö���"), LANG_COMMON("ͨ��(��������)")��
 * 5.����ű�����CS_CONTENTCHECK("�ű����ݼ��(ע����ȱ���ǰ���)"),
 * CS_UNIFORM("һ���Լ��(��Ԫ���ݣ�java��ƥ��)")��
 * 6.Ԥ�ƽű�����PS_CONTENTCHECK("�ű����ݼ�飨ע����ȱ���ǰ��飩"),
 * PS_UNIFORM("һ���Լ�飨��Ԫ���ݣ�java��ƥ�䣩")�� 7.���������ļ�����OCF_UPM("upm"), OCF_LOG("��־"),
 * OCF_EXCHANGEPLAT("�ⲿ����ƽ̨"), OCF_FORMULA("��ʽ����"),
 * 8.web������ع���:WEB_FRAME("Web�������"), WEB_JSP("JSPҳ��"), WEB_JS("JS�ű�"),
 * WEB_CSS("CSS���");
 * 
 * @author luoweid
 * 
 */
public enum SubCatalogEnum {
	// �������
	JC_CODECRITERION("����淶", CatalogEnum.JAVACODE), JC_CODEDESIGN("������ƣ���ģʽ��", CatalogEnum.JAVACODE), JC_PERFORMISSUES(
			"��������", CatalogEnum.JAVACODE), JC_CONCURRENISSUES("��������", CatalogEnum.JAVACODE), JC_PREBUILDCHECK("����ǰ���",
			CatalogEnum.JAVACODE),
	// Ԫ���ݹ���
	MD_BASESETTING("��������", CatalogEnum.METADATA), MD_FUNCCHECK("Ԫ���ݹ��ܼ��", CatalogEnum.METADATA), MD_EDITOR("Ԫ���ݱ༭��",
			CatalogEnum.METADATA), MD_PUBLISH("Ԫ���ݷ���", CatalogEnum.METADATA), MD_CODEORSCRIPT("Ԫ���ݴ���&�ű�����",
			CatalogEnum.METADATA),
	// UI�������ù���
	UF_MODELAPIORTEMPLATE("ģ��API&ģ��", CatalogEnum.UIFACTORY), UF_LAYOUT("ҳ�沼��", CatalogEnum.UIFACTORY), UF_BTNOREVENT(
			"��ť&�¼�", CatalogEnum.UIFACTORY), UF_CONFIGFILE("�����ļ�", CatalogEnum.UIFACTORY),
	// ������jar������
	JAR_VERSION("jar���汾", CatalogEnum.THIRDPARTYJAR), JAR_FUNC("jar������", CatalogEnum.THIRDPARTYJAR), JAR_DEPEND(
			"jar������", CatalogEnum.THIRDPARTYJAR),
	// �������
	LANG_METADATA("Ԫ����", CatalogEnum.LANG), LANG_BILLTEMP("����ģ��", CatalogEnum.LANG), LANG_QUERYTEMP("��ѯģ��",
			CatalogEnum.LANG), LANG_PRINTTEMP("��ӡģ��", CatalogEnum.LANG), LANG_CODE("�������", CatalogEnum.LANG), LANG_CONFIG(
			"���ö���", CatalogEnum.LANG), LANG_COMMON("ͨ��(��������)", CatalogEnum.LANG),
	// ����ű�����
	CS_CONTENTCHECK("�ű����ݼ��(ע����ȱ���ǰ���)", CatalogEnum.CREATESCRIPT), CS_UNIFORM("һ���Լ��(��Ԫ���ݣ�java��ƥ��)",
			CatalogEnum.CREATESCRIPT),
	// Ԥ�ƽű�����
	PS_CONTENTCHECK("�ű����ݼ�飨ע����ȱ���ǰ��飩", CatalogEnum.PRESCRIPT), PS_UNIFORM("һ���Լ�飨��Ԫ���ݣ�java��ƥ�䣩",
			CatalogEnum.PRESCRIPT),
	// ���������ļ�����
	OCF_UPM("upm", CatalogEnum.OTHERCONFIGFILE), OCF_LOG("��־", CatalogEnum.OTHERCONFIGFILE), OCF_EXCHANGEPLAT("�ⲿ����ƽ̨",
			CatalogEnum.OTHERCONFIGFILE), OCF_FORMULA("��ʽ����", CatalogEnum.OTHERCONFIGFILE), OCF_AOP("aop", CatalogEnum.OTHERCONFIGFILE),
	// web������ع���
	WEB_FRAME("Web�������", CatalogEnum.WEBDEVELOP), WEB_JSP("JSPҳ��", CatalogEnum.WEBDEVELOP), WEB_JS("JS�ű�",
			CatalogEnum.WEBDEVELOP), WEB_CSS("CSS���", CatalogEnum.WEBDEVELOP);

	private String name;
	private CatalogEnum catalogEnum;

	private SubCatalogEnum(String name, CatalogEnum catalogEnum) {
		this.name = name;
		this.catalogEnum = catalogEnum;
	}

	public String getName() {
		return name;
	}

	public CatalogEnum getCatalogEnum() {
		return catalogEnum;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * ��ȡ��Ӧ��Ŀ����Ŀ����
	 * 
	 * @param catalogEnum
	 * @return
	 */
	public static SubCatalogEnum[] getSubCatalogByCatalog(CatalogEnum catalogEnum) {
		SubCatalogEnum[] result = null;
		switch (catalogEnum) {
		case JAVACODE:
			result = new SubCatalogEnum[] { SubCatalogEnum.JC_CODECRITERION, SubCatalogEnum.JC_CODEDESIGN,
					SubCatalogEnum.JC_CONCURRENISSUES, SubCatalogEnum.JC_PERFORMISSUES, SubCatalogEnum.JC_PREBUILDCHECK };
			break;
		case METADATA:
			result = new SubCatalogEnum[] { SubCatalogEnum.MD_BASESETTING, SubCatalogEnum.MD_CODEORSCRIPT,
					SubCatalogEnum.MD_EDITOR, SubCatalogEnum.MD_FUNCCHECK, SubCatalogEnum.MD_PUBLISH };
			break;
		case UIFACTORY:
			result = new SubCatalogEnum[] { SubCatalogEnum.UF_BTNOREVENT, SubCatalogEnum.UF_CONFIGFILE,
					SubCatalogEnum.UF_LAYOUT, SubCatalogEnum.UF_MODELAPIORTEMPLATE };
			break;
		case THIRDPARTYJAR:
			result = new SubCatalogEnum[] { SubCatalogEnum.JAR_DEPEND, SubCatalogEnum.JAR_FUNC,
					SubCatalogEnum.JAR_VERSION };
			break;
		case LANG:
			result = new SubCatalogEnum[] { SubCatalogEnum.LANG_BILLTEMP, SubCatalogEnum.LANG_CODE,
					SubCatalogEnum.LANG_COMMON, SubCatalogEnum.LANG_CONFIG, SubCatalogEnum.LANG_METADATA,
					SubCatalogEnum.LANG_PRINTTEMP, SubCatalogEnum.LANG_QUERYTEMP };
			break;
		case CREATESCRIPT:
			result = new SubCatalogEnum[] { SubCatalogEnum.CS_CONTENTCHECK, SubCatalogEnum.CS_UNIFORM };
			break;
		case PRESCRIPT:
			result = new SubCatalogEnum[] { SubCatalogEnum.PS_CONTENTCHECK, SubCatalogEnum.PS_UNIFORM };
			break;
		case OTHERCONFIGFILE:
			result = new SubCatalogEnum[] { SubCatalogEnum.OCF_EXCHANGEPLAT, SubCatalogEnum.OCF_FORMULA,
					SubCatalogEnum.OCF_LOG, SubCatalogEnum.OCF_UPM };
			break;
		case WEBDEVELOP:
			result = new SubCatalogEnum[] { SubCatalogEnum.WEB_CSS, SubCatalogEnum.WEB_FRAME, SubCatalogEnum.WEB_JS,
					SubCatalogEnum.WEB_JSP };
			break;
		default:
			break;
		}
		return result;
	}

}
