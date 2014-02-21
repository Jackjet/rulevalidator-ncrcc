package com.web.rule.page.paravo;

import java.io.Serializable;

import com.web.pub.page.handler.PubQryPara;

public class StaticQryPara extends PubQryPara implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2014-1-7 обнГ2:02:31
	 */
	private static final long serialVersionUID = -199990658583469018L;

	private String recoredID;

	public String getRecoredID() {
		return recoredID;
	}

	public void setRecoredID(String recoredID) {
		this.recoredID = recoredID;
	}

}
