<div class="ui-creditamount">
	<!-- 信用额度列表（开始） -->
	<ul class="ui-creditamoutul">
		{{#each model.records}}
		<li class="ui-creditamoutli" title="产品线：{{productLines}}&#13销售组织：{{csaleorgid_name}}&#13余额：{{custcurrencyid_curSign}} {{nbalancemny}}">
			<!-- 产品线 --> 
			<span class="ui-creditamout-productline">{{productLines}}</span>
			 <!-- 信用余额 --> 
			 <span class="ui-creditamout-balance">
				<span class="ui-creditamout-curSign">{{custcurrencyid_curSign}}</span>
				<span class="ui-creditamout-nbalancemny">{{nbalancemny}}</span>
			</span>
			<div class="clear"></div>
			<ul class="ui-creditamoutsubul">
				<li class="ui-creditamoutsubli">信用控制域：{{pkOrg_name}}</li>
				<li class="ui-creditamoutsubli">销售组织：{{csaleorgid_name}}</li>
			</ul>
		</li>
		{{/each}}
	</ul>
	<div class="clear"></div>
</div>