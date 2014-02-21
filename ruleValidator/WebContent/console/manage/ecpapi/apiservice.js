define(function(require, exports, module) {
			var $ = require("$");
			$ = require("jquery.extend")($);
			$.ajax({
						type : "GET",
						url : "/ecp/service/baseServiceCount.json",
						success : function(msg, status, response) {
							for(var item in msg){
								console.log(item);
							}
							$("#content").html(msg);
						}
					});
		});