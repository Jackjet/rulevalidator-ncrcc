<div class="product_detail">
	<div class="product_detail_title font_yahei font_20">{{model.vname}}</div>
	<div class="product_detail_desc">
		<div class="product_detail_desc_box">
			<ul class="desc_list">
				{{#productdesc this}}
				{{/productdesc}}
			</ul>
			<label class="purchase_box">
				<span productid="{{attrs.prodid}}" class="purchase_button_addcart noprice nonabnum  nabnumloaded priceloaded">
					<span class="J-num floatleft">数量</span> 
					<input scale="{{model.pk_measdoc_unitScale}}" class="J-num" value="1">
					<span class="J-cunitid floatleft unit">{{model.pk_measdoc_name}}</span>
					<a href="#" class="add_to_cart J-cart"  product="{{model.cproductid}}"></a>
				</span>
				<span class="purchase_button_favorite">
					<a href="#" class="add_to_favorite J-favorite" data-ectype="fav" data-productid="{{model.cproductid}}"></a>
				</span>
			</label>
		</div>
	</div>
</div>
