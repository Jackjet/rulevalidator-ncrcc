define(function(require, exports, module) {
	var default_config = [["20.4963%","56.4338%","20.4963%"],["48.7132%","48.7132%","0%"],["68.1985%", "29.2279%", "0%"],["29.2279%", "68.1985%", "0%"]];		
	var portalConfig = {
				layout_config:default_config,
				portlet_config : [{
							width : default_config[0][0], // 列所占的比例，如果是 如三列 3 4 3，两列 5，5 等
							widgets : [{// 组件数组：表示此列中包含的组件
								id : "top10",
								name : "billboard",// 组件名称
								attrs : { // 组件属性
									title : "最新top10",
									more : true,
									moreurl : G.PAGE.TOP_MORE_LINK
								}
							}, {
								id : "recentview",
								name : "recentview",
								attrs : {
									title : "浏览过的商品"
								}
							}, {
								id : "bulletin",
								name : "bulletin",
								attrs : {
									title : "公告",
									more : true,
									moreurl : G.PAGE.BULLETIN
								}
							}]
						}, {
							width : default_config[0][1],
							widgets : [{
										id : "slide",
										name : "simpleSlide",
										attrs : {
											title : "广告宣传"
										}

									}, {
										id : "floorSpec",
										name : "floor",
										attrs : {
											type : "special"
										}
									}, {
										id : "floornew",
										name : "floor",
										attrs : {
											type : "new"
										}
									}]
						}, {
							width : default_config[0][2],
							widgets : [{
										id : "orderbox",
										name : "orderbox",
										attrs : {
											title : "消息",
											more : true,
											moreurl : G.PAGE.ORDER
										}
									}]
						}],
				// 允许添加的组件列表
				pannel_config : [{
							id : "top10",
							iconCss : "pannelTop10",
							name : "billboard",// 组件名称
							attrs : { // 组件属性
								title : "最新top10",
								more : true,
								moreurl : G.PAGE.TOP_MORE_LINK
							}
						}, {
							id : "recentview",
							iconCss : "pannelRecent",
							name : "recentview",
							attrs : {
								title : "浏览过的商品"
							}
						}, {
							id : "bulletin",
							iconCss : "pannelBulletin",
							text : "公告",
							name : "bulletin",
							attrs : {
								title : "公告",
								more : true,
								moreurl : G.PAGE.BULLETIN
							}
						}, {
							id : "slide",
							iconCss : "pannelSlide",
							name : "simpleSlide",
							attrs : {
								title : "广告宣传"
							}
						}, {
							id : "floorSpec",
							iconCss : "pannelSpec",

							name : "floor",
							attrs : {
								title : "特价促销",
								type : "special"
							}
						}, {
							id : "floornew",
							iconCss : "pannelNew",
							name : "floor",
							attrs : {
								title : "新品推荐",
								type : "new"
							}
						}, {
							id : "orderbox",
							iconCss : "pannelOrderBox",
							name : "orderbox",
							attrs : {
								title : "消息",
								more : true,
								moreurl : G.PAGE.ORDER
							}
						},{
							id:"creditamount",
							iconCss:"pannelCreditamount",
							name:"creditamount",
							attrs:{
								title:"信用额度",
								more: true,
								moreurl:G.PAGE.ACCOUNT_CREDIT
							}
						}],
				compropsetting_default:{'indexsetting':{'logo_change':'','topInfo':{},'footInfo':{}},'loginsetting':{'login_logo':'','login_left':'','login_bg':'','login_alert':'','login_tel':'','login_email':''}},
				loginalert_default : "如果您还没有Yonyou在线商城的账号，您可以通过以下方式获取您的账号",
				logintel_default : "011-62435701",
				loginemail_default : "webmaster@yonyou.com",
				topinfo_default : {"topInfo":"产品热线：","topInfoContent":"800-400-5662"},
				footinfo_default : {"footInfo":"版权所有：用友软件股份有限公司  Yonyou 2012  服务电话：+800-400-5662"}
				
	};
			module.exports = portalConfig;
		});