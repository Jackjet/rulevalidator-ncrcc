/**
 * 计算工具类
 */
define(function(require, exports, module) {
	/**
	 * 对外接口
	 */
	exports.add = accAdd;
	/**
	 * 对外接口
	 */
	exports.mul = accMul;
	
	var big = require('BigDecimal');
	var BigDecimal = big.BigDecimal;
	var MathContext = big.MathContext;

	function accMul(arg1, arg2) {
		if(arg1 == ""){
			arg1 = "0";
		}
		if(arg2 == ""){
			arg2 = "0";
		}
		var a = new BigDecimal(arg1+"");
		
		var b = new BigDecimal(arg2+"");
		
		return a.multiply(b).toString();
	}

	function accAdd(arg1, arg2) {
		var a = new BigDecimal(arg1);
		var b = new BigDecimal(arg2);
		return a.add(b).toString();
	}

});