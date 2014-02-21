
/**
 * 价格计算联动
 * 
 */
define(function  (require, exports, module) {
	var $ =require("$");
	var big = require('BigDecimal');
	var BigDecimal = big.BigDecimal;
	var MathContext = big.MathContext;
	
	/**
	 * validator
	 */
	function validator(input) {
		var value = $(input).val();
		// 先把非数字的都替换掉，除了数字和.
		value = value.replace(/[^\d.]/g, "");
		// 必须保证第一个为数字而不是.
		value = value.replace(/^\./g, "");
		// 保证只有出现一个.而没有多个.
		value = value.replace(/\.{2,}/g, ".");
		// 保证.只出现一次，而不能出现两次以上
		value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
		if (parseFloat(value.value) <= 0) {
			value = '1';
		}
		
		var scaleStr = $(input).attr('scale');
		var scale = parseInt($.trim(scaleStr));
		
		if (value != null && value != undefined && value != "" && parseFloat(value) >= 0) {
			if (scaleStr) {
				var bd = new BigDecimal(value).setScale(scale, MathContext.ROUND_HALF_UP);
	 			$(input).val(bd).attr('title', bd);
			}
			return true;
		} else {
//			value = '1';
			value = new BigDecimal('1').setScale(scale, MathContext.ROUND_HALF_UP);
 			$(input).val(value).attr('title', value);
			return false;
		}
	}

	/**
	 * 计算合计金额
	 */
	var caclTotalSummy = function() {
		
		// 计算订单行金额和合计
		var total = new BigDecimal('0');
		$("input.ET_caclNumber").each(function(){
			var dom_tr = $(this).parents("tr");
			var caclNumber = new BigDecimal($.trim($(this).val()) == '' ? '0' : $.trim($(this).val()));
			var caclPrice = new BigDecimal($.trim(dom_tr.find(".ET_caclPrice").text()) == '' ? '0' : $.trim(dom_tr.find(".ET_caclPrice").text()));
			var scale = parseInt(dom_tr.find(".ET_caclSummy").attr("scale"));
			var norigtaxmny = caclNumber.multiply(caclPrice).setScale(scale, MathContext.ROUND_HALF_UP);
			dom_tr.find(".ET_caclSummy").text(norigtaxmny).attr('title', norigtaxmny);
			total = total.add(norigtaxmny);
		});
////		var total = 0.00;
//		// 找到该地址所有的金额input
//		$("span.ET_caclSummy").each(function() {
////			total = Maths.add(parseFloat(total),
////					formatter.fmtToFloat($(this).text()));
//			var str = $.trim($(this).text()) == '' ? '0' : $.trim($(this).text());
//			total = total.add(new BigDecimal(str));
//		});
		var scale = $(".ET_caclTotal").attr("scale");
		scale = isNaN(scale) ? 4 : parseInt(scale);
//		total = formatter.fmtNum(total, scale);
		total = total.setScale(scale, MathContext.ROUND_HALF_UP);
		$(".ET_caclTotal").text(total).attr('title', total);
	};
	/**
	 * 计算对外接口
	 */
	exports.cacl = function(){
		caclTotalSummy();
	};
	exports.init = function(option){
		// 价格联动计算
		$("body").delegate("input.ET_caclNumber", "blur", function() {
			validator(this);
			caclTotalSummy(this);
		});
		caclTotalSummy();
	};
	
});