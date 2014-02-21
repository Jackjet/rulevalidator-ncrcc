define(function(require, exports, module) {
	var Format=require("formatter");
	var configs={
		//渠道报送表列表配置
		chnlColumns:[{
			label :"单据信息",
			key :"vbillcode",
			format : "CHNL_BILLINFO",
			width :"20%",
			align :"center"
		},{
			label :"单据类型",
			key :"ctrantypeid_name",
			width :"10%",
			align :"center"
		},{
			label :"渠道节点",
			key :"cchannelnodeid_name",
			width :"10%",
			align :"center"
		},{
			label:"单据状态",
			key:"fstatusflag",
			width :"10%",
			align : "center",
			format:"STATUE"
		},{
			label:"报送日期从",
			key:"dfromdate",
			width :"10%",
			align : "center",
			format:"DATE"
		},{
			label:"报送日期到",
			key:"dtodate",
			width :"10%",
			align : "center",
			format:"DATE"
		},{
			label:"操作",
			width :"30%",
			align : "center",
			format:"CHNL_OPERATE"
		}],
		//期初报送表列表配置
		bgnColumns:[{
			label :"单据信息",
			key :"vbillcode",
			format : "BGN_BILLINFO",
			width :150,
			align :"center"
		},{
			label :"渠道节点",
			key :"cchannelnodeid_name",
			width :80,
			align :"center"
		},{
			label:"状态",
			key:"fstatusflag",
			width:70,
			align :"center",
			format:"STATUE"
		},{
			label:"单据日期",
			key:"dbilldate",
			width:70,
			align :"center",
			format:"DATE"
		},{
			label:"操作",
			width :"30%",
			align : "center",
			format:"BGN_OPERATE"
		}],
		//渠道报送表表头配置
		chnlHead:[{
			field:"pk_chanlsend_h",
			isShow:false
		},{
			label:"渠道节点",
			field:"cchannelnodeid",
			format:function(value,row){
				return "<span class='J-cchannelnodeid' id='"+value+"'>"+row.cchannelnodeid_name+"</span>";
			}
		},{
			label:"单据类型",
			field:"ctrantypeid",
			format:function(value,row){
				return "<span id='"+value+"'>"+row.ctrantypeid_name+"</span>";
			}
		},{
			field:"vbillcode",
			label:"报送表单号"
		},{
			field:"cprodlineid",
			label:"产品线",
			format:function(value,row){
				return "<span class='J-cprodlineid' id='"+value+"'>"+(row.cprodlineid_name || "")+"</span>";
			}
		},{
			field:"cbrandid",
			label:"品牌",
			format:function(value,row){
				return "<span class='J-cbrandid' id='"+value+"'>"+(row.cbrandid_name || "")+"</span>";
			}
		},{
			field:"cpsndocid_name",
			label:"业务员"
		},{
			field:"dfromdate",
			label:"报送日期从",
			format:function(value,row){
				return Format.fmtDate(value);
			}
		},{
			field:"dtodate",
			label:"报送日期到",
			format:function(value,row){
				return Format.fmtDate(value);
			}
		},{
			field:"ccurrencyid",
			label:"币种",
			format:function(value,row){
				return "<span class='J-ccurrencyid' id='"+value+"' scale='"+row.ccurrencyid_amountScale+"'>"+row.ccurrencyid_name+"</span>";
			}
		},{
			field:"fstatusflag",
			label:"单据状态",
			format:function(value,row){
				var status="";
				if(value=="1"){
					status="自由";
				}else if(value=="2"){
					status="报送确认";
				}else{
					status="已审核";
				}
				return "<span class='J-fstatusflag' id='"+value+"'>"+status+"</span>";
			}
		}],
		//期初报送表表头配置
		bgnHead:[{
			field:"pk_beginbill_h",
			isShow:false
		},{
			label:"渠道节点",
			field:"cchannelnodeid",
			format:function(value,row){
				return "<span class='J-cchannelnodeid' id='"+value+"'>"+row.cchannelnodeid_name+"</span>";
			}
		},{
			label:"单据类型",
			field:"ctrantypeid",
			format:function(value,row){
				return "渠道期初报送表";
			}
		},{
			field:"vbillcode",
			label:"报送表单号"
		},{
			field:"cprodlineid",
			label:"产品线",
			format:function(value,row){
				return "<span class='J-cprodlineid' id='"+value+"'>"+(row.cprodlineid_name || "")+"</span>";
			}
		},{
			field:"cbrandid",
			label:"品牌",
			format:function(value,row){
				return "<span class='J-cbrandid' id='"+value+"'>"+(row.cbrandid_name || "")+"</span>";
			}
		},{
			field:"cpsndocid_name",
			label:"业务员"
		},{
			field:"dbilldate",
			label:"单据日期",
			format:function(value,row){
				return Format.fmtDate(value);
			}
		},{
			field:"ccurrencyid",
			label:"币种",
			format:function(value,row){
				return "<span class='J-ccurrencyid' id='"+value+"' scale='"+row.ccurrencyid_amountScale+"'>"+row.ccurrencyid_name+"</span>";
			}
		},{
			field:"fstatusflag",
			label:"单据状态",
			format:function(value,row){
				var status="";
				if(value=="1"){
					status="自由";
				}else if(value=="2"){
					status="报送确认";
				}else{
					status="已审核";
				}
				return "<span class='J-fstatusflag' id='"+value+"'>"+status+"</span>";
			}
		}]
	};
	module.exports=configs;
});