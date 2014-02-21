<!-- 多选控件 -->
<!-- <div class="ui-ecp-multiselect" style="width:400px;"> -->
<div class="ui-ecp-multiselect" style="width:{{attrs.width}}px;">
	<!-- 域标签 -->
	<label class="label" style="width:{{attrs.labelWidth}}px; text-align: {{attrs.labelAlign}};">标签：</label>
	<!-- 域输入区 -->
	<div class="ms-field" style="width:{{attrs.fieldWidth}}px;">
		<!-- 选择区 -->
		<div class="selected-options" style="width:{{attrs.fieldWidth}}px;">
			<input type="hidden" value="" name="{{attrs.name}}">
			<div class="selected-options-holder" style="width:{{attrs.optionsHolderWidth}}px;">
				<!-- <span class="selected-option" data-attrs-key="a">语文111<a href="#" class="remove" data-attrs-key="a">x</a></span> -->
			</div>
			<!-- 下拉按钮 -->
			<a href="#" class="trigger down"></a>
		</div>
		<!-- 候选区 -->
		<div class="ms-options" style="width:{{attrs.fieldWidth}}px;">
			<ul>
			{{#each model}}
				<li><a href="#"  data-attrs-key="{{this.key}}">{{this.value}}</a></li>
			{{/each}}
			</ul>
			<span class="no-option">没有可供选择的选项了</span>
		</div>
	</div>
</div>