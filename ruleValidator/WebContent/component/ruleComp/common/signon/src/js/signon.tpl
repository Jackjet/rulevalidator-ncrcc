<div class="signon_top_wrapper">

	<!-- LOGO -->
	<div class="signon_logo_wrapper">
		<div class="signon_width floatcenter">
			<div class="floatleft signon_width">
				<div class="signon_logo w_100ps"></div>
			</div>
		</div>
	</div>

	<!-- 登录面板 -->
	<div class="signon_doc_wrapper signon_width idSignonPanel">
		<div class="signon_width floatcenter">
			<div class="floatleft signon_width">
				<div class="signon_doc_bg">
					<div class="signon_form_wrapper">

						<div id="signonform">
							<div style="width: 232px;height: 289px;"> </div>
						</div>
						<script type="text/javascript">
							seajs
									.use(
											'signonform',
											function(SignOnForm) {
												var signonform = new SignOnForm(
														{
															renderTo : "signonform"
														});
											});
						</script>
						<!--获取帐号-->
						<div class="account_notice w_100ps">
							<div class="font_yahei J-loginalert">如果您还没有规则执行结果查询账号，您可以通过以下方式获取您的账号</div>
							<div class="font_arial">
								<span class="icon_phone"></span>联系电话: <a class="J-logintel">624-30738</a>
							</div>
							<div class="font_arial">
								<span class="icon_email"></span>E-mail: <a class="J-loginemail"
									href="mailto:webmaster@yonyou.com">webmaster@yonyou.com</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>