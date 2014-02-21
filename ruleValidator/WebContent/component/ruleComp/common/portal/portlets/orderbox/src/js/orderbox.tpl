<div class="order_box">
	<div class="order_box_content">
		<ul>
			{{#each this.model}}
				<li>
					<a href="{{this.url}}{{this.param}}">
						<span class="font">{{this.name}}
						<span id="{{this.id}}" class="order_box_content_num">（{{this.num}}）</span><br>
					</a>
				</li>
			{{/each}}
		</ul>
	</div>
	<div class="order_box_footer">
		<a href="#" id="orderbox_createOrder" class="order_btn_add ">
			<span>新建订单</span>
			<span class="icon_add"></span>
		</a>
		<div class="clear"></div>
	</div>
</div>
	