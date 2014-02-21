
	var NetSignClassID = "<OBJECT ID=\"NetSign\" CLASSID=\"CLSID:E65C964D-6E7D-4489-8266-FB08C1428540\" width=\"0\" height=\"0\"></OBJECT>";
	document.write(NetSignClassID);
	function getSN(arrayIssuerDN) {
		var SN;
		if (arrayIssuerDN) {

			SN = NetSign.getSN(arrayIssuerDN);
			// SN=SN.replace(" ","");
			// 替换所有的空格
			SN = SN.replace(/\ /g, "");
			if (NetSign.errorNum != 0) {
				// alert("返回值〔"+NetSign.errorNum+"〕\n"+NetSign.errMsg);
				return;
			}
		} else {
			alert("输入为空");
			return false;
		}
		return SN;
	}

	function signmessage(strData, strUserCode) {
		var signtxt;
		if ((strData) && (strUserCode)) {
			signtxt = NetSign.signmessage(strData, strUserCode);

			if (NetSign.errorNum != 0) {
				alert("返回值〔" + NetSign.errorNum + "〕\n" + NetSign.errMsg);
				return;
			}
		} else {
			alert("输入为空");
			return false;
		}

		return signtxt;
	}

	function SignFile(strFilePath, strUserCode) {
		var signtxt;
		if ((strFilePath) && (strUserCode)) {
			signtxt = NetSign.SignFile(strFilePath, strUserCode);
			if (NetSign.errorNum != 0) {
				alert("返回值〔" + NetSign.errorNum + "〕\n" + NetSign.errMsg);
				return;
			}
		} else {
			alert("输入为空");
			return false;
		}
		return signtxt;

	}

	function verfiyFile(strFilePath, signature, cert) {
		var ret;
		if ((strFilePath) && (signature) && (cert)) {
			ret = NetSign.verfiyFile(strFilePath, signature, cert);

		} else {
			ret = "-1";
		}
		if (ret == 0) {
			return true;
		} else {
			return false;
		}

	}
