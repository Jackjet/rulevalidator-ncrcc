/**
 * 收藏夹对话框（配置请参考defaultOptions.json）
 */
define(function(require, exports, module) {

	// jquery
	var $ = require('$');
	// 依赖dialog
	var Dialog = require('dialog');
	// 依赖handlerbar
	var handlebars = require('handlebars');
	// 模板
	var contentTemplate = require('./content.tpl');
	var successTemplate = require('./success.tpl');
	var invalidTemplate = require('./invalid.tpl');
	var failureTemplate = require('./failure.tpl');
	var store = require("store");
	var placeholder = require("placeholder");
	// 样式
	require('../theme/{theme}/css/favorite_dialog.css');

	function loadFavorite() {
		$.ajax({
			url : G.API.ACCOUNT_FAVORITE_QUERYALL,
			success : function(data) {
				if (data) {
					var map = new Object();
					for ( var i = 0; i < data.length; i++) {
						var productid = data[i].productId;
						map[productid] = productid;
					}
					store.set("favorite", map);
					seajs.emit(G.EVENT.FAVORITE_LOADED, map);
				}
			}
		});
	}

	/**
	 * 合并属性
	 */
	function mergeOptions(options) {
		// 加载默认设置
		var defaultOptions = {
			// 新增url
			insertUrl : G.API.ACCOUNT_FAVORITE_INSERT,
			// 我的收藏夹url
			queryUrl : G.PAGE.ACCOUNT_FAVORITE,
			// 获取我的所有收藏标签url
			myTagsUrl : G.API.ACCOUNT_FAVORITE_TAGS,
			// 最多标签数
			maxTagCount : 6,
			// 提交后的回调函数
			doConfirm : function() {
			}
		};
		// 合并配置
		options = options || {};
		for ( var attr in options) {
			defaultOptions[attr] = options[attr];
		}
		return defaultOptions;
	}

	/**
	 * 对于标签值交替某值
	 */
	function tagToggleValue(value) {
		var field = $('input[name=tag]');
		var oldValue = field.val();
		var newValue = '';
		if (oldValue.match(value)) {
			// 已经有此值 => 删除
			var oldValues = oldValue.split(',');
			for ( var i in oldValues) {
				var temp = oldValues[i];
				if (temp == value) {
					// 匹配
					oldValues.splice(i, 1);
					newValue = oldValues.join(',');
				}
			}
		} else {
			// 没有此值 => 增加
			if (oldValue) {
				var oldValues = oldValue.split(',');
				oldValues.push(value);
				newValue = oldValues.join(',');
			} else {
				newValue = value;
			}
		}
		field.val(newValue);
	}

	/**
	 * 获取已经选择的标签值
	 */
	function getSelectValue() {
		var values = [];
		$('.tag-candidate a.tag-selected').each(function(i, item) {
			values.push($.trim($(item).text()));
		});
		return values;
	}

	/**
	 * 交互
	 */
	function interactive() {
		
		// 候选标签
		$('.tag-candidate a').click(function() {
			// 1）把选择的值放到输入控件
			var text = $.trim($(this).text());
			tagToggleValue(text);
			// 2）增加一个已经选择的角标
			$(this).toggleClass('tag-selected');
			return false;
		});

		// 输入框
		$('.ui-ecp-favorite-tag input[ectype=textfield]').blur(function() {
			if (validate()) {
				var value = $.trim($(this).val());
				if (value == '多个标签请用逗号隔开') {
					value = '';
				}
				// 保存在隐藏域
				var selectValue = [];
				$('.tag-candidate a.tag-selected').each(function(i, item) {
					selectValue.push($.trim($(item).text()));
				});

				if (value) {
					if (selectValue.length > 0) {
						value += ',' + selectValue.join(',');
					}
				} else {
					if (selectValue.length > 0) {
						value = selectValue.join(',');
					}
				}
				
				$('input[name=tag]').val(value);
			}
		});
	}

	/**
	 * 校验
	 */
	function validate() {
		// <div>
		// <input ectype="textfield" fieldname="tag" placeholder="多个标签请用逗号隔开"
		// maxlength="30">
		// <input name="tag" value="2" type="hidden">
		// </div>
		var input = $('.ui-ecp-favorite-tag input[ectype=textfield]');
		var inputValue = $.trim(input.val());

		// [1] 全角逗号 => 半角逗号 空白字符串去掉
		if (inputValue) {
			inputValue = inputValue.replace('\s', '').replace('，', ',');
			input.val(inputValue);
		}

		// [2] 检查是否包含特殊字符
		var regexp = /[~!@#$%^&*'"//\\?]/;

		if (regexp.test(inputValue)) {
			alert('不能包含特殊字符，请重新输入');
			return false;
		}

		return true;
	}

	/**
	 * 提交数据
	 */
	function doSubmit(insertUrl, queryUrl, productId) {
		// <div>
		// <input ectype="textfield" fieldname="tag" placeholder="多个标签请用逗号隔开"
		// maxlength="30">
		// <input name="tag" value="2" type="hidden">
		// </div>
		if (validate()) {
			// var tag = $.trim($('input[name=tag]').val());
			// // 保证标签值不能是【多个标签请用逗号隔开】
			// if(tag == '多个标签请用逗号隔开'){
			// tag = '';
			// }
			$.ajax({
				type : "POST",
				url : insertUrl,
				dateType : "json",
				data : {
					cproductid : productId,
					vtag : $.trim($('input[name=tag]').val())
				},
				success : function(data) {
					if (data[0]['new']) {
						var templateData = {
							url : queryUrl
						};
						var html = handlebars.compile(successTemplate)(templateData);
						$('.ui-ecp-favorite-tip').html(html);
					} else {
						var templateData = {
							url : queryUrl
						};
						var html = handlebars.compile(failureTemplate)(templateData);
						$('.ui-ecp-favorite-tip').html(html);
					}
					// 保存成功后触发事件
					seajs.emit("event_favorite_add_success");
					if (!store.get("favorite")) {
						var favorite = {
								productId:productId	
						}
						store.set("favorite", favorite);
					} else {
						var favorite = store.get("favorite");
						favorite[productId] = productId;
					}
				}
			});
		}
	}

	function showDialog(options) {

		// 合并配置
		options = mergeOptions(options);

		// 从服务器上获取当前用户所有标签
		$.ajax({
			url : options.myTagsUrl,
			success : function(data) {
				if (data) {
					var tagMap = data;
					var tags = [];
					var counter = 0;
					for ( var tag in tagMap) {
						if (counter++ < options.maxTagCount) {
							tags.push(tag);
						}
					}

					// 生成内容html
					var contentHtml = handlebars.compile(contentTemplate)(tags);

					// 创建dialog
					Dialog.dialog({
						title : '请填写标签',
						hasCloseTip : true,
						content : contentHtml,
						isHtmlContent : true,
						type : 'dialog',
						buttons : [ {
							name : '保存',
							isDefault : true,
							href : '#',
							method : function() {
								// 提交请求
								if (validate()) {
									doSubmit(options.insertUrl, options.queryUrl, options.productId);
									if (options.doConfirm) {
										options.doConfirm();
									}
									return false;
								} else {
									return false;
								}
							}
						}, {
							name : '取消',
							isDefault : false,
							href : '#',
							method : function() {
								Dialog.close();
								if (options.doCancel) {
									options.doCancel();
								}
								return false;
							}
						} ]
					});

					// 增加交互处理
					interactive();
					placeholder.init();
				} else {
					throw new Error('没有获取到当前用户的标签信息');
				}
			}
		});
	}

	exports.init = showDialog;
	exports.loadFavorite = loadFavorite;

	// 增加事件
	seajs.on(G.EVENT.FAVORITE_ADD, function(productId, callback) {
		showDialog({
			productId : productId
		});
		if (callback) {
			callback();
		}
	});
});
