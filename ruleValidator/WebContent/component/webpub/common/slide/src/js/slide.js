/*
 * 组件示例
 */

define(function(require, exports, module) {
			var $ = require("$");
			var logger = require("log");
			// 自定义：组件对应的css
			require("../theme/default/css/slide.css");
			var ImgResize = require("imgresize");

			/**
			 * 广告幻灯片
			 */
			var Advert = function(option) {
				var url = "";
				if (option.url) {
					url = option.url;
				} else {
					logger.error("slide组件url参数不能为空");
					return;
				}
				$.getJSON(url, function(json) {
					
				});
				$.ajax({
				   type: "GET",
				   url: url,
				   success: function(json){
				   	if(json.message ==null){
					     createSlideShow(json);
				   	}else{
				   		 logger.error(json.detail);
				   	}
				   },
				   error:function(msg){
				   	  logger.error(msg);
				   }
				});
				
				
				logger.error('this is error')
	

				// var blankImageUrl = ecpimages + '/s.gif';
				// 总幻灯片数
				var totalPictrue = 0;
				// 下一个应该展示的幻灯序号（也是下一个幻灯片所对应的缩略图序号）
				var slideIndex = 0;
				// 每组个数
				var groupNum = 4;
				// 缩略图一组宽度
				var thumbViewportWidth = 614;
				// 当前缩略图序号
				// var currentThumbIndex = 0;
				// // 下一个幻灯片序号
				// var nextSlideIndex = 0;
				// // 自动滑动定时器
				// var slideIntervalTimer = null;
				// // 自动滑动定时器
				// var autoSlideTimer = null;
				// 自动滑动间隔
				var intervalTime = 5000;
				// 幻灯片宽度
				var slidePictureWidth = 614;
				// 幻灯片高度
				var slidePictureHeight = 230;
				// // 是否自动滑动
				// var isAutoSlide = true;

				// 自动滚动定时器
				var slideTimer = null;
				// var intervalTimer = null;

				/**
				 * 选择器
				 * 
				 * @type
				 */
				var selector = {
					// 幻灯片展示区
					slideZone : '.slideshow'
				};

				/**
				 * 动态生成首页幻灯片
				 * 
				 * @param {}
				 *            slideShowData 幻灯片数据对象
				 * @param {}
				 *            auto 自动滚动[true | false]
				 */
				var createSlideShow = function(slideShowData, isAutoSlide) {
					// 初始化总幻灯片个数
					totalPictrue = slideShowData.length;
					// 幻灯片区域
					var slideZone = $(selector.slideZone);
					slideZone.html('');
					// 图片区
					var pictureZone = $('<div></div>')
							.addClass('slideshow_pics').css({
								width : (slideShowData.length + 1)
										* slidePictureWidth
							});
					// 缩略图区
					var thumbZone = $('<div></div>')
							.addClass('slideshow_thumbs');
					// 缩略图容器
					var thumbsContainer = $('<div></div>')
							.addClass('thumbs_container').css({
										width : (slideShowData.length + 1)
												* 154
									});
					// 挂载
					slideZone.append(pictureZone);
					slideZone.append(thumbZone);
					thumbZone.append(thumbsContainer);

					// 生成幻灯片和缩略图
					$(slideShowData).each(function(index) {
								createPictureAndThumb(slideShowData, index);
							});

					// 鼠标进入缩略图，更改计数器
					$('.container_item a').bind('mouseenter', function() {
								// 停止自动滑动定时任务
								if (slideTimer) {
									clearInterval(slideTimer);
									slideTimer = null;
								}

								// if (intervalTimer) {
								// clearTimeout(intervalTimer);
								// intervalTimer = null;
								// }

								// 修改幻灯片索引
								slideIndex = $(this).parent().attr('id')
										.split('_')[1];

								// 滑动
								slide();
							});

					// 鼠标离开，自动滑动
					$('.container_item a').bind('mouseleave', function() {
						// 创建自动滑动定时任务
						slideTimer = setInterval(slide, intervalTime);
							// intervalTimer = setTimeout(function(){
							// alert(1);
							// slideTimer = setInterval(slide, intervalTime);
							// }, 3000);
						});

					// 创建定时任务，持续滚动
					slideTimer = setInterval(slide, intervalTime);
					// 增加图片等比例缩放
					// 商品展示列表
					$(".slideshow_pics img").load(function() {
						this.onload = ImgResize.resize(this, 614,
								230);
					});
					$(".container_item img").load(function() {
						this.onload = ImgResize.resize(this, 150,
								80);
					});
				};

				/**
				 * 创建幻灯片和缩略图
				 * 
				 * @param {}
				 *            dataArray 幻灯片数据
				 * @param {}
				 *            index 序号
				 */
				function createPictureAndThumb(dataArray, index) {
					// 图片区
					var pictureZone = $('.slideshow_pics');
					// 缩略图容器
					var thumbsContainer = $('.thumbs_container');
					// 当前图片数据
					var data = dataArray[index];
					// 生成幻灯片大图
					var picture = '<a id="picture_"' + index + '" href="'
							+ data.imgLink + '">' + '<div style="width:'
							+ slidePictureWidth + 'px; height:'
							+ slidePictureHeight
							+ 'px;" class="nc-div-blank-bg">' + '<img src="'
							+ data.imageUrl + '" alt="' + data.imageAlt + '">'
							+ '</div>' + '</a>';
					pictureZone.append(picture);

					// 生成幻灯片缩略图
					var thumbItem = $('<div></div>').attr({
								id : "thumb_" + index
							}).addClass("container_item")
							.appendTo(thumbsContainer);
					// 遮罩
					var mask = $('<a></a>').attr({
								href : data.imgLink
							}).addClass("desc_mask")
							// .bind('mouseenter', slideThumb)
							.append($('<span></span>').html(data.thumbTitle
									+ " " + index)).append(data.thumbDesc)
							.appendTo(thumbItem);
					index == 0 ? $(mask).addClass("over_up") : $(mask)
							.addClass("recover");
					$('<div></div>').addClass("clear").appendTo(thumbItem);
					$('<span></span>').addClass("icon_arrow_up")
							.addClass(index == 0 ? "thumb_active" : "")
							.appendTo(thumbItem);
					$('<img />').attr({
								src : data.thumbUrl,
								alt : data.thumbAlt
							}).appendTo(thumbItem);
				}

				/**
				 * 滑动
				 * 
				 * @description 用来组合幻灯片滑动和缩略图滑动，同时修改计数器
				 */
				function slide() {
					logger.info('slide() & slideIndex = ' + slideIndex);
					// 滑动缩略图
					slideThumb();
					// 滑动幻灯片
					slidePicture();
					// 计数器加一
					slideIndex++;
					if (slideIndex == totalPictrue) {
						// 计数器超过最大照片数量 => 归位
						slideIndex = 0;
					}
				}

				/**
				 * 滑动到某张幻灯片
				 */
				function slidePicture() {

					logger.info('slidePicture() & slideIndex = ' + slideIndex);

					if (slideIndex == 0) {
						// 最后一张，将第一张 => 从最后一页跳到第一页会移动比较多的页面
						// 移动幻灯片
						$('.slideshow_pics').stop().css({
									marginLeft : -slidePictureWidth
								}).animate({
									marginLeft : 0
								});
					} else {
						$('.slideshow_pics').stop().animate({
									marginLeft : -slidePictureWidth
											* slideIndex
								});
					}
				}

				/**
				 * 改变缩略图特效
				 * 
				 * @description 特效包括：当前幻灯片箭头、缩略图遮罩
				 * @param {}
				 *            thumb
				 */
				function changeThumb(thumb) {

					logger.info('changeThumb() slideIndex = ' + slideIndex);
					// 应该展现的缩略图
					// var thumb = $('#thumb_' + slideIndex);

					// 切换箭头
					$('.thumb_active').removeClass('thumb_active');
					thumb.children('span.icon_arrow_up')
							.addClass('thumb_active');

					/*
					 * mask动画效果因为不精准而停止 $('.over_up').animate({marginTop:35},
					 * function() {
					 * $(this).removeClass('over_up').addClass('recover'); });
					 * thumb.children('.desc_mask').animate({marginTop:7},
					 * function() {
					 * $(this).removeClass('recover').addClass('over_up'); });
					 */
					$('.over_up').removeClass('over_up').addClass('recover');
					thumb.children('a.desc_mask').removeClass('recover')
							.addClass('over_up');
				}

				/**
				 * 滑动缩略图
				 */
				function slideThumb() {
					logger.info('slideThumb() & slideIndex = ' + slideIndex);

					// 应该展现的缩略图
					var thumb = $('#thumb_' + slideIndex);

					// =============================
					// 缩略图每4个为一组，整体移动到下一组
					// =============================
					// 组号
					var group = slideIndex / groupNum;
					// 该组第一个序号
					var groupFirst = slideIndex % groupNum;

					if (slideIndex != 0 && groupFirst == 0) {
						// 当前序号不是第一个 & 下一组的第一个元素
						// => 整体移动到下一组
						var thumbsOffLeft = -thumbViewportWidth * group;
						$('.thumbs_container').animate({
									marginLeft : thumbsOffLeft
								}, function() {
								});
					} else if (slideIndex == 0) {
						$('.thumbs_container').animate({
									marginLeft : 0
								}, function() {
								});
					}
					changeThumb(thumb);
				}
			}

			module.exports = Advert;
		});