define(function(require, exports, module) {
	// jquery
	var $ = require('$');
	// 依赖handlerbar
	var handlebars = require('handlebars');
	// 依赖FavoriteDialog
	var dialog = require('./FavoriteDialog.js');
	// 大按钮模板
	var template_big_button = require('./add_to_big.tpl');
	// 小按钮模板
	var template_small_button = require('./add_to_small.tpl');
	
	
	module.exports = function(options) {
		// 商品主键
		
		var productId = options.productId;
		if (!productId) {
			throw new Error('没有商品主键');
		}
		
		// 新增操作的url
		var renderTo = options.renderTo;
		if (!renderTo) {
			throw new Error('渲染到什么位置呢');
		}
		
		var type = options.type || 'small';
		
		var html = '';
		if (type == 'big') {
			html = handlebars.compile(template_big_button)({
				productId:productId
			});
		} else {
			html = handlebars.compile(template_small_button)({
				productId:productId
			});
		}
		
		// 渲染dom
		var target;
		if (typeof(renderTo) == 'string') {
			target = $('#' + renderTo);
		} else {
			target = $(renderTo);
		}
		target.html(html);
		
		// 鼠标点击弹出收藏对话框
		target.click(function() {
			var a = $(this).find('a');
			dialog.init({
				productId:productId,
				doConfirm:function() {
					a.addClass('added');
				}
			});
		});
		
	};
});
