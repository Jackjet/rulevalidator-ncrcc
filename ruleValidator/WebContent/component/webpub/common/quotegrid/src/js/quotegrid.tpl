<div class="bid_products">
    <ul class="bid_products_header">
        <li class="first_column">产品</li>
        <li class="amount_column">数量</li>
        <li class="price_column">价格</li>
        <li class="last_column">金额</li>
    </ul>
    {{#each this.model.records}}
    <div class="bid_products_body bg_f8">
        <div class="first_column">
            <strong>{{this.name}}</strong>
            <dl class="bid_product_config">
                <dt>配置:</dt>
                <dd>{{this.config}}</dd>
                <dt>规格:</dt>
                <dd>{{this.format}}</dd>
                <dt>型号:</dt>
                <dd>{{this.type}}</dd>
                <dt>产地:</dt>
                <dd>{{this.made}}</dd>
            </dl>
        </div>
    	<div class="amount_column">{{this.num}}件</div>
        <div class="price_column">
            <div class="form_row w_100ps">
                <div class="form_title w_80px">出厂价</div>
                <div class="form_field">
                    <label><input class="textfield pd_2px w_120px h_12px" value="{{this.factory_price}}"/></label>
                </div>
            </div>
            <div class="form_row w_100ps">
                <div class="form_title w_80px">加工费</div>
                <div class="form_field">
                    <label><input class="textfield pd_2px w_120px h_12px" value="{{this.process_cost}}"/></label>
                </div>
            </div>
            <div class="form_row w_100ps">
                <div class="form_title w_80px">运费</div>
                <div class="form_field">
                    <label><input class="textfield pd_2px w_120px h_12px" value="{{this.fare}}"/></label>
                </div>
            </div>
            <div class="form_row w_100ps">
                <div class="form_title w_80px">单价</div>
                <div class="form_field">
                    <label><input class="textfield pd_2px w_120px h_12px" value="{{this.univalent}}"/></label>
                </div>
            </div>
            <div class="form_row w_100ps">
                <div class="form_title w_80px">税率</div>
                <div class="form_field">
                    <label><input class="textfield pd_2px w_120px h_12px" value="{{this.tax_rate}}"/></label>
                </div>
            </div>
        </div>
        <div class="last_column">
            <span class="color_f73107">￥{{this.money}}</span>
        </div>
        
        <!--详细信息-->
        <div class="bid_product_detail">
	        <div class="switcher">
	            <a href="#" class="J-ProductDetail">明细</a>
	        </div>
	        {{#each this.detail}}
	        <div class="big_detail_modules">
	            <div class="big_detail_module bdbtm_dashed">
	                <div class="first_column">
	                    <strong>{{this.name}}</strong>
	                    <dl class="bid_product_config">
	                        <dt>规格：</dt>
	                        <dd>{{this.format}}</dd>
	                        <dt>型号：</dt>
	                        <dd>{{this.type}}</dd>
	                        <dt>产地：</dt>
	                        <dd>{{this.made}}</dd>
	                    </dl>
	                </div>
	                <div class="amount_column">{{num}}件</div>
	                <div class="price_column">
	                    <div class="form_row w_100ps">
	                        <div class="form_title w_80px">出厂价</div>
	                        <div class="form_field">
	                            <label><input class="textfield pd_2px w_120px h_12px" value="{{this.factory_price}}"/></label>
	                        </div>
	                    </div>
	                    <div class="form_row w_100ps">
	                        <div class="form_title w_80px">加工费</div>
	                        <div class="form_field">
	                            <label><input class="textfield pd_2px w_120px h_12px" value="{{this.process_cost}}"/></label>
	                        </div>
	                    </div>
	                    <div class="form_row w_100ps">
	                        <div class="form_title w_80px">运费</div>
	                        <div class="form_field">
	                            <label><input class="textfield pd_2px w_120px h_12px" value="{{this.fare}}"/></label>
	                        </div>
	                    </div>
	                    <div class="form_row w_100ps">
	                        <div class="form_title w_80px">单价</div>
	                        <div class="form_field">
	                            <label><input class="textfield pd_2px w_120px h_12px" value="{{this.univalent}}"/></label>
	                        </div>
	                    </div>
	                    <div class="form_row w_100ps">
	                        <div class="form_title w_80px">税率</div>
	                        <div class="form_field">
	                            <label><input class="textfield pd_2px w_120px h_12px" value="{{this.tax_rate}}"/></label>
	                        </div>
	                    </div>
	                </div>
	                <div class="last_column">
	                    <span class="color_f73107">￥{{this.money}}</span>
	                </div>
	            </div>
	        </div>
	        {{/each}}
	     </div>
	</div>
	{{/each}}
</div>
