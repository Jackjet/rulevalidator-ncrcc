define(function(require, exports, module) {
	
	require('../theme/default/css/search.css');
	require("button");
	var $ = require("$");
	var Widget = require("widget");
	$ = require("jquery.autocomplete")($);
	var template = require("./search.tpl");
	var placeholder=require("placeholder");
	var Search = Widget.extend({
		template:template,
		initCustAttr:function(){
			placeholder.init();
		},
		bindEvent:function(){
			var indexurl=this.get("attrs").indexurl || G.API.PRODUCT_AUTOFILL;
			var advanceQueryUrl=this.get("attrs").advanceQueryUrl ||G.PAGE.PRODUCT_PRODUCTLIST;
			var iconUrl=this.get("attrs").iconUrl||G.PAGE.PRODUCT_PRODUCTLIST ;
			//为输入框绑定索引服务
			$('#searchInput').autocomplete(indexurl, {
				max: 10,
				minChars: 1,
				width: 435,
				scrollHeight: 300,
				matchContains: true,
				autoFill: false,
				dataType:'json',
				selectFirst:false,/*是否默认选中第一行*/
				formatItem: function(row, i, max) {
					return row.name +"/"+ row.to ;
				},
				formatMatch: function(row, i, max) {
					return row.name + row.to;
				},
				formatResult: function(row) {
					return row.to;
				},
				parse: function(data) {
					return $.map(eval(data), function(row) {
						return {
							data: row,
							value: row.name+row.to,
							result: row.name
						};
					});
				}		 
			}).result(function(event, row, formatted) {
				$("#productName").attr("value",row.name);
		 	});
			//为输入框绑定回车事件
			$('#searchInput').bind('keydown', function(event) {
				if (event.keyCode == 13) {
					search();
				}
			});
			//为高级搜索绑定服务
			$('#advanceSearch').attr("href",advanceQueryUrl);
			
			//为图标按钮绑定服务
			//以前的写法是：
			//'<a href='javascript:search();' class="search_btn"></a>'
			$(".search_btn").click(search);
			function search(){
				if($('#searchInput').attr('value') != '输入商品名称 /编码'){
					//$("#productName").attr('value', $('#searchInput').attr('value'));
				}
				var inputValue = $.trim($('#searchInput').val());
				// FIXME 后台查询过滤掉了特殊字符，而前台没有提示
				var param = encodeURI(inputValue);
				var url = iconUrl +"&name=" + param;
				$('#searchBar').attr('action', url);
				$('#searchBar').submit();
			}		
		},
		afterRender:function(){
		}
	});
	module.exports = Search;
});