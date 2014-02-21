<legend style="font-weight: bold; font-size: 14px;">新增</legend>
<fieldset class="ui-menuitem-form">
	<legend>
		<span>
			<span>新增</span>
			<span class="ui-legend-separater"></span>
			
		</span>
	</legend>
	<div class="ui-form-panel">
		<form id="form-menu-add">
			<div class="ui-form-field hidden">
				<label class="label">父节点ID</label>
				<input name="parentId" type="hidden" value="{{parentId}}">
			</div>
			<div class="ui-form-field">
				<label class="label">菜单标题</label>
				<input name="menuTitle" class="textfield" placeholder="请填写菜单标题">
			</div>
			<div class="ui-form-field">
				<label class="label">资源</label>
				<input id="resourceSearcher" class="textfield" placeholder="请选择资源">
				<input type="hidden" name="resourceId">
			</div>
			<div class="ui-form-field">
				<label class="label">是否隐藏</label>
				{{#if hide}}
				<input class="textfield ui-inlineblock switcher" name="hide"
					value="1" type="hidden">
				{{else}} 
				<input class="textfield ui-inlineblock switcher" name="hide"
					value="0" type="hidden">
				{{/if}}
			</div>
		</form>
	</div>
</fieldset>
<hr>
<div class="ui-form-panel">
		<form id="form-menu-add">
			<div class="ui-form-field">
				<label class="label">&nbsp;</label>
				<button class="ui-button" title="保存" id="button-save">保存</button>
			</div>
		</form>
</div>
