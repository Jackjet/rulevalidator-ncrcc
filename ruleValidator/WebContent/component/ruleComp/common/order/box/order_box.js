define(function(require, exports, module) {
	var $ = require("$");
	require('order_theme');
	var Widget = require("widget");

	/**
	 * 下拉按钮
	 */
	function toggle(trigger) {
		if ($($(trigger).parents(".bar_content")).attr("class").indexOf("inside_width") > 0) {
			// 判断是展开还是收起
			if ($(trigger).parents(".rounded_extended_bar").length > 0) {
				// 如果展开，则收起
				$(trigger).parents(".info_line").next(".ui-box-body").slideUp('slow', function() {
					$(trigger).parents(".rounded_extended_bar").addClass("rounded_single_bar inside_width").removeClass("rounded_extended_bar");
					$(trigger).prev(".info_line").find(".trigger").removeClass("up").addClass("down");
					$(trigger).prev(".info_line").find(".content_info").show();
				});
			} else if ($(trigger).parents(".rounded_single_bar").length > 0) {
				// 如果收起 则展开
				$(trigger).parents(".rounded_single_bar").addClass("rounded_extended_bar inside_width").removeClass("rounded_single_bar");
				$(trigger).removeClass("down").addClass("up");
				$(trigger).parents(".bar_content").find(".content_info").hide();
				$(trigger).parents(".info_line").next(".ui-box-body").slideDown('slow', function() {
				});
			}
		} else if ($($(trigger).parents(".bar_content")).attr("class").indexOf("outside_width") > 0) {
			if ($(trigger).parents(".rounded_extended_bar").length > 0) {
				$(trigger).parents(".info_line").next(".ui-box-body").slideUp('slow', function() {
					$(trigger).parents(".rounded_extended_bar").addClass("rounded_single_bar inside_width").removeClass("rounded_extended_bar");
					$(trigger).prev(".info_line").find(".trigger").removeClass("up").addClass("down");
				});
			} else if ($(trigger).parents(".rounded_single_bar").length > 0) {
				$(trigger).parents(".rounded_single_bar").addClass("rounded_extended_bar inside_width").removeClass("rounded_single_bar");
				$(trigger).removeClass("down").addClass("up");
				$(trigger).parents(".info_line").next(".ui-box-body").slideDown('slow', function() {
				});
			}
		}
		return false;
	}

	/**
	 * 删除
	 */
	function remove(trigger) {
		$(trigger).parents('.J-box').remove();
	}

	module.exports = Widget.extend({
		template : require('./template/order_box.tpl'),
		afterRender : function() {
			this.constructor.superclass.afterRender.apply(this, arguments);

			var attrs = this.get('attrs');
			attrs.remove = attrs.remove ? attrs.remove : function() {
				return true;
			};
			attrs.toggle = attrs.toggle ? attrs.toggle : function() {
				return true;
			};

			// 下拉按钮
			$(this.element).find('.trigger').click(function() {
				if (!(attrs.toggle(this) == false)) {
					toggle(this);
				}
				return false;
			});

			// 删除按钮
			$(this.element).find('.icon_delete').click(function() {
				if (!(attrs.remove(this) == false)) {
					remove(this);
				}
				return false;
			});
		}
	});
});