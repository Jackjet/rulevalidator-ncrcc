<div class="floatcenter post_artical_width">
	<div class="post_artical_title font_yahei">
		<!-- 标题 -->
		{{model.vtitle}}
	</div>
	<div class="post_artical_subtitle">
		<!-- 分类 -->
		<span> 公告分类：{{model.cbulletintypeid_nam}} </span>
		&nbsp;&nbsp;
		<!-- 生效日期 -->
		<span> 生效日期：{{model._teffectivedate}}</span>
		<a onclick="javascript:window.print();" href="#" class="print operation bd_rit">
			<span class="icon_print">&nbsp;</span>打印
		</a> 
	</div>

	<div class="post_artical_content">
		<!-- 内容 -->
		{{html model.vcontent}}
		<br></br>
		<!-- 附件 -->
		{{#each model.attchmentList}}
		<div class="post_artical_download">
			<span>&nbsp;</span>
			{{#if this.isUrl}}
			<a class="" href="{{this.path }}" >{{this.fileName}}</a>
			{{else}}
			<a class="J-bulletin-download" href="#" path="{{this.path }}"
				fileName="{{this.fileName }}">{{this.fileName}}</a>
			{{/if}}
		</div>
		{{/each}}
	</div>
</div>