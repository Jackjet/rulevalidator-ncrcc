/*
 * 退换货公共校验
 */
define(function(require, exports, module) {

	var $ = require('$');
	var _dialog = require('dialog');
	var _stringUtils = require('stringUtils');
	var _tips = require('tips');

	function validationProduct() {
		var product_flag = '';
		if ($('.idAddressBar').length == 0) {
			var noticeText = '没有退货地址，请添加退货地址！';
			$('.order_single_width').append(_tips.noticeBarBlue(noticeText, 'org'));
			return false;
		}
		// 判断商品不能为空
		$('.idAddressBar').each(function(index, domEle) {
			if ($(domEle).find('tbody').children().length == 0) {
				product_flag = product_flag + ' ' + $(domEle).find('.content_name').html();
				flag = false;
			}
		});
		if (product_flag != '') {
			var noticeText = product_flag + '，无商品，请添加后提交!';
			$('.order_single_width').append(_tips.noticeBarBlue(noticeText, 'org'));
			return false;
		}
		return true;
	}

	/**
	 * 日期不能为空
	 */
	function validationDate() {
		var date_flag = true;
		// 判断预计发货日期不能为空
		$(".datepicker,.date_picker").each(function(index, domEle) {
			if ($(domEle).attr("value") == "") {
				_dialog.alertMsg("请输入预计发货日期！");
				date_flag = false;
			}
		});
		return date_flag;
	}

	/**
	 * 可退货数量校验
	 */
	function validationNqtunitNum() {
		var flag = true;
		$('.J-order-row-amount').each(function() {
			var row = $(this).parents('tr');
			var fretexchange = row.find('input[name$=fretexchange]').val();
			// 是否为换货行
			var isExchangeRow = false;
			if (fretexchange && fretexchange == 2) {
				isExchangeRow = true;
			}
			if (isExchangeRow) {
				return;
			}
			// 报价数量
			var nqtunitnum = parseFloat($(this).val());
			// 商品名称
			var productName = $(this).parent().parent().find('a:first').text();
			if (nqtunitnum <= 0) {
				_dialog.alertMsg('数量必须大于0，请修改');
				$(this).focus();
				flag = false;
			}
			// 参考订单创建退货单校验可退货数量
			var isRefer = $(this).parents('.J-order').attr('isRefer') === 'true' ? true : false;
			if (isRefer) {
				// 前置条件：参考退货单
				var max = parseFloat($(this).attr('max'));
				if (nqtunitnum > max) {
					_dialog.alertMsg('商品【' + productName + '】退货数量超过限制，最大可退货数量：' + max);
					$(this).val(max);
					$(this).trigger('orderCacl');
					$(this).focus();
					flag = false;
				}
			}
		});
		return flag;
	}

	// 商品必须有价格
	function priceValidation() {
		var flag = true;
		$("div.idBarContentExtended .caclPrice").each(function() {
			var price = $.trim($(this).text());
			if (!price || price == "暂无报价") {
				var noticeText = "商品无价格，请修改订单！";
				$(".order_single_width").append(_tips.noticeBarBlue(noticeText, "org"));
				flag = false;
			}
		});
		return flag;
	}
	;

	/**
	 * 不同地址块应该具备以下条件 收货地区 & 收货地址 & 预计发货日期 各个地址之间不同，否则页面提示让用户手工合并
	 */
	function checkAddress() {
		// 遍历地址
		var uniqueKeys = [];
		var result = true;
		$('.J-order-address').each(function(i, address) {
			var creceiveareaid = $(address).find('select[name$=creceiveareaid]').val();
			var creceiveaddrid = $(address).find('select[name$=creceiveaddrid]').val();
			var date = $(address).find('input[name$=dreceivedate]').val();
			var key = creceiveareaid + '@' + creceiveaddrid + '@' + date;
			if (uniqueKeys.length == 0) {
				uniqueKeys.push(key);
			} else {
				$(uniqueKeys).each(function(j, item) {
					if (key == item) {
						// 有相同的
						_dialog.alert({
							content : '第【<span style="color:red">' + (i + 1) + '</span>】和第【<span style="color:red">' + (j + 1) + '</span>】地址信息重复，请修改',
							isHtmlContent : true
						});
						result = false;
						return false;
					} else {
						// 没有相同的
						uniqueKeys.push(key);
					}
				});
			}
			if (!result) {
				return false;
			}
		});
		return result;
	}

	/**
	 * 找到所有退换行的换货行，如果没有找到返回空数组
	 * 
	 * @param tr
	 */
	function listExchangeOrderRow(tr) {
		tr = $(tr);
		var tbody = tr.parents("tbody");
		var returnRow = tr;
		var iReturnRow = parseInt(returnRow.attr('index'));
		var rows = tbody.children('tr');
		var exchangeRow = [];
		for ( var i = iReturnRow + 1; i < rows.length; i++) {
			var currentRow = $(rows[i]);
			var fretexchange = currentRow.find('input[name$=fretexchange]').val();
			var returnRowIndex = currentRow.find('input[name$=returnRowIndex]').val();
			if (fretexchange == 2 && (returnRowIndex && parseInt(returnRowIndex) == iReturnRow)) {
				// 是换货行 && 是当前退货单的换货行
				// currentRow.remove();
				exchangeRow.push(rows[i]);
			}
		}
		return exchangeRow;
	}

	/**
	 * 对于同一个退货行，不能有相同商品的换货行
	 */
	function exchangeRowValidation() {
		// 遍历所有退货行
		var inputs = $('form input[name$=fretexchange][value!=2]');
		for ( var i = 0; i < inputs.length; i++) {
			var input = inputs[i];
			var returnRow = $(input).parents('tr');
			var productIds = [];
			// 当前退货行对应的所有换货行
			var exchanges = listExchangeOrderRow(returnRow);
			for ( var j = 0; j < exchanges.length; j++) {
				var exchange = exchanges[j];
				var productId = $(exchange).find('input[name$=cproductid]').val();
				if (productIds.length == 0 || productIds.join(',').indexOf(productId) == -1) {
					// 不重复
					productIds.push(productId);
				} else {
					// 商品重复 => 找到哪一个商品重复了
					for ( var k = 0; k < productIds.length; k++) {
						if (productId == productIds[k]) {
							var indexOriginal = parseInt(returnRow.attr('index')) + k + 2;
							var indexDuplicate = parseInt($(exchange).attr('index')) + 1;
							var productName = $(exchange).find('td:eq(1) input').val();
							var content = _stringUtils
									.format(
											'<center>对于同一个退货行的所有换货行商品不能重复，<br>第【<span style="color:red">%s</span>】行和第【<span style="color:red">%s</span>】行的换货行商品【<span style="color:red">%s</span>】重复了，<br>请合并后再提交</center>',
											[ indexDuplicate, indexOriginal, productName ]);
							_dialog.alert({
								content : content,
								isHtmlContent : true
							});
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 总校验
	 */
	var validation = function() {
		if (validationDate() && validationProduct() && validationNqtunitNum() && priceValidation() && exchangeRowValidation() && checkAddress()) {
			return true;
		} else {
			return false;
		}
	};

	module.exports = {
		validation : validation
	};

});