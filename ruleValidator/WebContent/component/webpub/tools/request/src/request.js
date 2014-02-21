/**
 * 解析url参数
 */
define(function(require, exports, module) {
			var request = {
				_paraObj : {},
				_init : function() {
					this._parseUrlPara();
				},
				_parseUrlPara : function() {
					var that = this;
					var url;
					if(parent){
						 url= parent.location.href;
					}else{
						url = location.href;
					}
					var paraString = url.substring(url.indexOf("?") + 1,
							url.length).split("&");
					for (i = 0; j = paraString[i]; i++) {
						that._paraObj[j.substring(0, j.indexOf("="))
								.toLowerCase()] = j.substring(j.indexOf("=")
										+ 1, j.length);
					}
				},
				getParameter : function(paras) {
					var returnValue = this._paraObj[paras.toLowerCase()];
					return returnValue;
				}
			}
			request._init();
			module.exports = request;
		});
