{{#with model }}
<div class="inside_width idAddressBar rounded_extended_bar J-box">
	<div class="bar_corner lt"></div>
	<div class="bar_border inside_width top"></div>
	<div class="bar_corner rt"></div>
	<div class="bar_content inside_width">
		<!-- head -->
		<div class="info_line">
			<a href="#" class="trigger up"></a>
			<div class="content_name font_bold">
				<span>{{title}}</span>
			</div>
			<div class="content_info hidden"></div>
			<a href="#" class="icon_delete" title="删除"></a>
		</div>
		<!-- body -->
		<div class="ui-box-body w_100ps">{{{body}}}</div>
	</div>
	<div class="bar_corner lb"></div>
	<div class="bar_border inside_width btm"></div>
	<div class="bar_corner rb"></div>
</div>
{{/with}}
