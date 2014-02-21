define(function(require, exports, module) {
	var store = require("store");
	var $ =require("$");
			var Param = {
				params : {},
				init : function(key) {
					if (!store.get("param")) {
						var that = this;
						var url = G.API.PARAM;
						$.ajax({
									url : url,
									type : 'GET',
									async : false,
									success : function(data,status,response) {
										if (response.responseText != "gotoLogin" && response.responseText != "kickout") {
											that.params = data;
											store.set("param", data);
										}
									}
								});
					}
				},
				get : function(key) {
					if (store.get("param")) {
						return store.get("param")[key];
					}
				}
			};
			Param.init();
			G.ca = Param.get("caProvider");
			G.title = Param.get("title");
		});