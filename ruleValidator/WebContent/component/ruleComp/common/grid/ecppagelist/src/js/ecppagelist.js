define(function(require, exports, module) {
	var EcpPageGrid = require("ecppagegrid");
	
	require('../theme/default/css/ecppagelist.css');
	var ecppagelist_tpl = require("./ecppagelist.tpl");
	var handlebars = require("handlebars");
	/**
	 * 扩展handlebars
	 */
	handlebars.registerHelper("list", function(context, options) {
		var ret = "";
		if (context.model) {
			var records = context.model.records;
			var columns = context.attrs.columns;
			if (records) {
				for (var i = 0; i < records.length; i++) {
					ret += "<li>"
					for (var j = 0; j < columns.length; j++) {
						ret += "<span style='width:" + columns[j].width
								+ "px;text-align:" + columns[j].align + "'>";
						var data = records[i];
						var colId = columns[j].key;
						// 格式化列接口

						// 直接在format配置中写function
						var commonformat = columns[j].format;
						// 其他子类format中配置的方法
						var subformat;
						if (context.attrs.format) {
							subformat = context.attrs.format[columns[j].format];
						}
						if (typeof(commonformat) == "function") {
							ret += commonformat(data[colId], data);
						} else if (typeof(gridformat) == "function") {
							ret += gridformat(data[colId], data);
						} else if (typeof(subformat) == "function") {
							ret += subformat(data[colId], data);
						} else if (colId != null) {
							var value = data[colId];
							if (value != null) {
								ret += data[colId];
							} else {
								ret += "";
							}
						}
						ret += "</span>";
					}
					ret += "</tr>";
				}
			}
		}
		return ret;
	});
	var EcpPageGrid = EcpPageGrid.extend({
				template : ecppagelist_tpl,
				handlebars:handlebars
			});
	module.exports = EcpPageGrid;
});