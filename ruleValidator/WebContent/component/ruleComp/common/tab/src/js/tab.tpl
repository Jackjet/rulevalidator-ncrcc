<div class="tab_labels">
	
	{{#each attrs.items}}
		<div class="label_wapper {{#if this.disabled}}disabled{{/if}}">
			{{#if this.isShowNum}}
				<span class="label_amount_tabgreen label_regular label_amount_mrg_70">
					<span class="left"></span> 
					<span class="number font_arial">{{this.number}}</span>
					<span class="right"></span>
				</span>
			{{/if}}
			<a href="#{{this.id}}"
			{{#if this.active}}
				 class="label_active" style="z-index: 1"
			{{else}}
				 class="label_normal"
			{{/if}}
			>
					<span class="label_text" title="{{this.title}}">{{this.title}}</span>
				</a>
			<div class="pannel_edit"><span class="pannel_left">左移</span><span class="pannel_center">{{#if this.disabled}}显示{{else}}隐藏{{/if}}</span><span class="pannel_right">右移</span></div>
		</div>
	{{/each}}
</div>
<div class="labels_content">
{{#each attrs.items}}
	{{#if this.active}}
		<div id="{{this.id}}" class="content"></div>
	{{else}}
		<div id="{{this.id}}" class="content hidden"></div>
	{{/if}}
	
{{/each}}
</div>