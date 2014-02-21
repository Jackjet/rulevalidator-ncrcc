<div class="ui-ecpeditgrid">
	<table>
		<thead>
			<tr>
				<td class="hidden">序号</td>
				{{#each this.attrs.columns}}
					{{#if this.isHidden}}
					{{else}}
						<td>{{this.label}}</td>
					{{/if}}
				{{/each}}
				{{#if this.attrs.isEdit}}
					<td>操作</td>
					<td>操作</td>
				{{/if}}
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
	
	<div class="ui-form">
		<div class="button_filed ">
			<div class="button_wrapper">
				{{#if this.attrs.isEdit}}
					<a href="#" class="button_main J-add">新增</a>
					{{#each this.attrs.buttons}}
						<a href="#" class="button_main J-{{this.key}}">{{this.name}}</a>
					{{/each}}
				{{/if}}
			</div>
		</div>
	</div>
</div>


