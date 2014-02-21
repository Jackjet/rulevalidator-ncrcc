/**
 * Created by IntelliJ IDEA. User: Liu Yuhong Date: 12-10-31 Time: 下午4:59 Drop
 * menu plugin
 */
define(function(require, exports, module) {

	require('../theme/{theme}/css/drop_menu.css');
	var dropMenu_tpl = require("./drop_menu.tpl");
	var Widget = require("widget");
	var DropMenu = Widget.extend({
		// 初始化参数
		initCustAttr : function() {
			// 使用set给attr设置属性
			this.set("listWidth", "width: " + (this.get("itemWidth") + 2)
							+ "px; ");
			this.set("menuWidth", "width: " + (this.get("itemWidth") + 10)
							+ "px; ");
			this.set("lineWidth", "width: " + this.get("itemWidth") + "px; ");
			this.set("lineHeight", "height: " + this.get("itemHeight")
							+ "px; lineHeight: " + this.get("itemHeight")
							+ "px; ");
		},
		// 加载数据
		loadData : function() {
			var list = this;
			var url = this.get("url");
			$.ajax({
				type : 'POST',
				url : url,
				success : function(data) {
					list.set("listHeight", "height: "
									+ (list.get("itemHeight") * data.length)
									+ "px");
					list
							.set("borderHeight",
									"height: "
											+ (list.get("itemHeight")
													* data.length + 14) + "px");
					for (var loop = 0; loop < data.length; loop++) {
						data[loop]["lineWidth"] = "width: "
								+ list.get("itemWidth") + "px; ";
					}
					list.setModel(data);
				},
				dataType : 'json'
			});
		},
		// 事件绑定
		bindEvent : function() {
			console.log(this.element);
		},
		template : dropMenu_tpl
	});
	module.exports = DropMenu;
});