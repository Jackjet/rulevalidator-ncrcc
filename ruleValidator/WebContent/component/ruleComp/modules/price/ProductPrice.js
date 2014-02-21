/**
 * 商品价格（负责查询价格，精度处理）
 */
define(function(require, exports, module) {
	
	// 依赖jquery
	var $ = require('$');
	var scale = require('scale');
	var context=require("context");
	
	/**
	 * 填充价格
	 */
	function setPrice(node, price) {
		if (node && price) {
		var priceText = "";
			//如果是基准价
			if($(node).attr("data-priceType") == "base"){
				priceText = price.nbaseprice;
			}else{
				// 销售价格
				priceText = price.nsaleprice;
			}
			// 币符
			var corigcurrencyid_curSign = price.corigcurrencyid_curSign;
			// 精度,从store中取出的精度
			var corigcurrencyid_amountScale = context.get("priceScale");
			// 组装节点
			var currencySignNode = $('<span></span>').css({fontFamily: 'Microsoft YaHei'}).text(corigcurrencyid_curSign);
			var nsalePriceNode = $('<span></span>').attr({scale: corigcurrencyid_amountScale}).addClass("J-saleprice").text(priceText);
			var priceNode = $('<span></span>').append(currencySignNode).append(nsalePriceNode);
			$(node).html(priceNode);
			seajs.emit("setPriceSuccess",$(node).attr('productId') || $(node).attr('data-productid'));
		} else if (node && !price) {
			// 没有价格 => 填充提示信息
			$(node).parents('.J-order-product-price').empty();
			var priceNode = $('<font></font>').css({color:'black'}).text('暂无价格');
			$(node).html(priceNode);
			seajs.emit("setPriceFailure",$(node).attr('productId') || $(node).attr('data-productid'));
		} else {
			throw new Error('填充价格时发现异常');
		}
	}
	
	/**
	 * 填充价格（input）
	 */
	function setPriceForInput(node, price) {
		if (node && price) {
			// 销售价格
			var nsaleprice = price.nsaleprice;
			$(node).val(nsaleprice);
		} else {
			throw new Error('填充价格时发现异常');
		}
	}
	
	var init = function(options) {
		options = options || {};
		options.setPrice = options.setPrice ? options.setPrice : setPrice;
		options.url = options.url ? options.url : G.API.PRICE;
		
		// 选择器
		var selector="";
		if(options.selector){
			selector=options.selector.find('span[ectype=price],input[ectype=price],span[data-ectype=price]');
		}else{
			selector=$('span[ectype=price],input[ectype=price],span[data-ectype=price]');
		}
		// 节点
		var nodes = selector;
		
		// 商品主键
		var productIds = [];
		$(nodes).each(function(i, item) {
			var productId = $(item).attr('productId');
			if(!productId){
				productId  = $(item).attr('data-productid');
			}
			if (productId) {
				productIds.push(productId);
			}
		});

		// 查询
		if (productIds.length > 0) {
			$.ajax({
				type : "POST",
				url : options.url,
				dataType : 'json',
				data : {productIds:productIds.join(',')},
				success : function(data) {
					if (data) {
						$(nodes).each(function(i, item) {
							var productId = $(item).attr('productId');
							if(!productId){
								productId  = $(item).attr('data-productid');
							}
							var price = data[productId];
							var tagName = item.tagName;
							if (tagName == 'INPUT') {
								setPriceForInput(item, price);
							} else if (tagName == 'SPAN') {
								setPrice(item, price);
							}
						});
						// 精度控制
						scale.init();
						seajs.emit(G.EVENT.PRICE_LOADED);
						seajs.log("结束:"+(new Date().getTime()-window.begin));
					}
				}
			});
		}
	};
	/**
	 * 入口函数
	 */
	exports.init = init;
	seajs.on("price",function(options){
		init(options);
	});
});
