/*
 * 组件示例
 */

define(function(require, exports, module) {

	// 默认引入：组件继承自widget
	var Widget = require("widget");
	// 主题
	
	require('../theme/default/css/simpleshopcart.css');
	// 自定义：组件的html模板
	var template = require("./shopcart.tpl");
	// jquery 插件
	var $ = require("$");
	$ = require("jquery.cookie")($);
	$ = require("jquery.jtemplates")($);
	$ = require("jquery.custompara")($);
	$ = require("jquery.poshytip")($);
	$ = require("jquery.json2")($);
	
	var dialog=require("dialog");
	
	// 日志
	var logger = require("log");
	var Maths = require("maths");
	var commonCacl = require('cacl');
	var autoCompleteAdd = require('productAutoCompleteAdd');
	var dialog = require('dialog');
	require("nabnum");
	// 组件的定义 组件名大写
	var ShopCart = Widget.extend({
		template : template,
		afterRender : function() {
			// cookie key
			var ADD_SHOPCART_KEY = "";
			// cookie 过期时间
			var EXPIRES = 0;

			// 初始化常量
			ADD_SHOPCART_KEY = 'shopCart|' + G.userid;
			EXPIRES = new Date(new Date().getTime() + 7 * 24 * 60 * 60 * 1000); // 7天过期

			// 新增购物车页签商品
			var NEW = 0;// 表示购物车内商品
			var DEL = 1;// 表示购物车内已删除商品
			exports.NEW = NEW;
			exports.DEL = DEL;
			/**
			 * 对外接口，添加进购物车
			 */
			exports.add = function(productid) {
				addShopCart($(this), productid);
			};
			/**
			 * 对外接口，刷新购物车
			 */
			exports.refresh = function() {
				refreshShopCartContent();
			};
			/**
			 * 对外接口：设置已添加到购物车的商品样式
			 */
			exports.setAdded = function() {
				filterAddedProd();
			};
			/**
			 * 对外接口：清空购物车cookie
			 */
			exports.clearCookie = function(ids) {
				delShopCartCookie(ids);
			};
			/**
			 * 对外接口：设置购物车cookie
			 */
			exports.saveCookie = function(cookies) {
				setShopCartCookie(cookies);
			};

			/**
			 * 对外接口：校验商品是否可购买
			 */
			exports.validateProduct = function(target, option) {
				validateProduct(target, option);
			};

			/**
			 * 添加进购物车
			 */
			var addShopCart = function(event, productid_para) {
				var target = $(this);
				var productid = productid_para;
				if (productid == undefined) {
					productid = target.attr("productid");
				}
				var option = {};
				option.price = $("#product_price" + productid).attr("value");
				option.totalNum = $("#product_nnabnum" + productid).text();
				option.bspotflag = $("#product_nnabnum" + productid)
						.attr("bspotflag");
				option.buyNum = $("#product_buyNum" + productid).val();

				if (validateProduct(target, option)) {
					addProductToShopCart(productid, option.buyNum);
				};
				return false;
			};
			/**
			 * 从购物车删除商品
			 */
			var delFromShopCart = function(event) {
				var target = $(this);
				productid = target.attr("productid");
				var ids = [productid];
				// 从cookie中删除
				delShopCartCookie(ids);
				// 修改图标样式
				filterAddedProd();
				return false;
			}

			/**
			 * 从删除页签添加进购物车 不需要校验
			 */
			var addDelToCart = function() {
				var target = $(this);
				productid = target.attr("productid");
				addProductToShopCart(productid);
				return false;
			};
			/**
			 * 商品详细页面添加进购物车
			 */
			var addToCartInDetail = function() {
				var target = $(this);
				var productid = target.attr("productid");
				var option = {};
				option.price = $("#salePrice").text();
				option.totalNum = $("#product_nnabnum" + productid).text();
				option.bspotflag = $("#product_nnabnum" + productid)
						.attr("bspotflag");
				option.buyNum = $("#numberInput").val();

				var number = $("#numberInput").val();
				productid = target.attr("productid");
				if (validateProduct(target, option)) {
					addProductToShopCart(productid, number, function() {
								noticeBubble('userNameError', target, 185,
										'up', "成功加入购物车");
							});
				};
				return false;
			};

			/**
			 * 校验商品可否加入购物车
			 */
			var validateProduct = function(target, option) {
				var price = option.price;// 价格
				var totalNum = option.totalNum;// 库存
				var buyNum = option.buyNum;// 购买商品的数量
				var bspotflag = option.bspotflag;// 无货是否可下单
				if (price == null || price == "" || price == "null") {
					noticeBubble('userNameError', target, 185, 'up',
							"当前客户对应的商品没有商品价目<br/>表信息，暂时不可购买");
					return false;
				}
				if ((totalNum <= 0 || totalNum == "" || totalNum == "null" || totalNum == "无")
						&& ("N" == bspotflag || "false" == bspotflag)) {
					noticeBubble('userNameError', target, 210, 'up',
							"当前商品暂时缺货，暂时不可购买");
					return false;
				}
				if ((parseFloat(buyNum) > parseFloat(totalNum))
						&& ("N" == bspotflag || "false" == bspotflag)) {
					noticeBubble('userNameError', target, 210, 'up',
							"当前商品库存数量不足，暂时不可购买");
					return false;
				}
				return true;
			};

			/**
			 * 删除购物车cookies ids 删除的商品id数组
			 */
			var delShopCartCookie = function(ids) {
				if (ids == null || ids.length == 0 || ids == undefined) {
					return;
				}
				var productCookies = getShopCartCookieObject();
				for (var i = 0; i < ids.length; i++) {
					delete productCookies[ids[i]];
				}
				// 保存进cookie
				setShopCartCookie(productCookies);
			};
			/**
			 * 添加商品到购物车
			 */
			var addProductToShopCart = function(productid, number, callback) {
				if (number == undefined) {
					number = 1;
				}

				var productCookie = {
					productid : productid,
					flag : NEW,
					number : number
				};

				var productCookies = getShopCartCookieObject();
				// 如果为空则创建一个
				if (productCookies == null) {
					productCookies = new Object();
				}

				productCookies[productid] = productCookie;

				// 保存进cookie
				setShopCartCookie(productCookies);
				// 回调
				if (callback == undefined) {
					// 默认回调为更改图标
					filterAddedProd();
				} else {
					callback();
				}
				// 刷新购物车信息
				refreshShopCartContent();
			};

			/**
			 * 判断是否为空
			 */
			var isNotEmpty = function(object) {
				if (object == null || object.length == 0 || object == "") {
					return false;
				} else {
					return true;
				}
			};
			/**
			 * 获取购物车cookie对象 flag == new 返回购物车内商品 flag == del 返回删除页签的商品 否则返回所有
			 */
			var getShopCartCookieArray = function(flag) {
				var productCookies = $.cookie(ADD_SHOPCART_KEY);
				if (productCookies == null) {
					return new Array();
				};
				if (!isNotEmpty(productCookies))
					return;
				productCookies = evalProduct(productCookies);
				var alldata = new Array();
				var newdata = new Array();
				var deldata = new Array();
				for (var productid in productCookies) {
					var productCookie = productCookies[productid];
					if (productCookie.flag == NEW) {
						newdata.push(productCookie);
					} else {
						deldata.push(productCookie);
					}
					alldata.push(productCookie);
				}
				if (flag == NEW) {
					return newdata;
				} else if (flag == DEL) {
					return deldata;
				} else {
					return alldata;
				}
			};

			/**
			 * 获取购物车cookie对象
			 */
			var getShopCartCookieObject = function() {
				var productCookies = $.cookie(ADD_SHOPCART_KEY);
				if (!isNotEmpty(productCookies)) {
					return null;
				} else {
					productCookies = evalProduct(productCookies);
					return productCookies;
				}
			};
			/**
			 * 设置购物车cookies
			 */
			var setShopCartCookie = function(productCookies) {
				$.cookie(ADD_SHOPCART_KEY, $.toJSON(productCookies), {
							path : '/',
							expires : EXPIRES
						});
				refreshShopCartContent();
			};
			/**
			 * 通过id获取cookie中商品信息
			 */
			var getShopCartCookieById = function(productid) {
				var cookieObject = getShopCartCookieObject();
				return cookieObject[productid];
			};
			/**
			 * cookie string转对象
			 */
			var evalProduct = function(productCookies) {
				return eval("(" + productCookies + ")");
			};

			/**
			 * 初始化购物车数量
			 */
			var initShopCartCount = function() {
				var productCookies = getShopCartCookieArray(NEW);
				$(".cart_amount").children(".amount_no")
						.html(productCookies.length);
			};

			/**
			 * 设置已添加商品样式
			 */
			var filterAddedProd = function() {
				var productCookies = getShopCartCookieObject();
				$(".ET_delFromShopCart").each(function() {
					$(this).removeClass("added")
							.removeClass("ET_delFromShopCart")
							.addClass("add_to_cart").addClass("ET_addToCart")
							.attr("title", "加入购物车");
				});
				for (var prod in productCookies) {
					$(".ET_addToCart[productid ='" + prod + "']")
							.removeClass("add_to_cart")
							.removeClass("ET_addToCart").addClass("added")
							.addClass("ET_delFromShopCart").attr("title",
									"从购物车删除");
					// 同步商品列表数量
					$("#product_buyNum" + prod)
							.val(productCookies[prod].number);
				};
			};
			/**
			 * 展示购物车
			 */
			var renderShopCart = function(data) {
				var dataMap = new Object();
				for (var i = 0; i < data.length; i++) {
					dataMap[data[i].cproductid] = data[i];
				}
				var newdata = new Array();
				var deldata = new Array();
				var cookies = getShopCartCookieArray();
				var scale = -1;
				for (var i = 0; i < cookies.length; i++) {
					var product = dataMap[cookies[i].productid];
					if (product) {
						product.count = cookies[i].number;
						if (cookies[i].flag == NEW) {
							newdata.push(product);
							if (scale == -1) {
								scale = product.corigcurrencyid_amountScale;
							}
						} else {
							deldata.push(product);
						}
					}
				}
				// 通过模板展示页面
				// 购物车页签
				$('#shopCartLabel').setTemplateElement('templet_shopCartLabel')
						.processTemplate(newdata);
				// 已删除页签
				$('#shopCartDelLabel')
						.setTemplateElement('templet_shopCartDelLabel')
						.processTemplate(deldata);
				// 设置页签商品数量
				$(".idCartListAmount").text(newdata.length);
				$(".idCartDelListAmount").text(deldata.length);
				// 更改总购物车数量
				initShopCartCount();
				$(".ET_caclTotal").attr("scale", scale);
				// 获取库存
				$("span[ectype]:first").trigger("doEctype");
				// 计算合计
				commonCacl.cacl();
				// 展示自动补全添加插件
				autoCompleteAdd.init({
							callback : autoCompleteCallback
						});
			};
			/**
			 * 自动补全添加商品回调函数
			 */
			var autoCompleteCallback = function(json) {
				// 先把更改的商品数量保存到cookie中
				saveShopCartToCookie();
				// 添加到cookie中刷新购物车
				var productid = json.cproductid;
				var count = json.count;
				var cookie = getShopCartCookieById(productid);
				// 如果已经存在，则提示是否继续添加,数量累加
				if (cookie != undefined) {
					if (cookie.flag == NEW) {
						if (confirm("该商品已在购物车中，确认继续添加？")) {
							count = Maths.add(count, cookie.number);
							addProductToShopCart(productid, count);
						} else {
							refreshShopCartContent();
						}
					} else {
						if (confirm("该商品在购物车已删除中，确认添加到购物车？")) {
							addProductToShopCart(productid, count);
						} else {
							refreshShopCartContent();
						}
					}
				} else {
					addProductToShopCart(productid, count);
				}
			};

			/**
			 * 显示购物车
			 */
			var showShopCart = function() {
				if ($(".panelCartList").length > 0) {
					if ($(".panelCartList").is(":hidden")) {
						$("#panelCartList").show();
					} else {
						closeShopCart();
					}
				} else {
					var target = $(this);
					// 设置购物车显示位置
					$('<div></div>').addClass("panelCartList")
							.html($("#panelCartList")).insertBefore(target)
							.css({
										position : "absolute",
										zIndex : 600,
										marginTop : 20
									})
							.after($('<div></div>').addClass("clear"));
					$(".panelCartList").show();
					refreshShopCartContent();
				}
				return false;
			};
			/**
			 * 获取并显示购物车内容
			 */
			var refreshShopCartContent = function() {
				var productCookies = getShopCartCookieArray();
				var productIdArray = new Array();
				for (var i = 0; i < productCookies.length; i++) {
					productIdArray.push(productCookies[i].productid);
				}
				$.ajax({
							url : G.API.PRODUCTS,
							data : {
								productIds : productIdArray.join(",")
							},
							dataType : 'json',
							success : function(data) {
								if (!data) {
									data = new Array();
								}
								renderShopCart(data);
							}
						});
			};
			/**
			 * 关闭购物车
			 */
			var closeShopCart = function() {
				$("#panelCartList").hide();
				// 保存数据到购物车
				saveShopCartToCookie();
				return false;
			};
			/**
			 * 删除购物车行
			 */
			var delShopCartRow = function() {
				var target = $(this);
				var option = {
					title : "删除商品",
					content : "确认删除该商品？",
					confirm : function() {
						// 先把更改的商品数量保存到cookie中
						saveShopCartToCookie();
						// 更改cookie中商品状态
						var productCookies = getShopCartCookieObject();
						var productid = target.attr("productid");
						productCookies[productid].flag = DEL;
						setShopCartCookie(productCookies);
						return false;
					},
					cancel : function() {
						return true;
					}
				};
				dialog.confirm(option);
				return false;
			};
			/**
			 * 切换购物车页签
			 */
			var switchShopCartTab = function() {
				var currentTab = $(this).attr("href");
				$(".ET_switchShopCartList").each(function() {
					var tabId = $(this).attr("href");
					if (tabId == currentTab) {
						if ($(tabId).is(':hidden')) {
							$(tabId).show();
							$(this).removeClass("label_normal")
									.addClass("label_active");
						}
					} else {
						$(tabId).hide();
						$(this).removeClass("label_active")
								.addClass("label_normal");
					}
				});
			};
			/**
			 * 删除购物车页签
			 */
			var clearShopCart = function() {
				var option = {
					title : "删除商品",
					content : "确认删除所有购物车商品？",
					confirm : function() {
						var cookies = getShopCartCookieObject();
						for (var productid in cookies) {
							cookies[productid].flag = DEL;
						}
						setShopCartCookie(cookies);
						return false;
					},
					cancel : function() {
						return true;
					}
				};
				dialog.confirm(option);
			};
			/**
			 * 清空已删除页签
			 */
			var clearDelShopCart = function() {
				var option = {
					title : "删除商品",
					content : "确认清空已删除商品？",
					confirm : function() {
						var cookies = getShopCartCookieObject();
						for (var cookie in cookies) {
							if (cookies[cookie].flag == DEL) {
								delete cookies[cookie];
							}
						}
						setShopCartCookie(cookies);
						// 重置已购标志
						filterAddedProd();
						return false;
					},
					cancel : function() {
						return true;
					}
				};
				dialog.confirm(option);
			};
			/**
			 * 去结算
			 */
			var checkOut = function() {
				// 保存商品到cookie中
				saveShopCartToCookie();
				// 寄存购物车商品到数据库中
				// depositShopCart(function() {
				var url = G.API.ORDER_ADD;
				window.open(url);
				// });
				return false;
			};
			/**
			 * 保存到cookie中
			 * 
			 */
			var saveShopCartToCookie = function() {
				var productCookies = {};
				// 如果购物车页面已经渲染，保存数据
				if ($("#shopCartLabel tbody").length != 0) {
					$("#shopCartLabel tbody  tr").each(function() {
						var productid = $(this).attr("productid");
						var number = $(this).find(".ET_shopcart_saveNumber")
								.val();
						var cookie = {
							productid : productid,
							number : number,
							flag : NEW
						};
						productCookies[productid] = cookie;
					});
					$("#shopCartDelLabel tbody  tr").each(function() {
								var productid = $(this).attr("productid");
								var cookie = {
									productid : productid,
									number : 0,
									flag : DEL
								};
								productCookies[productid] = cookie;
							});
					// 保存进cookie
					setShopCartCookie(productCookies);
				}
			};
			/**
			 * 寄存购物车到服务器
			 */
			var depositShopCart = function(callback) {
				// 获取cookie数据
				var shopCartCookies = getShopCartCookieArray(NEW);
				var shopCartArray = new Array();
				for (var i = 0; i < shopCartCookies.length; i++) {
					var shopCartCookie = shopCartCookies[i];
					var shopCart = new Object();
					shopCart.cproductid = shopCartCookie.productid;
					shopCart.nqtunitnum = shopCartCookie.number;
					shopCart.isDel = shopCartCookie.flag;
					shopCartArray.push(shopCart);
				}
				var param = {
					shopCartUIVIews : shopCartArray
				};
				$.ajax({
							type : 'POST',
							url : G.API.SHOPCART_CACHE,
							data : $.customParam(param),
							dataType : 'json',
							success : function(data) {
								if (data.message == undefined) {
									callback(data);
									closeShopCart();
								} else {
									var option={
										title:"提示",
										content:data.message
									}
									dialog.alert(option);
								}
							},
							error : function(data) {
								//console.log(data);
							}
						});
			};

			var init = function() {
				// 基础设置把a标签默认行为去掉
				$(".cart_checkout a").bind("click", function() {
							return false;
						});
				// 遍历已经添加进购物车的商品，修改样式
				filterAddedProd();
				// 初始化购物车商品数量
				initShopCartCount();
				// 添加商品到购物车（通过小图标）
				$("body").delegate(".ET_addToCart", "click", addShopCart);
				// 从删除页签添加到购物车
				$("body").delegate(".ET_addDelToCart", "click", addDelToCart);
				// 订单详细页面添加到购物车
				$("body").delegate(".ET_addToCartInDetail", "click",
						addToCartInDetail);

				// 从购物车删除商品
				$("body").delegate(".ET_delFromShopCart", "click",
						delFromShopCart);
				// 代理设置商品已添加事件
				$("body").delegate(".add_to_cart", "doAdded", filterAddedProd);
				// 展现购物车
				$(".ET_showShopCart").bind('click', showShopCart);
				// 展现购物车
				$(".ET_checkOut").bind('click', checkOut);
				// 关闭购物车
				$(".ET_closeShopCart").bind('click', closeShopCart);

				// 删除购物车商品
				$("body").delegate(".ET_delShopCartRow", "click",
						delShopCartRow);
				// 切换购物车页签
				$("body").delegate(".ET_switchShopCartList", "hover",
						switchShopCartTab);
				// 删除购物车页签
				$("body").delegate(".ET_clearShopCart", "click", clearShopCart);
				// 清空已删除页签
				$("body").delegate(".ET_clearDelShopCart", "click",
						clearDelShopCart);

				// 计算模块初始化(给input绑定金额联动计算)
				commonCacl.init();
			};
			init();
		}
	});
	// 组件对外提供使用
	module.exports = ShopCart;
});