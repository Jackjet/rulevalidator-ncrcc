/*
 * 组件示例
 */

define(function(require, exports, module) {
			//默认引入：组件继承自widget
			var Widget = require("widget");

			//组件的定义 组件名大写
			var Example = Widget.extend({
						say:function(){
							alert("我是example示例");
						}
					});
			//组件对外提供使用
			module.exports = Example;
		});