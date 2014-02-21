/**
 * 订单详细页面相关js
 */
define(function(require, exports, module) {
	var $ = require("$");
	$ = require("jquery.extend")($);
	var orderEffect = require("./orderEffect");
	var scale = require("scale");
	var dialog = require("dialog");
	var store = require("store");
	$(function() {
		// 待签收的单据行需要添加链接，其它状态下不需要
		$("a.td_tip").each(function() {
			if ($(this).attr("statusflag") == 4) {
				$(this).attr("href", "/ecp/html/order/order.html?page=asnList&asnCode=" + $(this).attr("vbillcode"));
			} else {
				$(this).attr("style", "color:black;");
			}
			;
		});
		$("a.td_tip").bind('mouseenter', function() {
			var alink = $(this);
			var ccustomerpobid = alink.attr("bid");
			$.ajax({
				type : "GET",
				url : G.API.ORDER_EXECINFO,
				data : {
					bid : ccustomerpobid
				},
				success : function(array) {
					var result = $("<div>");
					if (array.length > 0) {
						for ( var i = 0; i < array.length; i++) {
							result.append(array[i].vmessage).append("<br/>");
						}
					} else {
						result.append("没有执行信息");
					}
					alink.find(".J-order-exe-info").replaceWith(result);
				}
			});
		});
		$(".orderCancel").bind('click', function() {
			var option = {
				title : "订单取消",
				content : "确定要取消该订单吗?",
				confirm : function() {
					var ccustomerpoid = $(".orderCancel").attr("value");
					$.ajax({
						type : "POST",
						url : G.API.ORDER_CANCEL,
						data : {
							orderid : ccustomerpoid
						},
						success : function(msg) {
							if (msg == "success") {
								var option = {
									title : "提示",
									content : "订单已取消"
								};
								dialog.close();
								dialog.alert(option);
								window.location = window.location;
							} else {
								var option = {
									title : "提示",
									isHtmlContent : true,
									content : "<div style='padding:0 30px;'>订单取消失败:" + msg.message + "</div>"
								};
								dialog.close();
								dialog.alert(option);
							}
						}
					});
				},
				cancel : function() {
					dialog.close();
				}
			};
			dialog.confirm(option);
			return false;
		});

		$(".J_sendNotice").bind("click", function() {
			var vfirstcode = $(this).attr("vfirstcode");
			var cproductid_code =  $(this).attr("cproductid_code");
			store.set("vfirstcode", vfirstcode);
			store.set("cproductid_code", cproductid_code);
		});
	});
});