/*
 * 组件示例
 */

define(function(require, exports, module) {

	// 默认引入：组件继承自widget
	var Widget = require("widget");
	// 自定义：组件对应的css
	require('../theme/default/css/buttongroup.css');
	// 自定义：组件的html模板
	var template = require("./buttongroup.tpl");

	var $ = require("$");
	
	// 组件的定义 组件名大写
	var Example = Widget.extend({
		template : template,
		afterRender : function() {
			var that = this;
			/* 添加订单按钮下拉菜单 */
			that.element.find(".btn_add_select").ready(function() {

				// todo: ajax请求后台获得下拉菜单数据JSON对象
				var orderAddMenu = that.get("subbuttons");

				/* 按钮绑定事件生成并下拉菜单 */
				that.element.find(".btn_add_select").bind('click', function() {
					// 下拉菜单定位
					var mrgTop = parseInt($(this).css("marginTop").split("px")[0]);
					if (Math.abs(mrgTop) > $(this).height())
						mrgTop = -(Math.abs(mrgTop) - $(this).height() + 1);
					else
						mrgTop = mrgTop + $(this).height() - 1;

					// 动态生成下拉菜单
					if (orderAddMenu.length > 0) {
						var menuBorderTop = $('<div></div>')
								.addClass("menu_border top");
						var menuList = $("<div></div>").addClass("menu_list");
						var menuBorderBtm = $('<div></div>')
								.addClass("menu_border btm");
						if (orderAddMenu.length == 1) {
							$(menuList).append($('<a></a>')
									.attr(orderAddMenu[0])
									.addClass("menu_item_single")
									.html(orderAddMenu[0].text));
						}
						if (orderAddMenu.length > 1) {
							$(menuBorderTop).append($("<div></div>")
									.addClass("sept_line"));
							$(menuBorderBtm).append($('<div></div>')
									.addClass("sept_line"));
							$(orderAddMenu).each(function() {
								$(menuList).append($('<a></a>')
										.addClass("menu_item_multiple")
										.attr(this).append($('<span></span>')
												.addClass("list_dot").hide())
										.append($('<span></span>')
												.addClass("menu_text")
												.html(this.text)).bind(
												'mouseenter', function() {
													$(this)
															.children(".list_dot")
															.show();
												}).bind('mouseleave',
												function() {
													$(this)
															.children(".list_dot")
															.hide();
												}));
							});
						}
						$(this).after($('<div></div>')
								.addClass("btn_menu hidden").css({
											marginTop : mrgTop
										}).append(menuBorderTop)
								.append(menuList).append(menuBorderBtm)
								.mouseleave(function() {
											$(this).remove()
										}).removeClass("hidden"));
						that.element.find(".btn_menu a").bind('click', function() {
									$(".btn_menu").remove();
								});
					}
					return false;
				});
			});
		}
	});
	// 组件对外提供使用
	module.exports = Example;
});