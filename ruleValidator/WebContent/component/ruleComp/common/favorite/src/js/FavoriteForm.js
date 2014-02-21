define(function(require, exports, module) {
	var $ = require('$');
	var handlebars = require('handlebars');
	var template = require('./tag_form.tpl');
	
	var Widget = require('widget');
	
	Widget.extend({
		template:template,
		loadDate:function() {
			
		},
		
	});
	
	/**
	 * 交互
	 */
	// TODO 补充标签选择的交互，以及增加一个隐藏域，用于表单提交
	function addEffect() {
		alert('addEffect');
	}
	
	/**
	 * 合并属性
	 */
	function mergeOptions(options) {
		// 加载默认设置
		var defaultOptions = {
								// 我的收藏夹url
								queryUrl:G.PAGE.ACCOUNT_FAVORITE,
								// 获取我的所有收藏标签url
								myTagsUrl:G.API.ACCOUNT_FAVORITE_TAGS,
								// 最多标签数
								maxTagCount:6,
								// 提交的文本域名
								fieldName:'tags',
								// 提交后的回调函数
								doConfirm:function() {}
							};
		// 合并配置
		options = options || {};
		for (var attr in options) {
			defaultOptions[attr] = options[attr];
			if (typeof(defaultOptions[attr]) == 'object' || defaultOptions[attr] == 'function') {
				mergeOptions(defaultOptions[attr]);
			}
		}
		return defaultOptions;
	}
	
	/**
	 * 从服务器端获取当前用户已经拥有的标签
	 */
	function init(options) {
		$.ajax({
			url : G.API.ACCOUNT_FAVORITE_TAGS,
			success : function(data) {
				if (data) {
					var tagMap = data;
					var tags = [];
					var counter = 0;
					for (var tag in tagMap) {
						if (counter++ < options.maxTagCount) {
							tags.push(tag);
						}
					}
					
					var html = handlebars.compile(template)(tags);
					$(options.renderTo).html(html);
					
					// TODO 从这里增加使用例子
					// 增加交互处理
					addEffect();
				} else {
					throw new Error('没有获取到当前用户的标签信息');
				}
			}
		});
	}
	
	module.exports = init;
});
