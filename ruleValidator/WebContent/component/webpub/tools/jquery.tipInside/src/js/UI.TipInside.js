define(function(require, exports, module) {
	return function(jQuery) {
		/**
		 * Created by IntelliJ IDEA. User: Liu Yuhong Date: 12-10-15 Time:
		 * 下午4:15 Generate a textfield input inside tips and a cleaner button.
		 */
		(function($) {

			$.fn
					.extend( {
						'version' : "1.0",
						UITipInside : function(options, value) {
							var self = this;

							var uiOptions = $.extend( {
								'required' : false,
								'tipText' : null,
								'textColor' : "#9f9f9f",
								'cleaner' : true,
								'cleanerSize' : 18,
								'callback' : null
							}, options);

							if (typeof value == 'string')
								value = "'" + value + "'";
							if (typeof options != 'object')
								eval("uiOptions." + options + " = " + value);

							$(self)
									.each(
											function(index, obj) {
												if ((obj.tagName != 'INPUT' || (obj.type != 'text' && obj.type != 'password'))
														&& obj.tagName != "TEXTAREA")
													return false;

												$(obj)
														.addClass(
																uiOptions.required ? "idInputRequired"
																		: "")
														.addClass(
																uiOptions.required ? "idInputRequired"
																		: "");

												var inputWidth = $(obj)
														.outerWidth(true), inputHeight = $(
														obj).outerHeight(true);
												if (uiOptions.required
														|| uiOptions.tipText != null
														|| obj.title.length > 0) {
													var tipText = uiOptions.required ? "*"
															: uiOptions.tipText
																	|| obj.title;
													var tipSpan = $(
															'<span></span>')
															.addClass("revert")
															.html(tipText)
															.css(
																	{
																		'width' : $(
																				obj)
																				.width(),
																		'paddingTop' : $(
																				obj)
																				.css(
																						"paddingTop"),
																		'paddingBottom' : $(
																				obj)
																				.css(
																						"paddingBottom"),
																		'paddingLeft' : $(
																				obj)
																				.css(
																						"paddingLeft"),
																		'paddingRight' : $(
																				obj)
																				.css(
																						"paddingRight"),
																		'height' : $(
																				obj)
																				.css(
																						"height"),
																		'lineHeight' : $(
																				obj)
																				.css(
																						"lineHeight"),
																		'textIndent' : 5,
																		'color' : uiOptions.required ? "red"
																				: uiOptions.textColor,
																		'backgroundColor' : "white"
																	})
															.bind(
																	'click',
																	function() {
																		obj
																				.focus();
																	});

													$(obj)
															.css(
																	{
																		'position' : "absolute",
																		'zIndex' : 2,
																		'background' : "transparent"
																	})
															.after(
																	$(
																			'<div></div>')
																			.addClass(
																					"clear")
																			.after(
																					tipSpan))
															.bind(
																	'keydown paste',
																	function() {
																		if (tipSpan
																				.html().length > 0)
																			tipSpan
																					.html("");
																	})
															.bind(
																	'keyup blur change',
																	function() {
																		if ($(
																				this)
																				.val().length == 0)
																			tipSpan
																					.html(tipText);
																	})
															.removeAttr("title")
															/* .val("") */
															.parent("label")
															.addClass(
																	"tipinside")
															.css(
																	{
																		'position' : "relative",
																		'width' : $(
																				obj)
																				.outerWidth(
																						true)
																	});
												} else {
													$(obj)
															.css(
																	{
																		'backgroundColor' : "white"
																	});
												}

												if (uiOptions.cleaner) {
													var cleaner = $(
															'<div></div>')
															.insertBefore(obj)
															.addClass(
																	"ui-cleaner")
															.append(
																	$(
																			'<a href="."></a>')
																			.bind(
																					'click',
																					function() {
																						$(
																								obj)
																								.val(
																										"")
																								.blur()
																								.focus()
																								.parent(
																										'label')
																								.find(
																										'.revert')
																								.html(
																										tipText);
																						$(
																								cleaner)
																								.hide();
																						return false;
																					}));

													cleaner
															.css( {
																'marginLeft' : inputWidth
																		- uiOptions.cleanerSize
																		- 5,
																'marginTop' : (inputHeight - uiOptions.cleanerSize) / 2
															});
													cleaner.after($(
															'<div></div>')
															.addClass("clear"));
													$(obj)
															.bind(
																	'focus keyup',
																	function() {
																		if ($(
																				this)
																				.val().length > 0)
																			cleaner
																					.show();
																	})
															.bind(
																	'keydown change',
																	function() {
																		if ($(
																				this)
																				.val().length == 0)
																			cleaner
																					.hide();
																	})
															.bind(
																	'keydown',
																	function(
																			event) {
																		if (event.keyCode == 9)
																			cleaner
																					.hide();
																	})
															.bind(
																	'click',
																	uiOptions.callback);
													$(obj)
															.parent("label")
															.bind(
																	'mouseenter',
																	function() {
																		if (obj.value.length > 0)
																			cleaner
																					.show();
																	})
															.bind(
																	'mouseleave',
																	function() {
																		cleaner
																				.hide();
																	});
												}
											});
						}
					});
		})(jQuery);
		return jQuery.noConflict(true);
	}
});