{{#with model}}
<div id="id" class="ui-field textfield_wrapper {{cls}} {{#if isHidden}}hidden{{/if}}">
	<label id="{{labelId}}" for="{{name}}" class="{{labelCls}} {{#if isLabelHidden}}hidden{{/if}}" style="{{labelStyle}}">{{label}}{{separator}}</label>
</div>
{{/with}}