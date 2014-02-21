/*
 * 图片商品服务
 */
define(function(require, exports, module) {
	var $ = require("$");
	$ = require("jquery.extend")($);
	var Log = require("log");
	var context = require("context");
	var store = require("store");
	var defaultimage=require("defaultimage");

	var ProductPicture = function() {
		var queryPicture = function() {
			var productidArray = new Array();
			var spanArray = $("img[ectype='picture']");
			if (spanArray.length == 0)
				return;
			spanArray.each(function() {
				var productid = $(this).attr("productid");
				productidArray.push(productid);
			});
				$.ajax({
					type : "GET",
					url : G.API.PICTURE_QUERY,
					dateType : "json",
					data : {
						ids : productidArray.join(",")
					},
					success : function(msg, status, response) {
						if (msg) {
							initPicture(spanArray, msg);
						} 
					}
				});
		};
		var initPicture = function(spanArray, msg) {
			spanArray.each(function() {
				var productid = $(this).attr("productid");
				var size=$(this).attr("size");
				//默认取第0组
				if(msg[productid]){
					$(this).attr("src","/"+msg[productid][0][size]);
				}
			});
		};
		// Dom加载完毕后初始化
		this.init = function() {
			queryPicture();
		};
	};

	// Dom加载完毕后初始化
	var productPicture = new ProductPicture();
	module.exports = productPicture;
});