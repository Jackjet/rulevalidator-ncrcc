define(function(require, exports, module) {
	var $ = require("$");
	var extendgrid_tpl = require("./extendgrid.tpl");
	var handlebars = require("handlebars");
	var Grid=require("grid");
	require("../theme/default/css/extendgrid.css");
	var scale=require("scale");
	var productPrice=require("productPrice");
	var shopcartcookie=require("shopcartcookie");
	var nabnum=require("nabnum");
			
	handlebars.registerHelper("extendgrid", function(context, options) {
		var ret = "";
		if (context.model) {
			var records = context.model;
			var columns = context.attrs.columns;
			var subcolumns = context.attrs.subcolumns;
			var extendName = context.attrs.extendName;
			for (var i = 0; i < records.length; i++) {
				ret += "<tr>";
				for (var j = 0; j < columns.length; j++) {
					var data = records[i];
					var colId = columns[j].key;
					var value=data[colId];
					var format=columns[j].format;
					if(format){
						ret+=format(value,data);
					}else{
						ret += "<td class='J-Click J-"+colId+"'>"+(value || "")+"</td>";
					}
				}
				ret += "</tr>";
			}
		}
		return ret;
	});

	var ExtendGrid = Grid.extend({
		initCustAttr : function() {
			var productCookies=shopcartcookie.getShopCartCookieObject();
			var count = 0;
			for(var i in productCookies){
				count++;
			}
			if(count>=6){
				this.isaddtocart=false;
			}else{
				this.isaddtocart=true;
			}
		},
		loadData : function(params) {
			var extendgrid=this;
			var autoRender = this.get("autoRender");
			if (autoRender) {
				var url = this.get("url");
				extendgrid.setStatus("加载数据中...");
				$.ajax({
					type : 'GET',
					url : url,
					success : function(data) {
						extendgrid.setModel(data);
						if(!data || data.length==0){
							extendgrid.setStatus("没有查询到符合条件的数据");
						}
					},
					error:function(){
						extendgrid.setStatus("数据加载失败！");
					},
					dataType : 'json',
					data : params
				});
			}
		},
		setStatus : function(status) {
			this.element.find(".J-extend-status").html(status);
		},
		query : function(params) {
			this.set("autoRender", true);
			this.loadData(params);
		},
		template : extendgrid_tpl,
		bindEvent:function(){
			ExtendGrid.superclass.initCustAttr.apply(this, arguments);
			var extendgrid=this;
			var isExtend = this.get("isExtend");
			if (isExtend) {
				$(".J-Click").live('click',function() {
					var HiddenDetail = $(this).parent("tr").next(".detail");
					if ($(HiddenDetail).hasClass("Hidden")) {
						$(HiddenDetail).show();
						$(HiddenDetail).removeClass("Hidden");
					} else {
						$(HiddenDetail).addClass("Hidden");
						$(HiddenDetail).hide();
					}
					$(".detail").not(HiddenDetail).find(".extendcontent").parents(".detail").addClass("Hidden");
					$(".detail").not(HiddenDetail).find(".extendcontent").parents(".detail").hide();
				});
			}
			var events=extendgrid.get("events");
			if(events){
				var length=events.length;
				for(var i=0;i<length;i++){
					var event=events[i];
					var trigger=event.trigger;
					var selector=event.selector;
					var handler=event.handler;
					$(selector).live(trigger,handler);
				}
			}
			window.parent.seajs.off(G.EVENT.SHOPCART_AFTER_CLEARALL);
			window.parent.seajs.off("add_to_cart_success");
			window.parent.seajs.off("remove_cart_success");
			window.parent.seajs.off("shopcart_full");
			window.parent.seajs.on(G.EVENT.SHOPCART_AFTER_CLEARALL, function(productId, amount, callback) {
				for(var i=0;i<productId.length;i++){
					$("."+productId[i]).removeClass('added').removeClass("ET_delFromShopCart")
					.addClass("ET_addToCart").attr("title", "加入购物车");;
				}
				extendgrid.isaddtocart=true;
			});
			seajs.on("setPriceSuccess",function(cproductid){
				$("."+cproductid).removeClass("noprice");
				$("."+cproductid).removeClass("hidden");
			});
			seajs.on("setNabnumSuccess",function(cproductid){
				$("."+cproductid).removeClass("nonabnum");
			});
			seajs.on("setPriceFailure",function(cproductid){
				$("."+cproductid).removeClass("hidden");
				$("."+cproductid).attr("title","暂无库存或价格，不能加入购物车");
			});
			seajs.on("setNabnumFailure",function(cproductid){
				$("."+cproductid).removeClass("hidden");
				$("."+cproductid).attr("title","暂无库存或价格，不能加入购物车");
			});
			window.parent.seajs.on("add_to_cart_success",function(productid){
				$(".ET_addToCart[productid ='" + productid + "']")
					.removeClass("ET_addToCart").addClass("added")
					.addClass("ET_delFromShopCart").attr("title","从购物车中删除");
			});
			window.parent.seajs.on("remove_cart_success",function(productids){
				$(".ET_delFromShopCart[productid='"+productids[0]+"']").removeClass("added")
					.removeClass("ET_delFromShopCart")
					.addClass("ET_addToCart").attr("title", "加入购物车");
			});
			window.parent.seajs.on("shopcart_full",function(){
				extendgrid.isaddtocart=false;
			});
			function ifCheck(index){
				if(index){
					var addedsize = $(".detail").eq(index).find('.add_to_cart.added').size();
					var totalsize = $(".detail").eq(index).find('.add_to_cart').size()
						-$(".detail").eq(index).find('.add_to_cart[class*=no]').size();
					if (addedsize == totalsize && addedsize!=0) {
						$('.all-add-to-shopcart').eq(index).prop("checked", true);
					} else {
						$('.all-add-to-shopcart').eq(index).prop("checked", false);
					}
				}else{
					$(".detail").each(function(i,item){
						var addedsize = $(item).find('.add_to_cart.added').size();
						var totalsize = $(item).find('.add_to_cart').size()-$(item).find('.add_to_cart[class*=no]').size();
						if (addedsize == totalsize && addedsize!=0) {
							$('.all-add-to-shopcart').eq(i).prop("checked", true);
						} else {
							$('.all-add-to-shopcart').eq(i).prop("checked", false);
						}
					});
				}
			}
			seajs.on("ifCheck", ifCheck);
			seajs.on(G.EVENT.NABNUM_LOADED, function(){
				productPrice.init();
			});
			seajs.on(G.EVENT.PRICE_LOADED, ifCheck);
		},
		afterRender:function(){
			$(".J-Click").last().parent().css("border-bottom","1px solid #e5e5e5");
			var extendAll = this.get("extendAll");
			if (!extendAll) {
				$(".detail").hide();
			}
			scale.init();
		},
		setChild:function(clickTR,records){
			var extendName=this.get("extendName");
			var ret="";
			var gridHtml = "";
			ret += "<tr class='detail Hidden'>";
			ret += "<td colspan='"+ this.get("columns").length+ "'>";
			ret += "<div class='extendcontent'>";
			ret +="<table>";
			var isSubTitle=this.get("isSubTitle");
			var subcolumns=this.get("subcolumns");
			if(isSubTitle){
				gridHtml+="<thead>";
				gridHtml += "<tr class='bdbtm'>";
				for(var n=0;n<subcolumns.length;n++){
					gridHtml += "<td class='column'>"+subcolumns[n].sublabel+"</td>";
				}
				gridHtml += "</tr>";
				gridHtml+="</thead>";
				gridHtml+="<tbody>";
			}
			if(records){
				for (var m = 0; m < records.length; m++) {
					var temp = records[m];
					gridHtml += "<tr class='bdbtm'>";
					for (var k = 0; k < subcolumns.length; k++) {
						var format=subcolumns[k].format;
						if(format){
							gridHtml+=format(temp[subcolumns[k].subkey],temp);
						}else{
							gridHtml += "<td class='column J-"+subcolumns[k].subkey+"'>";
							gridHtml +=temp[subcolumns[k].subkey] || "";
							gridHtml +="</td>";
						}
					}
					gridHtml += "</tr>";
				}
			}
			gridHtml+="</tbody>";
			ret += gridHtml;
			ret +="</table>";
			ret += "</div>";
			ret += "</td>";
			ret+="</tr>";
			$(clickTR).after(ret);
			var HiddenDetail=$(clickTR).next();
			this.afterRender();
			$(HiddenDetail).show();
			$(HiddenDetail).removeClass("Hidden");
			return HiddenDetail;
		}
	});
	module.exports = ExtendGrid;
});