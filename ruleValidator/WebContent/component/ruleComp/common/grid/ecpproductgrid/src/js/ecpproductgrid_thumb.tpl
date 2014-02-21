<div class="ui-ecpproductgrid">
	<div class="product_list_title">
		<a class="switch_view ">
			<span class="list_view_title J_switchDetail">详细</span>
		</a>
		<a class="switch_view active ">
			<span class="list_view_title J_switchThumb">缩略图</span>
		</a>
		<a class="switch_view ">
			<span class="list_view_title J_switchList">列表</span>
		</a>
	</div>
	<div class="product_list_thumbs" id="product_list_thumbs">
		{{#each model.records}}
		<div class="channel_item border_bottom">
			<span class="product_name nolabel">{{this.vname}}</span>
			<a class="product_thumb" href="{{../G.PAGE.PRODUCT_DETAIL}}{{this.cproductid}}" target="_blank"
				ectype="recent" productid="{{this.cproductid}}">
				<div class="nc-product-thumb">
					{{#if ../attrs.isLoadPic}}
						<img src="" alt="{{this.vname}}" title="{{this.vname}}"
							productid="{{this.cproductid}}" size="130_130" ectype="picture">
					{{else}}
						<img src="/{{this.vimageurl}}" alt="{{this.vname}}" title="{{this.vname}}">
					{{/if}}
				</div>
			</a>
			<div class="product_desc">
				<div class="J_product_prop">
					<div class="nabnum">
						<span>商品编码：</span>
						<span title="{{this.vcode}}">{{this.vcode}}</span>
					</div>
				</div>
				<div class="J_product_prop">
					<span>基准价：</span>
					<span class="font_bold based nc-no-float" ectype="price" data-priceType="base"
						productId="{{this.cproductid}}">
					</span>
				</div>
				<div class="J_product_prop">
					<span>当前价：</span>
					<span id="product_price{{this.cproductid}}" ectype="price" productId="{{this.cproductid}}"
					value="{{this.nsaleprice}}"></span>
				</div>
				<div class="J_product_prop">
					<div class="nabnum">
						<span>库存：</span>
						<span id="product_nnabnum{{this.cproductid}}" class="nc-product-num"
							scale="{{this.pk_measdoc_unitScale}}" " ectype="nabnum"
							productid="{{this.cproductid}}" bspotflag="{{this.bspotflag}}"
							unit="{{this.pk_measdoc_name}}" title="">
						</span> 
					</div>
				</div>
				<div class="J_product_prop">
					<span>销量：</span>
					<span>{{this.saleVolume}}{{this.pk_measdoc_name}}</span>
				</div>
				<div>
					<!-- 收藏 -->
					<a class="ui-favorite-add-button-small add_to_favorite" href="#" productId="{{this.cproductid}}" title="添加收藏"></a>
					<!-- 添加到购物车 -->
					<span class="noprice nonabnum nabnumloaded priceloaded">
						<a class="add_to_cart J-cart ET_addToCart nc-margin-right-21" href="#" productid="{{this.cproductid}}" title="加入购物车"></a>
					</span>
				</div>
			</div>
		</div>
		{{/each}}
		<div class="hidden">
		<div class="product_thumb_hide"><label>商品编码：</label><input type="checkbox">隐藏</div>
		<div class="product_thumb_hide"><label>基准价：</label><input type="checkbox">隐藏</div>
		<div class="product_thumb_hide"><label>当前价：</label><input type="checkbox">隐藏</div>
		<div class="product_thumb_hide"><label>库存：</label><input type="checkbox">隐藏</div>
		<div class="product_thumb_hide"><label>销量：</label><input type="checkbox">隐藏</div>
	</div>
	</div>
</div>
