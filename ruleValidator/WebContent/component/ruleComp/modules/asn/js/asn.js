/**
 * 订单签收管理
 */
define(function(require, exports, module) {
	require('../theme/{theme}/css/asn.css');
	
	var dialog = require('dialog');
	var $ = require("$");
	var Log = require("log");
	var scale = require('scale');
	var format=require("formatter");
	
	
	
	exports.init = function() {
		$(".ET_asnCmt").live("click",function(){
			transButtonState();
			commitAsnForm();
		});
	};

	/**
	 * 提交方法
	 */
	var commitAsnForm = function() {
		/*
		 * CA签名
		 */
		// FIXME 需要测试
		if (ca) {
			var strSn = "";
			var signature = "";
			var strSignData = G.userid;
			try {
				strSn = getSN(G.userCode);
			} catch (e) {

			}
			if (!strSn) {
				alert("请插入UKey后再进行操作！");
				return;
			}
			$("input[name$='nqtsignnum']").each(function(i, input) {
				var strValue = $(input).val();
				strSignData += "|" + strValue;
				signature = signmessage(strSignData, G.userCode);
					// 处理特殊字符,暂不打开
					// signature = encodeURIComponent(signature);
				});
			$("input[name=casn]").val(strSn);
			$("input[name=vsignature]").val(signature);
		}

		if(asnFormValidator()){
			var data=$("#asnForm").serialize();
		 	$.ajax({
		 		url:G.API.ORDER_ASNCOMMIT,
		 		data:data,
		 		type:"POST",
		 		dataType:"json",
		 		success:function(json){
		 			if ("success" == json.userObject) {
						alert("签收成功");
						window.location = G.PAGE.ORDER_ASNLIST;
					} else if (json.message != undefined) {
						alert("签收失败！");
						transButtonState();
						Log.error(json.detail);
						return;
					}
		 		},
		 		error:function(detail){
		 			alert("签收失败！"+detail.responseText.split(",")[0].split(":")[1]);
		 			transButtonState();
		 			Log.error(detail.responseText);
		 		}
		 	});
		}else{
			transButtonState();
		}
		return false;
	};

	var asnFormValidator=function(){
		var flag=true;
		if(!compareNum()){
			flag=false;
		}
		return flag;
	};
	
	var transButtonState=function(){
		if($(".ET_asnCmt").size()==1){
			//说明是由签收转为签收中
			$(".ET_asnCmt").text("签收中");
			$(".ET_asnCmt").css("cursor","not-allowed");
			$(".ET_asnCmt").addClass("ET_asnCmting").removeClass("ET_asnCmt");
		}else{
			$(".ET_asnCmting").text("签收");
			$(".ET_asnCmting").css("cursor","pointer");
			$(".ET_asnCmting").removeClass("ET_asnCmting").addClass("ET_asnCmt");
		}
	};

	var compareNum = function() {
		var flag=true;
		var str="";
		var length=$(".J-realAsn").length;
		for(var i=0;i<length;i++){
			var element=$(".J-realAsn").eq(i);
			//实际填写的值
			var real = parseFloat(element.val());
			//最大可以填写的值
			var sum = parseFloat(element.attr("num"));
			if (real > sum) {
				str+="第"+(i+1)+"行签收数量大于实际数量\n";
				flag=false;
			}
		}
		if(!flag){
			alert(str);
		}
		return flag;
	};

	$(function() {
		// 页面初始化，进行精度处理
		scale.init();
	});
});
