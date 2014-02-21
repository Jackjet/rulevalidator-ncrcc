<div class="ui-form">
	<form id="{{attrs.id}}" method="post" action="{{attrs.url}}">
		<div class="ui-form-content">
			{{#each attrs.items}}
			<div class="ui-form-content-filed">
				{{#if this.isMultiSelect}}
				<div id="{{this.renderTo}}"></div>
				{{else}}
					{{#if this.isHidden}}
						<input name="{{this.name}}" type="hidden" value="{{this.value}}" />
					{{else}}
						{{#if this.isCheckBox}}
							<input type="checkbox" name="{{this.name}}" class="J-{{this.name}}" style="margin-left: 30px;" value="1"/>
							<label class="ui-form-content-label-checkbox">{{this.label}}</label>
						{{else}}
							<label class="ui-form-content-label">{{this.label}}</label>
							{{#if this.isSelect}}
								<select name="{{this.name}}" class="J-{{this.name}} init">
									{{#if this.hasDefault}}
										{{#if this.emptyText}}
										<option value=''>{{this.emptyText}}</option>
										{{else}}
											<option value=''>全部</option>
										{{/if}}
									{{else}}
										
									{{/if}}
									{{#if this.lazy}}
									{{else}}
										{{#tree this.options}}{{/tree}}
									{{/if}}
								</select>
							{{else}}
								{{#if this.isDatepicker}}
									<input name="{{this.name}}" type="text" class="datepicker J-{{this.name}}" data-datepicker-defaultDate="{{this.value}}" />
									<input name="timezone" type="hidden" value="{{this.timezone}}"/>
								{{else}}
									{{#if this.isDaterange}}
										<input name="timezone" type="hidden" value="{{this.timezone}}"/>
										<input name="{{this.beginName}}" type="text" class="datepicker J-{{this.beginName}}" data-datepicker-defaultDate="{{this.beginValue}}"/>
											到
										<input name="{{this.endName}}" type="text" class="datepicker J-{{this.endName}}" data-datepicker-defaultDate="{{this.endValue}}"/>
									{{else}}
										{{#if this.isRange}}
											<input name="{{this.minName}}" type="text" class="nc-form-textfield-range J-{{this.minName}}" />
											到
											<input name="{{this.maxName}}" type="text" class="nc-form-textfield-range J-{{this.maxName}}" />
										{{else}}
											<input name="{{this.name}}" type="text" value="{{this.value}}" class="nc-form-textfield J-{{this.name}}" />
										{{/if}}
									{{/if}}
								{{/if}}
							{{/if}} 
						{{/if}}
					{{/if}}
				{{/if}}
			</div>
			{{/each}}
		</div>
		<div class="ui-form-button ">
			<div class="button_wrapper">
				{{#each attrs.buttons}} <a href="#"
					class="button_{{this.style}} J_button">{{this.text}}</a>
				{{/each}}
			</div>
		</div>
	</form>
	<div class="clear"></div>
</div>
