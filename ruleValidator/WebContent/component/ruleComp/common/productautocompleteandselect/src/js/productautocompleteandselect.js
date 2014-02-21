define(function(require, exports, module){
	var $ = require("$");
	var dialog = require("dialog");
	$ = require("jquery.autocomplete")($);
	require("../theme/default/css/productautocompleteandselect.css");
	/*option:
	 * className:   input Class Name
	 * url:       url to search product
	 * callback:  callbackfunction callback(data);
	 * loadBtnFunction: loadBtnFunction callback function
	 * */
	var initAutoComplete =function(option) {
		var className = "J_autoComplete";
		var url = option.url||G.API.PRODUCT_BASIC;
		var callback = function() {
		};
		if (option.className) {
			className = option.className;
		}
		if (option.callback) {
			callback = option.callback;
		}
		//init dom
		$("." + className).attr("placeholder","添加商品").addClass("autoInput").after('<a class="enter"></a><a class="loadBtn">...</a><div class="clear"></div>').before('<a class="addPic"></a>');
		//bindautocomplete
		if(option.loadBtnFunction){
			$(".loadBtn").live("click",option.loadBtnFunction);
		}
		$(".autoInput").live("focus",function(){
			$(this).parent().find(".addPic").removeClass("addPic");
			$(this).parent().find(".enter").css("width","8px");
			$(this).css("text-indent", "0px").attr("placeholder","输入名称/编码");
		});
		//鼠标离开
		$(".autoInput").live("blur",function(){
			//如果选中任意一条数据
			if($(this).val()==""){
				if(!$(this).prev().hasClass("addPic")){
					$(this).prev().addClass("addPic");
				}
				$(this).parent().find(".enter").css("width","0px");
				$(this).css("text-indent", "15px").attr("placeholder","添加商品");
			}else{
				$(this).parent().find(".enter").css("width","0px");
			}
		});
		$("." + className).autocomplete(G.API.PRODUCT_AUTOFILL, {
			max : 10, // 列表里的条目数
			minChars : 0, // 自动完成激活之前填入的最小字符
			width : 197, // 提示的宽度，溢出隐藏
			scrollHeight : 300, // 提示的高度，溢出显示滚动条
			matchContains : true, // 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
			autoFill : false, // 自动填充

			formatItem : function(row, i, max) {
				return row.name + "/" + row.to;
			},
			formatMatch : function(row, i, max) {
				return row.name + row.to;
			},
			formatResult : function(row) {
				return row.to;
			},
			parse : function(data) {
				return $.map(eval(data), function(row) {
					return {
						data : row,
						value : row.name,
						result : row.name
					};
				});
			}
		}).result(function(event, row, formatted) {
			//回调，由使用者注入
			callback(row.pkid,row.name);
		});
	};
	exports.init = initAutoComplete;
});