define(function(require, exports, module) {

	var $ = require("$");
	$ = require("jquery.extend")($);

	require('../theme/default/css/accountDetail.css');
	/**
	 * 常量
	 */
	var constant = {
		id : {
			accountDetail : 'account_accountDetail',
			updatePassword : 'account_updatePassword',
			credit : 'account_credit'
		},
		color : {
			balancemny : 'menu', // 颜色（信用余额）
			engrossmny : 'inactivecaption', // 颜色（信用占用）
			limitmny : '' // 颜色（信用额度）
		},
		url : {
			getAccount : G.API.ACCOUNT_DETAIL
		}
	};
	/**
	 * 加载账户明细
	 */
	function loadAccountDetailPage() {
		/*
		 * 账户模板<br> 说明：将内容和实现分离，模板是一种工厂类型的方式， 将最终html文档和账户数据分离
		 */
		function HtmlTemplate(data) {
			// 账户信息
			this.account = data;
			// 用户信息
			this.user = data.user;
			// 客户信息
			this.customer = data.customer;
			this.html = '';

			/**
			 * 个人信息
			 */
			this.createAccountInfoHtml = function() {
				var html = '<div class="account_info floatcenter">' + '<div class="account_info_title">' + '<div class="floatleft font_14 bg_white nc-font-weight-bold">个人信息</div>' + '</div>'
						+ '<div class="account_info_detail">' + '<div class="w_100ps">' + '<span>用户编码：</span>'
						+ (this.user.user_code ? '<span>' + this.user.user_code + '</span>' : '<span class="nc-field-warning">无</span>') + '</div>' + '<div class="w_100ps">' + '<span>用户名称：</span>'
						+ (this.user.user_name ? '<span>' + this.user.user_name + '</span>' : '<span class="nc-field-warning">无</span>') + '</div>' + '<div class="w_100ps">' + '<span>关联客户编码：</span>'
						+ (this.customer.code ? '<span>' + this.customer.code + '</span>' : '<span class="nc-field-warning">无</span>') + '</div>' + '<div class="w_100ps">' + '<span>关联客户名称：</span>'
						+ (this.customer.name ? '<span>' + this.customer.name + '</span>' : '<span class="nc-field-warning">无</span>') + '</div>' + '</div>' + '</div>';
				return html;
			};

			/**
			 * 收货地址
			 */
			this.createAddressHtml = function() {
				var html = '';
				var saleOrgArray = this.customer.saleOrgArray;
				if (saleOrgArray && saleOrgArray.length > 0) {
					html += '<div class="account_info floatcenter">';
					html += '<div class="account_info_title">';
					html += '<div class="floatleft font_14 bg_white nc-font-weight-bold">收货信息</div>';
					html += '</div>';
					html += '<div class="account_info_detail">';
					// 遍历销售组织
					for ( var i = 0; i < saleOrgArray.length; i++) {
						var saleOrg = saleOrgArray[i];
						html += '<fieldset class="nc-fieldset">';
						html += '<legend class="address-title">';
						html += '<span class="ui-account-arrow active"></span>';
						html += '<span class="">' + saleOrg.pk_saleorg_name + '</span>';
						html += '</legend>';
						// 收获地址
						var addressArray = saleOrg.customerAddress;
						if (addressArray && addressArray.length > 0) {
							// 排序（如果有默认收获地址，使用默认收获地址； 如果没有默认收货地址，使用第一个收获地址；）
							addressArray.sort(function(a, b) {
								if (!!a.isdefault && !b.isdefault) {
									// a是默认，b不是默认 => a排b前面 => 返回负值
									return -1;
								} else if (!!!a.isdefault && b.isdefault) {
									// a不是默认，b是默认 => b排a前面 => 返回正值
									return 1;
								} else {
									return 0;
								}
							});
							$(addressArray).each(function(i, item) {
								html += '<div class="address-detail">';
								html += '<div class="w_100ps">';
								html += '<span>客户收货地址：</span>';
								html += (item.addressName ? '<span>' + item.addressName + '</span>' : '<span class="nc-field-warning">无</span>');
								html += '</div>';
								html += '<div class="w_100ps">';
								html += '<span>收货地区：</span>' + (item.areaclName ? '<span>' + item.areaclName + '</span>' : '<span class="nc-field-warning">无</span>');
								html += '</div>';
								html += '<div class="w_100ps">';
								html += '<span>运输方式：</span>';
								html += (saleOrg.transportTypeName ? '<span>' + saleOrg.transportTypeName + '</span>' : '<span class="nc-field-warning">无</span>');
								html += '</div>';
								html += '</div>';
							});
						} else {
							html += '<div class="address">';
							html += '<div class="w_100ps">';
							html += '<span class="nc-field-warning">无</span>';
							html += '</div>';
							html += '</div>';
						}
						html += '</fieldset>';
					}
					html += '</div></div>';
				} else {
					html += '<div class="account_info floatcenter">';
					html += '<div class="account_info_title">';
					html += '<div class="floatleft font_14 bg_white nc-font-weight-bold">收货信息</div>';
					html += '</div>';
					html += '<div class="account_info_detail">';
					html += '<div class="w_100ps">';
					html += '<span class="nc-field-warning">无</span>';
					html += '</div>';
					html += '</div>';
					html += '</div>';
				}
				return html;
			};

			/**
			 * 创建html文档
			 */
			this.createLinkManHtml = function() {
				var html = '';
				var array = this.customer.customerLinkMan;

				if (array && array.length > 0) {

					// 排序（有默认联系人，使用默认联系人； 没有默认联系人，使用第一个联系人）
					array.sort(function(a, b) {
						if (!!a.isdefault && !b.isdefault) {
							// a是默认，b不是默认 => a排b前面 => 返回负值
							return -1;
						} else if (!!!a.isdefault && b.isdefault) {
							// a不是默认，b是默认 => b排a前面 => 返回正值
							return 1;
						} else {
							return 0;
						}
					});

					// 绘制节点
					html += '<div class="account_info floatcenter">';
					html += '<div class="account_info_title">';
					html += '<div class="floatleft font_14 bg_white nc-font-weight-bold">联系方式</div>';
					html += '</div>';
					html += '<div class="account_info_detail">';
					$(array).each(function(i, item) {
						html += '<div class="w_100ps">';
						html += '<span>联系人：</span>' + (item.name ? '<span>' + item.name + '</span>' : '<span class="nc-field-warning">无</span>');
						html += '</div>';
						html += '<div class="w_100ps">';
						html += '<span>Email：</span>' + (item.email ? '<span>' + item.email + '</span>' : '<span class="nc-field-warning">无</span>');
						html += '</div>';
						html += '<br>';
					});
					html += '</div>';
				} else {
					// 没有联系人
					html += '<div class="account_info floatcenter">';
					html += '<div class="account_info_title">';
					html += '<div class="floatleft font_14 bg_white nc-font-weight-bold">联系方式</div>';
					html += '</div>';
					html += '<div class="account_info_detail">';
					html += '<div class="w_100ps">';
					html += '<span class="nc-field-warning">无</span>';
					html += '</div>';
					html += '</div>';
				}
				html += '</div>';
				return html;
			};

			/**
			 * 创建html文档
			 */
			this.createHtml = function() {
				return this.createAccountInfoHtml() + this.createAddressHtml() + this.createLinkManHtml();
			};
		}

		/**
		 * 收货地址收缩和展开
		 */
		function bindingToggle() {
			var title = $('legend.address-title');
			title.click(function() {
				
				var detail = $(this).parent().find('.address-detail');
				var icon = $(this).parent().find('.ui-account-arrow');
				var isActive = false;
				
				if (icon.hasClass('active')) {
					isActive = true;
					icon.removeClass('active');
				} else {
					isActive = false;
					icon.addClass('active');
				}

				if (isActive) {
					detail.slideUp();
				} else {
					detail.slideDown();
				}
			});
		}

		/*
		 * 修改navigator
		 */
		var navigatorHtml = '&gt; <span>个人信息中心</span>';
		$('span#navigator').html(navigatorHtml);

		/*
		 * 通过ajax方式获取数据
		 */
		$.ajax({
			type : "GET",
			url : constant.url.getAccount,
			dataType : 'json',
			success : function(data) {
				if (data.message) {
					$('.center_content').html('<span class="ui-account-detail-error">' + message + '</span>');
				}
				// 获取
				var html = new HtmlTemplate(data).createHtml();
				var targetDiv = $('div.center_content');
				targetDiv.html('');
				targetDiv.html(html);

				// 绑定收缩动作
				bindingToggle();
			}
//		,
//			error : function() {
//				$('.center_content').html('<span class="ui-account-detail-error">获取账户信息出错</span>');
//				$('.center_content').html('<span class="ui-account-detail-error">' + message + '</span>');
//			}
		});
	}
	$(function() {
		loadAccountDetailPage();
	});
});