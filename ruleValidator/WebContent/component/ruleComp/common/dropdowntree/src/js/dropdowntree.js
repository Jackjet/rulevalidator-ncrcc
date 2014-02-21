define(function(require, exports, module) {
	var $ = require('$');
	$ = require('jquery.ztree')($);
	require("../theme/default/css/dropdowntree.css");
	var handlebars = require('handlebars');
	var tree=null;
	var dropdowntree = {
			/*
			 * container: div where to put ztree
			 * url : ajax url to get treenode data
			 * rootNode:format like:{id : "null",pId : '~',name : 'root',open : true,isParent : true},if returndata contain root node,set it null
			 * simpleData: match data format like:{enable: true,idKey: "returnDataIdField",pIdKey: "returnDataparentIdField"}
			 * inputname:use for form submit
			 * */
			init:function(option){
				var container = option.container;
				var url = option.url;
				var rootNode = option.rootNode;
				var simpleData = option.simpleData;
				var inputname = option.inputname;
				var selectwidth = option.width||"200";
				var template = require('./dropdowntree.tpl');
				var contentHtml = handlebars.compile(template);
				container.html(contentHtml);
				var treeNodes = [rootNode];
				function treeNodeClick(event, treeId, treeNode){
					$("#selectedinput").html(treeNode[simpleData.nameKey]);
					$("#selectedValue").val(treeNode[simpleData.idKey]);
					
				}
				var setting = {
						async : {
							enable : true,
							url : url,
							dataType : 'json'
						},
						data:{
							simpleData:simpleData
						},
						callback : {
							onClick : treeNodeClick
						}
				};
				tree = $.fn.zTree.init($("#ztree_container"), setting, treeNodes);
				if(treeNodes.length>0&&!$.browser.msie){
					// 展开根节点
					var root = tree.getNodeByParam('id', treeNodes[0].id);
					tree.expandNode(root);
				}
				//设置form使用的name
				$("#selectedValue").prop("name",inputname);
				//添加清空按钮
				$("#ztree_container").append('<a class="clearbtn">清空</a>');
				//清空按钮响应事件
				$(".clearbtn").live("click",function(){
					$("#selectedinput").html("");
					$("#selectedValue").val("");
				});
				//下拉框出现事件
				$("#selectedinput,.selectbtn").on("click",function(){
					$("#ztree_container").show();
				});
				//收起下拉框
				$("#ztree_container").on("mouseleave",function(){
					$(this).hide("fast");
				});
				//设置样式
				$("#ztree_container").css("width",(selectwidth-10)+"px");
				$("#selectedinput").css("width",(selectwidth-19)+"px");
			},
			selectValue:function(id,value){
				$("#selectedinput").html(value);
				$("#selectedValue").val(id);
			}
	};
	module.exports = dropdowntree;
});