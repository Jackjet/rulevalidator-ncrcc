define(function(require, exports, module) {
	var $ = require('$');
	require('./resource.css');
	$ = require('jquery.ztree')($);

	/**
	 * 初始化
	 */
	function init(options) {
		$(function() {
			var containerId = options.containerId;
			var setting = {};
			$.ajax({
				url : GLOBAL.API.MODULE_CHILDREN,
				data : {isCascade:'true'},
				success : function(data) {
					var container = $('#' + containerId);
					container.addClass('ztree');
					var nodes = format(data, parseModule2TreeNode);
					$.fn.zTree.init(container, setting, nodes);
				}
			});
		});
	};
	
	/**
	 * 格式化公式
	 */
	function parseModule2TreeNode(module) {
		if (!module) {
			throw new Error('数据不能为空');
		}
		if ($.isArray(module)) {
			throw new Error('数据不能使数组');
		}
		return {
			id:module.moduleId,
			pId:module.parentId,
			open:true,
			name:module.moduleName
//			isHidden:module.isHidden,
		};
	}
	
	/**
	 * 格式化
	 */
	function format(data, parser) {
		if (!data) {
			throw new Error('数据不能为空');
		}
		
		if (!$.isArray(data)) {
			throw new Error('数据必须为数组');
		}
		
		if (!parser) {
			throw new Error('格式化公式不能为空');
		}
		
		if (!$.isFunction(parser)) {
			throw new Error('格式化公式必须为函数类型');
		}
		
		var nodes = [];
		$(data).each(function(i, item) {
			var node = parser(item);
			if (item.children && $.isArray(item.children) && (item.children.length > 0)) {
				// 还有孩子节点 => 递归调用
				node.children = format(item.children, parser);
			}
			nodes.push(node);
		});
		return nodes;
	}

//	/**
//	 * 视图转化为树形节点
//	 */
//	function parse(module) {
//		var moduleId = module.moduleId;
//		var moduleName = module.moduleName;
//		var parentId = module.parentId;
//		var seq = module.seq;
//		var depth = module.depth;
//		var securityLevel = module.securityLevel;
//		return {
//
//		};
//	}

	module.exports = {
		init : init
	};

});