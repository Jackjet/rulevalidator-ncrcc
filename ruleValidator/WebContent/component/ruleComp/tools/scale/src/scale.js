/**
 * 精度处理
 */
define(function(require, exports, module) {
	var $ = require("$");
	//	var fmtNum = require("formatter").fmtNum;
	var big = require('BigDecimal');
	var BigDecimal = big.BigDecimal;
	var MathContext = big.MathContext;
//	// 数字
//	var regExpNumber = /^\d+(?:\.\d+)?$/;
//	// 业务逻辑（整数部分最多18位，小数部分最多8位）
//	var regExpValueNumber = /^\d{1,18}(\.\d{1,8})?$/;

	var Scale = function() {
		/**
		 * 精度过滤
		 */
		function setScaleFilter(ele) {
			var element;
			// 设置精度
			if(ele){
				element=ele;
			}else{
				element=$('*[scale]');
			}
			element.each(function() {
				var scale = parseInt($.trim($(this).attr("scale")));
				var value = '';
//				var value = $.trim($(this).text());
				if ($(this).is("span") || $(this).is("a")) {
					value = $.trim($(this).text());
					var s;
					if (/^-?\d+(?:\.\d+)?$/.test(value)) {
						s = new BigDecimal(value).setScale(scale, MathContext.ROUND_HALF_UP);
					}
					$(this).text(s).attr("title", s);
				} else if ($(this).is("input")) {
					value = $.trim($(this).val());
					var s;
					if (/^\d+(?:\.\d+)?$/.test(value)) {
						s = new BigDecimal(value).setScale(scale, MathContext.ROUND_HALF_UP);
					} else {
						if($(this).attr("empty")!="true"){
							s = new BigDecimal('1').setScale(scale, MathContext.ROUND_HALF_UP);
						}
					}
					$(this).val(s).attr("title", s);
				} else if ($(this).is("td")) {
					value = $.trim($(this).text());
					var s = new BigDecimal(value).setScale(scale, MathContext.ROUND_HALF_UP);
					$(this).append($('<span>').text(s)).attr("title", s);
				}
			});
		}

		/**
		 * 编辑框动态绑定精度处理方法
		 */
		function binddScaleBlurEvent() {
//			$("body").undelegate("*[scale]", "blur").delegate("*[scale]", "blur", function() {
			$('body').delegate("*[scale]", "blur", function() {
				if ($(this).is("input")) {
					var value = $.trim($(this).val());
					var scale = parseInt($.trim($(this).attr('scale')));
					if (/^\d+(?:\.\d+)?$/.test(value)) {
						// [0] 数字
						
						// [0.0] 0的处理
						if (!($(this).attr("zero") == "true") && value == '0' && $(this).attr("empty")!="true") {
//							alert('数量不能为0');
							var s = new BigDecimal('1').setScale(scale, MathContext.ROUND_HALF_UP);
							$(this).val(s).attr('title', s);
							return true;
						}
						
						// [0.1] 越界
						if (!/^\d{1,18}(\.\d{1,8})?$/.test(value)) {
							// [1.0.2] 越界
//							alert('整数部分不能超过18位，小数部分不能超过8位');
							if($(this).attr("empty")!="true"){
								var s = new BigDecimal('1').setScale(scale, MathContext.ROUND_HALF_UP);
								$(this).val(s).attr('title', s);
							}
							return true;
						} else {
							var s = new BigDecimal(value).setScale(scale, MathContext.ROUND_HALF_UP);
							$(this).val(s).attr('title', s);
							return true;
						}
					} else {
						// [1] 非数字
						if($(this).attr("empty")!="true"){
							var s = new BigDecimal('1').setScale(scale, MathContext.ROUND_HALF_UP);
							$(this).val(s).attr('title', s);
						}
						return true;
					}
					return true;
				}
			});
		}

		this.init = function() {
			setScaleFilter();
			binddScaleBlurEvent();
			seajs.on("scale", function(ele) {
				setScaleFilter(ele);
				binddScaleBlurEvent();
			});
		};
	};

	var scale = new Scale();
	$(function() {
		scale.init();
	});
	module.exports = scale;
});