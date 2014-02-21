/**
 * 可销量管理公共服务
 */
define(function(require, exports, module) {
	var $ = require("$");
	$ = require("jquery.extend")($);
	var Log = require("log");
	var context = require("context");

	var CommonNabnum = function() {
		// 价格
		var cache = {};
		var fillNabnum = function(selector) {
//			var begin=new Date().getTime();
			var ectypes="";
			if(selector){
				ectypes=selector.find('span[ectype=nabnum]');
			}else{
				ectypes=$('span[ectype=nabnum]');
			}
			// 需要查询库存的商品主键
			var productIds = [];
			// 组装需要通过ajax查询的商品主键
			ectypes.each(function() {
				var productId = $(this).attr('productid');
				if (cache[productId] == undefined) {
					productIds.push(productId);
				}
			});
			if (productIds.length > 0) {
				$.ajax({
					type : 'POST',
					url : G.API.NABNUM_QUERY,
					dateType : 'json',
					data : {
						ids : productIds.join(',')
					},
					success : function(data) {
//						seajs.log("查询库存耗时:"+new Date().getTime()-begin);
//						var begin1=new Date().getTime();
						if (!data.message) {
							// 将查询出来的库存保存到localstorage中
							$(data).each(function(i, item) {
								var productId = item.cproductid;
								var nabnum = item.nabnum;
								cache[productId] = nabnum;
							});
							setNabnum(ectypes);
//							var end=new Date().getTime();
//							seajs.log("设置库存时:"+end-begin1);
//							seajs.log("库存消耗总时间:"+end-begin);
						}
					}
				});
			} else {
				setNabnum(ectypes);
			}
		};
		var setNabnum = function(ectypes) {
			ectypes.each(function() {
				// 商品主键
				var productId = $(this).attr('productid');
				// 库存
				var nabnum = cache[productId];
				if (!nabnum) {
					nabnum = 0;
				}
				// 单位
				var unit = $(this).attr("unit");
				// 赠品标记
				var bspotflag = $(this).attr("bspotflag");
				$(this).siblings(".nabnum_unit").remove();
				// 设置库存
				$(this).attr('nabnum', nabnum);
				// 显示方式：0:有无,1:实际库存
				var showModel = context.get("showModel");
				if (showModel == 0) {
					var text = '';
					if (nabnum > 0) {
						text = '有';
						seajs.emit('setNabnumSuccess', productId);
					} else {
						text = '无';
						if (bspotflag == 'true') {
							seajs.emit('setNabnumSuccess', productId);
						}else{
							seajs.emit('setNabnumFailure', productId);
						}
					}
					$(this).text(text);
				} else {
					if (nabnum <= 0) {
						nabnum = 0;
						if (bspotflag == 'true') {
							seajs.emit('setNabnumSuccess', productId);
						}else{
							seajs.emit('setNabnumFailure', productId);
						}
					}else{
						seajs.emit('setNabnumSuccess', productId);
					}
					// 单位
					// 修改次行代码请注意样式nc-product-unit用来控制单位显示的长度，以防止库存行超长换行引起的格式不同意
					$(this).after($('<span>').addClass('nabnum_unit').attr('title', unit).text(unit));
					$(this).text(nabnum);
				}
			});
			var begin=
			seajs.emit("scale");
			seajs.emit(G.EVENT.NABNUM_LOADED);
		};
		// Dom加载完毕后初始化
		this.init = function() {
			fillNabnum();
			// 绑定精度事件
			$("body").delegate("*[ectype]", "doEctype", function(event) {
				fillNabnum();
			});
			seajs.off("nabnum").on("nabnum", function(selector) {
				fillNabnum(selector);
			});
		};

		/**
		 * 通过商品id查询可销量,查询后调用回调方法 productid 商品id callback 回调函数
		 */
		this.getNabnum = function(productid, callback) {
			var productIds = new Array();
			productIds.push(productid);
			$.ajax({
				type : "POST",
				url : G.API.NABNUM_QUERY,
				dateType : "json",
				data : {
					ids : productIds.join(",")
				},
				success : function(data) {
					if (data.message == undefined) {
						if (data.length > 0) {
							callback(data[0].nabnum);
						}
					} else {
						// console.error(data.message);
						Log.error(data.detail);
						return 0;
					}
				},
				error : function(data) {
					return 0;
				}
			});
		};

		/**
		 * 
		 */
		this.getNqtNum = function(productIds, callBack) {
			var flag;
			$.ajax({
				type : "POST",
				url : G.API.NABNUM_QUERY,
				dateType : "json",
				data : {
					ids : productIds.join(",")
				},
				async : false,
				success : function(data) {
					flag = callBack(data);
				}
			});
			return flag;
		};
	};

	// Dom加载完毕后初始化
	var commonNabnum = new CommonNabnum();
	$(document).ready(function() {
		commonNabnum.init();
	});
	module.exports = commonNabnum;
});