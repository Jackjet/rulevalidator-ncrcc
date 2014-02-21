/*
 * Bar组件
 */

define(function(require, exports, module) {
			//如果需要覆写css 则使用async 延迟加载
			require("../theme/{theme}/css/ecpbar.css");
			var Bar = require("common/bar/src/js/bar");
			var EcpBar = Bar.extend({
				classPrefix:"ui-ecp"
			});
			module.exports = EcpBar;
		});