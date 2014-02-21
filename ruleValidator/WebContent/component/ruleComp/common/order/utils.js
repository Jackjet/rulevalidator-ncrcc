define(function(require, exports, module) {

	var $ = require('$');
	var JSON = require('json');

	/**
	 * 复制一个对象
	 */
	function clone(json) {
		return JSON.parse(JSON.stringify(json));
	}

	/**
	 * 获取jquery对象
	 */
	function getEl(selector) {
		if (typeof (selector) == 'string') {
			return $(selector);
		}
	}
	module.exports = {
		clone : clone,
		getEl : getEl
	};

});