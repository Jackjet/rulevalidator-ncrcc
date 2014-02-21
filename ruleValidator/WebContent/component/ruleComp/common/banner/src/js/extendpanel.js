define(function(require, exports, module) {
	require('../theme/{theme}/css/banner.css');
	var $ = require("$");
	seajs.log($);
	$ = require("jquery.cookie")($);
	$ = require("jquery.json2")($);
	var store = require("store");
	var Navigation=require("navigation");
	var Icc = require("icc");
	var customsetting = require("customsetting");
	// 默认引入：组件继承自widget
	var Widget = require("widget");
	// 自定义：组件的html模板
	var template = require("./banner.tpl");
	// 组件的定义 组件名大写
	var Banner = Widget.extend({
		template : template,
		implement : [customsetting],
		initCustAttr : function() {
			// 从cookie中获取用户信息
			if (store.get("user")) {
				this.set("user_name", store.get("user").userName);
			}
			seajs.on("iccEnable",function(){
				$(".J-onlineService").show();
			});
		},
		bindEvent : function() {
			$(".J_logout").live("click", function() {
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
			var topInfo=store.get("topInfo")||{"topInfo":"产品热线：","topInfoContent":"800-400-5662"};
			store.set("topInfo",topInfo);
			$("#topInfo").html(topInfo["topInfo"]);
			$("#topInfoContent").html(topInfo["topInfoContent"]);
			//导航
			new Navigation({
				attrs:{
					menuurl:this.get("menuurl"),
					param:this.get("param")
				},
				renderTo:"nav"
			});
			//icc集成
			new Icc({
				renderTo:"icc"
			});
			//根据个性化设置修改页面样式
			customsetting.Init();
		}
	});
	// 组件对外提供使用
	module.exports = Banner;
});