//根据localStorage里的值绑定当前默认样式
var getTheme = function() {
	var theme = eval(window.localStorage["themecolor"]) || "default";
	return theme;
};
var path="/ruleValidator/component";
seajs.config({
	alias : {
		'class' : path+'/webpub/core/class/src/class',
		'events' : path+'/webpub/core/event/src/events',
		'editable' : path+'/webpub/core/editable/src/editable',
		'base' : path+'/webpub/core/base/src/base',
		'widget' : path+'/webpub/core/widget/src/widget',// 组件核心
		"autoRender" : path+"/webpub/core/widget/src/autoRender",// 自动加载组件
		'handlebars' : path+'/webpub/core/handlebars/src/handlebars',// 模板解析
		'backbone' : path+'/webpub/tools/backbone/src/backbone',// 前台mvc
		'plugin-text' : path+'/webpub/core/seajs/src/plugin-text',// 加载模板插件
		'plugin-reload' : path+'/webpub/core/seajs/src/plugin-reload',// 自动刷新插件
		'plugin-json' : path+'/webpub/core/seajs/src/plugin-json',// json加载插件

		/* 公共UI组件 */
		'log' : path+"/webpub/common/log/src/js/Log",// 日志组件
		"imgresize" : path+"/webpub/common/imgresize/src/js/imgresize",// 图片自适应
		"grid" : path+"/webpub/common/grid/src/js/Grid",// Grid组件
		"grid_1.0" : path+"/webpub/common/grid/1.0/src/js/Grid",
		"adaptdialog" : path+"/webpub/common/adaptdialog/src/js/adaptdialog",
		"grid" : path+"/webpub/common/grid/src/js/Grid",// 表格
		"posdialog" : path+"/webpub/common/posdialog/src/js/posdialog",// 可定位dialog
		"extendgrid" : path+"/webpub/common/extendgrid/src/js/ExtendGrid",// 可展开表格
		"simpleSlide" : path+"/webpub/common/slide2/src/js/slide2",// 滑动组件
		"editgrid" : path+"/webpub/common/editgrid/src/js/editgrid",// 可编辑表格
		"picturedisplay" : path+"/webpub/common/picturedisplay/src/js/picturedisplay",// 图片展示组件
		'switcher' : path+'/webpub/common/switcher/switcher', // 开关
		'notice' : path+'/webpub/common/notice/notice', // 通知
		'td_tips':path+"/webpub/tools/td_tips",//提示框

		/* bootstrap前端框架組件 */
		'bootstrap' : path+'/webpub/common/bootstrap/js/bootstrap',

		/* 公共工具组件 */
		"math.uuid" : path+"/webpub/tools/math.uuid/src/math.uuid",// 唯一码生成器
		"formatter" : path+"/webpub/tools/format/src/Formatter",// 数字日期格式化
		"maths" : path+"/webpub/tools/math/src/Maths",// 计算函数（增）
		"placeholder" : path+"/webpub/tools/placeholder/src/placeholder",// html的input
		"ca" : path+"/webpub/tools/ca/ca",// ca 安全认证

		// 提示组件
		"request" : path+"/webpub/tools/request/src/request",// 请求获取当前信息,
		"codemirror" : path+"/webpub/tools/codemirror/src/mode/javascript/javascript",// 代码高亮插件
		"jsl" : path+"/webpub/tools/codemirror/src/jsl",// 格式化json工具

		/* jquery 插件 */
		"jquery.ui.core" : path+"/webpub/tools/jquery.ui.core/src/js/jquery.ui.core",// 
		"jquery.ui.widget" : path+"/webpub/tools/jquery.ui.widget/src/js/jquery.ui.widget",// 
		"jquery.ui.mouse" : path+"/webpub/tools/jquery.ui.mouse/src/js/jquery.ui.mouse",// 
		"jquery.ui.sortable" : path+"/webpub/tools/jquery.ui.sortable/src/js/jquery.ui.sortable",// 
		
		// ui 核心
		"jquery.ui.datepicker" : path+"/webpub/tools/jquery.ui.datepicker/src/js/jquery.ui.datepicker",// 日期选择组件
		"jquery.cookie" : path+"/webpub/tools/jquery.cookie/jquery.cookie",
		"jquery.json2" : path+"/webpub/tools/jquery.json/jquery.json2",// json转换
		"jquery.pagination" : path+"/webpub/tools/jquery.pagination/src/js/jquery.pagination",// jquery翻页
		"jquery.jtemplates" : path+"/webpub/tools/jquery.jtemplates/src/jquery.jtemplates",// jquery模板引擎
		"jquery.custompara" : path+"/webpub/tools/jquery.customparam/src/jquery.custompara",// ajax
		"jquery.resize" : "ruleComp/tools/jquery.resize/jquery.resize",// 兼容ie的响应resize事件
		"jquery.md5" : path+"/webpub/tools/jquery.md5/jquery.md5", // md5
		
		// 提交数据格式化
		"jquery.poshytip" : path+"/webpub/tools/jquery.poshytip/src/js/jquery.poshytip",// jquery
		
		// 提示
		"jquery.autocomplete" : path+"/webpub/tools/jquery.autocomplete/src/js/jquery.autocomplete",// jquery
		
		// 自动补全
		"jquery.validate" : path+"/webpub/tools/jquery.validation/jquery.validate",// jquery校验
		"jquery.plupload.queue" : path+"/webpub/tools/jquery.plupload/src/js/jquery.plupload.queue",// jquery上传
		"jquery.ztree" : path+"/webpub/tools/ztree/src/jquery.ztree.all-3.5",// jquery
		
		// tree
		"BigDecimal" : path+"/webpub/tools/bigdecimal/src/BigDecimal",// BigDecimal
		
		 
		
		// 非cmd组件的配置
		'jsuri' : {
			src : path+'/webpub/tools/jsuri/Uri.js',
			exports : 'Uri'
		},
		'json' : {
			src : path+'/webpub/tools/store/json.js',
			exports : 'JSON'
		},
		'store' : {
			src : path+'/webpub/tools/store/store.js',
			exports : 'store',
			deps : [ 'json' ]
		},
		'$' : {
			src : path+'/webpub/core/jquery/src/jquery-1.7.2',
			exports : 'jQuery'
		},
		'moment' : {
			src : path+'/webpub/tools/moment/moment',
			exports : 'moment'
		}
	},
	plugins : [ 'text', 'shim' ],
	debug : true,
	/* 默认路径前缀 */
	paths : {
		'webpub' : '/web/component/webpub/',
	},
	charset : 'utf-8',
	editable : true
});
seajs.config({
	alias : {
		/**
		 * 命名规范 1：组件名全部小写 
		 * 		    2：名称不能重复 
		 * 			3：同一组件不能定义多个别名 
		 * 			4：js文件路径不用写后缀不用谢.js
		 */

		/** -------------------grid组件-------------------------- */
		// "deligrid" : "ruleComp/common/grid/deligrid/src/js/deligrid",//
		'editgrid' : path+'/ruleComp/common/grid/ecpeditgrid/src/js/ecpeditgrid',// 可编辑表格组件
		"ecpgrid" : path+"/ruleComp/common/grid/ecpgrid/src/js/ecpgrid",// ecp表格
		'ecppagegrid' :  path+'/ruleComp/common/grid/ecppagegrid/src/js/ecppagegrid', // 分页表格
		'pagegrid' : path+'/ruleComp/common/grid/ecppagegrid/src/js/ecppagegrid', // 分页表格
		"productgrid" : path+"/ruleComp/common/grid/ecpproductgrid/src/js/ecpproductgrid",// ecp商品展示表格
		'pagelist' : path+'/ruleComp/common/grid/ecppagelist/src/js/ecppagelist', // 分页表格
		
		
		/* 公共UI组件 */
		'log' : path+"/webpub/common/log/src/js/Log",// 日志组件
		"imgresize" : path+"/webpub/common/imgresize/src/js/imgresize",// 图片自适应
		"grid" : path+"/webpub/common/grid/src/js/Grid",// Grid组件
		"grid_1.0" : path+"/webpub/common/grid/1.0/src/js/Grid",
		 /** -------------------from组件---------------------------- */
		'multiselect' : path+'/ruleComp/common/multiselect/src/js/multiselect', // 多选
		 "formfield" : "ruleComp/common/formfield/src/formfield",// 表单域
		"form" : path+"/ruleComp/common/form/src/js/form",// 查询表单组件
		 // 发票自动补全
		 "buttongroup" :path+"/ruleComp/common/buttongroup/src/js/buttongroup",// 按钮组
		 "button" : path+"/ruleComp/common/button/src/js/button",// 按钮
		 "displaypanel" : path+"/ruleComp/common/displaypanel/src/js/displaypanel",// 展示面板
		 'exception' : path+'/ruleComp/common/exception/src/js/exception',
		 'dropdowntree' : path+'/ruleComp/common/dropdowntree/src/js/dropdowntree',// 下拉树
		// /** -------------------portal组件----------------------------
		// "construct" : "ruleComp/common/portal/portlet/src/js/construct",
		 "portlet_config" :path+"/ruleComp/common/portal/portlet/portlet_config",
		 /* ruleComp UI组件 */
		 "top" : path+"/ruleComp/common/top/src/js/top",// 页头
		 "footer" : path+"/ruleComp/common/footer/src/js/footer",// 页脚
		 "banner" : path+"/ruleComp/common/banner/src/js/banner",// 顶部栏
		 "navigation" : path+"/ruleComp/common/navigation/src/js/navigation",// 顶部栏
		 "signon" : path+"/ruleComp/common/signon/src/js/signon",// 登录
		 "signonform" : path+"/ruleComp/common/signonform/src/js/signonform",// 登录表单
		 "tab" : path+"/ruleComp/common/tab/src/js/tab",// Tab
		 "dialog" : path+"/ruleComp/common/dialog/src/js/dialog",// 模态弹出框组件
		 "sidemenu" : path+"/ruleComp/common/sidemenu/src/js/sidemenu",// 二级导航
		 "icc" : path+"/ruleComp/common/icc/src/icc",// 在线客服
		 "subnav" : path+"/ruleComp/common/subnav/src/js/subnav",// 面包屑导航
		 'search' : path+'/ruleComp/common/search/src/js/search',// 查询输入框组件
		 
		//业务模块
		 
		'order_orderImport' : path+'/ruleComp/modules/order/orderadd/js/orderImport',
		'order_store' : path+'/ruleComp/modules/order/orderstore/js/orderStore',
		'order_orderEffect' : path+'/ruleComp/modules/order/orderadd/js/orderEffect',
		'order_core' : path+'/ruleComp/modules/order/orderadd/js/orderCore',
		"account_accountDetail" : path+"/ruleComp/modules/account/accountDetail/js/accountDetail", // 帐户明细模块
		
		"ruleEvent":path+'/ruleComp/modules/rulecheck/js/ruleevents',//规则校验事件处理类
		
		// *
		// 工具组件**********************************************************
		// */
		"scale" : path+"/ruleComp/tools/scale/src/scale",// 精度处理
		 "picture" : path+"/ruleComp/tools/picture/src/picture",// 商品图片处理
		 'constants' : path+'/ruleComp/tools/constants/Constants',
		 "api" : path+"/ruleComp/tools/api/src/api",// 系统接口
		 "param" : path+"/ruleComp/tools/param/src/param",// 系统参数
		 "context" : path+"/ruleComp/tools/context/src/context",// 上下文
		 "menuStore" : path+"/ruleComp/tools/menu/src/menuStore.js",// 菜单
		// "cacl" : "ruleComp/tools/cacl/src/cacl",// 行计算
		 "validator" : path+"/ruleComp/tools/validator/src/validator",// 自定义校验器
		 "iframeresize" : path+"/ruleComp/tools/iframeresize/src/iframeresize", 
		// 自定义校验器
		 "stringUtils" : path+"/ruleComp/tools/stringutils/src/StringUtils", 
		 // 提示组件
		 "jquery.extend" : path+"/ruleComp/tools/jquery.extend/src/jquery.extend",// ruleComp
		 // jquery扩展
		 "template" : path+"/ruleComp/tools/xtemplate/src/XTemplate",// ruleComp
		 // tip扩展
		 "tips" : path+"ruleComp/tools/tips",//

		 // 模块**********************************************************
		 
		 /* 布局组件 */
		 "layout" : path+"/ruleComp/layout/src/js/layout",
		 "customsetting" : path+"/ruleComp/common/management/customsetting/src/js/customsetting",// 用户自定义组件（后续banner或其他组件实现）完成根据用户自定义展示页面样式
		 
		 
		 'user' : path+"/ruleComp/common/address/src/js/address",// 账户-用户添加
		 'user_update' : path+"/ruleComp/common/address/src/js/addressupdate" ,// 用户维护修改
		 "updatePassword" : path+"/ruleComp/modules/account/updatePassword/js/updatePassword", // 修改密码模块

		 'resource' : path+"/ruleComp/common/management/resouce/src/js/resouce",// 系统管理-资源添加
		 'resource_update' : path+"/ruleComp/common/management/resouce/src/js/resouceupdate" ,// 系统管理-资源维护修改

		 'meanus' : path+"/ruleComp/common/management/resouce/src/js/menus",// 系统管理-菜单添加
		 'meanus_update' : path+"/ruleComp/common/management/resouce/src/js/meanuupdate" ,// 系统管理-菜单维护修改
	},
	preload : [],
	plugins : [ 'text', 'shim' ],
	vars : {
		'theme' : getTheme()
	},
	debug : true,
	charset : 'utf-8',
	editable : true
});


 
