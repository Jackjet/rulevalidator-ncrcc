define(function(require, exports, module) {
	var Store = require("store");
	var Dialog = require("dialog");
	var Dragable = require("dragable");
	require('../theme/default/css/portletedit.css');
	var $ = require("$");
	module.exports = {
		editInit:function(Element){
				//top hover 设置样式
				$(Element).find(".ui-portlet-top").on("mouseover",function(){
						$(this).addClass("ui-portlet-top-hover");
				});
				//保证在后台管理页面，不触发任何按钮事件
				$(Element).parents("body").find("a:not(.i-collapse,.J-portlet-edit,.J-portlet-delete,.color a)").removeAttr("href").die().unbind();
				$(Element).find(".ui-portlet-top").on("mouseout",function(){
						$(this).removeClass("ui-portlet-top-hover");
				});
				//tool工具栏下拉
				$(Element).find(".i-collapse").on("click",function(ev){
					var icollapse = $(this).find("~ .J-tools");
					if(!icollapse.hasClass("ui-portlet-displayblock")){
						icollapse.addClass("ui-portlet-displayblock");
						icollapse.css("left",$(this).get(0).offsetLeft-187);
					}
					else{
						icollapse.removeClass("ui-portlet-displayblock");
					}
					ev.stopPropagation();
				});
				//颜色选择fn
				var colorChange = function(element,colorClass){
					$(element).parents(".construct").removeClass().addClass("construct").addClass(colorClass);
					$(element).parents(".ui-portlet-displayblock").removeClass("ui-portlet-displayblock");
					Dragable.afterDrop($(element).parents(".construct").attr("id"),colorClass);
				};
				//蓝色选择
				$(Element).find(".colorBlue a").on("click",function(){					
					colorChange(this,"ui-portlet-colorBlue");
				});
				//粉色选择
				$(Element).find(".colorPink a").on("click",function(){					
					colorChange(this,"ui-portlet-colorPink");
				});
				//默认选择
				$(Element).find(".colorDefault a").on("click",function(){					
					colorChange(this,"");
				});
				//删除组件事件
				$(Element).find(".J-portlet-delete").on("click",function(){
					var element = this;
					//只允许在首页进行删除组件操作
					if($("#construct").length>0)
					{
						var option = {
								title : "提示",
								content : "确定删除此组件?",
								confirm : function(){
									$(element).parents(".construct").remove();
									Dragable.afterDrop();
									return true;
								},
								cancel : function() {
									
								return true;
								}
						};
						
						Dialog.confirm(option);
					}
				});
				$(Element).find(".J-portlet-edit").on("click",function(){
					//只允许在首页进行编辑组件操作
					if($("#construct").length>0)
					{
						var element = this;
						var id = $(element).parents(".construct").eq(0).prop("id");
						if(id!=""){
							var layoutConfig = Store.get("layoutConfig");
							var flag = false;
							var widget ="";
							var indexConfig ="";
							var indexwidget ="";
							//获取当前的组件的attrs
							
								for(var i in layoutConfig){
									indexConfig = i;
									for(var j in layoutConfig[i].widgets){
										indexwidget = j;
										if(layoutConfig[i].widgets[j].id==id){
											widget=layoutConfig[i].widgets[j];
											flag = true;
											break;
										}
									}
									if(flag){
										break;
									}
								}
							
							var htmlContent =$("<div class='ui-portlet-editpanel'></div>");
							htmlContent.append("<div><input type='hidden' name='"+id+"' value='"+widget.id+"'/></div>");
							for(var i in widget.attrs){
								htmlContent.append("<div class='ui-portlet-edit'><span>"+i+":</span><input type='text' name='"+i+"' value='"+widget.attrs[i]+"'/></div>");
							}
							htmlContent.append("<div class='ui-portlet-submit'><input class='J-editSave' type='button' value='保存'></input><input class='J-editCancel' type='button' value='取消'></input></div>");
							$(this).parents(".construct").find(".ui-portlet-content").html(htmlContent);
							//取消按钮
							$(".J-editCancel").on("click",function(){
								reloadconstruct();
							});
							//保存按钮
							$(".J-editSave").on("click",function(){
								var editpanel = $(this).parents(".ui-portlet-editpanel");
								for(var i in layoutConfig[indexConfig].widgets[indexwidget].attrs){
									layoutConfig[indexConfig].widgets[indexwidget].attrs[i] =editpanel.find("input[name='"+i+"']").val();
								}
								Store.set("layoutConfig",layoutConfig);
								reloadconstruct();
							});
						}
					}
				});
				// 修改布局或portlet相关后重新加载
				function reloadconstruct(){
					seajs.use('construct', function(Construct) {
						if (Construct) {
						new Construct();
						}
					});
				}
				$(Element).find(".J-borderchecked").on("change",function(ev){
					Dragable.afterDrop($(this).parents(".construct").attr("id"),null,!($(this).prop("checked")));
					var construct = $(this).parents(".construct");
					construct.hasClass("ui-portlet-noborder")?construct.removeClass("ui-portlet-noborder"):construct.addClass("ui-portlet-noborder");
					
				});
				//隐藏边框后的hover事件
				if($(".J-portlet-dragable").length>0){
					$(Element).find(".ui-portlet-content,.ui-portlet-top").hover(function(){
						if($(Element).parent().hasClass("ui-portlet-noborder")){
							$(Element).css("border-width","1px");
							$(Element).find(".ui-portlet-bottom,.ui-portlet-top").css("display","block");
							$(Element).find(".ui-portlet-content").css("margin","0 5px 5px 5px");
						}
					},function(){
						if($(Element).parent().hasClass("ui-portlet-noborder")){
							$(Element).css("border","solid 0px #B5B7BA");
							$(Element).find(".ui-portlet-bottom,.ui-portlet-top").css("display","none");
							$(Element).find(".ui-portlet-content").css("margin","0px");
						}
					});
				};
				//收起下拉框
				$("body").unbind("click").on("click",function(){
					$(".ui-portlet-displayblock").removeClass("ui-portlet-displayblock");
				});
				$("body").unbind("dragover").on("dragover",function(){
					$("#dragBackground").remove();
				});
		}
	};
});
	