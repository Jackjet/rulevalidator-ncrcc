define(function(require, exports, module) {
	require("../theme/default/css/ecpeditgrid.css");
	require("../theme/default/css/form.css");
	require("button");
	require("api");
	
	var $ = require("$");
	var Backbone = require("backbone");
	var Handlebars = require("handlebars");
	var app_tpl = require("./ecpeditgrid.tpl");
	var row_tpl = require("./ecpeditgrid_row.tpl");
	$(function() {
		Backbone.emulateHTTP = true;
		Backbone.emulateJSON = true;
		// 判断是否是编辑状态
		var isEdit = false;
		// 记录当前行编辑前的发票抬头
		var oldValue = "";

		var InvoiceTitle = Backbone.Model.extend({
					idAttribute : "cinvoicetitleid",
					validate : function(attrs, options) {
						if (options.index == undefined && options.collection ) {
							// 获得当前编辑的发票抬头列表存在数量
							var existlength = options.collection.where({
								vinvoicetitle : attrs.vinvoicetitle
								}).length;
							// 编辑发票抬头时
							if (isEdit == true) {
								if(oldValue != attrs.vinvoicetitle && existlength > 0) {
									var message = "[" + attrs.vinvoicetitle
									+ "] 已经存在，请添加新的发票抬头！";
									alert(message);
									return message;
								}
							}
							// 新增发票抬头时
							else {
								if(existlength > 0) {
									var message = "[" + attrs.vinvoicetitle
									+ "] 已经存在，请添加新的发票抬头！";
									alert(message);
									return message;
								}
							}
						}
					}
				});
		var InvoiceTitleList = Backbone.Collection.extend({
					model : InvoiceTitle
				});
		var InvoiceTitles = new InvoiceTitleList();
		var ItemView = Backbone.View.extend({
			tagName : "tr",
			template : row_tpl,
			events : {
				"click .toggle" : "toggleDefault",
				"click .modify" : "edit",
				"click .destroy" : "clear",
				"keypress .edit" : "updateOnEnter",
				"click .save":"close"
//				"blur .edit" : "close"
			},
			initialize : function() {
				this.model.bind('change', this.render, this);
				this.model.bind('destroy', this.remove, this);
			},
			render : function() {
				this.$el
						.html(Handlebars.compile(this.template)(this.model.attributes));
				this.input = this.$('.edit');
				this.checkbox = this.$('.toggle')[0];
				return this;
			},
			//设置默认
			toggleDefault : function() {
				this.model.save({
							bisdefault : this.checkbox.checked
						}, {
							url:G.API.ACCOUNT_INVOICE,
							success : function() {
//								alert("设置默认成功");
							},
							error : function(model, resp) {
								alert("设置默认失败！");
							}
						});
			},
			edit : function() {
				this.$el.addClass("editing");
				this.input.focus();
				oldValue = this.input.val();
				return false;
			},
			//修改
			close : function() {
				// 点击保存时设置编辑标志为true
				isEdit = true;
				var that=this;
				var value = this.input.val();
				if (!value){
					this.clear();
				}
				//lixcha：验证之前的发票抬头中有没有与修改后的发票抬头一样的，有则不能修改此发票抬头
				this.model.save(
					{
						vinvoicetitle : value,
						mark:1
					},
					{
						url:G.API.ACCOUNT_INVOICE,
						success : function() {
//							alert("修改成功");
						},
						error : function(model, resp) {
							that.input.val($(that.input).prev().text());
						},
						collection:InvoiceTitles
					});
				that.$el.removeClass("editing");
				// 非编辑时设置编辑标志为false
				isEdit = false;
			},
			updateOnEnter : function(e) {
				if (e.keyCode == 13)
					this.close();
			},
			//删除
			clear : function() {
				if (confirm("确认删除该发票抬头吗?")) {
					this.model.destroy({
								url:G.API.ACCOUNT_INVOICE,
								success : function() {
//									alert("删除成功");
								}
							});
				}
				return false;
			}

		});
		var AppView = Backbone.View.extend({
					el : $("body"),
					template : app_tpl,
					initialize : function() {
						InvoiceTitles.bind('add', this.addOne, this);
						InvoiceTitles.bind('reset', this.addAll, this);
						var that = this;
						$(function() {
									that.loadInvoice();
								});
					},
					events : {
						"click #addInvoice" : "addInvoice",
						"click #account_updateInvoiceTitle" : "loadInvoice"
					},
					addAll : function() {
						InvoiceTitles.each(this.addOne);
					},
					loadInvoice : function() {
						this.$("#center_content").html(this.template);
						InvoiceTitles.fetch({
									url:G.API.ACCOUNT_INVOICE,
									success : function(model, resp) {

									},
									error : function(model, resp) {
										
									}
								});
						return false;
					},
					// 创建新发票抬头
					addInvoice : function(e) {
						var input = $("#add_invoice");
						if (!input.val()) {
							alert("请输入发票抬头名称");
							return false;
						}
						if(input.val().length>100){
							alert("输入的发票抬头字符长度不能大于100");
							return false;
						}
						// 新增时设置编辑标志为false
						isEdit = false;
						var resp;
						InvoiceTitles.create({
									vinvoicetitle : input.val(),
									mark:0
								}, {
									url:G.API.ACCOUNT_INVOICE,
									success : function(model, resp) {
										alert("新增成功");
									}
								});
						return false;
					},
					addOne : function(todo) {
						var view = new ItemView({
									model : todo
								});
						$("#center_content tbody").append(view.render().el);
					}
				});
		new AppView();
	});
});