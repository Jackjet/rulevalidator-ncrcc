<div class="ui-orderplanpanel height_40">
	<div class="ui-orderplanpanel-icon">
		<a href="#" class="close J-collapse"></a>
	</div>
	<div class="ui-orderplanpanel-content">
		{{#each this.model}}
			{{#if this.isShow}}
				<div class="item">
			{{else}}
				<div class="item hidden">
			{{/if}}
				<label>{{this.label}}：</label>
				{{#if this.isEditable}}
					<input class="J-{{this.field}} J-orderplanpanel-edit" data-key="{{this.key}}" value="{{this.value}}"/>
				{{else}}
					<span class="J-{{this.field}}" data-key="{{this.key}}">{{this.value}}</span>
				{{/if}}
			</div>
		{{/each}}
	</div>
	<div class="ui-orderplanpanel-button">
		<div class="btn_wrap">
			{{#each this.attrs.buttons}}
				{{#if this.show}}
					<a href="#" class="J-{{this.key}} {{this.type}}" style="{{this.style}}">{{this.name}}</a>
				{{else}}
					<a href="#" class="J-{{this.key}} hidden {{this.type}}">{{this.name}}</a>
				{{/if}}
			{{/each}}
		</div>
	</div>
	<div class="clear"></div>
</div>