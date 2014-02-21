/**
 * 要货计划填报页面所使用的grid
 */
define(function(require, exports, module) {
	var $ = require("$");
	var log=require("log");
	var Dialog=require("dialog");
	var deligrid_tpl = require("./deligrid.tpl");
	var deligrid_left_row=require("./deligrid_left_row.tpl");
	var deligrid_right_row=require("./deligrid_right_row.tpl");
	var Widget = require("widget");
	require("../theme/default/css/deligrid.css");
	var handlebars = require("handlebars");
	var math=require("maths");
	var store=require("store");
	var tools=require("./deligrid_tool");
	var autocomplete=require("productautocompleteandselect");
	
	handlebars.registerHelper("left_content",function(context,option){
		return tools.geneLeft(context);
	});
	handlebars.registerHelper("right_content",function(context,option){
		return tools.geneRight(context);
	});
	
	var Delidisplay = Widget.extend({
		delRows:[],
		handlebars : handlebars,
		template : deligrid_tpl,
		initCustAttr:function(){
			this.set("oldwidth",$("body").width());
			this.set("start",0);
			this.set("pagesize",200);
		},
		loadData : function(){
			var delidisplay = this;
			var url = delidisplay.get("url");
			$.ajax({
				url : url,
				data:delidisplay.get("data"),
				success : function(data) {
					if(data.fstatusflag=="-1" && data.bcommitflag=="N"){
						delidisplay.set("isEdit",true);
					}
					//扩展配置
					var left_structure=data.left_structure;
					var left_structure_map={};
					for(var i=0;i<left_structure.length;i++){
						left_structure_map[left_structure[i].field]=left_structure[i];
					}
					var left_content=delidisplay.get("left_content") || [];
					for(var j=0;j<left_content.length;j++){
						$.extend(true,left_structure_map[left_content[j].field],left_content[j]);
					}
					//获取单元格高度
					var tdHeight;
//					var tdHeight=$("#J-first-baseinfo td").height();
					var size=0;
					for(var i=0;i<data.left_structure.length;i++){
						if(data.left_structure[i].isShow){
							size++;
						}
					}
					if(data.series.length==2){
						tdHeight=size*28;
					}else{
						tdHeight=size*56;
					}
					delidisplay.set("tdHeight",tdHeight);
					delidisplay.setModel(data);
					$(window).scroll(function(){
						$(".right-top,.right-content").css("right",$(this).scrollLeft());
						$(".left-content,.right-content").css("top",$(".orderplanpanel").height()-$(this).scrollTop());
						return false;
					});
					//处理单价精度
					$(".J-nprice").attr("scale",store.get("context")["priceScale"]);
					delidisplay._refreshStatus();
					seajs.emit("deligrid_success");
					//精度处理
					seajs.emit("scale");
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					log.error(XMLHttpRequest.responseText);						
				},
				dataType : 'json'
			});
		},
		query:function(){
			this.loadData();
		},
		bindEvent:function(){
			var that=this;
			//鼠标进入和离开单元格
			$(".J-td-item.edit").live("mouseover",function(){$(this).find("input.edit").css("border","2px solid #E5BA6F");});
			$(".J-td-item.edit").live("mouseout",function(){
				//当鼠标离开单元格时，如果单元格中的input存在焦点，则单元格的边框不消失
				if(!$(this).find("input.edit").hasClass("focus")){
					$(this).find("input.edit").css("border","0 none");
				}
			});
			//单元格获得和失去焦点
			$(".right-content .J-td-item.edit").live("focus",function(){
				//判断当前这一行的基本信息是否已经填写完毕
				var index=$(this).parents("tr").prevAll().size();
				if($(".J-leftcontent").eq(index).find(".J-castunitid").attr("data-key")==""){
					Dialog.alert({title:"提示",content:"请先选择商品/物料/物料分类/产品线/品牌",isHtmlContent:true});
				}
				$(this).addClass("focus");
				return false;
			});
			//对不能编辑的单元格，要让其在focus的时候就blur
			$("input[readonly=readonly]").live("focus",function(){
				$(this).blur();
				return false;
			});
			$(".J-td-item.edit").live("blur",function(){
				$(this).find("input").css("border","0 none");
				$(this).removeClass("focus");
			});
			$(".J-leftcontent input.edit").live("change",function(){
				$(this).attr("data-key",$(this).val());
				return false;
			});
			$(".J-rightcontent input.edit").live("change",function(){
				var input=$(this);
				if(!/^\d{1,18}(\.\d{1,8})?$/.test(input.val())){
					Dialog.alert({title:"提示",content:"请输入一个正确格式的数字，整数部分不能超过18位，小数部分不能超过8位"});
					input.val(input.attr("data-prevalue"));
					return false;
				}
				seajs.emit("scale");
				if(input.attr("data-prevalue")!=input.val()){
					input.addClass("J-change");
					//联动计算
					var rowIndex=$(this).parents("tr").prevAll().size();
					var price=$(".J-nprice").eq(rowIndex).val();
					var type=input.attr("data-tptype");
					if(/num$/.test(type)){
						that.calculate(0,input,price,type);
					}else{
						that.calculate(1,input,price,type);
					}
				}
			});
			//绑定删除行操作
			$(".J-del.edit").live("click",function(){
				var index=$(this).parents("tr").prevAll().size();
				Dialog.confirm({
					title : "删除",content : "确定删除该行?",
					confirm : function() {
						var bid=$(".J-corderplanbid").eq(index).attr("data-key");
						if(bid!=""){
							$(".J-delrows").append($("<span></span>").text(bid));
						}
						$(".left-content tr").eq(index).remove();
						$(".right-content tr").eq(index).remove();
						var newHeight=that.get("height")-(that.get("tdHeight")+1);
						$("body").css("height",newHeight+"px");
						that.set("height",newHeight);
						that._refreshAfterAddOrDel();
						Dialog.close();
					},
					cancel : function() {
						return true;
					}
				});
				return false;
			});
			$(".J-add.edit").live("click",function(){
				var addHandler=that.get("addHandler");
				if(addHandler){
					addHandler($(this));
				}
				return false;
			});
			seajs.on("deligrid_refresh",function(data){
				that.set("isEdit",data.isEdit);
				if(data.isLoad){
					that.query();
				}else{
					that._refreshStatus();
				}
			});
			seajs.on("deligrid_add",function(length){
				for(var i=0;i<length;i++){
					that._addRow();
				}
				$(window).scrollTop(1000000);
			});
		}, 
		_addRow:function(){
			var that=this;
			//左边
			var series=that.model.series;
			var addModel={};
			addModel.attrs={
				tdHeight:that.get("tdHeight"),
				isEdit:that.get("isEdit")
			};
			var leftModel={};
			leftModel["left_structure"]=that.model.left_structure;
			leftModel["left"]=[that.model.left_structure];
			leftModel["series"]=series;
			addModel["model"]=leftModel;
			var left=handlebars.compile(deligrid_left_row)(addModel);
			//右边
			var rightModel=[];
			var righthead=that.model.right.head;
			for(var j=0;j<righthead.length;j++){
				var areaitem=righthead[j].value;
				for(var k=0;k<areaitem.length;k++){
					var tempModel={};
					if(k==0){
						tempModel["type"]="total";
					}else{
						tempModel["orderplan_tp_begin_date"]=areaitem[k];
					}
					tempModel["orderplan_area"]=j+1;
					var seriesModel=[];
					for(var m=0;m<series.length;m++){
						var temp={};
						temp["isEditable"]=series[m].isEditable;
						temp["type"]=series[m].type;
						temp["key"]=series[m].key;
						seriesModel.push(temp);
					}
					tempModel["orderplan_tp_data"]=seriesModel;
					rightModel.push(tempModel);
				}
			}
			rightModel["righthead"]=rightModel;
			addModel["series"]=that.model.series;
			addModel["model"]=[rightModel];
			var right=handlebars.compile(deligrid_right_row)(addModel);
			$(".left-content tbody").append(left);
			$(".right-content tbody").append(right);
			var newHeight=that.get("height")+(that.get("tdHeight")+1);
			$("body").css("height",newHeight+"px");
			that.set("height",newHeight);
			that._refreshAfterAddOrDel();
			return false;
		},
		//chg开头的字段表示变化的字段，ret开头的字段表示联动的自动
		calculate:function(mark,chg_ele,price,type){
			var chg_old=chg_ele.attr("data-prevalue") || "";
			var chg_new=chg_ele.val() || "";
			var ret_ele=(mark==1 ? chg_ele.parents("td").find(".J-"+type.replace("mny","num")) : chg_ele.parents("td").find(".J-"+type.replace("num","mny")));
			var ret_old=ret_ele.val() || "";
			var ret_new=(mark==0?math.mul(price,chg_new):math.div(chg_new,price));
			ret_ele.val(ret_new);
			if(ret_new!=ret_old){
				ret_ele.addClass("J-change");
			}
			//联动合计单元格
			var tdIndex1=chg_ele.parent().prevAll().size();
			var tdIndex2=ret_ele.parent().prevAll().size();
			var totalTD=chg_ele.parents("td").prevAll(".J-totalTD").eq(0);
			this.calTotal({
				totalEle:totalTD.find("input").eq(tdIndex1),
				index:tdIndex1,
				total:totalTD.find("input").eq(tdIndex1).val(),
				oldVal:chg_old,
				newVal:chg_new
			});
			this.calTotal({
				totalEle:totalTD.find("input").eq(tdIndex2),
				index:tdIndex2,
				total:totalTD.find("input").eq(tdIndex2).val(),
				oldVal:ret_old,
				newVal:ret_new
			});
			chg_ele.attr("data-prevalue",chg_new);
			ret_ele.attr("data-prevalue",ret_new);
			seajs.emit("scale");
		},
		calTotal:function(data){
			if(data.newVal==""){
				//校验是否所有字段为空
				var flag=true;
				var tds=data.totalEle.parents(".J-totalTD").nextUntil(".J-totalTD");
				$.each(tds,function(index,element){
					if($(element).find("input").eq(data.index).val()!=""){
						flag=false;
						return false;
					}
				});
				if(flag){
					data.totalEle.val("");
					return false;
				}
			}
			data.totalEle.val(math.add(math.sub(data.total,data.oldVal),data.newVal));
		},
		afterRender:function(){
			var that=this;
			var model=this.prop.model;
			//计算高度,每一行的高度*行数+头部高度+orderplanpanel的高度+底部滚动条
			if(model){
				that.set("height",(model.left.length)*(that.get("tdHeight")+1)+50+40+18);
				that.refresh(model);
				autocomplete.init({
					className:"J-add-prod input",
					callback : function(id,name) {
						$.ajax({
							url:G.API.QUERT_PRODUCT_AUTOFILL,
							dataType:"json",
							type:"POST",
							data:{
								template:$(".J-cplantemplateid").attr("data-key"),
								id:id,
								pkOrg:$(".J-pk_org").attr("data-key")
							},
							success:function(data){
								var mnyscale=$(".J-corigcurrencyid").attr("scale");
								var mark=id;
								//获取库存组织的值
								var pkOrg=data.stock_pk || "";
								var pkOrgName=data.stock_name || "";
								//如果发货库存组织是计划客体
								if($(".J-cstockorgid").hasClass("J-object")){
									mark+=pkOrg;
								}
								if($(".J-"+mark).size()!=0){
									alert("所选商品已经存在");
									return;
								}else{
									that._addRow();
									//获取单位的值
									var cunitid=data.pk_unit;
									var cunitid_name=data.pk_unit_name;
									var cunitid_scale=data.pk_unit_scale;
									//设置值
									$(".J-cproductid").last().attr("data-key",id).val(name).attr("id",id);
									//设置单位的值
									$(".J-leftcontent").last().find(".J-castunitid").attr("data-key",cunitid).val(cunitid_name);
									//设置发货库存组织的值
									$(".J-cstockorgid").last().attr("data-key",pkOrg).val(pkOrgName);
									//设置右边区域的精度
									$(".right-content tr").last().find("input[data-tptype$=num]").attr("scale",cunitid_scale);
									$(".right-content tr").last().find("input[data-tptype$=mny]").attr("scale",mnyscale);
									//设置价格
									$(".J-nprice").last().attr("data-key",data.price).val(data.price).attr("scale",store.get("context")["priceScale"]);
									seajs.emit("scale");
								}
							}
						});
					},
					loadBtnFunction:function(){
						var addHandler=that.get("addHandler");
						if(addHandler){
							addHandler($(".autocomplete input"));
						}
						return false;
					}
				});
			}
		},
		refresh:function(model){
			var that=this;
			//1.边线处理
			that._refreshBorder();
			//2.计算宽度
			//左边区域的宽度UE已经指定了，所以右边区域的宽度需要计算而不能使用百分比，宽度由屏幕宽度-左边区域
			$(".right-top,.right-content").css("width",that.get("oldwidth")-440);
			//计算有多少个时格
			var tp=model.right.head;
			var total=0;
			for(var j=0;j<tp.length;j++){
				total+=tp[j].value.length;
			}
			//获取每个时格的宽度
			if((that.get("oldwidth")-440)/total>100){
				//如果宽度大于100，那么说明下面没有出现滚动条
				$(".right-content td,.right-top-2tr td").css("width",$(".right-top-2tr td").width()+"px");
				$(".right-td-item").css("width","100%");
			}else{
				//如果不小于100，那么说明出现了滚动条，那么需要重新计算body的宽度，这样才能实现效果
				$("body").css("width",total*101+440+"px");
			}
			//3.计算高度
			$("body").css("height",this.get("height")+"px");
		},
		//在新增或删除行后进行的刷新,相比于refresh方法，少了一些步骤，以减少不必要的时间浪费
		_refreshAfterAddOrDel:function(){
			//1.边线处理
			this._refreshBorder();
			//2.计算高度
			$("body").css("height",this.get("height")+"px");
		},
		_refreshStatus:function(){
			var that=this;
			if(that.get("isEdit")==true){
				//处理删除按钮
				$(".J-del").addClass("edit");
    			$(".del-icon").removeClass("hidden");
    			//处理新增
    			if($(".J-cproductid").size()!=0){
    				$(".J-add-prod").removeClass("hidden");
    			}else{
    				$(".J-add-other").removeClass("hidden");
    				$(".J-add-other a").addClass("edit");
    			}
			}else{
				$(".J-del").removeClass("edit");
	 			$(".del-icon").addClass("hidden");
	 			$(".J-add-prod,.J-add-other").addClass("hidden");
			}
		},
		_refreshBorder:function(){
			//(1)左边栏目单元格的右边线和右边合计单元格的左边线重合，去掉右边合计单元格的边线，这样在右边区域左右移动的时候，始终保证最左边有边线
			$(".right-top tr,.right-content tr").each(function(){
				$(this).find("td").first().css("border-left","0 none");
			});
			//(2)右边内容区第一行的上边线和右边头部的下边线重合，去掉右边内容区第一行的上边线，这样在上下移动的时候，始终保证最上边有边线
			$(".right-content tr:first td").each(function(){
				$(this).css("border-top","0 none");
			});
			//(3)右边内容区最右边加上边线
			$(".right-content tr").each(function(){
				$(this).find("td").last().css("border-right","1px solid #929292");
			});
		}
	});
	module.exports = Delidisplay;
});