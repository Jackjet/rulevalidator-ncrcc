define(function(require, exports, module) {
	//主题
	require('../theme/default/exception.css');
	var Widget = require('widget');
	var $ = require("$");
	
	module.exports = Widget.extend({
		template:require('./exception.tpl'),
		setup:function() {
			
			// 绑定ALT + F12功能键
			$(document).live('keyup', function(e) {
				var event = window.event || e;
				var code = event.keyCode || event.which;
				var keyCode_F12 = 123;
				if (event.altKey && code == keyCode_F12) {
					$('.ui-exception .detail').toggleClass('hidden');
				}
			});
			
			// 跳转主页链接
			$('.returnToMain').live('click', function() {
				document.location = G.ecp || '.';
			});
			
		},
		afterRender:function() {
			
			// 将异常信息赋值到目标区域
			$('.ui-exception .message').text($('.ui-error .message').text());
			$('.ui-exception .detail').text($('.ui-error .detail').text());
			
		}
	});
});