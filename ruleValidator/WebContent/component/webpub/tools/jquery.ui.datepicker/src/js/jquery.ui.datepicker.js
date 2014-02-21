define(function(require, exports, module) {
	var moment = require("moment");
		return function(jQuery) {
			jQuery = require("./jquery.ui.datepicker.base")(jQuery);
			jQuery.datepicker.regional['zh-CN'] = {
				changeYear:true,
				changeMonth:true,
				closeText : '关闭',
				prevText : '&#x3c;上月',
				nextText : '下月&#x3e;',
				currentText : '今天',
				monthNames : ['一月', '二月', '三月', '四月', '五月', '六月', '七月',
						'八月', '九月', '十月', '十一月', '十二月'],
				monthNamesShort : ['一', '二', '三', '四', '五', '六', '七', '八',
						'九', '十', '十一', '十二'],
				dayNames : ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
				dayNamesShort : ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
				dayNamesMin : ['日', '一', '二', '三', '四', '五', '六'],
				weekHeader : '周',
				dateFormat : 'yy-mm-dd',
				firstDay : 1,
				isRTL : false,
				showMonthAfterYear : true,
				yearSuffix : '年',
				yearRange : 'c-200:c+200',
				// regional: "zh-CN",
				'showButtonPanel' : true,
				'closeText' : "清空",
				'gotoCurrent' : true,
				'beforeShow' : function(input, inst) {
					datepicker_CurrentInput = input;
					// $('button.ui-datepicker-current').hide();
					// $('button.ui-datepicker-current').addClass('hidden');
				}
			};
			jQuery.datepicker
					.setDefaults(jQuery.datepicker.regional['zh-CN']);
					
			var init=function(){
				jQuery(".datepicker").each(function(){
//					if(!jQuery(this).val()){
						var defaultDate = jQuery(this).attr("data-datepicker-defaultDate")||null;
						if (defaultDate != null) {
							if(moment(defaultDate, "YYYY-MM-DD").isValid()) {
								jQuery(this).val(defaultDate);
							} else {
								jQuery(this).val(moment().add("days",defaultDate).format("YYYY-MM-DD"));
							}
						}
//					}
				})
				/**
				 * 现在支持最小日期设置,在日历控件上设置data-datepicker-minDate属性 //示例如下
				 * //minDate 设置为0 最小日期为今天 设置为-1 最小日期为昨天 设置为-2 最小日期为前天， 设置为1
				 * 最小日期为明天 以此类推
				 */
				
				jQuery(".datepicker").live("click",function(){
					var minDate = jQuery(this).attr("data-datepicker-minDate")||null;
					var onSelectFun = jQuery(this).attr("data-datepicker-onSelect")||(function(){})();
					jQuery(this).attr({
						readOnly : true
						}).datepicker({
							minDate:minDate,
							onSelect:function(){
								eval(onSelectFun);
							}
						});
					jQuery(this).datepicker("show");
				})				
				var onCloseFun = jQuery(".datepicker").attr("data-datepicker-onClose")||(function(){})();
				jQuery(".ui-datepicker-close").live('click',
						function() {
								datepicker_CurrentInput.value = "";
								eval(onCloseFun);
						});
			}
			jQuery(function() {
				init();
			})
			seajs.on("datepicker",function(){
				init();
			});
			return jQuery.noConflict(true);
		}
	});