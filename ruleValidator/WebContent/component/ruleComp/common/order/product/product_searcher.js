define(function(require, exports, module) {
	var $ = require("$");
	$ = require("jquery.autocomplete")($);
	var StringUtils = require('stringUtils');
	var Widget = require("widget");

	/**
	 * 默认配置
	 */
	var defaultOption = {
		// 数据服务
		url : G.API.PRODUCT_AUTOFILL,
		renderTo : '.J-product-searcher',
		// listeners:{
		// loadSuccess:function() {},
		// loadError:function() {}
		// },
		// 匹配后的处理
		onSelect: function() {
		},
		searchConfig : {
			// 列表里的条目数
			max : 10,
			// 自动完成激活之前填入的最小字符
			minChars : 1,
			// 提示的宽度，溢出隐藏
			width : 197,
			// 提示的高度，溢出显示滚动条
			scrollHeight : 300,
			// 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
			matchContains : true,
			// 自动填充
			autoFill : false,
			// 查询结果格式化
			formatItem : function(row, i, max) {
				return StringUtils.format('%s / %s', [row.name, row.to]);
			},
			// 匹配
			formatMatch : function(row, i, max) {
				return row.name + row.to;
			},
			formatResult : function(row) {
				return row.to;
			},
			parse : function(data) {
				return $.map(eval(data), function(row) {
					return {
						data : row,
						value : row.name,
						result : row.name
					};
				});
			}
		}
	};

	function create(option) {
		option = $.extend(defaultOption, option);
		var renderTo = option.renderTo;
		if (typeof (renderTo) == 'string') {
			renderTo = $(renderTo);
		}
		renderTo.autocomplete(option.url, option.searchConfig).result(function(event, row, formatted) {
			option.onSelect(event, row, formatted);
		});
	}

	module.exports = {
		create : create
	};
});