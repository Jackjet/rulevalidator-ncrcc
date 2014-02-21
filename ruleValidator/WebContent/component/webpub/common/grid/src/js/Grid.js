/**
 * Grid 组件
 * 
 */
define(function(require, exports, module) {
	var $ = require("$");
	require("../theme/default/css/grid.css")
	var Widget = require("widget");
	var grid_tpl = require("./grid.tpl");
	var handlebars = require("handlebars");
	var GridFormat = require("./GridFormat");

	/**
	 * 扩展handlebars
	 */
	handlebars.registerHelper("grid", function(context, options) {
		var ret = "";
		if (context.model) {
			var records = context.model.records;
			// 数据选择方式
			var ismulti = context.attrs.ismulti;
			var issingle = context.attrs.issingle;
			var showRowNum = context.attrs.showRowNum;
			var columns = context.attrs.columns;
			if (records) {
				for ( var i = 0; i < records.length; i++) {
					// 设置隔行换色
					if (i % 2 == 1) {
						ret += "<tr class='row_even'>";
					} else {
						ret += "<tr>"
					}

					if (ismulti || issingle) {
						var input;
						if (ismulti) {
							input = '<input type="checkbox" name="grid"/>';
						} else {
							input = '<input type="radio" name="grid"/>';
						}
						ret += "<td style='width:25px;'>" + input + "</td>";
					}
					if (showRowNum) {
						ret += "<td class='gridIndex'>" + (i + 1) + "</td>";
					} else {
						ret += "<td class='gridIndex hidden'>" + (i + 1) + "</td>";
					}
					for ( var j = 0; j < columns.length; j++) {
						ret += "<td style='width:" + columns[j].width + "px;text-align:" + columns[j].align + "'>";
						var data = records[i];
						var colId = columns[j].key;
						// 格式化列接口

						// 直接在format配置中写function
						var commonformat = columns[j].format;
						// GridFormat配置中配置的方法
						var gridformat = GridFormat[columns[j].format];
						// 其他子类format中配置的方法
						var subformat;
						if (context.attrs.format) {
							subformat = context.attrs.format[columns[j].format];
						}
						if (typeof (commonformat) == "function") {
							ret += commonformat(data[colId], data);
						} else if (typeof (gridformat) == "function") {
							ret += gridformat(data[colId], data);
						} else if (typeof (subformat) == "function") {
							ret += subformat(data[colId], data);
						} else if (colId != null) {
							var value = data[colId];
							if (value != null) {
								ret += data[colId];
							} else {
								ret += "";
							}
						}
						ret += "</td>";
					}
					ret += "</tr>";
				}
			}
		}
		return ret;
	});

	var Grid = Widget.extend({
		template : grid_tpl,
		initCustAttr : function() {
			var attrs = this.get("attrs");
			var ismulti = attrs.ismulti;
			switch (ismulti) {
			case true:
				attrs.ismulti = true;
				attrs.issingle = false;
				break;
			case false:
				attrs.issingle = true;
				attrs.ismulti = false;
				break;
			default:
				attrs.ismulti = false;
				attrs.issingle = false;
			}
		},

		loadData : function(params) {
			var grid = this;
			var autoRender = this.get("autoRender");
			var model = this.get("model");
			if (model)
				return;
			if (autoRender) {
				var url = this.get("url");
				$.ajax({
					type : 'GET',
					url : url,
					success : function(data) {
						grid.setModel(data);
						if (parent) {
							parent.seajs.emit("event_grid_loaded");
						} else {
							seajs.emit("event_grid_loaded");
						}
					},
					error : function() {
					},
					dataType : 'json',
					data : params
				});
			}
		},
		handlebars : handlebars,
		format : {},

		query : function(params) {
			this.set("autoRender", true);
			this.loadData(params);
		},

		// checkbox绑定事件，全选，反选
		bindEvent : function() {
			var grid = this;
			$(".J_grid_selected_all").live("click", function() {
				var checked = $(this).attr("checked");
				if (!checked) {
					checked = false;
				}
				// 为了防止多页签和grid合用的时候带来的相互干扰的问题，详情见渠道
				$(this).parents(".ecp_grid_table").find("[name=grid]").each(function() {
					$(this).attr("checked", checked);
				});
			});

			var event = this.get("event");
			if (event) {
				for ( var i = 0; i < event.length; i++) {
					$("." + event[i].key).live(event[i].trigger, event[i].handler);
				}
			}
		},

		getSelectedIndex : function() {
			$(".ecp_grid_table").find("[name=grid]").each(function() {
				$(this).attr("checked", checked);
			})

		},

		/**
		 * 获取被选中的数据
		 */
		getSelectedData : function() {
			var data = [];
			var that = this;
			$("tbody input:checked").each(function() {
				var index = $(this).parent().parent().find(".gridIndex").text();
				data.push(that.get("model").records[index - 1]);
			});
			return data;
		},

		/**
		 * 获取选择的行数据
		 */
		getSelectedRow : function() {
			var data = [];
			var that = this;
			$("tbody input:checked").each(function() {
				var row = $(this).parent().parent();
				data.push(row);
			});
			return data;
		}

	});
	module.exports = Grid;
});