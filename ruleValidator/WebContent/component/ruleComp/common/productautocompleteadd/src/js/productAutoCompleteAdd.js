
/**
 * 商品查询自动补全
 * 
 */
define(function(require, exports, module) {
	//主题
	require('../theme/default/css/productautocompleteadd.css');
	var $ = require("$");
	$ = require("jquery.autocomplete")($);
	$ = require('jquery.jtemplates')($);
	var autoComplete = require('productAutoComplete');
	var placeholder = require('placeholder');
	var COMPONENT_NAME = "ProductAutoCompleteAdd";
	var Validator = require("validator");
	/**
	 * 初始化
	 */
	exports.init = function(option) {
		// 重绘输入域
		reWriteHtml(option);
		// 绑定事件
		$(".ET_btnAdd").bind("click", function(event) {
				var row = $(".ET_btnAdd").data("row");
				row.count = $(".ET_addInputAmount").val() || 1;
				if(row){
					option.callback(row);
				}
		});
		// 绑定事件
		$(".ET_addInputAmount").bind("blur", function() {
					Validator.validatorData(this);
				});
		// 初始化查询
		autoComplete.init({
			className : "ET_addInputName",
			callback : function(row) {
				$(".ET_btnAdd").data("row", row);
			}
		});
		// placeholder 设置
		placeholder.init($);
		// 绑定热键
		bindKeys();
	};
	/**
	 * 绑定热键
	 */
	var bindKeys = function() {
		$('.ET_addInputName:first').focus();
		$('body [ectype] input, body [ectype] a').bind('keydown',
			function(event) {
				var target = $(this);
				var parent = $(this).parent();
				var keyCode = event.keyCode;
				if (keyCode == 13) {
					if (target.hasClass("ET_addInputName")) {
						parent.find(".ET_addInputAmount").focus();
					} else if (target.hasClass("ET_addInputAmount")) {
						parent.find(".ET_btnAdd").focus();
					} else {
	
					};
				};
		});
	};
	/**
	 * 重写html
	 */
	var reWriteHtml = function(option) {
		$("body [ectype]").each(function() {
			if ($(this).attr("ectype") == COMPONENT_NAME) {
				var autoInput = '<input tabindex="1" class="textfiled add_input_name ET_addInputName" placeholder="输入商品名称/编码">';
				var numberInput = '<input tabindex="2" class="textfiled add_input_amount ET_addInputAmount" value="1">';
				var addBtn = '<a tabindex="3" class="btn_add ET_btnAdd" href="#"></a><div class="clear"></div>';
				var template = autoInput + numberInput + addBtn;
				$(this).setTemplate(template).processTemplate(option);
			}
		});
	};
});