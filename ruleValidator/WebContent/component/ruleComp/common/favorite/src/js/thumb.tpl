<div class="ui-ecpproductgrid">
	<div class="product_list_title">
		<a href="#" class="switch_view">
			<span class="list_view_title J_switchDetail">详细</span>
		</a>
		<a href="#" class="switch_view active">
			<span class="list_view_title J_switchThumb">缩略图</span>
		</a>
		<a href="#" class="switch_view">
			<span class="list_view_title J_switchList">列表</span>
		</a>
	</div>
	<div class="product_list_thumbs" id="product_list_thumbs">
		{{#each model.records}}
		<div class="channel_item border_bottom">
			<span class="product_name nolabel">{{this.product.vname}}</span>
			<a class="product_thumb" href="{{../G.PAGE.PRODUCT_DETAIL}}{{this.product.cproductid}}" target="_blank" ectype="recent" productid="{{this.product.cproductid}}">
				<div class="nc-product-thumb">
					<img src=""  alt="{{this.product.vname}}" title="{{this.product.vname}}"  productid="{{this.product.cproductid}}" size="130_130" ectype="picture">
				</div>
			</a>
			<ul class="rate">
				<li class="rated"></li>
				<li class="rated"></li>
				<li class="rated"></li>
				<li class="rated"></li>
				<li></li>
			</ul>
			<div class="product_desc">
				<span class="product_price" ectype="price" productId="{{this.product.cproductid}}"></span>
				<span>库存&nbsp;</span>
				<span id="product_nnabnum{{this.product.cproductid}}" class="nc-product-num" scale="{{this.product.pk_measdoc_unitScale}}" " ectype="nabnum" productid="{{this.product.cproductid}}"
					bspotflag="{{this.product.bspotflag}}" unit="{{this.product.pk_measdoc_name}}"></span>
				<a href="#" class="add_to_cart ET_addToCart" id="{{this.product.cproductid}}" productid="{{this.product.cproductid}}"></a>
			</div>
		</div>
		{{/each}}
	</div>
</div>
