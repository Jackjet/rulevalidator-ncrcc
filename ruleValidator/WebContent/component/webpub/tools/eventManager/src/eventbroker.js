/**
 * 事件代理（用于注册事件、注销事件、增加事件监听和传递消息）
 */
// FIXME auto-render中增加声明式处理
//define(function(require, exports, module) {
//			var Base = require("base");
//			var EventBroker = Base.extend({
//						eventSplitter : /\s+/,
//						/**
//						 * 是否注册过事件
//						 */
//						isRegister : function(event) {
//
//							if (!event || typeof(event) != 'string') {
//								throw new Error('请输入正确的事件名称')
//							}
//
//							var __events = this.__events
//									|| (this.__events = {});
//
//							var isRegister = false;
//							for (var e in __events) {
//								if (e === event) {
//									isRegister = true;
//									break;
//								}
//							}
//							return isRegister;
//						},
//
//						/**
//						 * 列出所有事件
//						 */
//						listEvent : function() {
//							var __events = this.__events
//									|| (this.__events = {});
//							var events = [];
//							for (var event in __events) {
//								events.push(event);
//							}
//							return events;
//						}
//					})
//			var eventBroker = new EventBroker();
//			
//			module.exports =eventBroker;
//		});