<legend style="font-weight: bold; font-size: 14px;">详单</legend>
<fieldset class="ui-menuitem-form">
	<legend>
		<span>
			<span>详单</span>
		</span>
	</legend>
	<div class="ui-form-panel">
		<form id="form-menu-detail">
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
					value="{{menuTitle}}" readonly="readonly">
			</div>
			<div class="ui-form-field">
				<label class="label">资源</label>
				<input class="textfield" readonly="readonly"
					value="{{resourceView.resourceName}}">
				<input type="hidden" name="resourceId">
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
					value="1" type="hidden" readonly="readonly">
				{{else}}
				<input class="textfield ui-inlineblock switcher" name="hide"
					value="0" type="hidden" readonly="readonly">
				{{/if}}
			</div>
		</form>
	</div>
</fieldset>
