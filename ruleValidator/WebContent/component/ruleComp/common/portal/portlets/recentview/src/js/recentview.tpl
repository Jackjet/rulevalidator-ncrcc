<div class="visited_list">
	<div class="clearlist">
		[
		<a href="#" id="clearUp">清空</a> ]
	</div>
	<div id="recentProductList">
		{{#each this.model}}
		<div class="visited_item">
			<a href="{{PRODUCT_DETAIL}}{{cproductid}}" target="_blank" data-attrs-ectype="product" data-attrs-productId="{{cproductid}}" title="{{vname}}">
				<img src="" title="{{vname}}" productid={{cproductid}} size="50_50" ectype="picture" />
				<div class="product_name">{{vname}}</div>
				<span data-pricetype="base" data-productid="{{cproductid}}" data-ectype="price"></span>
			</a>
		</div>
		{{/each}}
	</div>
</div>
<div class="clear"></div>
