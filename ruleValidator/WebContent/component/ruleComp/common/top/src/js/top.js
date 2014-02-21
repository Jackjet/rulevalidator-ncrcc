/*
 * 组件示例
 */

define(function(require, exports, module) {

	// 默认引入：组件继承自widget
	var Widget = require("widget");
	// 自定义：组件对应的css
	require('../theme/{theme}/css/top.css');
	// 自定义：组件的html模板
	var template = require("./top.tpl");
	// 组件的定义 组件名大写
	var Example = Widget.extend({
				template : template,
				afterRender :function(){
					Example.superclass.afterRender.apply(this, arguments);
				}
			});
	// 组件对外提供使用
	module.exports = Example;
});