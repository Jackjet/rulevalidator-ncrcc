define( function(require, exports, module) {
	require('../theme/{theme}/css/ecpgrid.css');
	var Grid = require("common/grid/src/js/Grid");
	var EcpGrid = Grid.extend({
		initCustAttr:function(){
			EcpGrid.superclass.initCustAttr.apply(this, arguments);
		}
	});
	module.exports = EcpGrid;
});