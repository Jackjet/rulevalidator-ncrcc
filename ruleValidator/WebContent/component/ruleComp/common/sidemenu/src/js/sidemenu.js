define(function(require, exports, module) {
	var category_tpl = require("./sidemenu.tpl");
	var Widget = require("widget");
	var $ = require("$");
	$ = require("jquery.extend")($);
	var request = require("request");
	require('../theme/{theme}/css/sidemenu.css');
	require("jsuri");
	var store = require("store");
	var MenuStore = require("menuStore");

	var SideMenu = Widget.extend({
		initCustAttr : function() {
			this.get("attrs").ecp = G.ctx;
		},
		template : category_tpl,
		loadData : function() {
			// url匹配一级菜单
			var menu = MenuStore.match(location.href);
			if (!menu) {
				var menuId = MenuStore.getCurrent(0);
				menu = MenuStore.get(menuId);
			}
			if (!menu) {
				throw new Error('没有获取到一级菜单');
			}
			
			this.setModel(menu);
		},
		bindEvent : function() {
			var that = this;
			$('div.order_menu ul.menu_group a').live("click", function() {
				// FIXME session过期提示
				// 增加加载中状态
				if ($.browser.msie) {
					$("iframe")[0].contentWindow.document.body.innerHTML = "";
				} else {
					$("iframe")[0].contentWindow.document.documentElement.innerHTML = "";
				}
				if ($(".loading").length == 0) {
					$($("iframe")[0]).before("<div class='loading'></div>");
				}
				// 更改menu选中状态
				$('ul.menu_group a.active').removeClass("active");
				$(this).addClass('active');
				// 设置iframeurl
				var url = new Uri($(this).attr("href"));
				var id = url.getQueryParamValue('id');
				if (id) {
					store.set("id", id);
				}
				var code = url.getQueryParamValue('code');
				if (code) {
					store.set("code", code);
				}
				$($("iframe")).attr({
					src : $(this).attr("href"),
					menuId : $(this).attr('id')
				});
				MenuStore.setCurrent($(this).attr('id'), 1);
			});
//			seajs.on(G.EVENT.SUBNAV_LOADED, function() {
//				that.setActive();
//			});
			// seajs.on("sidemenu_refresh", function() {
			// that.loadData();
			// });
		},

		// 设置活跃的二次菜单
		setActive : function(seq) {
			$('.sidemenu ul.menu_group a.active').removeClass('active');
			var targetMenu = $('.sidemenu ul.menu_group a').eq(seq);
			if (targetMenu && targetMenu.size() > 0) {
				targetMenu.addClass('active');
				MenuStore.setCurrent(targetMenu.attr('id'), 1);
				$(function() {
					var url = targetMenu.attr('href');
					if ($('iframe').attr('src') != url) {
						$('iframe').attr('src', targetMenu.attr('href'));
					}
				});
			}
			
			// 触发面包屑组件绘制
			seajs.emit('subnav@refresh');
			
		},
		afterRender : function() {
			// 设置默认二级菜单
			var seq = request.getParameter("seq");
			if (seq == undefined) {
				// 目标url中没有seq参数 => 从缓存中获取保存的值
				var menuId = MenuStore.getCurrent(1);
				if (menuId) {
					var menu = MenuStore.get(menuId);
					seq = menu.seq - 1;
				} else {
					// 缓存中没有保存二次菜单 => 取第一个菜单
					seq = 0;
				}
			}
			this.setActive(seq);
		}

	});
	module.exports = SideMenu;
});