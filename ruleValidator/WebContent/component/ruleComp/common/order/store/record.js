define(function(require, exports, module) {
	function Record(json) {
		var data = json || {};
		return {
			get:function(name) {
				return data[name];
			},
			set:function(name, value) {
				data[name] = value;
			}
		};
	}
	module.exports = Record;
});