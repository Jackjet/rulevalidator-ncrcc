/**
 * 订单详细页面相关js
 */
define(function(require, exports, module) {
	var $ = require("$");
	$ = require("jquery.extend")($);
	$(function() {
		
		$("a.td_tip").each(function() {
			$(this).attr("style", "color:black;");
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
	});
});