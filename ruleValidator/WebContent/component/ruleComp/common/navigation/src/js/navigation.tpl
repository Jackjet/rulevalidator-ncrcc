<div id="navigation" class="main_navi_bar">
	{{#each model}} 
		{{#if this.hide}}
		<a class="hidden"
		{{else}}
		<a 
		{{/if}}
		id="navi_{{menuId}}" menuid="{{menuId}}"
			href="{{../attrs.ctx}}{{resourceView.url}}" class="normal index{{menuSeq}}"
			seq="{{seq}}" menuseq="{{menuSeq}}">{{menuTitle}}</a>
	{{/each}}
</div>