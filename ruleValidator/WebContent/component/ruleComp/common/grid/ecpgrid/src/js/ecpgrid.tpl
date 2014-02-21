<div style="padding: 10px 0 0 0;">
	<table class="ecp_grid_table ui-ecpgrid" cellspacing="0" cellpadding="0">
		<thead>
			<tr>
				{{#if attrs.ismulti}}
				<td>
					<input type="checkbox" class="J_grid_selected_all">
				</td>
				{{/if}} 
				{{#if attrs.issingle}}
					<td>选择</td>
				{{/if}}
				{{#if attrs.showRowNum}}
				<td class="ui-ecpgrid-index">序号</td>
				{{/if}} 
				{{#each attrs.columns}}
				<td>{{this.label}}</td>
				{{/each}}
			</tr>
		</thead>
		<tbody>{{#grid this}} {{this}} {{/grid}}
		</tbody>
	</table>
	<div class="J-ecpgrid-status ui-ecpgrid-status"></div>
</div>





