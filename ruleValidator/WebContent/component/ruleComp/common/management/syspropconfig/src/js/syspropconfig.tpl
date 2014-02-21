<div class="row">
	<div class="col-md-12">
				<h3>参数配置-系统参数配置</h3>
				<hr>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<div class="btn-group pull-right">
			<button type="button" class="btn btn-info" id="save">
				<span class="glyphicon glyphicon-floppy-disk"></span><span> 保存</span>
			</button>
			<button type="button" class="btn btn-default" id="cancel">
				<span class="glyphicon glyphicon-remove"></span><span> 取消</span>
			</button>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>编号</td>
					<th width="23%">参数描述</th>
					<th>key</td>
					<th>value</td>
				</tr>
			</thead>
			<tbody>
			{{#foreach model}}
			{{/foreach}}	
			</tbody>
		</table>
	</div>
</div>