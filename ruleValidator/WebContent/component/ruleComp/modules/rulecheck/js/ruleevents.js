define(function(require, exports, module) {
	var $=require("$");
	var obj;
	var RuleCheckPlanEvent=[{
		key:"J-ruleCheck-del",
		trigger:"click",
		handler:function(){
			var recordID=$(this).attr("id");
			$.ajax({
				url: G.API.RULE_RECORD_DEL,
				data:{
					recordID:$(this).attr("id") 
				},
				success:function(data){
					if(data=="Y"){
						$("#"+recordID).parent().parent().remove();
						alert("删除成功");
					}else{
						alert("删除失败:"+data.message);
					}
				},
				type:'POST'
			});
		}
	},{
		key:"J-report",
		trigger:"click",
		handler:function(){
			var recordID=$(this).attr("id");
			$.ajax({
				url: G.API.RULE_STATUSREPORT,
				data:{
					recordID:$(this).attr("id") 
				},
				success:function(data){
					if(data=="Y"){
						$("#"+recordID).parent().parent().remove();
						alert("删除成功");
					}else{
						alert("删除失败:"+data.message);
					}
				},
				type:'POST'
			});
		}
	}];
	module.exports=RuleCheckPlanEvent;
});
