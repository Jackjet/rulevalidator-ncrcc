define(function(require, exports, module) {
	var Refer=require("refer");
	var refer = new Refer();
	var $ = require('$');
	var handfill=require("channel_handfill");
	
	var chnlRefer={
		selectMarSet:function(){
			refer.show({
				name:"请选择物料分类或者物料",
				isTab:false,
				items:[{
					configs:[{
						type:"ecppagegrid",
						config:{
							attrs : {
								isPagination : false,
								url:G.API.CHANNEL_QRYMATERIALSET,
								data:{
									pkChannelNode:$(".J-cchannelnodeid").attr("id"),
									pkProdLine:$(".J-cprodlineid").attr("id"),
									pkBrand:$(".J-cbrandid").attr("id"),
									mark:"2"
								},
								columns : [{
									label : "物料分类编码",
									key : "cmarbasclassid_code"
								},{
									label : "物料分类名称",
									key : "cmarbasclassid_name"
								},{
									label : "物料编码",
									key : "cmaterialid_code"
								},{
									label : "物料名称",
									key : "cmaterialid_name"
								},{
									label : "单位",
									key : "cunitid_name"
								}]
							},
							autoRender:true
						}
					}]
				}],
				confirm:function(grid, selectedRow, selectedData){
					return handfill.add(selectedData);
				},
				isMul:true
			});
		}
	};
	
	$(".J-cmarbasclassid").live("click",function(){
		var that=this;
		refer.show({
			name:"请选择物料分类",
			isTab:false,
			items:[{
				configs:[{
					type:"ecppagegrid",
					config:{
						attrs : {
							isPagination : false,
							url:G.API.CHANNEL_QRYMATERIALSET,
							data:{
								pkChannelNode:$(".J-cchannelnodeid").attr("id"),
								pkProdLine:$(".J-cprodlineid").attr("id"),
								pkBrand:$(".J-cbrandid").attr("id"),
								mark:"0"
							},
							columns : [{
								label : "物料分类编码",
								key : "cmarbasclassid_code"
							},{
								label : "物料分类名称",
								key : "cmarbasclassid_name"
							},{
								label : "单位",
								key : "cunitid_name"
							}]
						},
						autoRender:true
					}
				}]
			}],
			confirm:function(grid, selectedRow, selectedData){
				var index=$(that).parents("tr").prevAll().size();
				return handfill.modify(selectedData,index);
			},
			isMul:false
		});
	});
	
	$(".J-cmaterialid").live("click",function(){
		var that=this;
		refer.show({
			name:"请选择物料",
			isTab:false,
			items:[{
				configs:[{
					type:"ecppagegrid",
					config:{
						attrs : {
							isPagination : false,
							url:G.API.CHANNEL_QRYMATERIALSET,
							data:{
								pkChannelNode:$(".J-cchannelnodeid").attr("id"),
								pkProdLine:$(".J-cprodlineid").attr("id"),
								pkBrand:$(".J-cbrandid").attr("id"),
								mark:"1"
							},
							columns : [{
								label : "物料编码",
								key : "cmaterialid_code"
							},{
								label : "物料名称",
								key : "cmaterialid_name"
							},{
								label : "单位",
								key : "cunitid_name"
							}]
						},
						autoRender:true
					}
				}]
			}],
			confirm:function(grid, selectedRow, selectedData){
				var index=$(that).parents("tr").prevAll().size();
				return handfill.modify(selectedData,index);
			},
			isMul : false
		});
	});
	$(".J-caimchnlnodeid").live("click",function(){
		var that=this;
		refer.show({
			name:"请选择目的渠道节点",
			isTab:false,
			items:[{
				configs:[{
					type:"ecpgrid",
					config:{
						attrs : {
							isPagination : false,
							url:G.API.CHANNEL_QRYAIMNODE,
							data:{
								cchannelnodeid:$(".J-cchannelnodeid").attr("id")
							},
							columns : [{
								label : "编码",
								key : "vcode"
							},{
								label : "名称",
								key : "vname"
							}]
						},
						autoRender:true
					}
				}]
			}],
			confirm:function(grid, selectedRow, selectedData){
				var index=$(that).parents("tr").prevAll().size();
				$(that).attr("data-key",selectedData[0].pk_channelnode).val(selectedData[0].vname);
				$(".J-caimchanltypeid").eq(index).attr("data-key",selectedData[0].cchanltypeid).val(selectedData[0].cchanltypeid_name);
				return true;
			},
			isMul : false
		});
	});
	$(".J-csupplychnlnodeid").live("click",function(){
		var that=this;
		refer.show({
			name:"请选择供货渠道节点",
			isTab:false,
			items:[{
				configs:[{
					type:"ecpgrid",
					config:{
						attrs : {
							isPagination : false,
							url:G.API.CHANNEL_QRYSUPPLYNODE,
							data:{
								cchannelnodeid:$(".J-cchannelnodeid").attr("id")
							},
							columns : [{
								label : "编码",
								key : "vcode"
							},{
								label : "名称",
								key : "vname"
							}]
						},
						autoRender:true
					}
				}]
			}],
			confirm:function(grid, selectedRow, selectedData){
				$(that).attr("data-key",selectedData[0].pk_channelnode).val(selectedData[0].vname);
				return true;
			},
			isMul : false
		});
	});
	module.exports=chnlRefer;
});