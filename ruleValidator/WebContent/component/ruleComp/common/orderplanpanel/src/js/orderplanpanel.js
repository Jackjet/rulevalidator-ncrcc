define(function(require, exports, module) {
	require('../theme/default/css/orderplanpanel.css');
	var $ = require("$");
	var Widget = require("widget");
	var template = require("./orderplanpanel.tpl");
	var OrderplanPanel = Widget.extend({
		template : template,
		loadData:function(){
			var that=this;
			$.ajax({
				url:that.get("url"),
				dataType:"json",
				success:function(data){
					that.setModel(data);
					seajs.emit("orderplanpanel_success");
				},
				data:that.get("data")
			});
		},
		bindEvent:function(){
			var that=this;
			//绑定注册按钮事件
			var buttons=this.get("buttons");
			if(buttons){
				for(var i=0;i<buttons.length;i++){
					$(".J-"+buttons[i].key).live("click",buttons[i].handler);
				}
			}
			$(".J-collapse").live("click",function(){
				if($(this).hasClass("open")){
					$(".ui-orderplanpanel").addClass("height_40");
					$(this).removeClass("open").addClass("close");
				}else{
					$(".ui-orderplanpanel").removeClass("height_40");
					$(this).removeClass("close").addClass("open");
				}
				var extendHandle=that.get("extendHandle");
				if(extendHandle){
					extendHandle();
				}
				return false;
			});
			$(".J-orderplanpanel-edit").live("change",function(){
				$(this).attr("changed","true");
			});
		}
	});
	module.exports = OrderplanPanel;
});