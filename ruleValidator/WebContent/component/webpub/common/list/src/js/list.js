/*
 * listpanel组件
 */
define(function(require, exports, module) {
			var $ = require("$");

			require('../theme/{theme}/css/list.css');

			var list_tpl = require("./list.tpl");
			var Widget = require("widget");
			var List = Widget.extend({
						template : list_tpl,
						loadData : function() {
							var list = this;
							var url = this.get("url");
							$.ajax({
										type : 'POST',
										url : url,
										success : function(data) {
											list.setModel(data);
										},
										dataType : 'json'
									});
						}
					});
			module.exports = List;
		});