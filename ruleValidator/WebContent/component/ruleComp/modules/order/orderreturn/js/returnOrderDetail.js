/**
 * 订单详细页面相关js
 */
define(function(require, exports, module) {
	require('../theme/{theme}/css/order_return.css');
	var $ = require("$");
	var orderEffect = require("order_orderEffect");
	// var scale = require("scale");
	var dialog = require("dialog");
	var _price = require('order_return_price');
	$(function() {
		$(".J-return-rorder-cancel").live('click', function() {
			var orderId = $(this).attr('orderId');
			if (!orderId) {
				throw new Error('没有获取到【订单主键】，请确认【取消】按钮是否包含【订单主键】属性');
			}

			dialog.confirm({
				content : '<center>确定要取消该退货单吗</center>',
				isHtmlContent : true,
				buttons : [ {
					name : '确定',
					isDefault : false,
					href : '#',
					method : function() {
						var ccustomerid = orderId;
						$.ajax({
							type : "POST",
							url : G.API.ORDER_CANCEL,
							data : {
								orderid : ccustomerid
							},
							success : function(msg) {
								if (msg == "success") {
									window.location = window.location;
								}
							}
						});
					}
				}, {
					name : '取消',
					isDefault : true,
					href : '#',
					method : function() {
						dialog.close();
					}
				} ]
			});
			return false;
		});

		/**
		 * 修改退货单
		 */
		$('.J-order-modify').live('click', function() {
			var orderId = $(this).attr('orderId');
			$('#orderCommitForm').attr('action', G.API.ORDER_RETURN_MODIFY+'?orderId=' + orderId).submit();
			return false;
		});
		
		// 商品超链接
		$('*[ectype=product]').live('click', function() {
			// 主键
			var productId = $(this).attr('productId');
			if (productId) {
				productId = $.trim(productId);
			} else {
				throw new Error('没有设置商品主键，无法构造商品超链接');
			}
			window.open(GLOBAL.PAGE.PRODUCT_DETAIL + productId);
			return false;
		});

		_price.calculatePrice();
	});
});