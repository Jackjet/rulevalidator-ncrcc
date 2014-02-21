define(function(require, exports, module) {
	var category_tpl = require("./category.tpl");
	var Widget = require("widget");

	var $  = require("$");
	$ = require("jquery.extend")($);
	require('../theme/{theme}/css/category.css');
	require("math.uuid");
	
	var slideFinished = true;
	
	var Category = Widget.extend({
		template:category_tpl,
		initCustAttr:function(){
			this.set("collapse",this.get("collapse")=="open" ? "open" : "close");
			this.set("iscollapse",this.get("iscollapse")=="false" ? "false" : "true");
		},
		loadData : function() {
			var Category = this;
			var url = this.get("url")||G.API.CATEGORY;
			if(url.indexOf("G.API")>=0){
				url = eval(url);
			}
			$.ajax({
				url : url,
				success : function(data) {
					if(data =="gotoLogin"){
						alert(123);
					}
					Category.setModel(data);
				},
				dataType : 'json'
			});
		},
		afterRender : function() {
			if(this.get("iscollapse")=="true"){
				//将内容区的样式设置为不占位置
				this.element.addClass("ui-category-relative");
				this.element.find(".ui-category-content").addClass("ui-category-absolute");
				if(this.get("collapse")=="open"){
					this.element.find(".J-arrow").addClass("ui-category-collapse-icon-open");
				}else{
					this.element.find(".J-arrow").addClass("ui-category-collapse-icon-close");
				}
			}
			
			var searchUrl = this.get("searchUrl") || G.PAGE.PRODUCT_PRODUCTLIST+"&id=";
			var defaultHandler = function(target){
				var id = $(target).attr("id");
				window.location = searchUrl+id;
				return false;
			};
			var handler = this.get("handler") || defaultHandler;
			$(".J-ProductList").die("click").live("click",function(event){
					var target = event.currentTarget;
					$(target).trigger("categoryChange",target);
					handler(target);	
			});
			var lastMenu = $('div.sidebox_title.item_seperator').last();
			lastMenu.removeClass('item_seperator');

			var firstMenu = $('div.sidebox_title').first();
			if (firstMenu.find('a.trigger.category_down')) {
				firstMenu.find('a.trigger.category_down').removeClass('category_down').addClass('category_up');
			}

			var two = $('div.sidebox_inner_top ul.category_list');
			$.each(two, function(index, item){
				$(item).find('li.category_2nd_li').last().addClass('last_row');
			});
			var lastSubmenu = $('div.floatrelated.sidebox_inner_content.effectSlide').first();
			if (lastSubmenu) {
				lastSubmenu.removeClass("hidden");
				var firstMenu = $('div.sidebox_title.item_seperator').first();
				firstMenu.removeClass('item_seperator');
			}
			if(this.get("collapse")=="close"){
				$(".J-category-content").hide();
			}
		},
		bindEvent : function() {
			$(".category_down").live('click', function(event) {
				$(".category_up").trigger("click");
				slideCategory(this, "category_down", "category_up");
				return false;
			});

			/**
			 * 整体华东展开
			 */
			$(".category_up").live('click', function(event) {
				$(".item_seperator").css({
					'backgroundPosition' : "bottom"
				});
				if ($(this).parent(".sidebox_title").next(".effectSlide").length > 0) {
					var trigger = this;
					$(this).parent(".sidebox_title").next(".sidebox_inner_content").slideUp('slow', function() {
						
						$(trigger).removeClass("category_up").addClass("category_down");
						if ($(trigger).parent(".sidebox_title").next(".sidebox_inner_content").next(".sidebox_title").length > 0)
							$(trigger).parent(".sidebox_title").addClass("item_seperator");
					});
				} else {
					slideCategory(this, "category_up", "category_down");
				}
			});

			/**
			 * 整体关闭和展开
			 */
			function slideCategory(trigger, class1, class2) {
//				return;
				if (!slideFinished)
					return false;
				slideFinished = false;
				$(".category_top").children(".effectSlide").slideUp('', function() {
					if ($(".effectSlide").next(".sidebox_title").length > 0)
						$(".effectSlide").prev(".sidebox_title")
								.addClass("item_seperator");
					$(".effectSlide").prev(".sidebox_title").children("a.category_up")
							.removeClass("category_up").addClass("category_down");
					$(".effectSlide").removeClass("effectSlide");
				});
				$(trigger).parent(".sidebox_title").removeClass("item_seperator");
				$(trigger).parent(".sidebox_title").next(".sidebox_inner_content")
						.slideDown('', function() {
									$(trigger).removeClass(class1).addClass(class2);
									$(this).die('click', slideCategory);
									$(this).addClass("effectSlide");
									slideFinished = true;
								});
				$(".subMenuWrapper").remove();
				return true;
			}

			/**
			 * 二级分类滑动展开
			 */
			$(".category_2nd_a").live('mouseenter', function() {
				var marginleft=$(this).position().left;
				if (window.screen.width <= 1024) {
					marginleft+=160;
				}else{
					marginleft+=210;
				}
				$('<div></div>').css({
					position : "absolute",
					zIndex : 500,
					marginTop : 0,
					marginLeft : marginleft
				}).addClass("subMenuWrapper").html($(this).next(".hidden")
				.children().clone()).live('mouseleave click', function() {
					$(this).next(".clear").remove();
					$(this).remove();
				}).insertBefore($(this)).after($('<div></div>')
				.addClass("clear"));
				return false;
			});


			/**
			 * 二级分类滑动关闭
			 */
			$(".category_2nd_li").live('mouseleave', function() {
				$(".subMenuWrapper").next(".clear").remove();
				$(".subMenuWrapper").remove();
				return false;
			});
			var that=this;
			if(this.get("iscollapse")=="true"){
				if(G.isMobile){
					$(".J-collapse").live("touchstart",function(){
						$(".J-category-content").toggle();
						if($(".J-category-content:visible").length>0){
							that.element.find(".J-arrow").addClass("ui-category-collapse-icon-open");
							that.element.find(".J-arrow").removeClass("ui-category-collapse-icon-close");
						}else{
							that.element.find(".J-arrow").removeClass("ui-category-collapse-icon-open");
							that.element.find(".J-arrow").addClass("ui-category-collapse-icon-close");
						}
						return false;
					});
				}else{
					$(".J-collapse").live("mouseenter",function(){
						$(".J-category-content").show();
						that.element.find(".J-arrow").removeClass("ui-category-collapse-icon-close");
						that.element.find(".J-arrow").addClass("ui-category-collapse-icon-open");
						return false;
					});
					$(".ui-category-btm").live("mouseleave",function(){
						$(".J-category-content").hide();
						that.element.find(".J-arrow").removeClass("ui-category-collapse-icon-open");
						that.element.find(".J-arrow").addClass("ui-category-collapse-icon-close");
						return false;
					});
				}
			}
		}
	});
	module.exports = Category;
});