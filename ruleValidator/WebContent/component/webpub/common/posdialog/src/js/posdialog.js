/*
 * 定位dialog
 */
define(function(require, exports, module) {
	// 默认引入：组件继承自widget
	var Widget = require("widget");
	// 加载css文件
	var $ = require("$");
	require.async('../theme/{theme}/css/posdialog.css');
	var nabnum = require("nabnum");
	var scale = require("scale");

	// 自定义：组件的html模板
	var posdialog_tpl = require("./posdialog.tpl");

	// 组件的定义 组件名大写
	var Posdialog = Widget.extend({
		template : posdialog_tpl,
		// 初始化参数
		initCustAttr : function() {
			var attrs = this.get("attrs");
			var buttons = this.get("buttons");
			for ( var i = 0; i < buttons.length; i++) {
				var button = buttons[i];
				buttons[i] = {
					text : button.text || "按钮",
					handler : button.handler,
					style : button.style || "gray"
				};
			}
		},
		afterRender : function() {
			var position = this.get("attrs").position;
			var top = position.top - this.element.height() + "px";
			var left = (position.left - 360) + "px";
			this.element.css({
				position : "absolute",
				top : top,
				left : left
			});

			nabnum.init();
			scale.init();
		},
		// 按钮绑定事件
		bindEvent : function() {
			var that = this;
			$("#posdialogclose").bind("click", function() {
				that.get("parentNode").html("");
			});

			var buttons = this.get("buttons");
			this.element.find(".btns a").each(function(index) {
				$(this).bind("click", buttons[index].handler);
			})
		},

		close : function() {
			this.get("parentNode").html("");
		}
	});
	//组件对外提供使用
	module.exports = Posdialog;
});