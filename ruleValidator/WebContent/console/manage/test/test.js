define(function(require, exports, module) {
			var apis = require("api");
			require("./test.css");
			var $ = require("$");
			var JSON = require("json");

			for (var api in apis) {
				var index = 0;
				$("#content table").append($("<tr>").append($("<td>")
						.append(api)).append($("<td>").append("testing....")
						.attr("id", api)));
			}
			for (var api in apis) {
				var i = 0;
				$.ajax({
							type : "GET",
							url : apis[api],
							success : function(msg,status,response) {
								var td = $("<div>");
								var key = getKeyByValue(this.url);
								if(response.responseText =="gotoLogin"){
									content = td.append($("<a>")
											.append("请登录后测试"));
								}else if (msg.message) {
									content = td.append($("<a>")
											.append(msg.message)
											.addClass("error td-tip")
											.append($("<div>")
													.append(msg.detail)
													.addClass("detail")));
								} else {
									content = td
											.append($("<a>")
															.append("success")
															.addClass("success td-tip")
															.append($("<div>")
																	.append(JSON
																			.stringify(msg))));
								}
								$("#" + key).empty().append(content)
							},
							error : function(msg) {
								
							}
						});
			}
			function getKeyByValue(value) {
				for (var api in apis) {
					if (apis[api] == value) {
						return api;
						break;
					}
				}
			}
		});