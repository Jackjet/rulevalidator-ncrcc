seajs.config({
			alias : {
				"jquery" : "core/widget/src/jquery",
				"widget" : "core/widget/src/widget",
					'base' : 'core/base/src/base',
				"autoRener":"core/widget/src/autoRender",//自动加载组件
				"handlebars" : "core/widget/src/handlebars",
				"plugin-text" : "core/seajs/src/plugin-text",
				"plugin-reload" : "core/seajs/src/plugin-reload",
				"plugin-json" : "core/seajs/src/plugin-json",
				"Log":"common/log/src/js/Log",
				"jquery.cookie":"tools/jquery.cookie/jquery.cookie",
				"jquery.json2":"tools/jquery.json/jquery.json2",
				"jquery.ui.core":"tools/jquery.ui.core/src/js/jquery.ui.core",//jquery ui 核心
				"jquery.ui.datepicker":"tools/jquery.ui.datepicker/src/js/jquery.ui.datepicker",//日期选择组件
				
				//自定义组件
				"grid":"common/grid/src/js/Grid",
				"posdialog":"common/posdialog/src/js/posdialog",
				"editgrid":"common/editgrid/src/js/editgrid",
				"loading":"common/loading/src/js/loading",
				"productdisplay":"common/picturedisplay/src/js/picturedisplay",
				"quotegrid":"common/quotegrid/src/js/QuoteGrid"

			},
			preload : ["plugin-text", "plugin-json"],
			debug : true,
			base : "http://localhost/web/component/webpub/",
			charset : "utf-8"
		});

var GLOBAL = {
	
	base : "http://localhost/web/component/webpub/",
	ecp : "http://localhost/ecp/"
};
window.GLOBAL = window.G = GLOBAL;
seajs.use("autoRener");