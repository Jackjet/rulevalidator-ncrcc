define(function(require, exports, module) {
	var template = require("./syspropconfig.tpl");
	var handlebars = require('handlebars');
	var dialog = require('dialog');
	var $ = require("$");
	var Widget = require("widget");
	//ecpsystem.properties属性对应中文名
	var propDetailConfig = {
		"accountcode" : "对应的NC帐套编码",
		"ejbaddress" : "NC服务器地址",
		"datasource" : "对应的NC数据源",
		"index.dir" : "索引存放位置",
		"index.groupid" : "索引-搜索分组id",
		"index.sourceid" : "索引-对应的NC数据源",
		"isProductMode" : "是否是产品模式",
		"isSingleLogin" : "是否支持单点登录",
		"isRegister" : "是否注册",
		"fastdfs.tracker.conf" : "无描述",
		"portal" : "portal地址",
		"title" : "标题",
		"web.cache" : "配置缓存类型，部署在NC上(simple),部署在独立tomcat(ehcache)",
		"web.host" : "host地址",
		"web.com.port" : "远程通讯端口",
		"web.port" : "门户访问端口",
		"web.dbfirst" : "需要各自配置门户地址，设置web.dbfirst=false否则true",
		"web.page.alivetime" : "分页数据在内存的存活期",
		"web.page.maxcount" : "查询数据最大数量"
	};
	//注册foreach组件,输出对象中所有属性
	handlebars.registerHelper("foreach", function(context, option) {
		if(context){
			var html = "";
			var j = 0;
			for ( var i in context) {
				var name = propDetailConfig[i]||"";
				j++;
				var line = "<tr><td>" + j + "</td><td>" + name + 
				"</td><td>" + i + "</td><td><input type='text' name='"+i+"' class='form-control' value='"+ 
				context[i] + "'/><input type='hidden' value='"+ 
				context[i] + "'/></td></tr>";
				html+=line;
			}
			return html;
		}
	});
	var Syspropconfig = Widget
			.extend({
				template : template,
				render : function() {
					Syspropconfig.superclass.render.apply(this, arguments);
				},
				loadData : function() {
					var that = this;
					$.ajax({
						type : 'GET',
						dataType : 'json',
						url : G.API.MANAGEMENT_SYSCONFIG_QUERY,
						contentType : "application/x-www-form-urlencoded;charset=utf-8",
						success : function(data) {
							that.setModel(data);
						},
						error : function(data) {
							alert(data);
						}
					});
				},
				bindEvent:function(){
					var that = this;
					//修改保存后按钮样式
					var resetSaveButton = function(){
						$("#save").removeClass("btn-primary").addClass("btn-info");
					};
					//鼠标离开input事件
					$(".form-control").live("blur",function(){
						if($(this).val()!=$(this).find("~ input").val()){
							$(this).parent().addClass("has-success");
							if(!$("#save").hasClass("btn-primary")){
								$("#save").removeClass("btn-info").addClass("btn-primary");
							}
						}
						else{
							$(this).parent().removeClass("has-success");
							if($(".has-success").length==0){
								resetSaveButton();
							}
						}
					});
					//绑定取消按钮事件
					$("#cancel").live("click",function(){
						$(".form-control").each(function(){
							$(this).val($(this).find("~ input").val()).parent().removeClass("has-success");
							if($("#save").hasClass("btn-primary")){
								resetSaveButton();
							}
						});
					});
					$("#save").live("click",function(){
						var data = {};
						var changedElement = $(".has-success .form-control");
						if(changedElement.length>0){
							changedElement.each(function(){
								data[$(this).prop("name")]=$(this).val();
							});
							$.ajax({
								type : 'POST',
								data:{"data":JSON.stringify(data)},
								url : G.API.MANAGEMENT_SYSCONFIG_UPDATE,
								contentType : "application/x-www-form-urlencoded;charset=utf-8",
								success : function(data) {
									
									$(".has-success .form-control").each(function(){
										$(this).find("~ input").val($(this).val());
									});
									$(".has-success").removeClass("has-success");
									resetSaveButton();
									var option={
											title:"提示",
											content:data.result
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
						}
						else{
							var option={
									title:"提示",
									content:"值没有被修改！"
								};
							dialog.alert(option);
						}
					});
					
				}

			});
	module.exports = Syspropconfig;
});