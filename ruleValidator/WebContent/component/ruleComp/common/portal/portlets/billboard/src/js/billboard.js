define(function(require, exports, module) {
	var billboard_tpl = require("./billboard.tpl");
	var portlet=require("portlet");
	var handlebars = require("handlebars");
	var $ = require("$");
	require('../theme/default/css/billboard.css');
	var picture=require("picture");
	var Context=require("context");
	/* 菜单栏滑动效果全局变量，值为true时表示滑动效果结束 */
	var slideFinished = true;
	
	handlebars.registerHelper("billboardHtml", function(context, options) {
		var ret="";
		var arrModel = context.model;
		if(!arrModel) return "";
		for(var index=1;index<arrModel.length+1;index++){
		var data = arrModel[index-1];
		var showSaleVolum=Context.get("showSaleVolume");
//		if(data.vminimageurl ==null){
//			data.vminimageurl ="";
//		} 
		if(index < 3){
			ret +=
				'<div class="top10_item">' +
					'<div class="item_detail separ">' +
						'<div class="detail_label font_arial font_bold">'+index+'</div>' +
						'<div class="clear"></div>';
		}else if(index == 3){
			ret +=
				'<div class="top10_item effectTrigger">' +
				   '<div class="name_wrapper hidden">' +
						'<span class="item_sn">'+index+'</span>' +
						'<span class="name">'+data.vname+'</span>' +
				   '</div>' +
				   '<div class="item_detail separ">' +
					   '<div class="detail_label font_arial font_bold">'+index+'</div>' +
					   '<div class="clear"></div>';		
		}else{
			ret +=
				'<div class="top10_item">' +
					'<div class="name_wrapper">' +
						'<span class="item_sn">'+index+'</span>' +
							'<span class="name">'+data.vname+'</span>' +
					'</div>' +
					'<div class="item_detail hidden">' +
							'<div class="sn_wrapper">' +
								'<span class="item_sn">'+index+'</span>' +
						   '</div>';
		}
		ret +=
				'<a href="'+G.PAGE.PRODUCT_DETAIL+data.cproductid+'" target="_blank" class="top10_thumb" ectype="recent" productid="'+data.cproductid+'">' +
							   '<img src="" width="48" height="48" alt="" productid="'+data.cproductid+'" size="50_50" ectype="picture"/>' +
						   '</a>' +
						   '<div class="top10_desc">' +
							   '<span class="desc_title nc-field-long-hide" title="'+data.vname+'">'+data.vname+'</span>';
							   if(showSaleVolum){
								   ret+= '<span scale='+data.pk_measdoc_unitScale+'>'+data.saleVolume+'</span>';
						   		   ret+='<span>'+data.pk_measdoc_name+'</span>';
							   }
							  
						   ret+='</div>' +
					   '</div>' +
				 	'</div>';
		}
		return ret;
	});
	
	var Billboard = portlet.extend({
			initCustAttr:function(){
				this.set("moreurl",this.get("moreurl"));
			},
			portlet_template : billboard_tpl,
			loadData : function() {
				var that=this;
				var url = this.get("url")||G.API.REPORT_TOP10;
				$.ajax({
					url : url,
					success : function(data) {
						that.setModel(data);
						that.bindEvent();
						picture.init();
					},
					dataType : 'json'
				});
			},
			handlebars : handlebars,
			bindEvent : function() {
				$(".name_wrapper").bind('mouseenter', function() {
					if (!slideFinished)
						return false;
					slideFinished = false;
					$(".effectTrigger").children(".item_detail").slideUp("fast");
					$(".effectTrigger").children(".name_wrapper").slideDown("fast",
							function() {
								$(".effectTrigger").removeClass("effectTrigger");
							});
					$(this).slideUp("slow", function() {
								slideFinished = true;
							});
					$(this).next(".item_detail").slideDown("fast", function() {
								$(this).parent(".top10_item").addClass("effectTrigger");
							});
					return false;
				});
			},
			afterRender:function(){
				Billboard.superclass.afterRender.apply(this, arguments);
				seajs.emit("scale");
			}
		});
		
	module.exports = Billboard;
});