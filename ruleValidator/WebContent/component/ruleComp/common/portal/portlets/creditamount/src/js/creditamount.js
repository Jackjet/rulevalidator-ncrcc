define(function(require, exports, module) {
	var $ = require('$');
	var Portlet=require("portlet");
	require('../theme/default/css/creditamount.css');
	var template = require('./creditamount.tpl');
	
	var Creditamount= Portlet.extend({
		portlet_template : template,
		initCustAttr:function(){
			this.set("moreurl",this.get("moreurl"));
		},
		loadData : function() {
			var that = this;
			$.ajax({
				type : 'GET',
				url : G.API.CREDIT,
				dataType : 'json',
				success : function(data) {
					if (data) {
						for(var i=0;i<data.length&&i<10;i++){
							if(data[i].productLines==null||data.productLines==""){
								data[i].productLines="全部产品线";
							}
						}
						if (data.length > 10) {
							that.setModel({
								records: data.slice(0, 10),
								notempty:true
							});
						} else {
							that.setModel({
								records:data,
								notempty:true
							});
						}
					}else{
						that.setModel("没有信用额度数据");
					}
				},
				error:function(response) {
					that.setModel("查询信用额度失败");
				}
			});
		},
		afterRender:function(){
			Creditamount.superclass.afterRender.apply(this, arguments);
			$(".ui-creditamoutsubul").eq(0).show();
		}
	});
	
	module.exports=Creditamount;
});