package com.yonyou.nc.codevalidator.sqltrans;

import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * ģ��: TranslateToSybase.java 
 * ����: ��SqlServer��䷭�뵽Sybase���
 *  
 */

public class TranslateToSybase extends TranslatorObject {
    /**
     * TranslateToSybase ������ע�͡�
     */
    public TranslateToSybase() {
        super(SYBASE);
        Logger.setThreadState("nc.bs.mw.sqltrans.TranslateToSybase Over");
    }
}