<div class="ui-portlet">
	<div class="ui-portlet-top">
		<div class="ui-portlet-top-content">
			<span class="title">{{this.attrs.title}}</span>
			<a class="ui-portlet-top-tools i-collapse"></a>
			
			<ul class="J-tools ui-portlet-top-collapsetool">   
		    <li>    
		    <ul class="ui-portlet-tool-colorChange">  
		    <li>边框颜色：</li>                  
		    <li class="color colorDefault">    
		    <a></a>    
		    </li>  
		    <li class="color colorBlue">    
		    <a title="蓝色"></a>    
		    </li>  
		    <li class="color colorPink">    
		    <a title ="粉色"></a>    
		    </li>   
		    </ul>    
		    </li>  
		    <li><a>   
		    	边框：<input class="J-borderchecked" type="checkbox" value="true" {{#if this.attrs.noborder}} {{else}}checked="checked"{{/if}}>显示</input>
		    	</a>    
		    </li> 
		    <li>    
		    <a class="J-portlet-edit">编辑</a>    
		    </li>                       
		    <li>    
		    <a class="J-portlet-delete">删除</a>    
		    </li>            
		    </ul>
			<a class="ui-portlet-top-tools i-move"></a>
		</div>
	</div>
	<div class="ui-portlet-content J-content">
		<div class="loading"></div>
	</div>
	<div class="ui-portlet-bottom">
			{{#if this.attrs.more}}
				<a href="{{this.attrs.moreurl}}" class="more">更多&gt;&gt;</a>
			{{/if}}
			<div class="clear"></div>
	</div>
</div>