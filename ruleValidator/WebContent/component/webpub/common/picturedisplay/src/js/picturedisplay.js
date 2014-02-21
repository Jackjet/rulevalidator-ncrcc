
/*
 * 组件示例
 */
define(function(require, exports, module) {

	// 默认引入：组件继承自widget
	var Widget = require("widget");
	// 自定义：组件对应的css
	require('../theme/{theme}/css/picturedisplay.css');
	// 自定义：组件的html模板
	var template = require("./picturedisplay.tpl");
	var $ = require("$");
	$=require("jquery.extend")($);
	var request = require("request");
	var defaultImage = require("defaultimage");
	// 组件的定义 组件名大写
	var Example = Widget.extend({
		template : template,
		initCustAttr : function() {
			this.get("attrs").ctx = G.ctx;
		},
		loadData : function() {
			var that = this;
			$(function() {
				var id = request.getParameter("id");
				var url = G.API.PRODUCT_IMAGES;
				$.ajax({
					type : 'GET',
					url : url,
					data:{
            				"id":id
            			},
					success : function(data) {
						that.setModel(data);
					},
					dataType : 'json'
				});
			})
		},
		afterRender : function() {
			var that=this;
			defaultImage.init();
			$(function() {
				// 默认显示第一张图片
				$(".picture_thumbs .thumb_item_in:first").addClass("active");
				$(".picture_box .picture").not(":first").css("display","none");
				var span=$("<span></span>").addClass("arrow");
				$(".thumb_item_out:first").append(span);
				that.element.find(".thumb_item_in img").each(function(){
					if($(this).attr("isdefault")=="true"){
						$(this).css("width","50px");
					}
				});
				/* 商品详细页－商品图片切换 */
				$(".picture_thumbs .thumb_item_in").bind('mouseenter',function() {
					// 去掉小图上的三角
					$(".picture_thumbs .thumb_item_out.active").remove("span");
					$(".picture_thumbs .thumb_item_in.active").removeClass("active");
					// 给当前小图添加三角
					$(this).addClass("active").parent().append(span);
					// 获取所有小图片
					var thumbs = $(".picture_thumbs .thumb_item_in");
					// 隐藏所有大图
					$(".picture_box .picture").css("display","none");
					// 根据小图索引得到大图并显示
					var index=$.inArray(this,thumbs);
					$(".picture_box .picture").eq(index).css("display","table-cell");
					return false;
				});
			})
		},
		bindEvent : function() {

		}
	});
	// 组件对外提供使用
	module.exports = Example;
});