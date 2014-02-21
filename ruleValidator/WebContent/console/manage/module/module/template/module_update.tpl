<div class="ui-form-panel">
	<form id="form-module-update">
		<div class="ui-form-field hidden">
			<label class="label">模块ID</label>
			<input class="textfield readonly" value="{{moduleId}}" readonly="readonly">
			<input name="moduleId" type="hidden" value="{{moduleId}}">
		</div>
		<div class="ui-form-field hidden">
			<label class="label">父模块ID</label>
			<input name="parentId" type="hidden" value="{{parentId}}">
		</div>
		<div class="ui-form-field">
			<label class="label">模块编码</label>
			<input name="moduleCode" class="textfield" placeholder="请填写模块ID" value="{{moduleCode}}">
		</div>
		<div class="ui-form-field">
			<label class="label">模块名称</label>
			<input name="moduleName" class="textfield" placeholder="请填写资源名称" value="{{moduleName}}">
		</div>
		<div class="ui-form-field hidden">
			<label class="label">depth</label>
			<input name="depth" class="textfield" value="{{seq}}" type="number">
		</div>
		<div class="ui-form-field">
			<label class="label">安全级别</label>
			{{#select collection_securityLevel}} {{/select}}
		</div>
		<div class="ui-form-field">
				<label class="label">顺序</label>
				<input class="textfield" name="seq" value="{{seq}}" type="number">
			</div>
	</form>
</div>