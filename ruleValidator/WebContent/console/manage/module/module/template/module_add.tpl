<div class="ui-form-panel">
	<form id="form-module-add">
		<div class="ui-form-field hidden">
			<label class="label">父模块ID</label>
			<input class="textfield readonly" value="{{parentId}}" readonly="readonly">
			<input name="parentId" type="hidden" value="{{parentId}}">
		</div>
		<div class="ui-form-field">
			<label class="label">模块编码</label>
			<input name="moduleCode" class="textfield" placeholder="请填写模块ID" value="{{parentCode}}">
		</div>
		<div class="ui-form-field">
			<label class="label">模块名称</label>
			<input name="moduleName" class="textfield" placeholder="请填写资源名称">
		</div>
		<!-- <div class="ui-form-field">
			<label class="label">SEQ</label>
			<input name="seq" class="textfield" placeholder="请填写URL">
		</div> -->
		<!-- <div class="ui-form-field">
			<label class="label">depth</label>
			<input name="depth" class="textfield" placeholder="请填写URL">
		</div> -->
		<div class="ui-form-field">
			<label class="label">安全级别</label>
			{{#select collection_securityLevel}} {{/select}}
		</div>
	</form>
</div>