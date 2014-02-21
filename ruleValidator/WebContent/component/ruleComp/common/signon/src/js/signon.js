/*
 * 组件示例
 */

define(function(require, exports, module) {
	if (self.location.href != self.top.location.href&&self.top.location.href.indexOf(G.PAGE.MANAGMENT_PAGE)<0) {
		self.top.location.href = G.PAGE.LOGIN;
	}
	
	require('../theme/{theme}/css/signon.css');
	// 默认引入：组件继承自widget
	var Widget = require("widget");
	// 自定义：组件的html模板
	var template = require("./signon.tpl");
	var $ = require("$");
	var customsetting = require("customsetting");
	//默认信息获取
	var portletConfig = require("portlet_config");
	// 组件的定义 组件名大写
	var Banner = Widget.extend({
		template : template,
		Implements : [customsetting],
		initCustAttr : function() {
			//表示使用customsetting设置样式
			this.set("custom",true);
		},
		afterRender : function() {
			document.title = G.title;
		},
		custom:function(){
			//设置登录下方描述
			var login_alert = portletConfig.loginalert_default;
			var login_tel =portletConfig.logintel_default;
			var login_email = portletConfig.loginemail_default;
			if (store.get("compropsetting")) {
				// 设置Logo
				if (store.get("compropsetting").loginsetting.login_logo!="") {
					$(".signon_logo").addClass("signon_logo_change");
					var filename = store.get("compropsetting").loginsetting.login_logo;
					$("head").append("<style>.signon_logo_change{background-image:url('"
									+ this.getImgPath(filename)
									+ "');}</style>");
				}
				//设置左侧图片
				if (store.get("compropsetting").loginsetting.login_left!="") {
					$(".signon_doc_bg").addClass("signon_doc_bg_change");
					var filename = store.get("compropsetting").loginsetting.login_left;
					$("head").append("<style>.signon_doc_bg_change{background-image:url('"
									+ this.getImgPath(filename)
									+ "');}</style>");
				}
				//设置登录北京图片
				if (store.get("compropsetting").loginsetting.login_bg!="") {
					$(".signon_body_bg").addClass("signon_body_bg_change");
					var filename = store.get("compropsetting").loginsetting.login_bg;
					$("head").append("<style>.signon_body_bg_change{background-image:url('"
									+ this.getImgPath(filename)
									+ "');}</style>");
				}
				//设置登录下方描述
				if(store.get("compropsetting").loginsetting.login_alert!=""){
					login_alert = store.get("compropsetting").loginsetting.login_alert;
				}
				if(store.get("compropsetting").loginsetting.login_tel!=""){
					login_tel = store.get("compropsetting").loginsetting.login_tel;
				}
				if(store.get("compropsetting").loginsetting.login_email!=""){
					login_email = store.get("compropsetting").loginsetting.login_email;
				}
			}
			$(".J-loginalert").html(login_alert);
			$(".J-logintel").html(login_tel);
			$(".J-loginemail").html(login_email);
			$(".J-loginemail").prop("href","mailto:"+login_email);
		}
	});
	// 组件对外提供使用
	module.exports = Banner;
});