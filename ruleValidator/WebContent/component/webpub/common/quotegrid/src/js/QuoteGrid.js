
/**
 * Grid 组件
 * 
 */
define(function(require, exports, module) {
	require("../theme/default/css/quotegrid.css")
	var Widget = require("widget");
	var quotegrid_tpl = require("./quotegrid.tpl");
	var handlebars = require("handlebars");
	var $ = require("$");

	var QuoteGrid = Widget.extend({
				template : quotegrid_tpl,
				loadData : function() {
					var extendgrid = this;
					var url = this.get("url");
					$.ajax({
						type : 'POST',
						url : url,
						success : function(data) {
							extendgrid.setModel(data);
							extendgrid._bindEvent();

						},
						error:function(){
							console.log("c");
						},
						dataType : 'json'
					});
				},
				handlebars : handlebars,
				
				_bindEvent : function() {
					//绑定点击事件
					var that=this;
					this.element.find(".J-ProductDetail").click(function(){
						that.element.find(".big_detail_modules").slideToggle(function(){
							if(that.element.find(".switcher a").hasClass("up")){
								that.element.find(".switcher a").removeClass("up");
							}else{
								that.element.find(".switcher a").addClass("up");
							}
						});
					});
				}
			});
	module.exports = QuoteGrid;
});