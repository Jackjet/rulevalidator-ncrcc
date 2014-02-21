define(function(require, exports, module) {
	// 默认引入：组件继承自widget
	var Widget = require("widget");
	// 自定义：组件对应的css
	require('../theme/{theme}/css/productdesc.css');
	// 自定义：组件的html模板
	var template = require("./productdesc.tpl");
	var $ = require("$");
	$ = require("jquery.extend")($);
	var request = require("request");
	var productPrice = require("productPrice");
	var nabnum = require("nabnum");
	var FavoriteButton = require('favoritebutton');
	var store=require("store");
	var scale=require("scale");
	var handlebars = require("handlebars");
	
	handlebars.registerHelper("get", function(model, key) {
		if(model){
			return model[key];
		}else{
			return "";
		}
	});
	
	handlebars.registerHelper("productdesc",function(context,option){
		var ret="";
		var disableret="";
		var columns=context.attrs.columns;
		var model=context.model;
		var pid=context.attrs.prodid;
		var useStore = store.get("productinfo")||null;
		
		if(useStore){
			//所有显示的字段
			for(var j in useStore[0]){
				for(var i=0;i<columns.length;i++){
					if(columns[i].show&&columns[i].key==useStore[0][j]){
						ret+="<li class='J-"+columns[i].key+"'>";
						//如果改变了名称属性
						if(useStore[2]){
							ret+="<span class='J-span-title'>"+(useStore[2][i]||columns[i].name)+"</span><span>:</span>";
						}
						else{
							ret+="<span class='J-span-title'>"+columns[i].name+"</span><span>:</span>";
						}
						if(model){
							var type=columns[i].type;
							var mark=columns[i].mark || "";
							if(type=="price"){
								ret+="<span class='"+mark+"' ectype='price' productId='"+pid+"'></span>";
							}else if(type=="nabnum"){
								ret+="<span class='"+mark+"' scale='"+model.pk_measdoc_unitScale+"' ectype='nabnum' productId='"+pid+"' bspotflag='"+model.bspotflag+"' unit='"+model.pk_measdoc_name+"'></span>";
							}else if(type=="baseprice"){
								ret+="<span class='"+mark+"' data-pricetype='base' ectype='price' productId='"+pid+"'></span>";
							}else{
								var key=columns[i].key;
								ret+="<span class='"+mark+"'>"+(model[key] || "")+"</span>";
							}
						}
						ret+="</li>";
					}
				}
			}
			//所有隐藏的字段
			for(var j in useStore[1]){
				for(var i=0;i<columns.length;i++){
					if(columns[i].show&&columns[i].key==useStore[1][j]){
						disableret+="<li class='J-"+columns[i].key+" disabled'>";
						//如果改变了名称属性
						if(useStore[2]){
							ret+="<span class='J-span-title'>"+(useStore[2][i]||columns[i].name)+"</span><span>:</span>";
						}
						else{
							ret+="<span class='J-span-title'>"+columns[i].name+"</span><span>:</span>";
						}
						if(model){
							var type=columns[i].type;
							var mark=columns[i].mark || "";
							if(type=="price"){
								disableret+="<span class='"+mark+"' ectype='price' productId='"+pid+"'></span>";
							}else if(type=="nabnum"){
								disableret+="<span class='"+mark+"' scale='"+model.pk_measdoc_unitScale+"' ectype='nabnum' productId='"+pid+"' bspotflag='"+model.bspotflag+"' unit='"+model.pk_measdoc_name+"'></span>";
							}else if(type=="baseprice"){
								disableret+="<span class='"+mark+"' data-pricetype='base' ectype='price' productId='"+pid+"'></span>";
							}else{
								var key=columns[i].key;
								disableret+="<span class='"+mark+"'>"+(model[key] || "")+"</span>";
							}
						}
						disableret+="</li>";
					}
				}
			}
		}
		else{
			for(var i=0;i<columns.length;i++){
				if(columns[i].show){
					ret+="<li class='J-"+columns[i].key+"'>";
					ret+="<span class='J-span-title'>"+columns[i].name+"</span><span>:</span>";
					if(model){
						var type=columns[i].type;
						var mark=columns[i].mark || "";
						if(type=="price"){
							ret+="<span class='"+mark+"' ectype='price' productId='"+pid+"'></span>";
						}else if(type=="nabnum"){
							ret+="<span class='"+mark+"' scale='"+model.pk_measdoc_unitScale+"' ectype='nabnum' productId='"+pid+"' bspotflag='"+model.bspotflag+"' unit='"+model.pk_measdoc_name+"'></span>";
						}else if(type=="baseprice"){
							ret+="<span class='"+mark+"' data-pricetype='base' ectype='price' productId='"+pid+"'></span>";
						}else{
							var key=columns[i].key;
							ret+="<span class='"+mark+"'>"+(model[key] || "")+"</span>";
						}
					}
					ret+="</li>";
				}
			}
		}
		return ret+disableret;
	});
	
	// 组件的定义 组件名大写
	var Example = Widget.extend({
		id:"productdesc",
		template : template,
		initCustAttr : function() {
			this.productId=request.getParameter("id");
			this.set("prodid",request.getParameter("id"));
		},
		handlebars : handlebars,
		defaultOption:{
			columns:
				[
	        		{name:"商品编号",key:"vcode",show:true,isEdit:false},
	        		{name:"当前价",key:"price",show:true,type:"price",isEdit:false},
	        		{name:"基准价",key:"baseprice",show:true,type:"baseprice",isEdit:false},
	        		{name:"库存",key:"nabnum",show:true,type:"nabnum",isEdit:false},
	        		{name:"品牌",key:"pk_brand_name",show:true,isEdit:true},
	        		{name:"产品线",key:"pk_prodline_name",show:true,isEdit:true},
	        		{name:"规格",key:"materialspec",show:true,isEdit:true},
	        		{name:"物料",key:"cinventoryid_name",show:true,isEdit:true},
	        		{name:"销售组织",key:"saleOrg",show:true,isEdit:true},
	        		{name:"赠品",key:"large",show:true,mark:"J-largess",isEdit:true},
	        		{name:"辅助属性",key:"freeprop",show:true,mark:"J-free",isEdit:true}
	        	]
		},
		loadData : function() {
			var that = this;
			var id = request.getParameter("id");
			var url = G.API.PRODUCT;
			$.ajax({
				type : 'GET',
				url : url,
				data:{
					"id":id
				},
				success : function(data) {
					that.setModel(data);
					if(data){
						//拼接自由辅助属性
						var summary = data.vfreeStr;
						if (data.vfreeStr && data.vfreeStr.length > 30) {
							summary = summary.substring(0, 30) + '……';
						}
						var detailThumb = $('<a class="ui-free-properties-thumb"></a>').append(summary);
						var detailDesc = $('<ul class="ui-free-properties-desc"></ul>');
						for(var i=0;i<data.vfrees.length;i++){
							if(data.vfrees[i]){
								detailDesc.append("<li>&nbsp;"+data.vfrees[i]+"&nbsp;</li>");
							}
						}
						$(".J-free").append(detailThumb.append(detailDesc));
					}
					seajs.emit("event_productdesc_loaded");
				},
				dataType : 'json'
			});
		},
		afterRender : function() {
			productPrice.init();
			nabnum.init();
			scale.init();
			if($(".page_config").length>0){
				this.managable();
			}
		},
		//在后台管理页面允许进行显示详情编辑
		managable : function(){
			$(".desc_list li").each(function(i,ele){
				$(this).append('<span class="icon icon_remove" title="隐藏/显示"></span><span class="icon icon_down" title="下移"></span><span class="icon icon_up" title="上移"></span>');
			});
			//编辑属性名称
			$(".J-span-title").prop("title","点击改变属性名称").on("click",function(){
				if($(this).find("input").length==0){
					var title = $(this).html();
					$(this).html("<input type='text' value='"+title+"'></input>");
					$(this).find("input[type='text']").focus();
					$(this).find("input[type='text']").on("blur",function(){
						var parent = $(this).parent();
						parent.html($(this).val());
						saveproductinfo();
					});
				}
			});
			
			
			//下移
			$(".icon_down").on("click",function(){
				var element = $(this).parent();
				if(element.next(":not(.disabled)").length>0){
					setbackground(element);
					element.next().after(element);
					saveproductinfo();
				}
			});
			//上移
			$(".icon_up").on("click",function(){
				var element = $(this).parent();
				if(element.prev().length>0){
					setbackground(element);
					element.prev().before(element);
					saveproductinfo();
				}
			});
			//remove
			$(".icon_remove").on("click",function(){
				var parent = $(this).parent();
				if(parent.hasClass("disabled")){
					$(this).parent().removeClass("disabled");
				}
				else{
					parent.addClass("disabled").appendTo(parent.parent());
				}
				saveproductinfo();
			});
			//remove
			$("li:not(.disabled) .icon_remove").toggle(function(){
				var parent = $(this).parent();
				parent.addClass("disabled").appendTo(parent.parent());
				saveproductinfo();
			},function(){
				$(this).parent().removeClass("disabled");
				saveproductinfo();
			});
			function setbackground(ele){
				$(".desc_list li").removeClass("li_selected");
				ele.addClass("li_selected");
			}
			function saveproductinfo(){
				var data = [[],[],[]];
				$(".desc_list li").each(function(i,ele){
					if(!$(this).hasClass("disabled")){
						data[0].push($(ele).clone().removeClass("li_selected").prop("class").substring(2));
					}
					else{
						data[1].push($(ele).clone().removeClass("disabled").removeClass("li_selected").prop("class").substring(2));
					}
					data[2].push($(ele).find(".J-span-title").html());
				});
				store.set("productinfo",data);
			}
		},
		bindEvent : function() {
			var that=this;
			$(".J-favorite").live("click",function(){
				seajs.emit(G.EVENT.FAVORITE_ADD,request.getParameter("id"));
				return false;
			});
			$(".J-cart").live("click",function(){
				if($(this).parent().hasClass("noprice") 
						|| $(this).parent().hasClass("nonabnum"))
					return false;
				var num=$(".J-num:input").val();
				var id=$(this).attr("product");
				seajs.emit(G.EVENT.SHOPCART_ADD,{id:id,number:num});
				alert("加入购物车成功!");
				return false;
			});
			seajs.on("setPriceSuccess",function(productid){
				that.element.find("span[productid="+productid+"]").removeClass("noprice");
			});
			seajs.on("setNabnumSuccess",function(productid){
				that.element.find("span[productid="+productid+"]").removeClass("nonabnum");
			});
			function removeMark(mark){
				that.element.find("."+mark).removeClass(mark);
			}
			seajs.on(G.EVENT.NABNUM_LOADED,function(){
				removeMark("nabnumloaded");
			});
			seajs.on(G.EVENT.PRICE_LOADED,function(){
				removeMark("priceloaded");
			});
			//商品tab页默认(仅在后台管理页面使用)
			$("#propdefault").on("click",function(){
				store.set("productinfo","");
				window.location.reload();
			});
		},
		doEdit:function(){
			var componentConfigs=store.get("componentConfigs") || {};
			var configs=componentConfigs[this.id] || {};
			var columns=configs.columns || this.defaultOption.columns;
			if(columns){
				var size=$(".J-checkbox").size();
				if(size==0){
					for(var i=0;i<columns.length;i++){
						if(columns[i].isEdit){
							var checkboxes=$("<span><input class='J-checkbox' type='checkbox'></span>");
							$(".J-"+columns[i].key).find("span:last").after(checkboxes);
							if(columns[i].show){
								$(".J-"+columns[i].key).find(".J-checkbox").prop("checked",true);
							}
						}
					}
				}
				$(".J-checkbox").show();
			}
		},
		saveEdit:function(){
			var columns=$.extend(true,[],this.get("columns"));
			for(var i=0;i<columns.length;i++){
				if(columns[i].isEdit){
					if($(".J-"+columns[i].key).find(".J-checkbox").prop("checked")==false){
						columns[i].show=false;
						$(".J-"+columns[i].key).hide();
					}
				}
				
			}
			$(".J-checkbox").hide();
			return {columns:columns};
		},
		reset:function(){
			this.set("columns",this.defaultOption.columns);
			this.render();
		}
	});
	// 组件对外提供使用
	module.exports = Example;
});