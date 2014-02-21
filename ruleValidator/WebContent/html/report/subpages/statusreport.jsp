<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

 
<script src="/ruleValidator/component/webpub/common/echarts/esl.js"></script>
<script type="text/javascript">
	 require.config({
		 paths:{ 
		     echarts:'/ruleValidator/component/webpub/common/echarts/echarts',
		     'echarts/chart/bar' : '/ruleValidator/component/webpub/common/echarts/echarts-map',
		     'echarts/chart/line': '/ruleValidator/component/webpub/common/echarts/echarts-map',
		     'echarts/chart/map' : '/ruleValidator/component/webpub/common/echarts/echarts-map'
		 }
	});
	 require(
	         [
	             'echarts',
	             'echarts/chart/bar',
	             'echarts/chart/line',
	             'echarts/chart/map'
	         ],
		
       function(ec) {
         //--- 折柱 ---
         var myChart = ec.init(document.getElementById('main'));
         myChart.setOption({
             tooltip : {
                 trigger: 'axis'
             },
             legend: {
                 data:['蒸发量','降水量']
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
             xAxis : [
                 {
                     type : 'category',
                     data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
                 }
             ],
             yAxis : [
                 {
                     type : 'value',
                     splitArea : {show : true}
                 }
             ],
             series : [
                 {
                     name:'蒸发量',
                     type:'bar',
                     data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3]
                 },
                 {
                     name:'降水量',
                     type:'bar',
                     data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
                 }
             ]
         });
	         }
	 );
	
 
</script>
</head>

<body class="bg_white">
	<div data-widget="top"></div>
	<div data-module="order" class="layout_content">
		<div data-widget="banner"></div>
		<div class="top_bar"></div>
		<div data-page="orderCenter">
			<div data-widget="subnav"></div>
			<div class="center_content">
				 <div id="chars"></div>
				 <div id="main" style="height:500px;border:1px solid #ccc;padding:10px;"></div>
			</div>
		</div>
	</div>
	<div data-widget="footer"></div></body>
</html>