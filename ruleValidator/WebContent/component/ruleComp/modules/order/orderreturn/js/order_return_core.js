define(function(require, exports, module) {
	require('../../orderadd/theme/{theme}/css/order.css');
	require('../theme/{theme}/css/order_return.css');
	require('../theme/default/css/order_price.css')
	
	var $ = require('$');
	var _dialog = require('dialog');
	var _nabnum = require("nabnum");
	var _scale = require("scale");
	var _maths = require("maths");
	var _formatter = require("formatter");
	
	
	/**
	 * 计算行总价
	 */
	function calculateRowPrice() {
		var row = $(this);
		// 数量
		var num = row.find('.J-order-row-amount').val();
		// 价格
		var price = row.find('.J-order-product-price').text();
		// 行合计
		var total = _maths.mul(num, price);
		// 精度
		var scale = row.find('.J-order-row-price').attr('scales');
		// 设置金额和隐藏target
		total = _formatter.fmtNum(total, scale);
		row.find('.J-order-row-price').text(total);
	}

	/**
	 * 计算地址总价
	 */
	function calculateAddressPrice() {

	}

	/**
	 * 计算订单总价（单个订单）
	 */
	function calculateOrderPrice() {
		// 计算行总价
		calculateRowPrice();
		// 计算地址总价
		calculateAddressPrice();
	}
	
	

	/**
	 * 切换显示地址栏
	 */
	function toggleAddress() {
		if ($($(this).parents(".bar_content")).attr("class").indexOf("inside_width") > 0) {
			// 判断是展开还是收起
			if ($(this).parents(".rounded_extended_bar").length > 0) {
				// 如果展开，则收起
				$(this).parents(".info_line").next(".idBarContentExtended").slideUp('slow', function() {
					$(this).parents(".rounded_extended_bar").addClass("rounded_single_bar inside_width").removeClass("rounded_extended_bar");
					$(this).prev(".info_line").find(".trigger").removeClass("up").addClass("down");
					$(this).prev(".info_line").find(".content_info").show();
				});
			} else if ($(this).parents(".rounded_single_bar").length > 0) {
				// 如果收起 则展开
				$(this).parents(".rounded_single_bar").addClass("rounded_extended_bar inside_width").removeClass("rounded_single_bar");
				$(this).removeClass("down").addClass("up");
				$(this).parents(".bar_content").find(".content_info").hide();
				$(this).parents(".info_line").next(".idBarContentExtended").slideDown('slow', function() {
				});
			}
		} else if ($($(this).parents(".bar_content")).attr("class").indexOf("outside_width") > 0) {
			if ($(this).parents(".rounded_extended_bar").length > 0) {
				$(this).parents(".info_line").next(".idBarContentExtended").slideUp('slow', function() {
					$(this).parents(".rounded_extended_bar").addClass("rounded_single_bar inside_width").removeClass("rounded_extended_bar");
					$(this).prev(".info_line").find(".trigger").removeClass("up").addClass("down");
				});
			} else if ($(this).parents(".rounded_single_bar").length > 0) {
				$(this).parents(".rounded_single_bar").addClass("rounded_extended_bar inside_width").removeClass("rounded_single_bar");
				$(this).removeClass("down").addClass("up");
				$(this).parents(".info_line").next(".idBarContentExtended").slideDown('slow', function() {
				});
			}
		}
		return false;
	}

	/**
	 * 地址删除
	 */
	function removeAddress() {
		
		// 判断是否是最后一个地址
		if ($('.J-order-address').length == 1) {
			_dialog.alert({
				content:'<center>已经是最后一个地址信息了，不能删除！</center>',
				isHtmlContent:true
			});
			return false;
		}
		
		var trigger = $(this);
		_dialog.dialog({
			type:'confirm',
			content:'<center>您确定删除该收货地址吗？</center>',
			isHtmlContent:true,
			buttons:[{
				name:'确定',
				isDefault:false,
				href:'#',
				method:function() {
					trigger.parents(".J-order-address").remove();
//					var address_summy = 0.00;
//					var product_number = 0;
//					// 找到该地址所有的金额input
//					var address = $(".idAddressBar");
//					address.find("span.norigtaxmny").each(function() {
//						address_summy = maths.add(parseFloat(address_summy), formatter.fmtToFloat($(this).text()));
//						product_number++;
//					});
//					$(".nheadsummny").text(formatter.fmtNum(address_summy, 4));
//					// 计算地址商品清单
//					calTotalProd();
//					address.trigger("updateIndex");
					
					// 重新计算价格
					
					// 更新索引
					_dialog.close();
				}
			},{
				name:'取消',
				isDefault:true,
				href:'#',
				method:function() {
					_dialog.close();
				}
			}]
		});
		return false;
	}

	// 变换地址
	$("body").delegate(".bar_content .trigger", "click", toggleAddress);
	// 删除地址
	$("body").delegate(".bar_content .icon_delete", "click", removeAddress);
	
	
	module.exports = {
		calculateRowPrice:calculateRowPrice,
		calculateAddressPrice:calculateAddressPrice,
		calculateOrderPrice:calculateOrderPrice
	};

});