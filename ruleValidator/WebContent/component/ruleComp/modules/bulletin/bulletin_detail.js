define(function(require, exports, module) {
			var $ = require("$");
			$ = require("jquery.extend")($);
			var Widget = require("widget");
			require("./bulletinDetail.css");
			var handlebars = require("handlebars");
			var detail_tpl = require("./bulletin_detail.tpl");
			var Uri = require("jsuri");
			handlebars.registerHelper("html", function(content) {
				return new handlebars.SafeString(content || "");
			});

			function downloadAttachment(dom) {
				var node = $(this);
				var command = G.API.DOWNLOAD;
				var path = node.attr('path');
				var fileName = node.attr('fileName');
				var parameter = 'realPath=' + path + '&fileName=' + fileName;
				var targetUrl = encodeURI(encodeURI(command + '?' + parameter));
				window.open(targetUrl);
			};

			var bulletinDetail = Widget.extend({
						template : detail_tpl,
						handlebars:handlebars,
						loadData : function() {
							var bulletinDetail = this;
							var bulletinId = new Uri(window.document.location.href).getQueryParamValue("bulletinId");
							$.ajax({
								url : G.API.BULLETIN_DETAIL,
								data:{
									"bulletinId":bulletinId
								},
								success : function(data) {
									bulletinDetail.setModel(data);
								},
								dataType : 'json'
							});
						},
						bindEvent : function() {
							$(".J-bulletin-download").live('click', downloadAttachment);
						}
					});
			module.exports = bulletinDetail;
		});