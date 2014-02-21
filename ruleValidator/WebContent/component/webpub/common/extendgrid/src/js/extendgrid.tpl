<div style="padding: 10px 0 0 0;">
	<table class="ui-extendgrid"  cellspacing="0" cellpadding="0" >
		<thead>
			<tr class="row_bg">
				{{#each attrs.columns}}
				{{#if this.isHidden}}
					<td class="hidden">{{this.label}}</td> 
				{{else}}
					<td>{{this.label}}</td> 
				{{/if}}
				{{/each}}
			</tr>
		</thead>
		<tbody>
			{{#extendgrid this}}
				{{this}}
			{{/extendgrid}}
		</tbody>
	</table>
	<div class="J-extend-status ui-status"></div>
</div>




