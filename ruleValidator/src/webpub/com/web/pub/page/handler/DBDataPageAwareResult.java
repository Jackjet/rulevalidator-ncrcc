package com.web.pub.page.handler;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库分页查询
 * 
 * @author zhenghb
 * @date 2012-7-3 下午01:20:53
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DBDataPageAwareResult implements IPageAwareResult {
	private final IDBQuery query;

	private final List data;

	private int currentPage = 1;

	public DBDataPageAwareResult(IDBQuery query, IDBQueryHandler handler)
			throws Exception {
		super();
		// 判断非空
		if (query == null)
			throw new RuntimeException("查询条件不能为空!");
		if (query.getCountPerPage() <= 0)
			throw new RuntimeException("每页的数量必须为大于0的整数!");
		this.query = query;

		// 查数据outline
		data = handler.query(query);
	}

	@Override
	public int getTotalPages() {
		if (data == null)
			return 0;
		if (data.size() % query.getCountPerPage() == 0) {
			return data.size() / query.getCountPerPage();
		} else {
			return data.size() / query.getCountPerPage() + 1;
		}
	}

	@Override
	public int getTotalRecords() {
		if (data == null)
			return 0;
		return data.size();
	}

	@Override
	public List firstPage() {
		return toPage(1);
	}

	@Override
	public List lastPage() {
		return toPage(getTotalPages());
	}

	@Override
	public List nextPage() {
		return toPage(currentPage + 1);
	}

	@Override
	public List previousPage() {
		return toPage(currentPage - 1);
	}

	@Override
	public List toPage(int page) {
		List list = new ArrayList();
		int totalPages = getTotalPages();
		if (totalPages > 0) {
			if (page <= 0)
				page = 1;
			// for NCMA interface,when argument 'page' is larger than
			// totalpages,avoid return reduplicate records,return a empty list
			if (page > getTotalPages()) {
				// page = getTotalPages();
				return list;
			}
			this.currentPage = page;

			int countPerPage = query.getCountPerPage();

			int start = (page - 1) * countPerPage;
			int nend = page * countPerPage;
			int end = nend > getTotalRecords() ? getTotalRecords() : nend;
			list = data.subList(start, end);
		}
		return list;
	}
}
