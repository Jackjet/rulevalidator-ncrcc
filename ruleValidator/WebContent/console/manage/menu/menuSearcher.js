/**
 * 菜单项
 * 
 */
define(function(require, exports, module) {
	var $ = require('$');
	$ = require('jquery.autocomplete')($);
	var StringUtils = require('stringUtils');

	exports.init = function(option) {
		option = option || {};
		option.callback = option.callback || function() {
		};
		var container;
		if (option.renderTo) {
			container = $(option.renderTo);
		} else {
			throw new Error('请指定渲染容器');
		}
		if (!container || container.length == 0) {
			throw new Error('没有找到容器');
		}
		container.autocomplete(G.API.MENU_QUERY, {
			max : 10, // 列表里的条目数
			minChars : 1, // 自动完成激活之前填入的最小字符
			width : 350, // 提示的宽度，溢出隐藏
			scrollHeight : 300, // 提示的高度，溢出显示滚动条
			matchContains : true, // 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
			autoFill : false, // 自动填充
			formatItem : function(row, i, max) {
//				return StringUtils.format('<span class="ui-searche-title">%s</span> / <span class="ui-searche-url">%s</span>', [row.menuItemDesc, row.url]);
				return StringUtils.format('%s \ %s', [row.menuTitle, row.url]);
			},
			formatMatch : function(row, i, max) {
				return row.menuTitle + row.resourceView.resourceUrl;
			},
			formatResult : function(row) {
				return row.menuTitle;
			},
			parse : function(data) {
				return $.map(eval(data), function(row) {
					return {
						data : row,
						value : row.resourceView.resourceUrl,
						result : row.menuTitle
					};
				});
			}
		}).result(function(event, row, formatted) {
			option.callback(event, row, formatted);
		});
	};
});