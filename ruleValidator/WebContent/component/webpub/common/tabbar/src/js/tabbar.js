define(function(require, exports, module) {
	var $ = require("$");
/*
 * tabpanel组件
 */
	require("common/tabbar/src/theme/default/css/tabbar.css");
	var tabbar_tpl = require("common/tabbar/src/js/tabbar.tpl");
	var Bar = require("bar");
    var switchable=Bar.extend({
    	template:tabbar_tpl,
    	//初始化特殊属性
    	initCustAttr:function(){
    		var items=this.get("items");
    		var triggersArray=new Array();
    		var stringContentsArray=new Array();
    		var objectContents=new Array();
    		$.each(items,function(key,element){
    			triggersArray.push(element.title);
    			if(typeof element.content=="string"){
    				stringContentsArray.push(element.content);
    			}else{
    				stringContentsArray.push("");
    				$(element.content).attr("objindex",key);
    				objectContents.push(element.content);
    			}
    		});
    		this.custProp={
    			triggers:triggersArray,
    			contents:stringContentsArray,
    			objectContents:objectContents,
    			moreurl:this.get("moreurl")
    		};
    		
    	},
    	parseElementFromTemplate:function(){
    		var that=this;
    		var template = this.compile(this.template);
			this.element = $(template(this.custProp));
			
			//将子控件的内容render进来
			$.each(this.custProp["objectContents"],function(key,element){
				var index=$(element).attr("objindex");
				var parentNode=that.element.find(".J-content").eq(index);
				element.set("parentNode", parentNode);
			});
			
			//为每个触发器添加索引
			$.each(this.element.find(".title_label"),function(index,record){
				$(record).attr("data",index);
			});
			
			// 初始切换到哪个面板
    		var activeIndex=this.get("activeIndex");
    		this.element.find(".title_label").eq(activeIndex).addClass("active");
    		this.element.find(".container").eq(activeIndex).addClass("active");
    		
    		//触发方式
    		var triggerType=this.get("triggerType");
    		this._bindEvents(triggerType);
			return this.element;
    	},
    	
        _bindEvents: function(triggerType) {
            var that = this;
            //click
            if (this.get('triggerType') == 'click') {
            	this.element.find(".title_label").click(focus);
            }else{
            	//hover
            }

            function focus(ev) {
                that.switchTo($(this).attr('data'));
            }
        },
    	
    	 // 切换到指定 index
        switchTo: function(toIndex) {
//        	this.element.find(".channel_title a.title_label.active").removeClass("active");
//        	this.element.find(".channel_title a.title_label").eq(toIndex).addClass("active");
            this.set('activeIndex', toIndex);
            
            return this.element;
        }
    });

    module.exports = switchable;
});
