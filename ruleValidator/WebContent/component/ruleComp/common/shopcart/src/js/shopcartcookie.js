/**
 * 购物车cookie操作接口
 * 购物车中对外提供的接口都在这里处理
 */
define(function(require, exports, module) {

	var $ = require("$");
	$ = require("jquery.cookie")($);
	$ = require("jquery.json2")($);
		var store=require("store");
	var context = require('context');
		
	// 初始化常量
	var ADD_SHOPCART_KEY = "";
	var user = store.get("user");
	if(user){
		ADD_SHOPCART_KEY = 'shopCart|' + user.userId;
	}
	var EXPIRES = 0;
	var PRODUCT_MAX = parseInt(context.get('shopCartMax') || 100);
	var ShopCartCookie = {
		// 表示购物车内商品
		NEW : 0,
		DEL : 1,
		ALERTMARK:false,
		
		/**
		 * 查询商品信息，返回值包括商品名称、编码、库存等信息
		 * para:flag 为查询标记，指查询购物车页签信息或是已删除信息
		 * callback：为回调函数，因为涉及到异步问题
		 */
		getData : function(flag, callback) {
			var that=this;
			var cookies = this.getShopCartCookieArray();
			if(cookies.length>0){
				var productIdArray = new Array();
				for (var i = 0; i < cookies.length; i++) {
					productIdArray.push(cookies[i].productid);
				}
				$.ajax({
					url : G.API.PRODUCTS,
					data : {
						productIds : productIdArray.join(",")
					},
					dataType : 'json',
					success : function(data) {
						if (data == null) {
							data = new Array();
						}
						if (data.message == undefined) {
							var dataMap = new Object();
							for (var i = 0; i < data.length; i++) {
								dataMap[data[i].cproductid] = data[i];
							}
							var newdata = new Array();
							var deldata = new Array();
							var scale = -1;
							for (var i = 0; i < cookies.length; i++) {
								var product = dataMap[cookies[i].productid];
								if (product) {
									product.count = cookies[i].number;
									if (cookies[i].flag == that.NEW) {
										newdata.push(product);
										if (scale == -1) {
											scale = product.corigcurrencyid_amountScale;
										}
									} else {
										deldata.push(product);
									}
								}
							}
							callback(data);
						}
					}
				});
			}else{
				callback(cookies);
			}
		},
		
		/**
		 * 通过cookie查询购物车信息 
		 */
		getShopCartCookieArray : function(flag) {
			//var productCookies = $.cookie(ADD_SHOPCART_KEY);
			var productCookies = store.get(ADD_SHOPCART_KEY);
			if (productCookies == null) {
				return new Array();
			};
			if (!this.isNotEmpty(productCookies))
				return;
			productCookies = this.evalProduct(productCookies);
			var alldata = new Array();
			var newdata = new Array();
			var deldata = new Array();
			for (var productid in productCookies) {
				var productCookie = productCookies[productid];
				if (productCookie.flag == this.NEW) {
					newdata.push(productCookie);
				} else {
					deldata.push(productCookie);
				}
				alldata.push(productCookie);
			}
			if (flag == this.NEW) {
				return newdata;
			} else if (flag == this.DEL) {
				return deldata;
			} else {
				return alldata;
			}
		},
 
		/**
		 * 保存商品值cookies中
		 */
		saveCookie:function(productCookies){
//			$.cookie(ADD_SHOPCART_KEY, $.toJSON(productCookies), {
//				path : '/',
//				expires : EXPIRES
//			});
			store.set(ADD_SHOPCART_KEY,$.toJSON(productCookies));
		},
		
		/**
		 * 获取购物车cookie对象
		 */
		getShopCartCookieObject:function() {
			//var productCookies = $.cookie(ADD_SHOPCART_KEY);
			var productCookies = store.get(ADD_SHOPCART_KEY);
			if (!this.isNotEmpty(productCookies)) {
				return null;
			} else {
				productCookies = this.evalProduct(productCookies);
				return productCookies;
			}
		},
		
		/**
		 * 根据ID判断商品是否存在购物车中
		 */
		exist: function(ids) {
			var flag=true;
			var productCookies = this.getShopCartCookieObject();
			var productidArray={};
//			for(var i=0;i<productCookies.length;i++){
//				productidArray[productCookies[i].productid]=productCookies[i].productid;
//			}
			//cookies不为空
			if(productCookies!=null){
				for (var j = 0; j < ids.length; j++) {
					 if(!productCookies[ids[j]]){
						 flag=false;
						 break;
					 }
				}
			}else{
				flag=false;
			}
			return flag;
		},
		
		/**
		 * 删除购物车cookies ids 删除的商品id数组
		 */
		clearCookie:function(ids) {
			if (ids == null || ids.length == 0 || ids == undefined) {
				return;
			}
			var productCookies = this.getShopCartCookieObject();
			for (var i = 0; i < ids.length; i++) {
				delete productCookies[ids[i]];
			}
			// 保存进cookie
			this.setShopCartCookie(productCookies);
		},
		
		/**
		 * 设置购物车cookies
		 */
		setShopCartCookie: function(productCookies,refreshmark) {
			var count=0;
			for(var i in productCookies){
				count++;
			}
			if(count <= PRODUCT_MAX){
				store.set(ADD_SHOPCART_KEY,$.toJSON(productCookies));
				if(!refreshmark){
					$("div:first").trigger("shopcartRefresh");
				}
				if(count == PRODUCT_MAX){
					seajs.emit("shopcart_full");
				}
				return true;
			}else{
				alert("购物车数量已达上限" + PRODUCT_MAX + "，请删除一些商品后再继续添加！");
				return false;
			}
		},
		
		/**
		 * cookie string转对象
		 */
		evalProduct : function(productCookies) {
			return eval("(" + productCookies + ")");
		},
		

		/**
		 * 判断是否为空
		 */
		isNotEmpty : function(object) {
			if (object == null || object.length == 0 || object == "") {
				return false;
			} else {
				return true;
			}
		}
	}
	module.exports = ShopCartCookie;
});