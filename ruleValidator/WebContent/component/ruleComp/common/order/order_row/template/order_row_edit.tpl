{{#with model }}
<tr class="ui-order-row J-order-row" index="{{index}}">
	<!-- 商品编码 -->
	<td>
		<a href="{{vhtmurl}}" target="_blank">
			<span title="{{cproductid_name}}">{{cproductid_name}}</span>
		</a>
	</td>
	<!-- 商品名称 -->
	<td>
		<a class="" href="{{vhtmurl}}" target="_blank">
			<span title="{{cproductid_name}}">{{cproductid_name}} </span>
		</a>
	</td>
	<!-- 物料 -->
	<td>
		<span title="{{cmaterialid_name}}">{{cmaterialid_name}}</span>
	</td>
	<!-- 销售组织 -->
	<td>
		<span title="{{pk_org_name}}">{{pk_org_name}}</span>
	</td>
	<!-- 价格 -->
	<td class="ui-align-right">
		<span class="ui-currency-sign" title="{{corigcurrencyid_curSign}}">{{corigcurrencyid_curSign}}</span>
		<span class="ui-table-cell ui-currency-number J-order-product-price"
			scale="{{nqtorigtaxprice_priceScale}}" title="{{nqtorigtaxprice}}">{{nqtorigtaxprice}}</span>
	</td>
	<!-- 数量 -->
	<td class="ui-align-right">
		<input
			class="textfiled order_nqtunitnum J_checkQtNum J-order-row-amount"
			name="nqtunitnum" scale="{{cqtunitid_unitScale}}"
			title="{{nqtunitnum}}" value="{{nqtunitnum}}">
		<span class="nc-unit ui-product-unit">{{cqtunitid_name}}</span>
	</td>
	<!-- 库存 -->
	<td class="ui-align-right">
		<span ectype="nabnum" productid="{{cproductid}}"
			scale="{{cqtunitid_unitScale}}" unit="{{cqtunitid_name}}"
			bspotflag="{{bspotflag}}" nabnum="{{nqtunitnum}}"
			title="{{nqtunitnum}}">{{nqtunitnum}}</span>
		<span class="nabnum_unit" title="{{cqtunitid_name}}">{{cqtunitid_name}}</span>
	</td>
	<!-- 金额 -->
	<td class="ui-align-right">
		<span class="ui-currency-sign" title="{{corigcurrencyid_curSign}}">{{corigcurrencyid_curSign}}</span>
		<span class="ui-table-cell ui-currency-number J-order-row-price"
			scale="{{nqtorigtaxprice_priceScale}}" title="{{nqtorigtaxprice}}">{{nqtorigtaxprice}}</span>
	</td>
	<!-- 操作 -->
	<td class="ui-operation">
		<a href="#" class="J-delete">删除</a>
	</td>
	<!-- 隐藏域 -->
	<td class="ui-hidden">
		<input type="hidden" name="cproductid" value="{{cproductid}}">
		<input type="hidden" name="pk_org" value="{{pk_org}}">
		<!-- <input type="hidden"
			name="cmaterialid"
			value="{{cmaterialid}}">
		<input type="text"
			name="pk_org_name" value="{{pk_org_name}}">
		<input type="hidden"
			name="corigcurrencyid"
			value="{{corigcurrencyid}}">
		<input type="hidden"
			name="nqtorigtaxnetprc" value="{{nqtorigtaxnetprc}}">
		<input type="hidden" name="cqtunitid"
			value="{{cqtunitid}}">
		<input type="hidden"
			name="nqtorigtaxprice_priceScale"
			value="{{nqtorigtaxprice_priceScale}}">
		<input type="hidden"
			name="fretexchange" value="{{fretexchange}}">
		<input type="hidden"
			name="cexchangesrcretid" value="{{cexchangesrcretid}}">
		<input type="hidden"
			name="corigcurrencyid_amountScale"
			value="{{corigcurrencyid_amountScale}}"> -->
	</td>
</tr>
{{/with }}