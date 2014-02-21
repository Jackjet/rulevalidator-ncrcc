define(function(require,exports,module){
	var $=require("$");
	module.exports={
		editInit:function(){
			if($(".J-edit").size()==0){
				//1.加框
				var designDiv=$("<div></div>").css({
					"text-align": "center",
					"margin-left": "30px",
					"cursor": "pointer",
					"float":"left",
					"background-color":"#D3E5F1",
					"padding":"5px"
				}).text("编辑").addClass("J-edit");
				this.element.parent().after(designDiv);
				//2.绑定编辑事件
				var that=this;
				$(".J-edit").live("click",function(){
					that.reset();
					that.doEdit();
					$(this).text("保存").addClass("J-save").removeClass("J-edit");
				});
				//3.绑定保存事件
				$(".J-save").live("click",function(){
					var id=that.id;
					var configs=that.saveEdit();
					var componentConfigs=store.get("componentConfigs") || {};
					componentConfigs[id]=configs;
					store.set("componentConfigs",componentConfigs);
					$(".J-save").text("编辑").addClass("J-edit").removeClass("J-save");
				});
			}
		},	
		doEdit:function(){

		},
		saveEdit:function(){

		},
		reset:function(){
			
		}
	}
});