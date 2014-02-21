define(function(require, exports, module) {
	// 主题
	require('../theme/default/css/favorite.css');
	var $ = require('$');
	var Class = require('class');
	var template_plain = require('./plain.tpl');
	var template_thumb = require('./thumb.tpl');
	var template_list = require('./list.tpl');
	var PageGrid = require('ecppagegrid');
	var ImgResize = require('imgresize');
	require('nabnum');
	var ProductPrice = require('productPrice');
	var picture=require("picture");
	var shopcartcookie = require("shopcartcookie");
	var store=require("store");
	var Dialog=require("dialog");
	
	// 组件定义
	var FavoriteGrid = Class.create(PageGrid, {
		template : template_plain,
		bindEvent : function() {
			var grid = this;
			var that = this;
			$(".J_switchList").live("click", function() {
				grid.setTemplate(template_list);
				top.seajs.emit("event_grid_loaded", 1400);
				return false;
			});
			$(".J_switchThumb").live("click", function() {
				grid.setTemplate(template_thumb);
				top.seajs.emit("event_grid_loaded", 1000);
				return false;
			});
			$(".J_switchDetail").live("click", function() {
				grid.setTemplate(template_plain);
				top.seajs.emit("event_grid_loaded", 800);
				return false;
			});
			
			$('.ui-favorite-btn-remove').live('click', function() {
				var a = $(this);
				var productId = $.trim(a.attr('productid'));
				
				// 提示用户
				/*Dialog.confirm({
					type:'confirm',
					title:'确认对话框',
					content:'<center>确认要删除该收藏吗？</center>',
					isHtmlContent:true,
					confirm:function() {
						$.ajax({
							type : "POST",
							url : G.API.ACCOUNT_FAVORITE_DELETE,
							dateType : "json",
							data : {productIds:productId},
							success : function(data) {
								grid.query();
								var favorite=store.get("favorite");
								delete favorite[productId];
								store.set("favorite",favorite);
								top.seajs.emit('event_dialog_close');
							}
						});
					}
				});*/
				Dialog.confirm({
					title : "确认对话框",content : '<center>确认要删除该收藏吗？</center>',
					isHtmlContent:true,
					moveToInner:true,
					confirm : function() {
						$.ajax({
							type : "POST",
							url : G.API.ACCOUNT_FAVORITE_DELETE,
							dateType : "json",
							data : {productIds:productId},
							success : function(data) {
								grid.query();
								var favorite=store.get("favorite");
								delete favorite[productId];
								store.set("favorite",favorite);
								Dialog.close();
							}
						});
					},
					cancel : function() {
						return true;
					}
				});
				return false;
			});
			
			// 全部删除
			$('.btn_all_delete_from_favorite').live('click', function() {
				var btns = $(this).parents('table').find('.ui-favorite-btn-remove');
				var productIds = [];
				btns.each(function(i, item) {
					var btn = $(item);
					productIds.push($(btn).attr('productid'));
				});

				// 提示用户
				top.seajs.emit('event_dialog', {
					type:'confirm',
					title:'确认对话框',
					content:'<center>确认要删除本页全部收藏吗？</center>',
					isHtmlContent:true,
					confirm:function() {
						$.ajax({
							type : "POST",
							url : G.API.ACCOUNT_FAVORITE_DELETE,
							dateType : "json",
							data : {productIds:productIds.join(',')},
							success : function(data) {
								grid.query();
								store.set("favorite",{});
								top.seajs.emit('event_dialog_close');
							}
						});
					}
				});
			});
			
			// 给批量加入按钮绑定事件
			$('.all-add-to-shopcart').live('click', function() {
				var checked = $(this)[0].checked;
				if (checked) {
					$('.J-cart').each(function(i, item) {
						if(that.isaddtocart){
							if (!$(item).parent().hasClass('nonabnum') && !$(item).parent().hasClass('noprice')) {
								if (!$(item).hasClass('added')) {
									$(item).trigger('click');
								}
							}
						}else{
							alert("购物车数量已达上限100，请删除一些商品后再继续添加！");
							$(".all-add-to-shopcart").prop("checked",false);
							return false;
						}
					});
				} else {
					$('.J-cart').each(function(i, item) {
						if ($(item).hasClass('added')) {
							$(item).trigger('click');
						}
					});
					that.isaddtocart=true;
				}
				return true;
			});
			// 购物车
			// FIXME 缺少一致性的处理，应该和购物车中的商品保持一致性
			$('.J-cart').live('click', function() {
				// 按钮
				var button = $(this);
				// 商品主键
				var productId = $.trim(button.attr('productid'));
				if($(this).parent().hasClass("noprice") 
						|| $(this).parent().hasClass("nonabnum")) return false;
				if (button.hasClass('added')) {
					// 删除
					window.parent.seajs.emit(G.EVENT.SHOPCART_REMOVE, {productId : productId});
					that.isaddtocart=true;
					$('.all-add-to-shopcart').prop("checked", false);
				} else {
					var buynum = $(this).parents("tr").find("#product_buyNum").val()||1;
					// 新增
					window.parent.seajs.emit(G.EVENT.SHOPCART_ADD, {
								id : productId,
								number : buynum
							});
					seajs.emit("ifCheck");
				}
				return false;
			});
			window.parent.seajs.off(G.EVENT.SHOPCART_AFTER_CLEARALL);
			window.parent.seajs.off("add_to_cart_success");
			window.parent.seajs.off("remove_cart_success");
			window.parent.seajs.off("shopcart_full");
			//响应购物车删除商品后的事件
			window.parent.seajs.on(G.EVENT.SHOPCART_AFTER_CLEARALL, function(productId, amount, callback) {
				for(var i=0;i<productId.length;i++){
					$(".ui-operate").find("a[productid=" + productId[i] + "]").filter(".add_to_cart").removeClass('added');
				}
				$('.all-add-to-shopcart').prop("checked", false);
				that.isaddtocart=true;
			});

			function ifCheck(){
				that.element.find(".J-cart").each(function(){
					if($(this).parent().hasClass("nonabnum") || $(this).parent().hasClass("noprice")){
						$(this).attr("title","暂无库存或价格，不能加入购物车");
					}else{
						if (shopcartcookie.exist([ $(this).attr("productid") ])) {
							$(this).removeClass("ET_addToCart").addClass("added").addClass("ET_delFromShopCart").attr("title","从购物车删除");
						}else{
							$(this).attr("title","加入购物车");
						}
					}
				});
				
				var addedsize = $(".add_to_cart.added").size();
				var totalsize = $(".add_to_cart").parent("div[class='']").size();
				if (addedsize == totalsize && addedsize!=0) {
					$('.all-add-to-shopcart').prop("checked", true);
				} else {
					$('.all-add-to-shopcart').prop("checked", false);
				}
			}
			function removeMark(mark){
				that.element.find("."+mark).removeClass(mark);
			}
			seajs.on("ifCheck", ifCheck);
			// 商品没有价格和库存时不能加入购物车
			seajs.on("setPriceSuccess", function(cproductid) {
				$("a[productid=" + cproductid + "]")
						.filter(".add_to_cart").parent().removeClass("noprice");
			});
			seajs.on("setNabnumSuccess", function(cproductid) {
				$("a[productid=" + cproductid + "]")
						.filter(".add_to_cart").parent().removeClass("nonabnum");
			});
			seajs.on(G.EVENT.NABNUM_LOADED, function(){
				ifCheck();
				removeMark("nabnumloaded");
			});
			seajs.on(G.EVENT.PRICE_LOADED, function(){
				ifCheck();
				removeMark("priceloaded");
			});
			
			window.parent.seajs.on("add_to_cart_success",function(productid){
				$(".ET_addToCart[productid ='" + productid + "']")
					.removeClass("ET_addToCart").addClass("added")
					.addClass("ET_delFromShopCart").attr("title","从购物车删除");
			});
			window.parent.seajs.on("remove_cart_success",function(productids){
				$(".ET_delFromShopCart[productid='"+productids[0]+"']").removeClass("added")
					.removeClass("ET_delFromShopCart")
					.addClass("ET_addToCart").attr("title", "加入购物车");
			});
			
			window.parent.seajs.on("shopcart_full",function(){
				that.isaddtocart=false;
			});
		},
		initCustAttr:function() {
			var productCookies=shopcartcookie.getShopCartCookieObject();
			var count = 0;
			for(var i in productCookies){
				count++;
			}
			if(count>=100){
				this.isaddtocart=false;
			}else{
				this.isaddtocart=true;
			}
			this.setParams({
				isDesc:true
			});
		},
		afterRender : function() {
			var grid = this;
			var records=grid.model.records || [];
			var favorite={};
			for(var i=0;i<records.length;i++){
				var key=records[i].productId;
				favorite[key]=key;
			}
			store.set("favorite",favorite);
			picture.init();
			// 查询价格
			ProductPrice.init();
			
			// 查询库存
			$('span[ectype]').eq(0).trigger('doEctype');
//			// 购物车
//			$(".add_to_cart").eq(0).trigger("doAdded");
			
//			$('body').delegate('.all-add-to-shopcart', 'click',addToCartInDetail);
//			$(document.body).delegate('.all-add-to-shopcart', 'click', function() {
//				$(this).parents('table').find('.add_to_cart').each(function() {
//					$(this).trigger('click');
//				});
//			});
			// 对购物车中已有的商品在商品详细、缩略图、列表中图标高亮显示
			this.element.find(".add_to_cart").each(function() {
				if(shopcartcookie.exist([$(this).attr("id")])) {
					$(this).addClass("added");
				}
			});
			
			//商品展示列表
			this.element.find(".product_list_thumbs img").each(
					function(index, dom) {
						dom.onload = ImgResize.resize(this, 169, 110);
					});
			this.element.find(".lists_item img").each(function(index, dom) {
				dom.onload = ImgResize.resize(this, 100, 80);
			});
			
			var targetColumn = $('.ui-grid-order');
			var thumb = targetColumn.find('a');
			var arrow = targetColumn.find('.arrow span');
			var o = this;
			
			var isDesc = o.get('isDesc');
			if (isDesc) {
				arrow.removeClass('up').addClass('down');
				thumb.attr('order', 'desc');
			} else {
				arrow.removeClass('down').addClass('up');
				thumb.attr('order', 'asc');
			}
			
			
			thumb.click(function() {
				
				// 当前排序
				var order = thumb.attr('order') || 'desc';
				// 改变排序图表
				if (order && order == 'desc') {
					// 当前排序是降序 => 改为升序排列
					o.set('isDesc', false);
					arrow.removeClass('down').addClass('up');
					o.setParams({
						isDesc:false
					});
				} else {
					// 当前排序是升序 => 改为降序排列
					o.set('isDesc', true);
					arrow.removeClass('up').addClass('down');
					o.setParams({
						isDesc:true
					});
				}
				// 重新查询
				o.query();
			});
		},
	});

	module.exports = FavoriteGrid;
});