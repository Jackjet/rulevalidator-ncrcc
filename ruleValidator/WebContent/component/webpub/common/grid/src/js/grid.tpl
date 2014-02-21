<table class="infolist ecp_grid_table"  cellspacing="0" cellpadding="0" >
	<thead>
		<tr class="row_bg">
			{{#if attrs.ismulti}}
				<td style="width:25px;"><input type="checkbox" class="J_grid_selected_all"/>选择</td>
			{{/if}}
			{{#if attrs.showRowNum}}
				<td>序号</td>
			{{/if}}
			{{#if attrs.issingle}}
				<td>选择</td>
			{{/if}}
			
			{{#each attrs.columns}}
			<td style="text-align:{{this.align}};">{{this.label}}</td> 
			{{/each}}
		</tr>
	</thead>
	<tbody>
		{{#grid this}} 
			{{this}}
		{{/grid}}
	</tbody>
</table>





