define(function(require, exports, module) {
	var constants={
		ERROR_CUSTOMER:"excel输入的用户和当前操作的用户不匹配",
		ERROR_PRODUCT:"商品不在允销目录之内",
		ERROR_NUMBER:"购买数量输入错误,请输入一个正确的数字",
		ERROR_DATE:"期望到货日期输入错误,请输入一个正确格式的日期",
		ERROR_FORMAT_DATE:"期望到货日期输入错误,输入的日期要大于或等于当前",
		ERROR_ADDRESS:"收货地址输入错误,请在模版对应字段的下拉框中选择"
	};
	var $=require("$");
	$=require("jquery.plupload.queue")($);
	var AdaptDialog=require("adaptdialog");
	var Dialog=require("dialog");
	var pluploadQueue=require("jquery.plupload.queue");
	$ = pluploadQueue($);
	$("#button_returnOrderAdd").live("click",function(){
		window.open(G.API.ORDER_RETURN_ADD);
		return false;
	});
	$("#button_OrderAdd").live("click",function(){
		window.open(G.API.ORDER_ADD);
		return false;
	});
	$("#button_excelTemplateExport").live("click",function(){
		window.open(G.API.ORDER_TEMPLATEEXPORT);
		return false;
	});
	$("#button_excelImportOrder").live("click",function(){
		var adaptDialog=new AdaptDialog({
			attrs:{
				title:"订单导入",
				width:600,
				height:395
			}
		});
		adaptDialog.show();
		
		$(".J-content").pluploadQueue({
			runtimes : 'html4',
			max_file_size : '2000mb',
			unique_names : true,
			multiple_queues:true,
			flash_swf_url : G.ctx+'/themes/default/scripts/upload/plupload.flash.swf',
			silverlight_xap_url : G.ctx+'/themes/default/scripts/upload/plupload.silverlight.xap',
			preinit: {
				UploadFile: function(up, file) {
					 up.settings.url = G.API.ORDER_IMPORT;
				}
			},
			init: {
				FileUploaded: function(up, file, info) {
					var isForward=false;
					var success=true;
					var html="";
					var results;
					try {
						results = $.parseJSON($(info.response).text());
					} catch(e) {
						results = $.parseJSON(info.response);
					}
					for(var i=0;i<results.length;i++){
						if(results[i]=="systemerror"){
							html+="excel解析失败,请重新下载模版后再上传";
							break;
						}
						if(results[i].index==0){
							if(results[i].errors!=null && results[i].errors.length!=0){
								html+="<div style='margin-left:40px;'>表头信息输入有误,具体信息如下:</div>";
								var errors=results[i].errors;
								var length=errors.length;
								for(var j=0;j<length;j++){
									html+="<div style='margin-left:60px;'>"+constants[errors[j]]+"</div>";
								}
								success=false;
							}
						}else{
							if(results[i].errors==null || results[i].errors.length==0){
								if(success){
									html+="<div style='margin-left:40px;'>第"+results[i].index+"行的商品导入成功</div>";
									isForward=true;
								}
							}else{
								html+="<div style='margin-left:40px;'>第"+results[i].index+"行的商品导入出现问题，具体信息如下:</div>";
								var errors=results[i].errors;
								var length=errors.length;
								for(var j=0;j<length;j++){
									html+="<div style='margin-left:60px;'>"+constants[errors[j]]+"</div>";
								}
							}
						}
					}
					Dialog.confirm({
						content:html,
						title:"提示",
						isHtmlContent:true,
						confirm:function(){
							if(isForward){
								window.open(G.API.ORDER_IMPORT_ADD);
								return true;
							}
							return true;
						}
					});
				}
			}
		});
	});
});
