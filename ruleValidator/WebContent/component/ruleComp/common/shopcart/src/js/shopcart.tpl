<!--购物车按钮-->
<div class="cart_checkout">
	<!--隐藏的购物车列表-->
	<div class="floatleft hidden">
		<!--购物车列表-->
		<div id="panelCartList" class="dialog_panel">
			<!-- 购物车头部 页签和删除按钮 -->
			<div class="panel_top">
				<div class="btn_panel">
					<div class="label_wapper">
						<span class="label_amount_green label_regular label_amount_mrg_70">
							<span class="left"></span>
							<span class="number font_arial idCartListAmount et_prodNum">0</span>
							<span class="right"></span>
						</span>
					    <a href="#shopCartLabel" class="label_active ET_switchShopCartList">
					     	<span class="label_text">购物车</span>
					    </a>
					</div>
					<div class="label_wapper">
						<span
							class="label_amount_orange label_regular label_amount_mrg_70">
							<span class="left"></span>
							<span class="number font_arial idCartDelListAmount">0</span>
							<span class="right"></span> 
						</span>
						 <a href="#shopCartDelLabel" class="label_normal ET_switchShopCartList"> 
						 	<span class="label_text">已删除</span>
						 </a>
					</div>
					<a href="#tableCartDel" class="panel_closer ET_closeShopCart"></a>
				</div>
			</div>
			<!-- 购物车列表数据 -->
			<div class="panel_content">
				<!-- 购物车列表 -->
				<div id="shopCartLabel" class="w_100ps idTableCartList"></div>
				<!-- 已删除列表 -->
				<div id="shopCartDelLabel" class="w_100ps idTableCartDel hidden"></div>
				<textarea id="templet_shopCartLabel" class="hidden">
					<div class="shopcart_table">
						<table border="1" cellpadding="0" cellspacing="0">
							<thead>
								<tr>
									<td>序号</td>
									<td>商品编码</td>
									<td>商品名称</td>
									<td>物料</td>
									<td>销售组织</td>
									<td class="">价格</td>
									<td class="w_150px">数量</td>
									<td>库存</td>
									<td class="">金额</td>
									<td>操作</td>
								</tr>
							</thead>
							<tbody>
								{#foreach $T as shopCart}
									
									{#if $T.shopCart$index%2==0}
										<tr id="{$T.shopCart.cshopcartid}" productid="{$T.shopCart.cproductid}">
									{#elseif $T.shopCart$index%2==1}
										<tr id="{$T.shopCart.cshopcartid}" productid="{$T.shopCart.cproductid}" class="row_even">
									{#/if}
											<td class="align_center">{$T.shopCart$index+1}</td>
											<td>{$T.shopCart.vcode}</td>
											<td>{$T.shopCart.vname}</td>
											<td>{$T.shopCart.cinventoryid_name}</td>
											<td>{$T.shopCart.saleOrgName}</td>
											<td class="align_right">
												{#if $T.shopCart.pk_org_priceScale!=""}
													<span class="ft_yahei">{$T.shopCart.corigcurrencyid_curSign}</span>
													<span scale="{$T.shopCart.pk_org_priceScale}" class="ET_caclPrice">{$T.shopCart.price}</span>
												{#else}
													<font color="red">暂无报价</font>
												{#/if}
											</td>
											<td class="align_left">
												<label>
													<input value="{$T.shopCart.count}" scale="{$T.shopCart.pk_measdoc_unitScale}"
														class="textfiled w_90px ET_caclNumber ET_shopcart_saveNumber" />
													<span class="nc-unit">{$T.shopCart.pk_measdoc_name}</span>
												</label>
											</td>
											<td class="align_right">
												<span scale="{$T.shopCart.pk_measdoc_unitScale}"
													ectype="nabnum" productid="{$T.shopCart.cproductid}"
													unit="{$T.shopCart.pk_measdoc_name}"></span>
											</td>
											<td class="align_right">
												<span class="ft_yahei">{$T.shopCart.corigcurrencyid_curSign}</span>
												<span class="ET_caclSummy"
													scale="{$T.shopCart.corigcurrencyid_amountScale}"></span>
											</td>
											<td class="align_center">
												<div>
													<a href="#" class="ET_addToFavorite" productid="{$T.shopCart.cproductid}">[收藏]</a>
												</div>
												<div>
													<a href="#" class="ET_delShopCartRow" productid="{$T.shopCart.cproductid}">[删除]</a>
												</div>
											</td>
										</tr>
									{#/foreach}
							</tbody>
						</table>
					</div>
					<div class="list_btm">
                       	<div class="floatleft">
                           	<div ectype="ProductAutoCompleteAdd"></div>
                           </div>
                           <div class="list_total font_yahei">
                           	<span class="total_title">合计金额：</span>
                              	<span class="total_price ET_caclTotal"  scale="4">${mnyAmount}</span>
                               <span class="list_clear">[ <a
							href="#" class="ET_clearShopCart">全部删除</a> ]</span>
                           </div>
                   	</div>
                   	
				</textarea>
				<textarea id="templet_shopCartDelLabel" class="hidden">
				<div class="shopcart_table">
					<table border="1" cellpadding="0" cellspacing="0">
						<thead>
							<tr>
								<td>序号</td>
								<td>商品编码</td>
								<td>商品名称</td>
								<td>物料</td>
								<td>销售组织</td>
								<td class="">价格</td>
								<td class="w_150px">库存</td>
								<td class="">操作</td>
							</tr>
						</thead>
						<tbody>
							{#foreach $T as shopCart}
								{#if $T.shopCart$index%2==0}
									<tr id="{$T.shopCart.cshopcartid}" productid="{$T.shopCart.cproductid}">
								{#elseif $T.shopCart$index%2==1}
									<tr id="{$T.shopCart.cshopcartid}" productid="{$T.shopCart.cproductid}" class="row_even">
								{#/if}
										<td class="align_center">{$T.shopCart$index+1}</td>
										<td>{$T.shopCart.vcode}</td>
										<td>{$T.shopCart.vname}</td>
										<td>{$T.shopCart.cinventoryid_name}</td>
										<td>{$T.shopCart.saleOrgName}</td>
										<td class="align_right">
											{#if $T.shopCart.pk_org_priceScale!=""}
												<span class="ft_yahei">{$T.shopCart.corigcurrencyid_curSign}</span>
												<span scale="{$T.shopCart.pk_org_priceScale}" class="ET_caclPrice">{$T.shopCart.price}</span>
											{#else}
												<font color="red">暂无报价</font>
											{#/if}
										</td>
										<td class="align_right">
											<span scale="{$T.shopCart.pk_measdoc_unitScale}"
												ectype="nabnum" productid="{$T.shopCart.cproductid}"
												unit="{$T.shopCart.pk_measdoc_name}"></span>
										</td>
										<td class="align_center"><a href="" class="ET_addDelToCart" productid="{$T.shopCart.cproductid}">添加到购物车</a></td>
									</tr>
							{#/foreach}
						</tbody>
					</table>
				</div>
					<div class="dlist_btm">
	                          <div class="list_total font_yahei">
	                              <span class="list_clear">[ <a href="#" class="ET_clearDelShopCart">清空</a> ]</span>
	                          </div>
	                  	</div>
				</textarea>
			</div>
			<!-- 购物车底部 -->
			<div class="panel_btm">
				<div class="shopcart_left_btn_panel hidden">
					<a href="#" class="btn_gray font_hei">寄存</a> 
					<a href="#" class="btn_gray font_hei">取出</a>
				</div>
				<div class="button_pane">
					<a href="{{G.API.ORDER_ADD}}" class="btn_blue font_hei ET_checkOut">去结算</a> 
					<a href="#" class="btn_gray font_hei ET_closeShopCart">关闭</a>
				</div>
			</div>
		</div>
	</div>
	<!--查看购物车按钮-->
	<a href="#" class="cart_amount ET_showShopCart">
		<span class="amount_no et_prodNum">0</span> <span class="amount_unit">件</span>
	</a> 
	<a href="{{G.API.ORDER_ADD}}" target="_blank" class="checkout J_checkOut"> 
		<span class="checkout_text">去结算</span> 
	</a>
</div>
