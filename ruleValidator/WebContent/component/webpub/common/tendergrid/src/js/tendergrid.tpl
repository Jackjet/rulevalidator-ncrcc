<div class="subpage_table">
    <table class="tenderTable" border="0" cellpadding="0" cellspacing="0">
        <thead>
            <tr>
            	{{#each this.attrs.columns}}
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
</div>