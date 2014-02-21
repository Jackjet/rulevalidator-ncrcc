define(function(require, exports, module) {
	var Spinner = require("./spin"); 
	var Loading = {
		load:function(id){
			 new Spinner().spin(document.getElementById(id));
		}
	}
	module.exports = Loading;
});