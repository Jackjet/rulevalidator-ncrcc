package com.web.pub.page.handler;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;

/**
 * 管理Result
 * 
 * 1) 相同查询直接沿用原有实例 2) 实例构造默认有3分钟的存活期 3) newSearchId与oldSearchId相等，表示分页查询
 * 4)newSearchId与oldSearchId不相等，表示重新查询
 * 
 * @author zhenghb
 * @date 2012-7-3 下午05:09:56
 */
@Scope(value = "singleton")
public class DBDataPageSearcher {
	private static final Log logger = LogFactory
			.getLog(DBDataPageSearcher.class);
	private final static int aliveTime = Integer.valueOf(System.getProperty(
			"web.page.alivetime", "180"));
	private final ConcurrentHashMap<String, Timer> timerMap = new ConcurrentHashMap<String, Timer>();
	private final ConcurrentHashMap<String, IPageAwareResult> resultMap = new ConcurrentHashMap<String, IPageAwareResult>();
	private static DBDataPageSearcher instance = new DBDataPageSearcher();

	private DBDataPageSearcher() {
	}

	public static DBDataPageSearcher getInstance(IDBQueryHandler newHandler) {
		return instance;
	}

	/**
	 * 
	 * 
	 * @param handler
	 * @param para
	 * @return
	 * @throws Exception
	 * @date 2012-8-16 上午10:43:53
	 */
	public synchronized IPageAwareResult getPageAwareResult(
			IDBQueryHandler handler, IDBQuery para) throws Exception {
		// 搜索标识，判断是重新查询还是分页查询
		String oldSearchId = para.getOldSearchId();
		String newSearchId = para.getNewSearchId();
		// 如果内存中没有匹配的结果，将新的查询结果放入resultMap中
		if (!StringUtils.isEmpty(oldSearchId)
				&& !StringUtils.isEmpty(newSearchId)) {
			// 判断是否为分页查询
			if (newSearchId.equals(oldSearchId)) {
				if (!resultMap.containsKey(newSearchId)) {
					// synchronized (resultMap) {
					if (!resultMap.containsKey(newSearchId)) {
						resultMap.put(newSearchId, new DBDataPageAwareResult(
								para, handler));
					}
					// }
				}
			} else {
				resultMap.remove(oldSearchId);
				timerMap.remove(oldSearchId);
				resultMap.put(newSearchId, new DBDataPageAwareResult(para,
						handler));
			}
		} else if (StringUtils.isEmpty(oldSearchId)) {
			// 首次查询oldSearchId为空
			resultMap
					.put(newSearchId, new DBDataPageAwareResult(para, handler));
		} else {
			logger.error("分页查询必须传递搜索id号!");
			throw new RuntimeException("分页查询必须传递搜索id号!");
		}
		// 安排定时管理
		schedule(newSearchId);
		return resultMap.get(newSearchId);
	}

	/**
	 * 安排定时管理,清除缓存
	 * 
	 * @param searchId
	 * @date 2012-8-16 上午10:43:24
	 */
	public void schedule(final String searchId) {
		Timer timer = null;
		// 翻页查询时，重新安排timer
		if (timerMap.containsKey(searchId)) {
			synchronized (timerMap) {
				timer = timerMap.get(searchId);
				timer.cancel();
				timer.purge();
				timerMap.remove(searchId);
			}
		}
		timer = new Timer(searchId);
		synchronized (timerMap) {
			timer = new Timer(searchId);
			timerMap.put(searchId, timer);
		}
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// clear the search result.
				resultMap.remove(searchId);
				timerMap.remove(searchId);
			}
		}, aliveTime * 1000);
	}
}