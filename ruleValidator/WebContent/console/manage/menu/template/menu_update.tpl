<legend style="font-weight: bold; font-size: 14px;">修改</legend>
<fieldset class="ui-menuitem-form">
	<legend>
		<span>
			<span>修改</span>
			<span class="ui-legend-separater"></span>
		</span>
	</legend>
	<div class="ui-form-panel">
		<form id="form-menu-update">
			<div class="ui-form-field hidden">
				<label class="label">父节点ID</label>
				<input name="parentId" type="hidden" value="{{parentId}}">
			</div>
			<div class="ui-form-field hidden">
				<label class="label">菜单ID</label>
				<input name="menuId" type="hidden" value="{{menuId}}">
			</div>
			<div class="ui-form-field">
				<label class="label">菜单标题</label>
				<input name="menuTitle" class="textfield" placeholder="请填写菜单标题"
					title="{{menuTitle}}" value="{{menuTitle}}">
			</div>
			<div class="ui-form-field">
				<label class="label">资源</label>
				<input id="resourceSearcher" class="textfield" placeholder="请选择资源"
					title="{{resourceView.resourceName}}"
					value="{{resourceView.resourceName}}">
				<input type="hidden" name="resourceId"
					value="{{resourceView.resourceId}}">
			</div>
			<div class="ui-form-field" style="width: 460px;">
				<label class="label">URL</label>
				<span id="url" title="{{resourceView.url}}" class="ui-to-long"
					style="width: 350px">{{resourceView.url}}</span>
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
			<div class="ui-form-field">
				<label class="label">顺序</label>
				<input class="textfield" name="seq" value="{{seq}}" type="number">
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
