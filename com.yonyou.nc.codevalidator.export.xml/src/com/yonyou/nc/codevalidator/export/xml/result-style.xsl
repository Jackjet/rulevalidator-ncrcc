<?xml version="1.0" encoding="UTF-8"?>
<!-- Edited with XML Spy v2007 (http://www.altova.com) -->
<html xsl:version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">
 <head>
<style type="text/css">
body,table{
 font-size:14px;
}
table{
 table-layout:fixed;
 empty-cells:show; 
 border-collapse: collapse;
 margin:0 auto;
}
td{
 align:left;
}
h1,h2,h3{
 margin:0;
 padding:0;
}

table.tab_css_3{
 border:1px solid ;
}
table.tab_css_3 th {
 background-repeat::repeat-x;
}
table.tab_css_3 td{
 border:1px solid;
 padding:0 1.5em 0;
}
table.tab_css_3 th{
 border:1px solid;
 padding:0 2px 0;
}
table.tab_css_3 tr.tr_css{}
 
.hover{
   background-color: #53AB38;
   color: #fff;
}

</style>

 </head>
 
  <body style="font-family:Arial,helvetica,sans-serif;font-size:12pt;
        background-color:#EEEEEE">
	<h1>NC规则执行结果文件</h1>
	<h2>公共参数</h2>
    <xsl:for-each select="rule-result-elements/common-param-list/common-param">
      <div style="padding:4px">
        <span >
        <xsl:value-of select="@param-name"/></span>
        ： <xsl:value-of select="@param-value"/>
      </div>
    </xsl:for-each>
	
	<h2>规则执行结果条目</h2>
		<table class="tab_css_3"> 
			<tr>
				<th width="140">序号</th>
				<th width="180">规则类型</th>
				<th width="180">规则子类型</th>
				<th width="400">规则详情</th>
				<th width="250">特殊参数</th>
				<th width="250">执行结果</th>
				<th width="300">备注</th>
			</tr>
			<xsl:for-each select="rule-result-elements/rule-result/rule-config-item">
				<tr>
					<td><xsl:value-of select="@simple-identifier"/></td>
					<td><xsl:value-of select="@category"/></td>
					<td><xsl:value-of select="@sub-category"/></td>
					<td><xsl:value-of select="@detail"/></td>
					<td><xsl:value-of select="@special-parameters"/></td>
					<td><xsl:value-of select="@run-result"/></td>
					<td><xsl:value-of select="@memo"/></td>
				</tr>
			</xsl:for-each>
		</table>
  </body>
</html>
