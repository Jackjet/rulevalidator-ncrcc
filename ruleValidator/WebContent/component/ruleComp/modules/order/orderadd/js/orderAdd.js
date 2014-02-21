/**
 * 创建订单相关js
 */

define(function(require, exports, module) {
	var $ = require("$");
	$ = require("jquery.cookie")($);
	var orderCore = require("./orderCore");
	var orderEffect = require("./orderEffect");
	var dialog = require("dialog");
	//	var tips = require("tips");
	//	var StringUtils = require('stringUtils');
	var validator = require('order_validate');
	var store = require('store');
	var address = require("account_address"); 
	
	var constant = {
		selector : {
			vinvoicename : '#vinvoicename',
			vinvoicename_link : '.ET_addInvoiceName'
		},
		key : {
			vinvoicename : 'vinvoicename|' + G.userid
		},
		expires : new Date(new Date().getTime() + 30 * 24 * 60 * 60 * 1000)
	};

	var OrderAdd = function() {

		/**
		 * 订单暂存
		 */
		var tempSaveOrder = function() {
			
			// 订单行不能为空
			if (!validator.product()) {
				return false;
			}
			
			var option = {
				title : "提交订单",
				content : "正在暂存订单，请稍后......"
			};
			dialog.loading(option);
			var viewData = $("#orderAddForm").serialize();
			$.ajax({
				url : G.API.ORDER_TEMPSAVE,
				type : "POST",
				data : viewData,
				success : function(orderView) {
					if (orderView.message) {
						dialog.close();
						dialog.alertMsg("请添加收货地址和商品后暂存！");
					} else {
						afterOrderTempSave(orderView);
					}
				},
				error : function() {
					dialog.alertMsg("添加商品失败，请重试！");
				}
			});
			return false;
		};

		var afterOrderTempSave = function(orderView) {
			$("#ccustomerpoid").val(orderView.ccustomerpoid);
			$("#vbillcode").val(orderView.vbillcode);
			dialog.close();
			dialog.alertMsg("订单暂存成功，订单编号为：" + orderView.vbillcode);
		};

		/**
		 * 将发票抬头添加至cookies
		 */
		var addInvoiceToCookies = function() {
			var invoiceName = $(constant.selector.vinvoicename).val();
			if (invoiceName != "") {
				$.cookie(constant.key.vinvoicename, invoiceName, {
					path : '/',
					expires : constant.expires
				});
			}
			dialog.alertMsg("已保存成功");
			return false;
		};

		/**
		 * 保存开票抬头到cookies中
		 */
		var initInvoiceName = function() {
			var vinvoicename = $.cookie(constant.key.vinvoicename);
			if (vinvoicename) {
				$(constant.selector.vinvoicename).val(vinvoicename);
			} else {
				var vinvoicename = $(constant.selector.vinvoicename).attr("value");
				$(constant.selector.vinvoicename).val(vinvoicename);
			}
		};

		/**
		 * 删除订单行
		 */
		function deleteRow() {
			// 删除行
			$(this).parents('tr').remove();
			// 更新索引
			orderCore.updateIndex();
			// 校验
			validator.all();
			return false;
		}

		/**
		 * 提交订单
		 * @returns
		 */
		var confirmOrder = function() {
			if (validator.all()) {
				$('#orderAddForm').attr('action', G.API.ORDER_CONFIRM).submit();
			}
			return false;
		};

		/**
		 * 初始化函数
		 */
		this.init = function() {
			$("#orderAddSubmit").bind('click', confirmOrder);
			$("#orderTempSave").bind('click', tempSaveOrder);
			$(constant.selector.vinvoicename_link).bind('click', addInvoiceToCookies);
			initInvoiceName();

			// 删除行
			$('body').undelegate('.deleteOrderRow', 'click');
			$('.deleteOrderRow').undelegate('click').die('click').live('click', deleteRow);

			// 校验库存
			//			$('.J-order-row-amount').live('blur', function() {
			//				validator.nabnum();
			//			});
			$('body').delegate('.J-order-row-amount', 'blur', function() {
				validator.nabnum();
			});
			//添加地址簿事件
			$(".idAddressAdd").live("click",function(){
				address.addAddress(null,$(this));
			});

		};
	};

	var orderAdd = new OrderAdd();

	$(document).ready(function() {

		// 期望到货日期
		var userid=store.get("user").userId;
		var dreceivedate=store.get("dreceivedate") || {};
		var userdreceivedate = dreceivedate[userid] || 7;
		$('input[name$=dreceivedate]').each(function(i, item) {
			if (!$(item).val()) {
				$(item).attr('data-datepicker-defaultDate', userdreceivedate);
			}
		});
		seajs.emit('datepicker');
		orderAdd.init();
		seajs.on(G.EVENT.NABNUM_LOADED, function() {
			validator.nabnum();
		});
	});
	module.exports = orderAdd;
});
