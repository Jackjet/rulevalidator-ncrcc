<div class="ui-form-panel">
	<form id="form-address-update">
		<fieldset>
		<legend><strong>联系人信息</strong></legend>
		<div class="ui-form-field">
			<label class="label">账号<span class="star">*</span></label>
			<input name="pkUser"  type="hidden" id="name" value="{{this.model.pkUser}}">
			<input name="userCode" class="textfield" maxlength="300" id="userCode" value="{{this.model.userCode}}">
		</div>
		<div class="ui-form-field">
			<label class="label">名称<span class="star">*</span></label>
			<input name="userName" class="textfield" maxlength="300" id="userName" value="{{this.model.userName}}">
		</div>
		<div class="ui-form-field">
			<label class="label">性别</label>
			<select name="sex" id="sex" >
				<option value="1" >男</option>
				<option value="2" {{#if this.model.sex}}selected="selected"{{/if}}>女</option>
			</select>
		</div>
		<div class="ui-form-field">
			<label class="label">手机号码</label>
			<input name="cellPhone" class="textfield" maxlength="30" id="cellPhone" value="{{this.model.cellPhone}}">
		</div>
		<div class="ui-form-field">
			<label class="label">Email</label>
			<input name="email" class="textfield" maxlength="300" id="email" style="width:316px" value="{{this.model.email}}">
		</div>
		<div class="ui-form-field">
			<label class="label">备注</label>
			<input name="memo" class="textfield" id="memo" maxlength="300" style="width:316px" value="{{this.model.memo}}">
		</div>
		</fieldset>
	</form>
</div>