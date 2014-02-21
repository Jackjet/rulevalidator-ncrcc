define(function(require, exports, module) {
	require("../theme/default/css/editgrid.css");
	var $ = require("$");
	var Backbone = require("backbone");
	var Handlebars = require("handlebars");
	var Log = require("log");
	var app_tpl = require("./editgrid.tpl");
	var row_tpl = require("./editgrid_row.tpl");
	var Grid=require("grid");
	var Json=require("jquery.json2")($);
	$ = require("jquery.custompara")($);
	
	Handlebars.registerHelper("row", function(context, options) {
		var ret = "";
		var model=context.model;
		var columns=context.columns;
		var isEdit=context.isEdit;
		if (model) {
			for (var i = 0; i < columns.length; i++) {
				var key=columns[i].key;
				var value=model[key];
				var format=columns[i].format;
				if(format){
					ret+=format(value,model);
				}else{
					var stringvalue;
					if(parseInt(value)==0){
						stringvalue="0";
					}else{
						stringvalue=value?value:"";
					}
					var type=columns[i].type;
					if(type=="select"){
						ret+="<td><div class='view'>"+stringvalue+"</div><select class='J-init edit J-"+key+"'><option value=''>请选择</option>";
						if(value){
							ret+="<option value='"+value+"' selected='true'>"+value+"</option>";
						}
						ret+="</select></td>";
					}else if(type=="hidden"){
						ret+="<td class='hidden'><input type='hidden' class='J-"+key+"' value='"+stringvalue+"'></td>";
					}else if(type=="text"){
						ret+="<td><input type='text' class='J-"+key+"' value='"+stringvalue+"'></td>";
					}else{
						ret+="<td class='J-"+key+"'>"+stringvalue+"</td>";
					}
				}
			}
			if(isEdit){
				ret+="<td><a class='J-update view' href='#'>编辑</a><a class='J-confirm edit' href='#'>确认</a></td>";
				ret+="<td><a class='J-delete view' href='#'>删除</a><a class='J-cancel edit' href='#'>取消</a></td>";
			}
		}
		return ret;
	});
	
	var EditGrid=Grid.extend({
		template:app_tpl,
		Data:{},
		bindEvent:function(){
			var events =this.get("event");
			if(events){
				for(var i=0;i<events.length;i++){
					var key=".J-"+events[i].key;
					var trigger=events[i].trigger;
					var handler=events[i].handler;
					$(key).live(trigger,handler);
				}
			}
			var buttons=this.get("buttons");
			if(buttons){
				for(var i=0;i<buttons.length;i++){
					var key=".J-"+buttons[i].key;
					var handler=buttons[i].handler;
					$(key).bind("click",handler);
				}
			}
		},
		initCustAttr:function(){
			var columns = this.get("columns");
			for (var i = 0; i < columns.length; i++) {
				var column = columns[i];
				if(column){
					if(column.type=="hidden"){
						column.isHidden=true
					}
				}else{
					//解决在ie8下面的问题
					//在ie8下面columns会多一项
					columns.splice(i,1);
				}
			}
		},
		//获取某个格的值
		getValue:function(row,key,type){
			if(type=="select"){
				var value=$("tbody tr").eq(row).find(".J-"+key).find("option:selected").val();
				return value!="empty"?value:"";
			}else if(type=="text" ||type=="hidden"){
				return $("tbody tr").eq(row).find(".J-"+key).val();
			}else{
				return $("tbody tr").eq(row).find(".J-"+key).text();
			}
		},
		getData:function(){
			return this.Data;
		},
		setData:function(data){
			this.Data=data;
		},
		query:function(params){
			var that=this;
			var data=that.get("data");
			$("#id_center_content tbody").find("tr").remove();
			that.list.fetch({
				url:that.get("url"),
				success : function(models, resp) {
					$.each(models.models,function(index,model){
						model.attributes.exist=true;
					});
					that.setData(that.list.toJSON());
				},
				data:data
			});
			return false;
		},
		afterRender:function(){
			var EditGrid=this;
			$(function() {
				Backbone.emulateHTTP = true;
				Backbone.emulateJSON = true;
				var RowModel = Backbone.Model.extend({
					idAttribute:EditGrid.get("id")
				});
				var RowModelList = Backbone.Collection.extend({
					model : RowModel
				});
				var rowModelList = new RowModelList();
				EditGrid.list=rowModelList;
				var ItemView = Backbone.View.extend({
					tagName : "tr",
					template : row_tpl,
					events : {
						"click .J-update" : "update",
						"click .J-delete" : "clear",
						"click .J-confirm":"confirm",
						"click .J-cancel":"cancel"
					},
					initialize : function() {
						this.model.bind('change', this.render, this);
						this.model.bind('remove', this.remove, this);
					},
					render : function() {
						var columns=EditGrid.get("columns");
						this.$el
								.html(Handlebars.compile(this.template)({model:this.model.attributes,columns:columns,isEdit:EditGrid.get("isEdit")}));
						this.input = this.$('.view');
						return this;
					},
					confirm:function(object){
						var row=$(object.currentTarget).parent().parent().prevAll().size();
						var temp=EditGrid.getData()[row] || {};
						var columns=EditGrid.get("columns");
						for(var i=0;i<columns.length;i++){
							var key=columns[i].key;
							var type=columns[i].type;
							var value=EditGrid.getValue(row,key,type);
							var validate=columns[i].validate;
							if(validate){
								if(!validate.call(columns[i],value,row)){
									return false;
								}
							}
							temp[key]=value;
						}
						this.model.attributes=temp;
						this.model.attributes.exist=true;
						this.model.trigger("change");
						this.$el.removeClass("editing");
						EditGrid.setData(rowModelList.toJSON());
						return false;
					},
					cancel:function(object){
						if(this.model.attributes.exist){
							this.model.trigger("change");
							this.$el.removeClass("editing");
						}else{
							$(object.currentTarget).parent().parent().remove();
							this.model.destroy();
						}
						return false;
					},
					update : function() {
						var row=this.$el.prevAll().size();
						if(EditGrid.get("beforeUpdate").call(EditGrid,this.model.attributes,row)){
							this.$el.addClass("editing");
						}
						return false;
					},
					clear : function() {
						rowModelList.remove(this.model);
						EditGrid.setData(rowModelList.toJSON());
						return false;
					}
				});
				var AppView = Backbone.View.extend({
					el : $("body"),
					template : app_tpl,
					initialize : function() {
						rowModelList.bind('reset', this.addAll, this);
						rowModelList.bind('add', this.addOne, this);
					},
					events : {
						"click .J-add" : "addRow"
					},
					addAll : function() {
						rowModelList.each(this.show);
					},
					load : function() {
						EditGrid.query();
					},
					// 新增一行
					addRow : function() {
						var increment=EditGrid.get("increment");
						var no;
						var last=$(".J-"+increment).last();
						if(last.size()==0){
							no=10;
						}else{
							no=parseInt(last.text())+10;
						}
						
						var data={exist:false};
						data[increment]=no;
						rowModelList.add(data);
						return false;
					},
					addOne : function(todo) {
						var view = new ItemView({
							model : todo
						});
						var el=view.render().el;
						$("#id_center_content tbody")
								.append(el);
						$(el).addClass("editing");
						$(el).find(":text").attr("disabled","disabled");
					},
					show:function(todo){
						var view = new ItemView({
							model : todo
						});
						var el=view.render().el;
						$("#id_center_content tbody").append(el);
					}
				});
				var app = new AppView();
				app.load();
			});
		}
	});
	module.exports = EditGrid;
});