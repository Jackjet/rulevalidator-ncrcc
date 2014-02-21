define(function(require, exports, module) {
	require('../theme/default/css/portlet.css');
	var portlet_tpl = require("./portlet.tpl");
	var handlebars = require("handlebars");
	//引入弹框组件
	var Widget = require("widget");
	//是否可拖拽
	var isUseDrag = true;
	var Dragable = require("dragable");
	var Portletedit = require("portletedit");
	var $ = require("$");
	var Portlet = Widget.extend({
		template : portlet_tpl,
		Implements : [ Dragable,Portletedit],
		render : function() {
			Portlet.superclass.render.apply(this, arguments);
			//只在后台维护页面允许进行拖拽等相关操作
			if($(".J-portlet-dragable").length>0){
				//初始化需要绑定拖拽对应的div
				this.dragInit(this.element);
				//绑定portlet编辑事件
				this.editInit(this.element);
			}
		},
		bindEvent:function(){
		},
		isUseDrag:isUseDrag,
		afterRender:function(){
			if(this.prop.model){
				var ptpl=handlebars.compile(this.portlet_template)(this.prop);
				this.element.find(".J-content").html(ptpl);
			}
		}
	});
	module.exports = Portlet;
});