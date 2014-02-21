define(function(require, exports, module) {
	var DeanSign = "<object ID='PXCtl' codebase='download/js/DatechActiveX.CAB' classid='clsid:8740B6BF-2B0C-44AC-A7A4-659BD0E5CB1D'></object>"
	document.write(DeanSign);
	/**
	 * 
	 */
	function checkPw(passwd) {
		return PXCtl.checkPw(passwd);
	}
	function getSN(strUserCode) {
		return PXCtl.getSN(strUserCode);
	}
	function signmessage(strData, strUserCode) {
		var singData = PXCtl.signmessage(strData, strUserCode);
		return singData;
	}
	function SignFile(strFilePath, strUserCode) {
		var singData = PXCtl.SignFile(strFilePath, strUserCode);
		return singData;
	}
	function verfiyFile(strFilePath, signature, cert) {
		var verify = PXCtl.verfiyFile(strFilePath, signature, cert);
		return verify;
	}
	//暴露接口
	exports.signmessage = signmessage;
});