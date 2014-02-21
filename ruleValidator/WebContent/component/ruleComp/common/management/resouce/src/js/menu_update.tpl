<div class="ui-form-panel">
	<form id="form-address-update">
		<fieldset>
			<legend><strong>菜单信息</strong></legend>
			<div class="ui-form-field">
				<label class="label">菜单名称<span class="star">*</span></label>
				<input name="menuId"  type="hidden" id="menuId"  value="{{this.model.menuId}}">
				<input name="menuTitle" class="textfield" maxlength="300" style="width:316px" id="menuTitle" value="{{this.model.menuTitle}}">
			</div>
			<div class="ui-form-field">
				<label class="label">上级菜单<span class="star">*</span></label>
				<select name="parentId" id="parentId" >
					<option value="1" >男</option>
					<option value="2" {{#if this.model.parentId}}selected="selected"{{/if}}>女</option>
				</select>
			</div>
			<div class="ui-form-field">
				<label class="label">菜单资源<span class="star">*</span></label>
				<select name="resourceId" id="resourceId" >
				</select>
			</div>
			<div class="ui-form-field">
				<label class="label">显示顺序</label>
				<input name="seq" class="textfield" maxlength="300" id="seq"  value="{{this.model.seq}}">
			</div>
			<div class="ui-form-field">
				<label class="label">是否隐藏</label>
				<select name="hide" id="hide" >
					<option value="Y" >是</option>
					<option value="N" {{#if this.model.hide}}selected="selected"{{/if}}>否</option>
				</select>
			</div>
			<div class="ui-form-field">
				<label class="label">是否叶子</label>
				<select name="leaf" id="leaf" >
					<option value="Y" >是</option>
					<option value="N" {{#if this.model.leaf}}selected="selected"{{/if}}>否</option>
				</select>
			</div>
		</fieldset>
	</form>
</div>