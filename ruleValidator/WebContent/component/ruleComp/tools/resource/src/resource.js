//define("resource", [], function(require, exports, module) {
//	var $ = require("$");
//
//	var store = require("store");
//	var Resource = {
//		data : {},
//		init : function(key) {
//			if (!store.get("resource")) {
//				var that = this;
//				var url = G.API.RESOURCE;
//				$.ajax({
//					url : url,
//					type : 'GET',
//					async : false,
//					success : function(data, status, response) {
//						if (response.responseText != "gotoLogin" && response.responseText != "kickout") {
//							that.data = data;
//							store.set("resource", data);
//						}
//					}
//				});
//			}
//		},
//		// 获取所有模块
//		getModules : function() {
//			return store.get("resource");
//		},
//		// 获取模块下的页面
//		getPages : function(moduleCode) {
//			var data = store.get("resource");
//			var result = null;
//			if (moduleCode) {
//				for ( var i = 0; i < data.length; i++) {
//					if (data[i].code == moduleCode) {
//						result = data[i];
//						break;
//					}
//				}
//			}
//			return result;
//		},
//		// 获取页面信息
//		getPage : function(pageCode) {
//			var data = store.get("resource");
//			var result = [];
//			if (pageCode) {
//				for ( var i = 0; i < data.length; i++) {
//					var children = data[i].children;
//					if (children.length > 0) {
//						for ( var j = 0; j < children.length; j++) {
//							if (children[j].code == pageCode) {
//								result.push(data[i]);
//								result.push(children[j]);
//								return result;
//							}
//						}
//					}
//				}
//			}
//		},
//		// 根据菜单编码获取菜单名称
//		getResourceByCode : function(code) {
//			var data = store.get("resource");
//			if (data) {
//				for ( var i = 0; i < data.length; i++) {
//					if (data[i].code == code) {
//						return data[i]["name"];
//					}
//				}
//			}
//		}
//	};
//	Resource.init();
//	module.exports = Resource;
//});