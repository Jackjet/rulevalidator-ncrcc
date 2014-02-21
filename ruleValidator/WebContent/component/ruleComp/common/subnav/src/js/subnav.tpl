<div class="subpage_navi">
	<a href="{{attrs.ctx}}" target="_parent">首页</a>
	<span>&gt;</span>
		{{#each model}}
		{{#if childrenView}}
		<a href="{{../../attrs.ctx}}{{resourceView.url}}" target="_parent" title="{{menuTitle}}">{{menuTitle}}</a>
		<span>&gt;</span>
		{{else}}
		<b><span title="{{menuTitle}}">{{menuTitle}}</span></b>
		{{/if}}
	{{/each}}
	<div class="clear"></div>
</div>

