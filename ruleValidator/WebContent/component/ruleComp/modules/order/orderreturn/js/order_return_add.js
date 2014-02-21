/*
 * 退换货流程新增函数库
 */
define(function(require, exports, module) {
	
	require('../../orderadd/theme/{theme}/css/order.css');
	require('../theme/{theme}/css/order_return.css');
	require('../theme/default/css/order_price.css');
	
	var $ = require('$');
	var _dialog = require('dialog');
//	var _nabnum = require("nabnum");
//	var _scale = require("scale");
//	var _maths = require("maths");
//	var _formatter = require("formatter");
	var _product_field = require('order_product_field');
	var _handlebars = require('handlebars');
	var _dialog = require('dialog');
	var _price = require('order_return_price');
	
	/**
	 * 增加收获地址
	 */
	function addAddress(event) {
		var template = $($('.J-order-address')[0]);
		var copy = template.clone();
		copy.find('tbody tr').remove();
		template.after(copy);
		_price.calculatePrice();
		// 商品搜索控件
		if (copy.find('.J-order-auto-product').length > 0) {
			// 有商品搜索控件 => 自制退换货流程 => 需要商品搜索控件
			// 删除原来的控件（原来的由于id相同不能使用）
			var container = copy.find('.J-order-auto-product');
			container.empty();
			// 创建一个新的
			var oldId = container.attr('id');
			var id = oldId.split('_')[0] + '_' + (parseInt(oldId.split('_')[1]) + 1);
			var mark = parseInt(container.attr('mark')) + 1;
			container.attr('id', id);
			container.attr('mark', mark);
			createProductSearcher(container);
		}
		return false;
	}
	
	/**
	 * 根据商品主键创建退货行
	 */
	function addReturnRow(event) {
		// 增加商品行按钮
		var dom = event.data.dom;
		// 商品主键
		var productId = event.data.productId;
		$.ajax({
			url : G.API.PRODUCT_BASIC,
			data : {
				cproductid : productId
			},
			success : function(data) {
				if (!data.message) {
					if (data == 'noprice') {
						var option = {
								title : '提示',
								content : '该商品没有价格，不能购买！'
						};
						_dialog.alert(option);
						$('.ET_addInputName:first').focus();
					} else if (data == 'nostock') {
						var option = {
								title : '提示',
								content : '该商品没有库存，不能购买！'
						};
						_dialog.alert(option);
						$('.ET_addInputName:first').focus();
					} else if (data.length < 1) {
						var option = {
								title : '提示',
								content : '没有查到该商品，不能购买！'
						};
						_dialog.alert(option);
						$('.ET_addInputName:first').focus();
					} else {
						// 商品
						var product = data[0];
						// 组装商品属性，属性列表将用于该退货行的隐藏表单域，用于提交数据
						var attrs = [];
						for (var attr in product) {
							attrs.push({
								key:attr,
								value:product[attr]
							});
						}
						product.attrs = attrs;
						// 每个商品创建一个退货行
						var rowTemplate = require('../template/order_return_row_return.tpl');
						var rowHtml = _handlebars.compile(rowTemplate)(product);
						// 渲染节点
						$(dom).parents('.J-order-address').find('tbody').append(rowHtml);
						// 计算价格
						_price.calculatePrice();
						return false;
					}
				}
			},
			error : function() {
				var option = {
						title : "提示",
						content : "添加商品失败，请重试！"
				};
				_dialog.alert(option);
			}
		});
		
		return false;
	}
	
	/**
	 * 切换显示地址栏
	 */
	function toggleAddress() {
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
	}

	/**
	 * 地址删除
	 */
	function removeAddress() {
		
		// 判断是否是最后一个地址
		if ($('.J-order-address').length == 1) {
			_dialog.alert({
				content:'<center>已经是最后一个地址信息了，不能删除！</center>',
				isHtmlContent:true
			});
			return false;
		}
		
		var trigger = $(this);
		_dialog.dialog({
			type:'confirm',
			content:'<center>您确定删除该收货地址吗？</center>',
			isHtmlContent:true,
			buttons:[{
				name:'确定',
				isDefault:false,
				href:'#',
				method:function() {
					trigger.parents(".J-order-address").remove();
					// 重新计算价格
					_price.calculatePrice();
					// 更新索引
					_dialog.close();
				}
			},{
				name:'取消',
				isDefault:true,
				href:'#',
				method:function() {
					_dialog.close();
				}
			}]
		});
		return false;
	}

	/**
	 * 创建商品搜索控件
	 */
	function createProductSearcher(container) {
		var containerId = $(container).attr('id');
		var mark = $(container).attr('mark');
		new _product_field({
			renderTo:containerId,
			attrs:{
				mark:mark,
				callback:function(data) {
//					// 获取数据
//					var productId = product.productId;
//					var productName = product.productName;
					var dom = data.dom;
					var productId = data.productId;
					$(dom).find('.J-order-add-product-row').unbind('click').click({
						dom:dom,
						productId:productId
					}, addReturnRow);
				}
			}
		});
	}
	
	function bindEvent() {
		$('.J-order-auto-product').each(function(i, item) {
			// 创建自动查询匹配商品控件
			new _product_field({
				renderTo:item.id,
				attrs:{
					mark:'J-order-auto-product-input',
					callback:function(data) {
//						// 获取数据
//						var productId = product.productId;
//						var productName = product.productName;
						var dom = data.dom;
						var productId = data.productId;
						$(dom).find('.J-order-add-product-row').unbind('click').click({
							dom:dom,
							productId:productId
						}, addReturnRow);
					}
				}
			});
		});
	}
	
	$(function() {
		bindEvent();
	});
	
	// 变换地址
	$('.bar_content .trigger').live('click', toggleAddress);
	// 删除地址
	$('.bar_content .icon_delete').live('click', removeAddress);
	// 增加收获地址
	$('.returnOrderAddAddress').live('click', addAddress);
	// 初始化第一个商品搜索控件
	createProductSearcher($('.J-order-auto-product'));
	
});