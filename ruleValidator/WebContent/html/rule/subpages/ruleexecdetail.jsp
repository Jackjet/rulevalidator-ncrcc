<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript" 
	src="/ruleValidator/component/webpub/core/seajs2/dist/sea.js" 
	data-config="/ruleValidator/html/global.js" 
	data-main="/ruleValidator/html/main.js"></script>
	
<script type="text/javascript">
	seajs.use(["api","account_accountDetail"]);
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
				<div class="account_info floatcenter" style="width:1000px">
					<div class="account_info_title">
						<div class="floatleft font_14 bg_white nc-font-weight-bold">执行结果信息</div>
					</div>
					<div class="account_info_detail">
						<div class="w_100ps">
							<span>执行单元：</span> <span>${resultVO.executeUnit.prodCode} ${resultVO.executeUnit.moduleCode} ${resultVO.executeUnit.compCode}</span>
						</div>
						<div class="w_100ps">
							<span>规则：</span> <span>${resultVO.ruleDefinition.descDetails}</span>
						</div>
						<div class="w_100ps">
							<span>特殊参数：</span> <span>${resultVO.specParams}</span>
						</div>
						<div class="w_100ps">
							<span>是否通过：</span> <span>${resultVO.passFlag}</span>
						</div>
						<div class="w_100ps">
							<span>运行结果：</span> <span>${resultVO.result}</span>
						</div>
					</div>
				</div>
				<div class="account_info floatcenter" style="width:1000px">
					<div class="account_info_title">
						<div class="floatleft font_14 bg_white nc-font-weight-bold">运行结果详细信息</div>
					</div>
					<br/>
					<div class="account_info_detail">
						<div class="w_100ps">
							<span>${resultVO.resultDetail}</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div data-widget="footer"></div></body>
</html>