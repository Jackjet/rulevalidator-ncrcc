/* 用于动态设置改变页面的样式 */
define(function(require, exports, module) {
	var Store = require("store");
	var $ = require("$");
	require('../theme/default/css/customsetting.css');
	// 主题颜色
	var colorconfig = {
		"#E8336A" : ["#F5A0B8", "#DF1B5A","pink"],
		"#56B156" : ["#59B559", "#459E45","success"],
		"#47B9DA" : ["#59BFDE", "#35B2D6","info"],
		"#64A2D3" : ["#70B4EA", "#107CBE","primary"],
		"#EEA43C" : ["#F0AC4C", "#EC9821","warning"],
		"#D64C48" : ["#D74F4B", "#CA312D","danger"]
	};
	module.exports = {
		custom: function(){
		},
		//返回上传图片保存位置
		getImgPath:function(filename){
			return  G.ctx+"/component/ecp/common/management/customsetting/src/theme/default/img/"+filename;
		}
	};
});		
		
		
		/*,// top组件初始化时调用
		topInit : function() {
			// 设置主题颜色
			if (Store.get("themeColor_change")) {
				var themeColor = Store.get("themeColor_change");
				// 商品分类背景色
				$("head")
						.append("<style>.top_bar .ui-category-collapse{background-color:"
								+ themeColor + "}</style>")
						// 商品分类边框
						.append("<style>.top_bar .ui-category-content{border:2px solid "
								+ themeColor + ";border-top:0px;}</style>")
						.append("<style>body a.search_btn{"
								+ "width:43px;"
								+ "height:27px;"
								+ "border-radius:12px 0px 12px 0px;-moz-border-radius: 12px 0px 12px 0px;-webkit-border-radius: 12px 0px 12px 0px;"
								+ "border:solid 1px "+ themeColor+ ";"
								+ this.formatBackground(colorconfig[themeColor][0],colorconfig[themeColor][1],themeColor)
								+ "}"
								+ "body a.search_btn:hover{" 
								+ "background-color:"+ colorconfig[themeColor][0]+ ";" 
								+ "border-color:"+ themeColor+ ";" 
								+ "background-position:0px 0px;cursor:pointer;}</style>");

			}
			else{
				$("head").append("<style>.search_content{width:0px;}</style>");
			}
		},
		// sidemenu组件初始化调用,修改titile背景色
		sidemunuInit:function(){
			// 设置主题颜色
			if (Store.get("themeColor_change")) {
				var themeColor = Store.get("themeColor_change");
				// 左侧导航背景色
				$("head").append("<style>body .menu_group_title{"
								+ "border:1px solid " + themeColor + ";"
								+ "border-top:0px;background:" + themeColor
								+ "}</style>");
			}
		},
		// ecppaagegrid组件初始化调用，修改底部当前页数页数背景色
		pagegridInit:function(){
			if (Store.get("themeColor_change")) {
				var themeColor = Store.get("themeColor_change");
				// 左侧导航背景色
				$("head").append("<style>.pagination .current{background: " + themeColor+"}</style>");
			}
		},
		// 统一设置a.btn-blue样式
		setButtonStyle:function(){
			if (Store.get("themeColor_change")) {
				var themeColor = Store.get("themeColor_change");
				// 左侧导航背景色
				$("head").append("<style>body a.btn_blue,.ui-dialog a.btn_blue,a.btn_normal_blue,.ui-form .button_wrapper a.button_main{" 
				+ this.formatBackground(colorconfig[themeColor][0],colorconfig[themeColor][1],themeColor)
				+ "border-radius: 5px;-moz-border-radius: 5px;-webkit-border-radius: 5px;"
				+ "height: 25px;"
				+ "margin: 1px 0 0 0;"
				+ "line-height:25px;"
				+ "border: 1px solid rgb(128, 128, 128)}" 
				+ "a.btn_normal_blue{height:22px;line-height:22px;margin:1px 0 0 10px;}" 
				+ "a.btn_normal_blue:hover{background-position: 0 0;}.ui-form .button_wrapper a.button_main{margin:0 0 0 10px;}" 
				+"</style>");
			}
		},
		formatBackground:function(colorStart,colorEnd,themeColor){
			return "background:"+themeColor+";"
					+ "background: -moz-linear-gradient(top,"+colorStart+","+colorEnd+");"
					+ "background: -webkit-linear-gradient(top,"+colorStart+","+colorEnd+");";
					//+ "filter: progid:DXImageTransform.Microsoft.Gradient(startColorStr='"+colorStart+"',endColorStr='"+colorEnd+"',gradientType='0');";
		}*/
