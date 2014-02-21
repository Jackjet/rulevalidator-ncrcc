define(function(require, exports, module) {
	var $=require("$");
	var handfill=require("orderplan_handfill");
	var OrderPlanEvent=[{
		key:"J-export",
		trigger:"click",
		handler:function(){
			handfill.exportExcel($(this).attr("id"));
			return false;
		}
	},{
		key:"J-confirm",
		trigger:"click",
		handler:function(){
			handfill.sendconfirm($(this).attr("id"),$(this).attr("ts"));
			return false;
		}
	}];
	
	module.exports=OrderPlanEvent;
});



