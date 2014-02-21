<div>
	<table class="ui-ecpgrid"  cellspacing="0" cellpadding="0" >
		<thead>
			<tr>
				{{#each attrs.columns}}
				<td>{{this.label}}</td>
				{{/each}}
			</tr>
		</thead>
		<tbody>
			{{#grid this}} 
				{{this}}
			{{/grid}}
		</tbody>
	</table>
	<div class="J-pagination pagination" style="float: right;"></div>
</div>





