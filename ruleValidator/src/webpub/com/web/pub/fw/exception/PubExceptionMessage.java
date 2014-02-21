package com.web.pub.fw.exception;

/**
 * 异常提示信息枚举类
 * 
 * @author:sunshine
 * @date:2014-2-11 下午1:40:19
 */
public enum PubExceptionMessage {

	PRODUCT_NOT_FOUND {
		@Override
		public String getDescription() {
			return "对不起，没有找到对应的商品。";
		}
	}, // 枚举值逗号分隔

	SAMPLE {
		@Override
		public String getDescription() {
			return "出错。";
		}
	};// 枚举值结束时用分号

	// 声明抽象方法
	public abstract String getDescription();
}
