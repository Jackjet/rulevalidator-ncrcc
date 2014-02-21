define(function(require, exports, module) {
	var $ = require('$');
	var log = require('log');
	
	
	exports.isNotNull = function(o, message) {
		if (o) {
			log.error(message);
		}
	};
	
	exports.isNull = function(o, message) {
		if (!o) {
			log.error(message);
		}
	};
	
	exports.hasText = function(o, message) {
		if(typeof(o) == 'string') {
			if ($.trim(o)) {
				
			}
		}
	};
	
});