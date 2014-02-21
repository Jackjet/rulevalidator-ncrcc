<div class="ui-field textfield_wrapper">
	<label for="{{name}}" class="{{labelCls}}" style="{{labelStyle}}">{{label}}{{separator}}</label>
	<select name="{{name}}">
		{{#each collection}}
		<option value="{{key}}" title="{{value}}">{{value}}</option>
		{{#/ach}}
	</select>
</div>