/*后台管理，样式自定义左侧导航*/
define(function(require, exports, module) {
	//主题数据模型 第一组系统主题，第二组自定义主题
	var themeconfig=[{
		themes:[
		        {id:"default",name:"经典蓝",css:"btn-default"},//主题名称、名称、对应图片样式
		        {id:"grace",name:"粉红色",css:"btn-pink"},
		        {id:"red",name:"橙色",css:"btn-danger"},
		        {id:"green",name:"绿色",css:"btn-success"},
		        {id:"purple",name:"紫色",css:"btn-purple"},
	            {id:"gray",name:"灰色",css:"btn-gray"}
		        ]},{
		themes:[ {id:"selfblue",name:"自定义蓝",css:"theme-self"}]},{
		themeSelected:"default"}]; //当前使用的主题名称、以及对应的主题索引	
	var Widget = require("widget");
	var Store = require("store");
	require("dialog");
	if(!Store.get("themeconfig"))
		Store.set("themeconfig",themeconfig);
	var $ = require("$");
	var template = require("./themeleftnav.tpl");
	var dialog = require("dialog");
	require("../theme/default/css/themeleftnav.css");
		//获取所有首页显示组件
	var portletConfig = require("portlet_config");
	var pannelConfig = portletConfig.pannel_config;
	var layoutWidthConfig = portletConfig.layout_config;
	var themeleftnav = Widget.extend({
		template:template,
		initCustAttr:function(){
			this.set("systhemedropdown",Store.get("themeconfig")[0]["themes"]);
			this.set("selfthemedropdown",Store.get("themeconfig")[1]["themes"]);
			var searchpara= window.location.search.split("=")[1];			
			this.set("searchpara",searchpara);
		},
		render:function(){
			themeleftnav.superclass.render.apply(this, arguments);
		},
		afterRender:function(){
			//设置左侧list显示
			if(this.get("searchpara")){
				var id = this.get("searchpara");
				$("#"+id).removeClass("hide");
				$("#"+id).find(".collapse").addClass("in");
				$('#tabs a').each(function(){
					$(this).hide();
				});
				$(".contentdiv").hide();
				//展开收起按钮
				$(".J-btn-add").on("click",function(){
					$(this).next("div").css("display")=="none"?$(this).next("div").show("normal"):$(this).next("div").hide("normal");
					if($(this).find("span").length>0){
						var span = $(this).find("span");
						if(span.hasClass("glyphicon-minus")){
							span.removeClass("glyphicon-minus");
						}
						else{
							span.addClass("glyphicon-minus");
						}
					}
				});
				if(id=="widgetstyle"){
					$('#tabs a').eq(2).show().tab("show");
					$('#tabs a').eq(3).show();
				}else{
					$('#tabs a').eq(0).show();
					$('#tabs a').eq(1).show();
				}
			}
			//头部信息
			var topInfo = Store.get("compropsetting").indexsetting.topInfo!=""?Store.get("compropsetting").indexsetting.topInfo:portletConfig.topinfo_default;
			//底部信息
			var footInfo = Store.get("compropsetting").indexsetting.footInfo!=""?Store.get("compropsetting").indexsetting.footInfo:portletConfig.footinfo_default;
			$("#topInfo").val(topInfo["topInfo"]+topInfo["topInfoContent"]);
			$("#footInfo").val(footInfo["footInfo"]);
			//布局信息
			var layoutConfig = store.get("layoutConfig");
			//设置布局选中状态
			this.setLayoutConfig(layoutConfig[0]["width"]);
			//提示信息
			$("#loginAlert").val(Store.get("compropsetting").loginsetting.login_alert||portletConfig.loginalert_default);
			$("#loginTel").val(Store.get("compropsetting").loginsetting.login_tel||portletConfig.logintel_default);
			$("#loginEmail").val(Store.get("compropsetting").loginsetting.login_email||portletConfig.loginemail_default);
			//联系电话
			//email
		},
		bindEvent:function(){
			//关闭页面时去掉保存按钮
			$(window).unload(function(){
				$(top.window.document).find("#save").hide();
			});
			var that = this;
			var iframeWindow =$("#selfiframe").get(0).contentWindow;
			var iframeDocument = $($("#selfiframe").get(0).contentWindow.document);
			//刷新保存按钮样式
			var refreshSaveButton = function(){
				$(top.window.document).find("#save").removeClass("btn-info").addClass("btn-primary");
			};
			//头部信息修改
			$("#topInfo").on("blur",function(){
				var topInfo = Store.get("compropsetting").indexsetting.topInfo;
				if($("#topInfo").val()!=topInfo["topInfo"])
				{	
					refreshSaveButton();
					topInfo["topInfo"] = $(this).val();
					topInfo["topInfoContent"] = "";
					$($("#selfiframe").get(0).contentWindow.document).find("#topInfo").html(topInfo["topInfo"]);
					$($("#selfiframe").get(0).contentWindow.document).find("#topInfoContent").html(topInfo["topInfoContent"]);
					var data = getcompropsetting();
					data.indexsetting.topInfo = topInfo;
					Store.set("compropsetting",data);
				}
			});
			//底部信息修改
			$("#footInfo").on("blur",function(){
				var footInfo = Store.get("compropsetting").indexsetting.footInfo;
				if($("#footInfo").val()!=footInfo["footInfo"])
				{	
					refreshSaveButton();
					$($("#selfiframe").get(0).contentWindow.document).find("#footInfo").html($("#footInfo").val());
					footInfo["footInfo"] = $(this).val();
					var data = getcompropsetting();
					data.indexsetting.footInfo = footInfo;
					Store.set("compropsetting",data);
				}
			});
			//登录页面联系电话修改
			$("#loginTel").on("blur",function(){
				var loginTel = Store.get("compropsetting").loginsetting.login_tel;
				if($(this).val()!=loginTel){
					refreshSaveButton();
					var data = getcompropsetting();
					data.loginsetting.login_tel = $(this).val();
					Store.set("compropsetting",data);
					reloadindexlogin();
				}
			});
			//登录页面email修改
			$("#loginEmail").on("blur",function(){
				var loginEmail = Store.get("compropsetting").loginsetting.login_email;
				if($(this).val()!=loginEmail){
					refreshSaveButton();
					var data = getcompropsetting();
					data.loginsetting.login_email = $(this).val();
					Store.set("compropsetting",data);
					reloadindexlogin();
				}
			});
			//登录提示信息修改
			$("#loginAlert").on("blur",function(){
				var loginAlert = Store.get("compropsetting").loginsetting.login_alert;
				if($(this).val()!=loginAlert){
					refreshSaveButton();
					var data = getcompropsetting();
					data.loginsetting.login_alert = $(this).val();
					Store.set("compropsetting",data);
					reloadindexlogin();
				}
			});
			//切换主题重载页面
			var reloadindexpage = function(){
				$("#tabs a").eq(0).tab('show');
				$("#selfiframe").get(0).contentWindow.document.location.reload();
			};
			//切换登录重载页面
			var reloadindexlogin = function(){
				$("#tabs a").eq(1).tab('show');
				$("#loginiframe").get(0).contentWindow.document.location.reload();
			};
			//改变主题颜色
			$("#themecolor .btn").on("click",function(){
				var theme = $(this).val();
				Store.set("themecolor",theme);
				var themeconfig = Store.get("themeconfig");
				themeconfig[2]["themeSelected"] = theme;
				Store.set("themeconfig",themeconfig);
				reloadindexlogin();
				reloadindexpage();
			});
			//主題保存按钮事件
			$(top.window.document).find("#save").show().unbind("click").on("click",function(){
				$(this).removeClass("btn-primary").addClass("btn-info");
				var savedata = {};
				var data = portletConfig.compropsetting_default;
				var layoutConfig = Store.get("layoutConfig");
				//布局设置
				if(layoutConfig)
					savedata["layoutConfig"] = layoutConfig;
				//企业信息设置
				if(store.get("compropsetting")){
					data.loginsetting.login_logo = store.get("compropsetting").loginsetting.login_logo;
					data.loginsetting.login_left = store.get("compropsetting").loginsetting.login_left;
					data.loginsetting.login_bg = store.get("compropsetting").loginsetting.login_bg;
					data.loginsetting.login_email = store.get("compropsetting").loginsetting.login_email;
					data.loginsetting.login_alert = store.get("compropsetting").loginsetting.login_alert;
					data.loginsetting.login_tel = store.get("compropsetting").loginsetting.login_tel;
					
					data.indexsetting.logo_change = store.get("compropsetting").indexsetting.logo_change;
					data.indexsetting.topInfo = store.get("compropsetting").indexsetting.topInfo;
					data.indexsetting.footInfo = store.get("compropsetting").indexsetting.footInfo;
					savedata["compropsetting"] = data;
				}
				//商品描述属性设置
				if(store.get("productinfo")||store.get("productinfo")==""){
					savedata["productinfo"] = store.get("productinfo");
				}
				//商品描述tab页设置
				if(store.get("producttab")||store.get("producttab")==""){
					savedata["producttab"] = store.get("producttab");
				}
				//商品列表保存
				if(store.get("productlist")||store.get("productlist")==""){
					savedata["productlist"] = store.get("productlist");
				}
				//主题颜色
				if(store.get("themecolor")){
					savedata["themecolor"]= "'"+store.get("themecolor")+"'";
				}
				//主题颜色配置
				if(store.get("themeconfig")){
					savedata["themeconfig"]= store.get("themeconfig");
				}
				$.ajax({
								type : 'POST',
								data:{"data":JSON.stringify(savedata)},
								url : G.API.MANAGEMENT_UIPROPERTIESUPDATE,
								contentType : "application/x-www-form-urlencoded;charset=utf-8",
								success : function(data) {
									var option={
											title:"提示",
											content:data.result,
											moveToInner:true
										};
									dialog.alert(option);
									
								},
								error : function(data) {
									var option={
											title:"提示",
											content:data.result
										};
									dialog.alert(option);
								}
				});
			});
			//logo图片上传按钮客户端校验事件
			$("#logoSubmit,#loginLogoSubmit,#loginLeftPicSubmit").on("click",function(){
				if($(this).parents(".input-group").find("input[type=file]").val()=="")
				{
					alert("未选择图片。");
					return false;
				}
				return true;
			});
			$("input[type='file']").on("mouseover",function(){
				$(this).tooltip("show");
			});
			//首页logo恢复默认按钮
			$("#logoDefault").on("click",function(){
				var data = getcompropsetting();
				data.indexsetting.logo_change ="";
				Store.set("compropsetting",data);
				reloadindexpage();
			});
			//登录logo恢复默认按钮
			$("#loginLogoDefault").on("click",function(){
				var data = getcompropsetting();
				data.loginsetting.login_logo ="";
				Store.set("compropsetting",data);
				reloadindexlogin();
			});
			//登录左侧图片恢复默认按钮
			$("#loginLeftPicDefault").on("click",function(){
				var data = getcompropsetting();
				data.loginsetting.login_left ="";
				Store.set("compropsetting",data);
				reloadindexlogin();
			});
			//登录背景图片恢复默认按钮
			$("#loginBGDefault").on("click",function(){
				var data = getcompropsetting();
				data.loginsetting.login_bg ="";
				Store.set("compropsetting",data);
				reloadindexlogin();
			});
			//文件上传后回调函数,将改变logo状态保存到logo_change之中
			window.logoUploadCallback = function(filePath){ 
				var data = getcompropsetting();
				data.indexsetting.logo_change = filePath;
				Store.set("compropsetting",data);
				reloadindexpage();
			};
			window.loginLogoUploadCallback = function(filePath){
				var data = getcompropsetting();
				data.loginsetting.login_logo = filePath;
				Store.set("compropsetting",data);
				reloadindexlogin();
			};
			window.loginLeftUploadCallback = function(filePath){
				var data = getcompropsetting();
				data.loginsetting.login_left = filePath;
				Store.set("compropsetting",data);
				reloadindexlogin();
			};
			window.loginLBGUploadCallback = function(filePath){
				var data = getcompropsetting();
				data.loginsetting.login_bg = filePath;
				Store.set("compropsetting",data);
				reloadindexlogin();
			};
			//获取当前store中存储的comprosetting
			function getcompropsetting(){
				var data = Store.get("compropsetting")||portletConfig.compropsetting_default;
				return data;
			}
			// 布局-布局选择事件
			$("#collapsePanel li a").live("click", function() {
				if($(this).find(".click").length==0){
					// 从代码中获取当前store种存储的config，根据不同布局重写config,然后存入store中，并重载页面
					var layoutConfig = store.get("layoutConfig");
					// 三列 所有数值之和为98% 留出2%为doc_content的margin-left
					if ($(this).attr("id") == "layout-aaa") {
						layoutConfig = that.layoutWidthSetting(layoutWidthConfig[0][0], layoutWidthConfig[0][1], layoutWidthConfig[0][2],
								layoutConfig);
					}
					// 左右一致宽度
					else if ($(this).attr("id") == "layout-aa") {
						layoutConfig = that.layoutWidthSetting(layoutWidthConfig[1][0], layoutWidthConfig[1][1], layoutWidthConfig[1][2],
								layoutConfig);
					}
					// 左6右4
					else if ($(this).attr("id") == "layout-ba") {
						layoutConfig = that.layoutWidthSetting(layoutWidthConfig[2][0], layoutWidthConfig[2][1], layoutWidthConfig[2][2],
								layoutConfig);
					}
					// 坐4右6
					else if ($(this).attr("id") == "layout-ab") {
						layoutConfig = that.layoutWidthSetting(layoutWidthConfig[3][0], layoutWidthConfig[3][1], layoutWidthConfig[3][2],
								layoutConfig);
					}
					store.set("layoutConfig", layoutConfig);
					that.setLayoutConfig(layoutConfig[0]["width"]);
					// 修改布局后重新加载
					reloadindexpage();
				}
			});
			//首页组件维护-组件维护按钮事件
			$("#btnFresh , .J-panelClick").live("click",function(){
					that.refreshPanelEdit(iframeDocument);
			});
			//首页组件维护-添加组件点击事件
			$("#collapsePanleEdit .J-addBtn").live("click",function(){
				var index = $(this).attr("name");
				var outterThat = that;
				$(this).removeClass("btn-primary");
				$(this).attr("disabled","disabled");
				var item = pannelConfig[index];
				if(iframeDocument.find(".side_column #"+$(this).attr("id")).length==0){
					iframeDocument.find(".side_column").prepend("<div id='"+$(this).attr("id")+"' data-widget='"+item.name+"' class='construct' data-widget-auto-rendered='true'></div>")
					iframeWindow.seajs.use(item.name,
						function(widget) {
							if (widget) {
								new widget({
									attrs : item.attrs,
									renderTo : iframeDocument.find(".side_column").find(".construct").eq(0).get(0)
									});
								outterThat.afterPannelAdd(index,iframeDocument);
							}
					});
				}
			});
			//打开商品描述tab页
			$("#productstyle").on("click",function(){
				$("#tabs a").eq(2).tab('show');
			});
			//打开商品列表tab页
			$("#productliststyle").on("click",function(){
				$("#tabs a").eq(3).tab('show');
				parent.seajs.emit('event_widget_rendered');
			});
			
		},
		//刷新组件添加面板
		refreshPanelEdit:function(iframeDocument){
			//清除之前的列表
			$("#collapsePanleEdit .panel-body .list-group").remove();
			var htmlContent =$("<ul class='list-group'></ul>");
			//从所有组件列表中循环遍历是否首页已存在该组件
			for(i in pannelConfig)
			{
				if(pannelConfig[i].attrs!=null){
						htmlContent.append("<li class='list-group-item'><span class='label label-info'><span class='glyphicon glyphicon-star'></span>"+pannelConfig[i].attrs.title+"</span><button id='"+pannelConfig[i].id+"' name='"+i+"' type='button' class='J-addBtn pull-right btn btn-default btn-xs'><span class='glyphicon glyphicon-plus'></span> 添加组件</button></li>");
						if(iframeDocument.find("#construct #"+pannelConfig[i].id).length==0)
						{
							$(htmlContent).find("#"+pannelConfig[i].id).addClass("btn-primary");
						}
						else{
							$(htmlContent).find("#"+pannelConfig[i].id).attr("disabled","disabled");
						}
				}
			}
			//重绘组件列表
			$("#collapsePanleEdit .panel-body").append(htmlContent);
		},
		//改变多列布局方式
		layoutWidthSetting : function(left, center, right, layoutConfig) {
				layoutConfig[0].width = left;
				layoutConfig[1].width = center;
				layoutConfig[2].width = right;
				// 如果不为三列布局
				if (left != layoutWidthConfig[0][0]) {
					var temp = layoutConfig[2].widgets;
					for (var i = 0; i < temp.length; i++)
						layoutConfig[0].widgets.push(temp[i]);
					layoutConfig[2].widgets = [];
				}
				return layoutConfig;
		},
		setLayoutConfig : function(columnWidth){
				$(".click").removeClass("click");
				/*此处需要修改 方式*/
				if(columnWidth==layoutWidthConfig[0][0]){
					$("#layout-aaa strong").addClass("click");
				}
				else if(columnWidth==layoutWidthConfig[1][0]){
					$("#layout-aa strong").addClass("click");
				}
				else if(columnWidth==layoutWidthConfig[2][0]){
					$("#layout-ba strong").addClass("click");
				}
				else{
					$("#layout-ab strong").addClass("click");
				}	
		},
		//drop事件之后重写保存在store中的layoutConfig,重写页面布局配置数据,colorChangeId,和color用于保存颜色时用的参数
		afterPannelAdd:function(index,iframeDocument){
				if(store.get("layoutConfig"))
				{
					var construct = iframeDocument.find("#construct");
					if(construct.length>0)
					{
						//通过id 将旧的 config转成 新的config，而此时id在页面重新加载后才会按新的改变（而config已经改变），所以保留旧的config
						var layoutConfigTemp;
						var layoutConfig = [];
						layoutConfigTemp = store.get("layoutConfigTemp");
						layoutConfigTemp[0]["widgets"].push(pannelConfig[index]);
						construct.children().each(function(i,element){
							var dataSet = {};
							dataSet["width"] = layoutConfigTemp[i].width;
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
									if(!flag)
									{
										dataSet["widgets"].push(pannelConfig[index]);
									}
								}
							});
							layoutConfig.push(dataSet);
						});
						store.set("layoutConfig",layoutConfig);
						store.set("layoutConfigTemp",layoutConfigTemp);
					}
				}
		}
	});
	module.exports = themeleftnav;
});
