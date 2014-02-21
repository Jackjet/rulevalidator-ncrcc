define(function(require, exports, module) {
	var $ = require('$');
	var Portlet=require("portlet");
	require('../theme/default/css/bulletin.css');
	var template = require('./bulletin.tpl');
	
	var Bulletin= Portlet.extend({
		portlet_template : template,
		initCustAttr:function(){
			this.set("moreurl",this.get("moreurl"));
		},
		loadData : function() {
			var that = this;
			$.ajax({
				type : 'POST',
				url : G.API.BULLETIN,
				dataType : 'json',
				data : {
					bulletinTypeId : ''
				},
				success : function(data) {
					if (data) {
						if (data.total > 10) {
							that.setModel({
								records: data.records.slice(0, 10),
								notempty:true
							});
						} else {
							that.setModel({
								records:data.records,
								notempty:true
							});
						}
					}else{
						that.setModel("没有公告数据");
					}
				},
				error:function(response) {
					that.setModel("查询公告失败");
				}
			});
		}
	});
	
	module.exports=Bulletin;
});