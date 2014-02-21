define(function(require, exports, module) {
	var chnlFormatter={
		CHNL_BILLINFO:function(value,row){
			var ret = "<a href='"+G.PAGE.CHANNEL_CHNL_DETAIL+"?id="+row.pk_chanlsend_h+"&ctrantypeid="+
				row.ctrantypeid+"' target='_blank'>"+row.vbillcode+"</a>";
			ret+="<div>"+(row.cprodlineid_name || "")+"</div>";
			ret+="<div>"+(row.cbrandid_name || "")+"</div>";
			ret+="<div>"+(row.ccurrencyid_name || "")+"</div>";
			return ret;
		},
		BGN_BILLINFO:function(value,row){
			var ret = "<a href='"+G.PAGE.CHANNEL_BEGIN_DETAIL+"?id="+row.pk_beginbill_h+"' target='_blank'>"+row.vbillcode+"</a>";
			ret+="<div>"+(row.cprodlineid_name || "")+"</div>";
			ret+="<div>"+(row.cbrandid_name || "")+"</div>";
			ret+="<div>"+(row.ccurrencyid_name || "")+"</div>";
			return ret;
		},
		STATUE:function(status,row){
			if(status==1){
				return "自由";
			}else if(status==2){
				return "报送确认";
			}else{
				return "已审核";
			}
		},
		CHNL_OPERATE:function(value,row){
			var ret="";
			//excel按钮
			ret+="<a href='#' class='J-chnl-export' id='"+row.pk_chanlsend_h+"'>Excel导出</a>";
			//自由态
			if(row.fstatusflag==1){
				ret+="<a href='"+G.PAGE.CHANNEL_CHNL_HANDFILL+"?id="+row.pk_chanlsend_h+"&ctrantypeid="+row.ctrantypeid+"' target='_blank' style='margin-left:8px;'>手工填报</a>";
				ret+="<a id='"+row.pk_chanlsend_h+"' class='J-chnl-confirm J-confirm' href='#' style='margin-left:8px;'>报送确认</a>";
			}
			return ret;
		},
		BGN_OPERATE:function(value,row){
			var ret="";
			//excel按钮
			ret+="<a href='#' class='J-bgn-export' id='"+row.pk_beginbill_h+"'>Excel导出</a>";
			//自由态
			if(row.fstatusflag==1){
				ret+="<a href='"+G.PAGE.CHANNEL_BEGIN_HANDFILL+"?id="+row.pk_beginbill_h+"' target='_blank' style='margin-left:8px;'>手工填报</a>";
				ret+="<a id='"+row.pk_beginbill_h+"' class='J-bgn-confirm J-confirm' href='#' style='margin-left:8px;'>报送确认</a>";
			}
			return ret;
		}
	};
	module.exports=chnlFormatter;
});
