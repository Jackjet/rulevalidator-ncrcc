define(function(require, exports, module) {
	var $ = require('$');
	require('../theme/default/css/resource.css');
	var template = require('./menu_update.tpl');
	var Widget = require('widget');
	require('jquery.ui.datepicker')($);
	var Dropdowntree = require("dropdowntree");
	$ = require('jquery.ztree')($);
	var ROOT_ID = 'root';
	var menuupdate = Widget.extend({
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
					url : G.API.MEANUS_QUERY,
					data : {
						pkMenu : this.get("para")
					},
					dateType : "json",
					success : function(data) {
						if(that.get("para")!=null){
							var dataModel ={};
							var userView = data.records[0];
							if(userView!=null){
								dataModel["menuId"] =  userView.menuId;
								dataModel["menuTitle"] =  userView.menuTitle;
								dataModel["parentId"] =  userView.parentId;
								dataModel["resourceId"] = userView.resourceId||"";
								dataModel["seq"] = userView.seq||"";
								dataModel["hide"] = userView.hide=='Y'?true:false;
								dataModel["leaf"] = userView.leaf=='Y'?true:false;
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
//			$('#resourceId').live('change', function() {
//				var html = "<option>请选择</option>";
//				$("#vsection").html(html);
//				$("#city").html(html);
//				that.getAndSetRegion(null,1,$(this).find('option:selected').val(),"province");
//			});
			// provice => city
		},
		afterRender:function(){
			var that = this;
			
			$.ajax({
				type : "POST",
				url : G.API.MEANUS_QUERY,
				dateType : "json",
				data : {
					isRoot:true
				},
				success : function(data) {
					if (data != null && data.records!=null) {
						var html = "<option value='root'>根节点</option>";
						for ( var i = 0; i < data.records.length; i++) {
							html += "<option value=\""+data.records[i].menuId+ "\">" + data.records[i].menuTitle + "</option>";
						}
						$("#parentId").html(html);
					} else {
						alert("查询失败！！");
					}
				}
			});
			
			$.ajax({
				type : "POST",
				url : G.API.RESOURCE_QUERY,
				dateType : "json",
				data : {
				},
				success : function(data) {
					if (data != null && data.records!=null) {
						var html = "";
						for ( var i = 0; i < data.records.length; i++) {
							html += "<option value=\""+data.records[i].resourceId+ "\">" + data.records[i].resourceName + "</option>";
						}
						$("#resourceId").html(html);
					} else {
						alert("查询失败！！");
					}
				}
			});
			//初始化树形下拉框
//			var container = $('#areaclass');
//			var rootNode = {
//					id : "null",
//					pId : '~',
//					name : '根节点',
//					open : true,
//					isParent : true
//				};
//			var simpleData={
//				enable: true,
//				idKey: "pk_areacl",
//				pIdKey: "pk_fatherarea",
//				nameKey:"name"
//			};
//			var option={
//				container:container,
//				url:G.API.ACCOUNT_AREACLASS_QUERY,
//				rootNode:rootNode,
//				simpleData:simpleData,
//				inputname:"areaClassView.pk_areacl",
//				width:319
//			};
//			var id="";
//			var value="";
//			if(that.get("model")){
//				id=this.get("model").pk_areacl||"";
//				value=this.get("model").areaclassname!=null?this.get("model").areaclassname:"";
//			}
//			Dropdowntree.init(option);
//			//设置默认值
//			Dropdowntree.selectValue(id,value);
		}
	});
	module.exports = menuupdate;
});