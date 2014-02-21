/**
 * 发票抬头自动补全
 * 
 */
define(function(require, exports, module) {
	var $ = require('$');
	var dialog = require('dialog');
	$ = require('jquery.autocomplete')($);
	var StringUtils = require('stringUtils');

	exports.init = function(option) {
		option = option || {};
		option.callback = option.callback || function() {
		};
		option.className = option.className || 'J-invoice-auto';
		$('.' + option.className).autocomplete(G.API.ACCOUNT_INVOICE, {
			max : 10, // 列表里的条目数
			minChars : 1, // 自动完成激活之前填入的最小字符
			width : 197, // 提示的宽度，溢出隐藏
			scrollHeight : 300, // 提示的高度，溢出显示滚动条
			matchContains : true, // 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
			autoFill : false, // 自动填充
			formatItem : function(row, i, max) {
				if (row.bisdefault) {
					return StringUtils.format('%s（默认）', [row.vinvoicetitle]);
				} else {
					return row.vinvoicetitle;
				}
			},
			formatMatch : function(row, i, max) {
				return row.vinvoicetitle;
			},
			formatResult : function(row) {
				return row.vinvoicetitle;
			},
			parse : function(data) {
				return $.map(eval(data), function(row) {
					return {
						data : row,
						value : row.vinvoicetitle,
						result : row.vinvoicetitle
					};
				});
			}
		}).result(function(event, row, formatted) {

		});
	};
});