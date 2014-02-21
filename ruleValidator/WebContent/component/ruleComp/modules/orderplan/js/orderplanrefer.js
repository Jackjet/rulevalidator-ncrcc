define(function(require, exports, module) {
	var Refer=require("refer");
	var refer = new Refer();
	var $ = require('$');
	var math=require("maths");
	
	var referConfigs={
		REFER_PRODUCT:{
			name:"请选择商品",
			isTab:true,
			items:[{
				id:"tab1",
				title:"我的收藏",
				active:true,
				configs:[{
					id:"favorite_form",
					type:"form",
					config:{
						attrs:{
							id:'favorite_form',
							items:[{
								label :'标签',
								name : 'tag',
								type : 'select',
								options : "CODENAME_FAVORITE_TAG"
							},{
								name : 'isDesc',
								type : 'hidden',
								value: true
							}],
							buttons : [{
								text : '查询',
								id : 'submit',
								handler : function() {
									var grid=refer.getComponent("favorite_grid");
									grid.query($('#favorite_form'));
									return false;
								}
							},{
								text : '重置',
								type : 'reset'
							}]
						}
					}
				},{
					id:"favorite_grid",
					type:"ecppagegrid",
					config:{
						attrs : {
							isPagination : true,
							url : G.API.ACCOUNT_FAVORITE,
							columns : [{
								label : "商品编码",
								key : "product.vcode",
								align : "left"
							},{
								label : "商品名称",
								key : "product.vname",
								align : "left"
							},{
								label : "当前价",
								align : "left",
								format:function(value,row){
									return "<span id='product_price"+row.productId+"' class='font_bold based nc-no-float' ectype='price' "+
											"productId='"+row.productId+"'></span>";
								}
							},{
								label : "规格",
								key : "product.materialspec",
								align : "left"
							},{
								label : "型号",
								key : "product.materialtype",
								align : "left"
							}]
						},
						autoRender : true
					}
				}]
			},{
				id:"tab2",
				title:"商品列表",
				configs:[{
					id:"product_form",
					type:"form",
					config:{
						attrs : {
							id : "product_form",
							items : [{
								label : "商品分类",
								name : "categoryID",
								type : "select",
								options : "CODENAME_PRODUCT_CATEGORY"
							}, {
								label : "商品品牌",
								name : "brandID",
								type : "select",
								options : "CODENAME_PRODUCT_BRAND"
							}, {
								label : "产品线",
								name : "prodLineID",
								type : "select",
								options : "CODENAME_PRODUCT_PRODUCTLINE"
							}, {
								label : "商品编码",
								name : 'productCode',
								type : "text"
							}, {
								label : "商品名称",
								name : 'productName',
								type : "text"
							}],
							buttons : [{
								text : "查询",
								id : "submit",
								handler : function() {
									if ($(".J-categoryID").val() == "" && $(".J-brandID").val() == "" && $(".J-prodLineID").val() == ""
											&& $(".J-productCode").val() == "" && $(".J-productName").val() == "") {
										alert("请选择一个查询条件!");
										return false;
									}
									var grid=refer.getComponent("product_grid");
									grid.query($("#product_form"));
									return false;
								}
							},{
								text : "重置",
								type : "reset"
							}]
						}
					}
				},{
					id:"product_grid",
					type:"ecppagegrid",
					config:{
						attrs : {
							isPagination : true,
							pagesize:100,
							url : G.API.PRODUCT_LIST,
							columns : [{
								label : "商品编码",
								key : "vcode",
								align : "left"
							},{
								label : "商品名称",
								key : "vname",
								align : "left"
							},{
								label : "当前价",
								align : "left",
								format:function(value,row){
									return "<span id='product_price"+row.cproductid+"' class='font_bold based nc-no-float' ectype='price' "+
										"productId='"+row.cproductid+"'></span>";
								}
							},{
								label : "规格",
								key : "materialspec",
								align : "left"
							},{
								label : "型号",
								key : "materialtype",
								align : "left"
							}]
						},
						autoRender : true
					}
				}]
			}]
		},
		REFER_MATERIAL:{
			name:"请选择物料",
			isTab:false,
			items:[{
				configs:[{
					id:"matclass_tree",
					type:"tree",
					config:{
						url:G.API.ORDERPLAN_QRYMATCLASS,
						onClick:function(event, treeId, treeNode){
							seajs.emit("ECPPAGEGRID_QUERY",{
								name:"pk_materialclassid",
								value:treeNode.id
							});
						}
					},
					width:"27%"
				},{
					id:"matclass_grid",
					type:"ecppagegrid",
					config:{
						attrs : {
							isPagination : true,
							url:G.API.ORDERPLAN_QRYMATERIAL,
							columns : [{
								label : "编码",
								key : "code"
							},{
								label : "名称",
								key : "name"
							},{
								label : "规格",
								key : "materialspec"
							},{
								label : "型号",
								key : "materialtype"
							}]
						},
						autoRender : true
					},
					width:"65%"
				}]
			}]
		},
		REFER_MARBASCLASS:{
			name:"请选择物料分类",
			width:500,
			isTab:false,
			items:[{
				configs:[{
					id:"matclass_tree",
					type:"tree",
					config:{
						url:G.API.ORDERPLAN_QRYMATCLASS
					}
				}]
			}]
		},
		REFER_PRODLINE:{
			name:"请选择产品线",
			width:600,
			isTab:false,
			items:[{
				configs:[{
					id : "prodline_form",
					type:"form",
					config:{
						attrs:{
							items : [{
								label : "编码",
								name : 'productCode',
								type : "text"
							},{
								label : "名称",
								name : 'productName',
								type : "text"
							}],
							buttons : [{
								text : "查询",
								id : "submit",
								handler : function() {
									var grid=refer.getComponent("product_grid");
									grid.query($("#prodline_form"));
									return false;
								}
							},{
								text : "重置",
								type : "reset"
							}]
						}
					}
				},{
					id:"prodline_grid",
					type:"ecpgrid",
					config:{
						attrs : {
							url:G.API.ORDERPLAN_QRYPRODLINE,
							columns : [{
								label : "编码",
								key : "code"
							},{
								label : "名称",
								key : "name"
							}]
						},
						autoRender:true
					}
				}]
			}]
		},
		REFER_BRAND:{
			name:"请选择品牌",
			width:600,
			isTab:false,
			items:[{
				configs:[{
					id:"brand_grid",
					type:"ecpgrid",
					config:{
						attrs : {
							url:G.API.ORDERPLAN_QRYBRAND,
							columns : [{
								label : "编码",
								key : "code"
							},{
								label : "名称",
								key : "name"
							}]
						},
						autoRender:true
					}
				}]
			}]
		}
	};
	
	var orderplanrefer={
		/**
		 * 参照确定
		 * selectedData:选择的数据;element:点击的元素;field:点击的字段;pkFiled:主键的字段;namefield:名称的字段;type:标志是什么计划客体
		 */
		referConfirm:function(selectedRow,selectedData,element,field,pkfield,namefield,type){
			var messages=[];
			//(1)构建id数组
			var pkStr="";
			var pkOrgStr="";
			for(var i=0;i<selectedData.length;i++){
				pkStr=selectedData[i][pkfield];
				pkOrgStr=selectedData[i][pkfield]+"&"+$(".J-pk_org").attr("data-key");
			}
			//(2)根据计划客体id查询单位和发货库存组织
			$.ajax({
				url:G.API.QUERT_UNITANDSTOCK,
				dataType:"json",
				type:"POST",
				data:{
					field:field,
					template:$(".J-cplantemplateid").attr("data-key"),
					ids:pkStr,
					pkOrgs:pkOrgStr,
					mark:type
				},
				success:function(data){
					var results=[];
					var size=$(".J-leftcontent").size();
					var mnyscale=$(".J-corigcurrencyid").attr("scale");
					//去除重复,因为中间有重复的行，所以需要用dataIndex来表示当前这条数据对应第几个新增行
					for(var i=0,dataIndex=0;i<selectedData.length;i++){
						var mark=selectedData[i][pkfield];
						//获取单位的值
						var unit=data.unit || selectedData[i];
						var cunitid=unit[mark+"_unit_pk"] || unit.pk_measdoc || "";
						var cunitid_name=unit[mark+"_unit_name"] || unit.pk_measdoc_name || "";
						var cunitid_scale=unit[mark+"_unit_scale"] || unit.pk_measdoc_unitScale;
						//获取库存组织的值
						var orgkey="";
						if(type=="0" || type=="1" || type=="2"){
							orgkey=pkOrgStr;
						}else{
							orgkey=$(".J-pk_org").attr("data-key");
						}
						var stockOrg=data.stock || {};
						var stockOrg_key=stockOrg[orgkey+"pk"] || $(".J-pk_org").attr("data-key");
						var stockOrg_name=stockOrg[orgkey+"name"] || $(".J-pk_org").text();
						//如果发货库存组织是计划客体
						if($(".J-cstockorgid").hasClass("J-object")){
							mark+=stockOrg;
						}
						//如果客户订单号是计划客体
						if($(".J-vcustorderno").hasClass("J-object") && element){
							mark+=element.parents("td").find(".J-vcustorderno").attr("data-key");
						}
						if($(".J-"+mark).size()!=0){
							messages.push(selectedData[namefield]+"已经存在");
						}else{
							var tdIndex=element ? element.parents("tr").prevAll().size() : size+dataIndex;
							var result={
								tdIndex:tdIndex,
								pk:selectedData[i][pkfield],
								name:selectedData[i][namefield],
								price:selectedRow[i].find(".J-saleprice").text() || "",
								cunitid:cunitid,
								cunitid_name:cunitid_name,
								cunitid_scale:cunitid_scale,
								stockOrg_key:stockOrg_key,
								stockOrg_name:stockOrg_name,
								mark:mark
							};
							results.push(result);
							dataIndex++;
						}
					}
					//增行
					if(!element){
						seajs.emit("deligrid_add",results.length);
					}
					//设置值
					for(var j=0;j<results.length;j++){
						var td =$(".J-leftcontent").eq(results[j].tdIndex).find(".baseinfo");
						//设置商品或物料或物料分类或产品线或品牌的键和值
						td.find(".J-"+field).attr("data-key",results[j].pk).val(results[j].name).attr("id",results[j].pk);
						//设置单位的值,要保证没有改变的单位不上传
						td.find(".J-castunitid").attr("data-key",results[j].cunitid).val(results[j].cunitid_name);
						//设置发货库存组织的值,要保证没有改变的发货库存组织不上传
						td.find(".J-cstockorgid").attr("data-key",results[j].stockOrg_key).val(results[j].stockOrg_name);
						//设置右边区域的精度
						var index=element ? element.parents("tr").prevAll().size() : results[j].tdIndex;
						$(".right-content tr").eq(index).find("input[data-tptype$=num]").attr("scale",results[j].cunitid_scale);
						$(".right-content tr").eq(index).find("input[data-tptype$=mny]").attr("scale",mnyscale);
						//设置价格
						var price=results[j].price;
						td.find(".J-nprice").attr("data-key",price).val(price);
						//重新计算,并修改此行的标志
						if(element){
							$(".J-rightcontent").eq(index).find("td").each(function(){
								var td=this;
								$(this).find("[data-tptype$=num]").each(function(){
									var num=$(this).val();
									var mnyfield=$(this).attr("data-tptype").replace("num","mny");
									var mny=math.mul($(".J-nprice").eq(index).val(),num);
									if($(td).find(".J-"+mnyfield).attr("data-prevalue")!=mny){
										$(td).find(".J-"+mnyfield).val(mny);
										if(!$(td).hasClass("J-totalTD")){
											$(td).find(".J-"+mnyfield).addClass("J-change");
										}
									}
								});
							});
							element.parents("tr").attr("class","J-leftcontent J-"+results[j].mark);
						}
					}
					seajs.emit("scale");
				}
			});
		},
		modify:function(selectedRow,selectedData,element,field,pkfield,namefield,type){
			this.referConfirm(selectedRow,selectedData, element, field, pkfield, namefield, type);
			return true;
		},
		batchAdd:function(selectedRow,selectedData,field,pkfield,namefield,type){
			this.referConfirm(selectedRow,selectedData, null, field, pkfield, namefield, type);
			return true;
		}
	};
	
	//批量新增商品
	seajs.on("batchAddProduct",function(){
		refer.show($.extend(true,referConfigs.REFER_PRODUCT,{isMul:true,confirm:function(grid, selectedRow, selectedData){
			var result=[];
			for(var i=0;i<selectedData.length;i++){
				result.push(selectedData[i].product || selectedData[i]);
			}
			return orderplanrefer.batchAdd(selectedRow,result,"cproductid","cproductid","vname","0");
		}}));
	});
	//批量增加物料
	seajs.on("batchAddMaterial",function(){
		refer.show($.extend(true,referConfigs.REFER_MATERIAL,{isMul:true,confirm:function(grid, selectedRow, selectedData){
			return orderplanrefer.batchAdd(selectedRow,selectedData,"cmaterialvid","pk_material","name","1");
		}}));
	});
	//批量新增物料分类
	seajs.on("batchAddMarBas",function(){
		refer.show($.extend(true,referConfigs.REFER_MARBASCLASS,{isMul:true,confirm:function(grid, selectedRow, selectedData){
			return orderplanrefer.batchAdd(selectedRow,selectedData,"cmarbasclassid","pk_marbasclass","name","3");
		}}));
	});
	//批量新增产品线
	seajs.on("batchAddProdLine",function(){
		refer.show($.extend(true,referConfigs.REFER_PRODLINE,{isMul:true,confirm:function(grid, selectedRow, selectedData){
			return orderplanrefer.batchAdd(selectedRow,selectedData,"cprodlineid","pk_prodline","name","2");
		}}));
	});
	//批量新增品牌
	seajs.on("batchAddBrand",function(){
		refer.show($.extend(true,referConfigs.REFER_BRAND,{isMul:true,confirm:function(grid, selectedRow, selectedData){
			return orderplanrefer.batchAdd(selectedRow,selectedData,"cbrandid","pk_brand","name","4");
		}}));
	});
	//修改某行商品
	$(".J-cproductid.edit").live("click",function(){
		var that=this;
		refer.show($.extend(true,referConfigs.REFER_PRODUCT,{isMul:false,confirm:function(grid, selectedRow, selectedData){
			var result=selectedData[0].product ? [selectedData[0].product] : selectedData;
			return orderplanrefer.modify(selectedRow,result, $(that), "cproductid", "cproductid", "vname","0");
		}}));
	});
	//修改某行物料
	$(".J-cmaterialvid.edit").live("click",function(){
		var that=this;
		refer.show($.extend(true,referConfigs.REFER_MATERIAL,{isMul:false,confirm:function(grid, selectedRow, selectedData){
			return orderplanrefer.modify(selectedRow,selectedData, $(that), "cmaterialvid", "pk_material", "name","1");
		}}));
	});
	//修改某行物料分类
	$(".J-cmarbasclassid.edit").live("click",function(){
		var that=this;
		refer.show($.extend(true,referConfigs.REFER_MARBASCLASS,{isMul:false,confirm:function(grid, selectedRow, selectedData){
			return orderplanrefer.modify(selectedRow,selectedData, $(that), "cmarbasclassid", "pk_marbasclass", "name","3");
		}}));
	});
	
	//修改某行产品线
	$(".J-cprodlineid.edit").live("click",function(){
		var that=this;
		refer.show($.extend(true,referConfigs.REFER_PRODLINE,{isMul:false,confirm:function(grid, selectedRow, selectedData){
			return orderplanrefer.modify(selectedRow,selectedData, $(that), "cprodlineid", "pk_prodline", "name","2");
		}}));
	});
	//修改某行品牌
	$(".J-cbrandid.edit").live("click",function(){
		var that=this;
		refer.show($.extend(true,referConfigs.REFER_BRAND,{isMul:false,confirm:function(grid, selectedRow, selectedData){
			return orderplanrefer.modify(selectedRow,selectedData, $(that), "cbrandid", "pk_brand", "name","4");
		}}));
	});
	//修改发货库存组织
	$(".J-cstockorgid.edit").live("click",function(){
		var that=this;
		var td=$(that).parents("td");
		var id="";
		var mark="";
		if(td.find(".J-cproductid").size()!=0){
			id=td.find(".J-cproductid").attr("data-key");
			mark="0";
		}else if(td.find(".J-cmaterialvid").size()!=0){
			id=td.find(".J-cmaterialvid").attr("data-key");
			mark="1";
		}else if(td.find(".J-cprodlineid").size()!=0){
			id=td.find(".J-cprodlineid").attr("data-key");
			mark="2";
		}else if(td.find(".J-cbrandid").size()!=0){
			id=td.find(".J-cbrandid").attr("data-key");
			mark="4";
		}else if(td.find(".J-cmarbasclassid").size()!=0){
			id=td.find(".J-cmarbasclassid").attr("data-key");
			mark="3";
		}
		refer.show({
			name:"请选择发货库存组织",
			isTab:false,
			isMul:false,
			width:500,
			items:[{
				configs:[{
					id:"stock_grid",
					type:"ecpgrid",
					config:{
						attrs : {
							url:G.API.ORDERPLAN_QRYSTOCK,
							columns : [{
								label : "名称",
								key : "name"
							}]
						},
						autoRender:false
					}
				}]
			}],
			confirm:function(grid, selectedRow, selectedData){
				$(that).parents("td").find(".J-cstockorgid").attr("data-key",selectedData[0].pk).attr("id",selectedData[0].pk).val(selectedData[0].name).attr("changed","true");
				return true;
			}
		});
		var grid=refer.getComponent("stock_grid");
		grid.refresh({
			"pkOrg":$(".J-pk_org").attr("data-key"),
			"pk":id,
			"mark":mark
		});
	});
});