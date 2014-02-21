define(function(require, exports, module) {
	var $ = require("$");
			/**
			 * 日志输出
			 * 
			 * @description 日志的一种输出方式，
			 */
			function ConsoleAppender() {
				/**
				 * 容器id
				 * 
				 * @type String
				 */
				this.containerId = 'logContainer';

				/**
				 * 换行符
				 * 
				 * @type String
				 */
				this.wraper = '<br>';

				/**
				 * 样式class
				 * 
				 * @type String
				 */
				this.styleClass = 'nc-log-console';

				$(document).ready(function() {

							/*
							 * 创建console
							 */
							

							$(document).bind('keyup', function(e) {
										// console.log('into ConsoleAppender
										// keyup');
										var event = window.event || e;
										var code = event.keyCode || event.which;
										var keyCode_F11 = 122;
										if (event.altKey && code == keyCode_F11) {
											if ($('#logContainer')
													.is(':hidden')) {
												$('#logContainer').show();
											} else {
												$('#logContainer').hide();
											}
										}
									})
						})

				/**
				 * 消息
				 * 
				 * @type
				 */
				var messageQueue = [];

				/**
				 * 换行
				 */
				this.wrapLine = function() {
					if($('#' + this.containerId).length ==0){
						$('body').append($('<div></div>').attr({
										id : 'logContainer',
										'class' : 'nc-log-console'
									}))
					}
					$('#' + this.containerId).append(this.wraper);
				}

				/**
				 * 附加
				 */
				this.append = function(message) {
					messageQueue.push(message);
					if($('#' + this.containerId).length ==0){
						$('body').append($('<div></div>').attr({
										id : 'logContainer',
										'class' : 'nc-log-console'
									}))
					}
					$('#' + this.containerId).append(message);
					this.wrapLine();
				}

				/**
				 * 展示
				 */
				this.show = function() {
					$('#' + this.containerId).show();
				}

				/**
				 * 隐藏
				 */
				this.hide = function() {
					$('#' + this.containerId).hide();
				}
			}
			module.exports = ConsoleAppender;
		});