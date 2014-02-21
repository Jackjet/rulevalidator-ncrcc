<div class="ui-deligrid">
	<div class="left-top">
		<table>
		<tbody>
			<tr>
				<td class="baseinfo">
					<span class="baseinfo-text">基本信息</span>
					<span class="J-add-prod autocomplete hidden"><input class="J-add"></span>
					<span class="J-add-other hidden"><a href="#" class="J-add add-icon"></a></span>
				</td>
				<td class="series">栏目</td>
			</tr>
		</tbody>
		</table>
	</div>
	<div class="right-top width_70">
		<table>
			<thead>
			<tr class="right-top-1st">
				{{#each this.model.right.head}}
				<td class="{{this.key}} area" colspan="{{this.value.length}}">{{this.label}}</td>
				{{/each}}
			</tr>
			<tr class="right-top-2tr">
			{{#each this.model.right.head}}
				{{#each this.value}}
					<td class="{{../this.key}}_second area"><div style="min-width: 100px;">{{this}}</div></td>
				{{/each}}
			{{/each}}
			</tr>
			</thead>
		</table>
	</div>
	<div class="left-content">
		<table>
			<tbody>
				{{#left_content this}}
				{{/left_content}}
			</tbody>
		</table>
	</div>
	<div class="right-content width_70">	
		<table>
			<tbody>
				{{#right_content this}}
				{{/right_content}}
			</tbody>
		</table>
	</div>
	<div class="J-delrows"></div>
</div>
