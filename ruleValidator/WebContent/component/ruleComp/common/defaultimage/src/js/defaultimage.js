/**
 * 默认商品图片处理
 */
define(function(require, exports, module) {

	var $ = require("$");
	var DefaultProductImage = {
		init:function(){
			var setDefault = function(){
				$(this).attr("src",G.ecpimages+"/defaultimage/src/theme/default/img/defaultimg.jpg");
			};
			
			$("img[src='']").each(setDefault);
			$("img[src='/null']").each(setDefault);
			$("img[src='/']").each(setDefault);
		},
		setDefaultImage:function(object){
			object.attr("src",G.ecpimages+"/defaultimage/src/theme/default/img/defaultimg.jpg");
		}
		
	};
	
	module.exports = DefaultProductImage;

});