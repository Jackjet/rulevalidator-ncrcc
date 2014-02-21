<div class="ui-contextmenu" id="{{groupId}}">
	<ul class="menus">
		{{# each menus}}
		<li class="menu-item" id="{{menuId}}">
			<a class="menu-thumb" href="#" title="{{name}}">
				<span class="ui-icons {{iconCls}}"></span>
				<span class="menu-text">{{name}}</span>
			</a>
		</li>
		{{/each}}
	</ul>
</div>