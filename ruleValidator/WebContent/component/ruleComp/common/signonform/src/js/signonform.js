/*
 * 组件示例
 */

define(function(require, exports, module) {
	require("button");
	require('../theme/default/css/signonform.css');
	var $ = require("$");
	$ = require("jquery.cookie")($);
	$ = require("jquery.json2")($);
	var logger = require("log");
	// 默认引入：组件继承自widget
	var Widget = require("widget");
	// 自定义：组件的html模板
	var template = require("./signonform.tpl");
	var dialog = require("dialog");
	var localStore = require("store");

	// 组件的定义 组件名大写
	var Banner = Widget.extend({
		template : template,
		bindEvent : function() {
			var banner = this;
			/**
			 * 常量
			 * 
			 * @type
			 */
			var constant = {
				key : {
					isRememberMe : 'is_remember_me',
					authentication : 'authentication'
				},
				maxPwLength : 8,
				url : {
					validateCode : G.API.RANDOMID,
					login : G.API.LOGIN,
					homePage : G.ctx,
					main : G.main
				},
				expires : new Date(new Date().getTime() + 30 * 24 * 60 * 60 * 1000)
			};

			$(document).ready(function() {
				// 设置样式
				setStyle();
				// 设置行为
				setAction();
			});

			/**
			 * 设置行为
			 */
			function setAction() {

				// 设置验证码
				setValidateCode();

				// 绑定验证码
				$('img#codefont').bind('click', setValidateCode);

				// 绑定登录
				$('a.signon_button').bind('click', login);

				// 输入焦点定位到登录
				$('input#username').focus();

				// 记住我复选框
				$('#rememberpwd').bind('click', rememberMe);

				// 忘记密码事件
				$('.signon_forget_pwd').bind('click', forgetPwd);

				// 自动填充密码
				$('input#username').bind('blur', autoCompletePwd);

				// 密码区失去焦点后，真假密码赋值
				$('input#password').bind('blur', function() {
					var realPassword = $('input#password2').val();
					var mockPassword = $('input#password').val();
					if (realPassword && mockPassword && mockPassword.length >= 8 && realPassword.substr(0, constant.maxPwLength) == mockPassword) {
						// 假密码是由真密码计算出来的 => 读取的是cookie中保存的认证密码 => 用户点击过密码文本域但未修改
						// do nothing
					} else {
						// 用户修改过密码 => 将假密码复制给真密码
						$('input#password2').val(mockPassword);
					}
				});

				// 绑定热键
				bindKeys();

				// 填充认证信息
				setAnthen();
			}

			/**
			 * 从cookie中获取最近登录的用户信息，填充到登录框中
			 */
			function setAnthen() {
				logger.info('从cookie中读取认证信息');
				// 从cookies中读取用户名、密码和是否记住密码设置
				var anthenArray = $.cookie(constant.key.authentication);
				if (anthenArray) {
					anthenArray = $.evalJSON(anthenArray);
				} else {
					anthenArray = [];
				}

				$("#username").focus();
				/*
				 * cookie中的认证信息数组按照访问顺序排列，越往后访问日期越近 页面初始化时，取出访问日期最近的一个认证
				 */
				var anthen = getLastAnthen(anthenArray);
				if (anthen != null) {
					logger.info('从cookie中读取的最近登录的用户信息为：' + anthen);
					// 将缓存重现
					$('#username').attr({
						value : anthen.userName
					}).removeClass('username');
					if (anthen != null && anthen.password) {
						$('#password').attr({
							value : anthen.password.substr(0, constant.maxPwLength)
						}).removeClass('password');
						$('#password2').attr({
							value : anthen.password
						}).removeClass('password');
						$('#rememberpwd').attr({
							checked : true
						});
					}

				}
			}

			/**
			 * 获取最近的一个认证
			 * 
			 * @param {}
			 *            anthenArray 认证信息数组
			 * @return {} 最新的一个认证信息
			 */
			function getLastAnthen(anthenArray) {
				if (!anthenArray) {
					throw new Error('认证信息数组为空，请检查调用函数，需要保证实参不能为空');
				}
				var lastAnthen = null;
				for ( var i = 0; i < anthenArray.length; i++) {
					var anthen = anthenArray[i];
					if (!lastAnthen) {
						lastAnthen = anthen;
					} else {
						if (lastAnthen.timestamp && anthen.timestamp && lastAnthen.timestamp < anthen.timestamp) {
							// 当前认证信息的时间戳比最新的大 => 当前认证信息更新
							lastAnthen = anthen;
						}
					}
				}
				return lastAnthen;
			}

			/**
			 * 获取对应用户的认证信息
			 * 
			 * @param {}
			 *            anthenArray 认证信息数组
			 * @param {}
			 *            userName 用户名
			 */
			function getAnthen(anthenArray, userName) {
				if (!anthenArray) {
					throw new Error('认证信息数组为空，请检查调用函数，需要保证实参不能为空');
				}

				if (!userName) {
					throw new Error('用户名，请检查调用函数，需要保证实参不能为空');
				}
				var target = null;
				for ( var i = 0; i < anthenArray.length; i++) {
					var anthen = anthenArray[i];
					if (anthen && anthen.userName && anthen.userName == userName) {
						// 命中
						target = anthen;
						break;
					}
				}
				return target;
			}

			/**
			 * 自动填充密码
			 */
			function autoCompletePwd() {
				// 从cookie中读取认证信息
				var anthenArray = $.cookie(constant.key.authentication);
				if (anthenArray) {
					anthenArray = $.evalJSON(anthenArray);
				} else {
					anthenArray = [];
				}

				var userName = $('input#username').val();
				if (anthenArray && userName) {
					// 前置条件：cookie中缓存着用户认证信息 & 当前登录框中已经填写了用户名
					var anthen = getAnthen(anthenArray, userName);
					if (anthen != null && anthen) {
						$('input#password').val(anthen.password.substr(0, constant.maxPwLength));
						$('input#password2').val(anthen.password);
					} else {
						$('input#password').val('');
						$('input#password2').val('');
					}
				} else {
					$('input#password').val('');
					$('input#password2').val('');
				}
			}

			/**
			 * 绑定热键
			 */
			function bindKeys() {
				$('input').bind('keydown', function(event) {
					if (event.keyCode == 13) {
						if (event.target.id == "username") {
							$("#password").focus();
						} else if (event.target.id == "password") {
							$('#validate').focus();
						} else if (event.target.id == "validate") {
							$('.signon_button').click();
						}
					}
				});
			}

			/**
			 * 填充验证码
			 */
			function setValidateCode() {
				$('img#codefont').attr('src', constant.url.validateCode + '?num=' + new Date().getTime());
			}

			/**
			 * 校验
			 * 
			 * @return {Boolean}
			 */
			function validate() {

				$('.signon_error_notice span').addClass('hidden');

				// 用户名
				if ($.trim($('#username').val()).length == 0) {
					$('#username').focus().removeClass('username');
					$('#username').parent('label').removeClass('focus').addClass('error');
					$('.signon_error_notice span').removeClass('hidden');
					$('#signonError').html('用户名不能为空，请输入用户名！');
					$('#username').focus();
					return false;
				}

				// 密码
				if ($.trim($('#password').val()).length == 0) {
					$('#password').focus().removeClass('password');
					$('#password').parent('label').removeClass('focus').addClass('error');
					$('.signon_error_notice span').removeClass('hidden');
					$('#signonError').html('密码不能为空，请输入密码！');
					$('#password').focus();
					return false;
				}

				return true;
			}

			/**
			 * 将认证信息保存到cookie中
			 */
			function store(username, password) {

				// 入参检验
				if (!username) {
					var errorMessage = '入参用户名不能为空';
					logger.error(errorMessage);
					throw new Error(errorMessage);
				}

				if (!password) {
					password = '';
				}
				// cookie中保存用户名和密码
				var anthens = $.cookie(constant.key.authentication);
				if (anthens) {
					anthens = $.evalJSON(anthens);
				} else {
					anthens = [];
				}
				for ( var i = 0; i < anthens.length; i++) {
					var anthen = anthens[i];
					if (username && anthen.userName && username == anthen.userName) {
						// 相同认证 => 删除原来的认证
						anthens.splice(i, 1);
					}
				}

				// 认证信息数组尾部增加最新的认证
				anthens.push({
					userName : username,
					password : password,
					timestamp : new Date().getTime()
				});

				$.cookie(constant.key.authentication, $.toJSON(anthens), {
					path : '/',
					expires : constant.expires
				});
			}

			/**
			 * 记住我
			 */
			function rememberMe() {

				// 从cookies中读取用户名、密码和是否记住密码设置
				var anthenArray = $.cookie(constant.key.authentication);
				if (anthenArray) {
					anthenArray = $.evalJSON(anthenArray);
				} else {
					anthenArray = [];
				}

				var isRememberMe = $('#rememberpwd')[0].checked;
				if (!isRememberMe && anthenArray) {
					// 取消记住我 => 请求用户确认 => 删除cookie中对应用户的认证信息
					var userName = $('#username').val();
					if (confirm('确定取消当前用户[' + userName + ']的记住密码功能吗？')) {
						for ( var i = 0; i < anthenArray.length; i++) {
							var anthen = anthenArray[i];
							if (userName && anthen.userName && userName == anthen.userName) {
								// cookie中有该用户的认证信息 => 删除该用户的认证信息
								anthenArray.splice(i, 1);
								$.cookie(constant.key.authentication, $.toJSON(anthenArray), {
									path : '/',
									expires : constant.expires
								});
							}
						}
					}
				}
			}

			/**
			 * 登录
			 */
			function login() {
				if (validate()) {
					// 按钮动画
					$('.signon_button').hide();
					$('.signon_btn_animated').show();
					//提交前先删除相关的store信息防止未正常注销的用户在store中遗留token信息
					localStore.clear();
					// 提交请求
					$.ajax({
						type : 'POST',
						url : constant.url.login,
						data : {
							userName : $('#username').val(),
							pwd : $('#password2').val(),
							verificationCode : $('#validate').val(),
							login_type : $("#login_type")[0].checked ? "enforce" : ""
						},
						success : function(data) {
							if (data.ciphertext) {
								// 登录成功
								logger.info('login success');
								var isRememberMe = $('#rememberpwd').attr('checked');
								if (isRememberMe) {
									store($('#username').val(), data.ciphertext);
								} else {
									store($('#username').val(), '');
								}
								localStore.clear();
								localStore.set("user", data.usercontext);
								//管理页面
								if(data.usertype){
									window.location.href = GLOBAL.PAGE.MANAGE;
								}
								else{
									// 登录验证通过，跳转到主页
									if (banner.get("success")) {
										banner.get("success")(data.usercontext);
									} else if (data.usercontext.user_type == 0) {
										window.location = G.PAGE.CONSOLE;
									} else {
										window.location = constant.url.main;
									}
								}
								
							} else if (data.isOnline) {
								// 用户已经在线
								var dialog = require("dialog");
								var option = {
									title : "确认",
									content : "用户已经在线，是否强制登录？",
									confirm : function() {
										$("#login_type")[0].checked = true;
										login();
									},
									cancel : function() {
										$('.signon_error_notice span').removeClass('hidden');
										$('#signonError').html('用户已经在线');
										$('.signon_btn_animated').hide();
										$('.signon_button').show();
									}
								};
								dialog.confirm(option);
							} else if (data.message) {
								// 异常信息
								$('.signon_error_notice span').removeClass('hidden');
								$('#signonError').html(data.message);
								$('.signon_btn_animated').hide();
								$('.signon_button').show();
							}
						}
					});
				}
				return false;
			}

			/**
			 * 设置样式
			 */
			function setStyle() {

				$('#username').bind('focus', function() {
					$(this).parent('label').addClass('focus');
				}).bind('blur', function() {
					if ($(this).val() == '') {
						$(this).addClass('username');
					} else {
						$(this).removeClass('username');
					}
					$(this).parent('label').removeClass('focus').removeClass('error');
				}).bind('keydown', function() {
					$(this).removeClass('username');
				});

				$('#password').bind('focus', function() {
					$(this).parent('label').addClass('focus');
				}).bind('blur', function() {
					if ($(this).val() == '') {
						$(this).addClass('password');
					} else {
						$(this).removeClass('password');
					}
					$(this).parent('label').removeClass('focus').removeClass('error');
				}).bind('keydown', function() {
					$(this).removeClass('password');
				});

				$('#validate').bind('focus', function() {
					$(this).parent('label').addClass('focus');
					$('.idValidateNoticeBubble').removeClass('hidden');
				}).bind('blur', function() {
					if ($(this).val() == '') {
						$(this).addClass('validate');
					} else {
						$(this).removeClass('validate');
					}
					$(this).parent('label').removeClass('focus').removeClass('error');
					$('.idValidateNoticeBubble').addClass('hidden');
				}).bind('keydown', function() {
					$(this).removeClass('validate');
				});

				// 对于表单自动填充功能，初始化保存了上次的值，页面加载之后进行判断
				// 如果自动填充了用户名、密码和验证码，则这些表单控件的提示应该移除
				if ($('#username').val()) {
					$(this).removeClass('username');
				}

				if ($('#password').val()) {
					$(this).removeClass('password');
				}

				if ($('#validate').val()) {
					$('#validate').val('');
					$(this).removeClass('validate');
				}
			}

			/**
			 * 忘记密码
			 */
			function forgetPwd() {
				var username = $('#username').val();
				if (username == null || username == '') {
					$('.signon_error_notice span').removeClass('hidden');
					$('#signonError').html('用户名不可为空，请输入用户名');
				} else {
					dialog.confirm({
						title : "提示",
						content : "确认提交并重置密码？",
						confirm : function() {
							$.ajax({
								type : 'POST',
								url : G.API.FORGETPWD,
								data : {
									user_name : username
								},
								success : function(data) {
									$("#signonError").text(data.msgFlag).show();
								}
							});
							dialog.close();
						},
						cancel : function() {
							return true;
						}
					});
					
				}
			}

		}
	});
	// 组件对外提供使用
	module.exports = Banner;
});