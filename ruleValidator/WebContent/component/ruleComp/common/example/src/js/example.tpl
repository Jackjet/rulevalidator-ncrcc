<div class="ui-example">
	<div class="ui-example-top">{{this.model.title}}</div>
	<div class="ui-example-content">
		<ul>
			{{#each this.model.content}}
			<li>{{this}}</li>
			{{/each}}
		</ul>
	</div>
</div>
