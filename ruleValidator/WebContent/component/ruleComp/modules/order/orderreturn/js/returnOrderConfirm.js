/**
 * 确认订单
 */
define(function(require, exports, module) {
	require('../theme/{theme}/css/order_return.css');
	var $ = require("$");
	var dialog = require("dialog");
	var orderEffect = require("order_orderEffect");
	var tips = require("tips");
	var _price = require('order_return_price');

	$(document).ready(function() {
		var validateForm = function() {
			/*
			 * 根据订单价格判断是否可以下单（如果二次询价没有价格不可下单）
			 */
			var flag = "";
			$("tbody > tr").each(function() {
				var priceflag = false;
				$(this).find(".validatePrice").each(function() {
					var price = $.trim($(this).text());
					if (price == "" || price == "null" || price == 0) {
						$(this).text("暂无价格");
						priceflag = true;
					}
				});
				if (priceflag) {
					flag = flag + $(this).find(".cproductid_name").text() + " ";
				}
			});
			if (flag != "") {
				var noticeText = flag + "暂无价格，不可下单！请修改退货单！";
				$(".order_single_width").append(tips.noticeBarBlue(noticeText, "org"));
				$(".J-order-commit").hide();
				return false;
			} else {
				return true;
			}
		};

		validateForm();

		$(".J-order-commit").live('click', function() {
			/*
			 * CA签名
			 */
			var strSn = "";
			// FIXME 需要测试
			if (ca) {
				try {
					strSn = getSN(G.userCode);
				} catch (e) {

				}
				if (!strSn) {
					dialog.alert({
						title : "提示",
						content : "请插入UKey后再进行操作！"
					});
					return;
				}
				$("input[name$=nheadsummnycadata]").each(function(i, input) {
					var strValue = $(input).val();
					var strSignData = G.userid + "|" + strValue;
					var signature = signmessage(strSignData, G.userCode);
					// 处理特殊字符,暂不打开
					// signature =
					// encodeURIComponent(signature);
					$(input).parent().find('input[name$=vsignature]').val(signature);
				});
			}

			var option = {
				title : "提交退货单",
				content : "正在提交退货单..."
			};
			dialog.loading(option);
			var param = $("#orderCommitForm").serialize();
			param += "&casn=" + strSn;

			$.ajax({
				type : "POST",
				url : G.API.ORDER_RETURN_COMMIT,
				data : param,
				success : function(msg) {
					orderEffect.createOrderSuccess(msg, "return");
				}
			});
			return false;
		});

		/**
		 * 修改退货单
		 */
		$(".J-order-modify").live('click', function() {
			$("#orderCommitForm").attr("action", G.API.ORDER_RETURN_MODIFY).submit();
			return false;
		});

		// 合计
		_price.calculatePrice();

		return false;
	});
});
