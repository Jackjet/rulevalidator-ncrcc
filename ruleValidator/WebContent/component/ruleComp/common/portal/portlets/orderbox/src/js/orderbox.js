define(function(require, exports, module) {
	require('../theme/default/css/orderbox.css');
	var Portlet=require("portlet");
	var $ = require("$");
	var template=require('./orderbox.tpl');
	// 日志
	var logger = require("log");
	var store=require("store");
	// 组件的定义 组件名大写
	var OrderBox = Portlet.extend({
		portlet_template : template,
		initCustAttr:function(){
			this.set("moreurl",this.get("moreurl"));
		},
		loadData:function(){
			var that=this;
			$.ajax({
				type : 'GET',
				url : G.API.ORDER_NUMBER,
				dataType : 'json',
				success : function(data) {
					if(data){
						for(var i=0;i<data.length;i++){
							data[i].url=eval(data[i].url);
						}
					}
					that.setModel(data);
				},
				error : function() {
					logger.error("获取待办消息失败！");
				}
			});
		},
		afterRender : function() {
			OrderBox.superclass.afterRender.apply(this, arguments);
		},
		bindEvent : function() {
			var that=this;
			$("#orderbox_createOrder").live("click", function createOrder() {
				var url = G.API.ORDER_ADD;
				window.open(url);
				return false;
			});
		}
	});
	// 组件对外提供使用
	module.exports = OrderBox;
});