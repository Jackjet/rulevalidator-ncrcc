define(function(require, exports, module) {
	var $ = require("$");
	var handlebars = require('handlebars');
	var Utils = require('utils');
	var Field = require("field");
	var defaultModel = {
		name : '',
		value : '',
		fieldId : '',
		fieldCls : '',
		filedStyle : '',
		placeholder : '',
		validator : function() {
			return ture;
		}
	};

	var TextField = null;
	TextField = Field.extend({
		initAttr : function(config) {
			// TextField.superclass.initAttr.apply(this, arguments);
			// this.set('model', $.extend(this.get('model'),
			// Utils.clone(defaultModel)));
			// this.set('model', $.extend(this.get('model'), config));

			config.model = $.extend(Utils.clone(defaultModel), config.model);
			TextField.superclass.initAttr.apply(this, arguments);
		},
		parseElement : function() {
			TextField.superclass.parseElement.apply(this, arguments);
		},

		afterRender : function() {
			TextField.superclass.afterRender.apply(this, arguments);

			// 附加dom
			var template = require('./template/field_text.tpl');
			var model = this.get('model');
			var html = handlebars.compile(template)(model);
			this.element.find('label').after(html);

			var widget = this.getWidget();
			// var model = this.get('model');
			var that = this;
			// widget.find('input').blur(function() {
			// that.validate();
			// });
			// widget.find('input').blur(function() {
			// that.emit('field_validate');
			// });
		},
		/**
		 * 获取值
		 */
		getValue : function() {
			return this.getWidget().find('input').val();
		},
		/**
		 * 展现
		 */
		show : function() {
			this.getWidget().show();
		},
		/**
		 * 隐藏
		 */
		hide : function() {
			this.getWidget().hide();
		},
		bindValidator : function() {
			var that = this;
			this.getWidget().find('input').blur(function() {
				that.validate();
			});
		}
	// ,
	// /**
	// * 校验
	// */
	// validate : function() {
	// var attrs = this.get('attrs');
	// var validator = attrs.validator;
	// var isValid = true;
	// if (typeof(validator) == 'function') {
	// isValid = validator(this.getValue());
	// } else {
	// isValid = Validation.validate(validator);
	// }
	// if (!isValid) {
	//				
	// }
	// },
	});

	module.exports = TextField;

});