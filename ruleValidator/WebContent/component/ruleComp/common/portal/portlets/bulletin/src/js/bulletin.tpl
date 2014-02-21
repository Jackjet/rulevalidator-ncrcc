<div class="ui-bulletin">
	<ul class="bulletin-list">
		{{#if this.model.notempty}}
			{{#each model.records}}
			<li>
				<a target="_blank" href="{{../G.PAGE.BULLETIN_DETAIL}}{{cbulletinid}}" title="{{vtitle}}" id="{{cbulletinid }}">{{vtitle}}</a>
			</li>
			{{/each}}
		{{else}}
			<li>
				{{this.model}}
			</li>
		{{/if}}
	</ul>
	<div class="clear"></div>
</div>
