/**
 * 订单流程校验
 */
define(function(require, exports, module) {

	var $ = require('$');
	var dialog = require('dialog');
	var StringUtils = require('stringUtils');
	var tips = require('tips');

	/**
	 * 订单行
	 */
	function product() {
		// 收获地址至少有一个
		if ($('.J-order-address').length == 0) {
			var message = '没有收货地址，请添加收货地址！';
			$('.order_single_width').append(tips.noticeBarBlue(message, 'org'));
			return false;
		}
		// 商品不能为空
		var message = '';
		$('.J-order-address').each(function(i, address) {
			if ($(address).find('tbody > tr').length == 0) {
				message += StringUtils.format('第【%s】个收货地址没有商品行，请增加商品', [ i + 1 ]);
				return false;
			}
		});
		if (message) {
			$('.order_single_width').append(tips.noticeBarBlue(message, 'org'));
			return false;
		}
		return true;
	}

	/**
	 * 日期
	 */
	function date() {
		var date_flag = true;
		// 判断预计发货日期不能为空
		$(".datepicker,.date_picker").each(function(index, domEle) {
			if ($(domEle).attr("value") == "") {
				date_flag = false;
			}
		});
		if (!date_flag) {
			dialog.alertMsg("请输入期望到货日期！");
		}
		return date_flag;
	}

	/**
	 * 可销量检查（下单数量必须小于库存量）
	 */
	function nabnum() {
		var flag = true;
		var msg = '';
		$('.J-order .J-order-row-amount').each(function(index) {
			// 报价数量控件
			var input = $(this);
			// 库存控件
			var span = $(this).parents('tr').find('span[ectype=nabnum]');
			// 订单行
			var row = $(this).parents('tr');
			// 报价数量
			var nqtunitnum = parseFloat(input.val());
			// 库存
			var available = parseFloat(span.attr('nabnum'));
			// 商品名称
			var productName = row.find('td:eq(1) a').text();
			// 无货是否可下单
			var bspotflag = span.attr('bspotflag');
			
			// 购买数量输入非法校验
			if (nqtunitnum <= 0) {
				msg = StringUtils.format('第【%s】行商品【%s】购买数量必须大于0，请修改', [ index + 1, productName ]);
				$(this).css('color', 'red');
				flag = false;
			} else {
				$(this).css('color', 'black');
			}
			
			// 如果无货可下单为否时校验库存量
			if (bspotflag === 'false') {
				if (nqtunitnum > available) {
					msg = StringUtils.format('第【%s】行商品【%s】购买数量不能大于库存量，请修改', [ index + 1, productName ]);
					$(this).css('color', 'red');
					flag = false;
				}  else {
					$(this).css('color', 'black');
				}
			}
		});
		if (!flag) {
			dialog.alert({
				isHtmlContent : true,
				content : msg
			});
			$('.order_single_width').append(tips.noticeBarBlue(msg, 'org'));
		} else {
			// 通过校验 => 删除提示信息
			$('.notice_bar_org').remove();
		}
		return flag;
	}

	/**
	 * 价格
	 */
	function price() {
		var flag = true;
		$('.J-order .J-order-product-price').each(function() {
			var price = $.trim($(this).text());
			if (price == "" || price == "null" || price == "暂无报价") {
				var noticeText = "商品无价格，请修改订单！";
				$(".order_single_width").append(tips.noticeBarBlue(noticeText, "org"));
				flag = false;
			}
		});
		return flag;
	}
	// this.validateNamnum = validateNamnum;

	/**
	 * 收货信息
	 */
	function receiveInfo() {
		var receives = $(".J-order-address");
		var array = {};
		var str = "";
		$.each(receives, function(i, n) {
			var address = $(n).find("input[name$=creceiveaddrid]").val();
			var area = $(n).find("select[name$=creceiveareaid]").val();
			var date = $(n).find("input[name$=dreceivedate]").val();
			var key = address + area + date;
			if (array[key] != undefined) {
				str += "<div style='margin-left:30px;'>第" + (i + 1) + "个收货信息与第" + array[key] + "个收货信息重复</div>";
			} else {
				array[key] = (i + 1);
			}
		});
		if (str.length != 0) {
			dialog.alert({
				title : "提示",
				content : str,
				isHtmlContent : true
			});
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 汇总
	 */
	function all() {
		if (date() && product() && nabnum() && price() && receiveInfo()) {
			// 删除提示
			$('.bar_outter').remove();
			return true;
		} else {
			return false;
		}
	}

	module.exports = {
		product : product,
		date : date,
		nabnum : nabnum,
		price : price,
		receiveInfo : receiveInfo,
		all : all
	};

});