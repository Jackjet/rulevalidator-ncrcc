define(function(require, exports, module) {
	var $ = require("$");
	var form_tpl = require("./form.tpl");
	var Widget = require("widget");
	var Log = require("log");
	require("../theme/default/css/form.css");
	var handlebars = require("handlebars");
	var moment = require("moment");
	handlebars.registerHelper("tree", function(context, options) {
				var ret = "";
				var items = context;
				var generateOption = function(item, level) {
					var prefix = "";
					for (var i = 1; i < level; i++) {
						prefix += "&nbsp;";
					}
					var option = "<option id='"+item.code+"' value='" + item.code + "'>" + prefix + item.name + "</option>";
					if(item.children){
						var child_length = item.children.length;
						if (child_length > 0) {
							for (var i = 0; i < child_length; i++) {
								option += generateOption(item.children[i], level
												+ 1);
							}
						}
					}
					return option;
				}
				if(items){
					for (var i = 0; i < items.length; i++) {
						ret += generateOption(items[i], 1);
					}
				}
				return ret;
			});
	var Form = Widget.extend({
		handlebars : handlebars,
		// 初始化参数
		initCustAttr : function() {
			
			var attrs = this.get("attrs");
			
			var formid = attrs.id;
			if(formid){
				if($("#"+formid).length>0){
					if(console){ 
						console.error("指定的form组件的id["+formid+"]已经被使用,请检查！");
					};
				}
			}else{
				if(console){ console.log("请指定form组件的id");};
			}
			
			// 输入项初始化
			var items = this.get("items");
			for (var i = 0; i < items.length; i++) {
				var item = items[i];
				var type = item.type;
				item.options = item.options || [];
				if (item.lazy == undefined) {
					item.lazy = false;
				}
				
				switch (type) {
					case "select" :
						item.isSelect = true;
						//是否有默认选项<请选择>
						item.hasDefault = (item.hasDefault==undefined)?true:item.hasDefault;
						if (item.value && typeof(item.value) == 'object' && item.value.url && item.value.format) {
							// 调用者注入了ajax行为
							item.lazy = false;
							var url = item.value.url;
							var format = item.value.format;
							
							// 使用ajax请求获取数据并格式化
							$.ajax({
								url :url,
								async : false,
								success : function(data) {
									item.options = item.value.format(data);
								}
							});
							
						} else if(item.value){
								item.lazy = false;
							}
						// 查询数据
						//第一个判断条件是为了在ie下正常，因为当options为对象而不是字符串时，在ie下调用indexOf会报错
						if (typeof(item.options)=="string" && item.options.indexOf("CODENAME_") == 0) {
							if (item.lazy == false) {
								var url=G.API.CODENAME;
								var param = "";
								if(item.alias){
									param = $(".J-"+item.alias).val() || "";
								}else{
									if(item.filter){
										param = item.filter;
									}
								}
								$.ajax({
									url : url,
									async : false,
									data:{
										"key":item.options,
										"param":param
									},
									success : function(data) {
										if (!data.message) {
											item.options = data.items;
										} else {
											Log.error(data.message);
										}
									}
								});
							} else {
								(function(item_out) {
									// 保存item 防止item随着for循环而变化
									var item = item_out;
									$(".J-" + item.name).live("mouseover",
											function(event) {
												if ($(this).hasClass("init")) {
													var url=G.API.CODENAME;
													var param = "";
													if(item.alias){
														param = item.type=="select" ? $(".J-"+item.alias).find("option:selected").val() : $(".J-"+item.alias).val();
													}else{
														if(item.filter){
															param = item.filter;
														}
													}
													$.ajax({
														url : url,
														data:{
															"key":item.options,
															"param":param
														},
														success : function(data) {
															if (!data.message) {
																var html = "";
																var items = data.items;
																var template = handlebars
																		.compile("{{#tree this}}{{/tree}}");
																var html = template(items);
																$(".J-"+ item.name).append(html).removeClass("init");
															} else {
																Log.error(data.message);
															}
														}
													});
												}
											});
								})(item);
							}
						}

						// 事件绑定
						if (item.onFocus) {
							$(".J-" + item.name).live("focus", item.onFocus);
						}
						if (item.onChange) {
							$(".J-" + item.name).live("change", item.onChange);
						}

						break;
					case "hidden" :
						item.isHidden = true;
						break;
					case "datepicker" :
						item.isDatepicker = true;
						item.timezone = moment().zone()/60*-1;
						break;
					case "daterange" :
						item.isDaterange = true;
						item.beginValue = item.beginValue==undefined ? -7 : item.beginValue;
						item.endValue = item.endValue==undefined ? 0 : item.endValue;
						item.timezone = moment().zone()/60*-1;
						break;
					case "range" :
						item.isRange = true;
						break;
					case "multiselect" :
						item.isMultiSelect = true;
						break;
					case "checkbox" :
						item.isCheckBox = true;
						break;
					default :
						item.isText = true;
				}
			}
			// 按钮默认值初始化
			var buttons = this.get("buttons");
			for (var i = 0; i < buttons.length; i++) {
				var button = buttons[i];
				buttons[i] = {
					text : button.text || "按钮",
					id:button.id||"form_button_"+i,
					handler : button.handler,
					style : button.style || "normal",
					type:button.type
				};
			}
		},
		template : form_tpl,
		setup:function() {
			
		},
		//校验
		validate:function(){
			var that = this;
			var items = this.get("items");
			for(var i=0;i<items.length;i++){
				var item = items[i];
				if(item.required){
					if (item.type == "daterange") {
						var beginName = item.beginName;
						var beginDate = that.element.find("[name="+beginName+"]").val();
						var endName = item.endName;
						var endDate = that.element.find("[name="+endName+"]").val();

						if (beginDate == "" || endDate == "") {
							alert("开始和结束" + item.label + "必填！");
							return false;
						}
						if (beginDate > endDate) {
							alert('结束日期必须大于或者等于开始日期');
							return false;
						}
					} else {
						var name = item.name;
						// 常规字符型输入框
						if (name) {
							var value = that.element.find("[name="+name+"]").val();
							if(value ==""){
								alert(item.label+"必填！");
								return false;
							}
						}
					}
				}
			}
			return true;
		},
		// 按钮事件绑定
		bindEvent : function() {
			var that = this;
			var buttons = this.get("buttons");
			this.element.find("a.J_button").each(function(index) {
				if(buttons[index].type=="reset"){
					buttons[index].handler= function() {
						that.element.find(':input').not(':button, :submit, :reset, :hidden').val('').removeAttr('checked').removeAttr('selected');
						that.initDefaultValue();
						return false;
					};
				}
				$(this).bind("click", function(){
					if(that.validate()){
						buttons[index].handler();
						return false;
					}
				});
				$(this).attr("id",buttons[index].id);
			});
			
			
			// 多选
			var items = this.prop.attrs.items;
			$(items).each(function(i, item) {
				if (item.type == 'multiselect') {
					var attrs = item;
					seajs.use(['multiselect'], function(MultiSelect){
						new MultiSelect({
							renderTo:item.renderTo,
							attrs:attrs
						});
					});
				}
			});
		},
		afterRender:function(){
			this.initDefaultValue();
		},
		//init DefaultValue
		initDefaultValue:function(){
			require("jquery.ui.datepicker")($);
			//给开始日期和结束日期设置值，用于多时区
			//获取当前日期框里的值
			
			//设置开始日期为当前值的00:00:00
			//设置结束日期为当前日期的23:59:59
			//格式化为东八区时间。
			
			var items = this.get("items");
			for(var i=0;i<items.length;i++){
				var item = items[i];
				var type = item.type;
				var name = item.name;
				var value= item.value;
				switch (type) {
					case "select" :
						$("select[name="+name+"]").val(value);
					break;
					default :
				}
			}
		},
		reset:function(){
			this.change();
			this.bindEvent();
		}
	});
	module.exports = Form;
});