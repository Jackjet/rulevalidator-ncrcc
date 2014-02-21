/**
 * 信用额度检查工具
 */
define(function(require, exports, module) {
	var $ = require("$");
	require('../theme/{theme}/css/notice.css');
	var creditPainter = require('account_creditPainter');

	/**
	 * 信用额度检查
	 */
	function check() {
		$('.J-order').each(function(i, order) {
			// 修改隐藏域中的当前订单序号
			$('input[name=index]').val(i);
			$.ajax({
				type : "POST",
				url : G.API.CREDIT_SINGLE_CHECK/* + '?index=' + i*/,
				data : $('form').serialize(),
				success : function(data) {
					// 查询报错
					if (data.message || data.detail) {
						handleException(i);
						return;
					}
					if (data.creditCheck) {
						var occr = data;
						// 绘制信用额度图表
						draw(i, occr);
						// 信用额度检查结果提示
						if (!occr.ok) {
							// 单个订单开启额度检查 && 未通过检查
							// 超额
							notice('第<a href="#order[' + i + ']">[' + (i + 1) + ']</a>个订单商品总额超过了信用额度');
						}
					}
				}
			});
		});
	}

	/**
	 * 处理异常（查询系统参数） 如果要信用检查，图表区域提示文字，顶部提示信用检查未通过； 如果不需要信用检查，隐藏信用图表区域，顶部不提示；
	 */
	function handleException(index) {
		var orgId = $('.J-order:eq(' + index + ')').find('.order_base_info').find('input[name$=pk_org]').val();
		$.ajax({
			type : "GET",
			url : G.API.CREDIT_ISCREDITCHECK,
			data : {
				orgIds : orgId
			},
			success : function(data) {
				if (!data.message) {
					var isCreditCheck = data[0];
					if (isCreditCheck) {
						// 要信用检查
						// 1）图表区域提示文字，
						var chart = $($('.J_bar_order')[index]).find('.order_credit');
						chart.show();
						var html = '<center style="color:red">没有信用额度记录</center>';
						chart.find('.order_credit_chart').css({
							height : 50 + 'px'
						}).html(html);

						// 2）顶部提示信用检查未通过
						notice('第<a href="#order[' + index + ']">[' + (index + 1) + ']</a>个订单商品总额超过了信用额度');
					}
				}
			}
		});
	}

	/**
	 * 绘制某个订单的信用图表
	 */
	function draw(i, occr) {
		if (occr.creditCheck) {
			// 需要检查信用额度 => 展示图表区域
			$('.J-order:eq(' + i + ')').find('.order_credit').show();
		}
		var credits = occr.results;
		if (credits && credits.length > 0) {
			new creditPainter().draw({
				containerId : 'credit' + i,
				creditArray : credits,
				isTrigger : true
			});
		}
	}

	/**
	 * 展示提示
	 */
	function notice(msg) {
		var contentItem = '';
		contentItem += '<div class="J-notice-content-item">';
		contentItem += '<span class="warning_icon"></span>' + '<span style="height: 23px;">';
		contentItem += msg;
		contentItem += '</span>'/* + '<br>'*/;
		contentItem += '</div>';
		if ($('.notice_bar').length > 0) {
			// 已经有提示区域
			$('.notice_bar .J-notice-content-item:last').after(contentItem);
		} else {
			var html = '';
			html += '<div class="notice_bar">';
			html += '<!-- 上部 -->';
			html += '<div class="corner lt"></div>';
			html += '<div class="top"></div>';
			html += '<div class="corner rt"></div>';
			html += '<!-- 内容 -->';
			html += '<div class="content">';
			html += '<div class="text">';
			html += '<div class="floatcenter J-notice-content" style="width: 431px;">';
			html += '<!-- 内容条目 -->';
			html += '</div>';
			html += '</div>';
			html += '</div>';
			html += '<!-- 下部 -->';
			html += '<div class="corner lb"></div>';
			html += '<div class="bottom"></div>';
			html += '<div class="corner rb"></div>';
			html += '</div>';
//			$('.notice_bar').empty();
			$('.order_confirm_flow').after(html);
			$('.notice_bar .J-notice-content').append(contentItem);
		}
	}

	module.exports = {
		check : check
	};
});