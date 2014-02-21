<div id="posdialog" class="posdialog" >
	<div class="panel_top">
		<div class="panel_title">{{attrs.title}}</div>
		<a id="posdialogclose" class="panel_closer" href="javascript:void(0)"></a>
	</div>
	<div class="panel_content">
		<div class="content J-content" id="tableCartList">
			
		</div>
	</div>
	<div class="panel_btm">
		<div class="btns">
			{{#each attrs.buttons}}
				<a class="btn_normal_{{this.style}} " href="javascript:void(0)">{{this.text}}</a> 
			{{/each}}
		</div>
	</div>
	<div style="clear:both">
</div>