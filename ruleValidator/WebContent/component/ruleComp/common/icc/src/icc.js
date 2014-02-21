define(function(require, exports, module) {
	// 默认引入：组件继承自widget
	var Widget = require("widget");
	// 自定义：组件的html模板
	var template = require("./icc.tpl");
	var store = require("store");
	var userCode = store.get("user").userCode;
	var store = require("store");
	// 组件的定义 组件名大写
	var Icc = Widget.extend({
		iccServer :"",
		initCustAttr:function(){
			//window.hjUserData = "姓名|性别|固定电话|手机|邮箱|地址|公司名称|MSN|QQ|会员ID|会员等级|扩展信息";
			window.hjUserData = userCode;
			var attrs = this.get("attrs");
			if(store.get("param").iccServer!=null){
				this.iccServer = store.get("param").iccServer;
			}
			attrs.iccServer = this.iccServer;
			this.set("attrs",attrs);
		},
		template : template,
		afterRender : function() {
			if(this.iccServer !=""){
				seajs.emit("iccEnable");
			}
		}
	});
	// 组件对外提供使用
	module.exports = Icc;
});
