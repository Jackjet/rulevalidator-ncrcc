define(function(require, exports, module) {
	var $ = require('$');
	var Assert = require('assert');
	// $ = require('jquery.md5')($);

	/**
	 * 映射
	 */
	var mappers = {};

	/**
	 * 注册
	 */
	function register(code, mapper) {
		if (mappers[code]) {
			return false;
		} else {
			mappers[code] = mapper;
		}
	}
	
	/**
	 * 获取映射
	 */
	function getMapper(code) {
		Assert.notNull(code, '映射代码不能为空');
		return mappers[code];
	}

	/**
	 * MAOrderProductUIView => ProductBasicUIView
	 */ 
	register("MP>BP", {
		'count' : null,
		'saleOrgName' : 'pk_org_name',
		'pk_org' : 'pk_org',
		'corigcurrencyid' : 'corigcurrencyid',
		'cproductid' : 'cproductid',
		'vname' : 'cproductid_name',
		'pk_measdoc_unitScale' : 'cqtunitid_unitScale',
		'pk_measdoc' : 'cqtunitid',
		'pk_measdoc_name' : 'cqtunitid_name',
		'corigcurrencyid_amountScale' : 'corigcurrencyid_amountScale',
		'corigcurrencyid_curSign' : 'corigcurrencyid_curSign',
		'nbaseprice' : 'nqtorigtaxprice',
		'nsaleprice' : 'nqtorigtaxnetprc',
		'corigcurrencyid_curName' : 'corigcurrencyid_curName',
		'vcode' : 'cproductid_code',
		'vhtmurl' : null,
		'pk_org_priceScale' : null,
		'bspotflag' : 'bspotflag',
		'cinventoryid' : 'cmaterialid',
		'cinventoryid_name' : 'cmaterialid_name',
		'vfree1' : null,
		'vfree2' : null,
		'vfree3' : null,
		'vfree4' : null,
		'vfree5' : null,
		'vfree6' : null,
		'vfree7' : null,
		'vfree8' : null,
		'vfree9' : null,
		'vfree10' : null,
		'vimageurl' : null,
		'vminimageurl' : null,
		'viconurl1' : null,
		'viconurl' : null,
		'materialspec' : null,
		'materialtype' : null,
		'buyflag' : null,
		'price' : 'nqtorigtaxnetprc',
		'showMode' : null,
		'productLineId' : null,
		'vkeyword' : null,
		'saleVolume' : null
	});
	
	/**
	 * ProductBasicUIView => MAOrderProductUIView
	 */
	register("BP>MP", {
		bspotflag:'bspotflag',
		ccustomerpobid:null,
		ccustomerpoid:null,
		csrcid:null,
		csrcbid:null,
		vsrccode:null,
		crowno:null,
		vsrcrowno:null,
		cexchangesrcretid:null,
		returnRowIndex:null,
		cproductid:'cproductid',
		cproductid_code:'vcode',
		cproductid_name:'vname',
		nqtunitnum:null,
		cqtunitid:'pk_measdoc',
		cqtunitid_unitScale:'pk_measdoc_unitScale',
		cqtunitid_name:'pk_measdoc_name',
		nnabnum:null,
		nqtorigtaxprice:'nsaleprice',
		nqtorigtaxnetprc:null,
		nqtorignetprice:null,
		nqtorigprice:null,
		taxRate:null,
		nqtorigtaxprice_priceScale:'corigcurrencyid_amountScale',
		corigcurrencyid:'corigcurrencyid',
		corigcurrencyid_amountScale:'corigcurrencyid_amountScale',
		corigcurrencyid_curName:'corigcurrencyid_curName',
		corigcurrencyid_curSign:'corigcurrencyid_curSign',
		norigtaxmny:null,
		nnum:null,
		cunitid:null,
		ntotaloutnum:null,
		ntotalreturnnum:null,
		ntotalsignnum:null,
		cretreasonid:null,
		cretreasonid_name:null,
		frowstatus:null,
		fretexchange:null,
		cshopcartid:null,
		vhtmurl:'vhtmurl',
		caclPrice:null,
		cmaterialid:'cinventoryid',
		cmaterialid_code:null,
		cmaterialid_name:'cinventoryid_name',
		cmaterialvid:null,
		pk_org:'pk_org',
		pk_org_name:'saleOrgName',
		pk_org_code:null,
		availableReturnNum:null,
		cproductattrid:null,
	});

	// /**
	// * 获取json属性摘要
	// */
	// function getKeyDigest(json) {
	// var attrs = [];
	// for ( var attr in json) {
	// attrs.push(attr);
	// }
	// return degest(attrs.join(','));
	// }
	//
	// /**
	// * 获取json值摘要
	// */
	// function getValueDigest(json) {
	// var values = [];
	// for ( var attr in json) {
	// values.push(json[attr]);
	// }
	// return degest(values.join(','));
	// }
	//
	// /**
	// * 计算摘要（MD5摘要算法）
	// */
	// function digest(str) {
	// return $.md5(str);
	// }
	//
	// /**
	// * 获取模型映射关系
	// */
	// function getModelMapper(target, source) {
	//		
	// // 计算key和value摘要
	// var keyDigest = getKeyDigest(target);
	// var valueDigest = getKeyDigest(source);
	//		
	// }

	module.exports = {
		register : register,
		getMapper : getMapper,
	};
});