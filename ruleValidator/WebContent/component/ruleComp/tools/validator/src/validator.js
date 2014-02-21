/**
 * 校验
 */
define(function(require, exports, module) {
	// 数字类型（整数部分最多18位，小数部分最多8位）
	var REG_EXP_NUMBER = /^\d{1,18}(\.\d{1,8})?$/g;
	
	/**
	 * 校验数字
	 */
	function validateNumber(input) {
		
		if (input.tagName != 'INPUT') {
			throw new Error('只能处理input元素');
		}
		
		var value = input.val();
		if (value && REG_EXP_NUMBER.test(value)) {
			return true;
		} else {
			false;
		}
	}
	
	var Validator = {
		/**
		 * 其他类型转化为数字
		 */
		validatorData : function(obj) {
			// 先把非数字的都替换掉，除了数字和.
			obj.value = obj.value.replace(/[^\d.]/g, "");
			// 必须保证第一个为数字而不是.
			obj.value = obj.value.replace(/^\./g, "");
			// 保证只有出现一个.而没有多个.
			obj.value = obj.value.replace(/\.{2,}/g, ".");
			// 保证.只出现一次，而不能出现两次以上
			obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
			if (parseFloat(obj.value) <= 0) {
				obj.value = 1;
			}
			if (obj.value != null && obj.value != undefined && obj.value != "" && parseFloat(obj.value) >= 0) {
				return true;
			} else {
				obj.value = 1;
				return false;
			}
		}
	};
	module.exports = Validator;
});
