define(function(require, exports, module) {
	require('../theme/{theme}/css/banner.css');
	var $ = require("$");
	seajs.log($);
	$ = require("jquery.cookie")($);
	$ = require("jquery.json2")($);
	var store = require("store");
	var Navigation=require("navigation");
//	var Icc = require("icc");
	var customsetting = require("customsetting");
	// 默认引入：组件继承自widget
	var Widget = require("widget");
	// 自定义：组件的html模板
	var template = require("./banner.tpl");
	//默认信息获取
	var portletConfig = require("portlet_config");
	// 组件的定义 组件名大写
	var Banner = Widget.extend({
		template : template,
		Implements : [customsetting],
		initCustAttr : function() {
			// 从cookie中获取用户信息
			var user = store.get("user");
			if (store.get("user")) {
				this.set("user_name", user.userName);
			}
//			seajs.on("iccEnable",function(){
//				$(".J-onlineService").show();
//			});
			this.set('isGAdmin',  user.baseDocType == 0 ? true : false);
			//表示使用customsetting设置样式
			this.set("custom",true);
//			this.model.isGAdmin = user.baseDocType == 0 ? true : false;
		},
		bindEvent : function() {
			$(".J_logout").live("click", function() {
				$.ajax({
					type : 'GET',
					url : G.API.WEB_LOGOUT,
					success : function(data) {
						/*if (data.status == "success") {
							window.location = G.PAGE.LOGIN;
						}

						store.clear();*/
					}
				});
				$.ajax({
					type : 'GET',
					url : G.API.LOGOUT,
					success : function(data) {
						if (data.status == "success") {
							window.location = G.PAGE.LOGIN;
						}

						store.clear();
					}
				});
				return false;
			});
		},
		afterRender:function(){
			//导航
			new Navigation({
				attrs:{
					menuurl:this.get("menuurl"),
					param:this.get("param")
				},
				renderTo:"nav"
			});
//			//icc集成
//			new Icc({
//				renderTo:"icc"
//			});
		},
		//根据个性化设置修改LOGO样式
		custom:function(){
			// 设置Logo
			if (store.get("compropsetting")&&store.get("compropsetting").indexsetting.logo_change!="") {
				$(".banner_logo").addClass("banner_logo_change");
				var filename = store.get("compropsetting").indexsetting.logo_change;
				$("head").append("<style>.top_banner .banner_logo_change{background:url('"
								+ this.getImgPath(filename)
								+ "') no-repeat;}</style>");
			}
			//设置头部信息
			var topInfo = portletConfig.topinfo_default;
			if(store.get("compropsetting")&&store.get("compropsetting").indexsetting.topInfo!=""){
				topInfo =store.get("compropsetting").indexsetting.topInfo;
			}
			$("#topInfo").html(topInfo["topInfo"]);
			$("#topInfoContent").html(topInfo["topInfoContent"]);
		}
	});
	// 组件对外提供使用
	module.exports = Banner;
});