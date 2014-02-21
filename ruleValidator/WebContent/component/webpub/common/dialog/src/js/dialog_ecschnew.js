/**
 * 弹出框类
 */
define(function(require, exports, module) {
	
	var $ = require("jquery");
	require("core/widget/src/css/base.css");
	require("../theme/default/css/dialog.css");
	$ = require("tools/jquery.jtemplates/src/jquery.jtemplates")($);
	var lang = window.lang || "simpchn";
	//var newlang = '../i18n/'+lang+'/resource.json';
	var resource = {
		confirmButton : "确定",
		cancelButton : "取消"
	};
	if (strSelLan == "english") {
		resource = {
			confirmButton : "confirm",
			cancelButton : "cancel"
		};
	} else if (strSelLan == "tradchn") {
		resource = {
			confirmButton : "確定",
			cancelButton : "取消"
		};
	} else if (strSelLan == "french") {
		resource = {
			confirmButton : "confirm",
			cancelButton : "cancel"
		};
	}

	/**	
	 * option对象使用说明
	 * 
	 * title：弹出框的标题
	 * hasCloseTip：是否显示右上角关闭按钮
	 * content:具体显示内容
	 * isHtmlContent:内容是否支持html
	 * type:弹出框类型，调用时参数不需要设置，设置了也是无效的
	 * buttons:按钮数组,按钮都是a标签，渲染为按钮，主要属性如下：
	 *         name：按钮名称
	 *         isDefault:是否默认主显示按钮，为true时，按钮为蓝色高亮显示，否则为灰色
	 *         href:按钮对应的连接
	 *         method:onclick所绑定的方法
	 * 	
	 */
	var reWriteDialog = function(option) {
		//浏览器可视区高度
		var height = $(window).height();
		var width = $(window).width();
		//再不出现滚轴的情况下应该居中的高度
		var pandTop = (height - 222) / 2;
		var pandLeft = (width - 518) / 2;
		//滚轴高度				
		var scrollTop = $(document).scrollTop();
		//横轴长度				
		var scrollLeft = $(document).scrollLeft();
		//实际居中高度
		var top;
		if(option.top != null && option.top != ""){
			top = option.top;
		}else{
			top = scrollTop + pandTop;
		}
		var left = scrollLeft + pandLeft;
		// 创建dialog容器
		var outter = $("<div>").css({
			"z-index" : "9999",
			"left" : left,
			"position" : "absolute",
			"top" : top,
			"width" : "518px"
		}).attr("id", "Dialog");

		var container = "<div class='ui-dialog'>";
		var panelClose;
		if (!option.hasCloseTip) {
			panelClose = "";
		} else {
			panelClose = "<a class='panel_closer ET_closer' href='#'></a>";
		}
		// 头部
		var topPanel = "<div class='dialog_top'>" + "<div class='dialog_panel'>" + "   <span >{$T.title}</span>" + panelClose + "</div>" + "</div>";
		// 内容
		var contentPanel;
		if (option.isHtmlContent) {
			var content = $("<div>").append(option.content).html();
			contentPanel = "<div class='dialog_content'><p>" + content + "</p></div>";
		} else {
			contentPanel = "<div class='dialog_content'>" + "   <p >{$T.content}</p>" + "</div>";
		}

		// 底边
		var btnPanel;
		if (option.type == "alert") {
			option.confirm = option.confirm || resource.confirmButton;
			btnPanel = "<div class='dialog_btm'>" + "<div class='dialog_pane'>" + "<a class='btn_blue font_hei ET_alert' style='cursor:pointer'>{$T.confirm}</a>" + "</div>" + "</div>"
		} else if (option.type == "confirm") {
			btnPanel = "<div class='dialog_btm'>" + "<div class='dialog_pane'>" + "<a class='btn_blue font_hei ET_confirm' style='cursor:pointer'>" + resource.confirmButton + "</a>"
					+ "" + "" + "" + "</div>" + "</div>"
		} else if (option.type == "dialog") {
			var btnPane = $("<div>").addClass("dialog_pane");
			if (option.buttons) {
				for ( var i = 0; i < option.buttons.length; i++) {
					var btn = $("<a>").append(option.buttons[i].name);
					if (option.buttons[i].isDefault) {
						btn.addClass("btn_blue font_hei");
					} else {
						btn.addClass("btn_gray font_hei");
					}
					var href = option.buttons[i].href;
					if (href) {
						btn.attr("href", href);
					} else {
						btn.attr("href", "#");
					}
					btnPane.append(btn);
				}
			}

			var dialogBtm = $("<div>").addClass("dialog_btm");
			dialogBtm.append(btnPane);
			var tempPane = $("<div>").append(dialogBtm);
			btnPanel = tempPane.html();
		} else if (option.type == "loading") {
			btnPanel = "<div class='dialog_btm'>" + "<div class='dialog_pane'>" + "</div>" + "</div>"
		}
		var template = container + topPanel + contentPanel + btnPanel + "</div>";

		var dialogoverlay = $(".dialog-widget-overlay");
		if (dialogoverlay != null && dialogoverlay.length > 0) {
			return;
		}
		outter.setTemplate(template).processTemplate(option);
		//计算蒙版高度
		var dialog_height = page_height();
		//计算蒙版宽度
		var dialog_width = page_width();
		//设置蒙版样式
		var overlay = $("<div>").addClass('dialog-widget-overlay').css({
			width : dialog_width,
			height : dialog_height,
			"z-index" : 699
		});

		var dialog_wrapper = $("<div>").append(overlay).append(outter);
		//添加蒙版
		$("body").append(dialog_wrapper);

		//绑定关闭事件
		$(".ET_closer").bind("click", closerDialog);

		//绑定确认操作
		$(".ET_alert").unbind("click").bind("click", function() {
			closerDialog();
			return false;
		});

		//绑定确认操作
		$(".ET_confirm").bind("click", function() {
			if (option.confirm) {
				if (option.confirm()) {
					closerDialog();
				} else {

				}
				;
			} else {
				closerDialog();
			}
		});
		//绑定取消操作
		$(".ET_cancel").bind("click", function() {
			if (option.cancel) {
				option.cancel();
			}
			closerDialog();
		});

	};

	/**
	 * 弹出对话框，需设定按钮
	 */
	exports.dialog = function(option) {
		option.type = "dialog";
		reWriteDialog(option);
		//绑定点击事件
		$(".dialog_pane a").each(function(index) {
			var method = option.buttons[index].method;
			if (method) {
				$(this).unbind("click").bind("click", method);
			}
		});
		//绑定关闭事件
		$("#Dialog").show();
		return false;
	}

	/**
	 * 弹出confirm对话框
	 */
	exports.confirm = function(option) {
		option.type = "confirm";
		reWriteDialog(option);
		//绑定点击事件
		$(".dialog_pane a").each(function(index) {
			if(option.buttons){
				var method = option.buttons[index].method;
				if (method) {
					$(this).unbind("click").bind("click", method);
				}
			}
		});

		$("#Dialog").show();
		return false;
	};

	/**
	 * 
	 */
	exports.alert = function(option) {
		alert(option);
	};

	/**
	 * 
	 */
	exports.alertMsg = function(msg) {
		var option = {
			title : "提示",
			content : msg
		}
		alert(option);
		return false;
	}

	/**
	 * 弹出alert对话框
	 */
	var alert = function(option) {
		option.type = "alert";
		reWriteDialog(option);
		$("#Dialog").show();
		return false;
	}

	/**
	 * loading对话框
	 */
	exports.loading = function(option) {
		option.type = "loading";
		reWriteDialog(option);
		$("#Dialog").show();
		return false;
	}

	/**
	 * 关闭对话框
	 */
	exports.close = function() {
		closerDialog();
	}

	/**
	 * 关闭弹出对话框
	 */
	var closerDialog = function() {
		//删除对话框				
		$("#Dialog").remove();
		//删除蒙版
		$(".dialog-widget-overlay").remove();
	}

	/**
	 * 获取可视区高度
	 */
	var page_height = function() {
		var scrollHeight, offsetHeight;
		// handle IE 6
		if ($.browser.msie && $.browser.version < 7) {
			scrollHeight = Math.max(document.documentElement.scrollHeight, document.body.scrollHeight);
			offsetHeight = Math.max(document.documentElement.offsetHeight, document.body.offsetHeight);

			if (scrollHeight < offsetHeight) {
				return $(window).height() + 'px';
			} else {
				return scrollHeight + 'px';
			}
			// handle "good" browsers
		} else {
			return $(document).height() + 'px';
		}
	};

	/**
	 * 获取可视区宽度
	 */
	var page_width = function() {
		var scrollWidth, offsetWidth;
		// handle IE
		if ($.browser.msie) {
			scrollWidth = Math.max(document.documentElement.scrollWidth, document.body.scrollWidth);
			offsetWidth = Math.max(document.documentElement.offsetWidth, document.body.offsetWidth);

			if (scrollWidth < offsetWidth) {
				return $(window).width() + 'px';
			} else {
				return scrollWidth + 'px';
			}
			// handle "good" browsers
		} else {
			return $(document).width() + 'px';
		}
	}
	
	// 事件代理（用于ecp项目嵌套帧使用）
	if (top && top.seajs.on) {
		top.seajs.on('event_dialog', function(options) {
			reWriteDialog(options);
		});
	}
});