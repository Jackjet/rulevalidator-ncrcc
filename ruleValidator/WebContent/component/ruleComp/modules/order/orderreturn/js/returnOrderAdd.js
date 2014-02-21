// var ReturnOrderAdd = function() {
define(function(require, exports, module) {
	require('../theme/{theme}/css/order_return.css');

	var $ = require("$");
	var _dialog = require("dialog");
	//	var _tips = require("tips");
	var _orderCore = require('order_core');
	var _validator = require('order_return_validate');
	var _autoComplete = require('productAutoComplete');
	var _price = require('order_return_price');
	var _scale = require("scale");
	var _stringUtils = require('stringUtils');

	function ReturnOrderAdd() {

		// 缓存
		var cache = {
			exchangeGrids : []
		};

		/**
		 * 填充数据
		 * 
		 * @param tr
		 *            订单行
		 * @param product
		 *            商品数据
		 */
		function fillRow(tr, product) {
			/*
			 * 只能选择同一个销售组织的商品
			 */
			// var originalProductTd = tr.find('td:last');
			var originalPkOrg = $(tr).find('input[name$=pk_org]').val();// ('pk_org');
			var originalOrgName = $(tr).find('td:eq(3)').text();
			var originalProductName = $(tr).prev('tr').find('td:eq(1) a').html();
			var currentPkOrg = product.pk_org;
			if (originalPkOrg && currentPkOrg && originalPkOrg != currentPkOrg) {
				// 不是同一个销售组织
				_dialog.alert({
					title : "提示",
					hasCloseTip : true,
					isHtmlContent : true,
					content : _stringUtils.format('<br>很抱歉，只能更换销售组织[<span style="color:red;">%s</span>]下的的商品，请重新选择', [ originalOrgName ])
				});
				// 商品名称修改为原来的商品
				tr.find('td:eq(1)').find('input').val(originalProductName);
			} else {
				// 同一个销售组织

				// 同一个销售组织不允许增加多行相同商品，即：相同商品需要合并
				// 退货行序号
				var returnRowIndex = parseInt($(tr).find('input[name$=returnRowIndex]').val());
				// 退货行
				var returnRow = tr.parents('tbody').find('tr:eq(' + returnRowIndex + ')');
				// 所有的换货行
				var otherExchangeRows = listExchangeOrderRow(returnRow);
				if (otherExchangeRows.length > 0) {
					otherExchangeRows = $(otherExchangeRows);
					otherExchangeRows.each(function(i, row) {
						var productId = $(row).find('input[name$=cproductid]').val();
						if (productId && product.cproductid && productId == product.cproductid) {
							// 相同商品 => 提示 => 相同商品退货行数量加一
							var unitNumEl = $(row).find('input[name$=nqtunitnum]');
							var unitNum = parseFloat(unitNumEl.val());
							unitNumEl.val(unitNum + 1);
							return false;
						}
					});
				}

				// 价格精度
				var priceScale = (product.pk_org_priceScale ? product.pk_org_priceScale : product.pk_org_pricescale);
				// 价格
				var nsaleprice = product.nsaleprice;
				// 币符
				var curSign = (product.corigcurrencyid_curSign ? product.corigcurrencyid_curSign : product.corigcurrencyid_cursign);
				// 币种精度
				var curSacle = (product.corigcurrencyid_amountScale ? product.corigcurrencyid_amountScale : product.corigcurrencyid_amountscale);
				// 单位精度
				var unitScale = (product.pk_measdoc_unitScale ? product.pk_measdoc_unitScale : product.pk_measdoc_unitscale);
				// 销售组织
				var saleOrgName = (product.saleOrgName ? product.saleOrgName : product.saleorgname);
				// 商品id
				tr.find('input[name$=cproductid]').val(product.cproductid);
				// 商品编码
				tr.children('td:eq(0)').find('a').html(product.vcode);
				// 商品名称
				// tr.children('td:eq(1)').find('a').html(product.vname);
				// 物料
				tr.children('td:eq(2)').html(product.cinventoryid_name);
				// 销售组织
				tr.children('td:eq(3)').html(saleOrgName);

				// 单价和精度
				var price = curSign + '<span class="caclPrice J-order-product-price" scale="' + priceScale + '">' + product.price + '</span>';
				tr.children('td:eq(4)').html(price);
				tr.find('input[name$=nqtorigtaxnetprc]').val(nsaleprice);
				tr.find('input[name$=nqtorigtaxprice_priceScale]').val(priceScale);

				// 数量单位
				var unitTd = tr.children('td:eq(5)');
				unitTd.find('input').attr({
					scale : unitScale
				});
				unitTd.find('span').html(product.pk_measdoc_name);

				// 总金额和单位
				var totalPrice = curSign + '<span class="norigtaxmny J-order-row-price" scale="' + curSacle + '">' + product.price + '<span>';
				tr.children('td:eq(6)').html(totalPrice);

				// 币符
				tr.find('input[name$=corigcurrencyid_curSign]').val(curSign);
				// 退换货标记
				tr.find('input[name$=fretexchange').val(2);
				// 删除换货行有可能的商品信息隐藏域
				tr.find('td.J-td-product-info').remove();
				// 增加新的商品信息隐藏域
				tr.append($('<td>').addClass('J-td-product-info').attr(product).hide());

				_price.calculatePrice();
				_scale.init();
			}
		}

		/**
		 * 找到所有退换行的换货行，如果没有找到返回空数组
		 * 
		 * @param tr
		 */
		function listExchangeOrderRow(tr) {
			tr = $(tr);
			var tbody = tr.parents("tbody");
			var returnRow = tr;
			var iReturnRow = parseInt(returnRow.attr('index'));
			var rows = tbody.children('tr');
			var exchangeRow = [];
			for ( var i = iReturnRow + 1; i < rows.length; i++) {
				var currentRow = $(rows[i]);
				var fretexchange = currentRow.find('input[name$=fretexchange]').val();
				var returnRowIndex = currentRow.find('input[name$=returnRowIndex]').val();
				if (fretexchange == 2 && (returnRowIndex && parseInt(returnRowIndex) == iReturnRow)) {
					// 是换货行 && 是当前退货单的换货行
					// currentRow.remove();
					exchangeRow.push(rows[i]);
				}
			}
			return exchangeRow;
		}

		/**
		 * 更新所有的换货行引用
		 */
		function updateExchangeRowRef() {
			$('.J-order').each(function(i, order) {
				$(order).find('.J-order-address').each(function(j, address) {
					var trs = $(address).find('tbody tr');
					var returnRow;
					for ( var i = 0; i < trs.length; i++) {
						var currentRow = $(trs[i]);
						var fretexchange = currentRow.find('input[name$=fretexchange]').val();
						// var returnRowIndex =
						// currentRow.find('input[name$=returnRowIndex]').val();
						if (fretexchange == '1') {
							// 退货
							returnRow = currentRow;
							currentRow.find('input[name$=returnRowIndex]').val(-1);
						} else if (fretexchange == '2') {
							// 换货
							currentRow.find('input[name$=returnRowIndex]').val(returnRow.attr('index'));
						}
					}
				});
			});

		}

		/**
		 * 删除退货行
		 */
		function deleteReturnOrderRow(domA) {

			// 表体
			var tbody = $(domA).parents('tbody');
			var tr = $(domA).parents('tr');
			var fretexchange = tr.find('input[name$=fretexchange]').val();
			if (fretexchange) {
				if (fretexchange == '1') {
					// 退货行
					var returnRow = tr;
					// 列出所有的换货行
					var exchangeRows = listExchangeOrderRow(returnRow);
					if (exchangeRows.length > 0) {
						if (confirm("会删除所有关联的换货行，确定要删除吗？")) {
							// 删除所有关联的换货行
							$(exchangeRows).remove();
						} else {
							return false;
						}
					}
					// 删除退货行
					returnRow.remove();
				} else if (fretexchange == '2') {
					// 换货
					var exchangeTr = tr;
					exchangeTr.remove();
				}

				// 更新索引
				_orderCore.updateIndex();
				// 更新换货行引用
				updateExchangeRowRef();
			}

			_price.calculatePrice();

			return false;
		}

		this.deleteReturnOrderRow = deleteReturnOrderRow;

		/**
		 * 用退货行数据构造一个可编辑的换货行
		 * 
		 * @param returnTr
		 *            退货行
		 * @returns 可编辑的换货行
		 */
		function buildEditableRow(tr, searcherClassName) {
			// 退货行商品id
			// var productId = tr.find('input[name$=cproductid]').val();
			// 商品编码
			// var productCode = tr.find('td:first').find('a').html();
			var productName = tr.find('td:eq(1)').find('a').html();
			// 可编辑行
			var row = tr.clone();
			// row.addClass('J-row-editable');
			row.addClass('J-exchange-row ui-order-exchange-row');
			// row.attr({returnOrder:2});
			// 商品编码
			// row.find('td:first').children('a').remove();
			// row.find('td:first').append($('<input>').css({width:'100%'}).addClass('J__autoComplete').val(productCode));
			// // 商品名称
			row.find('td:eq(1)').empty();
			var productNameObj = $('<input>').css({
				width : '100%'
			}).addClass(searcherClassName).val(productName);
			row.find('td:eq(1)').append(productNameObj);

			// 删除换货按钮
			row.find('.J-td-operation-exchange').remove();
			// 删除修改按钮
			row.find('.J-td-operation-modify').remove();
			// 订单行类型
			row.find('.J-order-row-type').html('换货').attr('type', 2);
			// 修改退换货标记
			row.find('input[name$=fretexchange]').val(2);
			// 修改引用id
			row.find('input[name$=returnRowIndex]').val(tr.attr('index'));
			// 数量
			row.find('td:eq(5) input').val(1);
			// 删除退货原因
			row.find('td.J-td-return-reason').addClass('align_center').html('--');
			return row;
		}

		/**
		 * 激活可修改订单行
		 * 
		 * @param tr
		 */
		function enableRowEditor(tr) {
			var editableRow = buildEditableRow(tr);
			$(tr).after(editableRow).remove();
			_autoComplete.init({
				callback : function(product) {
					fillRow(editableRow, product);
				}
			});
		}

		/**
		 * 增加一个交换行
		 */
		function addExchangeRow(domA) {
			// 订单表
			var table = $(domA).parents('table');
			// 订单体
			var tbody = $(domA).parents('tbody');
			// 当前退货行
			var tr = $(domA).parents('tr');
			// 退货行商品id
			var productId = tr.find('input[name$=cproductid]').val();
			// 商品编码
			var productCode = tr.find('td:first').find('a').html();
			// 数量
			var amount = 1;
			// 可编辑行
			var searcherClassName = 'J__autoComplete' + '-' + tbody.find('tr').length;
			var editableRow = buildEditableRow(tr, searcherClassName);
			// console.log('=====>');
			// console.log(editableRow.find('input[name$=fretexchange]').val());
			// console.log(editableRow.find('input[name$=returnRowIndex]').val());
			// 挂载
			var exchangeRows = listExchangeOrderRow(tr);
			if (exchangeRows && exchangeRows.length > 0) {
				$(exchangeRows).last().after(editableRow);
			} else {
				tr.after(editableRow);
			}

			// 更新索引
			_orderCore.updateIndex();
			// 将选择的数据写在最后一个隐藏td中
			_autoComplete.init({
				className : searcherClassName,
				callback : function(product) {
					fillRow(editableRow, product);
				}
			});

			//			_orderCore.caclAddressSummy($(domA));
			_price.calculatePrice();
			_scale.init();
		}

		/**
		 * 确认订单
		 */
		var confirmReturnOrder = function() {
			// 更新索引
			_orderCore.updateIndex();
			// 更新换货行引用
			updateExchangeRowRef();
			if (_validator.validation()) {
				$("#orderAddForm").attr("action", G.API.ORDER_RETURN_CONFIRM)[0].submit();
			}
			return false;
		};

		/**
		 * 初始化函数
		 */
		this.init = function() {

			// 退货单ajax添加商品
			$("body").delegate(".J-return-order-add-product", "click", function() {
				_orderCore.orderAdd($(this).parents(".list_btm"), $(this).parents(".bar_content"), "return");
				return false;
			});

			// 订单行换货按钮
			$('.J-td-operation-exchange').live('click', function() {
				addExchangeRow(this);
				return false;
			});

			// 订单行删除按钮
			$('.J-td-operation-delete').live('click', function() {
				deleteReturnOrderRow(this);
				return false;
			});

			$("#returnOrderAddSubmit").bind('click', confirmReturnOrder);

			// 按钮动作
			$('.toggle_thumb').live('click', function() {
				// 按钮
				var thumb = $(this);
				// 当前退货行
				var returnTr = thumb.parents('tr');
				// 当前退货行商品id
				var productId = returnTr.find('input[name$=cproductid]').val();
				// 换货行
				var exchangeTrs = returnTr.parent('tbody').find('tr.exchange-row[productId=' + productId + ']');

				if (thumb.hasClass('plus')) {
					// 收缩 => 该退货行的所有换货行蜷缩；
					thumb.removeClass('plus').addClass('minus');
					exchangeTrs.each(function(i, tr) {
						$(tr).show();
					});
				} else {
					// 展开 => 该退货行的所有换货行展开
					thumb.removeClass('minus').addClass('plus');
					exchangeTrs.each(function(i, tr) {
						$(tr).hide();
					});
				}
			});

			// 商品搜索控件初始化
			$('.J_autoComplete_input input').each(function() {
				var searchClassName = 'J_autoComplete-' + $(this).parents('tbody').find('tr').length;
				$(this).addClass(searchClassName);
				var editableRow = $(this).parents('tr');
				var productName = $(this).val();
				_autoComplete.init({
					className : searchClassName,
					callback : function(product) {
						fillRow(editableRow, product);
					}
				});
				$(this).val(productName);
			});

			// 绑定计算事件（更改商品数量）
			$('body').delegate('.J-order-row-amount', 'blur', _price.calculatePrice);

			// 更新索引
			_orderCore.updateIndex();

			// 更新换货行引用
			updateExchangeRowRef();

			// 计算合计
			_price.calculatePrice();
		};
	}

	$(document).ready(function() {
		returnOrderAdd = new ReturnOrderAdd();
		returnOrderAdd.init();

		// 期望到货日期
		var userid = store.get("user").userId;
		var dreceivedate = store.get("dreceivedate") || {};
		var userdreceivedate = dreceivedate[userid] || 7;
		$('input[name$=dreceivedate]').each(function(i, item) {
			if (!$(item).val()) {
				$(item).attr('data-datepicker-defaultDate', userdreceivedate);
			}
		});
		seajs.emit('datepicker');
	});

});