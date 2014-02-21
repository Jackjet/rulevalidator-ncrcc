<div class="top_banner w_100ps">
	<!-- logo点击区域 -->
	<div class="J-logo banner_logo"></div>
	<div class="banner_right">
		<!-- banner -->
		<div class="banner_top">
			<div class="hotline">
				<span id="topInfo" class="floatleft font_yahei">7*24小时服务热线：</span> <span
					id="topInfoContent" class="floatleft font_arial">624-30738</span> <span
					class="floatleft font_yahei onlineservice J-onlineService"><span
					class="onlineservice_logo"></span><a
					href="javascript:hj5107.openChat();">在线客服</a>
				</span>
			</div>

			<div class="account_func">
				<div>
					<span title="用户信息" class="account_icon username"></span> <span
						class="floatleft mrg_l5px">您好， <a href="{{G.PAGE.ACCOUNT}}"
						id="userName">{{attrs.user_name}}</a> </span>
				</div>
				<!-- <div class="separator management">
					<span title="后台管理" class="favorite_icon"></span> <span
						class="floatleft mrg_l5px"> <a
						href="{{G.PAGE.MANAGMENT_PAGE}}">后台管理</a> </span>
				</div> -->
				<div class="separator">
					<span title="注销" class="account_icon signout"></span> <span
						class="floatleft mrg_l5px">[<a href="#" class="J_logout">注销</a>]</span>
				</div>
				<div class="separator">
					<span title="帮助" class="account_icon icon_help"></span> <span
						class="floatleft mrg_l5px"> <a href="{{G.PAGE.HELP}}"
						title="帮助" target="_blank">帮助</a> </span>
				</div>
			</div>
		</div>
		{{#if attrs.hideNavigation}} {{else}}
		<!-- 顶级导航 -->
		<div id="nav"></div>
		<div id="icc"></div>
		<script type="text/javascript">
			seajs.use('$', function($) {
				$(function() {
					$('.J-logo').bind('click', function() {
						document.location = G.ecp;
					});
				});
			});
		</script>
		{{/if}}
	</div>
	<div class="clear"></div>
</div>