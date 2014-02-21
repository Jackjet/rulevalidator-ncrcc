				<div class="panel-group" id="accordion">
					<iframe id="uploadiframe" name="uploadiframe" class="uploadiframe"></iframe>
					<div class="panel panel-default hide" id="company">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a  class="accordion-toggle" data-toggle="collapse"
									data-parent="#accordion" href="#pagechange">企业信息设置</a>
							</h4>
						</div>
						<div id="pagechange" class="panel-collapse collapse">
							<div id="collapseTwo">
								<div class="panel-body">
									<label class="label-title">门户首页</label><button class="pull-right btn btn-default btn-xs btn-info J-btn-add" title="展开"><span class="glyphicon glyphicon-plus"></span></button>
									<div class="contentdiv">
									<label>LOGO图片：</label>
									<div class="form-group">
										 <form id="uploadform" action="{{G.PAGE.MANAGEMENT_LOGOUPLOAD}}" method="post"  enctype="multipart/form-data" target="uploadiframe">
										 	<div class="input-group">
										 	<input type="file" name="changed_Logo" class="form-control" id="file" accept=".png,.jpg,.bmp,.gif" data-toggle="tooltip" data-placement="top" title="(推荐图片大小300*64)">
	     									<span class="input-group-btn">
	       									<input class="btn btn-info" type="submit" id="logoSubmit" value="上传">
	       									</input>
	       									<input class="btn btn-default" type="button" id="logoDefault" value="默认"></input>
	      									</span>
	      									</div>
	  									 </form>
									</div>
									<form class="form" role="form">
										<div class="form-group">
											<label>头部信息：</label> <input type="text" class="form-control"
												id="topInfo" title="产品热线：800-400-5662">
										</div>
										<div class="form-group">
											<label>底部信息：</label> <input type="text" class="form-control"
												id="footInfo"
												title="版权所有：用友软件股份有限公司 Yonyou 2012 服务电话：+800-400-5662">
										</div>
									</form>
									</div>
								</div>
								<div class="panel-body">
									<label class="label-title">登陆页面</label><button class="pull-right btn btn-default btn-xs btn-info J-btn-add" title="展开"><span class="glyphicon glyphicon-plus"></span></button>
									<div class="contentdiv">
									<label>LOGO图片：</label>
									<div class="form-group">
										 <form id="uploadform" action="{{G.PAGE.MANAGEMENT_LOGINLOGOUPLOAD}}" method="post"  enctype="multipart/form-data" target="uploadiframe">
										 	<div class="input-group">
										 	<input type="file" name="login_Logo" class="form-control" id="loignLogofile" accept=".png,.jpg,.bmp,.gif" data-toggle="tooltip" data-placement="top" title="(推荐图片大小300*64)">
	     									<span class="input-group-btn">
	       									<input class="btn btn-info" type="submit" id="loginLogoSubmit" value="上传"></input>
	       									<input class="btn btn-default" type="button" id="loginLogoDefault" value="默认"></input>
	      									</span>
	      									</div>
	  									 </form>
									</div>
									<label>左侧图片：</label>
									<div class="form-group">
										 <form id="uploadform" action="{{G.PAGE.MANAGEMENT_LOGINLEFTUPLOAD}}" method="post"  enctype="multipart/form-data" target="uploadiframe">
										 	<div class="input-group">
										 	<input type="file" name="login_Left" class="form-control" id="loginLeftPicfile" accept=".png,.jpg,.bmp,.gif" data-toggle="tooltip" data-placement="top" title="(推荐图片大小496*427)">
	     									<span class="input-group-btn">
	       									<input class="btn btn-info" type="submit" id="loginLeftPicSubmit" value="上传">
	       									</input>
	       									<input class="btn btn-default" type="button" id="loginLeftPicDefault" value="默认"></input>
	      									</span>
	      									</div>
	  									 </form>
									</div>
									<label>背景图片：</label>
									<div class="form-group">
										 <form id="uploadform" action="{{G.PAGE.MANAGEMENT_LOGINBGUPLOAD}}" method="post"  enctype="multipart/form-data" target="uploadiframe">
										 	<div class="input-group">
										 	<input type="file" name="login_BG" class="form-control" id="loginBGfile" accept=".png,.jpg,.bmp,.gif" data-toggle="tooltip" data-placement="top" title="(推荐图片大小764*507)">
	     									<span class="input-group-btn">
	       									<input class="btn btn-info" type="submit" id="loginBGSubmit" value="上传">
	       									</input>
	       									<input class="btn btn-default" type="button" id="loginBGDefault" value="默认"></input>
	      									</span>
	      									</div>
	  									 </form>
									</div>
									<form class="form" role="form">
										<div class="form-group">
											<label>提示信息：</label> <input type="text" class="form-control"
												id="loginAlert" title="产品热线：800-400-5662">
										</div>
										<div class="form-group">
											<label>联系电话：</label> <input type="text" class="form-control"
												id="loginTel"
												title="011-62435701">
										</div>
										<div class="form-group">
											<label>Email：</label> <input type="text" class="form-control"
												id="loginEmail"
												title="webmaster@yonyou.com">
										</div>
									</form>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="panel panel-default hide" id="indexwidget">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a  class="accordion-toggle J-panelClick" data-toggle="collapse"
									data-parent="#accordion" href="#widgetchange">首页组件设置</a>
							</h4>
						</div>
						<div id="widgetchange" class="panel-collapse collapse">
							<div id="collapsePanel">
								<div class="panel-body layout-setting">
									<label class="label-title">布局选择</label><button class="pull-right btn btn-default btn-xs btn-info J-btn-add" title="展开"><span class="glyphicon glyphicon-plus"></span></button>
									<div class="contentdiv">
									<ul>
									<li><a id='layout-aaa'><strong>ABA</strong></a></li>
									<li><a id='layout-aa'><strong>AA</strong></a></li>
									<li><a id='layout-ba'><strong>BA</strong></a></li>
									<li><a id='layout-ab'><strong>AB</strong></a></li>
									</ul>
									</div>
								</div>
							</div>
							
							<div id="collapsePanleEdit">
								<div class="panel-body">
									<label class="label-title">组件添加</label><button class="pull-right btn btn-default btn-xs btn-info" id="btnFresh" title="刷新"><span class="glyphicon glyphicon-refresh"></span></button>
								</div>
							</div>
						</div>
					</div>
					<div class="panel panel-default hide" id="widgetstyle">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a  class="accordion-toggle" data-toggle="collapse"
									data-parent="#accordion" href="#widgetstyles">组件样式设置</a>
							</h4>
						</div>	
						<div id="widgetstyles" class="panel-collapse collapse">
							<div class="panel-body">
								<a id="productstyle" class="secondNavigation">商品描述设置</a>	
								<a id="productliststyle" class="secondNavigation">商品列表设置</a>								
							</div>
						</div>
					</div>		
					<div class="panel panel-default hide" id="theme">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a  class="accordion-toggle" data-toggle="collapse"
									data-parent="#accordion" href="#themechange">主题设置</a>
							</h4>
						</div>
						<div id="themechange" class="panel-collapse collapse">
							<div id="themecolor">
								<div class="panel-body">
									<label style="display:block">主题颜色：</label>
									{{#each attrs.systhemedropdown}}
									<button class="btn {{this.css}} pull-left ui-leftnav-colorful" value="{{this.id}}">{{this.name}}</button>
									</li>
									{{/each}}
								</div>
							</div>
						</div>
					</div>			
				</div>