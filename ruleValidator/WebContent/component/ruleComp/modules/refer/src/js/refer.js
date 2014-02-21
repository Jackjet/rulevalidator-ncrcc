define(function(require, exports, module) {
	var $ = require('$');
	require('../theme/default/css/refer.css');
	$ = require('jquery.ztree')($);
	var AdaptDialog = require("adaptdialog");
	var handlebars = require('handlebars');
	var template = require('./refer.tpl');
	var Tab = require("tab");
	var Form = require("form");
	var EcpPageGrid = require('ecppagegrid');
	var EcpGrid= require('ecpgrid');
	var Widget = require("widget");
	var productPrice = require("productPrice");

	var Refer = Widget.extend({
		components:{},
		_createTab : function(options) {
			var items = options.items;
			$(".ui-refer").append($("<div></div>").attr("id", "tab").addClass("ui-refer-tab"));
			tab = new Tab({
				attrs : {
					items : items
				},
				renderTo : "tab"
			});
			this._createComponent(options);
		},
		_createForm : function(option) {
			var form = new Form(option.config);
			this.components[option.id] = form;
		},
		_createEcpPageGrid : function(option) {
			option.config.afterRender = function() {
				EcpPageGrid.superclass.afterRender.apply(this, arguments);
				productPrice.init();
			};
			var grid = new EcpPageGrid(option.config);
			this.components[option.id] = grid;
		},
		_createEcpGrid : function(option) {
			var grid = new EcpGrid(option.config);
			this.components[option.id] = grid;
		},
		_createTree : function(option) {
			var that = this;
			var setting = {
				treeNode:{
					noCheck:false
				},
				callback : {
					onClick : option.config.onClick || function() {}
				}
			};
			$.ajax({
				type : 'GET',
				url : option.config.url,
				dateType : 'json',
				success : function(data) {
					var container = $('.ui-refer-tree');
					container.addClass('ztree');
					var tree = $.fn.zTree.init(container, setting, that._format(data, that._parser));
					that.components[option.id] = tree;
				}
			});
		},
		_createComponent : function(options) {
			var items = options.items;
			for ( var i = 0; i < items.length; i++) {
				var configs = items[i].configs;
				for ( var j = 0; j < configs.length; j++) {
					var renderTo = "";
					var element = $("<div></div>").css("width", (configs[j].width || "96%")).addClass("ui-refer-" + configs[j].type).attr("id",
							"ui-refer-" + configs[j].type).attr("componentid",configs[j].id).attr("componenttype",configs[j].type);
					if (options.isTab) {
						renderTo = "tab" + (i + 1) + " .";
						$("#tab" + (i + 1)).append(element);
					} else {
						$(".ui-refer").append(element);
					}
					renderTo += "ui-refer-" + configs[j].type;
					$.extend(true, configs[j].config, {renderTo : renderTo});
					if (configs[j].type == "form") {
						this._createForm(configs[j]);
					} else if (configs[j].type == "ecppagegrid") {
						configs[j].config.attrs.ismulti=options.isMul;
						this._createEcpPageGrid(configs[j]);
					} else if (configs[j].type == "tree") {
						this._createTree(configs[j]);
					}else if(configs[j].type == "ecpgrid"){
						configs[j].config.attrs.ismulti=options.isMul;
						this._createEcpGrid(configs[j]);
					}
				}
			}
		},
		show : function(options) {
			var that=this;
			options.confirm = options.confirm ? options.confirm : function() {};
			var dialog = new AdaptDialog({
				attrs : {
					title : options.name,
					hasCloseTip : true,
					content : handlebars.compile(template)(options),
					isHtmlContent : true,
					type : 'dialog',
					width : options.width || 950,
					height : 500,
					buttons : [ {
						key : 'add',
						name : '确定',
						method : function() {
							var component=null;
							var selectedRow=null;
							var selectedData=null;
							if($(".J-content table").size()!=0){
								//获取grid选择的数据
								var componentid=options.isTab ? $(".content:visible div[componenttype$=grid]").attr("componentid") : 
									$("div[componenttype$=grid]").attr("componentid");
								component=that.getComponent(componentid);
								selectedRow=component.getSelectedRow();
								selectedData=component.getSelectedData();
							}else{
								//获取tree选择的数据
								var componentid=$("div[componenttype$=tree]").attr("componentid");
								component=that.getComponent(componentid);
								selectedRow=component.getSelectedNodes();
								selectedData=[];
								for(var i=0;i<selectedRow.length;i++){
									selectedData.push(selectedRow[i].data);
								}
							}
							if (options.confirm(component, selectedRow, selectedData)) {
								dialog.close();
							}
							return false;
						}
					}, {
						key : 'cancel',
						name : '取消',
						method : function() {
							dialog.close();
							return false;
						}
					} ]
				}
			});
			dialog.show();
			this.components={};
			if (options.isTab) {
				this._createTab(options);
			} else {
				this._createComponent(options);
			}
			return false;
		},
		_parser : function(data) {
			if (!data) {
				throw new Error('数据不能为空');
			}
			if ($.isArray(data)) {
				throw new Error('数据不能使数组');
			}
			return {
				id : data.pk_marbasclass,
				pId : data.pk_parent,
				open : true,
				name : data.name,
				data : data
			};
		},
		_format : function(data, parser) {
			var that = this;
			if (!data) {
				throw new Error('数据不能为空');
			}
			if (!$.isArray(data)) {
				throw new Error('数据必须为数组');
			}
			if (!parser) {
				throw new Error('格式化公式不能为空');
			}
			if (!$.isFunction(parser)) {
				throw new Error('格式化公式必须为函数类型');
			}
			var nodes = [];
			$(data).each(function(i, item) {
				var node = parser(item);
				if (item.children && $.isArray(item.children) && (item.children.length > 0)) {
					// 还有孩子节点 => 递归调用
					node.children = that._format(item.children, parser);
				}
				nodes.push(node);
			});
			return nodes;
		},
		getComponent:function(id){
			return this.components[id];
		}
	});
	module.exports = Refer;
});