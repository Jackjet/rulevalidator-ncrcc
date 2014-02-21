define(function(require, exports, module) {

	require('../theme/{theme}/css/navigation.css');
	var Widget = require("widget");
	var navigation_tpl = require("./navigation.tpl");
	var $ = require("$");
	require("jquery.extend")($);
	var MenuStore = require("menuStore");
	var Navigation = Widget.extend({
		template : navigation_tpl,
		initCustAttr : function() {
			var attrs = this.get("attrs");
			attrs.ctx = G.ctx;
		},
		loadData : function() {
			var menus = MenuStore.getAll();

			// [1] 去除隐藏菜单
			var rawDataIndex = menus.length;
			$(menus).each(function() {
				if (!this.hide) {
					menus.push(this);
				}
			});
			menus.splice(0, rawDataIndex);

			 // [2] 增加序号（菜单序号以0开始，而菜单样式以1开始）
			 $(menus).each(function(i) {
				 this.menuSeq = i + 1;
			 });

			this.setModel(menus);
			seajs.emit("sidemenu_refresh");
		},

		// 设置活跃的一级菜单
		setActive : function(menuSeq) {
			// 去除所有菜单的活跃状态
			$('#navigation a.active').removeClass('active').addClass('normal');
			// 设置活跃
//			$('#navigation a').eq(seq).removeClass('normal').addClass('active');
			$('#navigation a[menuseq=' + menuSeq + ']').removeClass('normal').addClass('active');
			
		},
		afterRender : function() {
			
			// 绑定事件
			var that = this;
			$('#navigation a').off('click').on('click', function() {
				var menuId = $(this).attr('menuid');
				if (!menuId) {
					throw new Error('一级菜单menuId为空');
				}
				var seq = parseInt($(this).attr('menuseq'));
				that.setActive(seq);
				MenuStore.setCurrent(menuId, 0);
				MenuStore.setCurrent(null, 1);
			});
			
			// 通过匹配url来寻找目标菜单
			var menuId = null;
			var menu = MenuStore.match(location.href);
			if (menu) {
				menuId = menu.menuId;
				MenuStore.setCurrent(menu.menuId, 0);
				MenuStore.setCurrent(null, 1);
			}
			// seq => menuSeq
			var menuSeq = 1;
			if (menuId) {
				menuSeq = $('#navigation a[menuid=' + menuId + ']').attr('menuseq');
			}
			this.setActive(menuSeq);
		}
	});

	module.exports = Navigation;
});