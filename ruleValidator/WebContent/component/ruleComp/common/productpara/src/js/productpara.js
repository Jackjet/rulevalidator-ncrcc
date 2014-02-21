define(function(require, exports, module) {
	// 默认引入：组件继承自widget
	var Widget = require("widget");
	// 自定义：组件对应的css
	require('../theme/default/css/productpara.css');
	// 自定义：组件的html模板
	var template = require("./productpara.tpl");
	var handlebars = require("handlebars"); 
	var $ = require("$");
	
	handlebars.registerHelper("productpara", function(context, options) {
		var title=context.attrs.title;
		var body=context.attrs.body;
		var key=context.attrs.key;
		var value=context.attrs.value;
		var ret = "";
		var model=context.model;
		if (model) {
			for (var i = 0; i < model.length; i++) {
				ret+="<div class='ui-productpara-title'><span>"+(model[i][title] || "")+"</span></div>";
				ret+="<div class='ui-productpara-content'>";
				var bodys=model[i][body];
				if(bodys){
					for(var j=0;j<bodys.length;j++){
						ret+="<div class='ui-productpara-content-item'>";
						ret+="<span class='ui-productpara-content-param'>"+(bodys[j][key] || "")+"</span>";
						if(bodys[j][value]){
							ret+="<span class='ui-productpara-content-value'>"+bodys[j][value]+"</span>";
						}else if(bodys[j].vdefaultvalue){
							ret+="<span class='ui-productpara-content-value'>"+bodys[j].vdefaultvalue+"</span>";
						}else{
							ret+="<span class='ui-productpara-content-value'></span>";
						}
						
						ret+="</div>";
					}
				}
				ret+="</div>";
			} 
		}
		return ret;
	});
	var ProductPara=Widget.extend({
		template:template
	});
	
	module.exports = ProductPara;
});