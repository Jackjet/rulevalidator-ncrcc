define(function(require, exports, module) {
	var Dialog=require("dialog");
	var $=require("$");
	var upload=require("channel_upload");
	$(function(){
		$("#J-batchChnlConfirm").live("click",function() {
			confirm(0);
			return false;
		});
		
		$("#J-batchBeginConfirm").live("click",function(){
			confirm(1);
			
			return false;
		});
		
		$("#J-batchChnlExcelImport").live("click",function(){
			upload("渠道报送表excel导入",G.PAGE.CHANNEL_CHANNELIMPORT+"?mark=0");
		});
		
		$("#J-batchBeginExcelImport").live("click",function(){
			upload("期初报送表excel导入",G.PAGE.CHANNEL_CHANNELIMPORT+"?mark=1");
		});
	});
	
	function confirm (mark){
		var subPage=$("iframe")[0].contentDocument.body;
		var tab=$(subPage).find("#tab");
		var inputs;
		//区分是否有页签，如果有页签，说明是渠道首页或者是渠道报送表这两个页面
		if(tab.size()!=0){
			if($(subPage).find("#tab1").size()!=0){
				//有tab1这个页面说明是渠道首页，因为渠道报送报送表的页签上的id都是交易类型的id。需要根据mark来判断是取渠道报送表页签还是期初报送表页签
				if(mark=="0"){
					inputs=tab.find("#tab1 input:checked");
				}else{
					inputs=tab.find("#tab2 input:checked");
				}
			}else if(mark=="0"){
				//说明是渠道报送表页面
				inputs=tab.find(".content:visible input:checked");
			}else{
				inputs=[];
			}
		}else{
			if(mark==0){
				inputs=$(subPage).find("#chnlgrid input:checked");
			}else{
				inputs=$(subPage).find("#chnlbegingrid input:checked");
			}
		}
		var data = "";
		if(inputs.length!=0){
			inputs.each(function(index,value) {
				if ($(this).parent().parent().find(".J-confirm").size() != 0) {
					var pk = $(this).parent().parent().find(".J-confirm").attr("id");
					if (index == inputs.size() - 1) {
						data += pk;
					} else {
						data += pk+ ",";
					}
				}
			});
		}
		if(data==""){
			Dialog.alert({
				title : "提示",
				content : "没有可以报送确认的单据"
			});
			return false;
		}
		Dialog.loading({
			title:"提示",
			content:"正在进行操作，请稍后"
		});
		$.ajax({
			url : G.API.CHANNEL_CHANNELCONFIRM,
			data : {
				data:data,
				mark:mark
			},
			success : function(data) {
				Dialog.close();
				if (data != null && data[0] !=null){
					if(data[0].pk_chanlsend_h != null){
						seajs.emit("chnl_refresh",data);
					}else{
						seajs.emit("bgn_refresh");
					}
					Dialog.alert({
						title : "提示",
						content : "报送确认成功"
					});
				} else {
					Dialog.alert({
						title : "提示",
						content : "报送确认失败:"+data.message
					});
				}
			},
			type : 'POST'
		});
	}
});