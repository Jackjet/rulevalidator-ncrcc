define(function(require, exports, module) {
	var $ = require('$');
	require('../theme/default/css/resource.css');
	var Dialog = require("dialog");
	var Notice = require('./notice.js');
	/**
	 * 新增地址
	 * grid 不为空表示是收货地址维护页面，且dom为空
	 * dom 不为空表示是订单新增页面调用的地址新增， 且grid为空
	 */
	function addMenus(grid,dom) {
		var that = dom;
		//行政区划
		var contentHtml = "<iframe id='addressEdit' data-resize='true' src='"+G.PAGE.MENUS_EDIT+"' frameborder='0' scrolling='no' width='450px' height='200px'></iframe>";
		Dialog.dialog({
			title : '新增用户',
			hasCloseTip : true,
			content : contentHtml,
			isHtmlContent : true,
			pandTop:7,
			type : 'dialog',
			buttons : [ {
				name : '保存',
				isDefault : true,
				href : '#',
				method : function() {
					// [1] 点按钮，提交ajax请求，完成新增;					
					if (validateMenus()) {
						var areaValue="";
						if(that){
							areaValue = $($(top.document).find("#addressEdit").get(0).contentWindow.document).find("#selectedinput").text();
						}
						$.ajax({
							type : "POST",
							url : G.API.MEANUS_INSERT,
							dateType : "json",
							data : $($(top.document).find("#addressEdit").get(0).contentWindow.document).find('#form-address-update').serialize(),
							success : function(data) {
								if (data!=null&&data.message!="excep") {
									Notice.show($('#resource-grid'), '添加成功');
									// [2] 调用grid的查询
									if(grid){
										grid.query('#form');
									}
									if(that){
										var title =data.addressView.addressfullname;
										var areaselect = that.parents(".address_info").find("select[name$='.creceiveareaid']");
										//如果包含收货地区
										if(data.pk_areacl!=null){
											//如果收货地区下拉框中不存在该收货地区，在option中动态添加一条
											if(areaselect.find("option[value='"+data.pk_areacl+"']").length==0){
												areaselect.append("<option value="+data.pk_areacl+">"+areaValue+"</option>");
											}
											areaselect.val(data.pk_areacl);
										}else{
											that.parents(".address_info").find("select[name$='.creceiveareaid']").val("");
										}
										that.prev().val(title);
										that.prev().attr("title",title);
										that.next().val(data.pk_address);
									}
								}else if(data.message=="excep"){
									alert("相同的用户编码已存在！！！");
								}else {
									Notice.show($('#resource-grid'), '添加失败');
								}
							}
						});
						Dialog.close();
					}
				}
			}, {
				name : '取消',
				isDefault : false,
				href : '#',
				method : function() {
					Dialog.close();
					return false;
				}
			} ]
		});
	}
	/**
	 * 修改地址
	 */
	function updateMenus(record, grid) {
		//获取主键
		var pk = record.menuId||"";
		var contentHtml = "<iframe id='addressEdit' data-resize='true' frameborder='0' scrolling='no' src='"+G.PAGE.MENUS_EDIT+"?pkUser="+pk+"' width='450px' height='200px'></iframe>";
		Dialog.dialog({
			title : '修改菜单信息',
			hasCloseTip : true,
			content : contentHtml,
			isHtmlContent : true,
			pandTop:7,
			type : 'dialog',
			buttons : [ {
				name : '保存',
				isDefault : true,
				href : '#',
				method : function() {
					if (validateMenus()) {
						$.ajax({
							type : "POST",
							url : G.API.MEANUS_UPDATE,
							dataType : "json",
							data : $($(top.document).find("#addressEdit").get(0).contentWindow.document).find('#form-address-update').serialize(),
							success : function(data) {
								if (data!=null&&data.message!="excep") {
									Notice.show($('#resource-grid'), '修改成功');
									// [2] 调用grid的查询
									grid.query('#form');
								}else if(data.message=="excep"){
									alert("相同的用户编码已存在！！！");
								}else {
									Notice.show($('#resource-grid'), '修改失败');
								}
							}
						});
						Dialog.close();
					}
				}
			}, {
				name : '取消',
				isDefault : false,
				href : '#',
				method : function() {
					Dialog.close();
					return false;
				}
			} ]
		});
	}
	/**
	 * 删除用户
	 */
	function delMenus(record, grid) {
		// [0] 获取主键
		var dataModel = {};
		dataModel["pk_address"] = record.pk_address;
		// [1] 提交ajax请求，完成删除
		Dialog.dialog({
			title : '删除用户',
			hasCloseTip : true,
			content : "确定要删除吗？",
			isHtmlContent : true,
			type : 'dialog',
			buttons : [ {
				name : '确定',
				isDefault : true,
				href : '#',
				method : function() {
					// [1] 点按钮，提交ajax请求，完成删除;
					$.ajax({
						type : "POST",
						url : G.API.MEANUS_DELETE,
						data :record,
						dateType : "json",
						success : function(data) {
							if (data!=null) {
								Notice.show($('#resource-grid'), '删除成功');
								// [2] 调用grid的查询
								//record.css("display", "none");
								 grid.query('#form');
							} else {
								Notice.show($('#resource-grid'), '删除失败');
							}
						}
					});
					Dialog.close();
				}
			}, {
				name : '取消',
				isDefault : false,
				href : '#',
				method : function() {
					Dialog.close();
					return false;
				}
			} ]
		});
	}
	/**
	 * 用户校验
	 */
	function validateMenus() {
		 var form = $($(top.document).find("#addressEdit").get(0).contentWindow.document);
		 form.find(".error").removeClass("error");
		 var name=form.find("#userCode");
		 var flag = false;
		 var Msg = "";
		 if(name.val()==""){
			 Msg+="账号不能为空！";
			 name.addClass("error").focus();
			 flag = true;
		 }
		 var detailinfo=form.find("#userName");
		 if(detailinfo.val()==""){
			 Msg+="名称不能为空！";
			 detailinfo.addClass("error").focus();
			 flag = true;
		 }
		 if(flag){
			 alert(Msg);
			 return false;
		 }
		return true;
	}
	function parseParam(param,key){
		var paramStr ="";
		if(param instanceof String||param instanceof Number||param instanceof Boolean){
			paramStr +="&"+key+"="+encodeURIComponent(param);
		}
		else{
			$.each(param,function(i){
				var k=key==null?i:key+("."+i);
				paramStr+='&'+parseParam(this,k);
			});
		}
		return paramStr.substring(1);
	}
	exports.addMenus = addMenus;
	exports.delMenus = delMenus;
	exports.updateMenus = updateMenus;
});