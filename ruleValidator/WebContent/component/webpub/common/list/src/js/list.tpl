   <ul class="friends_list">
		{{#each model}}
		<li><a target="_blank" href="{{this.url}}">{{this.name}}</a></li>
		 {{/each}}
   </ul>