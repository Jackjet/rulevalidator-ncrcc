/*
 * Bar组件
 */
define(function(require, exports, module) {
	
			var cssurl = '../theme/default/css/bar.css';
			require('../theme/default/css/bar.css');

			var bar_tpl = require("./bar.tpl");

			var Widget = require("widget");
			var Bar = Widget.extend({
						template : bar_tpl
					});
			module.exports = Bar;
		});