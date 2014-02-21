<div class="ui-ecp-favorite-dialog">
	<div class="ui-ecp-favorite-tip"></div>
	
	<div class="ui-ecp-favorite-tag">
		<div class="field">
			<label class="label">
				<span class="text-align-right">自定义标签：</span>
			</label>
			
			<div>
				<input ectype="textfield" fieldName="tag"  placeholder="多个标签请用逗号隔开" maxlength="30"/>
				<input name="tag" value="" type="hidden"/>
			</div>
		</div>
		<div class="field">
			<label  class="label">
				<span class="text-align-right">常用标签：</span>
			</label>
			<div class="tag-candidate">
				{{#each this}}
				<a href="#">{{this}}</a>
				{{/each}}
			</div>
		</div>
	</div>
</div>
