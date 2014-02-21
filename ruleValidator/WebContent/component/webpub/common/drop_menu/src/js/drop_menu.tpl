<div class="ui-menu" style="{{attrs.menuWidth}}">
    <div class="ui-menu_arrow up"></div>
    <div class="clear"></div>
    <div class="float_left w_100ps">
        <div class="ui-menu_corner lt"></div>
        <div class="ui-menu_border_horizon top" style="{{attrs.lineWidth}}"></div>
        <div class="ui-menu_corner rt"></div>
    </div>
    <div class="float_left w_100ps">
        <div class="ui-menu_border_vertical lft" style="{{attrs.borderHeight}}"></div>
        <div class="ui-menu_list" style="{{attrs.listWidth}}{{attrs.listHeight}}">
		{{#each model}}
            <a id="{{this.itemId}}" href="{{this.itemUrl}}" onclick="{{this.callback}}; return false;" style="{{this.lineWidth}}{{attrs.lineHeight}}">{{this.itemName}}</a>
        {{/each}}
        </div>
        <div class="ui-menu_border_vertical rit" style="{{attrs.borderHeight}}"></div>
    </div>
    <div class="float_left w_100ps">
        <div class="ui-menu_corner lb"></div>
        <div class="ui-menu_border_horizon btm" style="{{attrs.lineWidth}}"></div>
        <div class="ui-menu_corner rb"></div>
    </div>
</div>