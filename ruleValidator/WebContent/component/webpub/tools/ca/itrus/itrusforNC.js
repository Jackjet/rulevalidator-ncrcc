var itruspta = "<object id='iTrusPTA' codebase='/ecp/themes/default/scripts/ca/itrus/itruscertctl.cab#version=2,5,3,0' classid='clsid:1E0DFFCF-27FF-4574-849B-55007349FEDA'></object>"
var secureTool = "<object id='SecureTool' codebase='/ecp/themes/default/scripts/ca/itrus/itruscertctl.cab#version=3, 2, 11, 504' classid='clsid:0AA5FDF3-548C-4907-96A3-8C12CAC1932D'></object>"
var epassPin = "<object ID='VerPin' codebase='/ecp/themes/default/scripts/ca/itrus/itruscertctl.cab#Version=1,0,10,1129' classid='clsid:A94C8ACE-2A80-4CFB-8D2B-64859FCC30FD'></object>"
document.write(itruspta);
document.write(secureTool);
document.write(epassPin);

function getSN(ncid) {
	var m = 0;
	var cert = getcert(ncid);
	var SN = cert.SerialNumber;
	for (var k = 0; k < SN.length; k++) {
		if (SN.charAt(k) == '0') {
			m = m + 1;
		} else {
			SN = SN.substring(m, SN.length);
			return SN;
		}
	}
}

function getNCID(ncid) {
	var arrayCerts = filterCerts("", 0, "");
	for (var i = 0; i < arrayCerts.length; i++) {
		var subject = GetCertSubject(arrayCerts[i]);
		var str = new Array();
		str = subject.split(",");
		for (var j = 0; j < str.length; j++) {
			var str2 = str[j].split("=");
			for (var k = 0; k < str2.length; k++) {
				var str3 = str2[k].split(":");
				if (str3[0] == 'UID') {
					if (str3[1] == ncid) {
						return ncid;
					}
				}
			}
		}
	}
	return;
}

function CheckPassword(pwd, ncid) {
	var flag = 100;
	cert = getcert(ncid);
	var csp = cert.CSP;
	var flag = VerPin.SetUKeyPINWithCSP(pwd, csp);
	if (flag == 0) {
		return true;
	}
	return false;
}
function signmessage(message, ncid) {
	var cert = getcert(ncid);
	if (cert == null) {
		return;
	}
	if (message == null || message == "") {
		return;
	}
	var b64Cert = cert.GetEncodedCert(2); // base64缂栫爜
	var alg = "sha1";
	SecureTool.SetSignerCertificate(b64Cert);
	var signature = SecureTool.SignData(message, alg);
	return signature;
}

function getcert(ncid) {
	var cert;
	var arrayCerts = filterCerts("", 0, "");
	for (var i = 0; i < arrayCerts.length; i++) {
		var subject = GetCertSubject(arrayCerts[i]);
		var str = new Array();
		str = subject.split(",");
		for (var j = 0; j < str.length; j++) {
			var str2 = str[j].split("=");
			for (var k = 0; k < str2.length; k++) {
				var str3 = str2[k].split(":");
				if (str3[0] == 'UID') {
					var NCUserIdentifier = str3[1];
					if (NCUserIdentifier == ncid) {
						var cert = arrayCerts[i]; // base64缂栫爜
						return cert;
					}
				}
			}
		}
	}
	return cert;
}

function verfiymessage(message, signature, b64Cert) {
	var alg = "sha1";
	SecureTool.SetSignerCertificate(b64Cert);
	try {
		if (!SecureTool.VerifyData(message, alg, signature))
			return true;
	} catch (e) {
		return false
	}
	return false;
}

function getFileHash(fileName) {
	return filehash = SecureTool.GetFileHash(fileName, "sha1");
}

function SignFile(fileName, ncid) {
	var filehash = getFileHash(fileName);
	var cert = getcert(ncid);
	if (cert == null) {
		return;
	}
	if (filehash == null || filehash == "") {
		return;
	}
	var b64Cert = cert.GetEncodedCert(2); // base64缂栫爜
	var alg = "sha1";
	SecureTool.SetSignerCertificate(b64Cert);
	var signature = SecureTool.SignData(filehash, alg);
	return signature;
}
function verfiyFile(fileName, signature, b64Cert) {
	var filehash = getFileHash(fileName);
	if (signature == null || signature == "") {
		return;
	}
	if (filehash == null || filehash == "") {
		return;
	}
	var alg = "sha1";
	SecureTool.SetSignerCertificate(b64Cert);
	try {
		if (!SecureTool.VerifyData(filehash, alg, signature))
			return true;
	} catch (e) {
		alert(e.number);
		alert(e.message);
		return false
	}
	return false;
}