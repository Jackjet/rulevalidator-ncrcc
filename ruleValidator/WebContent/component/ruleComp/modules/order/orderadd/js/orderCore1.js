/**
 * 订单核心js
 */
define(function(require, exports, module) {
	var $ = require("$");
	$ = require("jquery.autocomplete")($);
	$ = require("jquery.ui.datepicker")($);

	var shopCartCookies = require("shopcartcookie");
	var Grid = require("ecpgrid");
	var Posdialog = require("posdialog");
	var dialog = require("dialog");
	var stringUtils = require("stringUtils");
	var nabnum = require("nabnum");
	var handlebars = require('handlebars');
	var scale = require("scale");
	var maths = require("maths");
	var formatter = require("formatter");
	var validator = require('validator');
	var OrderPrice = require('order_return_price');
	var StringUtils = require('stringUtils');
	var tips = require('tips');
	var context = require("context");
	var orderValidator = require('order_validate');
	var returnOrderValidator = require('order_return_validate');
	var ECP_CTX = G.ctx;

	var OrderCore = function() {
		
		/**
		 * 地址删除
		 */
		var closeAddress = function() {
			var addresses = $(this).parents('.J-order').find('.J-order-address');
			if (addresses.length > 0) {
				if (addresses.length == 1) {
					if (confirm('这是最后一个地址了，确定删除吗？')) {
						$(this).parents('.J-order-address').remove();
					}
				} else {
					if (confirm('您确定删除该收货地址吗？')) {
						$(this).parents('.J-order-address').remove();
					}
				}
			}
			updateIndex();
			return false;
		};
		
		/**
		 * 新增地址
		 */
		var orderAddressAdd = function(flag) {
			// 收起其他地址
			slideUpOtherAddress();
			// 计算当前收货信息条数量
			var addressBars = $(".idAddressBar").length + 1;
			// 收货地区
			var customerArea = $(".customerArea").clone();
			// 收货地址
			var customerAddress = $(".customerAddress").clone();
			var contentTitleLabel = "收货信息";
			var customerAreaLabel = "收货地区：";
			var customerAddressLabel = "收货地址：";
			var dreceivedateLabel = "期望到货日期：";
			var showShopCart = true;
			var addProductMenuClass = "idOrderAdd";
			var orderItemLine = $('<tr></tr>').append($('<td></td>').html("商品编码")).append($('<td></td>').html("商品名称")).append($('<td></td>').html("物料")).append($('<td></td>').html("销售组织")).append(
					$('<td></td>').html("价格")).append($('<td></td>').html("数量")/* .addClass("w_150px") */).append($('<td></td>').html("库存")).append($('<td></td>').html("金额")).append(
					$('<td></td>').html("操作"));
			// 退货
			if (flag && flag == "return") {
				contentTitleLabel = "退货信息";
				customerAreaLabel = "退货地区";
				customerAddressLabel = "退货地址";
				dreceivedateLabel = "预计发货日期：";
				showShopCart = false;
				addProductMenuClass = "J-return-order-add-product";
				orderItemLine = $('<tr>').append($('<td>').html("商品编码")).append($('<td>').html("商品名称")).append($('<td>').html("物料")).append($('<td>').html("销售组织")).append($('<td>').html("价格"))
						.append($('<td>').html("数量").addClass("w_150px")).append($('<td>').html("金额")).append($('<td>').html("退货原因")).append($('<td>').html("类型")).append($('<td>').html("操作"));
			}

			var option = {
				"addressBars" : addressBars,// 地址索引
				"contentTitleLabel" : contentTitleLabel,// 地址总标题
				"customerAreaLabel" : customerAreaLabel,// 地区label
				"customerAddressLabel" : customerAddressLabel,// 地址label
				"dreceivedateLabel" : dreceivedateLabel,// 日期label
				"customerArea" : customerArea,// 收货地区
				"customerAddress" : customerAddress, // 收货地址
				"showShopCart" : showShopCart,// 是否显示从购物车添加
				"addProductMenuClass" : addProductMenuClass,// 动态添加商品按钮的class
				"orderItemLine" : orderItemLine,
				'isReturn' : flag && flag == 'return' ? true : false
			// 商品列表列头
			};
			$(this).parent(".order_btn_wrapper").before(generateAddressTemplate(option));
			// 初始化自动补全插件
			initAutoComplete();
			// 地址
			initAutoAddress();
			// 更新索引
			updateIndex();
			// 初始化日历控件
			seajs.emit("datepicker");
			return false;
		};
		/**
		 * 生成收货地址模板
		 */
		var generateAddressTemplate = function(data) {
			// 获得数据
			var addressBars = data.addressBars;// 地址
			var contentTitleLabel = data.contentTitleLabel;// 地址总标题
			var customerAreaLabel = data.customerAreaLabel;// 地区label
			var customerAddressLabel = data.customerAddressLabel;// 地址label
			var dreceivedateLabel = data.dreceivedateLabel;// 日期label
			var customerArea = data.customerArea;// 收货地区
			var customerAddress = data.customerAddress;// 收货地址
			var showShopCart = data.showShopCart;// 是否显示从购物车添加
			var addProductMenuClass = data.addProductMenuClass;// 动态添加商品按钮的class
			var orderItemLine = data.orderItemLine;// 订单体列头
			var isReturn = data.isReturn;
			// 期望收获日期
			var userid=store.get("user").userId;
			var dreceivedates=store.get("dreceivedate") || {};
			var dreceivedate = dreceivedates[userid] || 7;
			// 计算数据
			var name_index = addressBars - 1;
			var customerAreaOptions = customerArea[0].innerHTML;
			var creceiveareaid_name = customerArea.find("option:first").text();
/**			原代码保留 @author Zhongming Xu @date 2013-09-05 PM 04:00
			var customerAddressOptions = customerAddress[0].innerHTML;
*/
			var creceiveaddrid_name = customerAddress.find("option:first").text();

			// 地址详细地址部分
			var address_info = $('<div></div>').addClass("address_info")
					/**
					 原代码保留
					 @DATE 2013-09-17 PM 01:59
					 @AUTHOR Zhongming Xu
					.append($('<label></label>').html(customerAreaLabel)).append($('<div></div>')
					.append($('<select></select>').attr({name : "addressUIView[" + name_index + "].creceiveareaid"})
					.append(customerAreaOptions)))
					*/
						
					.append($('<label></label>').attr({'for' : "address" + addressBars}).html(customerAddressLabel))
/**			
  原保留代码
  @AUTHOR Zhongming Xu
  @DATE 2013-08-20 PM 05:57
			.append($('<div></div>').append($('<input></input>').attr({
				.append($('<select></select>')})
					.addClass("textfiled_address user_input_address")
					.append(customerAddressOptions))
*/
			/**
			 * 收货地址文本框
			 * @AUTHOR Zhongming Xu
			 * @DATE 2013-08-20 PM 05:57
			 */
			.append($('<div></div>').append($("<div></div>")
					.append($('<input></input>')
						.attr({'name' : "addressUIView[" + name_index + "].creceiveaddrid_name"})
						.attr({'id' : "receiveaddr[" + name_index + "]"})
						.attr({'placeholder' : "请输入车号 / 行驶证"})
						.addClass("textfiled_address user_input_address"))
					.append($('<a></a>')
						.attr({'href' : "#"})
						.addClass("btn_Address_lookup A_search"))
					.append($('<a></a>')
						.attr({'href' : "#"})
						.addClass("btn_Address_add J-address"))
					.append($('<input></input>')
						.attr({'id' : "receiveaddr[" + name_index + "]"})
						.attr({'name' : "addressUIView[" + name_index + "].creceiveaddrid"})
						.addClass("hidden addressPK"))))

			.append($('<label></label>')
				.attr({'for' : "address" + addressBars})
				.html(dreceivedateLabel))
			.append($('<div></div>')
					.append($('<input data-datepicker-defaultDate="' + dreceivedate + '" style="width:120px;"/>')
					.attr({name : "addressUIView[" + name_index + "].dreceivedate"}).addClass("textfiled datepicker").attr("data-datepicker-minDate", "0")))
			.append($('<label></label>').html("商品清单： "))
			.append($('<div></div>').append($('<span></span>').addClass("color_red J-product-type").attr({id : "amound" + addressBars}).html(0)).append($('<span></span>').html(" 种")));
			// 商品列
			if (orderItemLine == undefined) {
				orderItemLine = $('<tr></tr>').append($('<td></td>').html("商品编码")).append($('<td></td>').html("商品名称")).append($('<td></td>').html("物料")).append($('<td></td>').html("销售组织")).append(
						$('<td></td>').html("价格")).append($('<td></td>').html("数量").addClass("w_150px")).append($('<td></td>').html("库存")).append($('<td></td>').html("金额")).append(
						$('<td></td>').html("操作"));
			}
			// 地址详细商品列表
			var product_table = $('<table></table>').attr({
				border : 1,
				cellpadding : 0,
				cellSpacing : 0
			}).append($('<thead class="align_center"></thead>').append(orderItemLine)).append($('<tbody></tbody>'));
			// 地址详细底部左部
			var list_btm_left = $('<div></div>').addClass("floatleft").append($('<label></label>').append($('<input />').addClass("textfiled add_input_name").val("输入商品名称 / 编码"))).append(
					$('<label></label>').append($('<input />').addClass("textfiled add_input_amount").val(1))).append($('<a href="#"></a>').addClass("btn_add").addClass(addProductMenuClass));
			// 从购物车添加
			if (showShopCart) {
				list_btm_left.append($('<a href="#"></a>').addClass("btn_gray_long addProductFromCart").html("从购物车添加"));
			}
			// 地址详细底部右部
			var list_btm_right;
			if (isReturn) {
				var html = '<div class="list_total ui-order-total-price J-order-return-total-price-address-footer">' + '<span class="ui-order-total-price-label">合计金额： </span>'
						+ '<span class="ui-order-price-return-symbol">[退]</span>' + '<span class="ui-order-price-return-number" scale="4">0</span>'
						+ '<span class="ui-order-price-exchange-symbol">[换]</span>' + '<span class="ui-order-price-exchange-number" scale="4">0</span>' + '</div>';
				list_btm_right = $(html);
			} else {
				list_btm_right = $('<div></div>').addClass("list_total font_yahei").append($('<span></span>').addClass("total_title font_currency").html("合计金额： ")).append(
						$('<span></span>').addClass("total_price").attr("scale", "4").html("0.00")).append(
						$('<span></span>').addClass("list_clear").append("[ ").append($('<a href="#"></a>').html("清空")).append(" ]"));
				// <div class="list_total ui-order-total-price
				// J-order-total-price-address-footer">
				// <span class="total_title">合计金额：</span>
				// <span class="ui-order-price-number" scale="4"></span>
				// <span class="list_clear">
				// <a href="#">[清空]</a>
				// </span>
				// </div>
				var html = '<div class="list_total ui-order-total-price J-order-total-price-address-footer">' + '<span class="total_title">合计金额： </span>'
						+ '<span class="ui-order-price-number" scale="4"></span>' + '<span class="list_clear">' + '<a href="#">[清空]</a>' + '</span>' + '</div>';
				list_btm_right = $(html);
			}

			// 地址详细底部
			var list_btm = $('<div></div>').addClass("list_btm").append(list_btm_left).append(list_btm_right);
			// 详细地址信息
			var idBarContentExtended = $('<div></div>').addClass("idBarContentExtended w_100ps mrg_b5px").append(address_info).append(product_table).append(list_btm);
			// 折叠起来的地址信息
			var info_line = '';
			if (isReturn) {
				addressHeaderTotalPrice = '<div class="w_300px ui-order-total-price J-order-return-total-price-address-collapse">' + '<span class="ui-order-total-price-label">合计金额： </span>'
						+ '<span class="ui-order-price-return-symbol">[退]</span>' + '<span class="ui-order-price-return-number" scale="4">0</span>'
						+ '<span class="ui-order-price-exchange-symbol">[换]</span>' + '<span class="ui-order-price-exchange-number" scale="4">0</span>' + '</div>';
				info_line = $('<div></div>').addClass("info_line").append($('<a href="#"></a>').addClass("trigger up")).append(
						$('<div></div>').addClass("content_name font_bold").append(contentTitleLabel).append($("<span>").addClass("index").text(addressBars))).append(
						$('<div></div>').addClass("content_info hidden").append(
								$('<div></div>').addClass("w_130px").append($('<span></span>').html("商品清单： ")).append($('<span></span>').addClass("color_red J-product-type").attr({
									id : "amound" + addressBars
								}).html(0)).append($('<span></span>').html(" 种"))).append(addressHeaderTotalPrice).append(
								$('<div></div>').addClass("w_320px").attr({
									id : "address" + addressBars
								}).append($('<span></span>').addClass("creceiveareaid_name").append(creceiveareaid_name)).append(
										$('<span></span>').addClass("creceiveaddrid_name").append(creceiveaddrid_name)))).append($('<a href="#"></a>').addClass("icon_delete"));
			} else {
				// <div class="w_180px ui-order-total-price
				// J-order-total-price-address-collapse">
				// <span class="ui-order-total-price-label">合计金额：</span>
				// <span class="ui-order-price-number" scale="4">0</span>
				// </div>
				addressHeaderTotalPrice = '<div class="w_180px ui-order-total-price J-order-total-price-address-collapse">' + '<span class="ui-order-total-price-label">合计金额： </span>'
						+ '<span class="ui-order-price-number" scale="4">0</span>' + '</div>';
				info_line = $('<div></div>').addClass("info_line").append($('<a href="#"></a>').addClass("trigger up")).append(
						$('<div></div>').addClass("content_name font_bold").append(contentTitleLabel).append($("<span>").addClass("index").text(addressBars))).append(
						$('<div></div>').addClass("content_info hidden").append(
								$('<div></div>').addClass("w_130px").append($('<span></span>').html("商品清单： ")).append($('<span></span>').addClass("color_red J-product-type").attr({
									id : "amound" + addressBars
								}).html(0)).append($('<span></span>').html(" 种"))).append(addressHeaderTotalPrice).append(
								$('<div></div>').addClass("w_320px").attr({
									id : "address" + addressBars
								}).append($('<span></span>').addClass("creceiveareaid_name").append(creceiveareaid_name)).append(
										$('<span></span>').addClass("creceiveaddrid_name").append(creceiveaddrid_name)))).append($('<a href="#"></a>').addClass("icon_delete"));
			}

			// 地址栏内容 给地址设置索引
			var bar_content = $('<div></div>').addClass("bar_content inside_width").append(info_line).append(idBarContentExtended);
			// 地址栏
			var idAddressBar = $('<div></div>').addClass("rounded_extended_bar inside_width idAddressBar J-order-address").append($('<div></div>').addClass("bar_corner lt")).append(
					$('<div></div>').addClass("bar_border inside_width top")).append($('<div></div>').addClass("bar_corner rt")).append(bar_content).append($('<div></div>').addClass("bar_corner lb"))
					.append($('<div></div>').addClass("bar_border inside_width btm")).append($('<div></div>').addClass("bar_corner rb"));
			return idAddressBar;
		};
		/**
		 * 从购物车添加
		 */
		var addFromCart = function(event) {
			addProd2ShopCart(event);
			// 订单有效性检验
			// orderValidator.all();
			return false;
		};

		/**
		 * 商品加入订单购物车
		 */
		var addProd2ShopCart = function(event) {
			shopCartCookies.getData(shopCartCookies.NEW, function(data) {
				var modelData = {
					total : data.length,
					records : data
				};

				var grid = new Grid({
					model : modelData,
					attrs : {
						columns : [
								{
									label : "商品编码",
									key : "vcode",
									align : 'left',
									format : function(value, row) {
										return StringUtils.format('<a href="%s/html/product/product.html?id=%s" target="_blank" >%s</a>', [ G.ctx, row.cproductid, value ]);
									}
								},
								{
									label : "商品名称",
									key : "vname",
									align : 'left',
									format : function(value, row) {
										return StringUtils.format('<a href="%s/html/product/product.html?id=%s" target="_blank" >%s</a>', [ G.ctx, row.cproductid, value ]);
									}
								},
								{
									label : "物料",
									align : 'left',
									key : "cinventoryid_name"
								},
								{
									label : "销售组织",
									align : 'left',
									key : "saleOrgName"
								},
								{
									label : "价格",
									key : "price",
									align : 'right',
									format : function(value, row) {
										return StringUtils.format('<span class="ft_yahei">%s</span><span class="caclPrice" scale="%s">%s</span>', [ row.corigcurrencyid_curSign,
												context.get("priceScale"), value ]);
									}
								},
								{
									label : "数量",
									key : "vcode",
									align : 'left',
									format : function(value, row) {
										return StringUtils.format('<input class="textfiled shopCart_caclSummy" value="%s" scale="%s"/><span>%s</span>', [ row.count, row.pk_measdoc_unitScale,
												row.pk_measdoc_name ]);
									}
								},
								{
									label : "库存",
									key : "nnabnum",
									align : 'right',
									format : function(value, row) {
										return StringUtils.format('<span class="norigtaxmny" ectype="nabnum" unit="%s" productid="%s"scale="%s"></span>', [ row.pk_measdoc_name, row.cproductid,
												row.pk_measdoc_unitScale ]);
									}
								},
								{
									label : "金额",
									key : "vcode",
									align : 'right',
									format : function(value, row) {
										return StringUtils.format('<span class="ft_yahei">%s</span><span class="norigtaxmny" scale="%s">%s</span>', [ row.corigcurrencyid_curSign,
												row.corigcurrencyid_amountScale, row.price * row.count ]);
									}
								}, {
									label : "删除",
									key : "vcode",
									format : function() {
										return '<a href="#" class="deleteOrderRow">[删除]</a>';
									}
								} ],
						ismulti : true
					}
				});

				var p = $(event.currentTarget);
				var position = p.position();
				var posiDialog = new Posdialog({
					attrs : {
						title : "添加订单",
						buttons : [ {
							text : "确认",
							style : "blue",
							handler : function() {
								// 地址
								var address = p.parents('.J-order-address');
								// 商品信息（批量）
								var products = grid.getSelectedData();
								// 选择的行（批量）
								var selectedRows = grid.getSelectedRow();
								if (data.length > 0) {
									// var tbody
									// =
									// p.parent().parent().parent().parent();
									// var tbody
									// =
									// p.parents('.J-order-address').find('tbody');
									for ( var i = 0; i < products.length; i++) {
										// 商品信息
										var product = products[i];
										// 选择的行数据
										var selectedRow = selectedRows[i];
										// 报价数量
										var number = parseFloat(selectedRow.find('.shopCart_caclSummy').val());
										product.count = number;
										// 增加商品行
										addProductItem([ product ], null, address, "order", function(tr) {
											$(tr).find('.J-order-row-amount').val(number);
										});
										// 计算金额
										// OrderPrice.calculatePrice();
									}
									posiDialog.close();
									nabnum.init();
									updateIndex();
									// 订单有效性检验
									orderValidator.all();
								} else {
									var option = {
										title : "提示",
										content : "未选择数据"
									};
									dialog.alert(option);
								}
							}
						}, {
							text : "取消",
							handler : function() {
								posiDialog.close();
							}
						} ],
						content : grid,
						position : {
							left : position.left,
							top : position.top
						}
					},
					renderTo : "addProductFromCart"
				});
			});
		};

		/**
		 * 绑定回车事件，回车后，光标锁定下一个输入框
		 */
		var keyDownFocus = function(event, dom) {
			if (event.keyCode == 13) {
				var clsList = event.target.classList;
				if (clsList.length >= 2) {
					if (clsList[1] == "add_input_name") {
						$(dom).parent().parent().find(".add_input_amount").focus();
					} else if (clsList[1] == "add_input_amount") {
						var parentDiv = $(dom).parent().parent();
						parentDiv.find('a:first').focus();
					} else if (clsList[1] == "idOrderAdd") {
						$(dom).parent().parent().find(".add_input_name").focus();
					} else if (clsList[1] == "user_input_address") {
						$(dom).parent().parent().find(".user_input_address").focus();
					}
				}
			}
		};

		/**
		 * 购物车价格计算
		 */
		var shopcartSummy = function() {
			var input = $(this);
			if (numberValidator(input)) {
				caclRowSummy(input);
			}
		};

		/**
		 * 计算行金额 约定 价格 class = 'caclPrice' 合计 class = 'norigtaxmny'
		 */
		var caclRowSummy = function(input) {
			var dom_tr = input.parent().parent();
			if (dom_tr.hasClass('J-order-row-blargress')) {
				return;
			}
			var caclNumber = input.val();// 数量
			var caclPrice = dom_tr.find(".caclPrice").text();// 价格
			var norigtaxmny = maths.mul(caclNumber, caclPrice);// 行合计
			// 设置金额和隐藏input
			var scale = dom_tr.find("span.norigtaxmny").attr("scale");
			norigtaxmny = formatter.fmtNum(norigtaxmny, scale);
			dom_tr.find("span.norigtaxmny").text(norigtaxmny);
		};

		/**
		 * 计算购物车合计
		 */
		var caclShopCartSummy = function() {
			var trs = $(".order_add_from_cart").find("tbody").children();
			for ( var i = 0; i < trs.length; i++) {
				var dom_tr = $(trs[i]);
				var caclNumber = dom_tr.find(".shopCart_caclSummy").val();// 数量
				var caclPrice = dom_tr.find(".caclPrice").text();// 价格
				var norigtaxmny = maths.mul(caclNumber, caclPrice);// 行合计
				// 设置金额和隐藏input
				var scale = dom_tr.find("span.norigtaxmny").attr("scale");
				norigtaxmny = formatter.fmtNum(norigtaxmny, scale);
				if (isNaN(norigtaxmny)) {
					norigtaxmny = 0.00;
				}
				dom_tr.find("span.norigtaxmny").text(norigtaxmny);
			}
		};

		/**
		 * 增加订单行
		 * 
		 * 
		 * @param listBtm
		 *            按钮父级元素$(".list_btm")
		 * @param panel
		 *            列表父级对象
		 * @param ordertype
		 *            如果flag =="return" 新增退货商品
		 */
		var orderAdd = function(listBtm, panel, ordertype, callback) {
			var productId = $(listBtm).find(".add_input_name").attr("pid");
			var productPrevName = $(listBtm).find(".add_input_name").attr("pname");
			var productNowName = $(listBtm).find(".add_input_name").val();

			if (productId == "" || productId == undefined) {
				var option = {
					title : "提示",
					content : "没有该商品"
				};
				dialog.alert(option);
			} else if (productPrevName != productNowName) {
				var option = {
					title : "提示",
					content : "没有该商品"
				};
				dialog.alert(option);
			} else {
				// ajax查询商品信息
				$.ajax({
					url : GLOBAL.API.PRODUCT_BASIC,
					data : {
						cproductid : productId
					},
					success : function(json) {
						addProduct(json, listBtm, panel, ordertype, callback);
					},
					error : function() {
						var option = {
							title : "提示",
							content : "添加商品失败，请重试！"
						};
						dialog.alert(option);
					}
				});

			}
			return false;
		};

		this.orderAdd = orderAdd;
		
		
		var addProductItem=function(json, listBtm, panel, ordertype, callback){
			if (json == "noprice") {
				var option = {
					title : "提示",
					content : "该商品没有价格，不能购买！"
				};
				dialog.alert(option);
				return;
			} else if (json == "nostock") {
				var option = {
					title : "提示",
					content : "该商品没有库存，不能购买！"
				};
				dialog.alert(option);
				return;
			} else if (json.length < 1) {
				var option = {
					title : "提示",
					content : "没有查到该商品，不能购买！"
				};
				dialog.alert(option);
				return;
			}
			var product = json[0];
			var cproductid = product.cproductid;
			var cmaterialid = product.cinventoryid;
			var pkSaleOrg = product.pk_org;
			var cproductid_code = product.vcode;
			var cproductid_name = product.vname;
			var cmaterial_name = product.cinventoryid_name;
			var csaleOrg_name = product.saleOrgName;
			var unitScale = product.pk_measdoc_unitScale;// 单位主键
			var pk_measdoc_name = product.pk_measdoc_name;// 单位名称
			var nsaleprice = parseFloat(product.nsaleprice);// 销售价（含税净价）
			var priceScale = product.pk_org_priceScale;
			var htmurl = G.PAGE.PRODUCT_DETAIL + product.cproductid;
			var corigcurrencyid = product.corigcurrencyid;
			// 备用以后需要显示含税单价
			var nbaseprice = parseFloat(product.nbaseprice);// 基准价（含税单价）
			var corigcurrencyid_curSign = product.corigcurrencyid_curSign;// 币符
			var corigcurrencyid_amountSCale = product.corigcurrencyid_amountScale;
			// var nnabnum = product.nnabnum;// 库存
			var amount = product.count;
			// 币种精度
			var corigcurrencyid_amountScale = product.corigcurrencyid_amountScale;
			// 获取报价数量
			if (listBtm) {
				// 商品搜索控件 => 从控件的输入框中获取
				amount = $(listBtm).find(".add_input_amount").val();
			}
			var norigtaxmny = nsaleprice * amount;// 金额
			var bspotflag = product.bspotflag;// 是否无货可下单
			var pkUnit = product.pk_measdoc;// 报价单位

			var listBody = $(panel).find("table:first").find("tbody");
			var listSize = $(listBody).find("tr").length;
			var newRow = $('<tr></tr>').attr({
				index : listSize
			});
			// if (listSize > 0 && listSize % 2 != 1) {
			// $(newRow).addClass("row_even");
			// }
			// 获得当前地址索引,地址名称减1
			var address_index = $(panel).find(".index").text() - 1;
			var product_index = listSize;
			// 输入域name前缀
			var prefix = 'addressUIView[' + address_index + '].productUIView[' + product_index + '].';
			// 商品名称
			var productName = $('<td></td>').append($('<a></a>').addClass("cproductid_name").attr({
				href : htmurl,
				target : '_blank'
			}).append(cproductid_name));
			// 商品编码
			var productCode = $('<td></td>').append($('<a></a>').attr({
				href : htmurl,
				target : '_blank'
			}).append(cproductid_code));
			// 物料名称
			var mateiralCode = $('<td></td>').append(cmaterial_name);
			// 销售组织
			var saleOrgCode = $('<td></td>').append(csaleOrg_name);
			// 含税净价
			var productPrice = $('<td></td>').addClass("align_right ft_yahei").append(corigcurrencyid_curSign).append(
					$('<span></span>').addClass("caclPrice J-order-product-price").attr("scale", priceScale).text(nsaleprice));
			// 数量
			var productNum = $('<td class="align_left"></td>').append($('<input />').addClass("textfiled order_nqtunitnum J_checkQtNum J-order-row-amount").val(amount).attr({
				name : prefix + 'nqtunitnum',
				scale : unitScale
			})).append($("<span class='nc-unit' >").text(pk_measdoc_name));
			var productNnabnum = $('<td></td>').addClass("align_right validateNabnum").html(
					"<span ectype='nabnum'" + "productid='" + cproductid + "' scale='" + unitScale + "' unit='" + pk_measdoc_name + "'bspotflag='" + bspotflag + "'></span>");

			// 金额
			var productNtaxmny = $('<td></td>').addClass("align_right ft_yahei").append(corigcurrencyid_curSign).append(
					$('<span></span>').addClass("norigtaxmny J-order-row-price").attr("scale", corigcurrencyid_amountSCale).append(norigtaxmny));
			// 退货原因
			var reasonEl = $('[for=cretreasonid]');
			var reasonElCopy = $(reasonEl[0]).css({
				width : '100%'
			}).clone().removeClass('hidden').attr({
				name : prefix + 'cretreasonid'
			});
			var productReturnreason = $('<td class="J-td-return-reason "></td>').append(reasonElCopy);

			/* 隐藏input ，用于提交到下一个页面 */
			var hiddenInput = $('<td></td>').addClass("hidden");

			hiddenInput.append($('<input></input>').attr({
				type : 'text',
				name : prefix + 'cproductid',
				value : cproductid
			}));
			hiddenInput.append($('<input></input>').attr({
				type : 'text',
				name : prefix + 'cmaterialid',
				value : cmaterialid
			}));
			hiddenInput.append($('<input></input>').attr({
				type : 'text',
				name : prefix + 'pk_org',
				value : pkSaleOrg
			}));
			hiddenInput.append($('<input></input>').attr({
				type : 'text',
				name : prefix + 'pk_org_name',
				value : csaleOrg_name
			}));
			hiddenInput.append($('<input></input>').attr({
				type : 'text',
				name : prefix + 'corigcurrencyid',
				value : corigcurrencyid
			}));
			hiddenInput.append($('<input></input>').attr({
				type : 'text',
				name : prefix + 'nqtorigtaxnetprc',
				value : nsaleprice
			}));
			hiddenInput.append($('<input></input>').attr({
				type : 'text',
				name : prefix + 'cqtunitid',
				value : pkUnit
			}));
			hiddenInput.append($('<input></input>').attr({
				type : 'text',
				name : prefix + 'cqtunitid_unitScale',
				value : unitScale
			}));

			// 操作
			var operation = $('<td></td>').addClass("J-td-operation").append($('<a href="#" class="deleteOrderRow"></a>').html("删除"));
			// 组装HTML
			if (ordertype == "order") {

				// 退换货标记
				var fretexchange = 0;
				// 退货行引用id
				var cexchangesrcretid = '';
				// 退货行引用序号
				var returnRowIndex = -1;

				hiddenInput.append($('<input></input>').attr({
					type : 'text',
					name : prefix + 'fretexchange',
					value : fretexchange
				}));
				hiddenInput.append($('<input></input>').attr({
					type : 'text',
					name : prefix + 'cexchangesrcretid',
					value : cexchangesrcretid
				}));
				hiddenInput.append($('<input></input>').attr({
					type : 'text',
					name : prefix + 'returnRowIndex',
					value : returnRowIndex
				}));
				
				hiddenInput.append($('<input></input>').attr({
					type : 'text',
					name : prefix + 'corigcurrencyid_amountScale',
					value : corigcurrencyid_amountScale
				}));

				// 新增订单行
				$(newRow).append(productCode).append(productName).append(mateiralCode).append(saleOrgCode).append(productPrice).append(productNum).append(productNnabnum).append(productNtaxmny)
						.append(hiddenInput).append(operation);
			} else if (ordertype == "return") {

				// 退换货标记
				var fretexchange = 1;
				// 退货行引用id
				var cexchangesrcretid = '';
				// 退货行引用序号
				var returnRowIndex = -1;
				// 行类型
				var lineType = $('<td class="J-td-lineType J-td-row-type">').append('<span class="J-order-row-type" type="1">退货</span>');

				hiddenInput.append($('<input></input>').attr({
					type : 'text',
					name : prefix + 'fretexchange',
					value : fretexchange
				}));
				hiddenInput.append($('<input></input>').attr({
					type : 'text',
					name : prefix + 'cexchangesrcretid',
					value : cexchangesrcretid
				}));
				hiddenInput.append($('<input></input>').attr({
					type : 'text',
					name : prefix + 'returnRowIndex',
					value : returnRowIndex
				}));
				hiddenInput.append($('<input></input>').attr({
					type : 'text',
					name : prefix + 'nqtorigtaxprice_priceScale',
					value : priceScale
				}));

				hiddenInput.append($('<input></input>').attr({
					type : 'text',
					name : prefix + 'corigcurrencyid_curSign',
					value : corigcurrencyid_curSign
				}));

				// 操作（换货）
				var operation_change = $('<a>').attr({
					href : '#',
					title : '换货',
					style : 'margin-right:10px;',
					'class' : 'J-td-operation-exchange'
				}).html('换货');
				// 操作（删除）
				var operation_delete = $('<a>').attr({
					href : '#',
					title : '删除',
					'class' : 'J-td-operation-delete'
				}).html('删除');
				operation = $('<td>').addClass("J-td-operation").append(operation_change).append(operation_delete);
				var productTd = $('<td>').attr(json[0]).hide();
				// 新增退货单行
				$(newRow)
				// 商品编码
				.append(productCode)
				// 商品名称
				.append(productName)
				// 物料
				.append(mateiralCode)
				// 销售组织
				.append(saleOrgCode)
				// 单价
				.append(productPrice)
				// 商品数量
				.append(productNum)
				// // 库存
				// .append(productNnabnum)
				// 合计
				.append(productNtaxmny)
				// 退货原因
				.append(productReturnreason)
				// 类型
				.append(lineType)
				// 操作
				.append(operation)
				// 隐藏域
				.append(hiddenInput)
				// 商品数据
				.append(productTd);
			}
			// 添加商品
			addProductRow($(listBody), newRow);
			$(listBtm).find(".list_clear").removeClass("hidden");
		};

		/**
		 * ajax添加商品
		 * 
		 * @param {}
		 *            json
		 * @param {}
		 *            listBtm
		 * @param {}
		 *            panel
		 */
		var addProduct = function(json, listBtm, panel, ordertype, callback) {
			addProductItem(json, listBtm, panel, ordertype, callback);
			// 库存
			nabnum.init();
			// 计算索引
			updateIndex();
			// 订单有效性检验
			// orderValidator.all();
			orderValidator.price();
			// 退货单有效性检验
			returnOrderValidator.validation();
			// 执行回调
			if (callback && typeof (callback) == 'function') {
				callback(newRow);
			}
		};

		/**
		 * 删除订单行
		 */
		var deleteOrderRow = function() {
			// if (confirm("确定删除该商品吗？")) {
			var table = $(this).parents("table");
			// 删除行
			removeRow(this, table.parent());
			// 更新索引
			// table.parents(".idAddressBar").trigger("updateIndex");
			updateIndex();
			// 订单有效性检验
			orderValidator.all();
			// 退货单有效性检验
			returnOrderValidator.validation();
			return false;
		};
		/**
		 * 删除订单列表行
		 * 
		 * @param trigger
		 *            删除按钮
		 * @param panel
		 *            列表父级对象
		 */
		function removeRow(trigger, panel) {
			// todo: ajax发送请求删除后台数据
			$(trigger).parents("tr").remove();
			$(panel).find($(trigger).parents("table").find("tbody").find("tr")).each(function(index) {
				$(this).removeClass();
				if (index % 2 == 1)
					$(this).addClass("row_even");
			});
			// 订单有效性检验
			orderValidator.all();
			// 退货单有效性检验
			returnOrderValidator.validation();
		}

		/**
		 * 计算商品数量
		 */
		function caculateProductTypeNumber() {
			/*
			 * 第一步：计算每个地址的商品数量
			 */
			// 遍历地址
			$('.J-order-address').each(function(i, address) {
				var inventory = {};
				// 遍历订单行
				$(address).find('tbody > tr').each(function(i, row) {
					var item = $(row);
					// 商品主键
					var cproductid = item.find('input[name$=cproductid]').val();
					// 退换货标记
					var fretexchange = item.find('input[name$=fretexchange]').val();
					// 赠品标记
					var blargessflag = item.find('input[name$=blargessflag]').val();

					// 赠品和换货行不计算商品种类
					if (blargessflag == 'true' /* 赠品 */
							|| (fretexchange && (fretexchange == 2 || fretexchange == '2')/* 是换货 */)) {
						return true;
					}
					if (cproductid) {
						inventory[cproductid] = item;
					}
				});
				var counter = 0;
				for ( var cproductid in inventory) {
					counter++;
				}
				$(address).find('.J-product-type').text(counter);
			});

			/*
			 * 第二步：计算整单的商品数量（普通订单流程新增环节）
			 */
			// 遍历订单
			// $('.J-order').each(function(i, order) {
			var inventory = {};
			// 遍历地址
			$('.J-order-address').each(function(j, address) {
				// $(order).find('.J-order-address').each(function(j,
				// address) {
				// 遍历订单行
				$(address).find('tbody > tr').each(function(k, row) {
					var item = $(row);
					// 商品主键
					var cproductid = item.find('input[name$=cproductid]').val();
					// 退换货标记
					var fretexchange = item.find('input[name$=fretexchange]').val();
					// 赠品标记
					var blargessflag = item.find('input[name$=blargessflag]').val();
					// 赠品和换货行不计算商品种类
					if (blargessflag == 'true' /* 赠品 */
							|| (fretexchange && (fretexchange == 2 || fretexchange == '2')/* 是换货 */)) {
						return true;
					}
					if (cproductid) {
						inventory[cproductid] = item;
					}
				});
			});
			var counter = 0;
			for ( var cproductid in inventory) {
				counter++;
			}
			$('.J-all-product-type').text(counter);
		}

		/**
		 * 设置域名
		 */
		function setFieldName() {
			// 更新文本域名称
			// 需要更新索引的文本域名称后缀
			var orderAddressFieldNames = [ 'creceiveareaid', 'creceiveaddrid', 'dreceivedate' ];
			var orderItemFieldNames = [ 'cproductid', 'cshopcartid', 'nqtunitnum', 'cmaterialid', 'pk_org', 'pk_org_name', 'corigcurrencyid', 'csrcbid', 'cretreasonid', 'fretexchange',
					'cexchangesrcretid', 'returnRowIndex', 'nqtorigtaxnetprc', 'nqtorigtaxprice_priceScale', 'corigcurrencyid_curSign', 'vsrccode', 'csrcid', 'vsrcrowno', 'norigtaxmny',
					'ntotaloutnum', 'ntotalreturnnum', 'ntotalsignnum', 'cmaterialvid', 'ccustomerpoid', 'ccustomerpobid', 'nqtorignetprice', 'nqtorigprice', 'cqtunitid', 'blargessflag', 'taxRate',
					'nqtorigtaxprice', 'clargesssrcid', 'cqtunitid_unitScale', 'corigcurrencyid_amountScale' ];

			// 是否为多订单
			var isMultiOrder = false;
			if ($('input[name^=multiOrderUIViews]').length > 0) {
				isMultiOrder = true;
			}

			// deep_0：遍历订单
			$('.J-order').each(function(i, order) {
				// 订单前缀
				var orderPrefix = isMultiOrder ? 'multiOrderUIViews[' + i + '].' : '';
				// deep_1：遍历地址
				$(order).find('.J-order-address').each(function(j, address) {
					// 地址前缀
					var addressPrefix = 'addressUIView[' + j + '].';
					// 更新地址头部文本域名称
					$(orderAddressFieldNames).each(function() {
						var field = $(address).find('input[name$=' + this + ']');
						field.attr('name', orderPrefix + addressPrefix + this);
					});

					// deep_2：遍历订单行
					$(address).find('tbody > tr').each(function(k, row) {
						// 订单行前缀
						var rowPrefix = 'productUIView[' + k + '].';
						// 更新行索引
						$(row).attr('index', k);
						// 更新订单行文本域名称
						$(orderItemFieldNames).each(function() {
							var field = $(row).find('input[name$=' + this + ']');
							field.attr('name', orderPrefix + addressPrefix + rowPrefix + this);
						});
					});
				});
			});
		}

		/**
		 * 填充清单（订单流程、退换货流程新增页面动态增删商品会影响清单内的值）
		 */
		function fillInventory() {

			// deep_0：遍历订单
			$('.J-order').each(function(i, order) {
				/*
				 * 第一步：计算单个订单中的所有商品的数量和金额
				 */
				// 退货行列表
				var returnMap = {};
				// 换货行列表
				var exchangeMap = {};
				// 普通订单行列表
				var normalMap = {};
				// deep_1：遍历地址
				$(order).find('.J-order-address').each(function(j, address) {
					// deep_2：遍历订单行
					$(address).find('tbody > tr').each(function(k, row) {
						item = $(row);
						var item = $(row);
						// 产品主键
						var productId = item.find('input[name$=cproductid]').val();
						// 退换货标记
						var fretexchange = item.find('input[name$=fretexchange]').val();
						// 赠品标记
						var blargessflag = item.find('input[name$=blargessflag]').val();
						// 赠品和换货行不计算商品种类
						// 前置条件：非赠品，赠品步入清单
						if (blargessflag == 'true') {
							return true;
						}
						// 数量
						var amount = parseFloat(item.find('.J-order-row-amount').val());
						// 单价
						var price = parseFloat(item.find('.J-order-product-price').text());
						// 行合计
						var total = price * amount;
						if (fretexchange) {
							// 退换货
							if (fretexchange == 2) {
								// 换货
								if (exchangeMap[productId]) {
									exchangeMap[productId].amount += amount;
									exchangeMap[productId].total += total;
								} else {
									exchangeMap[productId] = {
										productId : productId,
										amount : amount,
										total : total,
										dom : item.clone()
									};
								}
							} else {
								// 退货
								var copy = item.clone();
								copy.find('td:eq(2), td:eq(3), td:eq(7), td:eq(9)').remove();
								// 含税净价
								var nqtorigtaxnetprc = parseFloat(copy.find('input[name$=nqtorigtaxnetprc]').val());
								// 税率
								var taxRate = '17%';
								copy.find('td:eq(1)').after('<td>' + nqtorigtaxnetprc + '</td><td>' + taxRate + '</td>');
								if (returnMap[productId]) {
									returnMap[productId].amount += amount;
									returnMap[productId].total += total;
								} else {
									returnMap[productId] = {
										productId : productId,
										amount : amount,
										total : total,
										dom : copy
									};
								}
							}
						} else {
							// 普通订货
							if (normalMap[productId]) {
								normalMap[productId].amount += amount;
								normalMap[productId].total += total;
							} else {
								normalMap[productId] = {
									productId : productId,
									amount : amount,
									total : total,
									dom : item.clone()
								};
							}
						}
					});
				});

				var inventory = $(order).find('.J-order-inventory');
				for ( var attrName in returnMap) {
					if (typeof (attrName) != 'string') {
						return true;
					}
					var productId = attrName;
					var data = returnMap[productId];
					var dom = data.dom;
					var amount = data.amount;
					var total = data.total;
					var isExist = false;
					inventory.find('tbody > tr').each(function(i, row) {
						if ($(row).find('input[name$=cproductid]').val() == productId) {
							// 已经有该行
							$(row).find('.J-order-row-amount').text(amount);
							$(row).find('.J-order-row-price').text(total);
							isExist = true;
							return false;
						}
					});
					if (!isExist) {
						dom.find('.J-order-row-amount').text(amount);
						dom.find('.J-order-row-price').text(total);
						inventory.find('tbody').append(dom);
					}
				}

				for ( var attrName in exchangeMap) {
					if (typeof (attrName) != 'string') {
						return true;
					}
					var productId = attrName;
					var data = exchangeMap[productId];
					var dom = data.dom;
					var amount = data.amount;
					var total = data.total;
					var isExist = false;
					inventory.find('tbody > tr').each(function(i, row) {
						if ($(row).find('input[name$=cproductid]').val() == productId) {
							// 已经有该行
							$(row).find('.J-order-row-amount').text(amount);
							$(row).find('.J-order-row-price').text(total);
							isExist = true;
							return false;
						}
					});
					if (!isExist) {
						dom.find('.J-order-row-amount').text(amount);
						dom.find('.J-order-row-price').text(total);
						inventory.find('tbody').append(dom);
					}
				}

				for ( var attrName in normalMap) {
					if (typeof (attrName) != 'string') {
						return true;
					}
					var productId = attrName;
					var data = normalMap[productId];
					var dom = data.dom;
					var amount = data.amount;
					var total = data.total;
					var isExist = false;
					inventory.find('tbody > tr').each(function(i, row) {
						if ($(row).find('input[name$=cproductid]').val() == productId) {
							// 已经有该行
							$(row).find('.J-order-row-amount').text(amount);
							$(row).find('.J-order-row-price').text(total);
							isExist = true;
							return false;
						}
					});
					if (!isExist) {
						dom.find('.J-order-row-amount').text(amount);
						dom.find('.J-order-row-price').text(total);
						inventory.find('tbody').append(dom);
					}
				}
			});
		}

		/**
		 * 更新多地址索引
		 */
		function updateIndex() {
			// 设置域名
			setFieldName();
			// 计算总清单商品
			fillInventory();
			// 计算商品数量
			caculateProductTypeNumber();
			// 计算价格
			OrderPrice.calculatePrice();
		}

		this.setFieldName = setFieldName;
		this.updateIndex = updateIndex;

		/**
		 * 清空地址订单行
		 * 
		 * @return {Boolean}
		 */
		function clearAddressOrders() {
			if (confirm("确定清空所有商品吗？")) {
				if ($(this).parents(".list_btm").prev("table").children("tbody").children("tr").length > 0) {
					$(this).parents(".list_btm").prev("table").children("tbody").children("tr").remove();
					// $(this).addClass("hidden");
				}
			}
			updateIndex();
			// 订单有效性检验
			orderValidator.all();
			// 退货单有效性检验
			returnOrderValidator.validation();
			return false;
		}
		/**
		 * 初始化自动补全商品
		 */
		var initAutoComplete = function() {
			$('.add_input_name').autocomplete(GLOBAL.API.PRODUCT_AUTOFILL, {
				max : 10, // 列表里的条目数
				minChars : 1, // 自动完成激活之前填入的最小字符
				width : 197, // 提示的宽度，溢出隐藏
				scrollHeight : 300, // 提示的高度，溢出显示滚动条
				matchContains : true, // 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
				autoFill : false, // 自动填充

				formatItem : function(row, i, max) {
					return row.name + "/" + row.to;
				},
				formatMatch : function(row, i, max) {
					return row.name + row.to;
				},
				formatResult : function(row) {
					return row.to;
				},
				parse : function(data) {
					return $.map(eval(data), function(row) {
						return {
							data : row,
							value : row.name,
							result : row.name
						};
					});
				}
			}).result(function(event, row, formatted) {
				// 保存id值
				$(event.currentTarget).attr("pid", row.pkid);
				$(event.currentTarget).attr("pname", row.name);
			});

			$('.add_input_name').live('blur', function() {
				var text = $(this).val();
				if (text == "") {
					$(this).val('输入商品名称 / 编码');
				}
			});

			$('.add_input_name').live('focus', function() {
				var text = $(this).val();
				if (text == "输入商品名称 / 编码") {
					$(this).val('');
				}
			});
		};
		
		/**
		 * 初始化自动补全收货地址
		 */
		var initAutoAddress = function() {
			$('.user_input_address').autocomplete(G.API.ADDRESS_AUTOFILL, {
				max : 10, // 列表里的条目数
				minChars : 1, // 自动完成激活之前填入的最小字符
				width : 220, // 提示的宽度，溢出隐藏
				scrollHeight : 300, // 提示的高度，溢出显示滚动条
				matchContains : true, // 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
				autoFill : false, // 自动填充
				formatItem : function(row, i, max) {
					return row.name + " / " + row.detailinfo;
				},
				formatMatch : function(row, i, max) {
					return row.name + row.detailinfo;
				},
				formatResult : function(row) {
					return row.detailinfo;
				},
				parse : function(data) {
					return $.map(eval(data), function(row) {
						return {
							data : row,
							value : row.pk_address,
							result : row.name + " / " + row.detailinfo
						};
					});
				}
			}).result(function(event, row, formatted) {
				$(event.currentTarget).attr("value",row.name + " / " + row.detailinfo);
				var index=$(this).parents(".J-order-address").prevAll(".J-order-address").size();
				$("input[name='addressUIView[" + index + "].creceiveaddrid']").attr("value", row.pk_address);
			});
		};

		/**
		 * 新增一个地址时，关闭其他地址
		 */
		var slideUpOtherAddress = function() {
			$(".idBarContentExtended").each(function() {
				$(this).slideUp('slow', function() {
					$(this).parents(".rounded_extended_bar").addClass("rounded_single_bar inside_width").removeClass("rounded_extended_bar");
					$(this).prev(".info_line").find(".trigger").removeClass("up").addClass("down");
					$(this).prev(".info_line").find(".content_info").show();
				});
			});
		};
		/**
		 * 添加商品
		 * 
		 * @param {}
		 *            tbody 多地址栏中的商品
		 * @param {}
		 *            row
		 */
		var addProductRow = function(tbody, row) {
			var cproductid = row.find("input[name$='.cproductid']").val();
			var number = parseFloat(row.find("input[name$='.nqtunitnum']").val());
			if (tbody.children().length > 0) {
				var appendFlag = false;
				var numberInput;
				var pronumber;
				// 循环地址栏中每一条商品，与要添加的商品比较
				tbody.children().each(function() {
					var proid = $(this).find("input[name$='.cproductid']").val();
					// 如果地址栏中的商品与待添加商品一致，则记录
					if (!$(this).hasClass('J-exchange-row') && cproductid == proid) {
						appendFlag = true;
						numberInput = $(this).find("input[name$='.nqtunitnum']");
						pronumber = parseFloat(numberInput.val());
					}
				});
				// 如果存在相同商品
				if (appendFlag) {
					// 获取当前商品名称
					var prodName = numberInput.parent().parent().find("a:first").text();
					// 提示，是否添加相同商品，如果确认添加，则直接更新原有商品中的数量，否则不添加
					if (confirm("订单中已经包含商品【" + prodName + "】，确定添加该商品吗？")) {
						numberInput.val(number + pronumber);
						// 计算订单金额
						numberInput.trigger("orderCacl");
					}
				} else {
					// 如果不存在相同商品，则直接添加
					tbody.append(row);
				}
			} else {
				tbody.append(row);
			}
			// 重新设置精度
			// setScaleFilter();
		};
		/**
		 * 校验商品是否有价格
		 */
		var checkPrice = function() {
			// 根据订单价格判断是否可以下单（如果二次询价没有价格不可下单）
			var flag = "";
			$(".caclPrice").each(function() {
				var price = $.trim($(this).text());

				if (price == "" || price == "null") {
					$(this).addClass("nc-field-warning").append("暂无报价");
					flag = flag + $(this).parent().find(".cproductid_name").text() + " ";
				}
			});
		};
		/**
		 * 数字校验
		 */
		var numberValidator = function(input) {
			return true;
			var pattern = /^[0-9\ ]+$/;
			if (pattern.test(input.val())) {
				if (input.val())
					return true;
			} else {
				input.val(0.00);
				tips.noticeBubble('Error', input, 0, 'up', "只能输入数字！");
				return false;
			}
		};
		/**
		 * 联动更新地址栏收货地区
		 */
		var areaChange = function(event) {
			var options = event.currentTarget.options;
			var areaName = "";
			for ( var i = 0; i < options.length; i++) {
				if (options[i].selected == true) {
					areaName = options[i].innerHTML;
					break;
				}
			}
			$(this).parents(".idAddressBar").find(".creceiveareaid_name").text(areaName);
		};

		/**
		 * 联动更新地址栏收货地址
		 */
		var addresChange = function(event) {
			var options = event.currentTarget.options;
			var addressName = "";
			for ( var i = 0; i < options.length; i++) {
				if (options[i].selected == true) {
					addressName = options[i].innerHTML;
					break;
				}
			}
			$(this).parents(".idAddressBar").find(".creceiveaddrid_name").text(addressName);
		};
		/**
		 * 订单打印
		 */
		var orderPrint = function() {
			$(".orderPrintArea").printArea({
				mode : "popup",
				popClose : true,
				popHt : 700,
				popWd : 600
			});
		};
		var initTotalMny;
		var datepicker_flag = 0;
		
		/*
		 * 用户输入关键字
		 * 		按关键字在<td>集合里查找相匹配的字符串 
		 * 	@author 徐中明
		 * 	@date 2013-08-20 PM 02:11
		 */
		 var getAddress = function getAddressData(){
			var data = $("#inputDetailinfo").val();
			//table里的数据集
			var addressList = $(".J-table td");
			for(var i= 0;i < addressList.length;i++){
				var curCon = $(addressList[i]).find(".J-show").text();
				var temp1 = curCon.replace("<strong>", "");
				$(addressList[i]).find(".J-show").text(temp1.replace("</strong>", ""));
			}
	 		for (var i = 0; i < addressList.length; i++) {
	 			var curCon = $(addressList[i]).find(".J-show").text();
	 			if(data == "") return;
	 			var cur_con = curCon.replace(data, "<strong>" + data + "</strong>");
	 			$(addressList[i]).find(".J-show").html(cur_con);
	 		}
		};
		
		/*
		 * 查询收货地址
		 * 
		 * @author 徐中明
		 * @date 2013-08-16 AM 11:14
		 */
		var address_search = function(index){
			var template = require('./search_address.tpl');
			var contentHtml = handlebars.compile(template);
			$.ajax({
				type : "POST",
				url : G.API.ADDRESS_ALL_ADDRESS,
				dateType : "json",
				success : function(data) {
					dialog.dialog({
						title : '地址列表',
						hasCloseTip : true,
						content : contentHtml,
						isHtmlContent : true,
						type : 'dialog',
						buttons : [ {
							name : '确定',
							isDefault : true,
							href : '#',
							method : function() {
								dialog.close();
								return false;
							}
						}, {
							name : '取消',
							isDefault : false,
							href : '#',
							method : function() {
								dialog.close();
								return false;
							}
						} ]
					});
					for (var i=0; i<data.length; i++) {
						var array = data[i];
						var span1=$("<span class='J-addrPKHidden'></span>").css("display","none").text(array.pk_address);
						var span2=$("<span class='J-show'></span>").append(array.name + " / " + array.detailinfo);
						$(".J-table").append($("<tr style='margin-bottom:10px;'></tr>").append($("<td></td>").append(span1).append(span2)));
					}
					var tempNum = 8;
					if ($(".J-table tr").size() < tempNum) {
						var tempTR = "";
						for (var j = 0; j < tempNum; j++) {
							tempTR += "<tr><td></td></tr>";
						}
						$(".J-table").append(tempTR);
					}
					$(".J-index").text(index);
				}
			});
			//<Input/>标签里ID属性
			$("#inputDetailinfo").live("keyup", getAddress);

			$(".J-table td").live("dblclick click mouseover",function(event){
				if(event.type == 'mouseover'){
					$(this).find(".J-show").css("cursor", "default");
				}
				var index = $(".J-index").text();
				$(".J-table tr td").css("background-color", "#FFFFFF");
				$(this).css("background-color", "#80C02A");
				var zhengWen = $(this).find(".J-show").text();
				var zhengWenPK = $(this).find(".J-addrPKHidden").text();
				zhengWen = zhengWen.replace("<strong>", "");
				zhengWen = zhengWen.replace("</strong>", "");
				$("input[name='addressUIView[" + index + "].creceiveaddrid_name'").attr("value", zhengWen);
				$("input[name='addressUIView[" + index + "].creceiveaddrid'").attr("value", zhengWenPK);
				if(event.type == 'dblclick'){
					dialog.close();
				}
			});
		};
		this.address_search = address_search;
		
		/* 弹窗查询退货地址 * @author 徐中明 * @date 2013-08-16 PM 07:08 */
		var returnQueryAddress = function(index){
			address_search(index);
		};
		this.returnQueryAddress = returnQueryAddress;
		
		/**
		 * 初始化
		 */
		this.init = function() {
			initAutoComplete();
			//收货地址
			initAutoAddress();
			checkPrice();
			// // 计算商品清单
			// calTotalProd();
			// 初始化购物车合计
			caclShopCartSummy();
			// 初始化商品合计
			// caclOrderSummy();
			OrderPrice.calculatePrice();
			// 代理事件（保证新增的地址不用再次绑定事件）
			// 收货地址删除
			$("body").delegate(".bar_content .icon_delete", "click", closeAddress);
			// 从购物车添加商品
			$("body").delegate(".addProductFromCart", "click", addFromCart);

			// // 绑定商品录入事件校验
			// $("body").delegate(".J_checkQtNum", "blur", function() {
			// var inputNum = parseFloat($(this).val());
			// var span = $(this).parent().parent().find("span[ectype=nabnum]");
			// var bspotflag = span.attr("bspotflag");
			// var realNum = parseFloat(span.text());
			// if (bspotflag == "false" && inputNum > realNum) {
			// tips.noticeBubble('userNameError', $(this), 185, 'up',
			// "订单数量不能超过库存数量");
			// }
			// return false;
			// });
			// 绑定商品录入事件校验
			$('body').delegate('.J-order-row-amount', 'blur', function() {
				// // 报价数量
				// var inputNum = parseFloat($(this).val());
				// // var span =
				// $(this).parent().parent().find('span[ectype=nabnum]');
				// // 库存
				// var nabnum =
				// parseFloat($(this).parents('td').find('span[ectype=nabnum').attr('nabnum'));
				// var bspotflag =
				// $(this).parents('td').find('span[ectype=nabnum').attr('bspotflag');
				// bspotflag = (bspotflag === 'true' ? true : false);
				//				
				// var realNum = parseFloat(span.text());
				// if (bspotflag == 'false' && inputNum > realNum) {
				// tips.noticeBubble('userNameError', $(this), 185, 'up',
				// '订单数量不能超过库存数量');
				// }
				// return false;
				orderValidator.nabnum();
			});

			// 订单ajax添加商品
			$("body").delegate(".idOrderAdd", "click", function() {
				orderAdd($(this).parents(".list_btm"), $(this).parents(".bar_content"), "order");
				return false;
			});
			// 绑定回车事件，回车后，光标锁定下一个输入框
			$("body").delegate(".add_input_name,.add_input_amount,.idOrderAdd,.user_input_address", "keydown", function(event) {
				keyDownFocus(event, this);
			});
			// ajax添加商品数量过滤
			$("body").delegate(".add_input_amount", "blur", function() {
				validator.validatorData(this);
			});
			// // 退货单ajax添加商品
			// $("body").delegate(".J-return-order-add-product", "click",
			// function() {
			// orderAdd($(this).parents(".list_btm"), $(this)
			// .parents(".bar_content"), "return");
			// return false;
			// });
			// 订单行删除事件
			// $("body").delegate(".deleteOrderRow", "click", deleteOrderRow);
			$("body").delegate(".deleteOrderRow", "click", deleteOrderRow);
			// 清空订单行事件
			$("body").delegate(".list_btm .list_clear", "click", clearAddressOrders);

			// 订单商品价格联动
			$("body").delegate("input[name$='.nqtunitnum']", "blur", OrderPrice.calculatePrice);
			// 购物车商品价格联动
			$("body").delegate("input[name='nqtunitnum']", "blur", shopcartSummy);
			// 收货地区联动
			$("body").delegate("select[name$='creceiveareaid']", "change", areaChange);
			// 收货地址联动
			$("body").delegate("select[name$='creceiveaddrid']", "change", addresChange);

			// 订单行计算事件
			$("body").delegate("input[name$='.nqtunitnum']", "orderCacl", shopcartSummy);
			// 索引更新事件
			$("body").delegate(".idAddressBar", "updateIndex", this.updateIndex);
			// 普通事件
			// 新增收货地址
			$(".orderAddAddress").bind('click', orderAddressAdd);

			// 新增退货地址
			$(".returnOrderAddAddress").bind('click', function() {
				// 把this对象传递给orderAddressAdd方法
				orderAddressAdd.call(this, "return");
				return false;
			});
			// 打印
			$(".orderPrint").bind('click', orderPrint);

			// nabnum.init();
			scale.init();

			// 商品超链接
			$('*[ectype=product]').live('click', function() {
				// 主键
				var productId = $(this).attr('productId');
				if (productId) {
					productId = $.trim(productId);
				} else {
					throw new Error('没有设置商品主键，无法构造商品超链接');
				}
				window.open(GLOBAL.PAGE.PRODUCT_DETAIL + productId);
				return false;
			});

			// 订单取消
			$('.J-return-order-cancel').live('click', function() {
				location.href = G.ctx;
			});

			// // 订单打印
			// $('*[ectype=print]').live('click', function() {
			// // 类型
			// var type = $.trim($(this).attr('type'));
			// // 主键
			// var data = $.trim($(this).attr('data'));
			// // 链接
			// var url = '';
			// switch (type) {
			// case 'order': {
			// url = G.API.ORDER_PRINT + '?orderid=' + data;
			// break;
			// }
			// case 'returnOrder': {
			// url = G.API.ORDER_RETURN_PRINT + '?orderid=' + data;
			// break;
			// }
			// default: {
			// url = G.API.ORDER_PRINT + '?orderid=' + data;
			// break;
			// }
			// }
			// window.open(url);
			// return false;
			// });
		};
	};
	
	var orderCore = new OrderCore();
	/**
	 * 页面加载默认操作
	 */
	$(function() {
		orderCore.init();
	});
	module.exports = orderCore;
});