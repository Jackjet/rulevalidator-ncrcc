package com.yonyou.nc.codevalidator.plugin.domain.mm.script.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonRuleParams;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

/**
 * ��ѯģ��Ĳ�ѯ������Ҫ����UE�淶�����磺���������Ĳ����������������Ĳ��������ַ����Ĳ���������������ƵĲ������ȣ�
 * 
 * @author qiaoyanga
 */
@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "��ѯģ��Ĳ�ѯ������Ҫ����UE�淶�����磺���������Ĳ����������������Ĳ��������ַ����Ĳ���������������ƵĲ������ȣ�", memo = "", solution = "", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, subCatalog = SubCatalogEnum.PS_CONTENTCHECK, relatedIssueId = "139", coder = "qiaoyanga")
public class TestCase00139 extends AbstractScriptQueryRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws ResourceParserException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] funcodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);

        if (funcodes == null || funcodes.length < 1) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), "δ���ò���");
            return result;
        }

        for (String funcode : funcodes) {
            StringBuilder noteBuilder = new StringBuilder();
            String sql =
                    "select templateid from pub_systemplate_base s where isnull(s.dr,0) = 0 "
                            + "and s.tempstyle = 1 and s.funnode = '" + funcode + "'";
            DataSet ds = this.executeQuery(ruleExecContext, sql);
            if (ds.isEmpty()) {
                noteBuilder.append("û�з���ϵͳ��ѯģ�壡");
                continue;
            }
            StringBuilder templateid = new StringBuilder();
            for (DataRow row : ds.getRows()) {
                templateid.append(", ");
                templateid.append("'" + (String) row.getValue("templateid") + "'");

            }
            String intemplateid = templateid.substring(1, templateid.length());
            StringBuilder sb = new StringBuilder();
            sb.append("select pc.field_code,pc.data_type,pc.opera_code ");
            sb.append("from pub_query_condition pc ");
            sb.append("inner join pub_query_templet pt on pc.pk_templet=pt.id where ");
            sb.append("pt.id in (" + intemplateid + ")");
            sb.append(" and isnull(pc.dr,0)=0 and isnull(pt.dr,0)=0");
            DataSet ds1 = this.executeQuery(ruleExecContext, sb.toString());
            if (ds1.isEmpty()) {
                noteBuilder.append("�ڵ����Ĳ�ѯģ�岻���ڣ�");
                continue;
            }
            for (DataRow row1 : ds1.getRows()) {
                // ��������
                String fieldCode = (String) row1.getValue("field_code");
                // ��������
                int dataType = ((Integer) row1.getValue("data_type")).intValue();
                // ����������
                String operCode = (String) row1.getValue("opera_code");
                switch (dataType) {
                // ����
                    case 2:
                        List<String> operList = this.splitByflag(operCode);
                        if (!this.isContain(operList, new String[] {
                            "=", ">", ">=", "<", "<="
                        })) {
                            noteBuilder.append("�������Ͳ�ѯ����������������UE�淶:=@>@>=@<@<=@(����;����;���ڵ���;С��;С�ڵ���)");
                        }
                        break;
                    // ��������
                    case 3:
                        operList = this.splitByflag(operCode);
                        if (!this.isContain(operList, new String[] {
                            "=", "between"
                        })) {
                            noteBuilder.append("�������Ͳ�ѯ����������������UE�淶:between@=@(����;����)");
                        }
                        break;
                    // �߼�
                    case 4:
                        operList = this.splitByflag(operCode);
                        if (!this.isContain(operList, new String[] {
                            "="
                        })) {
                            noteBuilder.append("�߼����Ͳ�ѯ����������������UE�淶:=@(����)");
                        }
                        break;
                    // ����
                    case 5:
                        operList = this.splitByflag(operCode);
                        if (fieldCode.endsWith(".code")) {
                            if (!this.isContain(operList, new String[] {
                                "=", "left like"
                            })) {
                                noteBuilder.append("�����ѯ����������������UE�淶:=@left like@(����;�����)");
                            }
                        }
                        else if (!this.isContain(operList, new String[] {
                            "="
                        }) && !this.isContain(operList, new String[] {
                            "=="
                        })) {
                            noteBuilder.append("�������Ͳ�ѯ����������������UE�淶:=@(����)����==@(����)");
                        }
                        break;
                    // ʱ������
                    case 8:
                        operList = this.splitByflag(operCode);
                        if (!this.isContain(operList, new String[] {
                            "=", "between"
                        })) {
                            noteBuilder.append("ʱ�����Ͳ�ѯ����������������UE�淶:between@=@(����;����)");
                        }
                        break;

                    // ����
                    case 9:
                        operList = this.splitByflag(operCode);
                        if (fieldCode.endsWith(".name") && !this.isContain(operList, new String[] {
                            "=", "left like"
                        })) {
                            noteBuilder.append("���Ʋ�ѯ����������������UE�淶:=@left like@(����;�����)");
                        }
                        break;
                    default:
                        break;
                }
            }
            if (noteBuilder.toString().length() > 0) {
                result.addResultElement(funcode, noteBuilder.toString());
            }
        }
        return result;
    }

    /**
     * �Ѳ�������@���в��
     * 
     * @param operCode
     * @return
     */
    private List<String> splitByflag(String operCode) {
        List<String> list = new ArrayList<String>();
        if (operCode == null || "".equals(operCode)) {
            return list;
        }
        return Arrays.asList(operCode.split("@"));
    }

    private boolean isContain(List<String> list, String[] opers) {
        if (list != null && list.size() > 0 && opers != null && opers.length > 0) {
            for (String oper : opers) {
                if (!list.contains(oper)) {
                    return false;
                }
            }
        }
        return true;

    }
}
