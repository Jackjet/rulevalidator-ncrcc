
/**
 * html5 placeholder 属性 在不支持placeholder的浏览器中通过js来模拟
 * 
 */
define(function(require, exports, module) {
			var $ = require("$");
			// 对外接口 初始化
			exports.init = function() {
				init();
			};
			/**
			 * 初始化
			 */
			var init = function() {
				initValue();
				bindEvent();
			};
			var isSupport = function() {
				return 'placeholder' in document.createElement('input');
			};
			/**
			 * 初始化值
			 */
			var initValue = function() {
				if (!isSupport()) {
					$("input[placeholder]").each(function() {
								$(this).val($(this).attr("placeholder"));
							});
				}
			};
			/**
			 * 绑定事件
			 */
			var bindEvent = function() {
				if (!isSupport()) {
					$("body").delegate("[placeholder]", "focus", function() {
								if ($(this).val() === $(this)
										.attr("placeholder")) {
									$(this).val('');
								}
							});
					$("body").delegate("[placeholder]", "blur", function() {
								if ($(this).val() === '') {
									$(this).val($(this).attr("placeholder"));
								}
							});
				}
			};
		});
