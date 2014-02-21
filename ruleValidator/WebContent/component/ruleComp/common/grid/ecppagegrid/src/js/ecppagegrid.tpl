<div style="padding: 10px 0 0 0;">
	<table class="ecp_grid_table ui-ecpgrid" cellspacing="0"
		cellpadding="0">
		<thead>
			<tr>
				{{#if attrs.ismulti}}
				<td>
					<input type="checkbox" class="J_grid_selected_all">
				</td>
				{{else}}
				{{#if attrs.issingle}}
				<td>
					<span>选择</span>
				</td>
				{{/if}} 
				{{/if}} 
				{{#if attrs.showRowNum}}
				<td class="ui-ecpgrid-index">序号</td>
				{{/if}} 
				{{#each attrs.columns}}
				<td style="width: {{this.width}}">{{this.label}}</td>
				{{/each}}
			</tr>
		</thead>
		<tbody>{{#grid this}} {{this}} {{/grid}}
		</tbody>
	</table>
	<div class="J-ecpgrid-status ui-ecpgrid-status"></div>
	<div class="clear"></div>
	<div class="J-pagination ui-pagination-bar">
		<div class="J-pagesize hidden float_left"></div>
		<div class="J-pagelink pagination float_right"></div>
	</div>
</div>