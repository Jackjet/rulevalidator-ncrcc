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

	SelectField = Field.extend({
		initAttr : function(config) {
			config.model = $.extend(Utils.clone(defaultModel), config.model);
			SelectField.superclass.initAttr.apply(this, arguments);
		},
		parseElement : function() {
			SelectField.superclass.parseElement.apply(this, arguments);
		},

		afterRender : function() {
			SelectField.superclass.afterRender.apply(this, arguments);

			// 附加dom
			var template = require('./template/field_select.tpl');
			var model = this.get('model');
			var html = handlebars.compile(template)(model);
			this.element.find('label').after(html);

			var widget = this.getWidget();
			var that = this;
		},
		/**
		 * 获取值
		 */
		getValue : function() {
			return this.getWidget().find('select option:select').val();
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
	});

	module.exports = SelectField;

});