package com.web.pub.task;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;

import com.web.pub.src.service.IWebSrcService;

/**
 * ≤Â»Î‘§÷√Ω≈±æ
 * 
 * @author:sunshine
 * @date:2014-1-21 œ¬ŒÁ3:17:41
 */
public class InitDBSqlTask implements InitializingBean {

	@Resource
	private IWebSrcService webSrcService;

	@Override
	public void afterPropertiesSet() throws Exception {
		webSrcService.initMenuData();
	}

}
