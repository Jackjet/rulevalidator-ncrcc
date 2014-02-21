define(function(require, exports, module) {
	var $ = require("$");
	$ = require("jquery.extend")($);
	var request = require("request");
	var Widget = require("widget");
	require('../theme/default/css/subnav.css');
	var template = require("./subnav.tpl");
	var MenuStore = require("menuStore");

	var Example = Widget.extend({
		template : template,
		initCustAttr : function() {
			this.get("attrs").ctx = G.ctx;
		},
		bindEvent : function() {
			parent.seajs.on('subnav@refresh', this.setMenuPath);
		},
		loadData : function() {
			var that = this;
			$(function() {
				that.setMenuPath();
			});
		},
		// 从缓存中获取菜单路径
		setMenuPath:function() {
			var menuIds = MenuStore.getCurrent();
			var menus = [];
			if (menuIds && $.isArray(menuIds)) {
				for (var i = 0; i < menuIds.length; i++) {
					var menuId = menuIds[i];
					if (menuId) {
						menus.push(MenuStore.get(menuId));
					}
				}
			}
			if (menus.length == 1 && menus[0].childrenView) {
				menus.push(menus[0].childrenView[0]);
			} 
			this.setModel(menus);
		}
	});
	module.exports = Example;
});