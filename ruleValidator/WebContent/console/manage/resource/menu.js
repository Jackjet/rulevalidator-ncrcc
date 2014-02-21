define(function(require, exports, module) {
	var $ = require("$");
	$ = require("jquery.ztree")($);
	require("iframeresize");
	var setting = {};
	var initPageTree = function(){
		$.ajax({
			type : "GET",
			url : "",
			success : function(msg, status, response) {
				for(var i=0;i<msg.length;i++){
					msg[i].url=G.ctx+msg[i].url;
					var children = msg[i].children;
					if(children.length>0){
						for(var j=0;j<children.length;j++){
							children[j].url=G.ctx+children[j].url;
						}
					}
				}
				$.fn.zTree.init($("#treeDemo"), setting, msg);
			}
		});
	};
	initPageTree();
	var initCompTree = function(){
		var msg=[
					{"name":"表单","children":[
					 	{"name":"form","target":"main","url":"/ecp/component/ecp/common/form/example/example.html?seajs-reload=8000"}
					]},
					{"name":"Tab","children":[
					  	{"name":"tab","target":"main","url":"/ecp/component/ecp/common/tab/example/example.html?seajs-reload=8000"}
					]},
					{"name":"panel","children":[
					    {"name":"category","target":"main","url":"/ecp/component/ecp/common/category/example/example.html?seajs-reload=8000"},
					    {"name":"orderbox","target":"main","url":"/ecp/component/ecp/common/portal/portlets/orderbox/example/example.html?seajs-reload=8000"},
					    {"name":"bulletin","target":"main","url":"/ecp/component/ecp/common/portal/portlets/bulletin/example/example.html?seajs-reload=8000"},
					    {"name":"recentview","target":"main","url":"/ecp/component/ecp/common/portal/portlets/recentview/example/example.html?seajs-reload=8000"},
					    {"name":"top10","target":"main","url":"/ecp/component/ecp/common/portal/portlets/billboard/example/example.html?seajs-reload=8000"},
					    {"name":"sidemenu","target":"main","url":"/ecp/component/ecp/common/sidemenu/example/example.html?seajs-reload=8000"}
					]},
					{"name":"button","children":[
					    {"name":"button","target":"main","url":"/ecp/component/ecp/common/button/example/example.html?seajs-reload=8000"},
					    {"name":"buttongroup","target":"main","url":"/ecp/component/ecp/common/buttongroup/example/example.html?seajs-reload=8000"}
					]},
					{"name":"grid","children":[
						{"name":"ecpgrid","target":"main","url":"/ecp/component/ecp/common/ecpgrid/example/example.html?seajs-reload=8000"},
						{"name":"ecppagegrid","target":"main","url":"/ecp/component/ecp/common/ecppagegrid/example/example.html?seajs-reload=8000"},
						{"name":"ecpproductgrid","target":"main","url":"/ecp/component/ecp/common/ecpproductgrid/example/example.html?seajs-reload=8000"}
					]},
					{"name":"list","children":[
						{"name":"ecppagelist","target":"main","url":"/ecp/component/ecp/common/ecppagelist/example/example.html?seajs-reload=8000"},
					]},
					{"name":"导航","children":[
						{"name":"navigation","target":"main","url":"/ecp/component/ecp/common/navigation/example/example.html?seajs-reload=8000"},
						{"name":"subnav","target":"main","url":"/ecp/component/ecp/common/subnav/example/example.html?seajs-reload=8000"}
					]},
					{"name":"自动填充","children":[
					   	{"name":"search","target":"main","url":"/ecp/component/ecp/common/search/example/example.html?seajs-reload=8000"},
					   	{"name":"productautocomplete","target":"main","url":"/ecp/component/ecp/common/productautocomplete/example/example.html?seajs-reload=8000"},
					   	{"name":"productautocompleteadd","target":"main","url":"/ecp/component/ecp/common/productautocompleteadd/example/example.html?seajs-reload=8000"}
					]},
					{"name":"其他","children":[
					    {"name":"top","target":"main","url":"/ecp/component/ecp/common/top/example/example.html?seajs-reload=8000"},
					    {"name":"footer","target":"main","url":"/ecp/component/ecp/common/footer/example/example.html?seajs-reload=8000"},
					    {"name":"signon","target":"main","url":"/ecp/component/ecp/common/signon/example/example.html?seajs-reload=8000"},
					    {"name":"signonform","target":"main","url":"/ecp/component/ecp/common/signonform/example/example.html?seajs-reload=8000"},
					    {"name":"shopcart","target":"main","url":"/ecp/component/ecp/common/shopcart/example/example.html?seajs-reload=8000"},
					    {"name":"商品描述","target":"main","url":"/ecp/component/ecp/common/productdesc/example/example.html?seajs-reload=8000"},
					    {"name":"banner","target":"main","url":"/ecp/component/ecp/common/banner/example/example.html?seajs-reload=8000"}
					]}
				];
		$.fn.zTree.init($("#treeDemo"), setting, msg);
		
	};
	$("#pageview").bind("click",function(){
		initPageTree();
		$("#compview").removeClass("active");
		$("#pageview").addClass("active");
		return false;
	});
	$("#compview").bind("click",function(){
		initCompTree();
		$("#pageview").removeClass("active");
		$("#compview").addClass("active");
		return false;
	});
});