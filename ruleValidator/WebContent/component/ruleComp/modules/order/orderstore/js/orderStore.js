define(function(require, exports, module) {
	var $ = require('$');
	var store = require('store');
	var StringUtils = require('stringUtils');
	var Uri = require('jsuri');

	var ORDER = 'order';
	var TYPE_ORDER = 'order';
	var TYPE_RETURN = 'return_order';

	/**
	 * 获取缓存
	 */
	function getCache() {
		return store.get(ORDER) || {};
	}

	/**
	 * 设置缓存
	 */
	function setCache(cache) {
		store.set(ORDER, cache);
	}

	/**
	 * 获取当前订单编码
	 */
	function getCurrentOrderCode() {
		var cache = getCache();
		return cache[TYPE_ORDER];
	}

	/**
	 * 获取当前退货单编码
	 */
	function getCurrentReturnOrderCode() {
		var cache = getCache();
		return cache[TYPE_RETURN];
	}

	/**
	 * 设置当前订单编码
	 */
	function setCurrentOrderCode(code) {
		var cache = getCache();
		cache[TYPE_ORDER] = code;
		setCache(cache);
	}

	/**
	 * 设置当前退货单编码
	 */
	function setCurrentReturnOrderCode(code) {
		var cache = getCache();
		cache[TYPE_RETURN] = code;
		setCache(cache);
	}

	module.exports = {
		getCurrentOrderCode : getCurrentOrderCode,
		getCurrentReturnOrderCode : getCurrentReturnOrderCode,
		setCurrentOrderCode : setCurrentOrderCode,
		setCurrentReturnOrderCode : setCurrentReturnOrderCode
	};
});