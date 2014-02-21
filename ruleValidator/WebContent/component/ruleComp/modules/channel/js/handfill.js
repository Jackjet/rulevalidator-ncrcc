define(function(require, exports, module) {
	var context=require("context");
	var $=require("$");
	var Dialog=require("dialog");
	var formatter=require("formatter");
	$=require("jquery.custompara")($);
	$(".J-ninprice").live("blur",function(){
		compute($(this),0);
	});
	$(".J-ninnum").live("blur",function(){
		compute($(this),0);
	});
	$(".J-ninmny").live("blur",function(){
		compute($(this),0);
	});
	$(".J-noutprice").live("blur",function(){
		compute($(this),1);
	});
	$(".J-noutnum").live("blur",function(){
		compute($(this),1);
	});
	$(".J-noutmny").live("blur",function(){
		compute($(this),1);
	});
	$(".J-noutmny").live("blur",function(){
		compute($(this),1);
	});
	$(".J-nsumnum").live("blur",function(){
		compute($(this),2);
	});
	//价格数量金额三算二
	var compute=function(element,type){
		if(element.val()!="" && !/^\d{1,18}(\.\d{1,8})?$/.test(element.val())){
			Dialog.alert({title:"提示",content:"请输入一个正确格式的数字，整数部分不能超过18位，小数部分不能超过8位"});
			element.val(element.attr("data-key"));
			seajs.emit("scale");
			return false;
		}
		element.attr("data-key",element.val());
		seajs.emit("scale");
		var tr=element.parents("tr");
		var priceElement,numElement,mnyElement;
		if(type==0){
			//入库数据的自动换算
			priceElement=tr.find(".J-ninprice");
			numElement=tr.find(".J-ninnum");
			mnyElement=tr.find(".J-ninmny");
			
		}else if(type==1){
			//出库数据的自动换算
			priceElement=tr.find(".J-noutprice");
			numElement=tr.find(".J-noutnum");
			mnyElement=tr.find(".J-noutmny");
		}else{
			var sumnum=element.val()==""?"":formatter.fmtNum(element.val(),element.attr("chnlscale"));
			element.val(sumnum);
			return;
		}
		var priceScale=priceElement.attr("chnlscale");
		var numScale=numElement.attr("chnlscale");
		var mnyScale=mnyElement.attr("chnlscale");
		var price=priceElement.val()==""?"":formatter.fmtNum(priceElement.val(),priceScale);
		var num=numElement.val()==""?"":formatter.fmtNum(numElement.val(),numScale);
		var mny=mnyElement.val()==""?"":formatter.fmtNum(mnyElement.val(),mnyScale);
		//1.三个都为空
		if(price=="" && num=="" && mny==""){
			return;
		}
		//2.任意两个为空
		if((price=="" && num=="") || (price=="" && mny=="") || (num=="" && mny=="")){
			priceElement.val(price);
			numElement.val(num);
			mnyElement.val(mny);
		}
		//3.单价数量不为空，金额为空
		if(price!="" && num!="" && mny==""){
			priceElement.val(price);
			numElement.val(num);
			mnyElement.val(formatter.fmtNum(price*num,mnyScale));
		}
		//4.单价金额不为空，数量为空
		if(price!="" && mny!="" && num==""){
			if(parseInt(price)==0){
				priceElement.val(0,priceScale);
				numElement.val(formatter.fmtNum(0,numScale));
				mnyElement.val(formatter.fmtNum(0,mnyScale));
			}else{
				priceElement.val(price);
				numElement.val(formatter.fmtNum(mny/price,numScale));
				mnyElement.val(mny);
			}
		}
		//5.数量金额不为空，单价为空
		if(num!="" && mny!=""){
			if(parseInt(num)==0){
				priceElement.val("");
				numElement.val("");
				mnyElement.val("");
			}else{
				priceElement.val(formatter.fmtNum(mny/num,priceScale));
				numElement.val(num);
				mnyElement.val(mny);
			}
		}
		seajs.emit("scale");
	};
	
	exports.add=function(selectedData){
		var messages="";
		var length=0;
		//判断重复
		for(var i=0,index=1;i<selectedData.length;i++){
			var field1=selectedData[i].cmarbasclassid ? "cmarbasclassid" : "cmaterialid";
			if($("."+field1+"_"+selectedData[i][field1]).size()!=0){
				messages+="<div>第"+index+"行所选的物料分类或物料已经存在</div>";
				selectedData.splice(i--,1);
			}else{
				length++;
			}
			index++;
		}
		if(messages!="" && selectedData.length==0){
			Dialog.alert({
				title:"提示",
				isHtmlContent:true,
				content:"所选的物料分类或物料已经存在，请重新选择</div>"
			});
			return false;
		}else if(messages!="" && selectedData.length!=0){
			Dialog.alert({
				title:"提示",
				isHtmlContent:true,
				content:"<div style='margin-left:30px;'>"+messages+"</div>"
			});
		}
		//新增行
		var oldRowIndex=$(".ui-ecpeditgrid tbody tr").size();
		seajs.emit("add_row",length);
		//处理字段
		for(var j=0,eleIndex=oldRowIndex;j<selectedData.length;j++){
			var data=selectedData[j];
			var field1=selectedData[j].cmarbasclassid ? "cmarbasclassid" : "cmaterialid";
			var field2=selectedData[j].cmarbasclassid ? "cmaterialid" : "cmarbasclassid";
			//设置物料或者物料分类的值
			$(".J-"+field1).eq(eleIndex).attr("data-key",data[field1]).val(data[field1+"_code"]).addClass(field1+"_"+data[field1]);
			$(".J-"+field1+"_name").eq(eleIndex).val(data[field1+"_name"]);
			//设置单位字段的值
			$(".J-cunitid").eq(eleIndex).attr("data-key",data.cunitid).val(data.cunitid_name);
			$(".ui-ecpeditgrid tbody tr").eq(eleIndex).find(".J-ninnum,.J-noutnum,.J-nsumnum").attr("scale",data.cunitid_unitScale).attr("chnlscale",data.cunitid_unitScale);
			$(".ui-ecpeditgrid tbody tr").eq(eleIndex).find(".J-ninmny,.J-noutmny").attr("scale",$(".J-ccurrencyid").attr("scale")).attr("chnlscale",$(".J-ccurrencyid").attr("scale"));
			//将互斥字段置为不可编辑
			$(".J-"+field2).eq(eleIndex).attr("data-key","").val("");
			$(".J-"+field2+"_name").eq(eleIndex).attr("data-key","").val("");
			$(".ui-ecpeditgrid tbody tr").eq(eleIndex).find(".J-ninprice,.J-noutprice,.J-ninmny,.J-noutmny,.J-ninnum,.J-noutnum,.J-nsumnum").removeClass("noborder").removeAttr("disabled");
			eleIndex++;
		}
		seajs.emit("scale");
		return true;
	};
	
	exports.modify=function(selectedData,index){
		//判断重复
		var data=selectedData[0];
		var field1=data.cmarbasclassid ? "cmarbasclassid" : "cmaterialid";
		var field2=data.cmarbasclassid ? "cmaterialid" : "cmarbasclassid";
		if($("."+field1+"_"+data[field1]).size()!=0){
			Dialog.alert({
				title:"提示",
				isHtmlContent:true,
				content:"所选的物料分类或物料已经存在，请重新选择"
			});
			return false;
		}
		//先将之前的"material_id值"这种class去掉，然后添加新的
		var classname1=field1+"_"+$(".J-"+field1).eq(index).attr("data-key");
		//设置物料或者物料分类的值
		$(".J-"+field1).eq(index).attr("data-key",data[field1]).val(data[field1+"_code"]).removeClass(classname1).addClass(field1+"_"+data[field1]);
		$(".J-"+field1+"_name").eq(index).val(data[field1+"_name"]);
		//设置单位字段的值
		$(".J-cunitid").eq(index).attr("data-key",data.cunitid).val(data.cunitid_name);
		$(".ui-ecpeditgrid tbody tr").eq(index).find(".J-ninnum,.J-noutnum,.J-nsumnum").attr("scale",data.cunitid_unitScale).attr("chnlscale",data.cunitid_unitScale);
		//处理互斥字段
		var classname2=field2+"_"+$(".J-"+field2).eq(index).attr("data-key");
		$(".J-"+field2).eq(index).attr("data-key","").val("").removeClass(classname2);
		$(".J-"+field2+"_name").eq(index).attr("data-key","").val("");
		$(".ui-ecpeditgrid tbody tr").eq(index).find(".J-ninprice,.J-noutprice,.J-ninmny,.J-noutmny,.J-ninnum,.J-noutnum,.J-nsumnum").removeClass("noborder").removeAttr("disabled");
		seajs.emit("scale");
		return true;
	};
	
	//验证不允许存在相同的物料或者物料分类
	exports.validateSame=function(selectedData){
		var value=selectedData[field];
		if($("."+field+"_"+value).size()!=0){
			alert("不允许存在相同的物料或物料分类");
 			return false;
		}
	 	return true;
	};
	
	//获取数据
	exports.getData=function(){
		var data={};
    	var trs=$("#id_center_content").find("tbody tr");
    	var bDatas=[];
    	$.each(trs,function(){
    		var temp={};
    		var pk_b=$(this).attr("data-key");
    		if(pk_b){
    			temp[$(this).attr("data-field")]=pk_b;
    		}
    		var inputs=$(this).find(".J-commit");
    		$.each(inputs,function(){
    			var field=$(this).attr("data-field");
    			temp[field]=$(this).attr("isNum")=="true" ? $(this).val() : $(this).attr("data-key") || "";
    		});
    		bDatas.push(temp);
    	});
    	if($(".J-pk_chanlsend_h").text()){
    		//渠道报送表
    		data["pk_chanlsend_h"]=$(".J-pk_chanlsend_h").text();
    		data["chnlSendBillBUIViews"]=bDatas;
    	}else{
    		//期初报送表
    		data["pk_beginbill_h"]=$(".J-pk_beginbill_h").text();
    		data["chnlBeginBUIViews"]=bDatas;
    	}
    	return data;
	};
	exports.operate=function(url,callback,title,forwardUrl){
		var data=this.getData();
		var size=$("#id_center_content").find("tbody tr").size();
    	if(size==0){
    		Dialog.alert({
				title:"提示",
				content:"表体行不能为空"
			});
	    	return false;
    	}
    	if(size>500){
    		if(forwardUrl){
    			Dialog.alert({
    				title:"提示",
    				content:"表体行超过500行，建议您使用Excel导入方式进行填报或修改会更快捷，如仅报送确认，请返回前页列表处报送确认，谢谢！"
    			});
    		}else{
    			Dialog.alert({
    				title:"提示",
    				content:"表体行超过500行，建议您使用Excel导入方式进行填报或修改会更快捷，谢谢！"
    			});
    		}
    		return false;
    	}
    	var message="";
    	$("tbody tr").each(function(i,value){
    		if(!$(this).find(".J-cunitid").attr("data-key")){
    			message+="<div style='margin-left:30px;'>第"+(i+1)+"行的单位为空</div>";
    		}
    	});
    	if(message!=""){
    		Dialog.alert({
				title:"提示",
				isHtmlContent:true,
				content:message
			});
    		return false;
    	}
    	Dialog.loading({
			title:"提示",
			content:"正在进行操作，请稍后"
		});
    	if(context.get("csellerid")){
    		data["csellerid"]=context.get("csellerid");
    	}
    	$.ajax({
	    	url:url,
	    	data:$.customParam(data),
	    	type:'POST', 
	    	success:function(result){
    			var flag=true;
    			Dialog.close();
    			var htmlContent;
				if(result.pk_chanlsend_h!=null || (result[0] && result[0].pk_chanlsend_h!=null) || 
						result.pk_beginbill_h!=null || (result[0] && result[0].pk_beginbill_h!=null)){
					title+="成功";
					if(forwardUrl){
						htmlContent="单据号<br><br><a class='handoperate' href='"+forwardUrl+"'>"+$(".J-vbillcode").text()+"</a></div>";
					}else{
						htmlContent=title+"。单据号为："+$(".J-vbillcode").text();
					}
				}else{
					title+="失败";
					htmlContent=result.message;
					flag=false;
				}
    			Dialog.alert({
					title:title,
					isHtmlContent:true,
					content:htmlContent
				});
    			if(flag){
    				callback();
    			}
    		},
    		error:function(result){
				Dialog.alert({
					title:"提示",
					content:result.message
				});
    		}
		});
	};
	//报送确认,只改变状态
	exports.confirm=function(data,url,pkField,title,forwardUrl){
		$.ajax({
			url:url,
			type:"POST",
			data:data,
			dataType:"json",
			success:function(data){
				if(data!=null && data[0] && data[0][pkField]!=null){
					Dialog.alert({
						title:title+"报送确认成功",isHtmlContent:true,content:"单据号<br><br><a class='handoperate' href='"+forwardUrl+"'>"+$(".J-vbillcode").text()+"</a></div>"
					});
					$(".J-fstatusflag").text("报送确认").attr("id","2");
        			$(".J-unconfirm").removeClass("hidden");
        			$(".J-confirm").addClass("hidden");
				}else{
					Dialog.alert({
						title:title+"报送确认失败",content:data.message
					});
				}
			}
		});
	};
	//取消报送确认
	exports.unconfirm=function(data,url,pkField,title,forwardUrl){
		$.ajax({
			url:url,
			type:"POST",
			data:data,
			dataType:"json",
			success:function(data){
				if(data!=null && data[pkField]!=null){
					Dialog.alert({
						title:title+"取消报送确认成功",isHtmlContent:true,content:"单据号<br><br><a class='handoperate' href='"+forwardUrl+"'>"+$(".J-vbillcode").text()+"</a></div>"
					});
					$(".J-fstatusflag").text("自由").attr("id","1");
        			$(".J-unconfirm").addClass("hidden");
        			$(".J-confirm").removeClass("hidden");
				}else{
					Dialog.alert({
						title:title+"取消报送确认失败",content:data.message
					});
				}
			}
		});
	};
	exports.fmtNumTD=function(key,value,scale){
		return "<input type='text' class='J-"+key+"' data-field='"+key+"' value='"+value+"' " +
				"data-key='"+value+"' scale='"+scale+"' chnlscale='"+scale+"' isNum='true' empty='true'>";
	};
});