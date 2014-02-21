window.GLOBAL = window.G = {};
seajs.use(['api','menuStore',"layout",'autoRender'/*,'param','context',/*,"autoRender"*/], function(Api, MenuStore/*, Param, Context*/) {
	MenuStore.create({
		callback:function() {
			seajs.use('autoRender');
		}
	});
});


