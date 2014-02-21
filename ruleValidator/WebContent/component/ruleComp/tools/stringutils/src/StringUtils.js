/**
 * 字符串工具类
 */
define(function(require, exports, module) {
	
	/**
	 * 获取键值对
	 */
	exports.getKeyValue = function(str) {
		
		if (!str || typeof(str) != 'string') {
			return;
		}
		
		// 键值对
		var pair = {
				key:'',
				value:''
		};
		
		/**
		 * 键值对正则表达式
		 */
		var regExp = /(\w)*="(\w)*"/;
		if (str.search(regExp) > -1) {
			var matchers = str.match(regExp);
			pair.key = matchers[0];
			pair.value = matchers[1];
		}
		return pair;
	};
	
	
	/**
	 * 格式化
	 */
	exports.format = function(template, params) {
		// 异常提示
		if (typeof (template) != 'string') {
			throw new Error('模板必须是字符串类型，你提供的模板为：' + template);
		}
		if (!params || !params.length) {
			throw new Error('填充参数必须为数组类型，你提供的填充为：' + params);
		}
		if (!template.match(/%s/g)) {
			throw new Error('模板中没有占位符，请检查模板：' + template);
		}
		if (template.match(/%s/g).length != params.length) {
			throw new Error('模板占位符和填充参数格式不匹配');
		}
		params.reverse();
		while (template.match('%s')) {
			var param = params.pop();
			template = template.replace('%s', param);
		}
		return template;
	};
	
	/**
	 * 包裹
	 */
	exports.wrap = function(template, params) {
		// 特征符
		var symbol = params.symbol || '%s';
		// 左部分
		var left = params.left;
		// 右部分
		var right = params.right;
		
		var segments = template.split(symbol);
		for (var i = 0; i < segments.length - 1; i++) {
			var segment = segments[i];
			segment += left + symbol + right;
			segments.splice(i, 1, segment);
		}
		return segments.join('');
	};
	
	exports.uniqueStr=function(str){
		var s = str.split(',');
	    var dic = {};
	    for (var i = s.length; i--; ) {
	        dic[s[i]]=s[i];
	    }
	    var r = [];
	    for (var v in dic) {
	    	r.push(dic[v]);
	    }
	    return r.join();
	};

});

// console.log(new StringUtils().format('a%sklsdjfk%slajsdlf%sla', ['xxxxx', 'yyyyy', 'zzzzz']));