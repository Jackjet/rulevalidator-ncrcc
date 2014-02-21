define(function(require, exports, module) {
	var Uri = require("jsuri");
	G.ctx = "/ruleValidator";
	G.main="/ruleValidator/html/main.html";
	G.web = "/web";
	G.isMobile = false;
	var isMobile = {
		Android : function() {
			return navigator.userAgent.match(/Android/i) ? true : false;
		},
		BlackBerry : function() {
			return navigator.userAgent.match(/BlackBerry/i) ? true : false;
		},
		iOS : function() {
			return navigator.userAgent.match(/iPhone|iPad|iPod/i) ? true : false;
		},
		Windows : function() {
			return navigator.userAgent.match(/IEMobile/i) ? true : false;
		},
		any : function() {
			return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Windows());
		}
	};
	if (isMobile.any()) {
		G.isMobile = true;
	}
	// 全部的ajax 查询api接口
	GLOBAL.API = {
		/**登陆接口*/	
		RANDOMID : G.ctx + "/esfw/pub/randomid.json",// 验证码接口
		LOGIN : G.ctx + "/account/login.json",// 登录接口
		LOGOUT : G.ctx + "/account/logout.json",// 登出接口
		WEB_LOGOUT : G.web + "/account/logout.json",// 登出接口
		FORGETPWD : G.ctx + "/account/forgetPwd.json",// 忘记密码接口	
		
		
		RULE_STATIC:G.ctx+"/rule/rulesatic.json",//规则统计信息
		RULE_RECORD:G.ctx+"/rule/resrecord.json",//执行记录
		RULE_RECORD_DETAIL:G.ctx+"/rule/resrecorddetail.json",//执行记录明细
		RULE_EXEC_DETAIL:G.ctx+"/rule/ruleexedetail.htm?resultId=",//执行记录明细
		RULE_RECORD_DEL:G.ctx+"/rule/delrecord.json?resultId=",//执行记录明细
		RULE_STATUSREPORT:G.ctx+"/rule/rulesatic.json",//执行记录明细
		RULE_PASSRATE:G.ctx+"/rule/rulesatic_pie.json",
		
		/** 用户接口***************************************************** */
		USER_QUERY:G.ctx+"/account/getUser.json",//查询用户信息
		USER_INSERT:G.ctx+"/account/addUser.json",//新增用户
		USER_UPDATE:G.ctx+"/account/updateUser.json",//修改用户
		USER_DELETE:G.ctx+"/account/delUser.json",//删除用户
		USER_UPDATEPWD:G.ctx +'/account/updatePwd.json',// 修改密码
		
		/** 系统管理接口***************************************************** */
		MEANUS_QUERY:G.ctx+"/src/pub/getMenus.json",//查询用户信息
		MEANUS_INSERT:G.ctx+"/src/pub/addMenus.json",//新增用户
		MEANUS_UPDATE:G.ctx+"/src/pub/updateMenus.json",//修改用户
		MEANUS_DELETE:G.ctx+"/src/pub/delMenus.json",//删除用户

		RESOURCE_QUERY:G.ctx+"/src/pub/getResource.json",//查询用户信息
		RESOURCE_INSERT:G.ctx+"/src/pub/addResource.json",//新增用户
		RESOURCE_UPDATE:G.ctx+"/src/pub/updateResource.json",//修改用户
		RESOURCE_DELETE:G.ctx+"/src/pub/delResource.json",//删除用户
		 
		
		
		
		
		
		/** 系统接口***************************************************** */
		CODENAME : G.ctx + "/src/pub/getCodeName.json",// 编码名称查询接口
		
		
		

		/** 公告接口***************************************************** */
		BULLETIN : G.ctx + "/bulletin/validBulletin.json",// 公告查询接口
		BULLETIN_DETAIL : G.ctx + "/bulletin/detail.json",// 公告详细接口

		  
		
		// 模块
		MODULE_INSERT : G.web + '/console/module/insert.json',
		MODULE_DELETE : G.web + '/console/module/delete.json',
		MODULE_UPDATE : G.web + '/console/module/update.json',
		MODULE_DETAIL : G.web + '/console/module/detail.json',
		MODULE_QUERY : G.web + '/console/module/query.json',
		MODULE_CHILDREN : G.web + '/console/module/children.json',


		// 菜单
		MENU_INSERT : G.web + '/console/menu/insert.json',
		MENU_DELETE : G.web + '/console/menu/delete.json',
		MENU_UPDATE : G.web + '/console/menu/update.json',
		MENU_DETAIL : G.web + '/console/menu/detail.json',
		MENU_QUERY : G.web + '/console/menu/query.json',
		MENU_WEB_LOGIN : G.web + '/account/login.json',
		MENU_CHILDREN : G.ctx + '/src/pub/children.json',

		// 后台管理-参数设置
		MANAGEMENT_SYSCONFIG_QUERY : G.ctx + '/esfw/management/systemPorpertiesList.json',// 查询systemprop参数
		MANAGEMENT_SYSCONFIG_UPDATE : G.ctx + '/esfw/management/systemPorpertiesUpdate.json',// 保存systemprop参数
		MANAGEMENT_UIPROPERTIESUPDATE : G.ctx + '/esfw/management/uiPorpertiesUpdate.json',// 保存uiprop参数
		MANAGEMENT_UICONFIG_QUERY : G.ctx + '/esfw/management/uiPorpertiesQuery.json',
		// service
		SERVICE_CONTROLLER : G.ctx + "/esfw/service/ctlService.json",
		SERVICE_CONTROLLER_NOPARAM : G.ctx + "/esfw/service/ctlNoParamService.json",
		SERVICE_CONTROLLER_HASPARAM : G.ctx + "/esfw/service/ctlHasParamService.json",
		SERVICE_BASE_COUNT : G.ctx + "/esfw/service/baseServiceCount.json",
		SERVICE_ECPAPI : G.ctx + "/esfw/service/qryECService.json",
		SERVICE_CONTROLLER_TEST : G.ctx + "/esfw/service/tree.json",

		BILL_TEMPLATE_QUERY : G.ctx + "/channel/bill/template.json",// 查询单据模板

		'结束符' : '请在此前新增代码'
	};
	// 系统url链接
	GLOBAL.PAGE = {
		LOGIN : G.ctx + '/login.html',
		ACCOUNT : G.ctx + "/html/account/account.html",
		ACCOUNT_FAVORITE : G.ctx + "/html/account/account.html?seq=5",
		MANAGMENT_PAGE : G.ctx + "/console/manage/manage.html",
		HELP : G.ctx + '/html/help/help.html',
		MANAGE : G.ctx + '/console/manage/manage.html',
		MAINTAIN_RESOURCE : G.ctx + '/console/manage/resource.html',
		MAINTAIN_MENU : G.ctx + '/console/manage/menu.html',
		
		RULE_RECORD_DETAIL:G.ctx+"/html/rule/subpages/reportdetail.html?recordID=",//执行记录明细
		RULE_JIRA:"http://ncrcc.yonyou.com/browse/NCRCC-",//jira连接
		
		/* 账户中心 */
		USER_EDIT : G.ctx + "/html/account/subpages/user_update.html",// 地址簿编辑页面
		
		/* 系统管理 */
		MENUS_EDIT : G.ctx + "/html/manager/subpages/menus_update.html",// 菜单编辑页面
		RESOURCE_EDIT : G.ctx + "/html/manager/subpages/resource_update.html",// 资源编辑页面

		/** 后台管理 */
		CONSOLE : G.ctx + '/console/manage/manage.html', // 后台管理页面
		MANAGEMENT_LOGOUPLOAD : G.ctx + "/esfw/management/logoupload.json", // 外观-LOGO图片上传
		MANAGEMENT_LOGINLOGOUPLOAD : G.ctx + "/esfw/management/loginlogoupload.json",// 登陆页面LOGO图片上传
		MANAGEMENT_LOGINLEFTUPLOAD : G.ctx + "/esfw/management/loginleftupload.json",// 登陆页面左侧图片上传
		MANAGEMENT_LOGINBGUPLOAD : G.ctx + "/esfw/management/loginbgupload.json",// 登陆页面左侧图片上传
		MANAGEMENT_MANAGE : G.ctx+"/esfw/management/manage.htm",//登录权限校验
		
		
	};
	module.exports = GLOBAL.API;
});