<div class="product_picture">

	<div class="picture_box">
		{{#each model}}
			<div class="picture">
				<img  alt="" src="/{{this.350_350}}">
			</div>
		{{/each}}
	</div>
	<div class="picture_thumbs">
		{{#each model}}
			 <div class="thumb_item_out">
			 	<div class="thumb_item_in">
					<img alt="" src="/{{this.50_50}}">
				</div>
			 </div>
		{{/each}}
	</div>
</div>