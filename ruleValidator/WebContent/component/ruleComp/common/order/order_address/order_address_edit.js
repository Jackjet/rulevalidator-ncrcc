define(function(require, exports, module) {
	var $ = require("$");
	require('order_theme');
	var Widget = require("widget");
	
	module.exports = Widget.extend({
		template : require('./template/order_address_edit.tpl'),
		afterRender : function() {
			this.constructor.superclass.afterRender.apply(this, arguments);
		}
	});
});