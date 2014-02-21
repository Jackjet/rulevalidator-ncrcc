define(function(require, exports, module) {
	require('./theme/default/css/credit.css');
	var errorMessageTemplate = require('./credit_error.tpl');
	var handlebars = require('handlebars');

	var CreditPainter = require("./CreditPainter");
	var $ = require("$");
	$ = require("jquery.extend")($);
	/**
	 * 常量
	 */
	var constant = {
		id : {
			accountDetail : 'account_accountDetail',
			updatePassword : 'account_updatePassword',
			credit : 'account_credit'
		},
		color : {
			balancemny : 'menu', // 颜色（信用余额）
			engrossmny : 'inactivecaption', // 颜色（信用占用）
			limitmny : '' // 颜色（信用额度）
		},
		url : {
			getCredit : G.API.CREDIT // 信用额度
		}
	};

	/**
	 * 加载信用额度页面
	 */
	function loadCreditPage() {
		$.ajax({
			type : "GET",
			url : constant.url.getCredit,
			dataType : 'json',
			success : function(data) {
				new CreditPainter().draw({
					containerId : 'center_content',
					creditArray : data,
					isTrigger : false
				});
			},
			error : function(response) {
				var responseText = response.responseText;
				var message = '您没有设置信用额度';
				var html = handlebars.compile(errorMessageTemplate)({
					message:message,
					detail:responseText,
				});
				$('#center_content').html(html);
				throw new Error('信用额度查询出错');
			}
		});
	}
	$(function() {
		loadCreditPage();
	});
});