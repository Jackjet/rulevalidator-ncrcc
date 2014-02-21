 <div class="sidemenu">
    <div class="menu_group_title">
        <span class="title_icon order"></span>
        <span>{{model.menuTitle}}</span>
    </div>
    <div class="order_menu">
        <ul class="menu_group">
        	{{#each model.childrenView}}
			{{#if hide}}
				<li class="hidden">
			{{else}}
				<li>
			{{/if}}
					<a id="{{menuId}}" target="main" href="{{../attrs.ecp}}{{resourceView.url}}" >
						<span>{{menuTitle}}</span>
						<span class="active_icon"></span>
						<span class="menu_bottomline"></span>
					</a>
				</li>
			{{/each}}
		</ul>
    </div>
</div>