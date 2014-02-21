define(function(require, exports, module) {
	// 依赖jQuery
	var $ = require("$");
	// 依赖字符串工具
	var StringUtils= require('/ecp/component/ecp/tools/StringUtils.js');
	
	/**
	 * 包装dom属性，返回json格式
	 */
	exports.getJsonFromDom = function(dom) {
		var attributes = dom.attributes;
		var json = {};
		for (var index in attributes) {
			var node = attributes.item(index);
//			console.log(node);
			var name = node.name;
			var value = node.value;
			json[name] = value;
		}
//		console.log(json);
		return json;
	};
});