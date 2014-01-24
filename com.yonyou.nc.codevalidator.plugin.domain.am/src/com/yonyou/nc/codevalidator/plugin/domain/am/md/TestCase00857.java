package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.CollectionUtils;
import com.yonyou.nc.codevalidator.sdk.utils.ListEqualVO;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;
/**
 * ZG
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "���Ե���ģ���ϵ��Զ�������Ԫ�����Զ������ƥ������", 
		relatedIssueId = "857", subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder="ZG")
public class TestCase00857 extends AbstractMetadataResourceRuleDefinition{

	@Override
	protected IRuleExecuteResult processResources(
			List<MetadataResource> resources,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		//�����
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		if(resources != null && resources.size() > 0){
			for(MetadataResource resource :resources){
				
				//��ȡԪ�����ļ�
				IMetadataFile metadataFile = resource.getMetadataFile();
				
				//��ȡԪ��������ʵ��
				List<IEntity> entitys = metadataFile.getAllEntities();
				if(!metadataFile.containsMainEntity()){
					continue;
				}
				//��ʵ��
				IEntity mainentity = metadataFile.getMainEntity();  
			
				//Ԫ�����ϵ������Զ�����
				List<String> metadefitem = new ArrayList<String>();   //���Ԫ�����ϵ�����ʵ����Զ�����
				for (IEntity entity:entitys) {            //����ʵ��
					List<IAttribute> listentiy = entity.getAttributes();
					for(IAttribute ia:listentiy){
						if(ia.getName().startsWith("def")){
							metadefitem.add(ia.getName());  
						}
					}	
				}
				
				String mainfuncode = "";
				//Ŀ�� �õ���ʵ�塪��������ģ���ϵ������Զ�����
				String mainid = mainentity.getId(); 
				String mainsql = "select funcode from sm_funcregister where mdid = '"+mainid+"'";
				//DataSet mainds = getDataSet(mainsql);
				DataSet mainds = SQLQueryExecuteUtils.executeQuery(mainsql, ruleExecContext.getRuntimeContext());
				List<DataRow> maindatarow = mainds.getRows();
				Map<String,List<String>> map = new HashMap<String, List<String>>();
				
				if(!mainds.isEmpty()){    //����ע����
					for(DataRow dmain:maindatarow){ //��Ӧ���ģ��ʱ
						List<String> mainlist = new ArrayList<String>();
						mainfuncode = (String) dmain.getValue("funcode");
						String sqlsql = "select b.METADATAPROPERTY " +
										"from pub_billtemplet a,pub_billtemplet_b b " +
										" where a.PK_BILLTEMPLET = b.PK_BILLTEMPLET " +
										"and a.nodecode = '"+mainfuncode+"'";
						//DataSet dsbill_bb = getDataSet(sqlsql);  //����ģ���ֶ�
						DataSet dsbill_bb = SQLQueryExecuteUtils.executeQuery(sqlsql, ruleExecContext.getRuntimeContext());  //����ģ���ֶ�
						
						if(!dsbill_bb.isEmpty()){  //˵�����˵���ģ��
							List<DataRow> datarowbilltemplet_bb = dsbill_bb.getRows();
							for(DataRow drr:datarowbilltemplet_bb){
								String s = (String)drr.getValue("METADATAPROPERTY");
								if(StringUtils.isNotBlank(s)) {
									String stringarray[] = s.split("\\.");
									int length = stringarray.length;
									if(stringarray[length-1].startsWith("def")){
										mainlist.add(stringarray[length-1]);     //��õ���ģ�������Զ�����
									}
								}
							}
//							List<String> shengyulist = new ArrayList<String>(); 
//							if(mainlist.size() != metadefitem.size()){   //�����˵���������Զ�����
//								for(String main:metadefitem){
//									if(mainlist.contains(main)){
//										mainlist.remove(main);
//									}else{
//										shengyulist.add(main);
//									}
//								}
//								if(shengyulist.size()>0){
//									map.put(mainfuncode, shengyulist);
//								}
//								
//							}
							
							
							//modified by zhangnane ԭ�еıȽϷ���������
							ListEqualVO<String> judgeVO = CollectionUtils.isListEqual(mainlist, metadefitem);
							List<String> notIncludeDef = judgeVO.getNotExistDest();
							if(notIncludeDef!=null&&notIncludeDef.size()>0){
								map.put(mainfuncode, notIncludeDef);
							}
						}
					}
				}
				if(!map.isEmpty()){   
					StringBuilder noteBuilder = new StringBuilder();
					for(Entry<String, List<String>> key:map.entrySet()){
						noteBuilder.append(key.getKey()+":"+key.getValue());
					}
					result.addResultElement("\n"+resource.getResourcePath(), "����ģ��ȱ����Ϣ���� |(���ܺţ�ȱ���Զ�����)|��"+noteBuilder.toString()+"\n");  //Ϊ����ȱ��(��)�Զ�������ֶ� 
				}
			}
		}
		return result;
	}
	
}
