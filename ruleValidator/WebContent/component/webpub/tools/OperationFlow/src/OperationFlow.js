define(function(require, exports, module) {
	return function(jQuery) {
/**
 * Created by IntelliJ IDEA. User: Liu Yuhong Date: 12-10-11 Time: 上午12:48
 * Generate a operation flow plugin.
 */

		(function($) {

			$.fn.extend({
						'version' : "1.0",
						UIOperationFlow : function(options, value) {
							var self = this;
							if (self.prop("tagName") == undefined)
								return false;

							var uiOptions = $.extend({
										'items' : null,
										'itemCurrent' : 1,
										'activeArrow' : false,
										'arrowUp' : false
									}, options);

							if (typeof value == 'string')
								value = "'" + value + "'";
							if (typeof options != 'object')
								eval("uiOptions." + options + " = " + value);

							if (uiOptions.itemStyle != "gray"
									&& uiOptions.itemStyle != "blue")
								uiOptions.itemStyle = "gray";
							var width = self.innerWidth(true) - 40;
							var flow = $('<div></div>').appendTo(self)
									.addClass("ui-flow").css({
												'width' : width
											});
							if (uiOptions.arrowUp)
								$('<div></div>').addClass("arrow_up")
										.appendTo(flow).after($('<div></div>')
												.addClass("clear"));
							$(uiOptions.items).each(function(index) {
								var style = uiOptions.itemCurrent == index + 1
										? "active"
										: uiOptions.itemCurrent > index + 1
												? "blue"
												: "gray";
								$('<a></a>')
										.appendTo(flow)
										.attr({
													'href' : this.itemUrl
												})
										.append($('<span></span>')
												.addClass("bg_left"))
										.append($('<span></span>')
												.addClass("bg_right")
												.append($('<span></span>')
														.addClass("icon_no")
														.addClass(style)
														.addClass(uiOptions.activeArrow
																? "arrow"
																: "")
														.append($('<span></span>')
																.addClass("number")
																.addClass(style)
																.addClass("no_"
																		+ (index + 1))))
												.append($('<div></div>')
														.addClass("flow_name")
														.html(this.itemName)
														.addClass(style)
														.addClass(uiOptions.activeArrow
																? "arrow"
																: "")))
							});

							return flow;
						}
					});
		})(jQuery);
		return jQuery.noConflict(true);
	}
});