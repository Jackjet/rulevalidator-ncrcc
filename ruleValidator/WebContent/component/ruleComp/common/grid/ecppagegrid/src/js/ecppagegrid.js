define(function(require, exports, module) {
	var EcpGrid = require("ecpgrid");
	var ecppagegrid_tpl = require("./ecppagegrid.tpl");
	var $ = require("$");
	$ = require("jquery.extend")($);
	var log = require("log");
	$ = require("jquery.pagination")($);
	var Math = require("math.uuid");
	var EcpPageGrid = EcpGrid
			.extend({
				CURRENT_PAGE : 0,
				PAGE_SIZE : 20,
				template : ecppagegrid_tpl,
				newSearchId : "",
				oldSearchId : "",
				queryParams : [],
				// 初始化分页
				initPageBarSize : function(page, params) {
					var pagesize = '<div class="J-pagination-page" style="float: left;">'
							+ '<span>每页显示</span>'
							+ '<select>'
							+ '<option value="10" title="每页显示10条记录" selected="selected">10</option>'
							+ '<option value="20" title="每页显示20条记录">20</option>'
							+ '<option value="50" title="每页显示50条记录">50</option>'
							+ '</select>' + '<span>条记录</span>' + '</div>';
					page.find(".J-pagesize").html("").append(pagesize);
					// 分页大小选择
					var that = this;
					that.element.find('.J-pagination-page select')
							.unbind("change")
							.bind(
									'change',
									function() {
										top.window.scrollTo(0, 0);
										var pageSize = parseInt(that.element.find(
												'.J-pagination-page select option:selected')
												.attr('value'));
										// 设置当前页为-1，保证当前页和要查的页不同，翻页。
										that.newSearchId = Math.uuid();
										that.CURRENT_PAGE = -1;
										that.PAGE_SIZE = pageSize;
										that.page(params, 0);
									});
					// 设置分页大小
					$('.J-pagination-page select option').each(function() {
						var temp = parseInt($(this).attr('value'));
						if (temp == that.PAGE_SIZE) {
							$(this).attr('selected', 'selected');
						}
					});
				},
				initPageBar : function(params, total) {
					var that = this;
					var pageSelector = ".J-pagination";
					var page = that.element.find(pageSelector);
					if (page.length == 0) {
						page = $(pageSelector);
					}
					that.initPageBarSize(page, params);
					var paginatonBar = page.find(".J-pagelink");
					if (total == 0) {
						paginatonBar.hide();
						return;
					} else {
						paginatonBar.show();
						$('.J-pagesize').show();
					}
					paginatonBar.pagination(total,// 总条数
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
					if(parent!=window){
						top.window.scrollTo(0, 0);
					}
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

				/**
				 * 设置查询参数
				 * 
				 * @param jsonObj
				 */
				setParams : function(jsonObj) {
					for ( var attrName in jsonObj) {
						var attrValue = jsonObj[attrName];
						this.setMapValue(this.queryParams, attrName, attrValue);
					}
					// this.queryParams = params;
				},

				initCustAttr : function() {
					EcpPageGrid.superclass.initCustAttr.apply(this, arguments);
					var pagesize = this.get("pagesize");
					if (pagesize) {
						this.PAGE_SIZE = pagesize;
					}
					this.set("queryParams",this.setParams(this.get("data")));
				},
				// ajax加载数据
				loadData : function(params) {
					if (!params) {
						params = this.queryParams;
					}
					var autoRender = this.get("autoRender");
					if (autoRender) {
						// this.setMapValue(params, "newSearchId",
						// this.newSearchId);
						// this.setMapValue(params, "oldSearchId",
						// this.oldSearchId);
						this.setMapValue(params, "newSearchId",
								this.newSearchId);
						this.setMapValue(params, "oldSearchId",
								this.oldSearchId);
						this.setMapValue(params, "pageInfo.curPageNo",
								this.CURRENT_PAGE + 1);
						this.setMapValue(params, "pageInfo.pageSize",
								this.PAGE_SIZE);
						var grid = this;
						this.element.find("tbody").html("");
						grid.setStatus("加载数据中...");
						var url = this.get("url");
						$.ajax({
							url : url,
							type:'POST',
							success : function(data) {
								if (data == null || data.records == null
										|| data.records.length == 0) {
									grid.setModel(data);
									grid.setStatus("没有查询到符合条件的数据");
									grid.initPageBar(params, 0);
								} else {
									grid.setModel(data);
									grid.initPageBar(params, data.total);
									grid.setStatus("");
								}
								if (parent) {
									parent.seajs.emit("event_grid_loaded");
								} else {
									seajs.emit("event_grid_loaded");
								}
							},
							error : function(XMLHttpRequest, textStatus,
									errorThrown, message) {
								log.error(XMLHttpRequest.responseText);
								grid.setStatus("数据加载失败！" + message.message);
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
					for ( var i = 0, length = map.length; i < length; i++) {
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
						if (form.length == 0) {
							new Error("gird查询没有找到对应的form");
						} else {
							this.queryParams = form.serializeArray();
						}
					} else if (form instanceof Object) {
						this.queryParams = [ form ];
					}
					this.CURRENT_PAGE = 0;
					this.newSearchId = Math.uuid();
					this.set("autoRender", true);
					this.loadData(this.queryParams);
				},
				bindEvent:function(){
					EcpPageGrid.superclass.bindEvent.apply(this, arguments);
					var ecppagegrid=this;
					seajs.on("ECPPAGEGRID_QUERY",function(data){
						ecppagegrid.query(data);
					});
				}
			});

	module.exports = EcpPageGrid;
});