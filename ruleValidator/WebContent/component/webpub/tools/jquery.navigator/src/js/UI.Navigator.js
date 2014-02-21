define(function(require, exports, module) {
	return function(jQuery) {
/**
 * Created by IntelliJ IDEA.
 * User: Phoenix
 * Date: 12-10-3
 * Time: 下午5:57
 * Generate page header.
 */

(function($) {

    $.fn.extend({
        'version': "1.0",
        UINavigator: function(options, value) {
            var self = this;
            if(self.prop("tagName") == undefined) return false;

            var uiOptions = $.extend({
                'items': null,
                'textColor': ""
            }, options);

            if(typeof value == 'string') value = "'" + value + "'";
            if(typeof options != 'object') eval("uiOptions." + options + " = " + value);

            var navigator = $( '<div></div>' ).addClass("ui-navigator").prependTo(self)
                .bind('mouseleave', function() {
//                    $( ".extends" ).removeClass("extends");
                    $( ".ui-menu" ).hide();
                });
            $( navigator ).after($( '<div></div>' ).addClass("clear"));
            $( uiOptions.items ).each(function(index, obj) {
            	if(this.needShow != false && this.needShow != "false"){
	                var item = $( '<a></a>' ).appendTo(navigator)
	                    .attr({ 'id': obj.itemId, 'href': obj.itemUrl })
	                    .bind('click', this.callback) 
	                    .html(obj.itemName)
	                    .css({ 'color': uiOptions.textColor != "" ? uiOptions.textColor : "" })
	                    .addClass(index == 0 ? "focus" : "");
	
	                if(obj.subMenu != undefined && $.isArray(obj.subMenu)) {
	                    setTimeout(function() {
	                        var subMenu = $( item ).UIDropMenu({ "menuId": obj.itemId + "Menu", 'items': obj.subMenu, 'eventName': "mouseenter", 'autoHide': true });
	                        $( item ).unbind('mouseenter');
	                        $( item ).bind('mouseenter', function() {
	//                            $( ".extends" ).removeClass("extends");
	                            $( ".ui-menu" ).not("#" + $( subMenu.menu ).attr("id")).hide();
	                            $( subMenu.menu ).not(":visible").slideDown("fast", "", function() {
	//                                item.addClass("extends");
	                            });
	                            $( subMenu.menu ).bind('mouseleave', function() {
	//                                $( item ).removeClass("extends");
	                            });
	                            $( subMenu.menuList ).find("a").bind('click', function() {
	//                                $( item ).removeClass("extends");
	                            });
	                        });
	                    },500)
	                } else {
	                    $( item ).bind('mouseenter', function() {
	                        $( ".ui-menu" ).hide();
	//                        $( ".extends" ).removeClass("extends");
	                    });
	                }
            	}
            });
            return navigator;
        }
    })

})(jQuery);
return jQuery.noConflict(true);
	}
});