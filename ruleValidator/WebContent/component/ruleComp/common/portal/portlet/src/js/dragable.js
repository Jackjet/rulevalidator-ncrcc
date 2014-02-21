define(function(require, exports, module) {
	var Store = require("store");
	require('../theme/default/css/dragable.css');
	var $ = require("$");
	var dragBackgroundHtml = "<div id='dragBackground' class='construct'><div class='J-drag ui-dragable-dragbackground'><a>拖拽到此处</a></div></div>";
	module.exports = {
			afterDrop:function(changeId,color,noborder){
				if(Store.get("layoutConfig"))
				{
					var construct = $("#construct");
					if(construct.length>0)
					{
						//通过id 将旧的 config转成 新的config，而此时id在页面重新加载后才会按新的改变（而config已经改变），所以保留旧的config
						var layoutConfigTemp;
						var layoutConfig = [];
						layoutConfigTemp = Store.get("layoutConfigTemp");
						//如果是更新颜色则取最新的config
						if(changeId){
							var flag = false;
							for(var i in layoutConfigTemp){
								for(var j in layoutConfigTemp[i].widgets){
									if(layoutConfigTemp[i].widgets[j].id==changeId){
										if(color){
											layoutConfigTemp[i].widgets[j].attrs["color"] = color;
										}
										if(noborder!=null){
											layoutConfigTemp[i].widgets[j].attrs["noborder"] = noborder;
										}
										flag = true;
										break;
									}
								}
								if(flag){
									break;
								}
							}
							Store.set("layoutConfigTemp",layoutConfigTemp);
						}
						construct.children().each(function(index,element){
							var dataSet = {};
							dataSet["width"] = layoutConfigTemp[index].width;
							dataSet["widgets"] = [];
							$(element).children().each(function(j,subelement){
								var subWigets = $(subelement).prop("id");
								var flag = false;
								if(subWigets!=""){
									for(var i in layoutConfigTemp){
										for(var j in layoutConfigTemp[i].widgets){
											if(layoutConfigTemp[i].widgets[j].id==subWigets){
												dataSet["widgets"].push(layoutConfigTemp[i].widgets[j]);
												flag = true;
												break;
											}
										}
										if(flag){
											break;
										}
									}
								}
							});
							layoutConfig.push(dataSet);
						});
						Store.set("layoutConfig",layoutConfig);
					}
				}
			},
			dragInit : function(Element){
				var that = this;
				//给所有Element绑定拖拽事件
				var dragElement =$(Element);
				//添加拖拽事件J-drag表明可拖拽的唯一标识
				dragElement.each(function(){
					if(!dragElement.hasClass(".J-drag")){
						$(this).addClass("J-drag").attr("draggable",true);
					}
				});
				var dragDropEnd = function(ev){
					$("#dragBackground").remove();
					window.dragObject = null;
					//阻止冒泡事件
					ev.stopPropagation();
				};
				//start
				dragElement.on("dragstart", function(ev){
					window.dragObject = this;						
					return true; 
				});
				//enter
				dragElement.on("dragenter", function(event){
					console.log("ondragenter");
				});
				//over
				dragElement.on("dragover", function(ev){
					var draggable = $(ev.target).parents().hasClass("J-drag")|| $(ev.target).hasClass("J-drag");						
					if(draggable) 
					{
						$("#dragBackground").remove();
						$(ev.target).parents(".construct").before(dragBackgroundHtml);
						$("#dragBackground").on("dragover",function(ev){
							return false;
						});
						$("#dragBackground").on("drop",function(ev){
							$(window.dragObject).parent().insertBefore($(ev.target).parents(".construct").eq(0));
							dragDropEnd(ev);
							return false; 
						});
						ev.stopPropagation();
						return false;
					} 
					else 
					{
						return true; 
					}
				});
				//end
				dragElement.on("dragend",function(){
					console.log("ondragend");
					//拖拽后重新布局
					if(seajs.emit){
						parent.seajs.emit('event_widget_rendered');
						top.seajs.emit('event_widget_rendered');
					}
					that.afterDrop();
				});	
				//end
				dragElement.on("drop",function(ev){
					
					//自己不允许拖动到自己
					if($(window.dragObject)[0]==$(ev.target).parents(".J-drag")[0])
					{			
						//组织冒泡事件
						ev.stopPropagation();
						return true;
					}
					$(window.dragObject).parent().insertBefore($(ev.target).parents(".construct").eq(0));
					dragDropEnd(ev);
					return false; 
				});
				//判断.side_column,.doc_content,.right_column是否已经绑定过接收drop事件
				if(this.isUseDrag&&$(".J-dropable").length==0)
				{					
					window.isDropEnter = false;		
					//添加一个空白，防止一侧所有div都被移走后用于占位
					$(".side_column,.doc_content,.right_column").addClass("J-dropable").append("<div class='J-blankDiv'>&nbsp;</div>");
					//drop事件
					$(".J-dropable").live("drop",function(ev){
						$(this).find(".J-blankDiv").eq(0).before($(window.dragObject).parent());
						dragDropEnd(ev);					
						return false; 
					});
					$(".J-dropable").live("dragover",function(ev){
						var draggable =$(ev.target).hasClass("J-dropable");	
						if(draggable) 
						{
							$("#dragBackground").remove();
							$(this).find(".J-blankDiv").eq(0).before(dragBackgroundHtml);
							$("#dragBackground").on("dragover",function(ev){
								return false;
							});
							$("#dragBackground").on("drop",function(ev){
								$(window.dragObject).parent().insertBefore($(ev.target).parents(".construct").eq(0));
								dragDropEnd(ev);	
								return false; 
							});
							ev.stopPropagation();
							return false;
						}
						else 
							return true; 
					});
				}
		}
	};
});