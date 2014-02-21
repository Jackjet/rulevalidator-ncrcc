define(function(require, exports, module) {
	/*
	 * user for init index.html layout 2013-08-20 songhlc
	 */
	var Store = require("store");
	var portletConfig = require("portlet_config");
	var $ = require("$");
	require("api");
	var Construct = function() {
		var columnConstruct = ['side_column', 'doc_content', 'right_column'];
		if (!Store.get("layoutConfig"))
			Store.set("layoutConfig", portletConfig.portlet_config);
		var config = Store.get("layoutConfig");
		store.set("layoutConfigTemp", config);
		$("#construct").html("").append("<div class='side_column'></div>")
				.append("<div class='doc_content'></div>")
				.append("<div class='right_column'></div>");
		// 绘制布局结构并且加载组件
		for (var index = 0; index < 3; index++) {
			// 获取列
			var constructColumn = $("#construct ." + columnConstruct[index]);
			for (var wIndex = 0; wIndex < config[index].widgets.length; wIndex++) {
				var divHtml;
				if (config[index].widgets[wIndex].attrs["color"])
					divHtml = "<div data-widget='"
							+ config[index].widgets[wIndex].name
							+ "' class='construct "
							+ config[index].widgets[wIndex].attrs["color"]
							+ "' id='" + config[index].widgets[wIndex].id
							+ "' data-widget-auto-rendered='true'>";
				else
					divHtml = "<div data-widget='"
							+ config[index].widgets[wIndex].name
							+ "' class='construct' id='"
							+ config[index].widgets[wIndex].id
							+ "' data-widget-auto-rendered='true'>";
				if(config[index].widgets[wIndex].attrs["noborder"]){
					divHtml = $(divHtml).addClass("ui-portlet-noborder").get(0);
				}
				if (constructColumn.find(".J-blankDiv").length == 0)
					constructColumn.append(divHtml);
				else
					constructColumn.find(".J-blankDiv").before(divHtml);
				(function(index, wIndex) {
					if (config[index].widgets[wIndex].name)
						seajs.use(config[index].widgets[wIndex].name, function(
								widget) {
							if (widget) {
								new widget({
									attrs : config[index].widgets[wIndex].attrs,
									renderTo : $("#"+ config[index].widgets[wIndex].id).get(0)
								});
							}
						});
				})(index, wIndex);
			}
			// 设置宽度
			if (config[index].width == "0" || config[index].width == "0%") {
				constructColumn.css("display", "none");
			} else {
				constructColumn.css("width", config[index].width);
			}
		}
		/*//添加floatpanel组件
		if($("#floatpanel").length==0){
		$("#construct").before("<div id='floatpanel'></div>");
		seajs.use("floatpanel",
				function(widget) {
					if (widget) {
						new widget({							
							renderTo : $("#floatpanel")
						});
					}
				});}*/
	};
	
	// 组件对外提供使用
	module.exports = Construct;
});