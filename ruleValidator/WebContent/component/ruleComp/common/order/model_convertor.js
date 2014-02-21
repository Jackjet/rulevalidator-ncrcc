define(function(require, exports, module) {
	var $ = require("$");
	var StringUtils = require('stringUtils');
	var Assert = require('assert');
	var ModelMapper = require('model_mapper');

	/**
	 * 通过转换映射关系获得模型
	 */
	function getModelByCode(source, mapperCode) {
		var mapper = ModelMapper.getMapper(mapperCode);
		return getModelByMapper(source, mapper);
	}

	/**
	 * 通过映射关系获得模型
	 */
	function getModelByMapper(source, mapper) {

		Assert.notNull(source, '源json为空');
		Assert.notNull(mapper, '映射关系为空');

		var target = {};

		for ( var key in mapper) {
			var value = mapper[key];
			if (value) {
				if (typeof (value) == 'object') {
					target[key] = getModelByMapper(source[key], value);
				} else if (typeof (value) == 'function') {
					target[key] = value(source, source[key]);
				} else {
					target[key] = source[value];
				}
			}
		}
		
		return target;
	}

	module.exports = {
		getModelByCode : getModelByCode,
		getModelByMapper : getModelByMapper
	};
});