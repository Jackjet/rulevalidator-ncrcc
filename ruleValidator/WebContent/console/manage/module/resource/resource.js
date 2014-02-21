define(function(require, exports, module) {
	var $ = require('$');
	require('./css/resource.css');
	var PageGrid = require('ecppagegrid');
	var Dialog = require("dialog");
	var handlebars = require('handlebars');
	var StringUtils = require('stringUtils');
	var Notice = require('notice');
	var SecuritySelect = require('security_select');
	var grid;

	// handlebars.registerHelper('raw', function(value) {
	// // return new handlebars.SafeString(value);
	// return value;
	// });

	/**
	 * 新增
	 */
	function addResource() {
		var moduleId = $('#form  #currentModuleId').val();
		var moduleCode = $('#form #currentModuleCode').val();
		if (!moduleId) {
			Dialog.alert({
				title : '提示',
				content : '<center>请在左侧模块树上选择某个模块节点作为新增资源的所属模块</center>',
				isHtmlContent : true
			});
			return false;
		}
		if (moduleId && moduleId == 'root') {
			Dialog.alert({
				title : '提示',
				content : '<center>不能以根节点为新增资源的所属模块</center>',
				isHtmlContent : true
			});
			return false;
		}

		var record = {
			moduleId : moduleId,
			moduleCode : moduleCode,
			collection_securityLevel : {
				name : 'securityLevel',
				collection : SecuritySelect.COLLECTION_SECURITY_LEVEL,
				selected : '0'
			}
		};
		var template = require('./template/resource_add.tpl');
		var contentHtml = handlebars.compile(template)(record);
		Dialog.dialog({
			title : '新增资源',
			hasCloseTip : true,
			content : contentHtml,
			isHtmlContent : true,
			type : 'dialog',
			moveToInner : true,//添加选项让弹出框在当前页面弹出而不是top
			buttons : [ {
				name : '保存',
				isDefault : true,
				href : '#',
				method : function() {
					// 提交请求
					if (validateResource()) {
						$.ajax({
							type : "POST",
							url : G.API.RESOURCE_INSERT,
							dateType : "json",
							data : $('#form-resource-add').serialize(),
							success : function(data) {
								if (data.resourceCode && data.resourceName) {
									// seajs.emit(G.EVENT.RESOURCE_QUERY);
									Notice.show($('#resource-grid'), '添加成功');
									grid.query($('#form'));
									Dialog.close();
								} else {
									if (data.detail && data.message) {
										Notice.show($('#resource-grid'), '<span style="color:red;">' + data.message + '</span>');
										Dialog.close();
									}
								}
							}
						});
					}
					return false;
				}
			}, {
				name : '取消',
				isDefault : false,
				href : '#',
				method : function() {
					Dialog.close();
					return false;
				}
			} ]
		});
	}

	function removeResource() {
		Dialog.confirm({
			title : '确认对话框',
			content : '<center>确定要删除吗？</center>',
			isHtmlContent : true,
			moveToInner : true,
			confirm : function() {
				var selected = grid.getSelectedData();
				var pks = [];
				$(selected).each(function(i, item) {
					pks.push(item.resourceId);
				});
				$.ajax({
					type : "POST",
					url : G.API.RESOURCE_DELETE,
					dateType : "json",
					data : {
						pks : pks.join(',')
					},
					success : function(data) {
						if (data.detail && data.message) {
							Notice.show($('#resource-grid'), '<span style="color:red;">' + data.message + '</span>');
							Dialog.close();
						} else {
							Dialog.close();
							Notice.show($('#resource-grid'), '删除成功');
							grid.query($('#form'));
						}
					}
				});
				return false;
			},
			cancel : function() {
				return false;
			}
		});
	}

	function updateResource(record) {
		record.collection_securityLevel = {
			name : 'securityLevel',
			collection : SecuritySelect.COLLECTION_SECURITY_LEVEL,
			selected : record.securityLevel
		};

		var template = require('./template/resource_update.tpl');
		var contentHtml = handlebars.compile(template)(record);
		Dialog
				.dialog({
					title : '修改资源',
					hasCloseTip : true,
					content : contentHtml,
					isHtmlContent : true,
					type : 'dialog',
					moveToInner : true,
					afterRender : function() {
						$('#form-resource-update input[name=url]').attr('title', record.url).val(record.url);
					},
					buttons : [
							{
								name : '保存',
								isDefault : true,
								href : '#',
								method : function() {
									if (validateResource()) {
										$
												.ajax({
													type : "POST",
													url : G.API.RESOURCE_UPDATE,
													dateType : "json",
													data : $('#form-resource-update').serialize(),
													success : function(data) {
														if (data.resourceId && data.resourceName) {
															Notice.show($('#resource-grid'), '修改成功');
															// var selectedRow =
															// grid.getSelectedRow()[0];
															// $(selectedRow).find('td:eq(3)').html(data.resourceName);
															// $(selectedRow).find('td:eq(4)').html(data.url);
															var index = grid.getSelectedIndex();
															var model = grid.get('model');
															var records = model.records;
															records[index] = data;
															var selectedRow = grid.getSelectedRow()[0];
															$(selectedRow).find('td:eq(2)').html(
																	'<span class="ui-grid-cell" style="width:100px;" title="' + data.resourceCode + '">'
																			+ data.resourceCode + '</span>');
															$(selectedRow).find('td:eq(3)').html(
																	'<span class="ui-grid-cell" style="width:100px;" title="' + data.resourceName + '">'
																			+ data.resourceName + '</span>');
															$(selectedRow).find('td:eq(4)').html(
																	'<span class="ui-grid-cell" style="width:300px;" title="' + data.url + '">' + data.url
																			+ '</span>');
															var html = SecuritySelect
																	.getProgressBar({
																		value : data.securityLevel,
																		parser : function(color, width) {
																			return StringUtils
																					.format(
																							'<span class="ui-progress-column" style="background-color:rgb(%s, %s, %s); width:%spx;">&nbsp;</span>',
																							[ color.r, color.g, color.b, width ]);
																		}
																	});
															$(selectedRow).find('td:eq(5)').html(html);
															Dialog.close();
														}
														if (data.message && data.detail) {
															Notice.show($('#resource-grid'), '<span style="color:red;">' + data.message + '</span>');
															Dialog.close();
														}
													}
												});
									}
									return false;
								}
							}, {
								name : '取消',
								isDefault : false,
								href : '#',
								method : function() {
									Dialog.close();
									return false;
								}
							} ]
				});

	}

	/**
	 * 资源校验
	 */
	function validateResource() {
		return true;
	}

	function create(options) {
		// 创建grid
		grid = new PageGrid({
			attrs : {
				isPagination : true,
				ismulti : true,
				// issingle: true,
				url : G.API.RESOURCE_QUERY_PAGE,
				columns : [
						// {
						// label : "ID",
						// key : "resourceId",
						// width : 100,
						// align : "left"
						// },
						{
							label : "编码",
							key : "resourceCode",
							width : 100,
							align : "left",
							format : function(value, data) {
								var html = '<span class="ui-grid-cell" style="width:100px;" title="' + value + '">' + value + '</span>';
								return html;
							}
						},
						{
							label : "名称",
							key : "resourceName",
							width : 100,
							align : "left",
							format : function(value, data) {
								var html = '<span class="ui-grid-cell" style="width:100px;" title="' + value + '">' + value + '</span>';
								return html;
							}
						},
						{
							label : "链接",
							key : "url",
							width : 300,
							align : "left",
							format : function(value, data) {
								var html = '<span class="ui-grid-cell" style="width:300px;" title="' + value + '">' + value + '</span>';
								// html.replace('\'\'', '\'"');
								return html;
							}
						},
						{
							label : "安全级别",
							key : "securityLevel",
							width : 100,
							align : "left",
							format : function(value, data) {
								var html = SecuritySelect.getProgressBar({
									value : value,
									parser : function(color, width) {
										return StringUtils.format(
												'<span class="ui-progress-column" style="background-color:rgb(%s, %s, %s); width:%spx;" title="' + value
														+ '">&nbsp;</span>', [ color.r, color.g, color.b, width ]);
									}
								});
								return html;
							}
						} ],
			},
			renderTo : options.containerId,
			autoRender : true
		});

		// 绑定事件
		seajs.on(G.EVENT.RESOURCE_QUERY, function(data) {
			if (data) {
				grid.query({
					name : 'moduleId',
					value : data.moduleId
				});
			} else {
				grid.query($('#form'));
			}
		});

		// 查询
		$('#button-query').live('click', function() {
			grid.query($('#form'));
		});

		// 重置
		$('#button-reset').live('click', function() {
			$('#form').get(0).reset();
			grid.query($('#form'));
		});

		// 隐藏和展现
		// $('.ui-container fieldset > legend').live('dblclick', function() {
		// $(this).next('div').slideToggle();
		// });

		// 新增资源
		$('#button-resource-add').live('click', addResource);
		// 修改资源
		$('#button-resource-update').live('click', function() {
			if (grid.getSelectedRow().length == 0) {
				Dialog.alert({
					title : '提示',
					content : '<span style="display:inline-block;width:400px; text-align:center">请选择一行记录</span>',
					isHtmlContent : true
				});
				return;
			}
			if (grid.getSelectedRow().length > 1) {
				Dialog.alert({
					title : '提示',
					content : '<span style="display:inline-block; width:400px; text-align:center">每次只能修改一行记录</span>',
					isHtmlContent : true
				});
				return;
			}
			var record = grid.getSelectedData()[0];
			updateResource(record);
		});
		// 删除资源
		$('#button-resource-remove').live('click', function(){
			if (grid.getSelectedRow().length == 0) {
				Dialog.alert({
					title : '提示',
					content : '<span style="display:inline-block;width:400px; text-align:center">请选择一行记录</span>',
					isHtmlContent : true
				});
				return;
			}
			else{
				removeResource();
			}
		});

		// 下拉框
		$('.ui-select').live('click', function() {
			// $(this).find('option').hide();
			var width = $(this).width();
			$(this).next('.ui-options').width(width);
			$(this).next('.ui-options').toggle();
			var coordinate = $(this).offset();
			coordinate.top += $(this).height();
			$(this).next('.ui-options').offset(coordinate);
		});

		$('.ui-options li').live('mouseenter', function() {
			$(this).find('span').addClass('active');
		});

		$('.ui-options li').live('mouseleave', function() {
			$(this).find('span').removeClass('active');
		});

		$('.ui-options li').live('click', function() {
			var value = $(this).attr('value');
			var option = $('<option>').val(value).attr('selected', 'selected').text(value);
			$(this).parents('.ui-options').prev('select').attr('value', value);
			$(this).parents('.ui-options').prev('select').empty().append(option);
			// $(this).parents('.ui-options').prev('select > option').show();
			$(this).parents('.ui-options').hide();
		});

		// seajs.on(G.EVENT.RESOURCE_SET_QUERY_PARAM, function(params) {
		// for (var name in params) {
		// var value = params[name];
		// $('#form').find('input[name=' + name + ']').val(value);
		// }
		// });
	}

	exports.create = create;
});