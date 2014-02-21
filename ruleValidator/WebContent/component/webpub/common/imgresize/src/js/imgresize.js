/*
 * 组件示例
 */

define(function(require, exports, module) {
	var $ = require("$");
			// 图片按比例缩放
			var flag = false;
			function resizeImage(image,ImgD, w, h) {
				var iwidth = w; // 定义允许图片宽度，当宽度大于这个值时等比例缩小
				var iheight = h; // 定义允许图片高度，当宽度大于这个值时等比例缩小
				if (image.width > 0 && image.height > 0) {
					flag = true;
					if (image.width / image.height >= iwidth / iheight) {
						if (image.width > iwidth) {
							ImgD.width = iwidth;
							ImgD.height = (image.height * iwidth) / image.width;
						} else {
							ImgD.width = image.width;
							ImgD.height = image.height;
						}

					} else {
						if (image.height > iheight) {
							ImgD.height = iheight;
							ImgD.width = (image.width * iheight) / image.height;
						} else {
							ImgD.width = image.width;
							ImgD.height = image.height;
						}
					}
				}
				ImgD.style.top = (h - ImgD.height) / 2 + "px";
				ImgD.style.left = (w - ImgD.width) / 2 + "px";
			}
			function resize(ImgD, w, h){
				var image = new Image();
				image.src = ImgD.src;
				if($.browser.msie){
					resizeImage(image,ImgD, w, h);
				}else{
					image.onload = function(){
						if(image.complete == true){
							 resizeImage(image,ImgD, w, h);
						}
					}
				}
				image.onerror = function(){
//					ImgD.src = G.ecpimages+"/defaultimg.jpg";
//					resize(ImgD, w, h);
				}
			}
			exports.resize = resize;
		});