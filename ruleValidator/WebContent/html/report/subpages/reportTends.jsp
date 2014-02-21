<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%-- <%@ include file="../echarts.html" %> --%>
<script src="/ruleValidator/component/webpub/common/echarts/echarts-plain-map.js"></script>
<script type="text/javascript" 
	src="/ruleValidator/component/webpub/core/seajs2/dist/sea.js" 
	data-config="/ruleValidator/html/global.js" 
	data-main="/ruleValidator/html/main.js"></script>
</head>
<body>
	<div id="main" style="height:500px;border:1px solid #ccc;padding:10px;"></div>
	<script type="text/javascript">
		seajs.use(["$","api"],function($,G_API){
			
			$.ajax({
				url: G_API.RULE_STATUSREPORT,
				type:'POST',
				success:function(data){
					paintChar(data);
				}
			});
			
			function paintChar(data){
				//--- 折柱 ---
			   	var myChart = echarts.init($('#main')[0]);
				var seriesData=data.seriesData;
				var legendData=new Array();
				var series=new Array() ;
				var i=0;
				for(var name in seriesData){
					legendData[i]=name;
					var item=new Object();
					item.name=name;
					item.type="bar";
					item.data=seriesData[name];
					series[i++]=item;
				}
			    var option=({
			    	   title:{
			    		  text:data.titleText,
			    		  subtext:data.titleSubText
			    	   },
			           tooltip : {
			               trigger: 'axis'
			           },
			           legend: {
			           		data:legendData
			           },
			           toolbox: {
			               show : true,
			               feature : {
			                   mark : true,
			                   dataView : {readOnly: false},
			                   magicType:['line', 'bar'],
			                   restore : true,
			                   saveAsImage : true
			               }
			           },
			           calculable : true,
			           xAxis : [{
			                   type : 'category',
			                   data :data.xAxisData
			               }
			           ],
			           yAxis : [{
			                   type : 'value',
			                   splitArea : {show : true}
			               }
			           ],
			           series : series
			    });
				myChart.setOption(option);			   
			}
		});
     
	</script>
</body>
</html>