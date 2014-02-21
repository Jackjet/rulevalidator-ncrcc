define(function(require, exports, module) {
	var upload=function(title,url){
		var AdaptDialog=require("adaptdialog");
		var $ = require("$");
		var pluploadQueue=require("jquery.plupload.queue"); 
		var Dialog=require("dialog");
		
		var adaptDialog=new AdaptDialog({
			attrs:{
				title:title,
				width:"600px"
			}
		});
		adaptDialog.show();
		$ = pluploadQueue($);
		$(parent.document).find(".J-content").pluploadQueue({
			runtimes : 'html4',
			max_file_size : '2000mb',
			unique_names : true,
			multiple_queues:true,
			flash_swf_url : '${pageContext.request.contextPath}/themes/default/scripts/upload/plupload.flash.swf',
			silverlight_xap_url : '${pageContext.request.contextPath}/themes/default/scripts/upload/plupload.silverlight.xap',
			preinit: {
				UploadFile: function(up, file) {
					 up.settings.url = url;
				}
			},
			init: {
				FileUploaded: function(up, file, info) {
					var results;
					var responseText=info.response;
					try {
						results = $.parseJSON($(responseText).text());
					} catch(e) {
						results = $.parseJSON(responseText);
					}
					if(results){
						for(var i=0;i<results.length;i++){
							if(results[i].excelImportBUIViews && results[i].excelImportBUIViews.length!=0){
								var html1="<div style='margin-left:40px;'>"+results[i].vfilename+"存在导入失败的行：</div>";
								for(var j=0;j<results[i].excelImportBUIViews.length;j++){
									html1+="<div style='margin-left:60px;'>第"+results[i].excelImportBUIViews[j].vlinenum+"行导入失败，失败原因是"+results[i].excelImportBUIViews[j].verror+"</div>"
								}
								Dialog.alert({
									type:"alert",
									title:"提示",
									content:html1,
									isHtmlContent:true
								});
							}else if(results[i].verror!=null){
								var html2="<div style='margin-left:40px;'>"+results[i].vfilename+"全部导入失败,失败原因是：</div>";
								html2+="<div style='margin-left:60px;'>"+results[i].verror+"</div>";
								Dialog.alert({
									type:"alert",
									title:"提示",
									content:html2,
									isHtmlContent:true
								});
							}else if(results[i].systemError!=null){
								var html3="<div style='margin-left:40px;'>"+results[i].vfilename+"全部导入失败,失败原因是：</div>";
								html3+="<div style='margin-left:60px;'>"+results[i].systemError+"</div>";
								Dialog.alert({
									type:"alert",
									title:"提示",
									content:html3,
									isHtmlContent:true
								});				
							}else if(results[i]=="parseerror"){
								Dialog.alert({
									type:"alert",
									title:"提示",
									content:"文件上传出现异常"
								});
							}else{
								Dialog.alert({
									type:"alert",
									title:"提示",
									content:results[i].vfilename+"全部导入成功"
								});
							}
						}
					}
				}
			}
		});
	};
	module.exports=upload;
});
