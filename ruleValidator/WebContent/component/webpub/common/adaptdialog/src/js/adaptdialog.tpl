<div class="ui-adaptdialog">	
	<div class="ui-adaptdialog-top">
		<span class="ui-adaptdialog-top-title">{{attrs.title}}</span>
		<span class="J-close ui-adaptdialog-top-close"></span>
	</div>
	<div class="ui-adaptdialog-content J-content">
	</div>
	<div class="ui-adaptdialog-btm">
		{{#each attrs.buttons}}
			<a href="#" id="{{this.key}}">{{this.text}}</a>
		{{/each}}
	</div>
</div>