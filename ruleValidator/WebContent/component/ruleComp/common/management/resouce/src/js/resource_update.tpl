<div class="ui-form-panel">
	<form id="form-address-update">
		<fieldset>
			<legend><strong>资源信息</strong></legend>
			<div class="ui-form-field">
				<label class="label">资源名称<span class="star">*</span></label>
				<input name="resourceName" class="textfield" style="width:316px" maxlength="300" id="resourceName" value="{{this.model.resourceName}}">
			</div>
			<div class="ui-form-field">
				<label class="label">URL<span class="star">*</span></label>
				<input name="resourceId"  type="hidden" id="resourceId" value="{{this.model.resourceId}}">
				<input name="url" class="textfield" maxlength="300" style="width:316px" id="url" value="{{this.model.url}}">
			</div>
			<div class="ui-form-field">
				<label class="label">安全级别</label>
				<select name="securityLevel" id="securityLevel" >
					<option value="1" >1</option>
					<option value="2" {{#if this.model.securityLevel}}selected="selected"{{/if}}>2</option>
				</select>
			</div>
			<div class="ui-form-field">
				<label class="label">模块编码</label>
				<input name="moduleId" class="textfield" maxlength="20" id="moduleId" value="{{this.model.moduleId}}">
			</div>
			<div class="ui-form-field">
				<label class="label">序号</label>
				<input name="seq" class="textfield" maxlength="2" id="seq" value="{{this.model.seq}}">
			</div>
		</fieldset>
	</form>
</div>