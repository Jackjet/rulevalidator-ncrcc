<div class="ui-floor">   
	<div class="ui-floor-btm">
		<!--去掉原来的floor标题样式
		<div class="ui-floor-title">
			<div class="ui-floor-title-text">{{attrs.title}}</div>
			<a class="ui-floor-title-more font_arial" href="{{attrs.moreLink}}"> <span
				class="floatleft">更多</span> <span class="icon_more_arrow"></span> </a>
		</div>-->
		<!--商品列表-->
		<div class="ui-floor-list">
			{{#each model}}
			<div class="ui-floor-item border_bottom">
				<span class="floor_icon ontop" style="background: url('/{{this.vribbonimgurl}}');"></span>
				<div class="clear"></div>
				<span title="{{this.vdisplayname}}" class="product_name labeled">{{this.vdisplayname}}</span>
				<a class="product_thumb" href="{{../G.PAGE.PRODUCT_DETAIL}}{{this.cproductid}}" target="_blank" data-attrs-ectype="product" data-attrs-productId="{{cproductid}}">
					<img src="/{{this.vimageurl}}"/> 
				</a>
				<div class="floor">
					<div>
						<span data-ectype="price" data-productid="{{this.cproductid}}"></span>
					</div>
					<div class="nabnum">
						<span>库存&nbsp;</span>
						<span unit="{{this.pk_measdoc_name}}"
							productid="{{this.cproductid}}" ectype="nabnum" scale="{{this.pk_measdoc_unitScale}}"
							class="nc-product-num" bspotflag="{{this.bspotflag}}"
							id="product_nnabnum{{this.cproductid}}">0</span>
					 	</span>
					 </div>
					  <div class="priceloaded nabnumloaded">
				  		<a productid="{{this.cproductid}}" class="J-cart add_to_cart ET_addToCart noprice nonabnum" href="#" productid="{{this.cproductid}}" title="加入购物车"></a>
					  </div>
				</div>
			</div>
			{{/each}}
		</div>
	</div>
	<div class="clear"></div>
</div>