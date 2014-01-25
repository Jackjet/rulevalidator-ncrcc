package com.yonyou.nc.codevalidator.plugin.domain.mm.util;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.vo.MmBillTempletBodyVO;
import com.yonyou.nc.codevalidator.plugin.domain.mm.vo.MmSystemplateVO;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 模板查询工具类，用来实现三大模板的查询
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
public class MmTempletQueryUtil {
    /**
     * 根据功能节点编码从功能借点默认模板表中查询到默认单据模板注册数据
     * 
     * @param funcode
     * @return
     * @throws RuleBaseException
     * @throws BusinessException
     */
    public static List<MmSystemplateVO> queryBillSystemplateVOList(String funcode, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from pub_systemplate_base where tempstyle = 0 and funnode = '" + funcode
                + "' and (dr = 0 or dr is null)");

        DataSet dataSet = null;
        dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(), ruleExecContext.getRuntimeContext());
        List<MmSystemplateVO> vos = new ArrayList<MmSystemplateVO>();
        if (MMValueCheck.isEmpty(dataSet)) {
            return vos;
        }
        for (DataRow dataRow : dataSet.getRows()) {
            MmSystemplateVO vo = new MmSystemplateVO();
            vo.setFunnode((String) dataRow.getValue(MmSystemplateVO.FUNNODE));
            vo.setNodekey((String) dataRow.getValue(MmSystemplateVO.NODEKEY));
            vo.setTemplateid((String) dataRow.getValue(MmSystemplateVO.TEMPLATEID));
            vo.setLayer((Integer) dataRow.getValue(MmSystemplateVO.LAYER));
            vo.setModuleid((String) dataRow.getValue(MmSystemplateVO.MODULEID));

            vo.setTempstyle((Integer) dataRow.getValue(MmSystemplateVO.TEMPSTYLE));
            vos.add(vo);
        }

        return vos;

    }

    /**
     * 查询出单据模板表体的信息,返回VO中只包含了常用的信息
     * 
     * @param templateid
     * @param ruleExecContext
     * @return
     * @throws RuleBaseException
     */
    public static List<MmBillTempletBodyVO> queryBillTempletBodyVOList(String templateid,
            IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from pub_billtemplet_b where ");
        sql.append("pk_billtemplet ='" + templateid + "'");
        DataSet dataSet = null;
        dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(), ruleExecContext.getRuntimeContext());
        List<MmBillTempletBodyVO> bodyVOs = new ArrayList<MmBillTempletBodyVO>();
        if (MMValueCheck.isEmpty(dataSet)) {
            return bodyVOs;
        }
        for (DataRow dataRow : dataSet.getRows()) {
            MmBillTempletBodyVO bodyVO = new MmBillTempletBodyVO();
            bodyVO.setPk_billtemplet((String) dataRow.getValue(MmBillTempletBodyVO.PK_BILLTEMPLET));
            bodyVO.setItemkey((String) dataRow.getValue(MmBillTempletBodyVO.ITEMKEY));
            bodyVO.setReftype((String) dataRow.getValue(MmBillTempletBodyVO.REFTYPE));
            bodyVO.setMetadatarelation((String) dataRow.getValue(MmBillTempletBodyVO.METADATARELATION));
            boolean editFlag = (Integer) dataRow.getValue(MmBillTempletBodyVO.EDITFLAG) == 1;
            bodyVO.setEditflag(editFlag);

            bodyVO.setHyperlinkflag((String) dataRow.getValue(MmBillTempletBodyVO.HYPERLINKFLAG));
            bodyVO.setListHyperlinkflag((String) dataRow.getValue(MmBillTempletBodyVO.LISTHYPERLINKFLAG));

            bodyVO.setPos((Integer) dataRow.getValue(MmBillTempletBodyVO.POS));
            bodyVO.setListshow((String) dataRow.getValue(MmBillTempletBodyVO.LISTSHOWFLAG));

            bodyVO.setShowflag((Integer) dataRow.getValue(MmBillTempletBodyVO.SHOWFLAG) == 1);
            bodyVO.setListshowflag(((String) dataRow.getValue(MmBillTempletBodyVO.LISTSHOWFLAG)).equals("Y"));
            bodyVO.setPos((Integer) dataRow.getValue(MmBillTempletBodyVO.POS));
            bodyVOs.add(bodyVO);
        }
        return bodyVOs;
    }

}
