define(function(require, exports, module) {
	var StringUtils = require('stringUtils');

	/**
	 * 异常处理
	 */
	function exceptionHandler(obj, message) {
		throw new Error(message);
	}
	
	/**
	 * 不为空
	 */
	function notNull(obj, message) {
		if (!obj) {
			throw new Error(message);
		}
	}

	/**
	 * 为空
	 */
	function isNull(obj, message) {
		if (!obj) {
			throw new Error(message);
		}
	}

	module.exports = {
		isNull : isNull,
		notNull : notNull
	};

});