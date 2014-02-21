/**
 * 公共常量
 */
define(function(require, exports, module) {
	var $ = require("$");
	var store = require("store");
	var Constants = {
		params : {},
		init : function(key) {
			var theme = "default";
			if (!store.get("context")) {
				var that = this;
				var url = G.API.CONTEXT;
				$.ajax({
					url : url,
					type : 'GET',
					async : false,
					success : function(data, status, response) {
						if (response.responseText != "gotoLogin"
								&& response.responseText != "kickout") {
							that.params = data;
							store.set("context", data);
						}
					}
				});
				/* 往store中写入主题信息 add by songjl */
				$.ajax({
					url : G.API.MANAGEMENT_UICONFIG_QUERY,
					type : 'GET',
					async : false,
					success : function(data, status, response) {
						if (response.responseText != "gotoLogin") {
							for ( var i in data) {
								data[i] != "" ? store.set(i, eval("(" + data[i]
										+ ")")) : store.set(i, "");
							}
						}
					}
				});
			}
			// 根据localStorage里的值绑定当前默认样式
			theme = eval(window.localStorage["themecolor"]) || "default";
			seajs.config({
				vars : {
					'theme' : theme
				}
			});
		},
		get : function(key) {
			if (store.get("context")) {
				return store.get("context")[key];
			}
		}
	};
	Constants.init();

	var user = store.get("user");
	if (user) {
		G.userid = user.userId;
		G.userCode = user.userCode;
	}
	G.ecp = G.ctx;
	G.ecpimages = G.ctx + '/component/ecp/common';// 组件地址
	G.isIntegration = Constants.get("integration");

	// 临时存储空间
	GLOBAL.temp = {};
	// 门户所有组件相关事件
	GLOBAL.EVENT = {
		TAB_ACTIVE : "event_tab_active",// tab组件页签切换
		SUBNAV_LOADED : "event_subnav_loaded",// 面包屑导航加载完毕
		NABNUM_LOADED : "event_nabnum_loaded",// 库存加载完毕
		PRICE_LOADED : "event_price_loaded",// 价格加载完毕
		FAVORITE_ADD : "event_favorite_add",// 收藏添加商品
		FAVORITE_LOADED : "event_favorite_loaded",// 收藏夹加载完毕
		SHOPCART_REMOVE : "event_shopcart_remove",// 购物车删除商品
		SHOPCART_AFTER_REMOVE : "event_shopcart_after_remove",// 购物车删除商品后
		SHOPCART_ADD : "event_shopcart_add",// 购物车添加商品
		SHOPCART_CHECK : "event_shopcart_check",// 去结算
		SHOPCART_AFTER_CLEARALL : "event_shopcart_clearall",// 清空购物车事件
		SUBNAV_REFRESH : "event_subnav_refresh",// subnav刷新
		// 资源查询
		RESOURCE_QUERY : 'event_resource_query',
		'end' : 'end'
	};

	module.exports = Constants;
});