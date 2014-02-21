/**
 * 格式化工具类
 * 
 */
define(function(require, exports, module) {
	var big = require('BigDecimal');
	var BigDecimal = big.BigDecimal;
	var MathContext = big.MathContext;
	var DateFormat = require("./dateformat");
	// 新加的
	var math = require("maths");
	/**
	 * 格式化数字
	 */
	exports.fmtNum = fmtNum;
	/**
	 * 格式化日期
	 */
	exports.fmtDate = fmtDate;
	exports.fmtLongDate = fmtLongDate;
	exports.fmtStrDate = fmtStrDate;

	exports.fmtToFloat = fmtToFloat;

	/**
	 * 四舍五入
	 * 
	 * @param {}
	 *            value
	 * @param {}
	 *            num
	 * @return {String}
	 */
	function adv_format(value, num) {// 
		if (isNaN(num) && isNaN(value)) {
			return 0;
		}
		var num = parseInt(num);
		var a_int = parseInt(value * Math.pow(10, (num + 1)));// 乘以10的num+1次方
		if (a_int == 0)
			return "0";
		var a_str = a_int.toString();// 转成字符串
		// 正数
		if (a_str.substring(0, 1) != "-") {
			var b_str = a_str.substring(a_str.length - 1, a_str.length);// 取最后一位
			var b_int = parseInt(b_str);
			if (b_int >= 5)
				a_int = a_int + 10;
		} else {
			// 负数
			var b_str = a_str.substring(a_str.length - 1, a_str.length);
			var b_int = parseInt(b_str);
			if (b_int >= 5)
				a_int = a_int - 10;
		}
		a_str = a_int.toString();
		// 小数点左侧取的位数
		var leftlength = a_str.length - (num + 1);
		// 小数点右侧取的位数
		var rightlength = (num + 1);
		// 小数点左侧的数
		var leftstr = a_str.substring(0, leftlength);
		var rightstr = a_str.substring(a_str.length - rightlength, a_str.length);
		// 小数点右侧的数
		rightstr = rightstr.substring(0, num);
		// 重新组合字符串
		var c_str = leftstr + "." + rightstr;
		if (value.toString().substring(0, 3) == "-0.")
			c_str = "-0." + rightstr;
		if (value.toString().substring(0, 2) == "0.")
			c_str = "0." + rightstr;
		return c_str;
	}

	/**
	 * 不处理四舍五入，直接截取精度
	 * 
	 * @param {}
	 *            value 数值
	 * @param {}
	 *            scale 精度
	 */
	function fmtNum(value, scale) {
		return new BigDecimal(value + '').setScale(parseInt(scale), MathContext.ROUND_HALF_UP).toString();
//		//filter NaN
//		if (isNaN(value)) {
//			value = 1;
//		}
//		if (isNaN(num)) {
//			num = -1;
//		}
//		var num = parseInt(num);
//		if (num == -1) {
//			return math.mul(value, 1);
//		}
//		value = round(value, num);
//		var nbr;
//		switch (typeof (value)) {
//		case "string":
//			nbr = value;
//			break;
//		case "number":
//			nbr = value.toString();
//			break;
//		default:
//			break;
//		}
//		// 获取整数部分
//		/*
//		 * if(nbr=="0"||nbr==""){ return 0; }
//		 */
//
//		var int1 = nbr.split(".")[0];
//		if (num <= 0) {
//			return int1;
//		} else {
//			// 获取小数部分
//			var dec = typeof (nbr.split(".")[1]) == "undefined" ? "0" : nbr.split(".")[1];
//			tmp = num - dec.length;
//			for ( var i = 0; i < tmp; i++) {
//				dec = dec + "0";
//			}
//
//			return int1 + "." + dec;
//		}
	}

	/**
	 * 格式化日期
	 * 
	 * @param {}
	 *            jsonDate
	 * @return {}
	 */
	function fmtDate(jsonDate) {
		var dateFormat = new DateFormat("yyyy-MM-dd");
		// var date = eval(jsonDate.replace(/\/Date\((\d+)\)\//gi, "new
		// Date($1)"));
		if (jsonDate) {
			return dateFormat.format(new Date(jsonDate));
		} else {
			return "";
		}
	}
	function fmtLongDate(jsonDate) {
		var dateFormat = new DateFormat("yyyy-MM-dd HH:mm:ss");
		// var date = eval(jsonDate.replace(/\/Date\((\d+)\)\//gi, "new
		// Date($1)"));
		if (jsonDate) {
			return dateFormat.format(new Date(jsonDate));
		} else {
			return "";
		}
	}

	function fmtStrDate(jsonDate) {
		if (jsonDate) {
			return jsonDate.substr(0, 10);
		} else {
			return "";
		}
	}
	/**
	 * 
	 * @param {}
	 *            v 四舍五入的值
	 * @param {}
	 *            e 精度
	 * @return {}
	 */
	function round(v, e) {
		var t = 1;
		for (; e > 0; t *= 10, e--)
			;
		for (; e < 0; t /= 10, e++)
			;
		return Math.round(v * t) / t;
	}
	/**
	 * 去掉数组中的重复值
	 * 
	 * @param {}
	 *            str
	 * @return {}
	 */
	function uniqueStr(str) {
		var s = str.split(',');
		var dic = {};
		for ( var i = s.length; i--;) {
			dic[s[i]] = s[i];
		}
		var r = [];
		for ( var v in dic) {
			r.push(dic[v]);
		}
		return r.join();
	}
	/**
	 * 将floatNum转换为数字
	 * 
	 * @param {}
	 *            floatNum
	 * @param {}
	 *            scale
	 * @return {}
	 */
	function fmtToFloat(floatNum) {
		if (floatNum == null || floatNum == 0) {
			return 0;
		} else {
			return parseFloat(floatNum.replace(/[^\d\.-]/g, ""));
		}
	}
});
