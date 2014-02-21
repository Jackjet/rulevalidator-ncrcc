<div class="ui-category-btm">
	<div class="J-collapse">
		<div class="ui-category-collapse">
			<label>全部商品分类</label>
			<span class="ui-category-collapse-icon J-arrow"></span>
		</div>
	</div>
	<!-- 一级菜单-->
	<div class="J-category-content ui-category-content">
		{{#each model}}
		<div class="sidebox_title item_seperator">
			<span class="title_icon category_1"></span>
			<a id="{{this.cid}}" class="title_text font_yahei J-ProductList">{{this.name}}</a>
			{{#if this.children}}
			<a href="#" class="trigger category_down"></a>
			{{/if}}
		</div>
		<div class="floatrelated sidebox_inner_content effectSlide hidden">
			{{#if this.children}}
			<ul class="category_list">
				{{#each this.children}}
				<li class="category_2nd_li">
					<a href="#" id="{{this.cid}}" class="category_2nd_a J-ProductList">
						<span class="menu_dot_gray"></span>
						<span class="menu_text">{{this.name}}</span>
					</a>
					<!--二级菜单-->
					{{#if this.children}}
					<div class="hidden J-ProductList">
						<div class="submenu">
							<div class="corner_lt"></div>
							<div class="border_top"></div>
							<div class="corner_rt"></div>
							<div class="border_lft">
								<div class="border_rit">
									<div class="menu_arrow"></div>
									<div class="clear"></div>
									<div class="menu_content">
										<div class="w_100ps">
											<!-- 三级菜单-->
											{{#each this.children}}
											<div class="menu_title nc-field-long-hide">
												<nobr>
													<a class="J-ProductList" href="#" id="{{this.cid}}">{{this.name}}</a>
												</nobr>
											</div>
											<ul class="menu_list">
												{{#if this.children}}
												<!-- 四级菜单 -->
												{{#each this.children}}
												<li>
													<a class="J-ProductList" href="#" id="{{this.cid}}">{{this.name}}</a>
												</li>
												{{/each}} {{/if}}
											</ul>
											{{/each}}
										</div>
									</div>
								</div>
							</div>
							<div class="corner_lb"></div>
							<div class="border_btm"></div>
							<div class="corner_rb"></div>
						</div>
					</div>
					{{/if}}
				</li>
				{{/each}}
			</ul>
			{{/if}}
		</div>
		{{/each}}
	</div>
	<div class="clear"></div>
</div>
