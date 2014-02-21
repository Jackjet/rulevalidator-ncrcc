/**
 * 资源名称自动补全
 * 
 */
define(function(require, exports, module) {
	var $ = require('$');
//	var dialog = require('dialog');
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
		container.autocomplete(G.API.RESOURCE_QUERY, {
			max : 10, // 列表里的条目数
			minChars : 1, // 自动完成激活之前填入的最小字符
			width : 350, // 提示的宽度，溢出隐藏
			scrollHeight : 300, // 提示的高度，溢出显示滚动条
			matchContains : true, // 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
			autoFill : false, // 自动填充
			formatItem : function(row, i, max) {
				return StringUtils.format('%s / %s / %s', [row.resourceCode, row.resourceName, row.url]);
			},
			formatMatch : function(row, i, max) {
				return row.resourceCode + row.resourceName + row.url;
			},
			formatResult : function(row) {
				return row.resourceCode;
			},
			parse : function(data) {
				return $.map(eval(data), function(row) {
					return {
						data : row,
						value : row.resourceCode,
						result : row.resourceCode
					};
				});
			}
		}).result(function(event, row, formatted) {
			option.callback(event, row, formatted);
		});
	};
});