<!-- 登录form -->
<div class="signon_form">
	<!-- 登录错误信息区域 -->
	<div class="signon_error_notice">
		<span class="icon hidden"></span> 
		<span id="signonError" class="hidden"></span>
	</div>

	<!-- 用户名 -->
	<label class="signon_input"> 
		<input type="text" id="username" class="username" title="请输入用户名" /> 
	</label>
	<!-- 密码-->
	<label class="signon_input"> 
		<input type="password" id="password" class="password" title="请输入密码" /> 
		<input type="password" id="password2" style="display: none;" /> 
	</label>
	<!-- 校验码 -->
	<label class="signon_validate">
		<input type="text" maxlength="4" id="validate" class="validate" title="请输入验证码" /> 
		<a href="#"> 
			<img src="" id="codefont" width="68" height="27" alt="" /> 
		</a> 
	</label>

	<!-- cookie设置和密码找回 -->
	<div>
		<input type="checkbox" id="rememberpwd" class="floatleft" />
		<label for="rememberpwd" class="signon_remember_pwd">记住密码</label> 
		<a href="#" class="signon_forget_pwd">忘记密码</a>
		<input type="checkbox" id="login_type"  class="hidden" />
	</div>

	<!-- 登录按钮 -->
	<div class="signon_button_wrapper">
		<a href="#" class="signon_button font_yahei">登 录</a> 
		<span class="signon_btn_animated" style="display: none;">登录中</span> 
		<span class="btn_inverse"></span>
	</div>
	<div style="clear: both;"></div>
</div>