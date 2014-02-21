define( function(require, exports, module) {

	require('../theme/default/css/ecpgrid.css');
	var Grid = require("grid");
	var EcpGridFormat=require("./EcpGridFormat");
	var ecpgrid_tpl = require("./ecpgrid.tpl");
	var $ = require("$");
	var EcpGrid = Grid.extend({
		template:ecpgrid_tpl,
		initCustAttr:function(){
			EcpGrid.superclass.initCustAttr.apply(this, arguments);
			var format=$.extend(true,EcpGridFormat,this.get("format"));
			this.set("format",format);
			this.set("showRowNum",true);
		},
		afterRender:function(){
			$(".ui-ecpgrid").trigger("child_numberChange",[this.element.find("tbody tr").size(),this.prop.parentNode.prevAll().size()]);
			$(".ui-ecpgrid").trigger("grid_afterRender");
		},
		// 设置状态
		setStatus : function(status) {
			this.element.find(".J-ecpgrid-status").html(status);
		},
		loadData : function(params) {
			this.setStatus("加载数据中...");
			var grid = this;
			var autoRender = this.get("autoRender");
			var model = this.get("model");
			var data=this.get("data");
			if (model)
				return;
			if (autoRender) {
				var url = this.get("url");
				$.ajax({
					type : 'POST',
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
					data : data
				});
			}
		},
		refresh:function(params){
			var ecpgrid = this;
			var url = this.get("url");
			$.ajax({
				type : 'POST',
				url : url,
				success : function(data) {
					ecpgrid.setModel(data);
				},
				error:function(){
				},
				dataType : 'json',
				data : params
			});
		}
	});
	module.exports = EcpGrid;
});