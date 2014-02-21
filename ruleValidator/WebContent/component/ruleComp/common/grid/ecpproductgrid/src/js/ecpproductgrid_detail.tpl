<div class="ui-ecpproductgrid">
	<div class="product_list_title">
		<a class="switch_view active ">
			<span class="list_view_title J_switchDetail">详细</span>
		</a>
		<a class="switch_view ">
			<span class="list_view_title J_switchThumb">缩略图</span>
		</a>
		<a class="switch_view ">
			<span class="list_view_title J_switchList">列表</span>
		</a>
	</div>

	<table cellspacing="0" cellpadding="0">
		<thead>
			<tr>
				<td class="J_vcode" width="20%">商品编码</td>
				<td class="J_vname" width="20%">商品名称</td>
				<td class="J_displayname" width="20%">展销商品名称</td>
				<td width="10%">基准价</td>
				<td width="10%">当前价</td>
				<td width="10%">库存</td>
				<td class="J_saleVolume" width="10%">销量</td>
				<td width="10%">数量</td>
				<td width="4%" title="全部购买">
					<input type="checkbox" value="" class="all-add-to-shopcart">
				</td>
				<td width="6%">收藏</td>
			</tr>
		</thead>
		<tbody>
			{{#each model.records}}
			<tr>
				<td class="J_vcode">
					<a href="{{../G.PAGE.PRODUCT_DETAIL}}{{this.cproductid}}" target="_blank"
						ectype="recent" productid="{{this.cproductid}}">
						<div class="wrap">{{this.vcode}}</div>
					</a>
				</td>
				<td class="J_vname">
					<a href="{{../G.PAGE.PRODUCT_DETAIL}}{{this.cproductid}}" target="_blank"
						ectype="recent" productid="{{this.cproductid}}">
						<div class="wrap">{{this.vname}}</div>
					</a>
				</td>
				<td class="J_displayname">
					<a href="{{../G.PAGE.PRODUCT_DETAIL}}{{this.cproductid}}" target="_blank"
						ectype="recent" productid="{{this.cproductid}}">
						<div class="wrap">{{this.vname}}</div>
					</a>
				</td>
				<td>
					<span class="font_bold based nc-no-float" ectype="price" data-priceType="base"
						productId="{{this.cproductid}}">
					</span>
				</td>
				<td>
					<span id="product_price{{this.cproductid}}" class="font_bold based nc-no-float"
						ectype="price" productId="{{this.cproductid}}" value="{{this.nsaleprice}}">
					</span>
				</td>
				<td>
					<span class="font_bold overhide" id="product_nnabnum{{this.cproductid}}"
						bspotflag="{{this.bspotflag}}" scale="{{this.pk_measdoc_unitScale}}" ectype="nabnum"
						productid="{{this.cproductid}}" unit="{{this.pk_measdoc_name}}">
					</span>
				</td>
				<td class="J_saleVolume">
					<span class="font_bold overhide" scale="{{this.pk_measdoc_unitScale}}">{{this.saleVolume}}</span>
					<span>{{this.pk_measdoc_name}}</span>
				</td>
				<td >
					<input type="text" id="product_buyNum" class="ui-ecpproductgird-textfield" scale="{{this.pk_measdoc_unitScale}}" value="1" />
					<span>{{this.pk_measdoc_name}}</span>
				</td>
				<td class="ui-operate">
					<!-- 添加到购物车 -->
					<div class="noprice nonabnum nabnumloaded priceloaded">
						<a class="add_to_cart J-cart ET_addToCart nc-margin-right-21" href="#" productid="{{this.cproductid}}" title="加入购物车"></a>
					</div>
				</td>
				<td class="ui-operate">
					<!-- 收藏 -->
					<div>
						<a class="ui-favorite-add-button-small add_to_favorite" href="#" productId="{{this.cproductid}}" title="添加收藏"></a>
					</div>
				</td>
			</tr>
			{{/each}}
			<tr class="hidden">
				<td><input type="checkbox" class="hidetd">隐藏</td>
				<td><input type="checkbox" class="hidetd">隐藏</td>
				<td><input type="checkbox" class="hidetd">隐藏</td>
				<td><input type="checkbox" class="hidetd">隐藏</td>
				<td><input type="checkbox" class="hidetd">隐藏</td>
				<td><input type="checkbox" class="hidetd">隐藏</td>
				<td><input type="checkbox" class="hidetd">隐藏</td>
				<td><input type="checkbox" class="hidetd">隐藏</td>
				<td><input type="checkbox" class="hidetd">隐藏</td>
				<td><input type="checkbox" class="hidetd">隐藏</td>
			</tr>
		</tbody>
	</table>
	<div class="J-ecpgrid-status ui-ecpgrid-status"></div>
</div>







