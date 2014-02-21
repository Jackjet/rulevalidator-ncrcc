
/**
 * Grid 列扩展组件
 * 
 */
define(function(require, exports, module) {
	
	/*模板支持*/
	var Handlebars = require("handlebars");
	/*格式化*/
	var Formatter = require("formatter");
	//常量类
	var Constants = require("/ecp/component/ecp/tools/constants/Constants");
	
	
	
	var EcpGridFormat = {
		/**
		 * 订单金额
		 * 添加币符和精度处理
		 */
		ORDER_SUMMY:function(value,data){
			return data['corigcurrencyid_curSign']+Formatter.fmtNum(value,data['corigcurrencyid_amountScale']);
		},
		/**
		 * 订单金额
		 * 添加币符和精度处理
		 */
		ORDER_STATUS:function(value,data){
			var orderStatus = Constants.orderStatus;
			return orderStatus.getName(value);
		}
	};
	module.exports = EcpGridFormat;

});