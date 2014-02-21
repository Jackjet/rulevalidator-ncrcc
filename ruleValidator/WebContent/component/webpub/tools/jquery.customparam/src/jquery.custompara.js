/**
 * jquery ajax提交数据编码
 */

define(function(require, exports, module) {
	return function(jQuery) {
(function($) {
			// copy from jquery.js
			var r20 = /%20/g, rbracket = /\[\]$/;

			$.extend({
						customParam : function(a) {
							var s = [], add = function(key, value) {
								// If value is a function, invoke it and return
								// its value
								value = jQuery.isFunction(value)
										? value()
										: value;
								s[s.length] = encodeURIComponent(key) + "="
										+ encodeURIComponent(value);
							};

							// If an array was passed in, assume that it is an
							// array of form elements.
							if (jQuery.isArray(a)
									|| (a.jquery && !jQuery.isPlainObject(a))) {
								// Serialize the form elements
								jQuery.each(a, function() {
											add(this.name, this.value);
										});

							} else {
								for (var prefix in a) {
									buildParams(prefix, a[prefix], add);
								}
							}

							// Return the resulting serialization
							return s.join("&").replace(r20, "+");
						}
					});

			/* private method */
			function buildParams(prefix, obj, add) {
				if (jQuery.isArray(obj)) {
					// Serialize array item.
					jQuery.each(obj, function(i, v) {
								if (rbracket.test(prefix)) {
									// Treat each array item as a scalar.
									add(prefix, v);

								} else {
									buildParams(prefix
													+ "["
													+ (typeof v === "object"
															|| jQuery
																	.isArray(v)
															? i
															: "") + "]", v, add);
								}
							});

				} else if (obj != null && typeof obj === "object") {
					// Serialize object item.
					for (var name in obj) {
						buildParams(prefix + "." + name, obj[name], add);
					}

				} else {
					// Serialize scalar item.
					add(prefix, obj);
				}
			};
		})(jQuery);
		return jQuery.noConflict(true);
	}
});