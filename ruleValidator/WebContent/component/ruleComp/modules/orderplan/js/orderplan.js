define(function(require, exports, module) {
	var Dialog=require("dialog");
	var AdaptDialog=require("adaptdialog");
	var pluploadQueue=require("jquery.plupload.queue"); 
	var $=require("$");
	$(function(){
		$("#J-batchConfirm").live("click",function() {
			var params="";
			var tabIndex=$($("iframe")[0].contentDocument.body).find("#grid");
			tabIndex.find("input:checked").each(function(index,value) {
				var row=$(this).parent().parent().find(".J-confirm");
				if(row.size()!=0){
					params+= row.attr("id")+"&"+row.attr("ts");
					if (index != tabIndex.find("input:checked").size() - 1) {
						params += ",";
					}
				}
			});
			if(params==""){
				Dialog.alert({
					title : "提示",
					content : "没有可以报送确认的单据"
				});
				return false;
			}else{
				Dialog.confirm({
					title : "提示",
					content : "确认报送确认该要货计划?",
					confirm : function() {
						Dialog.close();
						Dialog.loading({
							title:"提示",
							content:"正在进行操作，请稍后"
						});
						$.ajax({
							url : G.API.ORDERPLAN_CONFIRM,
							data : {
								pkAndTSs:params
							},
							success : function(data) {
								Dialog.close();
								if (data && data.length!=0){
									Dialog.alert({
										title : "提示",
										content : "报送确认成功"
									});
									$("iframe")[0].contentWindow.seajs.emit("orderplan_grid_refresh");
								} else {
									Dialog.alert({
										title : "提示",
										content : "报送确认失败:"+data.message
									});
								}
							},
							type : 'POST'
						});
					},
					cancel : function() {
						return true;
					}
				});
			}
		});
		
		$("#J-excelimport").live("click",function(){
			var adaptDialog=new AdaptDialog({
				attrs:{
					title:"要货计划导入",
					width:600,
					height:395
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
						 up.settings.url = G.API.ORDERPLAN_EXCELIMPORT;
					}
				},
				init: {
					FileUploaded: function(up, file, info) {
					var result;
					var html="";
					var responseText=info.response;
						try {
							result = $.parseJSON($(responseText).text());
						} catch(e) {
							result = $.parseJSON(responseText);
						}
						if(result && result.length!=0){
							for(var i=0;i<result.length;i++){
								if(result[i]=="parseerror"){
									html+="<div style='margin-left:60px;'>上传失败</div>";
								}else{
									html+="<div style='margin-left:60px;'>第"+(i+1)+"行导入失败，失败原因是"+result[i]+"</div>";
								}
							}
							Dialog.alert({
								type:"alert",
								title:"提示",
								content:html,
								isHtmlContent:true
							});
						}else{
							Dialog.alert({
								type:"alert",
								title:"提示",
								content:"导入成功",
								isHtmlContent:true
							});
						}
					}
				}
			});
		});
	});
});