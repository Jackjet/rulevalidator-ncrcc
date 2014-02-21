define(function(require, exports, module) {
	var $ = require("$");
	require('./css/field_validation.css');
	var Validation = require('validation');
	var handlebars = require('handlebars');

	var POSITION = {
		SIDE : 'side',
		BOTTOM : 'bottom'
	};
	
	/**
	 * 展示验证信息
	 */
	function addErrorMsg(renderTo, msg, position) {
		var model = {
			msg : msg
		};
		var template = require('./template/field_validation.tpl');
		var html = handlebars.compile(template)(model);
		if (typeof (renderTo) == 'string') {
			renderTo = $(renderTo);
		}
		switch (position) {
		case POSITION.SIDE:
			renderTo.after(html);
			break;
		case POSITION.BOTTOM:
			break;
		}
	}

	/**
	 * 删除错误信息
	 */
	function removeErrorMsg(renderTo, position) {
		switch (position) {
		case POSITION.SIDE:
			if (renderTo.next().hasClass('ui-validate-error')) {
				renderTo.next().remove();
			}
			break;
		case POSITION.BOTTOM:
			break;
		}
	}

	function _validate(config) {
		var renderTo = config.renderTo;
		if (typeof (renderTo) == 'string') {
			renderTo = $(renderTo);
		}
		var value = config.value;
		if (typeof (value) == 'function') {
			value = value();
		}
		var rules = config.rules;
		var errorMsg = config.errorMsg;
		var position = config.position || POSITION.SIDE;

		Validation.validate({
			value : value,
			rules : rules,
			handler : function(value, isValid, msg) {
				removeErrorMsg(renderTo, position);
				if (!isValid) {
					addErrorMsg(renderTo, errorMsg || msg, position);
				}
			}
		});
	}

	/**
	 * 删除某个容器下的提示
	 */
	function remove(container) {
		if (typeof (container) == 'string') {
			container = $(container);
		}
		container.find('.ui-validate-error').remove();
	}

	/**
	 * 删除所有提示
	 */
	function removeAll() {
		$('body').find('.ui-validate-error').remove();
	}

	/**
	 * 校验
	 */
	function validate(config) {
		if ($.isArray(config)) {
			$(config).each(function() {
				_validate(this);
			});
		} else {
			_validate(config);
		}
	}

	module.exports = {
		validate : validate,
		remove : remove,
		removeAll : removeAll,
		POSITION : POSITION
	};

});