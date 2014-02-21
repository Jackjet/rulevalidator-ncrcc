define(function(require, exports, module) {
	// 主题

	require('../theme/default/css/ecpproductgrid.css');
	var $ = require("$");
	var Class = require("class");
	var ecpproductgrid_list_tpl = require("./ecpproductgrid_list.tpl");
	var ecpproductgrid_thumb_tpl = require("./ecpproductgrid_thumb.tpl");
	var ecpproductgrid_detail_tpl = require("./ecpproductgrid_detail.tpl");
	var EcpPageGrid = require("ecppagegrid");
	var shopcartcookie = require("shopcartcookie");
	var favoriteDialog = require("favoritedialog");
	require("recentview");
	var nabnum = require("nabnum");
	var picture = require("picture");
	var defaultimage = require("defaultimage");
	var productPrice = require("productPrice");
	var store = require("store");
	var EcpProductGrid = Class.create(EcpPageGrid, {
		template : ecpproductgrid_detail_tpl,
		initCustAttr : function() {
			if (this.get("isLoadPic") == undefined) {
				this.set("isLoadPic", true);
			}
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
		},
		//绑定后台管理事件
		bindManagementEvent:function(){
			//后台管理重新绑定事件
			var that = this;
			//tab页切换
			$(this.element).find(".J_switchList").die("click").unbind("click").on("click", function() {
				that.setTemplate(ecpproductgrid_list_tpl);
				return false;
			});
			//tab页切换
			$(this.element).find(".J_switchThumb").die("click").unbind("click").on("click", function() {
				that.setTemplate(ecpproductgrid_thumb_tpl);
				return false;
			});
			//tab页切换
			$(this.element).find(".J_switchDetail").die("click").unbind("click").on("click", function() {
				that.setTemplate(ecpproductgrid_detail_tpl);
				return false;
			});
			//后台管理，tab页签显示设置
			var switchtab = $(this.element).find(".switch_view");
			switchtab.append('<input type="checkbox" class="hidenormal"></input><label class="hidenormal">隐藏</label>');
			switchtab.find(".hidenormal").hide().on("change",function(event){
				$(this).parent().hasClass("hidetab")?$(this).parent().removeClass("hidetab"):$(this).parent().addClass("hidetab");
				saveproductgrid($(this).parents(".ui-ecpproductgrid").parent().prop("id"));
			});
			//页签放上后显示隐藏checkbox
			switchtab.hover(
				function(){
					$(this).children().eq(0).nextAll().show();
				},function(){
					$(this).children().eq(0).nextAll().hide();
			});
			//后台管理页面，修改表头名称
			$(this.element).find("thead td").on("click",function(){
				if($(this).find("input").length==0){
					var title = $(this).html();
					$(this).html("<input type='text' value='"+title+"'></input>");
					$(this).find("input[type='text']").focus();
					$(this).find("input[type='text']").on("blur",function(){
						var parent = $(this).parent();
						parent.html($(this).val());
						saveproductgrid(parent.parents(".ui-ecpproductgrid").parent().prop("id"));
					});
				}
			});
			//后台管理页面，点击隐藏checkbox
			$(this.element).find(".hidetd").on("change",function(){
				var td = $(this).parent();
				var hidetd = td.parents("table").find("tr td").eq(td.prevAll().length);
				hidetd.hasClass("backgroundgray")?hidetd.removeClass("backgroundgray"):hidetd.addClass("backgroundgray");
				saveproductgrid(td.parents(".ui-ecpproductgrid").parent().prop("id"));
			});
			//后台管理页面，列表tab页字段显示
			$(this.element).find(".product_properties_hide input[type='checkbox']").on("change",function(){
				saveproductgrid($(this).parents(".ui-ecpproductgrid").parent().prop("id"));
			});
			//后台管理页面，缩略图tab页字段显示
			$(this.element).find(".product_thumb_hide input[type='checkbox']").on("change",function(){
				saveproductgrid($(this).parents(".ui-ecpproductgrid").parent().prop("id"));
			});
			//保存商品列是否显示到store中
			function saveproductgrid(parentDOM){
				var data=store.get("productlist")||{"config_productlist":[],"config_onsalelist":[],"config_report":[],"config_reportlist":[],
						  "tab_productlist":[],"tab_onsalelist":[],"tab_report":[],"tab_reportlist":[],
						  "headname_productlist":[],"headname_onsalelist":[],"headname_report":[],"headname_reportlist":[],
						  "listshow_productlist":[],"listshow_onsalelist":[],"listshow_report":[],"listshow_reportlist":[],
						  "thumb__productlist":[],"thumb_onsalelist":[],"thumb_report":[],"thumb_reportlist":[]
				};				
				data["tab_"+parentDOM]=[];
				//如果是在详细tab页
				if($(".config_"+parentDOM).find("thead td").length>0){
					data["config_"+parentDOM] =[]; 
					data["headname_"+parentDOM]=[];
				}
				//如果是在列表tab页
				if($(".config_"+parentDOM).find(".product_properties_hide").length>0){
					data["listshow_"+parentDOM] =[]; 
				}
				//如果是在缩略图tab页
				if($(".config_"+parentDOM).find(".product_thumb_hide").length>0){
					data["thumb_"+parentDOM] =[]; 
				}
				
				$(".config_"+parentDOM).find("thead td").each(function(i,element){
					//保存“详细”表头显示列
					if($(element).hasClass("backgroundgray")){
						data["config_"+parentDOM].push(false);
					}
					else{
						data["config_"+parentDOM].push(true);
					}
					//保存“详细”列名显示
					if($(element).find("input").length>0){
						data["headname_"+parentDOM].push("<input type='checkbox' class='all-add-to-shopcart'/>");
					}else{
						data["headname_"+parentDOM].push($(element).html());
					}
				});
				//保存“tab”页显示
				$(".config_"+parentDOM).find(".product_list_title a").each(function(i,element){
					if($(element).hasClass("hidetab")){
						data["tab_"+parentDOM].push(false);
					}
					else{
						data["tab_"+parentDOM].push(true);
					}
				});
				//保存列表页显示字段
				$(".config_"+parentDOM).find(".product_properties_hide").each(function(i,element){
					if($(element).find("input[type='checkbox']").prop("checked")){
						data["listshow_"+parentDOM].push(false);
					}
					else{
						data["listshow_"+parentDOM].push(true);
					}
				});
				//保存缩略图tab页显示字段
				$(".config_"+parentDOM).find(".product_thumb_hide").each(function(i,element){
					if($(element).find("input[type='checkbox']").prop("checked")){
						data["thumb_"+parentDOM].push(false);
					}
					else{
						data["thumb_"+parentDOM].push(true);
					}
				});
				store.set("productlist",data);
			};
		},
		//设置表格列是否可见以及tab页是否显示
		drawsetting:function(){
			var that =this;
			if(store.get("productlist")){
				/**
				 * 设置显示列 data:store中相应数据 dom:需要绑定的行 domhtml：页面样式class保证只在相应页面起作用
				 * 方法用于保存个性化设置到dom
				 * data：表格是否显示的data数据，
				 * dom：表格对应的表头tr，
				 * domhtml：当前位于哪个商品列表页面（productlist，onsalelist，report，reportlist）
				 * tabdata：tab标签页是否显示数据
				 * thname：表头的显示名称
				 * tabdom:tab页面所在的dom
				 * listshowdata：详细列表页面的是否隐藏显示的数据
				 */
				function resetproductlist(data,dom,domhtml,tabdata,thname,tabdom,listshowdata,thumbdata){
					//判断商品列表页面还是后台管理页面
					var isManagement = $("body."+domhtml).length>0?false:true;
					for(var i=0;i<data.length;i++){
						if(!data[i]){
							if(!isManagement){
								dom.each(function(){
									$(this).find("td").eq(i).addClass("hidden");
								});
							}else{
								dom.first().find("td").eq(i).addClass("backgroundgray");
								dom.last().find("td").eq(i).find(".hidetd").prop("checked",true);
							}
						}
					}
					//tab页
					for(var i=0;i<tabdata.length;i++){
						if(!tabdata[i]){
							tabdom.find(".product_list_title a").eq(i).addClass("hidetab");
						}
					}
					//列名
					for(var i=0;i<thname.length;i++){
						dom.first().find("td").eq(i).html(thname[i]);
					}
					//列表tab页字段名称
					var listshowname=[];
					if(thname.length>0){
						listshowname.push(thname[0]);//商品编码
						listshowname.push(thname[3]);//基准价
						listshowname.push(thname[4]);//当前价
						listshowname.push(thname[5]);//库存
						listshowname.push(thname[6]);//销量
					}
					//列表页属性显示
					for(var i=0;i<listshowdata.length;i++){
						if(!isManagement){
							//前台页面展示
							tabdom.find(".lists_item").each(function(){
								if(!listshowdata[i]){
									$(this).find(".product_properties div").eq(i).addClass("hidden");
								}
								//同时同步属性名称
								if(listshowname.length>0){
									$(this).find(".product_properties .product_td label").each(function(j,element){
										$(this).html(listshowname[j]+"：");
									});
								}
							});
						}else{
							if(!listshowdata[i]){
								tabdom.find(".product_properties_hide input[type='checkbox']").eq(i).prop("checked",true);
							}
							//同时同步属性名称
							if(listshowname.length>0){
								tabdom.find(".product_properties_hide label").each(function(j,element){
									$(this).html(listshowname[j]+"：");
								});
							}
						}
					}
					//缩略图页属性显示
					for(var i=0;i<thumbdata.length;i++){
						if(!isManagement){
							//前台页面展示
							tabdom.find(".channel_item").each(function(){
								if(!thumbdata[i]){
									$(this).find(".product_desc .J_product_prop").eq(i).addClass("hidden");
								}
								//同时同步属性名称
								if(listshowname.length>0){
									$(this).find(".product_desc .J_product_prop span:first-child").each(function(j,element){
										$(this).html(listshowname[j]+"：");
									});
								}
							});
						}else{
							if(!thumbdata[i]){
								tabdom.find(".product_thumb_hide input[type='checkbox']").eq(i).prop("checked",true);
							}
							//同时同步属性名称
							if(listshowname.length>0){
								tabdom.find(".product_thumb_hide label").each(function(j,element){
									$(this).html(listshowname[j]+"：");
								});
							}
						}
					}
				}
				var data = store.get("productlist");
				var dom;
				var tabdom;
				//四个页面，商品查询，展销商品查询，统计中心，销售排行
				var pagelist =["productlist","onsalelist","report","reportlist"];
				//设置所有查询列表可显示的数据
				for(var i=0;i<pagelist.length;i++){
					//商品查询页面
					if($(".config_"+pagelist[i]).length>0){
						dom = $(".config_"+pagelist[i]+" table tr");
						tabdom =$(".config_"+pagelist[i]);
						resetproductlist(data["config_"+pagelist[i]]||[],dom,"config_"+pagelist[i]||[],data["tab_"+pagelist[i]]||[],data["headname_"+pagelist[i]]||[],tabdom,data["listshow_"+pagelist[i]]||[],data["thumb_"+pagelist[i]]||[]);
					}
				}
			}
			var test = "tett";
		},
		bindEvent : function() {
			var that = this;
			if($(".J-indexproductlist").length){
				this.bindManagementEvent();
			}
			$(".J_switchList").live("click", function() {
				that.setTemplate(ecpproductgrid_list_tpl);
				return false;
			});
			$(".J_switchThumb").live("click", function() {
				that.setTemplate(ecpproductgrid_thumb_tpl);
				return false;
			});
			$(".J_switchDetail").live("click", function() {
				that.setTemplate(ecpproductgrid_detail_tpl);
				return false;
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
			// 给加入收藏夹绑定事件
			$('.add_to_favorite').live('click', function() {
				var button = $(this);
				var productId = $.trim(button.attr('productid'));
				if (!button.hasClass('added')) {
					// 增加保存收藏成功后事件
					window.parent.seajs.on("event_favorite_add_success", function() {
						button.addClass('added');
					});
					window.parent.seajs.emit(G.EVENT.FAVORITE_ADD, productId);
				}
				return false;
			});

			// 给加入购物车绑定事件
			$('.J-cart').live('click', function() {
				// 按钮
				var button = $(this);
				// 商品主键
				var productId = $.trim(button.attr('productid'));
				if(button.parent().hasClass("noprice") 
						|| button.parent().hasClass("nonabnum")){
					return false;
				}
				if (button.hasClass('added')) {
					// 删除
					window.parent.seajs.emit(G.EVENT.SHOPCART_REMOVE, {
						productId : productId
					});
					$('.all-add-to-shopcart').prop("checked", false);
					that.isaddtocart=true;
				} else {
					var buynum = $(this).parents("tr").find("#product_buyNum").val() || 1;
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
			// 响应购物车删除商品后的事件
			window.parent.seajs.on(G.EVENT.SHOPCART_AFTER_CLEARALL, function(productId, amount, callback) {
				for(var i=0;i<productId.length;i++){
					//详细页面
					$(".ui-operate").find("a[productid=" + productId[i] + "]").filter(".add_to_cart").removeClass('added');
					//缩略图页面
					$(".product_desc .add_to_cart[productid=" + productId[i] + "]").removeClass('added');
					//列表页面
					$(".ui-product-grid-operate .add_to_cart[productid=" + productId[i] + "]").removeClass('added');
				}
				$('.all-add-to-shopcart').prop("checked", false);
				that.isaddtocart=true;
			});
			// 对 收藏夹中已经存在的商品加上收藏夹高亮图标
			seajs.on(G.EVENT.FAVORITE_LOADED, function(obj) {
				$(".ui-favorite-add-button-small").each(function() {
					var productid = $(this).attr("productid");
					if (obj[productid]) {
						$(this).addClass("added");
					}
				});
			});
			// 商品没有价格和库存时不能加入购物车
			seajs.on("setPriceSuccess",function(cproductid){
				$("a[productid="+cproductid+"]").parent().removeClass("noprice");
			});
			seajs.on("setNabnumSuccess",function(cproductid){
				$("a[productid="+cproductid+"]").parent().removeClass("nonabnum");
			});
			function removeMark(mark){
				that.element.find("."+mark).removeClass(mark);
			}
			seajs.on(G.EVENT.NABNUM_LOADED,function(){
				removeMark("nabnumloaded");
			});
			seajs.on(G.EVENT.PRICE_LOADED,function(){
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
			seajs.on("ifCheck", ifCheck);
			seajs.on(G.EVENT.NABNUM_LOADED, ifCheck);
			seajs.on(G.EVENT.PRICE_LOADED, ifCheck);
		},
		afterRender : function() {
			var that = this;
			// 图片处理
			picture.init();
			defaultimage.init();
			// 价格处理
			productPrice.init();
			// 库存处理
			nabnum.init();
			favoriteDialog.loadFavorite();
			EcpProductGrid.superclass.afterRender.apply(this, arguments);
			this.drawsetting();			
		}
	});
	module.exports = EcpProductGrid;
});