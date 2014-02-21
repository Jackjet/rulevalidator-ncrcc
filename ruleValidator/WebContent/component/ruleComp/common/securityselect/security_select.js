define(function(require, exports, module) {
	var $ = require('$');
	require('./security_select.css');
	var handlebars = require('handlebars');
	var StringUtils = require('stringUtils');
	
	var COLLECTION_SECURITY_LEVEL = [ {
		key : 5,
		label : '5'
	}, {
		key : 4,
		label : '4'
	}, {
		key : 3,
		label : '3'
	}, {
		key : 2,
		label : '2'
	}, {
		key : 1,
		label : '1'
	}, {
		key : 0,
		label : '0'
	}, {
		key : -1,
		label : '-1'
	}, {
		key : -2,
		label : '-2'
	}, {
		key : -3,
		label : '-3'
	}, {
		key : -4,
		label : '-4'
	}, {
		key : -5,
		label : '-5'
	} ];

	var COLOR_SECURITY_LEVEL = {
		from : {
			r : 0,
			g : 50,
			b : 150
		},
		to : {
			r : 250,
			g : 0,
			b : 0
		}
	};
	
	/**
	 * 计算渐变色值
	 */
	function getColor(options) {
		var ratio = options.ratio;
		var from = options.color.from;
		var to = options.color.to;
		var color = {
			r : 0,
			g : 0,
			b : 0
		};

		color.r = parseInt((to.r - from.r) * ratio + from.r);
		color.g = parseInt((to.g - from.g) * ratio + from.g);
		color.b = parseInt((to.b - from.b) * ratio + from.b);
		return color;
	}

	/**
	 * 计算比例
	 */
	function getRatio(options) {
		var array = options.array;
		var target = options.target;
		var equals = options.equals;
		var ratio = 1;
		$(array).each(function(i, item) {
			if (equals(target, item)) {
				ratio = i / array.length;
				return false;
			}
		});
		return ratio;
	}
	
	/**
	 * 获取进度条
	 */
	function getProgressBar(options) {
		var value = options.value;
		var parser = options.parser;
		var ratio = getRatio({
			array : COLLECTION_SECURITY_LEVEL,
			equals : function(a, b) {
				if (a.key == b.key) {
					return true;
				} else {
					return false;
				}
			},
			target : {
				key : parseInt(value)
			}
		});
		var options = {
			color : COLOR_SECURITY_LEVEL,
			ratio : ratio
		};
		var color = getColor(options);
		var width = parseInt(100 * (1 - ratio));
		if (width == 0) {
			width = 5;
		}
		return parser(color, width);
	}
	
	/**
	 * 注册select组件
	 */
	handlebars
			.registerHelper(
					"select",
					function(context, option) {
						var html = '';
						var collection = context.collection;
						var selected = context.selected;
						var name = context.name;
						html += '<ul class="ui-options">';
						for ( var i = 0; i < collection.length; i++) {
							var key = collection[i].key;
							html += getProgressBar({
								value : key,
								parser : function(color, width) {
									var template = '<li class="ui-option" value="%s">';
									template += '<a>';
									template += '<span class="ui-progress-column" style="background-color: rgb(%s, %s, %s); width: %spx;" title="' + key + '">&nbsp;</span>';
									template += '</a>';
									template += '</li>';
									return StringUtils.format(template, [ key, color.r, color.g, color.b, width ]);
								}
							});
						}
						html += '</ul>';
						html = '<select class="ui-select" name="' + name + '"><option value="' + selected
								+ '" selected="selected">' + selected + '</option></select>' + html + '</select>';
						return html;
					});
	
	
	exports.COLLECTION_SECURITY_LEVEL = COLLECTION_SECURITY_LEVEL;
	exports.COLOR_SECURITY_LEVEL = COLOR_SECURITY_LEVEL;
	exports.getProgressBar = getProgressBar;
	
});