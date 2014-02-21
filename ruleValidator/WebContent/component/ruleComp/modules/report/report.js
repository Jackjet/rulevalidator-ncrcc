define(function(require, exports, module) {
	require("api");
	require("./theme/css/ztree.css");
	var $ = require("$");
	var tree = require("jquery.ztree");
	$=tree($);
	var setting = {
		callback:{
			onClick:function(event,treeId,treeNode){
				seajs.emit("reportCategory_click", treeNode.cid);
			}
		}
	};
//	$.ajax({
//		type : "GET",
//		url : G.API.BILLBOARD_CATEGORY,
//		success : function(msg, status, response) {
////			$.fn.zTree.init($("#treeDemo"), setting, [{"name":"销售排行分类"}]);
////			var obj=$.fn.zTree.getZTreeObj("treeDemo");
////			for(var i=0;i<msg.length;i++){
////				obj.addNodes(obj.getNodes()[0],msg[i]);
////			}
//		}
//	});
});