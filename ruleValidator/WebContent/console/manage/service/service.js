define(function(require, exports, module) {
			var $ = require("$");
			var JSON = require("json");
			$ = require("jquery.ztree")($);
			$ = require("jquery.extend")($);
			var jsl = require("jsl");
			var CodeMirror = require("codemirror");
			var setting = {
				callback : {
					onClick : function(event, id, data) {
						$("#J-allParams").html("");
						$("#name").text("");
						$("#surl").text("");
						$("#status").text(""); 
						$("#method").text(""); 
						if (data.surl != null) {
							$("#name").text(data.name);
							$("#surl").text(data.surl);
							$("#method").text(data.method);
							if (data.params) {
								for (var i = 0; i < data.params.length; i++) {
									var item = data.params[i];
									var paramSpan = '<span class="J-param"><input class="J-paramKey"  type="text" value="' + item.key + '"/><span>=</span><textarea cols="20" rows="2" class="J-paramValue"></textarea><label>' + item.comment + '</label></span>'
									$("#J-allParams").append(paramSpan);
								}
							}
						}
					}
				}
			};
			$.ajax({
						type : "GET",
						url : G.API.SERVICE_CONTROLLER_TEST,
						success : function(msg, status, response) {
							$.fn.zTree.init($("#treeDemo"), setting, msg);
						}
					});
			$("#test").click(function() {
						$("#status").text("test....");
						var url = G.ecp +"/"+ $("#surl").text();
						var params = {};
						$(".J-param").each(function() {
									var key = $(this).find(".J-paramKey").val();
									var value = $(this).find(".J-paramValue").val();
									params[key] = value;
								});
						var beginTime = new Date().getTime();
						$.ajax({
									type : $("#method").html(),
									url : url,
									data : params,
									success : function(msg, status, response) {
										var endTime = new Date().getTime();
										$("#result").html("");
										var html = ProcessObject(msg, 0, false, false, false)
										$("#Canvas").html("<PRE class='CodeContainer'>" + html + "</PRE>");
										$("#status").text("costTime:"+(endTime-beginTime)/1000+"秒");
									}
								});
					})
		});