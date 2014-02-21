package com.web.pub.fw.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.pub.codename.register.CodeNameRegister;
import com.web.pub.comp.combobox.IComboBoxModel;
import com.web.pub.fw.exception.PubBaseException;
import com.web.rule.service.IRuleQryService;

@Controller
@RequestMapping("src/pub")
public class WebPubController {

	@Resource
	private IRuleQryService ruleService;

	/**
	 * 查询下拉框名称信息
	 * 
	 * @param key
	 * @param param
	 * @return
	 * @throws PubBaseException
	 * @author:sunshine
	 * @date:2014-1-16 下午3:22:48
	 */
	@RequestMapping(value = "getCodeName.json", method = RequestMethod.GET)
	public @ResponseBody
	IComboBoxModel getCodeName(@RequestParam(required = true) String key,
			@RequestParam String param) throws PubBaseException {
		return new CodeNameRegister(ruleService).getQueryService(key)
				.getCodeNameModel(param);
	}
}
