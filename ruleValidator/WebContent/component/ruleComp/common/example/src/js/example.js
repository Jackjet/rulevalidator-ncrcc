		



				define(function(require, exports, module) {
					var Widget = require("widget");
					var $=require("$");
					require("../theme/default/css/example.css");
					var example_tpl = require("./example.tpl");
				
					var Example = Widget.extend({
						template : example_tpl,
						loadData:function(){
							var that=this;
							$.ajax({
								url:"/ecp/example/example.json",
								dataType:"json",
								success:function(data){
									that.setModel(data);
								}
							});
						}
					});
					
					module.exports = Example;
				});