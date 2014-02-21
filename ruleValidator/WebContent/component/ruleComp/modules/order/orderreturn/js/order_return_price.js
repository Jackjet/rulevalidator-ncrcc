/**
 * 退换货价格函数库
 */
define(function(require, exports, module) {
	require('../theme/default/css/order_price.css');
	var $ = require('$');
	var _scale = require("scale");
	// var _maths = require("maths");
	// var _formatter = require("formatter");
	var big = require('BigDecimal');
	var BigDecimal = big.BigDecimal;
	var MathContext = big.MathContext;

	/**
	 * 计算地址总价
	 */
	function calculatePrice() {

		// 计算订单行合计、地址合计、订单底部合计
		$('.J-order').each(
				function(i, order) {
					$(order).find('.J-order-address, .J-order-inventory ').each(
							function(j, address) {

								// 退货合计
								var totalReturnPrice = new BigDecimal('0');
								// 换货合计
								var totalExchangePrice = new BigDecimal('0');
								// 普通合计
								var totalPrice = new BigDecimal('0');

								// 遍历订单行，计算行合计
								$(address).find('tbody tr').each(function(i, row) {
									row = $(row);
									// a）计算行合计
									var num = new BigDecimal('0');
									var numField = row.find('.J-order-row-amount');
									if (numField.length > 0) {
										if (numField[0].nodeName == 'SPAN') {
											// num =
											// parseFloat(numField.text());
											num = new BigDecimal(numField.text());
										} else if (numField[0].nodeName == 'INPUT') {
											// num = parseFloat(numField.val());
											num = new BigDecimal(numField.val());
										}
										// 价格
										var price = new BigDecimal(row.find('.J-order-product-price').text());
										// var total = _maths.mul(price , num);
										var total = price.multiply(num);
										row.find('.J-order-row-price').text(total);

										// 币种精度
										var scale = parseInt(row.find('.J-order-row-price').attr('scale'));
										// var _total =
										// parseFloat(_formatter.fmtNum(total,
										// scale));
										var _total = total.setScale(scale, MathContext.ROUND_HALF_UP);

										// b）计算地址合计
										// 类型
										var type = row.find('.J-order-row-type').attr('type');
										// 默认为普通订货
										type = type || '0';
										if (type == '1') {
											// 退货
											// totalReturnPrice += _total;
											totalReturnPrice = totalReturnPrice.add(_total);
										} else if (type == '2') {
											// 换货
											// totalExchangePrice += _total;
											totalExchangePrice = totalExchangePrice.add(_total);
										} else {
											// 订单
											// totalPrice += _total;
											totalPrice = totalPrice.add(_total);
										}
									}
								});

								// c）设置地址底边价格合计
								setPrice({
									rxContainer : ($(address).find('.J-order-return-total-price-address-footer').length > 0) ? $(address).find('.J-order-return-total-price-address-footer')
											: $('.ui-order-price-return-number'),
									container : $(address).find('.J-order-total-price-address-footer'),
									totalReturnPrice : totalReturnPrice,
									totalExchangePrice : totalExchangePrice,
									totalPrice : totalPrice
								});

								// d）设置地址收缩后头部价格合计
								setPrice({
									rxContainer : $(address).find('.J-order-return-total-price-address-collapse'),
									container : $(address).find('.J-order-total-price-address-collapse'),
									totalReturnPrice : totalReturnPrice,
									totalExchangePrice : totalExchangePrice,
									totalPrice : totalPrice
								});
							});
				});

		// 第三步：设置订单底部的总计
		$('.J-order').each(
				function(i, order) {

					// a）计算当前地址所有行的价格合计（退货和换货分开计算）
					var totalReturnPrice = new BigDecimal('0');
					var totalExchangePrice = new BigDecimal('0');
					var totalPrice = new BigDecimal('0');

					$(order).find('.J-order-address').each(function(i, address) {
						// 退换货
						var footer = $(address).find('.J-order-return-total-price-address-footer');
						var temp = new BigDecimal(footer.find('.ui-order-price-return-number').text() == '' ? '0' : footer.find('.ui-order-price-return-number').text());
						// totalReturnPrice += parseFloat(temp);
						totalReturnPrice = totalReturnPrice.add(temp);
						// temp =
						// parseFloat(footer.find('.ui-order-price-exchange-number').text());
						temp = new BigDecimal(footer.find('.ui-order-price-exchange-number').text() == '' ? '0' : footer.find('.ui-order-price-exchange-number').text());
						// totalExchangePrice += parseFloat(temp);
						totalExchangePrice = totalExchangePrice.add(temp);

						// 普通订货
						footer = $(address).find('.J-order-total-price-address-footer');
						// temp = footer.find('.ui-order-price-number').text();
						temp = new BigDecimal(footer.find('.ui-order-price-number').text() == '' ? '0' : footer.find('.ui-order-price-number').text());
						// totalPrice += parseFloat(temp);
						totalPrice = totalPrice.add(temp);
					});

					setPrice({
						rxContainer : ($(order).find('.J-order-return-total-price-order-footer').length > 0) ? $(order).find('.J-order-return-total-price-order-footer')
								: $('.J-order-return-total-price-order-footer'),
						container : ($(order).find('.J-order-total-price-order-footer').length > 0) ? $(order).find('.J-order-total-price-order-footer') : $('.J-order-total-price-order-footer'),
						totalReturnPrice : totalReturnPrice,
						totalExchangePrice : totalExchangePrice,
						totalPrice : totalPrice
					});
				});

		// 第四步：计算清单价格合计
		$('.J-order').each(function(i, order) {
			$(order).find('.J-order-inventory').each(function(i, inventory) {

				// a）计算当前清单所有行的价格合计（退货和换货分开计算）
				var totalReturnPrice = new BigDecimal('0');
				var totalExchangePrice = new BigDecimal('0');
				var totalPrice = new BigDecimal('0');

				$(inventory).find('tbody tr').each(function(i, tr) {
					var row = $(tr);
					// 行合计
					var total = new BigDecimal(row.find('.J-order-row-price').text());
					// if (total) {
					// total = parseFloat(total);
					// }
					// 类型
					var type = row.find('.J-order-row-type').attr('type');
					if (type == '1') {
						// 退货
						// totalReturnPrice += total;
						totalReturnPrice = totalReturnPrice.add(total);
					} else if (type == '2') {
						// 换货
						// totalExchangePrice += total;
						totalExchangePrice = totalExchangePrice.add(total);
					} else {
						// 订单
						// totalPrice += total;
						totalPrice = totalPrice.add(total);
					}
				});

				// b）设置清单底边价格合计
				setPrice({
					rxContainer : $(inventory).find('.J-order-return-total-price-inventory-footer'),
					container : $(inventory).find('.J-order-return-total-price-inventory-footer'),
					totalReturnPrice : totalReturnPrice,
					totalExchangePrice : totalExchangePrice,
					totalPrice : totalPrice
				});

				// c）设置清单收缩后头部价格合计
				setPrice({
					rxContainer : $(inventory).find('.J-order-return-total-price-inventory-head'),
					container : $(inventory).find('.J-order-return-total-price-inventory-head'),
					totalReturnPrice : totalReturnPrice,
					totalExchangePrice : totalExchangePrice,
					totalPrice : totalPrice
				});
			});

		});

		// 精度计算
		_scale.init();
	}

	/**
	 * 设置退换货合计金额（如果退货合计或者换货合计为0，则删除节点）
	 */
	function setPrice(params) {
		// 退换货合计容器
		var rxContainer = $(params.rxContainer);
		// 订货合计容器
		var container = $(params.container);
		// 总退货合计金额
		var totalReturnPrice = params.totalReturnPrice;
		// 总换货合计金额
		var totalExchangePrice = params.totalExchangePrice;
		// 总订货金额
		var totalPrice = params.totalPrice;

		// 退货合计
		if (totalReturnPrice > 0) {
			rxContainer.find('.ui-order-price-return-number').text(totalReturnPrice);
			rxContainer.find('.ui-order-price-return-symbol').show();
			rxContainer.find('.ui-order-price-return-currency-sign').show();
			rxContainer.find('.ui-order-price-return-number').show();
		} else {
			rxContainer.find('.ui-order-price-return-symbol').hide();
			rxContainer.find('.ui-order-price-return-currency-sign').hide();
			rxContainer.find('.ui-order-price-return-number').hide();
		}

		// 换货合计
		if (totalExchangePrice > 0) {
			rxContainer.find('.ui-order-price-exchange-number').text(totalExchangePrice);
			rxContainer.find('.ui-order-price-exchange-symbol').show();
			rxContainer.find('.ui-order-price-exchange-currency-sign').show();
			rxContainer.find('.ui-order-price-exchange-number').show();
		} else {
			rxContainer.find('.ui-order-price-exchange-symbol').hide();
			rxContainer.find('.ui-order-price-exchange-currency-sign').hide();
			rxContainer.find('.ui-order-price-exchange-number').hide();
		}

		// 订货合计
		container.find('.ui-order-price-number').text(totalPrice);
	}

	// // 绑定计算事件（更改商品数量）
	// $('body').delegate('.J-order-row-amount', 'blur', calculatePrice);
	// $('.J-order-row-amount').live('blur', calculatePrice);

	module.exports.calculatePrice = calculatePrice;

});