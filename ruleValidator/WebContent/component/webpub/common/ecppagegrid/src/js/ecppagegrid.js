define(function(require, exports, module) {
			var EcpGrid = require("ecpgrid");
			var ecppagegrid_tpl = require("./ecppagegrid.tpl");
			var $ = require("$");
			$ = require("jquery.pagination")($);
			var Math = require("math.uuid");
			var EcpPageGrid = EcpGrid.extend({
						CURRENT_PAGE : 0,
						PAGE_SIZE : 10,
						template : ecppagegrid_tpl,
						newSearchId : "",
						oldSearchId : "",
						queryParams : [],
						// 初始化分页
						initPageBar : function(params, total) {
							var that = this;
							this.element.find(".J-pagination").pagination(
									total,// 总条数
									{
										'items_per_page' : that.PAGE_SIZE,// 每页多少个
										'num_display_entries' : 6,// 显示多少个
										'num_edge_entries' : 3,// 边界显示多少个
										'callback' : function(pageIndex, jq) {
											that.page(params, pageIndex);
											return false;
										},
										current_page : that.CURRENT_PAGE
									});

						},
						// 分页
						page : function(params, pageIndex) {
							if (this.CURRENT_PAGE != pageIndex) {
								this.CURRENT_PAGE = pageIndex;
								this.oldSearchId = this.newSearchId;
								this.setMapValue(params, "pageInfo.curPageNo",
										this.CURRENT_PAGE + 1);
								this.setMapValue(params, "pageInfo.pageSize",
										this.PAGE_SIZE);
								this.loadData(params);
							}
						},
						// ajax加载数据
						loadData : function(params) {
							if (!params) {
								params = this.queryParams;
							}
							var autoRender = this.get("autoRender");
							if (autoRender) {
								this.setMapValue(params, "newSearchId",
										this.newSearchId);
								this.setMapValue(params, "oldSearchId",
										this.oldSearchId);
								this.setMapValue(params, "pageInfo.curPageNo",
										this.CURRENT_PAGE + 1);
								this.setMapValue(params, "pageInfo.pageSize",
										this.PAGE_SIZE);
								var grid = this;
								var url = this.get("url");
								$.ajax({
											type : 'POST',
											url : url,
											success : function(data) {
												grid.setModel(data);
												if(data != null){
													grid.initPageBar(params,
															data.total);
												}else {
													grid.initPageBar(params,
															0);
												}
											},
											error : function(data) {

											},
											dataType : 'json',
											data : params
										});
							}
						},
						setMapValue : function(map, key, value) {
							var entry = this.getMapObject(key, map);
							if (entry == null) {
								map.push({
											name : key,
											value : value
										});
							} else {
								entry.value = value;
							}
						},
						getMapObject : function(key, map) {
							var object = null;
							for (var i = 0, length = map.length; i < length; i++) {
								var entry = map[i];
								if (key === entry.name) {
									object = entry;
								}
							}
							return object;
						},
						// 查询接口（回调分页）
						query : function(form) {
							if (form && form.serializeArray) {
								this.queryParams = form.serializeArray();
							}
							this.newSearchId = Math.uuid();
							this.set("autoRender", true);
							this.loadData(this.queryParams);
						}
					});

			module.exports = EcpPageGrid;
		});