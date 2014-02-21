define(function(require, exports, module) {
	var $ = require("$");
	// var Assert = require("assert");

	/**
	 * 常用正则表达式
	 */
	var REG_EXP = {
		NUMBER : /^[0-9]+\.{0,1}[0-9]{0,2}$/,
		FLOAT : /^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$/,
		DOUBLE : /^[-\+]?\d+(\.\d+)?$/,
		INT : /^[-\+]?\d+$/,
		QQ : /^[1-9]\d{4,8}$/,
		CHINESE : /^[\u0391-\uFFE5]+$/,
		ENGLISH : /^[A-Za-z]+$/,
		EMAIL : /^\w+([-+.]\w+)*@\w+([-.]\\w+)*\.\w+([-.]\w+)*$/,
		URL : /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/,
		MOBILE : /^((\(\d{3}\))|(\d{3}\-))?13\d{9}$/,
		PHONE : /^((\(\d{3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}$/,
		ID : /^\d{15}(\d{2}[A-Za-z0-9])?$/,
		PASSWORD : /^[\\w]{6,12}$/
	};

	var MSG = {
		NUMBER : '请填写数字',
		FLOAT : '请填写小数',
		DOUBLE : '请填写双精度数字',
		INT : '请填写正数',
		QQ : '请填写正确的QQ号码',
		CHINESE : '请填写中文',
		ENGLISH : '请填写英文字符',
		EMAIL : '请填写正确的email',
		URL : '请填写正确的网络地址',
		MOBILE : '请填写正确的手机号码',
		PHONE : '请填写正确的电话号码',
		ID : '请填写正确的身份证号码',
		PASSWORD : '请使用合法字符',
		NOT_EMPTY : '不能为空',
		MAX : '最大值不能超过',
		MIN : '最小值不能超过',
		MAX_LENGTH : '字符个数不能超过',
		MIN_LENGTH : '字符个数不能少于'
	};

	/**
	 * 规则
	 */
	var RULES = {
		NUMBER : 'NUMBER',
		FLOAT : 'FLOAT',
		DOUBLE : 'DOUBLE',
		INT : 'INT',
		QQ : 'QQ',
		CHINESE : 'CHINESE',
		ENGLISH : 'ENGLISH',
		EMAIL : 'EMAIL',
		URL : 'URL',
		MOBILE : 'MOBILE',
		PHONE : 'PHONE',
		ID : 'ID',
		PASSWORD : 'PASSWORD',
		NOT_EMPTY : 'notEmpty',
		MAX : 'max',
		MIN : 'min',
		MAX_LENGTH : 'maxLength',
		MIN_LENGTH : 'minLength'
	};

	var VALIDATOR = {
		notEmpty : function(value) {
			return !!value;
		},
		max : function(value, data) {
			return value <= data;
		},

		min : function(value, data) {
			return value >= data;
		},
		equals : function(value, data) {
			return value == data;
		}
	};

	/**
	 * 获取错误信息
	 */
	function getMsg(rule) {
		return MSG[rule];
	}

	/**
	 * 校验
	 */
	function validate(configs) {

		$(configs).each(function() {

			var config = this;
			var value = config.value;
			var rules = config.rules;
			var regexp = config.regexp;
			var handler = config.handler ? config.handler : function() {
			};

			var isValid = true;

			// Validation.validate({
			// value:'aa',
			// rules:Validation.RULES.NUMBER,
			// handler:function(value, isValid) {
			// console.log(isValid);
			// }
			// });

			if (typeof (rules) == 'string') {
				rules = [ rules ];
			}

			// [1] 自定义正则表达式
			if (regexp) {
				isValid = regexp.test(value);
				handler(value, isValid);
				return isValid;
			}

			var msg = [];
			$(rules).each(function() {

				// [2] 自定义验证器
				var validator = this.validator;
				if (validator && typeof (validator) == 'function') {
					isValid = validator(value);
					return isValid;
				}

				// [3] 内置正则表达式
				var regExp = REG_EXP[this] || REG_EXP[this.rule];
				if (regExp) {
					isValid = regExp.test(value);
					if (!isValid) {
						msg.push(getMsg(this || this.rule));
					}
					return isValid;
				}

				// [4] 内置验证器
				var validator = VALIDATOR[this] || VALIDATOR[this.rule];
				var data = this.data;
				if (validator) {
					isValid = validator(value, data);
					return isValid;
				}
			});

			if (handler) {
				if (isValid) {
					handler(value, isValid);
				} else {
					var message = null;
					if (msg || msg.length > 0) {
						message = msg.join(',');
					}
					handler(value, isValid, message);
				}
			}
		});
	}

	module.exports = {
		RULES : RULES,
		REG_EXP : REG_EXP,
		MSG : MSG,
		VALIDATOR : VALIDATOR,
		validate : validate
	};

});