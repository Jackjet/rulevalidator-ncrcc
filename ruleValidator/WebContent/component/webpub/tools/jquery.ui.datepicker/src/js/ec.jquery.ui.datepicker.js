define(function(require, exports, module) {
	var msgClose="关闭";
	var msgPreMonth="上月";//上月
	var msgNextMonth="";//下月
	var msgToday="下月";//今天
	var msgWeek="周";//周
	var msgYear="年";//年
	var msgClear="清空";//清空
	
	var msgMonth1="一月";//一月
	var msgMonth2="二月";//二月
	var msgMonth3="三月";//三月
	var msgMonth4="四月";//四月
	var msgMonth5="五月";//五月
	var msgMonth6="六月";//六月
	var msgMonth7="七月";//七月
	var msgMonth8="八月";//八月
	var msgMonth9="九月";//九月
	var msgMonth10="十月";//十月
	var msgMonth11="十一月";//十一月
	var msgMonth12="十二月";//十二月
	
	var msg1="一";//一
	var msg2="二";//二
	var msg3="三";//三
	var msg4="四";//四
	var msg5="五";//五
	var msg6="六";//六
	var msg7="七";//七
	var msg8="八";//八
	var msg9="九";//九
	var msg10="十";//十
	var msg11="十一";//十一
	var msg12="十二";//十二
	
	var msgWeek10="星期日";//星期日
	var msgWeek11="星期一";//星期一
	var msgWeek12="星期二";//星期二
	var msgWeek13="星期三";//星期三
	var msgWeek14="星期四";//星期四
	var msgWeek15="星期五";//星期五
	var msgWeek16="星期六";//星期六
	
	var msgWeek20="周日";//周日
	var msgWeek21="周一";//周一
	var msgWeek22="周二";//周二
	var msgWeek23="周三";//周三
	var msgWeek24="周四";//周四
	var msgWeek25="周五";//周五
	var msgWeek26="周六";//周六
	
	var msgWeek30="日";//日
	var msgWeek31="一";//一
	var msgWeek32="二";//二
	var msgWeek33="三";//三
	var msgWeek34="四";//四
	var msgWeek35="五";//五
	var msgWeek36="六";//六
	try{
		if(typeof(eval("getLanMsg")) == "function"){
			msgClose= getLanMsg("0ec22400-000112");//关闭
			msgPreMonth= "&#x3c;"+getLanMsg("0ec22400-000113");//上月
			msgNextMonth= getLanMsg("0ec22400-000114")+"&#x3e;";//下月
			msgToday= getLanMsg("0ec22400-000115");//今天
			msgWeek= getLanMsg("0ec22400-000116");//周
			msgYear= getLanMsg("0ec22400-000117");//年
			msgClear= getLanMsg("0ec22400-000118");//清空
			
			msgMonth1= getLanMsg("0ec22400-000121");//一月
			msgMonth2= getLanMsg("0ec22400-000122");//二月
			msgMonth3= getLanMsg("0ec22400-000123");//三月
			msgMonth4= getLanMsg("0ec22400-000124");//四月
			msgMonth5= getLanMsg("0ec22400-000125");//五月
			msgMonth6= getLanMsg("0ec22400-000126");//六月
			msgMonth7= getLanMsg("0ec22400-000127");//七月
			msgMonth8= getLanMsg("0ec22400-000128");//八月
			msgMonth9= getLanMsg("0ec22400-000129");//九月
			msgMonth10= getLanMsg("0ec22400-000130");//十月
			msgMonth11= getLanMsg("0ec22400-000131");//十一月
			msgMonth12= getLanMsg("0ec22400-000132");//十二月
			
			msg1= getLanMsg("0ec22400-000141");//一
			msg2= getLanMsg("0ec22400-000142");//二
			msg3= getLanMsg("0ec22400-000143");//三
			msg4= getLanMsg("0ec22400-000144");//四
			msg5= getLanMsg("0ec22400-000145");//五
			msg6= getLanMsg("0ec22400-000146");//六
			msg7= getLanMsg("0ec22400-000147");//七
			msg8= getLanMsg("0ec22400-000148");//八
			msg9= getLanMsg("0ec22400-000149");//九
			msg10= getLanMsg("0ec22400-000150");//十
			msg11= getLanMsg("0ec22400-000151");//十一
			msg12= getLanMsg("0ec22400-000152");//十二
			
			msgWeek10= getLanMsg("0ec22400-000160");//星期日
			msgWeek11= getLanMsg("0ec22400-000161");//星期一
			msgWeek12= getLanMsg("0ec22400-000162");//星期二
			msgWeek13= getLanMsg("0ec22400-000163");//星期三
			msgWeek14= getLanMsg("0ec22400-000164");//星期四
			msgWeek15= getLanMsg("0ec22400-000165");//星期五
			msgWeek16= getLanMsg("0ec22400-000166");//星期六
			
			msgWeek20= getLanMsg("0ec22400-000170");//周日
			msgWeek21= getLanMsg("0ec22400-000171");//周一
			msgWeek22= getLanMsg("0ec22400-000172");//周二
			msgWeek23= getLanMsg("0ec22400-000173");//周三
			msgWeek24= getLanMsg("0ec22400-000174");//周四
			msgWeek25= getLanMsg("0ec22400-000175");//周五
			msgWeek26= getLanMsg("0ec22400-000176");//周六
			
			msgWeek30= getLanMsg("0ec22400-000180");//日
			msgWeek31= getLanMsg("0ec22400-000181");//一
			msgWeek32= getLanMsg("0ec22400-000182");//二
			msgWeek33= getLanMsg("0ec22400-000183");//三
			msgWeek34= getLanMsg("0ec22400-000184");//四
			msgWeek35= getLanMsg("0ec22400-000185");//五
			msgWeek36= getLanMsg("0ec22400-000186");//六
		}
	}catch(e){
		try{
			if(typeof(eval("getLanMsgs")) == "function"){
				msgClose= getLanMsgs("0ec22180-200112");//关闭
				msgPreMonth= "&#x3c;"+getLanMsgs("0ec22180-200113");//上月
				msgNextMonth= getLanMsgs("0ec22180-200114")+"&#x3e;";//下月
				msgToday= getLanMsgs("0ec22180-200115");//今天
				msgWeek= getLanMsgs("0ec22180-200116");//周
				msgYear= getLanMsgs("0ec22180-200117");//年
				msgClear= getLanMsgs("0ec22180-200118");//清空
				
				msgMonth1= getLanMsgs("0ec22180-200121");//一月
				msgMonth2= getLanMsgs("0ec22180-200122");//二月
				msgMonth3= getLanMsgs("0ec22180-200123");//三月
				msgMonth4= getLanMsgs("0ec22180-200124");//四月
				msgMonth5= getLanMsgs("0ec22180-200125");//五月
				msgMonth6= getLanMsgs("0ec22180-200126");//六月
				msgMonth7= getLanMsgs("0ec22180-200127");//七月
				msgMonth8= getLanMsgs("0ec22180-200128");//八月
				msgMonth9= getLanMsgs("0ec22180-200129");//九月
				msgMonth10= getLanMsgs("0ec22180-200130");//十月
				msgMonth11= getLanMsgs("0ec22180-200131");//十一月
				msgMonth12= getLanMsgs("0ec22180-200132");//十二月
				
				msg1= getLanMsgs("0ec22180-200141");//一
				msg2= getLanMsgs("0ec22180-200142");//二
				msg3= getLanMsgs("0ec22180-200143");//三
				msg4= getLanMsgs("0ec22180-200144");//四
				msg5= getLanMsgs("0ec22180-200145");//五
				msg6= getLanMsgs("0ec22180-200146");//六
				msg7= getLanMsgs("0ec22180-200147");//七
				msg8= getLanMsgs("0ec22180-200148");//八
				msg9= getLanMsgs("0ec22180-200149");//九
				msg10= getLanMsgs("0ec22180-200150");//十
				msg11= getLanMsgs("0ec22180-200151");//十一
				msg12= getLanMsgs("0ec22180-200152");//十二
				
				msgWeek10= getLanMsgs("0ec22180-200160");//星期日
				msgWeek11= getLanMsgs("0ec22180-200161");//星期一
				msgWeek12= getLanMsgs("0ec22180-200162");//星期二
				msgWeek13= getLanMsgs("0ec22180-200163");//星期三
				msgWeek14= getLanMsgs("0ec22180-200164");//星期四
				msgWeek15= getLanMsgs("0ec22180-200165");//星期五
				msgWeek16= getLanMsgs("0ec22180-200166");//星期六
				
				msgWeek20= getLanMsgs("0ec22180-200170");//周日
				msgWeek21= getLanMsgs("0ec22180-200171");//周一
				msgWeek22= getLanMsgs("0ec22180-200172");//周二
				msgWeek23= getLanMsgs("0ec22180-200173");//周三
				msgWeek24= getLanMsgs("0ec22180-200174");//周四
				msgWeek25= getLanMsgs("0ec22180-200175");//周五
				msgWeek26= getLanMsgs("0ec22180-200176");//周六
				
				msgWeek30= getLanMsgs("0ec22180-200180");//日
				msgWeek31= getLanMsgs("0ec22180-200181");//一
				msgWeek32= getLanMsgs("0ec22180-200182");//二
				msgWeek33= getLanMsgs("0ec22180-200183");//三
				msgWeek34= getLanMsgs("0ec22180-200184");//四
				msgWeek35= getLanMsgs("0ec22180-200185");//五
				msgWeek36= getLanMsgs("0ec22180-200186");//六
			}
		}catch(e){
			
		}
	}
	if("" == msgClear || "null" == msgClear || null == msgClear){
		
	}
	var moment = require("moment");
		return function(jQuery) {
			jQuery = require("./jquery.ui.datepicker.base")(jQuery);
			jQuery.datepicker.regional['zh-CN'] = {
				changeYear:true,
				changeMonth:true,
				closeText : msgClose,//'关闭',
				prevText : msgPreMonth,//'&#x3c;上月',
				nextText : msgNextMonth,//'下月&#x3e;',
				currentText : msgToday,//'今天',
				monthNames : [msgMonth1, msgMonth2, msgMonth3, msgMonth4, msgMonth5, msgMonth6, msgMonth7,
						msgMonth8, msgMonth9, msgMonth10, msgMonth11, msgMonth12],
				monthNamesShort : [msg1, msg2, msg3, msg4, msg5, msg6, msg7, msg8,
						msg9, msg10, msg11, msg12],
				dayNames : [msgWeek10, msgWeek11, msgWeek12, msgWeek13, msgWeek14, msgWeek15, msgWeek16],
				dayNamesShort : [msgWeek20, msgWeek21, msgWeek22, msgWeek23, msgWeek24, msgWeek25, msgWeek26],
				dayNamesMin : [msgWeek30, msgWeek31, msgWeek32, msgWeek33, msgWeek34, msgWeek35, msgWeek36],
				weekHeader : msgWeek,//'周',
				dateFormat : 'yy-mm-dd',
				firstDay : 1,
				isRTL : false,
				showMonthAfterYear : true,
				yearSuffix : "",//'年',
				yearRange : 'c-200:c+200',
				// regional: "zh-CN",
				'showButtonPanel' : true,
				'closeText' : msgClear,//"清空",
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