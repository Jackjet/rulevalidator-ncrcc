<div class="channel_box">
	<div class="channel_title">
		{{#each this.triggers}}
		<a href="#" class="title_label">{{this}}</a>
		{{/each}}
		<a href="{{this.moreurl}}" class="title_more">更多</a>
	</div>
	<div class="containers">
		{{#each this.contents}}
		<div class="container J-content">{{this}}</div>
		{{/each}}
	</div>
</div>