define(function(require, exports, module) {
	var Widget = require("widget");
	require('../theme/default/css/displaypanel.css');
	var template = require("./displaypanel.tpl");
	var Handlebars = require("handlebars");
	var $=require("$");
	
	Handlebars.registerHelper("displaypanel", function(context, options){
		var ret="";
		var model=context.model || {};
		var items=context.attrs.items;
		for(var i=0;i<items.length;i++){
			var field=items[i].field;
			ret+=items[i].isShow==false ? "<div class='item hidden'>" : "<div class='item'>";
			ret+="<label>"+items[i].label+"：</label>";
			if(items[i].format){
				ret+=items[i].format(model[field],model);
			}else{
				ret+="<span class='J-"+field+"'>"+(model[field] || "")+"</span>";
			}
			ret+="</div>";
		}
		ret+="<div style='clear: both;'></div>";
		return ret;
	});
	
	var displaypanel = Widget.extend({
		loadData:function(){
			var that=this;
			var url=this.get("url");
			$.ajax({
				url:url,
				data:that.get("data"),
				success:function(data){
					that.setModel(data);
					seajs.emit("DISPLAYPANELLOADED");
				},
				dataType:"json"
			});
		},
		template : template
	});
	module.exports = displaypanel;
});