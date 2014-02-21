/**
 * iframe 高度设置
 */
define(function(require, exports, module) {
	var Base = require("base");
	var $ = require("$");
	require("./iframeresize.css");
	var ResizeIframe = Base.extend({
		resize : function resizeiframe(fixheight) {
			$("iframe").each(function() {
				//针对弹出框中的iframe设置data-resize属性，对此不重新计算iframe高度
				if(!$(this).attr("data-resize")){
					var iframe = this;
					if (!iframe)
						return;
					iframe.onload = function() {
						resize(iframe);
						$(".loading").remove();
					};
					iframe.onreadystatechange = function() {
						if (this.readyState && this.readyState == "complete") {
							resize(iframe);
							$(".loading").remove();
						}
					};
					resize(iframe, fixheight);
				}
			});
		}
	});
	var resize = function(iframe, fixheight) {
		$(iframe).height("900px");
		/*后台管理页面documentElement出现可能为null的情况*/
		var documentHeight = iframe.contentWindow.document.documentElement!=null ? iframe.contentWindow.document.documentElement.scrollHeight:0;
		var bodyHeight = 0;
		var body = iframe.contentWindow.document.body;
		
		var height = '';
		// 18px为滚动条宽带，两者高度差值大于18px说明高度调整，取最大者，否则不变。
		if (documentHeight -  bodyHeight > 18) {
			height = Math.max(documentHeight, bodyHeight);
		} else {
			height = bodyHeight;
		}
		height = fixheight || Math.max(height, 900);
		$(iframe).height(height + "px");
	};
	var resizeIframe = new ResizeIframe();
	resizeIframe.resize();
	seajs.on("event_grid_loaded", function(fixheight) {
		resizeIframe.resize(fixheight);
	});
	seajs.on("widget_template_change", function() {
		resizeIframe.resize();
	});
	seajs.on("iframe_loaded", function() {
		resizeIframe.resize();
	});
	seajs.on('event_widget_rendered', function() {
		resizeIframe.resize();
	});
	/*start 添加iframe最大最小化按钮*/
	if($("iframe").attr("data-expand")){
	var htmlContent ='<div class="maxcontent"><div class="expand_iframe" title="最大化">'+
					 '<div class="expand-top-line">&nbsp;</div>'+
					 '<div class="expand-content"><a>&nbsp;</a></div>'+
					 '<div class="expand-right-line">&nbsp;</div></div></div>';
	$(".subpage_content").prepend(htmlContent);
	$(".expand_iframe").toggle(
			function(){
				 $(".maxcontent").addClass("max");
				 $(".expand_iframe").prop("title","还原");
				 $(".side_column,.top_bar").addClass("ui-iframe-hide");
				 $(".subpage_content").removeClass("subpage_content").addClass("ui-iframe-hideWidth");					 	
			},
			function(){
				 $(".maxcontent").removeClass("max");
				 $(".expand_iframe").prop("title","最大化");
				 $(".side_column,.top_bar").removeClass("ui-iframe-hide");
				 $(".ui-iframe-hideWidth").removeClass("ui-iframe-hideWidth").addClass("subpage_content");
			});
	$(".expand_iframe").on("mouseover",function(){
		$(this).find("div").removeClass("ui-iframe-out");
		$(this).find("div").addClass("ui-iframe-hover");
	});
	$(".expand_iframe").on("mouseout",function(){
		$(this).find("div").removeClass("ui-iframe-hover");
		$(this).find("div").addClass("ui-iframe-out");
	});
	/*END 添加iframe最大最小化按钮*/
	}
	module.exports = resizeIframe;

});