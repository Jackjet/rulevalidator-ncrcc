define(function(require, exports, module) {
	
	var $ = require('$');
	var FormField = require('formfield');
	var handlebars = require('handlebars');
	var template = require('./multiselect.tpl');
	var selectionTemplate = require('./selection.tpl');
	var theme = G.theme || 'default';
	require('../theme/{theme}/css/multiselect.css');
	var defaultOptions = require('./multiselect_options');
	var log = require('log');
	module.exports = FormField.extend({
		initCustAttr : function() {
			// 合并默认配置
			var attrs = this.prop.attrs;
			for ( var name in defaultOptions) {
				if (!attrs[name]) {
					attrs[name] = defaultOptions[name];
				}
			}
			
			// 计算候选占位块宽度
			attrs.optionsHolderWidth = parseInt(attrs.width) - parseInt(attrs.labelWidth) - parseInt(attrs._triggerWidth);
		},
		template : template,
		loadData:function() {
			var attrs = this.prop.attrs;
			var optionsData = attrs.optionsData;
			var data = [];
			// FIXME 配置计算失败
			if (optionsData.local) {
				var local = optionsData.local;
				// 本地
				if (local.string) {
					var string = local.string;
					var array = $.trim(string).split(';');
					$(array).each(function(i, item) {
						var keyValue = $.trim(item).split(':');
						var key = $.trim(keyValue[0]);
						var value = $.trim(keyValue[1]);
						data.push({
							key:key,
							value:value
						});
					});
				} else {
					data = local.json;
				}
			} else if(optionsData.remote) {
				var remote = optionsData.remote;
				// 远程
				$.ajax({
					url :remote.url,
					data:remote.data,
					// TODO 需要支持懒加载，减少组件初始化造成的延迟
					async : false,
					success : function(tempData) {
						// 调用注入的格式化行为完成转换
						data = remote.format(tempData);
					}
				});
			} else {
				throw new Error('提供的数据不对');
			}
			// 注入模型
			this.setModel(data);
		},
		bindEvent : function() {
			// 收缩按钮
			$('.ui-ecp-multiselect .trigger').live('click', function() {
				// 改变按钮方向
				$(this).toggleClass('up');
				$(this).toggleClass('down');
				
				// 展示或者隐藏选择项
				if ($(this).hasClass('up')) {
					$(this).parents('.ui-ecp-multiselect').find('.ms-options').show();
				} else {
					$(this).parents('.ui-ecp-multiselect').find('.ms-options').hide();
				}
				return false;
			});
			
			// 列表项焦点提示
			$('.ui-ecp-multiselect .ms-options li').live('mouseenter', function() {
				$(this).addClass('focus');
				return false;
			});
			
			$('.ui-ecp-multiselect .ms-options li').live('mouseout', function() {
				$(this).removeClass('focus');
				return false;
			});
			
			
			// 鼠标点击列表项，减少列表项
			$('.ui-ecp-multiselect .ms-options li').live('click', function() {
//				$(this).addClass('focus');
//				// 从候选区删除
//				$(this).remove();
				// 隐藏当前列表项
				$(this).hide();
				// 在选择区增加
				addSelection(this);
				// 触发收缩按钮
				$(this).parents('.ui-ecp-multiselect').find('.trigger').trigger('click');
				return false;
			});
			
//			// 鼠标离开候选区域后，关闭候选区
//			$('.ms-options').live('mouseleave', function() {
//				$(this).parents('.ms-field').find('.selected-options .trigger').trigger('click');
//			});
			
			// 选择项鼠标聚焦时，出现删除按钮；
			// 鼠标离开，隐藏删除按钮；
			 $('.selected-options-holder .selected-option').live('mouseenter', function() {
				 $(this).find('.remove').removeClass('hidden').addClass('visible');
				 return false;
			 });
			 
			 $('.selected-options-holder .selected-option').live('mouseleave', function() {
				 $(this).find('.remove').removeClass('visible').addClass('hidden');
				 return false;
			 });
			 
			 // 点击删除按钮，删除选择区域的选择项，还原候选区域中的候选项
			 $('.ui-ecp-multiselect').find('.remove').live('click', function() {
				 removeSelection(this);
			 });
		},
		afterRender : function() {
			
		}
	});
	
	
	/**
	 * 增加选择项
	 */
	function addSelection(li) {
		var key = $(li).find('a').attr('data-attrs-key');
		var value = $(li).find('a').text();
//		<span class="selected-option" data-attrs-key="a">语文111<a href="#" class="remove">x</a></span>
		// dom结构
//		var selectedOption = $('<span></span>').addClass('selected-option').attr({
//			'data-attrs-key':key
//		}).text(value).append($('<a></a>').addClass('remove hidden').attr({
//			href:'#',
//			'data-attrs-key':key
//		}).text('x'));
		var selectedOption = handlebars.compile(selectionTemplate)({key:key,value:value});
		
		$('.selected-options-holder').append(selectedOption);
		
		
		// 隐藏域
		var input = $(li).parents('.ms-field').find('input');
		var oldKey = input.val();
		log.debug('add selection before oldKey = ' + oldKey);
		if (oldKey) {
			var array = oldKey.split(',');
			array.push(key);
			input.val(array.join(','));
		} else {
			input.val(key);
		}
		
		// 候选项没有时，展示提示条目
		if ($(li).parents('ul').find('li a:visible').length == 0) {
			$(li).parents('.ms-options').find('.no-option').show();
		}
		
		
		log.debug('add selection before newKey = ' + input.val());
		
	}
	
	/**
	 * 删除选择项
	 */
	function removeSelection(a) {
		
		// 如果候选项没有选项，恢复的第一个候选项，应该隐藏提示
		if ($(a).parents('.ms-field').find('.ms-options ul li:visible').length == 0) {
			$(a).parents('.ms-field').find('.no-option').hide();
		}
		
		 // 要删除的选择项的key
		 var targetKey = $(a).parents('.selected-option').attr('data-attrs-key');
		 log.debug('remove selection before targetKey = ' + targetKey);
		 // 还原候选项
		 $(a).parents('.ms-field').find('.ms-options li').each(function(i, item) {
			 var a = $(item).find('a');
			 var key = a.attr('data-attrs-key');
			 if (key == targetKey) {
				 $(item).show();
				 return false;
			 }
		 });
		 // 隐藏域
		 var input = $(a).parents('.ms-field').find('input');
		 var oldKey = input.val();
		 log.debug('remove selection before remove = ' + oldKey);
		 var array = oldKey.split(',');
		 $(array).each(function(i, item) {
			 if (item == targetKey) {
				 array.splice(i, 1);
				 return false;
			 }
		 });
		 input.val(array.join(','));
		 
		 log.debug('remove selection before newKey = ' + input.val());
		 
		 // 删除选择项
		 $(a).parents('.selected-option').remove();
	}
});