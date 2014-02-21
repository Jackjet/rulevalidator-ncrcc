define(function(require, exports, module) {
	var $ = require("$");
	var Widget = require("widget");
	
	exports = Widget.extend({
		template : require('./template/order_row_edit.tpl'),
		afterRender : function() {
			this.constructor.superclass.afterRender.apply(this, arguments);
		}
	});
});