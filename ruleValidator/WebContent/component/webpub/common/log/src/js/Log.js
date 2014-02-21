define(function(require, exports, module) {
	var $ = require("$");
	require("../theme/default/css/log.css")
	var ConsoleAppender = require("./ConsoleAppender");
			/**
			 * 日志组件
			 * 
			 * @param {}
			 *            options 配置
			 */
			function Logger(options) {

				/**
				 * 日志级别（全部启用）
				 * 
				 * @type Number
				 */
				this.LOG_LEVEL_ALL = 0;

				/**
				 * 日志级别（info）
				 * 
				 * @type Number
				 */
				this.LOG_LEVEL_INFO = 10;

				/**
				 * 日志级别（debug）
				 * 
				 * @type Number
				 */
				this.LOG_LEVEL_DEBUG = 20;

				/**
				 * 日志级别（warn）
				 * 
				 * @type Number
				 */
				this.LOG_LEVEL_WARN = 30;

				/**
				 * 日志级别（error）
				 * 
				 * @type Number
				 */
				this.LOG_LEVEL_ERROR = 40;

				/**
				 * 日志级别（全部关闭）
				 * 
				 * @type Number
				 */
				this.LOG_LEVEL_OFF = 50;

				/**
				 * 浏览器类型
				 * 
				 * @type
				 */
				this.brower;

				/**
				 * 浏览器版本
				 * 
				 * @type
				 */
				this.browerVersion;

				/**
				 * 日志级别
				 * 
				 * @type
				 */
				this.level = this.LOG_LEVEL_ALL;

				/**
				 * 输入对象
				 * 
				 * @type
				 */
				this.appender;

				/**
				 * 布局器
				 * 
				 * @type
				 */
				this.layout = {
					// 格式
					conversionPattern : ''
				};

				/**
				 * 输出
				 */
				this.append = function(args) {
					if (args.message) {
						// 使用错误提示信息
						this.appender.append(args.prefix + ':' + args.message);
					} else {
						// 使用Error的message
					}
				}

				/**
				 * 转化格式
				 * 
				 * @param {}
				 *            log 转化前的日志文本
				 * @return {String} 转化后的日志文本
				 */
				this.conver = function(message) {
					message += new Date().toString() + message;
					return message;
				}

				/**
				 * 输出日志（debug级别）
				 */
				this.debug = function() {
					if (this.LOG_LEVEL_DEBUG >= this.level) {
						// 转化参数
						var args = this.convertInput(arguments,
								this.LOG_LEVEL_DEBUG);
						// 打印
						this.append(args);
					}
				};

				/**
				 * 输出日志（info级别）
				 */
				this.info = function() {
					if (this.LOG_LEVEL_INFO >= this.level) {
						// 转化参数
						var args = this.convertInput(arguments,
								this.LOG_LEVEL_INFO);
						// 打印
						this.append(args);
					}
				};

				/**
				 * 输出日志（warn级别）
				 */
				this.warn = function() {
					if (this.LOG_LEVEL_WARN >= this.level) {
						// 转化参数
						var args = this.convertInput(arguments,
								this.LOG_LEVEL_WARN);
						// 打印
						this.append(args);
					}
				}

				/**
				 * 输出日志（error级别）
				 */
				this.error = function() {
					if (this.LOG_LEVEL_ERROR >= this.level) {
						// 转化参数
						var args = this.convertInput(arguments,
								this.LOG_LEVEL_ERROR);
						// 打印
						this.append(args);
					}
				};

				/**
				 * 转化级别
				 * 
				 * @description 由于javascript不支持静态变量，
				 *              不能使用Logger.LOG_LEVEL_DEBUG等，
				 *              所以调用者可以传递'debug'之类的字符串或者20这样的日志级别
				 * @param {}
				 *            lev 转化之前的级别
				 * @return {} 转化之后的级别
				 */
				this.convertLevel = function(lev) {
					var level;
					if (typeof(lev) == 'string') {
						// 字符型
						switch (lev) {
							case 'all' : {
								level = 0;
								break;
							}
							case 'info' : {
								level = 10;
								break;
							}
							case 'debug' : {
								level = 20;
								break;
							}
							case 'warn' : {
								level = 30;
								break;
							}
							case 'error' : {
								level = 40;
								break;
							}
							case 'off' : {
								level = 50;
								break;
							}
							default : {
								level = 0;
								break;
							}
						}
					} else if (typeof(lev) == 'number') {
						// 数字型
						level = lev;
					} else {
						throw new Error('未知的日志级别， level = ' + lev);
					}
					return level;
				}

				/**
				 * 反转换级别
				 * 
				 * @description 将日志级别转化为字符串
				 * @param {}
				 *            level 日志级别
				 */
				this.unconvertLevel = function(level) {
					var levelStr;
					switch (level) {
						case this.LOG_LEVEL_ALL : {
							levelStr = 'ALL';
							break;
						}
						case this.LOG_LEVEL_INFO : {
							levelStr = 'INFO';
							break;
						}
						case this.LOG_LEVEL_DEBUG : {
							levelStr = 'DEBUG';
							break;
						}
						case this.LOG_LEVEL_WARN : {
							levelStr = 'WARN';
							break;
						}
						case this.LOG_LEVEL_ERROR : {
							levelStr = 'ERROR';
							break;
						}
						default : {
							levelStr = 'ALL';
							break;
						}
					}
					return levelStr;
				}

				/**
				 * 转化入参
				 * 
				 * @param {}
				 *            args 参数列表
				 * @return {} 分解之后的参数
				 */
				this.convertInput = function(args, currentLevel) {
					// 错误提示信息
					var message;
					// Exception
					var error;
					if (args.length > 0) {
						if (args.length == 1) {
							message = args[0];
						} else if (args.length == 2) {
							message = args[0];
							error = args[1];
						} else {
							throw new Error('入参数怎么会有3个，哥们搞错了吧！')
						}
					} else {
						throw new Error('请输入日志信息')
					}
					// 日期
					var date = new Date().toString();
					// 日志级别
					var levelStr = this.unconvertLevel(currentLevel);
					// 整个前缀
					var prefix = date + ' ' + levelStr;
					// 返回值
					var returnValue = {
						prefix : prefix,
						message : message,
						error : error
					}
					return returnValue;
				}

				/**
				 * 初始化
				 */
				this.init = function(options) {
					// console.log(options.level)
					if (options && options.level) {
						$.extend(options, {
									level : this.convertLevel(options.level)
								})
					}
					$.extend(this, options);
					// 创建appender
					this.appender = new ConsoleAppender();
				}

				/**
				 * 设置日志级别
				 * 
				 * @description 用于独立模块单独设置日志级别
				 * @param {}
				 *            level
				 */
				this.setLevel = function(level) {
					this.level = this.convertLevel(level);
				}

				/**
				 * info日志级别是否可用
				 * 
				 * @return {Boolean}
				 */
				this.isInfoEnable = function() {
					if (this.LOG_LEVEL_INFO >= this.level) {
						return true;
					} else {
						return false;
					}
				}

				/**
				 * debug日志级别是否可用
				 * 
				 * @return {Boolean}
				 */
				this.isDebugEnable = function() {
					if (this.LOG_LEVEL_DEBUG >= this.level) {
						return true;
					} else {
						return false;
					}
				}

				/**
				 * warn日志级别是否可用
				 * 
				 * @return {Boolean}
				 */
				this.isWarnEnable = function() {
					if (this.LOG_LEVEL_WARN >= this.level) {
						return true;
					} else {
						return false;
					}
				}

				/**
				 * error日志级别是否可用
				 * 
				 * @return {Boolean}
				 */
				this.isErrorEnable = function() {
					if (this.LOG_LEVEL_ERROR >= this.level) {
						return true;
					} else {
						return false;
					}
				}
				/**
				 * 显示日志区域
				 */
				this.show = function(){
					this.appender.show();
				}
				this.init(options);
			}
			var Log = new Logger();
			module.exports= Log;
		});