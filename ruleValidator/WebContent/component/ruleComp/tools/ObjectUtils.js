/**
 * 数组工具类
 */
define(function(require, exports, module) {
	/**
	 * 合并数组
	 */
	exports.merge = function(array0, array1) {
		for (var index1 in array1) {
			var isDuplicate = false;
			var item1 = array1[index1];
			for (var index0 in array0) {
				var item0 = array0[index0];
				if (item0 === item1) {
					isDuplicate = true;
					break;
				}
			}
			if (!isDuplicate) {
				array0.push(item1);
			}
		}
		return array0;
	};
	
	/**
	 * 去除重复
	 */
	exports.unique = function(array) {
		return this.merge([], array);
	};
});
