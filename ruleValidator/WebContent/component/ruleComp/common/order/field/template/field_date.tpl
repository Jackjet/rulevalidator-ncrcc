{{#with model}}
<div id="id" class="ui-field textfield_wrapper {{cls}}">
	<label id="{{labelId}}" for="{{name}}" class="{{labelCls}}" style="{{labelStyle}}">{{label}}{{separator}}</label>
	<input id="{{fieldId}}" name="{{name}}" class="textfiled datepicker {{fieldCls}}" style="{{filedStyle}}" value="{{value}}" placeholder="{{placeholder}}" pattern="{{pattern}}">
	<label class="ui-error"></label>
</div>
{{/with}}

