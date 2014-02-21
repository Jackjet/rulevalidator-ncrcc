define(function(require, exports, module) {
	var $=require("$");
	var OrderPlanEvent=[{
		key:"J-chnl-export",
		trigger:"click",
		handler:function(){
			var that=this;
			$.ajax({
				url:G.PAGE.CHANNEL_CHANNELEXPORT,
				data:{
					pk:$(that).attr("id"),
					mark:"0"
				},
				success:function(data){
					if(data.path){
						var filepath=data.path;
						var filename=data.name;
						window.open(G.API.DOWNLOAD+"?realPath="+encodeURI(encodeURI(filepath))+"&fileName="+encodeURI(encodeURI(filename)));
					}else{
						alert("请在后台渠道报送表导出数据设置节点中设置对应交易类型的导出模板");
					}
				}
			});
		}
	},{
		key:"J-bgn-export",
		trigger:'click',
		handler:function(){
			var that=this;
			$.ajax({
				url:G.PAGE.CHANNEL_CHANNELEXPORT,
				data:{
					pk:$(that).attr("id"),
					mark:"1"
				},
				success:function(data){
					if(data.path){
						var filepath=data.path;
						var filename=data.name;
						window.open(G.API.DOWNLOAD+"?realPath="+encodeURI(encodeURI(filepath))+"&fileName="+encodeURI(encodeURI(filename)));
					}else{
						alert("请在后台渠道报送表导出数据设置节点中设置对应交易类型的导出模板");
					}
					
				}
			});
		}
	},{
		key:"J-chnl-confirm",
		trigger:"click",
		handler:function(){
			$.ajax({
				url:G.API.CHANNEL_CHANNELCONFIRM,
				data:{
					data:$(this).attr("id"),
					mark:"0"
				},
				success:function(data){
					if(data!=null && data[0] && data[0].pk_chanlsend_h!=null){
						alert("报送确认成功");
						parent.seajs.emit("chnl_refresh",data);
					}else{
						alert("报送确认失败:"+data.message);
					}
				},
				type:'POST'
			});
		}
	},{
		key:"J-bgn-confirm",
		trigger:'click',
		handler:function(){
			$.ajax({
				url:G.API.CHANNEL_CHANNELCONFIRM,
				data:{
					data:$(this).attr("id"),
					mark:"1"
				},
				success:function(data){
					if(data!=null && data[0] && data[0].pk_beginbill_h!=null){
						alert("报送确认成功");
						parent.seajs.emit("bgn_refresh");
					}else{
						alert("报送确认失败:"+data.message);
					}
				},
				type:'POST'
			});
		}
	}];
	module.exports=OrderPlanEvent;
});
