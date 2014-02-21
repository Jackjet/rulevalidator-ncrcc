define(function(require, exports, module) {
	var $ = require('$');
	require('./css/menu.css');
	$ = require('jquery.ztree')($);
	$ = require("jquery.custompara")($);
	var Dialog = require("dialog");
	var handlebars = require('handlebars');
	var Notice = require('notice');
	var ResourceSearcher = require('resource_searcher');
	var Switcher = require('switcher');

	var ROOT_ID = 'root';
	var tree = null;

	/**
	 * 新增
	 */
	function add(treeId, treeNode) {
		var parentId = treeNode.id;
		var record = {
			parentId : parentId
		};
		var template = require('./template/menu_add.tpl');
		var contentHtml = handlebars.compile(template)(record);
		$('.ui-menuitem').html(contentHtml);
		Switcher.create();
		$('#button-save').click(function() {
			if (validate($('#form-menu-add'))) {
				$.ajax({
					type : 'POST',
					url : G.API.MENU_INSERT,
					dateType : 'json',
					data : $('#form-menu-add').serialize(),
					success : function(data) {
						if (data.menuId && data.menuTitle) {
							// seajs.emit(G.EVENT.RESOURCE_QUERY);
							Notice.show($('body'), '添加成功');
							Dialog.close();
							$.fn.zTree.getZTreeObj(treeId);
							tree.addNodes(treeNode, {
								id : data.menuId,
								pId : data.parentId,
								name : data.menuTitle,
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
		});

		// 搜索框
		ResourceSearcher.init({
			renderTo : $('#form-menu-add #resourceSearcher'),
			callback : function(event, row, formatted) {

				var menuTitle = $('.ui-form-panel input[name=menuTitle]');
				if (!menuTitle.val()) {
					menuTitle.attr({
						title : row.resourceName
					}).val(row.resourceName);
				}

				$('#form-menu-add #resourceSearcher').attr({
					title : row.url
				});
				$('#form-menu-add input[name=resourceId]').attr({
					value : row.resourceId
				});
			}
		});
	}

	/**
	 * 新增
	 */
	function detail(event, treeId, treeNode) {
		if (treeNode.id != ROOT_ID) {
			var record = treeNode.data;
			var template = require('./template/menu_detail.tpl');
			var contentHtml = handlebars.compile(template)(record);
			$('.ui-menuitem').html(contentHtml);
			Switcher.create();
		}
	}

	function remove(treeId, treeNode) {
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
			content : '<center>确定要删除[' + treeNode.name + ']菜单吗？</center>',
			isHtmlContent : true,
			confirm : function() {
				$.ajax({
					type : 'POST',
					url : G.API.MENU_DELETE,
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

	function update(treeId, treeNode) {
		if (treeNode.id == ROOT_ID) {
			Dialog.alert({
				title : '提示框',
				content : '<center>不能修改根节点！</center>',
				isHtmlContent : true,
			});
			return false;
		}
		var record = treeNode.data;
		var template = require('./template/menu_update.tpl');
		var contentHtml = handlebars.compile(template)(record);
		$('.ui-menuitem').html(contentHtml);
		Switcher.create();
		$('#button-save').click(function() {
			if (validate($('#form-menu-update'))) {
				$.ajax({
					type : 'POST',
					url : G.API.MENU_UPDATE,
					dateType : 'json',
					data : $('#form-menu-update').serialize(),
					success : function(data) {
						if (data.menuId && data.menuTitle) {
							Notice.show($('body'), '修改成功');
							var tree = $.fn.zTree.getZTreeObj(treeId);
							treeNode.name = data.menuTitle;
							var newNodeData = parseMenu2TreeNode(data);
							$.extend(treeNode, newNodeData);
							$.extend(treeNode.data, data);
							// treeNode.nameCls = newNodeData.nameCls;
							tree.updateNode(treeNode);
							Dialog.close();
						}
						if (data.message && data.detail) {
							Notice.show($('#menu-grid'), '<span style="color:red;">保存失败</span>');
						}
					}
				});
			}
			return false;
		});

		// 搜索框
		ResourceSearcher.init({
			renderTo : $('#form-menu-update #resourceSearcher'),
			callback : function(event, row, formatted) {
				$('#form-menu-update #resourceSearcher').attr({
					title : row.url
				});
				$('#form-menu-update input[name=resourceId]').attr({
					value : row.resourceId
				});
				$('#form-menu-update #url').text(row.url).attr({
					title : row.url
				});
			}
		});

		return false;
	}

	/**
	 * 校验菜单
	 */
	function validate(form) {

		if (form) {
			if (!form.find('input[name=resourceId]').val()) {
				Notice.show(form.parents('fieldset'), '<span style="color:red;">资源不能为空</span>');
				return false;
			}
		}

		return true;
	}

	function addHoverDom(treeId, treeNode) {
		var sObj = $("#" + treeNode.tId + "_span");
		if (treeNode.editNameFlag || $("#button_add_" + treeNode.id).length > 0) {
			return;
		}
		var addStr = "<span class='button ui-icon-add' id='button_add_" + treeNode.id + "' title='增加子菜单'></span>";
		sObj.after(addStr);
		$("#button_add_" + treeNode.id).click(function() {
			add(treeId, treeNode);
			return false;
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
		return !(targetNode.data.menuId == ROOT_ID && (moveType == 'prev' || moveType == 'next'));
	}

	function beforeDrag(treeId, treeNodes) {

		// [1] 只允许单个单个节点拖拽
		if (treeNodes.length > 1) {
			Notice.show($('body'), '<span style="color:red;">亲，你还是一个一个来吧，我忙不过来啦</span>');
			return false;
		}

		// [2] 根节点不能动
		var node = treeNodes[0];
		if (node.data.menuId == ROOT_ID) {
			return false;
		}
		return true;
	}

	function updateSeq(treeNode) {
		var data = treeNode.data;
		data.seq += 1;
		$.ajax({
			type : 'POST',
			url : G.API.MENU_UPDATE,
			dateType : 'json',
			data : $.customParam(data),
			success : function(data) {
				//				Notice.show($('#menu-grid'), '更新成功');
			}
		});
	}

	function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
		// var tree = $.fn.zTree.getZTreeObj(treeId);
		var treeNode = treeNodes[0];
		var data = treeNode.data;
		var targetData = targetNode.data;
		data.resourceView.seq = 0;
		switch (moveType) {
		case 'inner': {
			data.parentId = targetData.menuId;
			var children = targetNode.children;
			if (children) {
				data.seq = children.length;
			} else {
				data.seq = 0;
			}
			break;
		}
		case 'prev': {
			data.parentId = targetData.parentId;
			var parent = targetNode.getParentNode();
			var children = parent.children;
			$(children).each(function(i, item) {
				item.data.seq = i;
				$.ajax({
					type : 'POST',
					url : G.API.MENU_UPDATE,
					dateType : 'json',
					data : $.customParam(item.data),
					success : function(data) {
//						Notice.show($('#menu-grid'), '更新成功' + i);
					}
				});
			});
			break;
		}
		case 'next': {
			data.parentId = targetData.parentId;
			var parent = targetNode.getParentNode();
			var children = parent.children;
			$(children).each(function(i, item) {
				item.data.seq = i;
				$.ajax({
					type : 'POST',
					url : G.API.MENU_UPDATE,
					dateType : 'json',
					data : $.customParam(item.data),
					success : function(data) {
//						Notice.show($('#menu-grid'), '更新成功' + i);
					}
				});
			});
			break;
		}
		}
		$.ajax({
			type : 'POST',
			url : G.API.MENU_UPDATE,
			dateType : 'json',
			data : $.customParam(data),
			success : function(data) {
				if (data.menuId && data.menuTitle) {
//					Notice.show($('#menu-grid'), '修改成功');
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
	function filter(menuTitle) {
		var nodes = tree.transformToArray(tree.getNodes());
		if (!menuTitle) {
			tree.showNodes(nodes);
		} else {
			var showNodes = [];
			var hideNodes = [];
			$(nodes).each(function() {
				if (!new RegExp(menuTitle).test(this.data.menuTitle)/* 匹配 */&& this.data.menuId != ROOT_ID/* 非根节点 */) {
					hideNodes.push(this);
				} else {
					showNodes.push(this);
				}
			});

			$(showNodes).each(function() {
				var path = getPath(this, function(node) {
					return node.data.menuId != ROOT_ID;
				});
				showNodes = showNodes.concat(path);
			});

			var showNodeMap = {};
			$(showNodes).each(function() {
				showNodeMap[this.data.menuId] = this;
			});

			showNodes = [];
			for ( var attrName in showNodeMap) {
				showNodes.push(showNodeMap[attrName]);
			}

			for ( var i = 0; i < hideNodes.length; i++) {
				var hideNode = hideNodes[i];
				if (showNodeMap[hideNode.data.menuId]) {
					hideNodes.splice(i--, 1);
				}
			}

			tree.showNodes(showNodes);
			tree.hideNodes(hideNodes);
		}

	}

	/**
	 * 创建菜单树视图
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
				menuId : ROOT_ID,
				menuTitle : ROOT_ID,
				parentId : '~'
			},
			isParent : true
		};
		var treeNodes = [ rootNode ];
		var setting = {
			async : {
				enable : true,
				url : GLOBAL.API.MENU_CHILDREN,
				dataType : 'json',
				autoParam : [ 'id=parentId' ],
				otherParam : {
					isCascade : false
				},
				dataFilter : function(treeId, parentNode, responseData) {
					return format(responseData, parseMenu2TreeNode);
				}
			},
			callback : {
				// onAsyncSuccess : function(event, treeId, treeNode, data) {
				//
				// },
				onClick : detail,
				beforeEditName : update,
				beforeRemove : remove,
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
					isMove : false
				}
			},
			view : {
				addHoverDom : addHoverDom,
				removeHoverDom : removeHoverDom,
				selectMulti : false,
				expandSpeed : 'fast'
			// nameIsHTML:true
			}
		};
		tree = $.fn.zTree.init(container, setting, treeNodes);

		// 展开根节点
		var root = tree.getNodeByParam('id', ROOT_ID);
		tree.expandNode(root);

		// 搜索
		$('.ui-menu .ui-tree-search .ui-search').keyup(function(event) {
			var cleaner = $(this).parent('form').find('.ui-cleaner');
			if (!!$.trim($(this).val()) && cleaner.hasClass('hidden')) {
				cleaner.removeClass('hidden');
			} else if (!$.trim($(this).val()) && !cleaner.hasClass('hidden')) {
				cleaner.addClass('hidden');
			}
			filter($(this).val());
		});

		$('.ui-menu .ui-tree-search .ui-cleaner').click(function() {
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
	function parseMenu2TreeNode(menu) {
		if (!menu) {
			throw new Error('数据不能为空');
		}
		if ($.isArray(menu)) {
			throw new Error('数据不能使数组');
		}
		return {
			id : menu.menuId,
			pId : menu.parentId,
			open : true,
			name : menu.menuTitle,
			nameCls : menu.hide ? 'ui-menu-hide' : '',
			data : menu,
			isParent : menu.leaf
		// isHidden:menu.isHidden,
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
			if (item.childrenView && $.isArray(item.childrenView) && (item.childrenView.length > 0)) {
				// 还有孩子节点 => 递归调用
				node.children = format(item.childrenView, parser);
			}
			nodes.push(node);
		});
		return nodes;
	}
	exports.create = create;
});