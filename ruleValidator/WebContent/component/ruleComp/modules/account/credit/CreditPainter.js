define(function(require, exports, module) {
	var $ = require('$');
	var theme = seajs.config.data.vars.theme;
	var Highcharts = require("highcharts");

	// 颜色
	var colors = {
		positive : {
			'default' : '#6ea2c8',
			grace : '#de5d8c'
		},
		negative : {
			'default' : '#bd8021',
			grace : '#bd8021'
		}
	};

	/**
	 * 信用额度画笔
	 * 
	 * @param {}
	 *            options
	 */
	function CreditPainter() {

		/**
		 * 将数字格式化成字符串（没三位数字加一个逗号）
		 * 
		 * @param num
		 */
		function formatNumber(source) {

			var string = source.toString();
			// 整数部分
			var integer = '';
			// 小数部分
			var decimal = '';
			if (string.indexOf('.') > -1) {
				integer = string.split('.')[0];
				decimal = string.split('.')[1];
			} else {
				integer = string;
				decimal = '00';
			}

			var integerArray = integer.split('').reverse();
			var integerArraySplited = [];
			for ( var i = 0; i < integerArray.length; i++) {
				integerArraySplited.push(integerArray[i]);
				if ((i + 1) < integerArray.length && (i + 1) % 3 == 0) {
					integerArraySplited.push(',');
				}
			}

			return integerArraySplited.reverse().join('') + '.' + decimal;
		}

		/**
		 * 转换
		 * 
		 * @param chartType
		 *            图表类型
		 */
		function translate(options) {

			// 颜色
			// 正值颜色
			var positiveColor = colors.positive[theme];
			// 负值颜色
			var negativeColor = colors.negative[theme];

			// 原始数据
			var metadata = options.metadata;
			// 容器id
			var containerId = options.containerId;
			// 图表类型
			var chartType = options.chartType;
			// 标题
			var title = options.title;

			// 销售组织
			var organName = metadata.pkOrg_name;
			// 渠道类型
			var channel = metadata.cchanneltypeids_names;
			// 产品线
			var line = metadata.cprodlineids_names;
			// 额度
			var a = parseFloat(metadata.nlimitmny$);
			var _a = metadata.nlimitmny$;
			// 余额
			var b = parseFloat(metadata.nbalancemny$);
			var _b = metadata.nbalancemny$;
			// 占用
			var c = parseFloat(metadata.nengrossmny$);
			var _c = metadata.nengrossmny$;
			// 币种
			var currencySign = metadata.custcurrencyid_curSign;
			// 币种名称
			var currencyName = metadata.custcurrencyid_curName;

			// 图表数据
			var chartData = {
				chart : {
					type : 'column',
					renderTo : containerId
				},
				title : {
					text : title
				},
				credits : {
					enabled : false
				},
				plotOptions : {
					column : {
						color : metadata.nbalancemny > 0 ? positiveColor : negativeColor,
						dataLabels : {
							enabled : true,
							formatter : function() {
								return '余额：' + currencySign + ' ' + metadata.nbalancemny$;
							}
						}
					}
				},
				tooltip : {
					formatter : function() {
						return '<b>信用控制域</b>：' + organName + '<br>' + '<b>渠道类型</b>：' + this.x + '<br>' + '<b>产品线</b>：' + this.series.name + '<br>' + '<b>信用额度</b>：' + currencySign + ' ' + _a + '<br>'
								+ '<b>信用余额</b>：' + currencySign + ' ' + _b;
					}
				},
				yAxis : {
					// min: 0,
					title : {
						text : '信用余额（' + currencyName + '）',
						align : 'high'
					},
					labels : {
						overflow : 'justify'
					}
				},
				legend : {
					layout : 'vertical',
					align : 'right',
					verticalAlign : 'top',
					// x: -100,
					y : 50,
					floating : true,
					borderWidth : 1,
					backgroundColor : '#FFFFFF',
					shadow : true
				}
			};

			// 补充图表数据
			var xAxis = {
				categories : []
			};

			if (channel) {
				$(channel).each(function(i, item) {
					if (item) {
						xAxis.categories.push(item);
					}
				});
			} else {
				xAxis.categories.push('全部渠道类型');
			}
			chartData.xAxis = xAxis;

			var series = [];
			if (line) {
				$(line).each(function(i, item) {
					if (item) {
						var dataArray = [];
						if (channel) {
							$(channel).each(function(j, item2) {
								dataArray.push(b);
							});
							series.push({
								name : item,
								data : dataArray
							});
						} else {
							series.push({
								name : item,
								data : [ b ]
							});
						}
					}
				});
			} else {
				var dataArray = [];
				if (channel) {
					$(channel).each(function(j, item2) {
						dataArray.push(b);
					});
					series.push({
						name : '全部产品线',
						data : dataArray
					});
				} else {
					series.push({
						name : '全部产品线',
						data : [ b ]
					});
				}
			}
			chartData.series = series;

			$.extend(chartData, metadata);
			return chartData;
		}

		/**
		 * 画图
		 */
		this.draw = function(options) {
			// 元数据（数组）
			var metadatas = options.creditArray;
			// 容器id
			var containerId = options.containerId;
			// 是否联动trigger按钮
			var isTrigger = options.isTrigger;

			// 整个图表的高度
			var height = metadatas.length * 180;
			$('#' + containerId).css({
				height : height + 'px'
			});
			// 容器
			var container = $('#' + containerId);

			// 每个信用额度创建一个图表
			$(metadatas).each(function(i, metadata) {

				// // 删除重复的容器
				// container.remove($('#creditChart_' + i));

				// 创建子容器
				container.append('<div id="' + containerId + '_creditChart_' + i + '" style="width:800px; height:180px;"></div>');

				// 组织数据
				var options = {
					metadata : metadata,
					chartType : 'column',
					containerId : containerId + '_creditChart_' + i,
					title : '销售组织：' + (metadata.csaleorgid_name ? metadata.csaleorgid_name : '全部销售组织')
				};
				var chartData = translate(options);
				// 画图
				new Highcharts.Chart(chartData);

			});
			if (height > 900) {
				$('body').css({
					height : height + 40 + 'px'
				});
			}
			top.seajs.emit('iframe_loaded');

			// 由于ie下没有解决自动收缩问题，增加动作脚本，关联收缩按钮
			if (!!isTrigger) {
				if (!!navigator.userAgent.match('MSIE')) {
					// 收缩按钮
					var trigger = $('#creditChart_0').parents('.J-credit').find('.trigger');
					if (trigger.hasClass('down')) {
						$('#' + containerId).find('div[id^=creditChart_]').css({
							display : 'none'
						});
					}
					trigger.click(function() {
						if ($(this).hasClass('down')) {
							$(this).parents('.J-credit').find('div[id^=creditChart]').show();
						} else {
							$(this).parents('.J-credit').find('div[id^=creditChart]').hide();
						}
					});
				}
			}
		};
	}

	module.exports = CreditPainter;
});