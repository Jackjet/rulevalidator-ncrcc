/**
 * Grid 列扩展组件
 * 
 */
define(function(require, exports, module) {

	/*模板支持*/
	var Handlebars = require("handlebars");
	//常量类
	var Constants = require("constants");
	var Formatter=require("formatter");
	var $ = require('$');
	
	// 加法函数，用来得到精确的加法结果
	function accAdd(arg1, arg2) {
		var r1,r2;
		try{
			r1 = arg1.toString().split(".")[1].length;
		} catch(e) {
			r1 = 0;
		}
		try{
			r2 = arg2.toString().split(".")[1].length;
		} catch(e) {
			r2 = 0;
		}
		var m = Math.pow(10, Math.max(r1,r2));
		return (arg1*m + arg2*m)/m;
	}

	// 乘法函数，用来得到精确的乘法结果
	function accMul(arg1, arg2) {
		var m = 0;
		var s1 = arg1.toString();
		var s2 = arg2.toString();
		try{
			m += s1.split(".")[1].length;
		} catch(e) {}
		try{
			m += s2.split(".")[1].length;
		} catch(e) {}
		return (Number(s1.replace(".", "")) * Number(s2.replace(".", "")))/Math.pow(10, m);
	}

	var EcpGridFormat = {
		RULE_REPORT_LINK : function(value, data) {
			data.G = G;
			var tpl = '<a target="_blank" href="{{G.PAGE.RULE_RECORD_DETAIL}}{{recordId}}">{{executeUnit.prodCode}},{{executeUnit.moduleCode}},{{executeUnit.compCode}}</a>';
			return Handlebars.compile(tpl)(data);
		},
		
		/*JIRA链接*/
		RULE_JIRA_LINK : function(value, data) {
			data.G = G;
			value=data.ruleDefinition.descDetails;
			var showInfo;
			if(value.length>18){
				showInfo=value.substr(0,18)+"...(more)";
			}else{
				showInfo=value;
			}
			var tpl = '<a target="_blank" href="http://ncrcc.yonyou.com/browse/NCRCC-{{ruleDefinition.relatedIssueId}}">'+showInfo+'</a>';
			return Handlebars.compile(tpl)(data);
		},
		
		/*JIRA链接*/
		RULE_UNIT_LINK : function(value, data) {
			data.G = G;
			var tpl = '{{executeUnit.prodCode}} {{executeUnit.moduleCode}} {{executeUnit.compCode}}';
			return Handlebars.compile(tpl)(data);
		},
		/*JIRA链接*/
		RULE_EXE_DETAIL : function(value, data) {
//			var showInfo;
//			if(value.length>20){
//				showInfo=value.substr(0,20);
//				showInfo=showInfo+"...(more)";
//			}else{
//				showInfo=value;
//			}
			var tpl = '<a target="_blank" href="{{G.API.RULE_EXEC_DETAIL}}{{resultId}}">查看明细</a>';
			return Handlebars.compile(tpl)(data);
		},
		/*JIRA链接*/ 
		RULE_REPORT_DEL : function(value, data) {
			var tpl = '<a target="#" class="J-ruleCheck-del" id="{{recordId}}" >删除</a>';
			return Handlebars.compile(tpl)(data);
		},
		
		 
		/**
		 * 订单状态
		 * 添加币符和精度处理
		 */
		RULE_STATUS : function(value, data) {
			var orderStatus = Constants.get("CODENAME_ORDER_STATUS");
			return orderStatus.getName(value);
		},

	};
	module.exports = EcpGridFormat;

});