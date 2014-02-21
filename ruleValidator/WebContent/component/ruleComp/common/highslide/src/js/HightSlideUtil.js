//============================================
// 用于将普通的image标签转换为使用HighSlide来展现的工具类
//
//============================================

define(function(require, exports, module) {
	var $=require("$");
	var hs=require("./highslide");
	$(document).ready(function() {
		
		// 库检验和默认设置
		if (!!hs) {
		    hs.graphicsDir = G.ecpimages + '/highslide/';
			hs.wrapperClassName = 'borderless';
			hs.registerOverlay({
				html: '<div class="closebutton" onclick="return hs.close(this)" title="关闭"></div>',
				position: 'top right',
				fade: 2 // fading the semi-transparent overlay looks bad in IE
			});
		} else {
			throw new Error('没有发现HighSlide实例，请检查页面是否引入了HighSlide库');
		}
		
		// 将image标签包裹一层a标签
		$('img.hsthumb').each(function(index, dom) {
			var src = $(dom).attr('src');
			var title = $(dom).attr('alt') || $(dom).attr('title');
			var description = $('<div>').addClass('highslide-caption').append(title);
			var wrapper = $('<a>').attr({
						'class':'highslide hsThumbnail',
						href : src,
						title : title
					});
			$(dom).wrap(wrapper[0]);
			$(dom).parent().after(description);
			$(dom).click(function(){
				return hs.expand($(this).parent('a')[0]);
			});
		});
	});
});