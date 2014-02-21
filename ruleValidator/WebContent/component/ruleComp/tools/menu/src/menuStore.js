define(function(require, exports, module) {
	var $ = require("$");
	$ = require('jquery.extend')($);
	var store = require("store");
	var StringUtils = require('stringUtils');
	var Uri = require('jsuri');

	// constants
	var MENUS = 'menus';
	// var CURRENT_MENU_ID = "current.menu_id";
	// var CURRENT_FRAME_MENU_ID = "current.frame_menu_id";
	// 菜单路径，例如：一级菜单a > 二级菜单b，菜单之间用逗号分隔
	var CURRENT_MENU_PATH = "current.menu_path";
	var ROOT_ID = 'root';

	// functoins
	function getAll() {
		return store.get(MENUS);
	}

	function children(menus, parentId) {
		var target = [];
		$(menus).each(function(i, menu) {
			if (menu.menuId == parentId) {
				target = menu.childrenView;
				return false;
			} else {
				if (menu.childrenView && menu.childrenView.length > 0) {
					target = children(menu.childrenView, parentId);
				}
			}
			if (target.length > 0) {
				return false;
			}
		});
		return target;
	}

	function getChildren(parentId) {
		var menus = store.get(MENUS);
		return children(menus);
	}

	function getMenu(menus, menuId) {
		var target = null;
		$(menus).each(function(i, menu) {
			if (menu.menuId == menuId) {
				target = menu;
				return false;
			} else {
				if (menu.childrenView && menu.childrenView.length > 0) {
					target = getMenu(menu.childrenView, menuId);
				}
			}
			if (target) {
				return false;
			}
		});
		return target;
	}

	function get(menuId) {

		if (!menuId) {
			throw new Error('菜单ID不能为空');
		}

		// 统一处理为数组
		menuId = $.isArray(menuId) ? menuId : [ menuId ];
		var menus = [];
		$(menuId).each(function() {
			menus.push(getMenu(getAll(), this));
		});

		if (menus.length == 1) {
			return menus[0];
		} else {
			return menus;
		}
	}

	function create(options) {

		options.callback = options.callback ? options.callback : function() {
		};

		if (!store.get(MENUS)) {
			$.ajax({
				url : GLOBAL.API.MENU_CHILDREN,
				type : 'GET',
				dateType : 'json',
				async : false,
				data : {
					isCascade : true,
					parentId : ROOT_ID
				},
				success : function(data, status, response) {
					if (response.responseText != "gotoLogin" && response.responseText != "kickout") {
						store.set(MENUS, data);
					}
					options.callback();
				}
			});
		} else {
			options.callback();
		}
	}

	/**
	 * 获取菜单路径，从根到当前菜单
	 */
	function getPath(menuId, path) {

		if (!menuId) {
			throw new Error('请输入正确的菜单ID');
		}

		if (!path) {
			path = [];
		}
		var menu = get(menuId);
		path.push(menu);
		if (menu.parentId != ROOT_ID) {
			getPath(menu.parentId, path);
		} else {
			path.reverse();
		}
		return path;
	}

	/**
	 * 获取URL匹配的菜单
	 */
	function match(url, menus) {
		var uri = new Uri(url);
		var path = uri.path();

		var index = path.search(G.ctx);
		if (index > -1) {
			path = path.substr(index + G.ctx.length);
		}
		if (!menus) {
			menus = getAll();
		}
		var target = null;
		$(menus).each(function() {
			var resourceCode = this.resourceView.resourceCode;
			var code = resourceCode.split('@')[0];
//			var regexp = new RegExp('/' + code + '/');
			var regexp = new RegExp(code);
			if (this.resourceView && regexp.test(path)) {
				target = this;
				return false;
			} else {
				if (this.childrenView) {
					target = match(url, this.childrenView);
				}
			}
		});
		return target;
	}

	/**
	 * 设置菜单路径
	 */
	function setCurrent(menuId, depth) {

		// [1] 全部菜单（菜单ID为数组）
		if (menuId && $.isArray(menuId)) {
			store.set(menuId.join(','));
			return;
		}

		// [2] 分级菜单
		var menuPathStr = store.get(CURRENT_MENU_PATH) ? store.get(CURRENT_MENU_PATH) : '';
		var menuPaths = menuPathStr.split(',');
		if (menuPaths.length < depth) {
			throw new Error(StringUtils.format('当前菜单层[depth=%s]深小于要求值[depth=%]', [ menuPaths.length, depth ]));
		} else {
			menuPaths[depth] = menuId;
		}
		store.set(CURRENT_MENU_PATH, menuPaths.join(','));
	}

	/**
	 * 获取菜单路径
	 */
	function getCurrent(depth) {
		if (arguments.length == 1) {
			// [1] 获取某层菜单
			var menuPathStr = store.get(CURRENT_MENU_PATH) ? store.get(CURRENT_MENU_PATH) : '';
			var menuPaths = menuPathStr.split(',');
			if (menuPaths.length < depth) {
				throw new Error(StringUtils.format('当前菜单层[depth=%s]深小于要求值[depth=%]', [ menuPaths.length, depth ]));
			} else {
				return menuPaths[depth];
			}
		} else {
			// [2] 获取菜单路径
			var menuPathStr = store.get(CURRENT_MENU_PATH);
			if(menuPathStr){
				menuPathStr.replace(/\s/, '');
				if (menuPathStr) {
					return menuPathStr.split(',');
				} else {
					return null;
				}
			}
		}
	}

	module.exports = {
		// 虚拟根ID
		ROOT_ID : ROOT_ID,
		// 实例化
		create : create,
		// 获取菜单（支持批量）
		get : get,
		// 获取所有菜单
		getAll : getAll,
		// 获取下级菜单
		getChildren : getChildren,
		// 获取菜单路径（从根到当前菜单）
		getPath : getPath,
		// 设置当前活跃菜单（或者菜单路径）
		setCurrent : setCurrent,
		// 获取当前活跃菜单
		getCurrent : getCurrent,
		// 通过URL匹配菜单
		match : match
	};
	// create();
});