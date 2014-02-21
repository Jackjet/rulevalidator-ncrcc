define(function(require, exports, module) {
	var OrderPlanFormat = {
		//格式化单据号
		ORDERPLAN_VBILLCODE:function(value,row){
			return "<a href='"+G.PAGE.ORDERPLAN_HANDFILL+"?id="+row.corderplanid+"' target='_blank'>"+row.vbillcode+"</a>";
		},
		//格式化单据状态
		ORDERPLAN_STATUS:function(value,row){
			if(value==-1){
				return "自由";
			}else if(value==2){
				return "审批中";
			}else if(value==1){
				return "审批通过";
			}else if(value==0){
				return "审批未通过";
			}
		},
		//格式化报送确认列
		ORDERPLAN_CONFIRM:function(value,row){
			if(value==true){
				return "<input type='checkbox' checked='true' disabled='disabled'>";
			}else{
				return "<input type='checkbox' disabled='disabled'>";
			}
		},
		//格式化操作
		ORDERPLAN_OPERATE:function(value,row){
			var ret="";
			//excel按钮
			ret+="<a href='#' class='J-export' id='"+value+"'>Excel导出</a>";
			//状态为自由且报送确认为否时有在线填报按钮和报送确认按钮
			if(row.fstatusflag==-1 && row.bcommitflag==false){
				ret+="<a href='"+G.PAGE.ORDERPLAN_HANDFILL+"?id="+row.corderplanid+"' target='_blank' style='margin-left:8px;'>在线填报</a>";
				ret+="<a id='"+row.corderplanid+"' class='J-confirm' href='#' ts='"+row.ts+"' style='margin-left:8px;'>报送确认</a>";
			}else if(row.fstatusflag==0){
				ret+="<a href='"+G.PAGE.ORDERPLAN_HANDFILL+"?id="+row.corderplanid+"' target='_blank' style='margin-left:8px;'>在线填报</a>";
			}
			return ret;
		}
	};
	module.exports=OrderPlanFormat;
});
