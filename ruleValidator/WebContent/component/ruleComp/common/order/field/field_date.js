define(function(require, exports, module) {
	var $ = require("$");
	require('./css/field.css');
	$ = require("jquery.ui.datepicker")($);
	//	var Utils = require('utils');
	var TextField = require('field_text');

	DateField = TextField.extend({

		initAttr : function() {
			DateField.superclass.initAttr.apply(this, arguments);
			var fieldCls = this.get('model').fieldCls;
			this.get('model').fieldCls = fieldCls ? fieldCls + ' datepicker ui-field-datepicker' : 'datepicker ui-field-datepicker';
		},
		afterRender : function() {
			DateField.superclass.afterRender.apply(this, arguments);
			seajs.emit('datepicker');
		},
		bindValidator : function() {
//			var that = this;
//			this.getWidget().find('input').blur(function() {
//				that.validate();
//			});
//			this.getWidget().find('.ui-datepicker-div').live('blur', function() {
//				console.log('blur');
//			});
//			$('body').delegate('.ui-datepicker-div').live('blur', function() {
//				console.log('blur');
//			});
		}
	});

	module.exports = DateField;

});