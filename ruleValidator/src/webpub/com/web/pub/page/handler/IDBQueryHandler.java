package com.web.pub.page.handler;

import java.util.List;

public interface IDBQueryHandler {

	List<?> query(IDBQuery query);

	long queryTotalCount(IDBQuery query);
}
