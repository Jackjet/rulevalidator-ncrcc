package com.yonyou.nc.codevalidator.sqltrans;

import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 模块: TranslateToSybase.java 
 * 描述: 将SqlServer语句翻译到Sybase语句
 *  
 */

public class TranslateToSybase extends TranslatorObject {
    /**
     * TranslateToSybase 构造子注释。
     */
    public TranslateToSybase() {
        super(SYBASE);
        Logger.setThreadState("nc.bs.mw.sqltrans.TranslateToSybase Over");
    }
}