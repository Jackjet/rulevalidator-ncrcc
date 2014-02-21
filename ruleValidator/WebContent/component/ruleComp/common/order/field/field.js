define(function(require, exports, module) {
	var $ = require("$");
	require('order_theme');
	var Utils = require('utils');
	var Widget = require("widget");
	var Placeholder = require('placeholder');
//	var Validation = require('validation');
	var FieldValidation = require('field_validation');

	var defaultModel = {
		/* 控件配置 */
		id : '',
		cls : '',
		/* 标签配置 */
		labelId : '',
		label : 'label',
		labelCls : '',
		labelStyle : 'width:15%;',
		separator : '：',
		isLabelHidden : false,
		isHidden : false
	};
	var Field = null;
	Field = Widget.extend({
		template : require('./template/field.tpl'),
		initAttr : function(config) {
			config.model = $.extend(Utils.clone(defaultModel), config.model);
			Field.superclass.initAttr.apply(this, arguments);
		},

		afterRender : function() {
			Field.superclass.afterRender.apply(this, arguments);
			Placeholder.init($);
		},
		/**
		 * 获取值
		 */
		getValue : function() {
		},
		/**
		 * 展现
		 */
		show : function() {
		},
		/**
		 * 隐藏
		 */
		hide : function() {
		},
		/**
		 * 校验
		 */
		validate : function() {
			var model = this.get('model');
			var validator = model.validator;
			if (typeof (validator) == 'function') {
				validator(this.getWidget(), this.getValue());
			} else {
				var validationConfig = $.extend({
					renderTo : this.element.find('input, select, textarea'),
					value : this.getValue()
				}, validator);
				FieldValidation.validate(validationConfig);
			}
		},
		// 组件加载
		render : function() {
			this.beforeRender();
			this.element = this.parseElementFromTemplate();
			var parentNode = this.get('parentNode');
			if (parentNode) {
				parentNode.append(this.element);
			}
			this.afterRender();
			if (this.get("editable")) {
				this.editInit();
			}
//			if (seajs.emit) {
//				top.seajs.emit('event_widget_rendered');
//			}
			this.bindValidator();
			return this.element;
		},
		/**
		 * 获取控件
		 */
		getWidget : function() {
			return $(this.element);
		},
		/**
		 * 设置校验
		 */
		bindValidator:function() {
		}
	});

	module.exports = Field;

});