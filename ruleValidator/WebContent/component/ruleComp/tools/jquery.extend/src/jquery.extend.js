define(function(require, exports, module) {
	return function(jQuery) {
		var Log = require("log");
		var store = require("store");
		var JSON = require("json");
		/* 重写jquery的ajax方法增加自定义扩展 */
		(function($) {
			// 备份jquery的ajax方法
			var _ajax = $.ajax;
			// 重写jquery的ajax方法
			$.ajax = function(opt) {
				// 备份opt中error和success方法
				var fn = {
					error : function(XMLHttpRequest, textStatus, errorThrown) {
					},
					success : function(data, textStatus) {
					}
				};
				if (opt.error) {
					fn.error = opt.error;
				}
				if (opt.success) {
					fn.success = opt.success;
				}
				if (opt.url.indexOf("?ts") < 0 && opt.url.indexOf("?token") < 0) {
					opt.url = opt.url + "?ts="+ new Date().getTime();
					if(store.get("user")){
						opt.url =  opt.url + "&token="+store.get("user").token;
					}
				}
				var gotoLogin = function(message) {
					if (!G.offline) {
						G.offline = true;
						if (store.get("user")) {
							alert(message);
							// 每次登陆清空菜单和上下文配置缓存
							store.remove("user");
							store.remove("resource");
							store.remove("context");
							store.remove("param");
							store.remove("favorite");
						}
						top.window.location = G.PAGE.LOGIN;
						return;
					}
				};
				// 扩展增强处理
				var _opt = $.extend(opt, {
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						if (XMLHttpRequest.responseText == "gotoLogin") {
							gotoLogin("您已离开太久，请重新登录！");
						} else if (XMLHttpRequest.responseText == "kickout") {
							gotoLogin("由于当前用户在其他客户端强制登录，本客户端将退出工作台！");
						} else if(XMLHttpRequest.responseText=="invalidToken"){
							gotoLogin("您已离开太久，请重新登录！");
						}else{
							try {// 取得错误信息的名称，后面需要加上“}”才能解析为json
								var message = JSON
										.parse(XMLHttpRequest.responseText
												.split(",")[0]
												+ '}');
								// 错误方法增强处理
							} catch (e) {}
							fn.error(XMLHttpRequest, textStatus, errorThrown,
									message);
						}
					},
					success : function(data, textStatus, response) {
						if (response.responseText == "gotoLogin") {
							gotoLogin("您已离开太久，请重新登录！");
						} else if (response.responseText == "kickout") {
							gotoLogin("由于当前用户在其他客户端强制登录，本客户端将退出工作台！");
						} else {
							if (data && data.message) {
								Log.error(data.detail);
							}
							fn.success(data, textStatus, response);
						}
					}
				});
				_ajax(_opt);
			};
		})(jQuery);
		return jQuery;
	};
});
