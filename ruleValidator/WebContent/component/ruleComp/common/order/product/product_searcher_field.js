define(function(require, exports, module) {
	var $ = require("$");
	require('./theme/{theme}/css/product_search_field.css');
	var Widget = require("widget");
	var Dialog = require("dialog");
	var ProductSearcher = require('product_searcher');
	var Placeholder = require('placeholder');

	/**
	 * 填写商品信息
	 */
	function onSuccess(widget, product) {

		// [1] 获取dom
		var input = widget.find('.ui-searcher');
		var hidden = input.next('input:hidden');
		var amout = widget.find('.ui-amout');

		// [2] 将商品信息填充在dom上

	}

	function keys(widget) {
		var input = widget.find('.ui-searcher');
		var amout = widget.find('.ui-amout');
		var button = widget.find('.ui-button-add');

		// [1] 商品搜索、商品数量两个控件获取焦点后，输入回车键，依次获取焦点
		$([ input, amout, button ]).each(function() {
			this.keydown(function(event) {
				var target = $(event.target);
				var keyCode = event.keyCode;
				if (keyCode == 13) {
					if (target.hasClass('ui-searcher')) {
						amout.focus();
					} else if (target.hasClass('ui-amout')) {
						button.focus();
					} else {
						button.trigger('click');
					}
					return false;
				}
			});
		});
	}

	/**
	 * 查询商品
	 */
	function getProduct(input, productId, onSuccess) {
		$.ajax({
			url : G.API.PRODUCT_BASIC,
			data : {
				cproductid : productId
			},
			success : function(json) {
				if (json.message == undefined) {
					if (json == "noprice") {
						var option = {
							title : "提示",
							content : "该商品没有价格，不能购买！"
						};
						Dialog.alert(option);
						input.focus();
					} else if (json == "nostock") {
						var option = {
							title : "提示",
							content : "该商品没有库存，不能购买！"
						};
						Dialog.alert(option);
						input.focus();
					} else if (json.length < 1) {
						var option = {
							title : "提示",
							content : "没有查到该商品，不能购买！"
						};
						Dialog.alert(option);
						input.focus();
					} else {
						// 查询商品成功
						onSuccess(json[0]);
					}
				}
			},
			error : function() {
				var option = {
					title : "提示",
					content : "查询商品失败！"
				};
				Dialog.alert(option);
			}
		});
	}
	var ProductSearcherField = null;
	ProductSearcherField = Widget.extend({
		template : require('./template/product_searcher_field.tpl'),
		afterRender : function() {
			this.constructor.superclass.afterRender.apply(this, arguments);
			Placeholder.init($);

			// [1] 创建商品查询控件
			var attrs = this.get('attrs');
			var widget = $(this.element);
			var input = $(this.element).find('.ui-searcher');
			var hidden = input.next('input:hidden');
			var amout = widget.find('.ui-amout');

			ProductSearcher.create({
				renderTo : input,
				onSelect : function(event, row, formatted) {
					// 把主键回写在隐藏域上
					hidden.val(row.pkid);
				}
			});

			// [2] 新增按钮
			var button = $(this.element).find('a');
			button.click(function() {
				var productId = hidden.val();
				getProduct(input, productId, function(product) {
					attrs.onAdd(widget, product);
				});
			});

			// [3] 输入回车依次获取焦点
			keys(widget);
		}
	});
	
	module.exports = ProductSearcherField;
});