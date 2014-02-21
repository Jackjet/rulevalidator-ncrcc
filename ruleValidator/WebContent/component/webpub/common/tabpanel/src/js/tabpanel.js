define(function(require, exports, module) {
/*
 * tabpanel组件
 */
	require("common/tabpanel/src/theme/default/css/tabpanel.css");
	require("common/tabpanel/src/theme/red/css/tabpanel.css");
	var tabpanel_tpl = require("common/tabpanel/src/js/tabpanel.tpl");
	var Widget = require("widget");
    var switchable=Widget.extend({
    	template:tabpanel_tpl,
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
    			objectContents:objectContents
    		};
    		
    	},
    	parseElementFromTemplate:function(){
    		var that=this;
    		var template = this.compile(this.template);
			this.element = $(template(this.custProp));
			
			//将子控件的内容render进来
			$.each(this.custProp["objectContents"],function(key,element){
				var index=$(element).attr("objindex");
				var parentNode=that.element.find(".ui-tab-container-cell").eq(index);
				element.set("parentNode", parentNode);
			});
			
			//为每个触发器添加索引
			$.each(this.element.find(".ui-tab-trigger-cell"),function(index,record){
				$(record).attr("data",index);
			});
			
			// 初始切换到哪个面板
    		var activeIndex=this.get("activeIndex");
    		this.element.find(".ui-tab-trigger-cell").eq(activeIndex).addClass("ui-tab-trigger-cell-current");
    		this.element.find(".ui-tab-container-cell").eq(activeIndex).addClass("ui-tab-container-cell-current");
    		this.element.find(".ui-tab-trigger-cell").last().css("border-right","none");
    		
    		//触发方式
    		var triggerType=this.get("triggerType");
    		this._bindEvents(triggerType);
			return this.element;
    	},
    	
        _bindEvents: function(triggerType) {
            var that = this;
            //click
            if (this.get('triggerType') == 'click') {
            	this.element.find(".ui-tab-trigger-cell").click(focus);
            }else{
            	//hover
            }

            function focus(ev) {
                that.switchTo($(this).attr('data'));
            }
        },
    	
    	 // 切换到指定 index
        switchTo: function(toIndex) {
        	this.element.find(".ui-tab-trigger-cell-current").removeClass("ui-tab-trigger-cell-current");
        	this.element.find(".ui-tab-trigger-cell").eq(toIndex).addClass("ui-tab-trigger-cell-current");
            this.set('activeIndex', toIndex);
            
            return this.element;
        }
//        // 切换到上一视图
//        prev: function() {
//            var fromIndex = this.get('activeIndex');
//            // 考虑循环切换的情况
//            var index = (fromIndex - 1 + this.get('length')) % this.get('length');
//            this.switchTo(index);
//        },
//        // 切换到下一视图
//        next: function() {
//            var fromIndex = this.get('activeIndex');
//            var index = (fromIndex + 1) % this.get('length');
//            this.switchTo(index);
//        }
    });

    module.exports = switchable;
});
