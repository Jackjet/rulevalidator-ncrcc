<!-- 退货行 -->
<tr>

	<!-- 商品编码 -->
	<td>
		<a href="{{G.PAGE.PRODUCT_DETAIL}}{{cproductid}}" target="_blank">{{vcode}}</a>
	</td>
	
	<!-- 商品名称 -->
	<td>
		<a class="cproductid_name" href="{{G.PAGE.PRODUCT_DETAIL}}{{cproductid}}" target="_blank">{{vname}}</a>
	</td>
	
	<!-- 物料 -->
	<td>
		<span>{{cinventoryid_name}}</span>
	</td>
	
	<!-- 销售组织 -->
	<td>
		<span>{{saleOrgName}}</span>
	</td>
	
	<!-- 价格 -->
	<td class="align_right">
		<span class="ui-order-price-currency-symbol">{{corigcurrencyid_curSign}}</span>
		<span class="ui-order-price-currency-number caclPrice J-order-product-price" scale="{{corigcurrencyid_amountScale}}">{{nsaleprice}}</span>
	</td>
	
	<!-- 数量 -->
	<td class="align_left">
		<input class="textfiled order_nqtunitnum J_checkQtNum J-order-row-amount" name="addressUIView[0].productUIView[0].nqtunitnum" scale="{{pk_measdoc_unitScale}}" value="1">
		<span class="nc-unit">{{pk_measdoc_name}}</span>
	</td>
	
	<!-- 金额 -->
	<td class="align_right">
		<span class="ui-order-price-currency-symbol">{{corigcurrencyid_curSign}}</span>
		<span class="ui-order-price-currency-number norigtaxmny J-order-row-price" scale="{{corigcurrencyid_amountScale}}">{{nsaleprice}}</span>
	</td>
	
	<!-- 退货原因 -->
	<td class="J-td-return-reason align_center">
		<select class="cretreasonid" for="addressUIView[0].productUIView[0].cretreasonid" style="width: 100%;" name="cretreasonid">
		</select>
	</td>
	
	<!-- 类型 -->
	<td class="align_center J-td-row-type">
		<span class="J-order-row-type" type="1">退货</span>
	</td>
	
	<!-- 操作 -->
	<td class="align_center J-td-operation">
		<a href="#" title="换货" style="margin-right: 10px;" class="J-td-operation-exchange">[换货]</a>
		<a href="#" title="删除" class="J-td-operation-delete">[删除]</a>
	</td>
	
	<!-- 商品信息 -->
	<td class="hidden">
		{{#each attrs}}
		<input type="text" name="addressUIView[0].productUIView[0].{{key}}" value="{{value}}">
		{{/each}}
	</td>
	
</tr>