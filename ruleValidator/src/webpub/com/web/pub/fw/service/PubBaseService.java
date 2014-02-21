package com.web.pub.fw.service;

import java.io.Reader;
import java.lang.reflect.Array;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;

import com.web.pub.fw.tools.DataConvertUtil;
import com.web.rule.persistence.entity.ExecuteRecordVO;
import com.web.rule.persistence.entity.RuleExecuteResultVO;
import com.web.rule.view.ExecuteRecordView;
import com.web.rule.view.RuleExecuteResultView;

@SuppressWarnings("unchecked")
public class PubBaseService {

	@SuppressWarnings("rawtypes")
	public Object convert(Object srcObjs, Class cls) {
		Object destObjs = null;
		if (srcObjs != null && srcObjs instanceof List) {
			List<Object> srcList = (List<Object>) srcObjs;
			List<Object> destList = new ArrayList<Object>();
			for (Object srcObj : srcList) {
				Object destObj;
				try {
					destObj = cls.newInstance();
					DataConvertUtil.convert(srcObj, destObj);
					destList.add(destObj);
				} catch (InstantiationException e) {
				} catch (IllegalAccessException e) {
				}
			}
			destObjs = destList;
		} else if (srcObjs != null && srcObjs instanceof Array) {

		}
		return destObjs;
	}

	public List<ExecuteRecordView> convert2View(List<ExecuteRecordVO> voList) {
		List<ExecuteRecordView> viewList = null;
		if (voList != null) {
			viewList = new ArrayList<ExecuteRecordView>();
			for (ExecuteRecordVO vo : voList) {
				ExecuteRecordView view = convert2View(vo);
				viewList.add(view);
			}
		}
		return viewList;
	}

	public ExecuteRecordView convert2View(ExecuteRecordVO vo) {
		ExecuteRecordView view = new ExecuteRecordView();
		view.setExeBeginTime(vo.getExeBeginTime());
		view.setExecuteUnit(vo.getExecuteUnit());
		view.setExeEndTime(vo.getExeEndTime());
		view.setRecordId(vo.getRecordId());
		return view;
	}

	public List<RuleExecuteResultView> convert2ReulutView(
			List<RuleExecuteResultVO> voList) {
		List<RuleExecuteResultView> viewList = new ArrayList<RuleExecuteResultView>();
		if (voList != null) {
			for (RuleExecuteResultVO vo : voList) {
				RuleExecuteResultView view = convert2View(vo);
				viewList.add(view);
			}
		}
		return viewList;
	}

	public RuleExecuteResultView convert2View(RuleExecuteResultVO vo) {
		RuleExecuteResultView view = new RuleExecuteResultView();
		view.setPassFlag(vo.getPassFlag());
		view.setResult(vo.getResult());
		view.setResultDetail(vo.getResultDetail());
		view.setResultId(vo.getResultId());
		view.setSpecParams(vo.getSpecParams());
		view.setExecuteRecord(vo.getExecuteRecord());
		view.setExecuteUnit(vo.getExecuteUnit());
		view.setRuleDefinition(vo.getRuleDefinition());
		return view;
	}

	/**
	 * 将Clob转成String ,静态方法
	 * 
	 * @param clob
	 *            字段
	 * @return 内容字串，如果出现错误，返回 null
	 */
	public static String clobToString(Clob clob) {
		if (clob == null)
			return null;

		StringBuffer sb = new StringBuffer(65535);// 64K
		Reader clobStream = null;
		try {
			clobStream = clob.getCharacterStream(1, 10000);
			char[] b = new char[60000];// 每次获取60K
			int i = 0;
			while ((i = clobStream.read(b)) != -1) {
				sb.append(b, 0, i);
			}
		} catch (Exception ex) {
			sb = null;
		} finally {
			try {
				if (clobStream != null) {
					clobStream.close();
				}
			} catch (Exception e) {
			}
		}
		if (sb == null)
			return null;
		else
			return sb.toString();
	}

}
