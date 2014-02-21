define(function(require, exports, module) {
	var $ = require('$');
	require('../theme/default/css/notice.css');
	
	function show(target, message, delay) {
		if (!target || target.length == 0) {
			target = $('body');
		}
		
		if (delay == undefined) {
			delay = 2000;
		}
		
		var dom = $('<div>').addClass('ui-notice').html(message);
		var coordinate = target.offset();
		coordinate.left += target.width() / 2 - 200 / 2;
		coordinate.top += target.height() / 2 - 26 / 2;
		var other = target.find('.ui-notice');
		coordinate.left += other.length * 10;
		coordinate.top += other.length * 10;
		target.append(dom);
		dom.offset(coordinate);
		dom.fadeToggle('slow', function() {
			setTimeout(function() {
				dom.fadeToggle('slow', function() {
					dom.remove();
				});
			},  delay);
		});
	}
	
	exports.show = show;
});