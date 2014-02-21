/*
 * 组件示例
 */

define(function(require, exports, module) {
	// 默认引入：组件继承自widget
	var Widget = require("widget");
	var store = require("store");
	var $ = require("$");
	// 自定义：组件对应的css
	
	require('../theme/default/css/footer.css');
	// 自定义：组件的html模板
	var template = require("./footer.tpl");
	var customsetting = require("customsetting");
	//默认信息获取
	var portletConfig = require("portlet_config");
	// 组件的定义 组件名大写
	var Footer = Widget.extend({
				template : template,
				Implements : [customsetting],
				initCustAttr : function() {
					//表示使用customsetting设置样式
					this.set("custom",true);
				},
				afterRender:function(){
					var that=this;
					window.onresize=function(){
						that.resize();
					};
					
				},
				resize:function(){
//					if(window.document.body.offsetHeight<=768){
//						this.element.css("position","relative");
//					}else{
//						this.element.css("position","absolute");
//						this.element.css("bottom","0px");
//					}
				},
				custom:function(){
					//设置底部信息
					var footInfo =portletConfig.footinfo_default; 
					if(store.get("compropsetting")&&store.get("compropsetting").indexsetting.footInfo!=""){
						footInfo =store.get("compropsetting").indexsetting.footInfo;
					}
					$("#footInfo").html(footInfo["footInfo"]);
				}
			});
	// 组件对外提供使用
	module.exports = Footer;
});