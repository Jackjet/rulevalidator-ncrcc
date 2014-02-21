/**
 * 用于生成左边和右边的行
 */
define(function(require, exports, module) {
	var tools={
		geneLeft:function(context){
			var ret="";
			if(context.model){
				var leftcontent=context.model.left || [];
				var series=context.model.series || [];
				var isEdit=context.attrs.isEdit;
				var left_structure=context.model.left_structure || [];
				var tdHeight=context.attrs.tdHeight;
				var lineHeight=tdHeight/series.length;
				for(var i=0;i<leftcontent.length;i++){
					var rowmark="";//行标记，标记当前一行以方便判断重复，规则是所有计划客体的值拼成串，中间已&分隔开
					var rowStr="<td class='baseinfo' style='height:"+tdHeight+"px'>";
					rowStr+="<div class='baseinfo_left'>";
					var leftItems=leftcontent[i];
					for(var k=0;k<leftItems.length;k++){
						var dataKey=leftItems[k].key || leftItems[k].value || "";
						var value=leftItems[k].value || "";
						if(left_structure[k].isShow && isEdit){
							rowStr+="<div class='J-td-item edit'>";
						}else if(left_structure[k].isShow){
							rowStr+="<div class='J-td-item'>";
						}else{
							rowStr+="<div class='J-td-item hidden'>";
						}
						rowStr+="<label>"+leftItems[k].label+"：</label>";
						rowStr+="<span>";
						if(left_structure[k].format){
							rowStr+=left_structure[k].format(leftItems[k],leftItems);
						}else{
							var editable=leftItems[k].isEditable ? "J-edit edit" : "noedit";//是否可以编辑
							var readonly=leftItems[k].isEditable ? "" : "readonly='readonly'";//是否可以编辑
							var object=leftItems[k].isobject ? " J-object" : "";//是否是计划客体
							if(leftItems[k].isobject){
								rowmark+=dataKey || "new";
							}
							var empty=leftItems[k].isnull ? "" : " J-empty";//是否可以为空，不能为空的要打上J-empty的标记
							rowStr+="<input id='"+dataKey+"' type='text' class='J-change "+editable+object+empty+" J-"+leftItems[k].field+"' data-field='"+leftItems[k].field+"'"+
								" data-key='"+dataKey+"' value='"+value+"' empty='true' "+readonly+">";
						}
						rowStr+="</span>";
						rowStr+="</div>";
					}
					rowStr+="</div>";
					rowStr+="<div class='baseinfo_right'>";
					rowStr+=isEdit ? "<div class='del-icon'>" : "<div class='del-icon hidden'>";
					rowStr+="<a href='#' class='J-del edit'></a>";
					rowStr+="</div>";
					rowStr+="</div>";
					rowStr+="</td>";
					rowStr+="<td class='series' style='height:"+tdHeight+"px'>";
					for(var j=0;j<series.length;j++){
						var mark=j%2==0 ? "even" : "odd";
						rowStr+="<div style='height:"+lineHeight+"px;line-height:"+lineHeight+"px' class='series-td-item "+mark+"'>"+series[j].label+"</div>";
					}
					rowStr+="</td>";
					rowStr+="</tr>";
					if(i==0){
						rowStr="<tr id='J-first-baseinfo' class='J-leftcontent J-"+rowmark+"'>"+rowStr;
					}else{
						rowStr="<tr class='J-leftcontent J-"+rowmark+"'>"+rowStr;
					}
					ret+=rowStr;
				}
			}
			return ret;
		},
		geneRight:function(context){
			var ret="";
			var isEdit=context.isEdit || context.attrs.isEdit;
			if(context.model){
				var series=context.series || context.model.series;
				var tdHeight=context.attrs.tdHeight || context.tdHeight;
				var lineHeight=tdHeight/series.length;
				var rightcontent=context.model.right ? context.model.right.body : context.model;
				for(var i=0;i<rightcontent.length;i++){
					ret+="<tr class='J-rightcontent'>";
					var tr=rightcontent[i];
					for(var j=0;j<tr.length;j++){
						var td=tr[j];
						var tpData=td.orderplan_tp_data;
						if(td.type=="total"){
							ret+="<td style='height:"+tdHeight+"px' class='J-totalTD area_"+td.orderplan_area+"'>";
							for(var k=0;k<tpData.length;k++){
								var value=tpData[k].value || "";
								ret+=k%2==0 ? "<div style='height:"+lineHeight+"px;line-height:"+lineHeight+"px' class='right-td-item even'>" : "<div style='height:"+lineHeight+"px;line-height:"+lineHeight+"px' class='right-td-item odd'>";
								ret+="<input class='noedit J-"+tpData[k].type+"' type='text' value='"+value+"' data-prevalue='"+value+"' readonly='readonly'"+ 
										" data-tptype='"+tpData[k].type+"' data-field='"+tpData[k].key+"' scale='"+tpData[k].amount_scale+"' empty='true'>";
								ret+="</div>";
							}
						}else{
							ret+="<td style='height:"+tdHeight+"px' class='area_"+td.orderplan_area+"' data-date='"+td.orderplan_tp_begin_date+"'>";
							for(var k=0;k<tpData.length;k++){
								ret+=k%2==0 ? "<div style='height:"+lineHeight+"px;line-height:"+lineHeight+"px' class='right-td-item J-td-item even" : "<div style='height:"+lineHeight+"px;line-height:"+lineHeight+"px' class='right-td-item J-td-item odd";
								ret+=isEdit ? " edit'>" : "'>";
								var value=tpData[k].value || "";
								if(tpData[k].isEditable && isEdit){
									ret+="<input type='text' class='J-edit edit J-"+tpData[k].type+"' value='"+value+"' data-prevalue='"+value+
										"' data-tptype='"+tpData[k].type+"' data-field='"+tpData[k].key+"' scale='"+tpData[k].amount_scale+"' empty='true'>";
								}else if(tpData[k].isEditable){
									ret+="<input type='text' class='J-edit noedit J-"+tpData[k].type+"' value='"+value+"' data-prevalue='"+value+
										"' readonly='readonly' data-tptype='"+tpData[k].type+"' data-field='"+tpData[k].key+"' scale='"+tpData[k].amount_scale+"' empty='true'>";
								}else{
									ret+="<input type='text' class='noedit J-"+tpData[k].type+"' value='"+value+"' data-prevalue='"+value+
									"' readonly='readonly' data-tptype='"+tpData[k].type+"' data-field='"+tpData[k].key+"' scale='"+tpData[k].amount_scale+"' empty='true'>";
								}
								ret+="</div>";
							}
						}
						ret+="</td>";
					}
					ret+="</tr>";
				}
			}
			return ret;
		}
	};
	module.exports=tools;
});