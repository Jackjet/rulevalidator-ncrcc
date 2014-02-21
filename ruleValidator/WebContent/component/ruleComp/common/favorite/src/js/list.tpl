<div class="ui-ecpproductgrid">
	<!-- tab页 -->
	<div class="product_list_title">
		<a href="#" class="switch_view ">
			<span class="list_view_title J_switchDetail">详细</span>
		</a>
		<a href="#" class="switch_view">
			<span class="list_view_title J_switchThumb">缩略图</span>
		</a>
		<a href="#" class="switch_view active">
			<span class="list_view_title J_switchList">列表</span>
		</a>
	</div>
	{{#each model.records}}
	<div class="lists_item border_btm">
		<a class="product_thumb item_thumb" href="{{../G.PAGE.PRODUCT_DETAIL}}{{this.product.cproductid}}" target="_blank" ectype="recent" productid="{{this.product.cproductid}}">
			<div class="nc-product-thumb">
				<img src="" alt="{{this.product.vname }}" title="{{this.product.vname}}" productid="{{this.product.cproductid}}" size="80_80" ectype="picture">
			</div>
		</a>
		<!-- 编码 -->
		<div class="product_code">
			<a href="{{../G.PAGE.PRODUCT_DETAIL}}{{this.product.cproductid}}" target="_blank" ectype="recent" productid="{{this.product.cproductid}}">
				<div class="wrap">{{this.product.vcode}}</div>
			</a>
		</div>
		<!-- 名称 -->
		<div class="product_name">
			<a href="{{../G.PAGE.PRODUCT_DETAIL}}{{this.product.cproductid}}" target="_blank" ectype="recent" productid="{{this.product.cproductid}}">
				<div class="wrap">{{this.product.vname}}</div>
			</a>
		</div>
		<!-- 价格和库存 -->
		<div class="product_and_nab">
			<label>当前价：</label>
			<span class="font_bold based nc-no-float" ectype="price" productId="{{this.product.cproductid}}"></span>
			<br>
			<label>库存：</label>
			<span class="font_bold overhide" id="product_nnabnum{{this.product.cproductid}}" bspotflag="{{this.product.bspotflag}}" scale="{{this.product.pk_measdoc_unitScale}}" ectype="nabnum"
				productid="{{this.product.cproductid}}" unit="{{this.product.pk_measdoc_name}}"
			> </span>
		</div>
		<!-- 收藏时间 -->
		<div class="favorite_ts">
			<label>收藏时间：</label>
			<span>{{this.favorite.ts$}}</span>
		</div>
		<!-- 操作 -->
		<div class="item_operation">
			<a href="#" class="add_to_cart  ET_addToCart " id="{{this.product.cproductid}}" productid="{{this.product.cproductid}}"></a>
		</div>
	</div>
	{{/each}}
</div>
