<%@ page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>出错啦</title>
<script type="text/javascript" src="/web/component/webpub/core/seajs2/dist/sea-debug.js" data-config="/ecp/html/global.js" data-main="/ecp/html/main.js"></script>
</head>
<body class="bg_white">
	<div data-widget="top"></div>
	<div class="layout_content">
		<div data-widget="banner"></div>
		<div style="display: none" class="ui-error">
			<div class="message">${exceptionMessage.message}</div>
			<div class="detail">${exceptionMessage.detail}</div>
		</div>
		<div data-widget="exception"></div>
	</div>
	<div data-widget="footer"></div>
</body>
</html>
