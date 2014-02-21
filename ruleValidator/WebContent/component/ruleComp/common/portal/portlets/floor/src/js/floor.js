define(function(require, exports, module) {
	var $ = require("$");
	// 默认引入：组件继承自widget
	var Widget = require("widget");
	// 自定义：组件对应的css
	require('../theme/default/css/floor.css');
	// 图片宽度自适应
	var ImgResize = require("imgresize");
	// 默认图片处理
	var defaultImage = require("defaultimage");
	// 自定义：组件的html模板
	var template = require("./floor.tpl");
	//继承portlet
	var Portlet=require("portlet");
	// 库存处理
	var nabnum = require("nabnum");
	var shopcartcookie=require("shopcartcookie");
	var productPrice=require("productPrice");
	var handlebars = require("handlebars");
	// 组件的定义 组件名大写
	var Channel = Portlet.extend({
		handlerbars:handlebars,
		portlet_template : template,
		loadData : function(params) {
			var channel = this;
			var url = this.get("url");
			var id = this.get("parentNode").attr("id");
			var attrs = this.get("attrs");
			params = params||{};
			if(attrs.type =="new"){
				params.fpromotionflag ="1";
			}else if(attrs.type =="special"){
				params.fpromotionflag ="2";
			}
			$.ajax({
				type : 'GET',
				url : url,
				success : function(data) {
					channel.setModel(data);
				},
				dataType : 'json',
				data : params
			});
		},
		bindEvent : function() {
			$(".channel_item").append($('<div></div>').addClass("clear"));
		},
		beforeRender:function(){
			
		},
		afterRender : function() {
			Channel.superclass.afterRender.apply(this, arguments);
			var that=this;	
//			for(var i=0;i<this.model.length;i++){
//				var id=this.model[i].cproductid;
//				if(shopcartcookie.exist([id])){
//					$(".ET_addToCart[productid ='" + id + "']").removeClass("add_to_cart").removeClass("ET_addToCart").addClass("added")
//						.addClass("ET_delFromShopCart").attr("title","从购物车删除");
//				}
//			}
			//价格处理
			productPrice.init();
			// 库存处理
			nabnum.init();
			//默认图片处理
			defaultImage.init();

			seajs.on("setPriceSuccess",function(cproductid){
				$("a[productid="+cproductid+"]").removeClass("noprice");
			});
			seajs.on("setNabnumSuccess",function(cproductid){
				$("a[productid="+cproductid+"]").removeClass("nonabnum");
			});
			function removeMark(mark){
				that.element.find("."+mark).removeClass(mark);
			}
			seajs.on(G.EVENT.NABNUM_LOADED,function(){
				removeMark("nabnumloaded");
				that.element.find(".J-cart").each(function(){
				     if($(this).hasClass("nonabnum") || $(this).hasClass("noprice")){
				      $(this).attr("title","暂无库存或价格，不能加入购物车");
				     }else{
				      if (shopcartcookie.exist([ $(this).attr("productid") ])) {
				       $(this).removeClass("ET_addToCart").addClass("added").addClass("ET_delFromShopCart").attr("title","从购物车删除");
				      }else{
				       $(this).attr("title","加入购物车");
				      }
				     }
				    });
			});
			seajs.on(G.EVENT.PRICE_LOADED,function(){
				removeMark("priceloaded");
				that.element.find(".J-cart").each(function(){
				     if($(this).hasClass("nonabnum") || $(this).hasClass("noprice")){
				      $(this).attr("title","暂无库存或价格，不能加入购物车");
				     }else{
				      if (shopcartcookie.exist([ $(this).attr("productid") ])) {
				       $(this).removeClass("ET_addToCart").addClass("added").addClass("ET_delFromShopCart").attr("title","从购物车删除");
				      }else{
				       $(this).attr("title","加入购物车");
				      }
				     }
				    });

			});
			seajs.on(G.EVENT.SHOPCART_AFTER_CLEARALL,function(){
				$(".add_to_cart").removeClass("added");
			});
			
			this.element.find(".J-cart").click(function(){
				if($(this).hasClass("noprice") 
						|| $(this).hasClass("nonabnum")){
					return false;
				}else{
					if($(this).hasClass("added")){
						seajs.emit(G.EVENT.SHOPCART_REMOVE,{productId:$(this).attr("productid")});
						$(this).removeClass("added");
						$(this).addClass("add_to_cart");
						return false;
					}else{
						seajs.emit(G.EVENT.SHOPCART_ADD,{id:$(this).attr("productid"),number:1});
						return false;
					}
				}
			});
			this.element.find("img").each(function(){
				$(this).attr("src",$(this).attr("src1"));
			});
		},
		initCustAttr:function(){
			var attrs = this.get("attrs");
			attrs.url=G.API.ONSALE;
			attrs.more = true;
			if(attrs.type =="new"){
				attrs.title="新品推介";
				/*attrs.moreLink=G.PAGE.ONSALE_NEW;*/
				attrs.moreurl=G.PAGE.ONSALE_NEW;
			}else if(attrs.type =="special"){
				attrs.title="特价促销";
				/*attrs.moreLink=G.PAGE.ONSALE_SPECIAL;*/
				attrs.moreurl=G.PAGE.ONSALE_SPECIAL;
			}else{
				
			}
		}
	});
	// 组件对外提供使用
	module.exports = Channel;
});