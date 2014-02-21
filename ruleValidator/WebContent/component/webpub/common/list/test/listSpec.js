

define(function(require) {
	require("tools/jasmine/jasmineh");
	var List = require("../src/js/list.js");
	// 建立个describe块
	describe('list组件测试', function() {
		// 建立it块
		it('adds two numbers together 1+2', function() {
					// 测试1+2是否等于3
					expect(1 + 2).toEqual(3);
				});

		it('jquery test', function() {
			var list = new List({
				attrs : {
					url : '/web/component/webpub/common/list/test/data.json'
				},
				autoRender : true
			});
			// 测试1+2是否等于3
			expect(list.get("url")).toBe('/web/component/webpub/common/list/test/data.json');
		});
		it('list 模型', function() {
			var list = new List({
				attrs : {
					url : '/web/component/webpub/common/list/test/data.json'
				},
				autoRender : true
			});
			// 测试1+2是否等于3
			expect(list.get("model").length).toBe(4);
		});
	});
});
