/**
 * 订单页面相关js效果，以及其他一些操作。
 * 
 */
define(function(require, exports, module) {
	require('../theme/{theme}/css/order.css');
	require('../theme/{theme}/css/notice.css');
	var $ = require("$");
	$ = require("jquery.extend")($);
	var dialog = require("dialog");
	var shopcartcookie = require("shopcartcookie");
	var OrderStore = require("order_store");
	
	var OrderEffect = function() {
		/**
		 * 切换显示地址栏
		 */
		var barSwitch = function() {
			if ($($(this).parents(".bar_content")).attr("class").indexOf("inside_width") > 0) {
				// 判断是展开还是收起
				if ($(this).parents(".rounded_extended_bar").length > 0) {
					// 如果展开，则收起
					$(this).parents(".info_line").next(".idBarContentExtended").slideUp('slow', function() {
						$(this).parents(".rounded_extended_bar").addClass("rounded_single_bar inside_width").removeClass("rounded_extended_bar");
						$(this).prev(".info_line").find(".trigger").removeClass("up").addClass("down");
						$(this).prev(".info_line").find(".content_info").show();
					});
				} else if ($(this).parents(".rounded_single_bar").length > 0) {
					// 如果收起 则展开
					$(this).parents(".rounded_single_bar").addClass("rounded_extended_bar inside_width").removeClass("rounded_single_bar");
					$(this).removeClass("down").addClass("up");
					$(this).parents(".bar_content").find(".content_info").hide();
					$(this).parents(".info_line").next(".idBarContentExtended").slideDown('slow', function() {
					});
				}
			} else if ($($(this).parents(".bar_content")).attr("class").indexOf("outside_width") > 0) {
				if ($(this).parents(".rounded_extended_bar").length > 0) {
					$(this).parents(".info_line").next(".idBarContentExtended").slideUp('slow', function() {
						$(this).parents(".rounded_extended_bar").addClass("rounded_single_bar inside_width").removeClass("rounded_extended_bar");
						$(this).prev(".info_line").find(".trigger").removeClass("up").addClass("down");
					});
				} else if ($(this).parents(".rounded_single_bar").length > 0) {
					$(this).parents(".rounded_single_bar").addClass("rounded_extended_bar inside_width").removeClass("rounded_single_bar");
					$(this).removeClass("down").addClass("up");
					$(this).parents(".info_line").next(".idBarContentExtended").slideDown('slow', function() {
					});
				}
			}
			return false;
		};

		/**
		 * 增加行焦点效果
		 * <h2> 说明： </h2>
		 * <ul>
		 * <li>在需要增加行焦点效果的table上增加定位标记J-table-click-effect</li>
		 * <li>调用addRowFocusEffect(event)，event只能是mouseenter和click</li>
		 * </ul>
		 */
		this.addRowFocusEffect = function(event) {
			// 表格定位样式
			var tableClass = 'J-table-click-effect';
			// 表格列定位样式
			var tdClass = 'J-td-click-effect';
			// 焦点样式
			var focusClass = 'background-color-lavender';
			// 事件列表
			var events = [ 'mouseenter', 'click' ];

			// 入参检查
			if (!event || !events.join(',').match(event)) {
				event = 'click';
			}

			function addEffect(tr) {
				// 去掉其他行样式
				$(tr).parents('table').find('tr').each(function(iTr, tr) {
					$(tr).find('td').each(function(iTd, td) {
						$(td).removeClass(focusClass);
					});
				});

				$(tr).find('td').each(function(iTd, td) {
					$(td).addClass(focusClass);
				});
			}
			;

			$('tr').live(event, function() {
				if ($(this).hasClass(tableClass)) {
					addEffect(this);
				} else if ($(this).parents('table').hasClass(tableClass)) {
					$(this).addClass(tdClass);
					addEffect(this);
				}
			});
		};

		/**
		 * 初始化
		 */
		this.init = function() {
			$("body").delegate(".bar_content .trigger", "click", barSwitch);
			// 改为orderCore.js中的updateIndex
			//			$("body").delegate("*", "caclNumber", caclOrderNumber);

			/**
			 * 取消订单商品保存到购物车中
			 */
			var orderSaveToShopCart = function() {
				var productIDs = [];
				$("input[name$='.cproductid']").each(function(index, dom) {
					var productid = $(this).val();
					productIDs.push(productid);
				});

				if (!shopcartcookie.exist(productIDs)) {
					var content = '<center>' + '<input type="checkbox" id="saveToShopCartCheckBox" checked="checked"></input>' + '<label style="font-weight:bold;">取消后，商品会保存在购物车内</label>'
							+ '<div class="order_add_btns">' + '</center>';
					var option = {
						title : "取消创建订单",
						content : content,
						isHtmlContent : true,
						confirm : function() {
							if ($("#saveToShopCartCheckBox").attr("checked") == "checked") {
								saveToShopCartCookie();
							} else {
								window.location = G.ctx;
							}
							return false;
						},
						cancel : function() {
							dialog.close();
						}
					};
					dialog.confirm(option);
				} else {
					window.location = G.ctx;
				}
				return false;
			};

			/**
			 * 保存商品进购物车
			 */
			var saveToShopCartCookie = function() {
				// 组装订单行中的商品信息，保存到购物车中
				var cookies = {};
				var total=0;
				$("input[name$='.cproductid']").each(function(index, dom) {
					var tr = $(this).parent().parent();
					var blargessflag = tr.find('input[name$=blargessflag]').val();
					if (blargessflag === 'true') {
						return true;
					}
					var productid = $(this).val();
					var number = tr.find("input[name$='nqtunitnum']").val();
					var object = {
						productid : productid,
						number : number,
						flag : shopcartcookie.NEW
					};
					if (!cookies[productid]) {
						cookies[productid] = object;
					}
					total+=1;
				});
				if(total<=100){
					shopcartcookie.saveCookie(cookies);
				}else{
					 alert("保存至购物车的数量超过购物车上限100，保存失败！");
					 return false;
				}
				window.location = G.ctx;
			};
			// 取消保存到购物车中
			$(".J-order-cancel").bind("click", orderSaveToShopCart);

			// 增加鼠标点击行视觉效果
			this.addRowFocusEffect();
		};

		/**
		 * 创建订单成功后提示
		 */
		this.createOrderSuccess = function(msg, flag) {
			// if (msg.message != undefined && msg.vbillcode == undefined) {
			if (msg.message || msg.detail) {

				var contentHtml = '<p><center><span class="color_red">' + msg.message + '<span></center></p>';
				var isExist = false;
				if ($('.ui-dialog').length > 0) {
					isExist = true;
				}

				if (isExist) {
					// 存在对话框，更新订单内容和按钮
					$('.dialog_content').html(contentHtml);
					var dialog_bottom = "<div class='dialog_pane'>" + "<a class='btn_blue font_hei ET_alert J-dialog-close' style='cursor:pointer'>确定</a>" + "</div>";
					$('.dialog_btm').html(dialog_bottom);
					$('.J-dialog-close').click(function() {
						$('.dialog-widget-overlay').remove();
						$(this).parents('.ui-dialog').remove();
					});
				} else {
					// 不存在对话框，新建一个
					var option = {
						title : "提交订单失败",
						isHtmlContent : true,
						content : contentHtml
					};
					dialog.alert(option);
				}

				return;
			} else {
				var orderTitle = "订单提交成功";
				var orderLink = G.PAGE.ORDER_DETAIL;
				var queryLink = G.PAGE.ORDER_LIST;
				var linkName = "订单查询";
				if (flag == "return") {
					orderTitle = "退货单提交成功";
					orderLink = G.PAGE.ORDER_RETURN_DETAIL;
					queryLink = G.PAGE.ORDER_RETURN_LIST;
					linkName = "退货单查询";
				}
				var length = msg.length;
				var orderlist = "单据号：<br><br>";
				var orderCodes = new Array();
				var productidArray = new Array();
				for ( var i = 0; i < length; i++) {
					var order = msg[i];
					var orderCodeList = orderLink + order.ccustomerpoid + "";
					orderlist = orderlist + "<a class='blue_noline' href='" + orderCodeList + "'>" + order.vbillcode + "</a>&nbsp;&nbsp;";
					// 每行显示三条订单信息
					if ((i + 1) % 3 == 0) {
						orderlist = orderlist + "<br>";
					}
					orderCodes.push(order.vbillcode);
					var orderItems = order.itemUIViews;
					for ( var j = 0; j < orderItems.length; j++) {
						var item = orderItems[j];
						productidArray.push(item.cproductid);
					}
				}
				var orderLink = queryLink;
				if (flag == "return") {
//					orderLink = orderLink + "&returnCodes=" + orderCodes.join(",");
					OrderStore.setCurrentReturnOrderCode(orderCodes.join(","));
				} else {
//					orderLink = orderLink + "&orderCodes=" + orderCodes.join(",");
					OrderStore.setCurrentOrderCode(orderCodes.join(","));
				}
				var option = {
					title : orderTitle,
					hasCloseTip : false,
					isHtmlContent : true,
					content : orderlist,
					buttons : [ {
						name : linkName,
						isDefault : true,
						href : orderLink
					}, {
						name : "返回首页",
						href : G.ctx
					} ]
				};
				dialog.close();
				dialog.dialog(option);
				// 隐藏关闭按钮
				$(".ui-dialog-titlebar-close").focus().hide();
				// 清空购物车cookie
				shopcartcookie.clearCookie(productidArray);
			}
		};
	};

	var orderEffect = new OrderEffect();
	/**
	 * 页面加载默认操作
	 */
	$(function() {
		orderEffect.init();
	});
	module.exports = orderEffect;
});
