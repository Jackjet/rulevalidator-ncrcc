define(function(require, exports, module) {
	var $ = require('$');
	require('../theme/default/css/address.css');
	var template = require('./address_update.tpl');
	var Widget = require('widget');
	require('jquery.ui.datepicker')($);
	var Dropdowntree = require("dropdowntree");
	$ = require('jquery.ztree')($);
	var ROOT_ID = 'root';
	var addressupdate = Widget.extend({
		template:template,
		initCustAttr:function(){
			if(window.location.search!=""){
				var para= window.location.search.split("=")[1];
				this.set("para",para);
			}
		},
		loadData:function(){		
			var that = this;
			//null则表示增加
			if(this.get("para")){
				$.ajax({
					type : "POST",
					async : false,
					url : G.API.USER_QUERY,
					data : {
						pkUser : this.get("para")
					},
					dateType : "json",
					success : function(data) {
						if(that.get("para")!=null){
							var dataModel ={};
							var userView = data.records[0];
							if(userView!=null){
								dataModel["pkUser"] =  userView.pkUser;
								dataModel["userCode"] =  userView.userCode;
								dataModel["userName"] = userView.userName||"";
								dataModel["sex"] = userView.sex==2?true:false;
								dataModel["email"] = userView.email||"";
								dataModel["memo"] = userView.memo||"";
								dataModel["cellPhone"] = userView.cellPhone||"";
								dataModel["memo"] = userView.memo||"";
							}
							that.setModel(dataModel);
						}
					}
				});
			}
		},
		bindEvent:function(){
			var that = this;
			// country => provice
//			$('#countrys').live('change', function() {
//				var html = "<option>请选择</option>";
//				$("#vsection").html(html);
//				$("#city").html(html);
//				that.getAndSetRegion(null,1,$(this).find('option:selected').val(),"province");
//			});
			// provice => city
		},
		afterRender:function(){
			var that = this;
			 
		} 
	});
	module.exports = addressupdate;
});