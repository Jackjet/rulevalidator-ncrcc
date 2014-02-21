define(function(require, exports, module) {
	var request = require("request");
	var $ = require("$");
	require("jquery.extend");
	var iframeresize = require("iframeresize");
	var EcpGrid = require("ecpgrid");
	var ProductPara=require("productpara");
	var productid = request.getParameter("id");
	var Tab = require("tab");
	var RECOMMEND_TAB_ID = "recommend";
	var PARAMETER_TAB_ID = "parameter";
	var NABNUM_TAB_ID = "nabnum";
	var GIFT_TAB_ID = "gift";
	var PACKLIST_TAB_ID = "packlist";
	var AFTERSALESERVICE_TAB_ID = "afterSaleService";
	var formatter=require("formatter");
	$(function(){
		var defaultitems = [{
			id : RECOMMEND_TAB_ID,
			title : '商品介绍',
			active : true
		}, {
			id : PARAMETER_TAB_ID,
			title : '规格参数'
		}, {
			id : NABNUM_TAB_ID,
			title : '库存信息'
		}, {
			id : GIFT_TAB_ID,
			title : '赠品信息'
		}, {
			id : PACKLIST_TAB_ID,
			title : '包装清单'
		}, {
			id : AFTERSALESERVICE_TAB_ID,
			title : '售后服务'
		}];
		var items = store.get("producttab")||defaultitems;
		var tab = new Tab({
					attrs : {
						items :items
					},
					renderTo : "tab"
				});
		$(".label_wapper a").click(function(){
			return false;
		});
		// 商品详情
		$.ajax({
			type : 'GET',
			url : G.API.PRODUCT_RECOMMEND,
			data:{
				"id":productid
			},
			success : function(result) {
				if(result){
					$("#" + RECOMMEND_TAB_ID).append($("<iframe frameBorder=0 scrolling=no width='100%'>").attr("src", "/" + result.data));
					seajs.emit("iframe_loaded");
				}else{
					var div=$("<div></div>").html("没有找到商品介绍").css("text-align","center").css("color","red");
					$("#" + RECOMMEND_TAB_ID).append(div);
				}
			},
			error:function(){
				var div=$("<div></div>").html("查找商品介绍出现错误").css("text-align","center").css("color","red");
				$("#" + RECOMMEND_TAB_ID).html(div);
			},
			dataType : 'json'
		});
		// 包装清单
		$.ajax({
			type : 'GET',
			url : G.API.PRODUCT_PACKLIST,
			data:{
				"id":productid
			},
			success : function(result) {
				if(result){
					$("#" + PACKLIST_TAB_ID).append($("<iframe frameBorder=0 scrolling=no width='100%'>").attr("src", "/" + result.data));
					seajs.emit("iframe_loaded");
				}else{
					var div=$("<div></div>").html("没有找到包装清单信息").css("text-align","center").css("color","red");
					$("#" + PACKLIST_TAB_ID).append(div);
				}
			},
			error:function(){
				var div=$("<div></div>").html("查询包装清单信息出现错误").css("text-align","center").css("color","red");
				$("#" + PACKLIST_TAB_ID).append(div);
			},
			dataType : 'json'
		});
		// 售后服务
		$.ajax({
			url : G.API.PRODUCT_AFTERSALESERVICE,
			data:{
				"id":productid
			},
			success : function(result) {
				if(result){
					$("#" + AFTERSALESERVICE_TAB_ID).append($("<iframe frameBorder=0 scrolling=no width='100%'>").attr("src", "/" + result.data));
					seajs.emit("iframe_loaded");
				}else{
					var div=$("<div></div>").html("没有找到售后服务信息").css("text-align","center").css("color","red");
					$("#" + AFTERSALESERVICE_TAB_ID).append(div);
				}
			},
			error:function(){
				var div=$("<div></div>").html("查询售后服务信息出现错误").css("text-align","center").css("color","red");
				$("#" + AFTERSALESERVICE_TAB_ID).append(div);
			},
			dataType : 'json'
		});
		// 处理库存页签
			$.ajax({
				type : "POST",
				dataType : 'json',
				url : G.API.NABNUM_QUERY,
				data : {
					ids : productid
				},
				success : function(data) {
					if (data && data.length == 1) {
						var option = {
							model : {
								total : data.length,
								records : data[0].nabnumDetailUIViewList
							},
							attrs : {
								columns : [{
											label : "库存组织名称",
											key : "stockOrgName",
											width : 150
										}, {
											label : "仓库名称",
											key : "pk_stordoc_name",
											width : 200
										}, {
											label : "仓库地址",
											key : "storePlace",
											width : 100
										}, {
											label : "数量",
											key : "nabnum",
											format:function(value,row){
												var ret="";
												ret+="<span scale='"+row.pk_measdoc_unitScale+"'>"+value+"</span>";
												ret+="<span>"+row.pk_measdoc_name+"</span>";
												return ret;
											},
											width : 100
										}]
							},
							renderTo : NABNUM_TAB_ID,
							autoRender : true
						};
						var ecpgrid = new EcpGrid(option);
						seajs.emit("scale");
					}else{
						var div=$("<div></div>").html("没有找到商品库存信息").css("text-align","center").css("color","red");
						$("#" + NABNUM_TAB_ID).append(div);
					}
				},
				error:function(){
					var div=$("<div></div>").html("查询库存信息出现错误").css("text-align","center").css("color","red");
					$("#" + NABNUM_TAB_ID).append(div);
				}
			});
		// 处理商品参数页签
		$.ajax({
			type : "GET",
			dataType : 'json',
			url : G.API.PRODUCT_PARAMETER,
			data:{
				"id":productid
			},
			success : function(data) {
				if (data && data.length>0) {
					var option = {
						model : data,
						attrs : {
							title:"groupName",
							body:"parameterList",
							key:"vparamname",
							value:"vparamvalue"
						},
						renderTo : PARAMETER_TAB_ID
					};
					var productpara = new ProductPara(option);
				}else{
					var div=$("<div></div>").html("没有找到商品参数信息").css("text-align","center").css("color","red");
					$("#" + PARAMETER_TAB_ID).append(div);
				}
			},
			error:function(){
				var div=$("<div></div>").html("查询参数信息出现错误").css("text-align","center").css("color","red");
				$("#" + PARAMETER_TAB_ID).append(div);
			}
		});
		// 处理赠品页签
		$.ajax({
			type:"GET",
			dataType : 'json',
			url : G.API.PRODUCT_GIFT,
			data:{
				"id":productid
			},
			success:function(data){
				if (data && data.length>0) {
					var option = {
						model : {
							total : data.length,
							records : data
						},
						attrs : {
							columns : [{
										label : "购买情况",
										key : "description",
										width:"300"
									}, {
										label : "随机赠品",
										key:"bodys",
										format:function(value,row){
											var ret="";
											for(var i=0;i<value.length;i++){
												var products=value[i].products;
												if(products && products.length!=0){
													var s=formatter.fmtNum(value[i].nnum,value[i].pk_measdoc_unitScale)+value[i].pk_measdoc_name;
													ret+="<div style='text-align:left'>可获赠"+s+"的以下赠品中的一种：</div>";
													for(var j=0;j<products.length;j++){
														ret+="<span>"+products[j].productid_name+"</span>";
														if(j==products.length-1){
															if(i!=value.length-1){
																ret+="<div style='border-bottom:1px solid #E5E5E5'></div>";
															}
														}else{
															ret+="<span>&nbsp;或&nbsp;</span>";
														}
													}
												}
											}
											return ret;
										}
									}]
						},
						renderTo : GIFT_TAB_ID,
						autoRender : true
					};
					var ecpgrid = new EcpGrid(option);
					if(data[0]){
						var str=data[0].description+":";
						var bodys=data[0].bodys;
						for(var m=0;m<bodys.length;m++){
							for(var k=0;k<bodys[m].descriptions.length;k++){
								str+=bodys[m].descriptions[k];
							}
						}
						if(str.length>30){
							str=str.substring(0, 30)+"…";
						}
						//productdesc组件和赠品页签不一定谁先加载完成
						//如果页签先加载完成，先把str存在GIFT_TAB_ID的description属性中，然后响应event_productdesc_loaded事件
						//如果productdesc先加载完成，则下面第一句就可以保证给赠品赋上值
						$(".J-largess").html($("<a></a>").css("color","black").attr("href","#").html(str));
						$("#" + GIFT_TAB_ID).attr("description",str);
					}
				}else{
					var div=$("<div></div>").html("没有找到商品赠品信息").css("text-align","center").css("color","red");
					$("#" + GIFT_TAB_ID).append(div);
				}
			},
			error:function(){
				var div=$("<div></div>").html("查询商品赠品信息出现错误").css("text-align","center").css("color","red");
				$("#" + GIFT_TAB_ID).append(div);
			}
		});
		//绑定赠品详情事件
		$(".J-largess").find("a").live("click",function(){
			tab.setActive(3);
			return false;
		});
		//商品tab页默认
		$("#tabdefault").on("click",function(){
			store.set("producttab","");
			window.location.reload();
		});
		
		seajs.on("event_productdesc_loaded",function(){
			$(".J-largess").html($("<a></a>").css("color","black").attr("href","#").html($("#" + GIFT_TAB_ID).attr("description")));
		});
	});
});