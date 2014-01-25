package com.yonyou.nc.codevalidator.rule.executor;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;

/**
 * �Ựִ�н���Ĺ���������ִ����ɺ󣬽�ִ�н�����뵽��manager��
 * @author mazhqa
 * @since V1.0
 */
public final class SessionExecResultManager {

	private List<SoftReference<SessionRuleExecuteResult>> ruleExecResultList;

	private static SessionExecResultManager INSTANCE = new SessionExecResultManager();

	private SessionExecResultManager() {
		ruleExecResultList = new CopyOnWriteArrayList<SoftReference<SessionRuleExecuteResult>>();
	}

	public static SessionExecResultManager getInstance() {
		return INSTANCE;
	}

	public void addResult(SessionRuleExecuteResult result) {
		ruleExecResultList.add(new SoftReference<SessionRuleExecuteResult>(result));
	}
	
//	public void removeResult(SessionRuleExecuteResult result) {
//		ruleExecResultList.remove(result);
//	}
	
	public void removeAllResults() {
		ruleExecResultList.clear();
	}

	public List<SoftReference<SessionRuleExecuteResult>> getRuleExecResultList() {
		return Collections.unmodifiableList(ruleExecResultList);
	}

}
