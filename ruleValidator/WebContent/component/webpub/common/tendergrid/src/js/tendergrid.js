define(function(require, exports, module) {
	require("../theme/default/css/tendergrid.css");
	var Widget = require("widget");
	var tendergrid_tpl = require("./tendergrid.tpl");
	$=require("tools/OperationFlow/OperationFlow")($);
	var handlebars = require("handlebars");
	
	handlebars.registerHelper("grid", function(context, options) {
		var ret = "";
		if (context.model) {
			var records = context.model.records;
			var columns = context.attrs.columns;
			for (var i = 0; i < records.length; i++) {
				ret += "<tr>";
				for (var j = 0; j < columns.length; j++) {
					ret += "<td>";
					var data = records[i];
					var colId = columns[j].key;
					//格式化列接口
					var format = columns[j].format;
					if (typeof(format) == "function") {
						ret += format(data[colId], data);
					} else if (colId != null) {
						ret += data[colId];
					}
					ret += "</td>";  
				}
				ret += "</tr><tr class='inner_flow'><td colspan='6'><div class='flow_wrapper'></div></td> </tr>";
			}
		}
		return ret;
	});

	var TenderGrid = Widget.extend({
		template : tendergrid_tpl,
		loadData : function() {
			var tendergrid = this;
			var url = this.get("url");
			$.ajax({
				type : 'POST',
				url : url,
				success : function(data) {
					tendergrid.setModel(data);
					tendergrid._bindEvent();
				},
				dataType : 'json'
			});
		},
		handlebars : handlebars,
		_bindEvent : function() {
			$(".flow_wrapper").UIOperationFlow(this.get("progress"));
			//绑定点击事件
			var that=this;
			this.element.find(".tenderTable tbody td").click(function(){
				var clickTR=$(this).parent().next();
				if(clickTR.hasClass("inner_flow")){
					clickTR.removeClass("inner_flow");
				}else{
					clickTR.addClass("inner_flow");
				}
			});
		}
	});
	module.exports = TenderGrid;
});