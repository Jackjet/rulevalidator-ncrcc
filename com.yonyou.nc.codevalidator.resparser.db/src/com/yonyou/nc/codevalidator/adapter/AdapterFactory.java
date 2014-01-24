package com.yonyou.nc.codevalidator.adapter;

import com.yonyou.nc.codevalidator.type.DBConsts;

public abstract class AdapterFactory {

	private AdapterFactory() {
	}

	static public Adapter getAdapter(int dbType) {
        switch (dbType) {
        case DBConsts.DB2:
            return new DB2Adapter();
        case DBConsts.ORACLE:
            return new OracleAdapter();
        case DBConsts.SYBASE:
            return new DB2Adapter();
        case DBConsts.SQLSERVER:
            return new SQLServerAdapter();
        case DBConsts.INFORMIX:
            return new DB2Adapter();
        case DBConsts.POSTGRESQL:
            return new PostgresAdapter();
		case DBConsts.GBASE:
			return new GbaseAdapter();
        default: //ȱʡΪSQLSERVER;
            return new SQLServerAdapter();
        }
    }
}
