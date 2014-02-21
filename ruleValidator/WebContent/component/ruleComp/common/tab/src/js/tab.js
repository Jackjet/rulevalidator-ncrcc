/*
 * Ecp Tab组件
 */

define(function(require, exports, module) {
	// 默认引入：组件继承自widget
	var Widget = require("widget");
	var $ = require("$");
	// 自定义：组件对应的css
	require('../theme/default/css/tab.css');
	// 自定义：组件的html模板
	var example_tpl = require("./tab.tpl");
	var store = require("store");
	var Log = require("log");
	// 组件的定义 组件名大写
	var Tab = Widget.extend({
		template : example_tpl,
		bindEvent : function() {
			/* 订单中心页－订单信息标签切换 */
			var trigger=this.get("trigger");
			if(!trigger){
				trigger="mouseenter";
			}
			if(trigger!="click"){
				$(".tab_labels a.label_active").live("click",function(){
					return false;
				});
			};
			$(".tab_labels a.label_normal").live(trigger,
					orderlLabelSwitch);
			function orderlLabelSwitch() {
				// 找到活动页面的z-index
				var active = $(this).parent().parent().find("a.label_active")
						.css("zIndex");
				// 设置z-index
				// 去除活动class
				// 添加普通class
				var a_active = $(this).parent().parent().find("a.label_active");
				a_active.css({
							zIndex : $(this).css("zIndex")
						}).removeClass("label_active").addClass("label_normal");
				a_active.addClass($($(this).attr("class"))[1]).bind(
						trigger, orderlLabelSwitch);
				//给当前tab设置index，设置活动class，取消绑定事件
				$(this).css({
							zIndex : active
						}).removeAttr("class").addClass("label_active").unbind(
						trigger, orderlLabelSwitch);
				//隐藏所有内容
				$(".labels_content").children().not(".hidden")
						.addClass("hidden");
				//根据a的href选择显示内容
				var tabContent = $(this.href.substring(this.href.indexOf("#"), this.href.length));
				tabContent.removeClass("hidden");
				tabContent.trigger(G.EVENT.TAB_ACTIVE);
				//显示左移右移隐藏按钮
				if($("body.page_config").length>0){
					$(".page_config .pannel_edit").hide();
					$(this).next().show("normal");
				}
				//return false;
			}
			$("body").delegate("*", "child_numberChange",function(event,num,index){
				$(".number").eq(index).text(num);
			});
			//tab左移
			$(".page_config .pannel_edit .pannel_left").live("click",function(){
				var that = $(this).parents(".label_wapper"); 
				if(that.prev().length>0){
					that.insertBefore(that.prev());
					saveToStore();
				}
			});
			//tab右移
			$(".page_config .pannel_edit .pannel_right").live("click",function(){
				var that = $(this).parents(".label_wapper");
				if(that.next(":not(.disabled)").length>0){
					that.insertAfter(that.next());
					saveToStore();
				}
			});
			//tab隐藏/显示
			$(".page_config .pannel_edit .pannel_center").live("click",function(){
				var that = $(this).parents(".label_wapper");
				if(!that.hasClass("disabled")){
					that.addClass("disabled").css("float","right").appendTo(that.parent(".tab_labels")).find(".pannel_center").html("显示");
				}
				else{
					that.css("float","left").removeClass("disabled").find(".pannel_center").html("隐藏");
				}
				saveToStore();
			});
			//隐藏编辑
			$(".page_config .tab_labels").live("mouseleave",function(){
				$(".page_config .pannel_edit").hide("slow");
			});
			//将tab顺序保存到store
			function saveToStore(){
				var data = [];
				$(".tab_labels .label_wapper").each(function(i,ele){
					var id = $(ele).find("a").prop("href").split('#')[1];
					var title = $(ele).find(".label_text").html();
					var temp={id:id,title:title};
					if(i==0){
						temp["active"]= true;
					}
					if($(this).hasClass("disabled")){
						temp["disabled"]= true;
					}
					data.push(temp);
				});
				store.set("producttab",data);
			}
		},
		setActive : function(index) {
			var trigger=this.get("trigger");
			if(!trigger){
				trigger="mouseenter";
			}
			if(index==undefined && isNaN(index)){
				index = 0;
			};
			if($(".tab_labels a").eq(index).length==0){
				Log.error("超出标签数量");
				index =0;
			}
			$(".tab_labels a").eq(index).trigger(trigger);
		},
		getActive:function(){
			return this.element.find(".label_active").parent().prevAll().size();
		}
	});
	// 组件对外提供使用
	module.exports = Tab;
});