define(function(require, exports, module) {
	// 自定义：组件对应的css
	require("../theme/default/css/recentview.css");
	// 自定义：组件的html模板
	var example_tpl = require("./recentview.tpl");

	var $ = require("$");
	$ = require("jquery.cookie")($);
	// 日志
	var logger = require("log");
	// 精度
	var scale = require("scale");
	// 图片宽度自适应
	var ImgResize = require("imgresize");
	// 默认图片处理
	var defaultImage = require("defaultimage");
	var ecpimages = G.ecpimages;
	var handlebars = require('handlebars');
	var picture=require("picture");
	var request=require("request");
	var Portlet=require("portlet");
	// 常量
	var constant = {
		register : {
			recentProductManager : 'recentProductManager' // 注册名称（最近商品管理器在window全局域）
		},
		selector : {
			recentProductList : '#recentProductList', // 容器id（最近浏览商品列表）
			recentProductColumn : '#recnetProductColumn', // 容器id（最近浏览商品栏目）
			recentProductItemContainer : '.visited_list'
		},
		url : {
			queryProducts : G.API.PRODUCTS // ajax方式批量查询商品
		},
		individual : {
			minSize : 1, // 个性化（最近浏览商品列表最少显示商品数量，少于此值将隐藏商品栏目）
			maxSize : 3, // 个性化（最近浏览商品列表最多显示商品数量，多于此值将只显示maxSize个商品）
			hideColumn : true
			// 个性化（最近浏览商品小于最小商品数量时，是否隐藏商品栏目）
		},
		cookieKey : 'recentProducts|' + G.userid, // 最近商品清单在cookie中存放的key值
		expires : new Date(new Date().getTime() + 30 * 24 * 60 * 60
				* 1000)
		// 超时日期（30天）
	};
	// 商品主键数组（按照浏览的降序排列，数组下标越大浏览时间越近）
	var productIdArray = [];
	// 商品json对象数组
	var products = [];
	
	// 组件的定义 组件名大写
	var RecentView = Portlet.extend({
		portlet_template : example_tpl,
		loadData:function(){
			var that=this;
			// 读取cookie
			var productIds = $.cookie(constant.cookieKey);
			if(productIds){
				productIdArray = productIds.split(',');
			}
			var params="";
			var pid=request.getParameter("id");
			if(pid){
				//新增
				for(var i=0;i<productIdArray.length;i++){
					if(pid==productIdArray[i]){
						productIdArray.splice(i, 1);
						break;
					}
				}
				productIdArray.push(pid);
				for(var j=0;j<productIdArray.length;j++){
					params+=productIdArray[j]+",";
				}
			}else{
				//展现
				params=productIds;
			}
			if (params) {
				// 获取商品基本信息
				$.ajax({
					url : constant.url.queryProducts,
					data : {
						productIds : params
					},
					dataType : 'json',
					success : function(data) {
						// 缓存商品基本信息数组
						products = data;
	
						// 并不是cookie中保存的所有商品id都能查到商品
						// 这种变动可能是由于数据库中的商品和本地缓存中保留的商品id不一致造成的
						// 对于这种情况，需要将查询的结果中，未获取到商品的id从缓存中剔除
						var validProductIds = [];
						for (var i = 0; i < products.length; i++) {
							var product = products[i];
							if (product) {
								var productId = product.cproductid;
								validProductIds.push(productId);
							}
						}
						// 保持cookie和本地缓存一致
						$.cookie(constant.cookieKey, validProductIds
										.join(','), {
									path : '/',
									expires : constant.expires
								});
	
						productIds = validProductIds;
	
						// 循环输出
						var start=products.length;
						var end=Math.max(products.length,constant.individual.maxSize)-constant.individual.maxSize;
						var dataModel=[];
						for (var i = start- 1; i >= end; i--) {
							products[i].PRODUCT_DETAIL = G.PAGE.PRODUCT_DETAIL;
							dataModel.push(products[i]);
						}
						that.setModel(dataModel);
						//图片处理
						picture.init();
						defaultImage.init();
						//价格处理
					seajs.emit("price");
					},
					error : function(response) {
						if(response.responseText =="gotoLogin"){
							window.location = G.PAGE.LOGIN;
						}
					}
				});
			} else {
				// 隐藏最近浏览栏目
				that.destroy();
			}
		},
		bindEvent : function() {
			var that=this;
			$("#clearUp").live('click', function(){
				// 删除本地缓存
				products = [];
				productIdArray=[];
				// 删除cookies
				$.cookie(constant.cookieKey, null, {
					path : '/',
					expires : constant.expires
				});
				// 删除页面节点
				$(constant.selector.recentProductList).find('div').remove();
				// 隐藏最近浏览栏目
				if (constant.individual.hideColumn) {
					that.destroy();
				}
				return false;
			});
		},
		afterRender : function() {
			RecentView.superclass.afterRender.apply(this, arguments);
			// 精度处理
			scale.init();
		},
		/**
		 * 创建商品html片段
		 * 
		 * @param {}
		 *            product
		 * @return {}
		 */
		__appendHtml : function(product) {
			var productTemplate = require('./recentview_product.tpl');
			product.PRODUCT_DETAIL = G.PAGE.PRODUCT_DETAIL;
			var html = handlebars.compile(productTemplate)(product);
			
			$(constant.selector.recentProductColumn).show();
			$(constant.selector.recentProductList).append(html);
		},
		/**
		 * 增加商品
		 * 
		 * @param {}
		 *            productId 商品id
		 */
		add : function(productId) {
			// ajax方式查询商品的基本信息
			$.ajax({
				url : constant.url.queryProducts,
				data : {
					productIds : productId
				},
				dataType : 'json',
				success : function(data) {
					var product;
					// 后台查询是个批量的，所以只需要取第一个
					if (data && data.length > 0 && data[0]) {
						product = data[0];
					}

					// 将product与本地缓存保持一致
					for (var i = 0; i < productIdArray.length; i++) {
						if (productIdArray[i]
								&& productIdArray[i] == productId) {
							// 重复了 => 更新商品的最近浏览位置 => 最近的商品排名靠后
							productIdArray.splice(i, 1);
							products.splice(i, 1);
							break;
						}
					}
					productIdArray.push(productId);
					products.push(product);

					// 保持cookie和本地缓存一致
					$.cookie(constant.cookieKey, productIdArray
							.join(','), {
						path : '/',
						expires : constant.expires
					});

					/*
					 * 在对应的位置插入商品，逻辑复杂， 改为全部删除，然后按照本地缓存从新绘制
					 */
					$(constant.selector.recentProductList)
							.find('div').remove();
					var start=products.length;
					var end=Math.max(products.length,constant.individual.maxSize)-constant.individual.maxSize;
					for (var i = start- 1; i >= end; i--) {
						this.__appendHtml(products[i]);
					}
					picture.init();
					//价格处理
					seajs.emit("price");
				},
				error : function(response) {
					if(response.responseText =="gotoLogin"){
						window.location = G.PAGE.LOGIN;
					}

				}
			});
		}
	});
	module.exports=RecentView;
});