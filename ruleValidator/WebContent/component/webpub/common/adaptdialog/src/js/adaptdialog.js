define(function(require, exports, module) {
	require("../theme/default/css/adaptdialog.css");
	var tpl = require("./adaptdialog.tpl");
	var Widget = require("widget");
	var $=require("$");
	var AdaptDialog = Widget.extend({
		template : tpl,
		
		bindEvent:function(){
			var that=this;
			//绑定注册按钮事件
			var buttons=this.get("buttons");
			if(buttons){
				for(var i=0;i<buttons.length;i++){
					var key=buttons[i].key;
					var handler=buttons[i].handler;
					$("#"+key).live("click",handler);
				}
			}
			//close事件
			if(parent){
				$(parent.document).find(".J-close").live("click",function(){
					$(parent.document).find(".ui-adaptdialog").remove();
					$(parent.document).find(".adaptdialog-widget-overlay").remove();
				});
			}else{
				$(".J-close").live("click",function(){
					$(".ui-adaptdialog").remove();
					$(".adaptdialog-widget-overlay").remove();
				});
			}

			
		},
		show:function(){
			//用户定义的宽度
			var custWidth=this.get("width");
			//浏览器可视区高度
			if(parent){
				var height = $(parent).height();
				var width = $(parent).width();
			}else{
				var height = $(window).height();
				var width = $(window).width();
			}
			//再不出现滚轴的情况下应该居中的高度
			var pandTop = (height - 222) / 2;
			var pandLeft = (width - 518)/2;
			//滚轴高度	
			if(parent){
				var scrollTop = $(parent.document).scrollTop();
			}else{
				var scrollTop = $(document).scrollTop();
			}
			//实际居中高度
			var top=scrollTop+pandTop;
			// 创建dialog容器
			var container=$("<div>").css({
				"z-index" : 700,
				"left" : pandLeft,
				"position" : "absolute",
				"top" : top,
				"width":custWidth
			}).addClass("ui-adaptdialog");
			//计算蒙版高度
			var dialog_height;
			var scrollHeight, offsetHeight;
			// handle IE 6
			if ($.browser.msie && $.browser.version < 7) {
				scrollHeight = Math.max(
						document.documentElement.scrollHeight,
						document.body.scrollHeight);
				offsetHeight = Math.max(
						document.documentElement.offsetHeight,
						document.body.offsetHeight);

				if (scrollHeight < offsetHeight) {
					dialog_height= $(window).height() + 'px';
				} else {
					dialog_height= scrollHeight + 'px';
				}
				// handle "good" browsers
			} else {
				if(parent){
					dialog_height= $(parent.document).height() + 'px';
				}else{
					dialog_height= $(document).height() + 'px';
				}
				
			}
			//计算蒙版宽度
			var dialog_width;
			var scrollWidth, offsetWidth;
			// handle IE
			if ($.browser.msie) {
				scrollWidth = Math.max(
						document.documentElement.scrollWidth,
						document.body.scrollWidth);
				offsetWidth = Math.max(
						document.documentElement.offsetWidth,
						document.body.offsetWidth);

				if (scrollWidth < offsetWidth) {
					dialog_width= $(parent).width() + 'px';
				} else {
					dialog_width= scrollWidth + 'px';
				}
				// handle "good" browsers
			} else {
				if(parent){
					dialog_width= $(parent.document).width() + 'px';
				}else{
					dialog_width= $(document).width() + 'px';
				}
			}
			//设置蒙版样式
			var overlay = $("<div>").addClass('adaptdialog-widget-overlay')
				.css({
					width: dialog_width,
					height: dialog_height,
					"z-index":699,
					backgroundColor: "black",
					opacity: 0.3,
					filter: "Alpha(Opacity = 30)"
				});
			//添加css
			$(parent.document.body).append("<link rel='stylesheet' " +
					"type='text/css' href='/web/component/webpub/tools/jquery" +
					".plupload/src/theme/default/css/jquery.plupload.queue.css'/>");
			$(parent.document.body).append("<link rel='stylesheet' " +
					"type='text/css' href='/web/component/webpub/common/adaptdialog/" +
					"src/theme/default/css/adaptdialog.css'/>");
			//添加蒙版
			if(parent){
				$(parent.document.body).append(overlay);
			}else{
				$("body").append(overlay);
			}
			container.append(this.element.html());
			if(parent){
				$(parent.document.body).append(container);
			}else{
				$("body").append(container);
			}
		}
	});
	module.exports = AdaptDialog;
});
