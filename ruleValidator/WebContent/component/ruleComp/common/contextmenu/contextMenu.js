define(function(require, exports, module) {
	var $ = require('$');
	require('./contextmenu.css');
	var handlebars = require('handlebars');
	var UUID = require('math.uuid');
	var menuGroupTpl = require('./contextmenu.tpl');

	// user customer menu data
	var data = [];
	// menu model
	var menuGroup = {};
	/**
	 * translate user defined data to templated data add extend attributes
	 */
	function translate(data) {
		var menuGroup = {
			groupId : UUID.uuidFast(),
			menus : []
		};
		$(data).each(function() {
			var menu = {
				menuId : UUID.uuidFast(),
				group : {},
				getMenuGroup : function() {
					return menuGroup;
				}
			};
			$.extend(menu, this);
			menuGroup.menus.push(menu);
			if (this.children && $.isArray(this.children) && this.children.length > 0) {
				menu.group = translate(this.children);
			}
		});
		return menuGroup;
	}

	function getMenuGroup(menuGroup, menuId) {
		var targetMenuGroup = null;
		$(menuGroup.menus).each(function() {
			if (this.menuId == menuId) {
				targetMenuGroup = this.group;
				return false;
			}
		});

		if (!targetMenuGroup) {
			$(menuGroup.menus).each(function() {
				if (this.group) {
					targetMenuGroup = getMenuGroup(this.group, menuId);
				}
			});
		}
		return targetMenuGroup;
	}

	function getParentMenuGroup(menuGroup, menuId) {
		var targetMenuGroup = null;
		$(menuGroup.menus).each(function() {
			if (this.menuId == menuId) {
				targetMenuGroup = menuGroup;
				return false;
			}
		});
		if (!targetMenuGroup) {
			$(menuGroup.menus).each(function() {
				if (this.group) {
					targetMenuGroup = getMenuGroup(this.group, menuId);
				}
			});
		}
		return targetMenuGroup;
	}

	function showSubmenu(event) {
		var menuEl = $(event.target);
		var menuId = menuEl.attr('id');
		var group = getMenuGroup(menuGroup, menuId);
		var parentGroup = getParentMenuGroup(menuGroup, menuId);
		if (group) {
			// FIXME 焦点进入子菜单时，父菜单不能隐藏
			// hide other submenu if
			$(parentGroup.menus).each(function() {
				$('#' + this.group.groupId).hide();
			});

			// show target submenu
			var submenu = $('#' + group.groupId);
			submenu.show();
			var left = menuEl.offset().left + menuEl.width();
			var top = menuEl.offset().top;
			submenu.offset({
				left : left,
				top : top
			});
		}
		return false;
	}

	function hideMenu(event) {
		$(event.target).hide();
		return false;
	}

	/**
	 * create menu html dom
	 */
	function createMenu(menuGroup) {
		var html = handlebars.compile(menuGroupTpl)(menuGroup);
		$(menuGroup.menus).each(function() {
			if (this.group.menus && this.group.menus.length > 0) {
				html += createMenu(this.group);
			}
		});
		return html;
	}

	/**
	 * add submenu auto display effect add customer action
	 */
	function addEffect(menuGroup) {
		$(menuGroup.menus).each(function() {
			var handler = this.handler;
			if (handler && $.isFunction(handler)) {
				$('#' + this.menuId).bind('click', function() {
					handler(this);
					hide(event);
					return false;
				});
			}
			addEffect(this.group);
		});
	}

	function create(options) {
		var event = options.event;
		var data = options.data;
		var hideModule = options.hideModule == undefined ? 'destroy' : 'hide';
		var width = options.width || 100;
		menuGroup = translate(data);
		var html = createMenu(menuGroup);
		$('body').append(html);
		$('.ui-contextmenu').css({
			width : width + 'px'
		});
		$('.ui-contextmenu ul.menus .menu-item').css({
			width : width + 'px'
		});
		addEffect(menuGroup);

		$('.ui-contextmenu').delegate('.menu-item', 'mouseenter', showSubmenu);
		$('.ui-contextmenu').live('mouseleave', function() {
			if (hideModule == 'hide')	{
				hide();
			} else {
				destroy();
			}
		});

		// $(selector).live('mousedown', function(event) {
		// if (event.button == '2') {
		// show(event);
		// event.preventDefault();
		// return false;
		// } else {
		// return true;
		// }
		// });

		show(event);
		
//		return {
//			show : show,
//			hide : hide
//		};
	}
	

	function destroy() {
		if (menuGroup.groupId) {
			$('#' + menuGroup.groupId).remove();
		}

		$(menuGroup.menus).each(function() {
			destroy(this);
		});
	}

	function show(event) {
		var target = $('#' + menuGroup.groupId);
		target.show();
		target.offset({
			left : event.layerX - 2,
			top : event.layerY - 2
		});
	}

	function hide(event) {
		//$(event.target).parents('.ui-contextmenu').hide();
		// destory after hide 1 min
		//setTimeout(destroy, 1 * 60 * 1000);
		destroy();
	}

	exports.create = create;
});