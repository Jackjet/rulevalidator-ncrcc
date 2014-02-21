define(function(require, exports, module) {
	var $ = require('$');
//	require('./css/resource.css');
	require('./css/module.css');
	$ = require('jquery.ztree')($);
	$ = require("jquery.custompara")($);
	var Dialog = require("dialog");
	var handlebars = require('handlebars');
	// var StringUtils = require('stringUtils');
	var Notice = require('notice');
	var SecuritySelect = require('security_select');
	var ROOT_ID = 'root';
	var tree = null;

	function validateModuel() {
		return true;
	}

	/**
	 * 新增
	 */
	function addModule(treeId, treeNode) {
		var parentId = treeNode.id;
		var record = {
			parentId : parentId,
			parentCode : treeNode.data.moduleCode == ROOT_ID ? '' : treeNode.data.moduleCode + '#',
			collection_securityLevel : {
				name : 'securityLevel',
				collection : SecuritySelect.COLLECTION_SECURITY_LEVEL,
				selected : '0'
			}
		};
		var template = require('./template/module_add.tpl');
		var contentHtml = handlebars.compile(template)(record);
		Dialog.dialog({
			title : '新增模块',
			hasCloseTip : true,
			content : contentHtml,
			isHtmlContent : true,
			moveToInner : true,
			type : 'dialog',
			buttons : [ {
				name : '保存',
				isDefault : true,
				href : '#',
				method : function() {
					// 提交请求
					if (validateModuel()) {
						$.ajax({
							type : 'POST',
							url : G.API.MODULE_INSERT,
							dateType : 'json',
							data : $('#form-module-add').serialize(),
							success : function(data) {
								if (data.moduleId && data.moduleName) {
									// seajs.emit(G.EVENT.RESOURCE_QUERY);
									Notice.show($('body'), '添加成功');
									Dialog.close();
									$.fn.zTree.getZTreeObj(treeId);
									tree.addNodes(treeNode, {
										id : data.moduleId,
										pId : data.parentId,
										name : data.moduleName,
										data : data
									});
								} else {
									if (data.detail && data.message) {
										Notice.show($('body'), '<span style="color:red;">' + data.message + '</span>');
										Dialog.close();
									}
								}
							}
						});
					}
					return false;
				}
			}, {
				name : '取消',
				isDefault : false,
				href : '#',
				method : function() {
					Dialog.close();
					return false;
				}
			} ]
		});
	}

	function removeModule(treeId, treeNode) {
		if (treeNode.id == ROOT_ID) {
			Dialog.alert({
				title : '提示框',
				content : '<center>不能删除根节点！</center>',
				isHtmlContent : true,
			});

			return false;
		}

		Dialog.confirm({
			title : '确认对话框',
			content : '<center>确定要删除[' + treeNode.name + ']模块吗？</center>',
			isHtmlContent : true,
			moveToInner : true,
			confirm : function() {
				$.ajax({
					type : 'POST',
					url : G.API.MODULE_DELETE,
					dateType : 'json',
					data : {
						pks : treeNode.id
					},
					success : function(data) {
						if (data.detail && data.message) {
							Notice.show($('body'), '<span style="color:red;">' + data.message + '</span>');
							Dialog.close();
						} else {
							var tree = $.fn.zTree.getZTreeObj(treeId);
							tree.removeNode(treeNode, false);
							Dialog.close();
							Notice.show($('body'), '删除成功');
							return false;
						}
					}
				});
				return false;
			},
			cancel : function() {
				return false;
			}
		});

		return false;
	}

	function updateModule(treeId, treeNode) {
		if (treeNode.id == ROOT_ID) {
			Dialog.alert({
				title : '提示框',
				content : '<center>不能修改根节点！</center>',
				isHtmlContent : true,
			});
			return false;
		}
		var record = treeNode.data;
		record.collection_securityLevel = {
			name : 'securityLevel',
			collection : SecuritySelect.COLLECTION_SECURITY_LEVEL,
			selected : record.securityLevel
		};

		var template = require('./template/module_update.tpl');
		var contentHtml = handlebars.compile(template)(record);
		Dialog.dialog({
			title : '修改模块',
			hasCloseTip : true,
			content : contentHtml,
			isHtmlContent : true,
			type : 'dialog',
			moveToInner : true,
			buttons : [ {
				name : '保存',
				isDefault : true,
				href : '#',
				method : function() {
					if (validateModuel()) {
						$.ajax({
							type : 'POST',
							url : G.API.MODULE_UPDATE,
							dateType : 'json',
							data : $('#form-module-update').serialize(),
							success : function(data) {
								if (data.moduleId && data.moduleName) {
									Notice.show($('body'), '修改成功');
									var tree = $.fn.zTree.getZTreeObj(treeId);
									// var newTreeNode =
									// parseModule2TreeNode(data);
									treeNode.name = data.moduleName;
									$.extend(treeNode.data, data);
									tree.updateNode(treeNode);
									Dialog.close();
								}
								if (data.message && data.detail) {
									Notice.show($('#module-grid'), '<span style="color:red;">保存失败</span>');
									Dialog.close();
								}
							}
						});
					}
					return false;
				}
			}, {
				name : '取消',
				isDefault : false,
				href : '#',
				method : function() {
					Dialog.close();
					return false;
				}
			} ]
		});
		return false;
	}

	/**
	 * 校验模块
	 */
	function validateModule() {
		return true;
	}

	/**
	 * 获取路径
	 */
	function getPath(treeNode, path) {
		if (!path) {
			path = [];
		}
		if (treeNode) {
			path.push(treeNode.data.moduleId);
		}
		var parentNode = treeNode.getParentNode();
		if (parentNode) {
			getPath(parentNode, path);
		}
		return path.reverse();
	}
	
	/**
	 * 获取所有子孙节点属性
	 */
	function getDescendantAttributes(treeNode, attributeGetter, attributes) {
		if (!attributes) {
			attributes = [];
		}
		
		if (!attributeGetter) {
			attributeGetter = function(treeNode) {
				return treeNode;
			};
		}
		
		if (!treeNode) {
			return attributes;
		} else {
			attributes.push(attributeGetter(treeNode));
		}
		var children = treeNode.children;
		if (children) {
			for (var i = 0; i < children.length; i++) {
				var child = children[i];
				getDescendantAttributes(child, attributeGetter, attributes);
			}
		}
		return attributes;
	}
	

	function addHoverDom(treeId, treeNode) {
		var sObj = $("#" + treeNode.tId + "_span");
		if (treeNode.editNameFlag || $("#button_add_" + treeNode.id).length > 0) {
			return;
		}
		var addStr = "<span class='button ui-icon-add' id='button_add_" + treeNode.id + "' title='增加子模块'></span>";
		sObj.after(addStr);
		$("#button_add_" + treeNode.id).click(function() {
			addModule(treeId, treeNode);
		});
		$("#button_add_" + treeNode.id).blur(function() {
			return false;
		});
	}

	function removeHoverDom(treeId, treeNode) {
		$("#button_add_" + treeNode.id).unbind().remove();
	}

	function beforeDrop(treeId, treeNodes, targetNode, moveType, isCopy) {
		// 不能放到根节点之前和之后
		return !(targetNode.data.moduleId == ROOT_ID && (moveType == 'prev' || moveType == 'next'));
	}
	
	function beforeDrag(treeId, treeNodes) {

		// [1] 只允许单个单个节点拖拽
		if (treeNodes.length > 1) {
			Notice.show($('body'), '<span style="color:red;">亲，你还是一个一个来吧，我忙不过来啦</span>');
			return false;
		}

		// [2] 根节点不能动
		var node = treeNodes[0];
		if (node.data.moduleId == ROOT_ID) {
			return false;
		}
		return true;
	}

	function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
		// var tree = $.fn.zTree.getZTreeObj(treeId);
		var treeNode = treeNodes[0];
		var data = treeNode.data;
		var targetData = targetNode.data;
		switch (moveType) {
		case 'inner': {
			data.parentId = targetData.moduleId;
			break;
		}
		case 'prev': {
			data.parentId = targetData.parentId;
			data.seq = targetData.seq - 1;
			break;
		}
		case 'next': {
			data.parentId = targetData.parentId;
			data.seq = targetData.seq + 1;
			break;
		}
		}
		$.ajax({
			type : 'POST',
			url : G.API.MODULE_UPDATE,
			dateType : 'json',
			data : $.customParam(data),
			success : function(data) {
				if (data.moduleId && data.menuTitle) {
					
				}
				if (data.message && data.detail) {
					Notice.show($('#menu-grid'), '<span style="color:red;">保存失败</span>');
				}
			}
		});
	}
	
	/**
	 * 获取节点路径
	 */
	function getPath(node, filter, path) {
		if (!path) {
			path = [];
			path.push(node);
		}
		var parentNode = node.getParentNode();
		if (parentNode && filter(parentNode)) {
			path.push(parentNode);
		}
		if (parentNode && parentNode.getParentNode()) {
			getPath(parentNode, filter, path);
		}
		return path;
	}
	
	/**
	 * 过滤菜单树节点
	 */
	function filter(moduleName) {
		var nodes = tree.transformToArray(tree.getNodes());
		if (!moduleName) {
			tree.showNodes(nodes);
		} else {
			var showNodes = [];
			var hideNodes = [];
			$(nodes).each(function() {
				if (!new RegExp(moduleName).test(this.data.moduleName)/*匹配*/&& this.data.moduleId != ROOT_ID/*非根节点*/) {
					hideNodes.push(this);
				} else {
					showNodes.push(this);
				}
			});
			
			$(showNodes).each(function(){
				var path = getPath(this, function(node) {
					return node.data.moduleId != ROOT_ID;
				});
				showNodes = showNodes.concat(path);
			});
			
			var showNodeMap = {};
			$(showNodes).each(function() {
				showNodeMap[this.data.moduleId] = this;
			});
			
			showNodes = [];
			for (var attrName in showNodeMap) {
				showNodes.push(showNodeMap[attrName]);
			}
			
			for (var i = 0; i < hideNodes.length; i++) {
				var hideNode = hideNodes[i];
				if (showNodeMap[hideNode.data.moduleId]) {
					hideNodes.splice(i--, 1);
				}
			}
			
			tree.showNodes(showNodes);
			tree.hideNodes(hideNodes);
		}
		
	}

	
	/**
	 * 创建模块树视图
	 */
	function create(options) {
		var containerId = options.containerId;
		var container = $('#' + containerId);
		container.addClass('ztree');
		var rootNode = {
			id : ROOT_ID,
			pId : '~',
			name : '根节点',
			open : false,
			data : {
				moduleId : ROOT_ID,
				moduleCode : ROOT_ID,
				parentId : '~'
			},
			isParent : true
		};
		var treeNodes = [ rootNode ];
		var setting = {
			async : {
				enable : true,
				url : GLOBAL.API.MODULE_CHILDREN,
				dataType : 'json',
				autoParam : [ 'id=parentId' ],
				otherParam : {
					isCascade : true
				},
				dataFilter : function(treeId, parentNode, responseData) {
					return format(responseData, parseModule2TreeNode);
				}
			},
			callback : {
				onAsyncSuccess : function(event, treeId, treeNode, data) {

				},
				onClick : function(event, treeId, treeNode) {
					var descendantIds = getDescendantAttributes(treeNode, function(treeNode) {
						return treeNode.data.moduleId;
					});
					$('#form  #currentModuleId').val(treeNode.data.moduleId);
					$('#form #currentModuleCode').val(treeNode.data.moduleCode);
					if (treeNode.data.moduleId != 'root') {
						$('#form input[name=moduleId]').val(descendantIds.join(','));
					} else {
						$('#form input[name=moduleId]').val('');
					}
					seajs.emit(G.EVENT.RESOURCE_QUERY);
				},
				beforeEditName : updateModule,
				beforeRemove : removeModule,
				beforeDrag : beforeDrag,
				beforeDrop : beforeDrop,
				onDrop : onDrop
			},
			edit : {
				enable : true,
				renameTitle : '修改',
				removeTitle : '删除',
				drag : {
					isCopy : false,
					isMove : true
				}
			},
			view : {
				addHoverDom : addHoverDom,
				removeHoverDom : removeHoverDom,
				selectMulti : false
			}
		};
		tree = $.fn.zTree.init(container, setting, treeNodes);

		// 展开根节点
		var root = tree.getNodeByParam('id', ROOT_ID);
		tree.expandNode(root);
		
		// 搜索
		$('.ui-module .ui-tree-search .ui-search').keyup(function(event) {
			var cleaner = $(this).parent('form').find('.ui-cleaner');
			if (!!$.trim($(this).val()) && cleaner.hasClass('hidden')) {
				cleaner.removeClass('hidden');
			} else if (!$.trim($(this).val()) && !cleaner.hasClass('hidden')) {
				cleaner.addClass('hidden');
			}
			filter($(this).val());
		});

		$('.ui-module .ui-tree-search .ui-cleaner').click(function() {
			$(this).parent('form').find('.ui-search').val('');
			var nodes = tree.transformToArray(tree.getNodes());
			tree.showNodes(nodes);
			$(this).addClass('hidden');
			return false;
		});
	}

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
			id : module.moduleId,
			pId : module.parentId,
			open : true,
			name : module.moduleName,
			data : module,
			isParent : module.leaf
		// isHidden:module.isHidden,
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
		// var root = {
		// name : ''
		// };
		$(data).each(function(i, item) {
			var node = parser(item);
			if (item.childrenView && $.isArray(item.childrenView) && (item.childrenView.length > 0)) {
				// 还有孩子节点 => 递归调用
				node.childrenView = format(item.childrenView, parser);
			}
			nodes.push(node);
		});
		return nodes;
	}


	exports.create = create;
});