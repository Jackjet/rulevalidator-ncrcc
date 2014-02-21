/**
 * 模板
 */
define(function(require, exports, module) {
	
	// 字符工具类
	var StringUtils = require('/ecp/component/ecp/tools/StringUtils.js');
	
	// 模板
	var tpl = '';
//	// 数据
//	this.data = config.data;
	// 标识符（开始）
	this._fstart = '$';
	// 标识符（左分界）
	this._fleft = '{';
	// 标识符（有分界）
	this._fright = '}';
	// 符合占位符的正则表达式
	var reg =  /\$\{([_a-zA-Z][\._a-zA-Z0-9]*)\}/gm;
	// 匹配数量
	var matchers = [];
	
	exports.setTemplate = function(template) {
		tpl = template;
		return this;
	};
	
	
	/**
	 * 获取json对象的值
	 * @param {} express 属性表达式
	 * @return {} 值
	 */
	function getValue(data, express) {
		if (!express) {
			throw new Error(StringUtils.format('属性表达式不能使空字符串，express=', [express]));
		}
		var attributes = express.split('.');
		var value = data;
		$.each(attributes, function(iAttribute, attribute) {
			value = value[attribute];
		});
		return value;
	};
	
	/**
	 * 将模板中的占位符替换为json对象中的值
	 */
	exports.apply = function(data) {
		var str = '';
		str = tpl.replace(reg, function(express, attributeExpress, index, source) {
			var value = getValue(data, attributeExpress);
			// 记录匹配信息
			matchers.push({
				index:index,
				express:express,
				value:value
			});
			return value;
		});
		return str;
	};
	
	/**
	 * 打印跟踪信息
	 */
	exports.trace = function() {
		$.each(matchers, function(i, matcher) {
			console.log(StringUtils.format('index[%s]，express[%s]，value[%s]', [matcher.index, matcher.express, matcher.value]));
		});
	};
	
	/**
	 * 获取打印信息（html格式）
	 */
	exports.getTrace = function() {
		var html = '';
		$.each(matchers, function(i, matcher) {
			html += StringUtils.format('index[%s]，express[%s]，value[%s]', [matcher.index, matcher.express, matcher.value]) + '<br>';
		});
		return html;
	};

	
});