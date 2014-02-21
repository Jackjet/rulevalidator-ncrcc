package com.web.pub.codename.iquery;

import com.web.pub.comp.combobox.IComboBoxModel;
import com.web.pub.fw.exception.PubBaseException;

public interface IQueryCodeName {

	/**
	 * 
	 * 
	 * @param param
	 * @return
	 * @throws EcpBaseException
	 */
	public IComboBoxModel getCodeNameModel(String param)
			throws PubBaseException;
}
