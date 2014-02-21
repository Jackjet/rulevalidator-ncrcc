define(function(require,exports,module){
	var $=require("$");
	$=require("jquery.extend")($);
	var Dialog=require("dialog");
	var math=require("maths");
	var store=require("store");
	
	exports.validate=function(isValidate){
		if(isValidate){
			var message=[];
			//1.表体行不能为空
			if($(".J-leftcontent:visible").size()==0){
				message.push("表体行不能为空");
			}
			$(".J-leftcontent:visible").each(function(index,ele){
				//取所有不能为空的字段，判断是否为空
				$(this).find("input.J-empty").each(function(){
					if($(this).attr("data-field")!="corderplanbid"){
						var value=$(this).attr("data-key")=="" ? $(this).val():$(this).attr("data-key");
						if(value==""){
							var label=$(this).parent().prev("label").text().substring(0,$(this).parent().prev("label").text().length-1);
							message.push("第"+(index+1)+"行的"+label+"为空");
						}
					}
				});
			});
			if(message.length!=0){
				var html = "<div style='margin-left:30px;'>";
				for(var i=0;i<message.length;i++){
					html+="<div>"+message[i]+"</div>";
				}
				html+="</div>";
				Dialog.close();
				Dialog.alert({title:"提示",content:html,isHtmlContent:true});
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	},
	//保存，保存并报送确认，取消报送确认
	exports.operate=function(operate,url,data,isValidate,callback){
		Dialog.loading({title:"提示",content:"正在进行操作，请稍后"});
		if(this.validate(isValidate)){
			var vbillcode=$(".J-vbillcode").text();
			Dialog.close();
			Dialog.confirm({
				title : "提示",
				content : "确定"+operate+"该要货计划？",
				confirm:function(){
					Dialog.close();
					Dialog.loading({title:"提示",content:"正在进行操作，请稍后"});
					$.ajax({
						type:"POST",
						url:url,
						dataType:"json",
						data:data,
						success:function(data){
							Dialog.close();
							if(data && data.corderplanid!=null){
								$(".J-ts").attr("data-key",data.saveTS);
								if(callback){
									callback();
								}
								Dialog.alert({title:operate+"成功",content:"单据号："+vbillcode,isHtmlContent:true});
							}else{
								Dialog.alert({title:operate+"失败",content:"失败原因为："+data.message,isHtmlContent:true});
							}
						},
						error:function(data){
							Dialog.close();
							Dialog.alert({title:operate+"失败",content:"失败原因为："+data.message,isHtmlContent:true});
						}
					});
				}
			});
		}
	};
	//点击修改按钮
	exports.update=function(){
		//表头部分
		$(".J-update").addClass("hidden");
		$(".J-save,.J-sendconfirm").removeClass("hidden");
		$(".J-vnote").removeClass("noborder");
		$(".J-findprice").removeClass("hidden");
		//表体部分
		seajs.emit("deligrid_refresh",{isEdit:true,isLoad:false});
		$(".J-td-item").addClass("edit");
		$(".J-edit").removeAttr("readonly").addClass("edit");
		return false;
	};
	//点击关闭按钮
	exports.cancel=function(){
		var that=this;
		if($(".J-change").size()==0 && $(".J-delrows").find("span").size()==0){
			Dialog.confirm({title : "提示",content : "确定后将跳转至要货计划首页?",
				confirm : function() {
					window.open(G.PAGE.ORDERPLAN,"_self");
				}
			});
		}else{
			Dialog.confirm({title : "提示",content : "当前页面存在编辑过的数据，是否保存？",
				confirm : function() {
					Dialog.close();
					that.operate("保存",G.API.ORDERPLAN_SAVE,$.customParam(that.getData()),true,function(){
						setTimeout(function(){window.open(G.PAGE.ORDERPLAN,"_self");},800);
					});
				},
				cancel:function(){
					window.open(G.PAGE.ORDERPLAN,"_self");
				}
			});
		}
		return false;
	};
	//报送确认--只改变状态
	exports.sendconfirm=function(pk,ts,callback){
		Dialog.confirm({
			title : "提示",
			type:"confirm",
			content : "确认报送确认该要货计划?",
			confirm : function() {
				Dialog.close();
				Dialog.loading({title:"提示",content:"正在进行操作，请稍后"});
				$.ajax({
					type:"POST",
					url:G.API.ORDERPLAN_CONFIRM,
					dataType:"json",
					data:{
						pkAndTSs:pk+"&"+ts
					},
					success:function(data){
						Dialog.close();
						if(data!=null){
							seajs.emit("orderplan_grid_refresh");
							if(callback){
								callback(data);
							}
							Dialog.alert({title:"提示",content:"报送确认成功",isHtmlContent:true});
						}else{
							Dialog.alert({title:"提示",content:"报送确认失败",isHtmlContent:true});
						}
					},
					error:function(data){
						Dialog.close();
						Dialog.alert({title:"提示",content:"报送确认失败",isHtmlContent:true});
					}
				});
			},
			cancel : function() {
				return true;
			}
		});
	};
	exports.exportExcel=function(pk){
		$.ajax({
			url:G.API.ORDERPLAN_EXCELEXPORT,
			data:{
				pk:pk
			},
			success:function(data){
				var filepath=data.path;
				var filename=data.name;
				window.open(G.API.DOWNLOAD+"?realPath="+encodeURI(encodeURI(filepath))+"&fileName="+encodeURI(encodeURI(filename)));
			}
		});
		return false;
	};
	exports.getData=function(){
		var dataModel={};
		dataModel.head=$(".J-corderplanid").text()+"&"+$(".J-ts").attr("data-key");
		//逐行遍历
		var dataBModels=[];
		var length=$(".J-leftcontent").length;
		for(var i=0;i<length;i++){
			var dataBModel={};
			var isChanged=false;
			//获取左边table数据
			var leftTR=$(".J-leftcontent").eq(i);
			var leftData="";
			leftTR.find(".J-change").each(function(){
				leftData+=$(this).attr("data-field")+"="+$(this).attr("data-key")+"&";
				isChanged=true;
			});
			//获取右边table的数据
			var rightTR=$(".J-rightcontent").eq(i);
			var rightData="";
			rightTR.find("td").each(function(){
				var td=this;
				$(this).find(".J-change").each(function(){
					rightData+=$(td).attr("data-date")+"="+$(this).attr("data-field")+"="+$(this).val()+"&";
					isChanged=true;
				});
			});
			if(isChanged){
				dataBModel.left=leftData;
				dataBModel.right=rightData;
				dataBModels.push(dataBModel);
			}
		}
		//删除行
		var delRows="";
		$(".J-delrows span").each(function(){
			delRows+=$(this).text()+"&";
		});
		dataModel.delRows=delRows;
		dataModel.bUIviews=dataBModels;
		dataModel.vnote=$(".J-vnote").val();
		return dataModel;
	};
	
	exports.findPrice=function(){
		var pricepara={};
		pricepara.orderplanid=$(".J-corderplanid").text();
		pricepara.confirmTS=$(".J-ts").attr("data-key");
		var leftDatas=[];
		$(".J-leftcontent").each(function(index,ele){
			var leftData=[];
			$(this).find("input").each(function(){
				leftData.push($(this).attr("data-field")+","+$(this).attr("data-key"));
			});
			leftDatas.push(leftData);
		});
		pricepara.allRows=leftDatas;
		$.ajax({
			type:"POST",
			url:G.API.ORDERPLAN_FINDPRICE,
			data:$.customParam(pricepara),
			dataType:"json",
			success:function(data){
				//询价后重新计算单价数量金额之间的关系
				for(var i=0;i<data.length;i++){
					var price=data[i] || "";
					//比较价格是否发生变化
					if($(".J-nprice").eq(i).val()!=price){
						$(".J-nprice").eq(i).attr("data-key",price).val(price).addClass("J-change");
						$(".J-rightcontent").eq(i).find("td").each(function(){
							var that=this;
							$(this).find("[data-tptype$=num]").each(function(){
								var num=$(this).val();
								var mnyfield=$(this).attr("data-tptype").replace("num","mny");
								var mny=math.mul($(".J-nprice").eq(i).val(),num);
								if($(that).find(".J-"+mnyfield).attr("data-prevalue")!=mny){
									$(that).find(".J-"+mnyfield).val(mny);
									if(!$(that).hasClass("J-totalTD")){
										$(that).find(".J-"+mnyfield).addClass("J-change");
									}
								}
							});
						});
					}
				}
				$(".J-nprice").attr("scale",store.get("context")["priceScale"]);
				seajs.emit("scale");
			}
		});
	};
});