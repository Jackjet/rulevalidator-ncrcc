/**
 * 确认订单
 */
define(function(require, exports, module) {
	var $ = require('$');
	var dialog = require('dialog');
	// var shopcart = require('shopcart');
	// var scale = require('scale');
	 var nabnum = require('nabnum');
	var CreditChecker = require('./CreditChecker');
	var orderEffect = require('order_orderEffect');
	var tips = require('tips');
	// var orderCore = require('order_core');
	// var orderPrice = require('order_return_price');
	var stringUtils = require('stringUtils');

	/**
	 * 设置域名
	 */
	function setFieldName() {
		// 更新文本域名称
		// 需要更新索引的文本域名称后缀
		var orderAddressFieldNames = [ 'creceiveareaid', 'creceiveaddrid', 'dreceivedate' ];
		var orderItemFieldNames = [ 'cproductid', 'cshopcartid', 'nqtunitnum', 'cmaterialid', 'pk_org', 'pk_org_name', 'corigcurrencyid', 'csrcbid', 'cretreasonid', 'fretexchange',
				'cexchangesrcretid', 'returnRowIndex', 'nqtorigtaxnetprc', 'nqtorigtaxprice_priceScale', 'corigcurrencyid_curSign', 'vsrccode', 'csrcid', 'vsrcrowno', 'norigtaxmny', 'ntotaloutnum',
				'ntotalreturnnum', 'ntotalsignnum', 'cmaterialvid', 'ccustomerpoid', 'ccustomerpobid', 'nqtorignetprice', 'nqtorigprice', 'cqtunitid', 'blargessflag', 'taxRate', 'nqtorigtaxprice',
				'clargesssrcid', 'cqtunitid_unitScale', 'corigcurrencyid_amountScale' ];

		// 是否为多订单
		var isMultiOrder = false;
		if ($('input[name^=multiOrderUIViews]').length > 0) {
			isMultiOrder = true;
		}

		// deep_0：遍历订单
		$('.J-order').each(function(i, order) {
			// 订单前缀
			var orderPrefix = isMultiOrder ? 'multiOrderUIViews[' + i + '].' : '';
			// deep_1：遍历地址
			$(order).find('.J-order-address').each(function(j, address) {
				// 地址前缀
				var addressPrefix = 'addressUIView[' + j + '].';
				// 更新地址头部文本域名称
				$(orderAddressFieldNames).each(function() {
					var field = $(address).find('input[name$=' + this + ']');
					field.attr('name', orderPrefix + addressPrefix + this);
				});

				// deep_2：遍历订单行
				$(address).find('tbody > tr').each(function(k, row) {
					// 订单行前缀
					var rowPrefix = 'productUIView[' + k + '].';
					// 更新行索引
					$(row).attr('index', k);
					// 更新订单行文本域名称
					$(orderItemFieldNames).each(function() {
						var field = $(row).find('input[name$=' + this + ']');
						field.attr('name', orderPrefix + addressPrefix + rowPrefix + this);
					});
				});
			});
		});
	}

	/**
	 * 校验价格： 1）价格大于零
	 */
	function validatePrice() {
		// 错误信息
		var msg = '';
		// 是否有效
		var isValid = true;
		// 遍历订单
		$('.J-order').each(function(i, order) {
			// 遍历地址
			$(order).find('.J-order-address').each(function(j, address) {
				// 遍历商品行
				$(address).find('.J-order-row-normal').each(function(k, item) {
					// 价格
					var price = $.trim($(item).find('.J-order-product-price').text());
					// 商品名称
					var productName = $.trim($(item).find('td:eq(1) > a').text());
					if (!price || parseFloat(price) <= 0) {
						isValid = false;
						msg += stringUtils.format('第【%s】个订单第【%s】收货地址中的第【%s】商品【%s】价格不正确<br>', [ i + 1, j + 1, k + 1, productName ]);
					}
				});
			});
		});

		if (!isValid) {
			$(".J-button").before(tips.noticeBarBlue(msg, "org"));
		}
		return isValid;
	}

	/**
	 * 校验库存： 1）报价数量为数字，并且大于零； 2）非赠品 &&【无货可下单标记】为假时：报价数量应该小于等于库存
	 */
	function validateNumber() {
		// 错误信息
		var msg = '';
		// 是否有效
		var isValid = true;
		// 遍历订单
		$('.J-order').each(function(i, order) {
			// 遍历地址
			$(order).find('.J-order-address').each(function(j, address) {
				// 遍历商品行
				$(address).find('.J-order-row-normal').each(function(k, item) {
					// 报价数量
					var number = parseFloat($.trim($(item).find('.J-order-row-amount').text()));
					// 无货可下单标记
					var bspotflag = $(item).find('span[ectype=nabnum]').attr('bspotflag');
					// 库存
					var nabnum = $(item).find('span[ectype=nabnum]').attr('nabnum');
					// 商品名称
					var productName = $.trim($(item).find('td:eq(1) > a').text());
					if (bspotflag === 'false' && number > nabnum) {
						isValid = false;
						msg += stringUtils.format('第【%s】个订单的第【%s】收货地址的第【%s】行商品【%s】的库存不足<br>', [ i + 1, j + 1, k + 1, productName ]);
					}
				});
			});
		});

		if (!isValid) {
			$(".J-button").before(tips.noticeBarBlue(msg, "org"));
		}
		return isValid;
	}

	/**
	 * 校验
	 */
	function validate() {
		return (validatePrice() && validateNumber());
	}

	$(document).ready(function() {
		if ($("#splitFlag").val() == "true") {
			$(".J-button").before(tips.noticeBarBlue("根据所购商品所属销售组织、币种，您的订单已被拆分成多个订单", "org"));
		}

		// var cc = new CreditChecker('orderCommitForm');
		if (G.isIntegration) {
			// 信用检查（包含画图）
			CreditChecker.check();
			// scale.init();
		}

		// 修改订单处理
		$(".J-order-modify").live('click', function() {
			var url = G.API.ORDER_MODIFY;
			window.open(url, '_self');
			return false;
		});

		/*
		 * form提交处理
		 */
		$(".J-order-commit").live('click', function() {
			if (!validate()) {
				return false;
			}
			/*
			 * CA签名
			 */
			var strSn = "";
			if (ca) {
				try {
					strSn = getSN(G.userCode);
				} catch (e) {

				}
				if (!strSn) {
					var option = {
						title : "提交订单",
						content : "请插入UKey后再进行操作！"
					};
					dialog.alert(option);
					return false;
				}
				$("input[name$=nheadsummny]").each(function(i, input) {
					var strValue = $(input).val();
					var strSignData = G.userid + "|" + strValue;
					var signature = signmessage(strSignData, G.userCode);
					// 处理特殊字符,暂不打开
					// signature = encodeURIComponent(signature);
					$(input).parent().find('input[name$=vsignature]').val(signature);
				});
			}

			var option = {
				title : "提交订单",
				content : "正在提交订单……"
			};
			dialog.loading(option);
			var param = $("#orderCommitForm").serialize();
			param += "&casn=" + strSn;
			$.ajax({
				type : "POST",
				url : G.API.ORDER_COMMIT,
				dateType : "json",
				data : param,
				success : function(msg) {
					dialog.close();
					// if (msg.message) {
					// msg = msg.message;
					// }
					orderEffect.createOrderSuccess(msg, "order");
				}
			});
			return false;
		});

		// 删除赠品
		$('.J-order-row-largess').live('click', function() {
			$(this).parents('tr').remove();
			// 更新索引
			// orderCore.updateIndex();
			// orderCore.setFieldName();
			// orderCore中包含了太多确认页面不需要的脚本，目前只需要更新域操作，所以将该函数本地化，摆脱对其的依赖
			setFieldName();
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
	});
});
