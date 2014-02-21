
/**
 * 商品查询自动补全
 * 
 */
define(function(require, exports, module) {
	var $ = require("$");
	var dialog = require("dialog");
	$ = require("jquery.autocomplete")($);
	
	exports.init = function(option) {
		initAutoComplete(option);
	};

	/**
	 * 初始化自动补全商品
	 */
	var initAutoComplete = function(option) {
		var className = "J_autoComplete";
		var callback = function() {
		};
		if (option.className) {
			className = option.className;
		}
		if (option.callback) {
			callback = option.callback;
		}
		$("." + className).autocomplete(G.API.PRODUCT_AUTOFILL, {
			max : 10, // 列表里的条目数
			minChars : 1, // 自动完成激活之前填入的最小字符
			width : 197, // 提示的宽度，溢出隐藏
			scrollHeight : 300, // 提示的高度，溢出显示滚动条
			matchContains : true, // 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
			autoFill : false, // 自动填充

			formatItem : function(row, i, max) {
				return row.name + "/" + row.to;
			},
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
		}).result(function(event, row, formatted) {
			
			// ajax查询商品信息
			$.ajax({
				url : G.API.PRODUCT_BASIC,
				data : {
					cproductid : row.pkid
				},
				success : function(json) {
					if (json.message == undefined) {
						if (json == "noprice") {
							var option={
								title:"提示",
								content:"该商品没有价格，不能购买！"
							};
							dialog.alert(option);
							$('.ET_addInputName:first').focus();
						} else if (json == "nostock") {
							var option={
								title:"提示",
								content:"该商品没有库存，不能购买！"
							};
							dialog.alert(option);
							$('.ET_addInputName:first').focus();
						} else if (json.length < 1) {
							var option={
								title:"提示",
								content:"没有查到该商品，不能购买！"
							};
							dialog.alert(option);
							$('.ET_addInputName:first').focus();
						} else {
							var data = json[0];
							callback(data);
						}
					}
				},
				error : function() {
					var option={
						title:"提示",
						content:"添加商品失败，请重试！"
					};
					dialog.alert(option);
				}
			});
		});
	};
});