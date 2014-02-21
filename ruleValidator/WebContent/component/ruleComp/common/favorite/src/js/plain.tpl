<div class="ui-ecpproductgrid">
	
	<table cellspacing="0" cellpadding="0">
		<thead>
			<tr>
				<td width="20%">编码</td>
				<td width="20%">名称</td>
				<td width="10%">价格</td>
				<td width="15%">库存</td>
				<td width="15%">
					<div class="ui-grid-order">
						<a href="#" class="title" column="ts" order="desc">收藏时间</a>
						<div class="arrow">
							{{#if this.attrs.isDesc}}
								<span class="down" title="降序"></span>
							{{else}}
								<span class="up" title="升序"></span>
							{{/if}}
						</div>
					</div>
				</td>
				<td width="10%" title="全部购买">
					<input type="checkbox" value="" class="all-add-to-shopcart">
				</td>
				<td width="10%">删除</td>
			</tr>
		</thead>
		<tbody>
			{{#each model.records}}
			<tr>
				<!-- 编码 -->
				<td>
					<a href="{{../G.PAGE.PRODUCT_DETAIL}}{{this.product.cproductid}}" target="_blank" ectype="recent" productid="{{this.product.cproductid}}">
						<div class="wrap">{{this.product.vcode}}</div>
					</a>
				</td>
				<!-- 名称 -->
				<td>
					<a href="{{../G.PAGE.PRODUCT_DETAIL}}{{this.product.cproductid}}" target="_blank" ectype="recent" productid="{{this.product.cproductid}}">
						<div class="wrap">{{this.product.vname}}</div>
					</a>
				</td>
				<!-- 价格 -->
				<td style="text-align: right;">
					<span class="font_bold based nc-no-float" ectype="price" productId="{{this.product.cproductid}}"></span>
				</td>
				<!-- 库存 -->
				<td style="text-align: right;">
					<span class="font_bold overhide" id="product_nnabnum{{this.product.cproductid}}" bspotflag="{{this.product.bspotflag}}"
						scale="{{this.product.pk_measdoc_unitScale}}" ectype="nabnum"
						productid="{{this.product.cproductid}}" unit="{{this.product.pk_measdoc_name}}"></span>
				</td>
				<!-- 收藏时间 -->
				<td>
					<span>
						<span>{{this.favorite.ts$}}</span>
					</span>
				</td>
				<!-- 操作 -->
				<td class="ui-operate">
					<div class="noprice nonabnum  nabnumloaded priceloaded">
						<a href="" class="add_to_cart J-cart ET_addToCart nc-margin-right-21" id="{{this.product.cproductid}}" productid="{{this.product.cproductid}}" title="加入购物车"></a>
					</div>
				</td>
				<td class="ui-operate">
					<div>
						<a href="" class="ui-favorite-btn-remove nc-margin-right-21" productid="{{this.product.cproductid}}" title="删除"></a>
					</div>
				</td>
			</tr>
			{{/each}}
		</tbody>
	</table>
</div>
