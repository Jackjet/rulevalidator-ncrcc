define(function(require, exports, module) {

	var $ = require("$");
	$ = require("jquery.extend")($);
	
	require('../theme/default/css/updatePassword.css');
	var Dialog=require("dialog");
	/**
	 * 常量
	 */
	var constant = {
		id : {
			accountDetail : 'account_accountDetail',
			updatePassword : 'account_updatePassword',
			credit : 'account_credit'
		},
		color : {
			balancemny : 'menu', // 颜色（信用余额）
			engrossmny : 'inactivecaption', // 颜色（信用占用）
			limitmny : '' // 颜色（信用额度）
		},
		url : {
			updatePassword : G.API.USER_UPDATEPWD // 修改密码
		}
	};
		/**
	 * 加载更新密码页面
	 */
	function loadUpdatePasswordPage() {
		/*
		 * 模板
		 */
		function HtmlTemplate(data) {
			this.html = '';
			/**
			 * 创建html文档
			 */
			this.createHtml = function() {
				this.html += '<form class="account_info_detail" style="">'
						+ '<div class="address_info">'
						+ '<div class="w_100ps nc-field-pw-margin">'
						+ '<label for="receiptDate1">&nbsp;</label>'
						+ '<input type="checkbox" checked="checked" id="isShowPassword">隐藏字符</input>'
						+ '</div>'
						+ '<div class="w_100ps nc-field-pw-margin">'
						+ '<label for="receiptDate1" class="floatleft">旧密码：</label>'
						+ '<input class="textfiled floatleft nc-field-pw-length" type="password" id="oldPassword"></input>'
						+ '</div>'
						+ '<div class="w_100ps nc-field-pw-margin">'
						+ '<label for="receiptDate1" class="floatleft">新密码：</label>'
						+ '<input class="textfiled floatleft nc-field-pw-length" type="password" id="newPassword"></input>'
						+ '</div>'
						+ '<div class="w_100ps float_left nc-field-pw-margin">'
						+ '<label for="receiptDate1" class="floatleft">确认新密码：</label>'
						+ '<input class="textfiled floatleft nc-field-pw-length" type="password" id="repeatNewPassword"></input>'
						+ '</div>'
						+ '<div class="w_100ps">'
						+ '<label for="receiptDate1" class="floatleft">&nbsp;</label>'
						+ '<a id="updatePasswordBotton" class="btn_normal_gray" href="#">更新</a>'
						+ '</div>' + '</div>' + '</form>';
				return this.html;
			};
		}

		/*
		 * 修改navigator
		 */
		var navigatorHtml = '&gt; <span>修改密码</span>';
		$('span#navigator').html(navigatorHtml);

		var html = new HtmlTemplate(null).createHtml();
		var targetDiv = $('div.center_content');

		// 删除内容
		targetDiv.html('');
		// 绘制新页面
		targetDiv.html(html);

		// 校验
		$('form.account_info_detail input.textfiled').bind('blur', function(e) {
			pwValidate(e.target);
		});
		// 根据复选框的勾选，改变三个文本域的类型为textfield或者password
		$('#isShowPassword').bind('click', function() {
			var checkbox = $('#isShowPassword')[0];
			var textfileds = $('form.account_info_detail input.textfiled');
			if (checkbox.checked) {
				// 隐藏字符（默认）
				$.each(textfileds, function(i, node) {
					node.setAttribute('type', 'password');
				});
			} else {
				// 显示字符 => 改变input标签的类型为textfield
				$.each(textfileds, function(i, node) {
					node.setAttribute('type', 'textfield');
				});
			}
		});

		/*
		 * 对更新操作绑定函数 点击更新按钮，发送ajax请求 将更改结果会写到提示域中
		 */
		$('#updatePasswordBotton').bind('click', function() {

			// 需要前台检验的域
			var fields = $('.account_info_detail .address_info input.textfiled');
			var isValidate = true;
			fields.each(function(index, field) {
				isValidate = isValidate && pwValidate(field);
			});

			if (!isValidate) {
				return;
			}

			$.ajax({
				type : "POST",
				url : constant.url.updatePassword,
				data : {
					oldPassword : $('#oldPassword').val(),
					newPassword : $('#newPassword').val()
				},
				dataType : 'json',
				success : function(data) {
					if (data.isSuccess) {
						$('#oldPassword')[0].value = '';
						$('#newPassword')[0].value = '';
						$('#repeatNewPassword')[0].value = '';
					}
					$('form.account_info_detail')
							.find('.nc-field-error, .et-error-message, .nc-field-right')
							.remove();
					Dialog.alert({
						title : '提示',
						content : '<br><center>' + data.message+ '</center>',
						isHtmlContent : true
					});
				},
				error : function() {
					$('form.account_info_detail')
							.find('.nc-field-error, .et-error-message, .nc-field-right')
							.remove();
					var errorMessage = '提交请求错误';
					throw new Error(errorMessage);
				}
			});
		});
	}
	
		/**
	 * 校验密码
	 */
	function pwValidate(targetField) {
		targetField = $(targetField);
		// 结果
		var result = {
			targetSelector : null,
			// 默认是错误的
			isValidate : false,
			tip : null
		};
		// 根据目标域的id作分支
		switch (targetField.attr('id')) {
			// 旧密码
			case 'oldPassword' : {
				result.targetSelector = ('#oldPassword');
				if (!targetField.val()) {
					result.tip = '<span class="nc-field-error"></span><span class="et-error-message" style="color:red;">旧密码不能为空</span>';
				} else {
					result.isValidate = true;
				}
				break;
			}
				// 新密码
			case 'newPassword' : {
				result.targetSelector = '#newPassword';
				if (!targetField.val()) {
					result.tip = '<span class="nc-field-error"></span><span class="et-error-message" style="color:red;">新密码不能为空</span>';
				} else if ($('#oldPassword').val()
						&& $('#oldPassword').val() == targetField.val()) {
					result.tip = '<span class="nc-field-error"></span><span class="et-error-message" style="color:red;">新密码不能和旧密码相同</span>';
				} else {
					result.isValidate = true;
				}
				break;
			}
				// 重复新密码
			case 'repeatNewPassword' : {
				result.targetSelector = '#repeatNewPassword';
				if (!targetField.val()) {
					result.tip = '<span class="nc-field-error"></span><span class="et-error-message" style="color:red;">请再填写一次新密码</span>';
				} else if ($('#newPassword').val()
						&& $('#newPassword').val() != targetField.val()) {
					result.tip = '<span class="nc-field-error"></span><span class="et-error-message" style="color:red;">两次填写的新密码不相同</span>';
				} else {
					result.isValidate = true;
				}
				break;
			}
		}
		if (result) {
			// 删除已有标记
			$(result.targetSelector)
					.parent()
					.find('.nc-field-error, .et-error-message, .nc-field-right')
					.remove();
			// 正确的检验项，统一使用一个标志
			if (result.isValidate) {
				result.tip = '<span class="nc-field-right"></span>';
			}
			$(result.targetSelector).after(result.tip);
		}
		return result.isValidate;
	}
	$(function() {
		loadUpdatePasswordPage();
	})
});