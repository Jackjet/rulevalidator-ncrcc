<div class="ui-ecpproductgrid">
	<div class="product_list_title">
		<a class="switch_view ">
			<span class="list_view_title J_switchDetail">详细</span>
		</a>
		<a class="switch_view">
			<span class="list_view_title J_switchThumb">缩略图</span>
		</a>
		<a class="switch_view active ">
			<span class="list_view_title J_switchList">列表</span>
		</a>
	</div>
	{{#each model.records}}
	<div class="lists_item border_btm">
		<div class="item_thumb">
			<a href="{{../G.PAGE.PRODUCT_DETAIL}}{{this.cproductid}}" target="_blank"
				class="item_thumb_wrapper" ectype="recent" productid="{{this.cproductid}}">
				{{#if ../attrs.isLoadPic}}
					<img src="" alt="{{this.vname}}" title="{{this.vname}}"
						productid="{{this.cproductid}}" size="80_80" ectype="picture">
				{{else}}
					<img src="/{{this.vimageurl}}" alt="{{this.vname}}" title="{{this.vname}}">
				{{/if}}
			</a>
		</div>
		<div class="product_name">
			<a href="{{../G.PAGE.PRODUCT_DETAIL}}{{this.cproductid}}" target="_blank" ectype="recent"
				productid="{{this.cproductid}}">
				<div class="wrap">{{this.vname}}</div>
			</a>
		</div>
		<div class="product_properties">
			<div class="product_td">
				<label>商品编码：</label>
				<span>{{this.vcode}}</span>
			</div>
			<div class="product_td">
				<label>基准价：</label>
				<span class="font_bold based" ectype="price" data-priceType="base"
					productId="{{this.cproductid}}"></span>
			</div>
			<div class="product_td">
				<label>当前价：</label>
				<span class="font_bold" id="product_price{{this.cproductid}}" value="{{this.nsaleprice}}">
					<span class="font_bold based " ectype="price" productId="{{this.cproductid}}"></span>
				</span>
			</div>
		</div>
		<div class="product_properties">
			<div class="product_td">
			<label>库存：</label>
			<span class="font_bold overhide"
				id="product_nnabnum{{this.cproductid}}"
				bspotflag="{{this.bspotflag}}" scale="{{this.pk_measdoc_unitScale}}"
				ectype="nabnum" productid="{{this.cproductid}}"
				unit="{{this.pk_measdoc_name}}">
			</span>
			</div>
			<div class="product_td">
			<label>销量：</label>
			<span>{{this.saleVolume}}{{this.pk_measdoc_name}}</span>
			</div>
		</div>
		<div class="ui-product-grid-operate noprice nonabnum nabnumloaded priceloaded">
			<!-- 添加到购物车 -->
			<a class="add_to_cart J-cart ET_addToCart nc-margin-right-21" href="#" productid="{{this.cproductid}}" title="加入购物车"></a>
		</div>
		<div class="ui-product-grid-operate ui-product-grid-operate-favorite">
			<!-- 收藏 -->
			<a class="ui-favorite-add-button-small add_to_favorite" href="#" productId="{{this.cproductid}}" title="添加收藏"></a>
		</div>
	</div>
	{{/each}}
	<div class="hidden">
		<div class="product_properties_hide"><label>商品编码：</label><input type="checkbox">隐藏</div>
		<div class="product_properties_hide"><label>基准价：</label><input type="checkbox">隐藏</div>
		<div class="product_properties_hide"><label>当前价：</label><input type="checkbox">隐藏</div>
		<div class="product_properties_hide"><label>库存：</label><input type="checkbox">隐藏</div>
		<div class="product_properties_hide"><label>销量：</label><input type="checkbox">隐藏</div>
	</div>
</div>





