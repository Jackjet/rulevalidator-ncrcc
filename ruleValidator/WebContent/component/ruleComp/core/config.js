window.GLOBAL = window.G = GLOBAL;
GLOBAL.ecpimages = GLOBAL.ctx+'/component/ecp/common';//组件地址
seajs.config({
	alias : {
		/**
		 * 命名规范 1：组件名全部小写 2：名称不能重复 3：同一组件不能定义多个别名 4：js文件路径不用写后缀不用谢.js /* /*公共组件
		 */
		'jquery' : 'core/widget/src/jquery',
		'base' : 'core/base/src/base',
		'widget' : 'core/widget/src/widget',// 组件核心
		"autoRender" : "core/widget/src/autoRender",// 自动加载组件
		'handlebars' : 'core/widget/src/handlebars',// 模板解析
		'plugin-text' : 'core/seajs/src/plugin-text',// 加载模板插件
		'plugin-reload' : 'core/seajs/src/plugin-reload',// 自动刷新插件
		'plugin-json' : 'core/seajs/src/plugin-json',// json加载插件

		/* 公共UI组件 */
		'log' : "common/log/src/js/Log",// 日志组件
		"imgresize" : "common/imgresize/src/js/imgresize",// 图片自适应
		"dialog" : "common/dialog/src/js/dialog",// 模态弹出框组件
		"adaptdialog" : "common/adaptdialog/src/js/adaptdialog",
		"grid" : "common/grid/src/js/Grid",// 表格
		"posdialog" : "common/posdialog/src/js/posdialog",// 可定位dialog
		"loading" : "common/loading/src/js/loading",// 进度条
		"extendgrid" : "common/extendgrid/src/js/ExtendGrid",// 可展开表格
		"simpleSlide" : "common/slide2/src/js/slide2",// 滑动组件
		"editgrid" : "common/editgrid/src/js/editgrid",// 可编辑表格
		"picturedisplay" : "common/picturedisplay/src/js/picturedisplay",//图片展示组件
		'exception':'/ecp/component/ecp/common/exception/src/js/exception',

		/* 公共工具组件 */
		"math.uuid" : "tools/math.uuid/src/math.uuid",// 唯一码生成器
		"formatter" : "tools/format/src/Formatter",// 数字日期格式化
		"maths" : "tools/math/src/Maths",// 计算函数（增）
		"placeholder" : "tools/placeholder/src/placeholder",// html的input 提示组件
		"request" : "tools/request/src/request",// 请求获取当前信息,
		'localStorage': 'tools/localstorage/src/localStorage',

		/* jquery 插件 */
		"jquery.ui.core" : "tools/jquery.ui.core/src/js/jquery.ui.core",// jquery
		// ui 核心
		"jquery.ui.datepicker" : "tools/jquery.ui.datepicker/src/js/jquery.ui.datepicker",// 日期选择组件
		"jquery.cookie" : "tools/jquery.cookie/jquery.cookie",
		"jquery.json2" : "tools/jquery.json/jquery.json2",// json转换
		"jquery.pagination" : "tools/jquery.pagination/src/js/jquery.pagination",// jquery翻页
		"jquery.jtemplates" : "tools/jquery.jtemplates/src/jquery.jtemplates",// jquery模板引擎
		"jquery.custompara" : "tools/jquery.customparam/src/jquery.custompara",// ajax
		// 提交数据格式化
		"jquery.poshytip" : "tools/jquery.poshytip/src/js/jquery.poshytip",// jquery
		// 提示
		"jquery.autocomplete" : "tools/jquery.autocomplete/src/js/jquery.autocomplete",// jquery
		// 自动补全
		"jquery.validate" : "tools/jquery.validation/jquery.validate",// jquery校验
		"jquery.plupload.queue" : "tools/jquery.plupload/src/js/jquery.plupload.queue",// jquery上传

		/* ecp UI组件 */
		"top" : "/ecp/component/ecp/common/top/src/js/top",// 页头
		"footer" : "/ecp/component/ecp/common/footer/src/js/footer",// 页脚
		"banner" : "/ecp/component/ecp/common/banner/src/js/banner",// 顶部栏
		"signon" : "/ecp/component/ecp/common/signon/src/js/signon",// 登录
		"category" : "/ecp/component/ecp/common/category/src/js/category",// 商品分类
		"recentview" : "/ecp/component/ecp/common/portal/portlets/recentview/src/js/recentview",// 最近浏览

		"sidemenu" : "/ecp/component/ecp/common/sidemenu/src/js/sidemenu",// 二级导航
		"orderbox" : "/ecp/component/ecp/common/portal/portlets/orderbox/src/js/orderbox",// 订单数量
		"bulletin" : "/ecp/component/ecp/common/portal/portlets/bulletin/src/js/bulletin",// 公告
		"shopcart" : "/ecp/component/ecp/common/shopcart/src/js/shopcart",// 购物车
		"form" : "/ecp/component/ecp/common/form/src/js/form",// 查询表单组件
		"tab" : "/ecp/component/ecp/common/tab/src/js/tab",// Tab
		"billboard" : "/ecp/component/ecp/common/portal/portlets/billboard/src/js/billboard",// top10
		'ecpeditgrid' : '/ecp/component/ecp/common/ecpeditgrid/src/js/ecpeditgrid',// 可编辑表格组件
		'search' : '/ecp/component/ecp/common/search/src/js/search',// 商品查询输入框组件
		"floor" : "/ecp/component/ecp/common/portal/portlets/floor/src/js/floor",// 首页展销商品和特价商品组件
		"productdesc" : "/ecp/component/ecp/common/productdesc/src/js/productdesc",//商品详细页面 展示组建
		'productpara':'/ecp/component/ecp/common/productpara/src/js/productpara',

		"shopcartcookie" : "/ecp/component/ecp/common/shopcart/src/js/shopcartcookie",// 购物车cookie操作
		"scale" : "/ecp/component/ecp/tools/scale/src/scale",// 精度处理
		"defaultimage" : "/ecp/component/ecp/common/defaultimage/src/js/defaultimage",// 默认图片处理
		"nabnum" : "/ecp/component/ecp/tools/nabnum/src/nabnum",// 库存处理
		"productAutoComplete" : "/ecp/component/ecp/common/productautocomplete/src/js/productAutoComplete",// 商品查询
		"productAutoCompleteAdd" : "/ecp/component/ecp/common/productautocompleteadd/src/js/productAutoCompleteAdd",// 商品自动添加
		'ecppagegrid' : '/ecp/component/ecp/common/ecppagegrid/src/js/ecppagegrid', // 分页表格
		'ecppagelist' : '/ecp/component/ecp/common/ecppagelist/src/js/ecppagelist', // 分页表格
		"ecpgrid" : "/ecp/component/ecp/common/ecpgrid/src/js/ecpgrid",// ecp表格
		"ecpproductgrid" : "/ecp/component/ecp/common/ecpproductgrid/src/js/ecpproductgrid",// ecp商品展示表格
		'productPrice' : '/ecp/component/ecp/modules/price/ProductPrice', // 商品价格服务
		'nabnum' : '/ecp/component/ecp/tools/nabnum/src/nabnum', // 商品价格服务
		"subnav" : "/ecp/component/ecp/common/subnav/src/js/subnav",// 面包屑导航
		"buttongroup" : "/ecp/component/ecp/common/buttongroup/src/js/buttongroup",// 按钮组
		"button" : "/ecp/component/ecp/common/button/src/js/button",// 按钮
		'favoritedialog' : '/ecp/component/ecp/common/favorite/src/js/FavoriteDialog',// 收藏对话框
		'favoritebutton' : '/ecp/component/ecp/common/favorite/src/js/FavoriteButton',// 收藏按钮
		'favoritegrid' : '/ecp/component/ecp/common/favorite/src/js/FavoriteGrid', // 收藏网格

		/* ecp 工具组件 */
		"context" : "/ecp/component/ecp/tools/context/src/context",// 上下文
		"cacl" : "/ecp/component/ecp/tools/cacl/src/cacl",// 行计算
		"validator" : "/ecp/component/ecp/tools/validator/src/validator",// 自定义校验器
		"iframeresize" : "/ecp/component/ecp/tools/iframeresize/src/iframeresize",// 自定义校验器
		"stringUtils" : "/ecp/component/ecp/tools/stringutils/src/StringUtils",// stringUtils
		// 提示组件
		"jquery.extend" : "/ecp/component/ecp/tools/jquery.extend/src/jquery.extend",// ecp
		// jquery扩展
		"template" : "/ecp/component/ecp/tools/xtemplate/src/XTemplate",// ecp
		// template扩展
		"tips" : "/ecp/component/ecp/tools/tips",//
		"hightSlideUtil" : "/ecp/component/ecp/common/highslide/src/js/HightSlideUtil",//

		/* ecp 模块 */
		/* 规范：模块名_js文件名 */
		"account" : "/ecp/component/ecp/modules/account/account", // 帐户中心模块
		"account_accountDetail" : "/ecp/component/ecp/modules/account/accountDetail/js/accountDetail", // 帐户明细模块
		"account_updatePassword" : "/ecp/component/ecp/modules/account/updatePassword/js/updatePassword", // 修改密码模块
		"account_creditPainter" : "/ecp/component/ecp/modules/account/credit/CreditPainter",
		"account_creditPainter" : "/ecp/component/ecp/modules/account/credit/CreditPainter",
		"account_credit" : "/ecp/component/ecp/modules/account/credit/credit", // 修改密码模块
		"help" : "/ecp/component/ecp/modules/help/help", // 帮助中心模块
		'bulletin_bulletinList' : '/ecp/component/ecp/modules/bulletin/bulletinList/js/bulletinList',

		// 渠道中心模块
		"channel_channel" : "/ecp/component/ecp/modules/channel/channel",
		"channel_upload" : "/ecp/component/ecp/modules/channel/upload",

		// 订单中心模块
		"order_orderAdd" : "/ecp/component/ecp/modules/order/orderadd/js/orderAdd",
		"order_orderConfirm" : "/ecp/component/ecp/modules/order/orderadd/js/orderConfirm",
		"order_orderDetail" : "/ecp/component/ecp/modules/order/orderadd/js/orderDetail",
		"order_orderImport" : "/ecp/component/ecp/modules/order/orderadd/js/orderImport",

		// 退货单
		"order_returnOrderAdd" : "/ecp/component/ecp/modules/order/orderreturn/js/returnOrderAdd",
		"order_returnOrderDetail" : "/ecp/component/ecp/modules/order/orderreturn/js/returnOrderDetail",
		"order_returnOrderConfirm" : "/ecp/component/ecp/modules/order/orderreturn/js/returnOrderConfirm",

		// 签收单
		"asn_asnDetail" : "/ecp/component/ecp/modules/asn/js/asn",
		
		//商品
		"product_product":"/ecp/component/ecp/modules/product/product",
		
		"help" : "/ecp/component/ecp/modules/help/js/help",
		/* 布局组件 */
		"layout" : "/ecp/component/ecp/layout/src/js/layout",
		/* portlet组件*/
		"construct" : "ecp/component/ecp/common/portal/portlet/src/js/construct",
		"portlet_config" : "ecp/component/ecp/common/portal/portlet/portlet_config",
			
	},
	preload : ['layout', 'autoRender','favoritedialog'],
	plugins:['text','nocache'],
	vars : {
		'theme':'default'
	},
	debug : true,
	/* 默认路径前缀 */
	base : '/web/component/webpub/',
	charset : 'utf-8'
});
//上下文
seajs.use("context");
// 全部的ajax 查询api接口
GLOBAL.API = {
	CATEGORY : G.ctx + "/product/category",// 查询商品分类,
	ONSALE_ADVERT : G.ctx + "/onsale/advert", // 展销商品 广告宣传
	ONSALE_NEW : G.ctx + "/onsale/query?type=newProd", // 展销商品 新品推荐
	ONSALE_SPECIAL : G.ctx + "/onsale/query?type=specialProd",// 展销商品 特价促销
	REPORT_TOP10 : G.ctx + "/billboard/ajaxQueryTopTen", // 首页 排行榜
	PRODUCT_IMAGES : G.ctx + "/product/images", // 商品详细 商品图片
	PRODUCT_RECOMMEND : G.ctx + "/product/recommend",// 商品详细 商品介绍
	NABNUM_SHOWMODEL : G.ctx + "/nabnum/queryShowModel",// 库存 显示方式
	NABNUM_QUERY : G.ctx + "/nabnum/query"// 库存 根据id查询库存
};
//系统url链接
GLOBAL.PAGE = {
	PRODUCT_PRODUCTLIST : G.ctx+"/product?page=productList",// 商品查询页面
	PRODUCT_DETAIL : G.ctx+"/html/product/product.html?id=",// 商品详细页面（需要商品id）
	TOP_MORE_LINK : G.ctx+"/report",// 首页top10链接
	ORDER_ASNLIST : G.ctx+"/forward?url=/order/subpage/asnList"//发货签收链接
},
//临时存储空间
GLOBAL.temp = {};
//门户所有组件相关事件
GLOBAL.EVENT = {
	TAB_ACTIVE : "event_tab_active",//tab组件页签切换
	SUBNAV_LOADED:"event_subnav_loaded",//面包屑导航加载完毕
	NABNUM_LOADED:"event_nabnum_loaded",//库存加载完毕
	FAVORITE_ADD:"event_favorite_add",//收藏添加商品
	SHOPCART_REMOVE:"event_shopcart_remove",//购物车删除商品 
	SHOPCART_ADD:"event_shopcart_add", //购物车添加商品
	// 资源查询
	RESOURCE_QUERY:'event_resource_query',
	'end':'end'
};
seajs.use(['jquery', 'jquery.ui.datepicker'], function($, datepicker) {
			$ = datepicker($);
		});
