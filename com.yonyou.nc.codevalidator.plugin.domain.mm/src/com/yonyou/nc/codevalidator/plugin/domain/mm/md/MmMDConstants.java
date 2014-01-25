package com.yonyou.nc.codevalidator.plugin.domain.mm.md;

/**
 * �������������Զ�������һЩ����
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
public final class MmMDConstants {

    private MmMDConstants() {

    }

    public final static String PK_ORG = "pk_org";

    public final static String PK_ORG_V = "pk_org_v";

    public final static String PK_GROUP = "pk_group";

    /**
     * ����OID
     */
    public final static String MATERIALOID = "cmateialid";

    /**
     * ����VID
     */
    public final static String MATERIALOVD = "cmateialvid";

    /**
     * ���ݺ�
     */
    public final static String BILLCODE = "���ݺ�";

    /**
     * DOC��ǩ
     */
    public final static String EXTEND_TAG_DOC = "DOC";

    /**
     * smart��ǩ
     */
    public final static String EXTEND_TAG_SMART = "smart";

    /**
     * �к��ֶ�
     */
    public final static String ROW_NO = "vrowno";

    /**
     * Ԫ�����в��ܰ����ļ��������Ƽ���
     */
    public final static String[] NONSTANDARD_NAMES = new String[] {
        "����", "ID", "PK", "OID", "VID", "����", "�ӱ�"
    };

    /**
     * �����Ϣ�ӿ����Լ���
     */
    public final static String[] AUDIT_INFOS = new String[] {
        "������", "����ʱ��", "����޸���", "����޸�ʱ��"
    };

    /**
     * ��֯��Ϣ�ӿ����Լ���
     */
    public final static String[] ORG_INFOS = new String[] {
        "��֯", "��֯��汾"
    };

    public final static String PK_LOCK = "PK��";

    /**
     * ������Ϣ��д�ӿڱ����Ӧ������
     */
    public final static String[] FLOW_BUSI_ITFS = new String[] {
        "����ID", "���ݺ�", "������֯", "�Ƶ���"
    };

    public final static String[] FLOW_BUSI_ITF_ALL_FIELDS = new String[] {
        "����ID", "���ݺ�", "������֯", "ҵ������", "�Ƶ���", "������", "����״̬", "��������", "����ʱ��", "��������", "��������", "��������pk", "��Դ��������",
        "��Դ����id", "�޶�ö��", "���ݰ汾pk"
    };

    /**
     * IRowNo�ӿ�����ӳ��
     */
    public final static String ROW_INFO = "�к�";

}
