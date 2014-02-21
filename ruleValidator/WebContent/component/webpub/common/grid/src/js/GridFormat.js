
/**
 * Grid 列扩展组件
 * 
 */
define(function(require, exports, module) {
	
	/*格式化*/
	var Formatter = require("formatter");
	
	var GridFormat = {
		DATE:function(value,data){
			return Formatter.fmtDate(value);
		},
		STR_DATE:function(value,data){
			return Formatter.fmtStrDate(value);
		},
		LONG_DATE:function(value,data){
			return Formatter.fmtLongDate(value);
		}
	};
	module.exports = GridFormat;

});