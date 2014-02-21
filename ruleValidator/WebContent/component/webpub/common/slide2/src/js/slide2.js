
/**
 * 幻灯片
 */
define(function(require, exports, module) {

	/*
	 * 依赖
	 */
	var $ = require("$");
	var logger = require("log");
	require("../theme/default/css/slide2.css");
	var widget = require("widget");
	var SimpleSlide = widget.extend({
		afterRender : function() {
			/*
			 * 本地变量
			 */
			// 幻灯片宽度
			var pictureWidth = 600;
			// 幻灯片高度
			// var pictureHeight = 300;
			// 按钮距幻灯片底部高度
			var buttonMarginBottom = -15;
			// 按钮宽度
			var buttonWidth = 15;
			// 按钮宽度
			var buttonHeight = 15;
			// 是否自动滚动
			var isAuto = true;
			// 自动滚动时间间隔
			var timerInterval = 5000;
			// 计时器
			var timer;
			// 当前显示区域索引
			var currentIndex = 0;
			// // 商品数据
			var products = [];
			// 数据转换
			var data = [];

			/**
			 * 查询展销商品
			 */
			function query(url) {
				var value = [];
				$.ajax({
					type : 'GET',
					url : url||G.API.ONSALE_ADVERT,
					dataType : 'json',
					async : false,
					success : function(data) {
						if (data != null) {
							value = data;
						}
					},
					error : function() {
						// return false;
					}
				});
				return value;
			}

			/**
			 * 数据转换
			 */
			function translate(products) {
				if (products) {
					// 前置条件：入参的商品列表不能为空
					var resultArray = [];
					$.each(products, function(index, item) {
						if (item) {
							var temp = {
								imageUrl : (item.vimageurl ? '/'
										+ item.vimageurl : G.ecpimages
										+ '/defaultimg.jpg'),
								// imageUrl:ecpimages +
								// '/defaultimg.jpg',
								imageAlt : item.vdisplayname,
								thumbUrl : (item.vminimageurl
										? '/' + item.vminimageurl
										: G.ecpimages
												+ '/defaultimg.jpg'),
								thumbTitle : item.vdisplayname,
								thumbDesc : item.vdisplayname,
								thumbAlt : item.vdisplayname,
								imgLink : G.PAGE.PRODUCT_DETAIL+item.cproductid
							};
							resultArray.push(temp);
						}
					});
					return resultArray;
				}
			}

			/**
			 * 创建dom结构
			 */
			function create(options) {
				// 创建面板
				
				var slide = $("<div>").addClass("slideshow")
						// 图片容器
						.append($('<div>').addClass('slide_images'))
						// 按钮容器
						.append($('<div>').addClass('slide_buttons').css({
									marginTop : -(buttonMarginBottom + buttonHeight)
								}));
				options.parentNode.append(slide);
				$('<div>').addClass('slideshow_pics').css({
							width : pictureWidth * data.length,
							left : 0
						}).appendTo($('.slide_images'));
				$('<div>').addClass('align_center').css({
							width : buttonWidth * data.length
						}).append($('<div>').addClass('slide_btn').css({
							width : buttonWidth * data.length
						})).appendTo($('.slide_buttons'));

				// 遍历数据，创建幻灯片和按钮
				$(data).each(function(index, item) {
					// 创建幻灯片
					var image = '<a href="' + this.imgLink + '" index="'
							+ index + '" title="' + this.imageAlt + '" target="_blank">'
							+ '<img src="' + this.imageUrl + '" title="'
							+ this.imageAlt + '" ></img>' + '</a>';
					$('.slideshow_pics').append($(image));

					// 创建按钮
					var button = '<a href="#" class="'
							+ (index == 0 ? 'clicked' : '') + '" index="'
							+ index + '"></a>';
					$('.slide_btn').append($(button));
				});

				// 自动滚动
				if (isAuto) {
					timer = setInterval(autoSlide, timerInterval);
				}

				$('.slideshow .slide_buttons .slide_btn a').live('mouseenter',
						function() {
							// console.log('mouseenter');
							// console.log(currentIndex);
							currentIndex = parseInt($(this).attr('index'));

							if (isAuto) {
								clearInterval(timer);
							}
							$('.slideshow_pics').animate({
										left : -pictureWidth * currentIndex
									});

							$('.slide_buttons a.clicked')
									.removeClass('clicked');
							$('.slide_buttons a:eq(' + currentIndex + ')')
									.addClass('clicked');
						});

				$('.slideshow .slide_buttons .slide_btn a').live('mouseleave',
						function() {
							// console.log('mouseleave');
							// console.log(currentIndex);
							if (isAuto) {
								timer = setInterval(autoSlide, timerInterval);
							}
						});

			}

			/**
			 * 自动滚动
			 */
			function autoSlide() {
				if (currentIndex < data.length) {
					$('.slideshow_pics').animate({
								left : -pictureWidth * currentIndex
							});
				} else {
					$('.slideshow_pics').animate({
								left : 0
							});
				}
				$('.slide_buttons a.clicked').removeClass('clicked');
				$('.slide_buttons a:eq(' + currentIndex + ')')
						.addClass('clicked');
				currentIndex++;
				currentIndex = currentIndex % data.length;
			}

			var init = function(options) {
				data = query(options.attrs.url);
				// data = translate(products);
				create(options);
			}
			init(this.prop);
		}
	})
	module.exports = SimpleSlide;
});