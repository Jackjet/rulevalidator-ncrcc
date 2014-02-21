<div class="ui-tab">
	<div class="ui-tab-triggers">
		<ul>
		{{#each triggers}}
			<li class="ui-tab-trigger-cell">{{this}}</li>
		{{/each}}
		</ul>
	</div>
	<div class="ui-tab-containers">
		{{#each contents}}
		<div class="ui-tab-container-cell">{{this}}</div>
		{{/each}}
	</div>
</div>
