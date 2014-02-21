/**
 * 
 */
define(function(require, exports, module) {
	var $ = require("$");
	exports.noticeBarBlue = function(noticeText, style) {
		$(".notice_bar_" + style).remove();
		var notice = $('<div></div>').addClass("notice_bar_x notice_bar_" + style).append(
				$('<span></span>').addClass("notice_icon for_notice_" + style)).append(
				$('<span></span>').addClass("notice_text for_notice_" + style).html(noticeText)).appendTo($("body"));

		return $('<div></div>').addClass("notice_bar_" + style + " bar_outter").append(
				$('<div></div>').addClass("notice_bar_" + style + " bar_inner").append(
						$('<div></div>').addClass("bar_content").append($('<div></div>').addClass("floatcenter").css({
							width : $(notice).width() + 23
						}).append($(notice)))));

	};

	/**
	 * 
	 */
	exports.noticeBubble = function(id, trigger, width, direction, text) {
		if (trigger && trigger.attr) {
			var tip = trigger.poshytip({
				className : 'tip-yellowsimple',
				timeOnScreen : 2000,
				alignTo : 'target',
				alignX : 'center',
				offsetY : 5,
				content : text
			});

			tip.poshytip('show');

			function destroy(tip) {
				return function() {
					tip.poshytip('destroy');
				};
			}

			setTimeout(destroy(tip), 2000);
		}

	};

	/**
	 * 
	 */
	exports.noticeBubble = function() {
		$(".notice_bubble_pink").next(".clear").remove();
		$(".notice_bubble_pink").remove();
	};
});