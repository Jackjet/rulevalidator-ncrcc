package com.yonyou.nc.codevalidator.sdk.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.yonyou.nc.codevalidator.rule.IRuleConfigContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionsReader;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.vo.CommonParamConfiguration;
import com.yonyou.nc.codevalidator.rule.vo.IRuleCheckConfiguration;
import com.yonyou.nc.codevalidator.rule.vo.ParamConfiguration;
import com.yonyou.nc.codevalidator.rule.vo.PrivateParamConfiguration;
import com.yonyou.nc.codevalidator.rule.vo.RuleCheckConfigurationImpl;
import com.yonyou.nc.codevalidator.rule.vo.RuleExecuteLevel;
import com.yonyou.nc.codevalidator.rule.vo.RuleItemConfigVO;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

public final class RuleXmlUtils {

	public static final String DEFAULT_ENCODING = "GBK";

	private RuleXmlUtils() {

	}

	public static void writeRuleCheckDefinition(OutputStream os, IRuleCheckConfiguration ruleCheckConfiguration)
			throws RuleBaseException {
		Document document = DocumentHelper.createDocument();
		document.addProcessingInstruction("xml-stylesheet",
				String.format("type=\"text/xsl\" href=\"%s\"", XmlPersistenceConstants.CONFIG_XSL_NAME));
		Element ruleConfiguration = document.addElement(XmlPersistenceConstants.RULE_CONFIGURATION);
		addCommonParamToElement(ruleConfiguration, ruleCheckConfiguration.getCommonParamConfiguration());
		addRuleItemConfigVoList(ruleConfiguration, ruleCheckConfiguration.getRuleItemConfigVoList());

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(DEFAULT_ENCODING);// 设置XML文件的编码格式
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(os, format);
			xmlWriter.write(document);
		} catch (UnsupportedEncodingException e) {
			throw new RuleBaseException(e);
		} catch (IOException e) {
			throw new RuleBaseException(e);
		} finally {
			try {
				if (xmlWriter != null) {
					xmlWriter.close();
				}
			} catch (IOException e) {
				throw new RuleBaseException(e);
			}
		}
	}

	private static void addCommonParamToElement(Element ruleConfiguration,
			CommonParamConfiguration commonParamConfiguration) {
		Element commonParametersElements = ruleConfiguration.addElement(XmlPersistenceConstants.COMMON_PARAMETERS);
		for (ParamConfiguration paramConfiguration : commonParamConfiguration.getParamConfigurationList()) {
			Element paramElement = commonParametersElements.addElement(XmlPersistenceConstants.COMMON_PARAMETER_LABEL);
			paramElement.addAttribute(XmlPersistenceConstants.COMMON_PARAMETER_NAME, paramConfiguration.getParamName());
			paramElement.addAttribute(XmlPersistenceConstants.COMMON_PARAMETER_VALUE,
					paramConfiguration.getParamValue());
		}
	}

	private static void addRuleItemConfigVoList(Element ruleConfiguration, List<RuleItemConfigVO> ruleItemConfigVOs) {
		Element ruleItemConfigElements = ruleConfiguration.addElement(XmlPersistenceConstants.RULEITEM_CONFIG_ELEMENTS);
		for (RuleItemConfigVO ruleItemConfigVO : ruleItemConfigVOs) {
			RuleDefinitionAnnotationVO ruleDefinitionVO = ruleItemConfigVO.getRuleDefinitionVO();
			Element ruleItemElement = ruleItemConfigElements
					.addElement(XmlPersistenceConstants.RULEITEM_CONFIG_ELEMENT);
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_EXECUTE_LEVEL, ruleItemConfigVO
					.getRuleExecuteLevel().getDisplayName());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_SERIALNO,
					ruleDefinitionVO.getSimpleIdentifier());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_CATEGORY, ruleDefinitionVO.getCatalog()
					.getName());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_SUB_CATEGORY, ruleDefinitionVO
					.getSubCatalog().getName());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_DESCRIPTION,
					ruleDefinitionVO.getDescription());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_CHECKROLE, ruleDefinitionVO.getCheckRole()
					.getName());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_REPAIR_LEVEL, ruleDefinitionVO
					.getRepairLevel().getName());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_CODER, ruleDefinitionVO.getCoder());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_CLASSNAME,
					ruleDefinitionVO.getRuleDefinitionIdentifier());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_SPECIAL_PARAM, ruleItemConfigVO
					.getPrivateParamConfiguration().toString());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_MEMO, ruleDefinitionVO.getMemo());
		}
	}

	public static void writeRuleExecuteResult(OutputStreamWriter os, List<IRuleExecuteResult> ruleExecuteResultList)
			throws RuleBaseException {
		Document document = DocumentHelper.createDocument();
		document.addProcessingInstruction("xml-stylesheet",
				String.format("type=\"text/xsl\" href=\"%s\"", XmlPersistenceConstants.RESULT_XSL_NAME));
		Element ruleConfiguration = document.addElement(XmlPersistenceConstants.RULE_EXECUTE_RESULT);

		if (ruleExecuteResultList.isEmpty()) {
			return;
		}
		IRuleConfigContext ruleConfigContext = ruleExecuteResultList.get(0).getRuleExecuteContext()
				.getRuleConfigContext();
		List<String> globalPropertyNames = ruleConfigContext.getGlobalPropertyNames();
		Element commonParametersElements = ruleConfiguration.addElement(XmlPersistenceConstants.COMMON_PARAMETERS);
		for (String paramName : globalPropertyNames) {
			Element paramElement = commonParametersElements.addElement(XmlPersistenceConstants.COMMON_PARAMETER_LABEL);
			paramElement.addAttribute(XmlPersistenceConstants.COMMON_PARAMETER_NAME, paramName);
			paramElement.addAttribute(XmlPersistenceConstants.COMMON_PARAMETER_VALUE,
					ruleConfigContext.getGlobalProperty(paramName));
		}

		addRuleExecuteResultList(ruleConfiguration, ruleExecuteResultList);

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(DEFAULT_ENCODING);// 设置XML文件的编码格式
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(os, format);
			xmlWriter.write(document);
		} catch (UnsupportedEncodingException e) {
			throw new RuleBaseException(e);
		} catch (IOException e) {
			throw new RuleBaseException(e);
		} finally {
			try {
				if (xmlWriter != null) {
					xmlWriter.close();
				}
			} catch (IOException e) {
				throw new RuleBaseException(e);
			}
		}
	}

	private static void addRuleExecuteResultList(Element ruleConfiguration,
			List<IRuleExecuteResult> ruleExecuteResultList) {
		Element ruleItemConfigElements = ruleConfiguration.addElement(XmlPersistenceConstants.RULEITEM_RESULT_ELEMENT);
		for (IRuleExecuteResult executeResult : ruleExecuteResultList) {
			String ruleDefinitionIdentifier = executeResult.getRuleDefinitionIdentifier();
			RuleDefinitionAnnotationVO ruleDefinitionVO = RuleDefinitionsReader.getInstance().getRuleDefinitionVO(
					ruleDefinitionIdentifier);
			Element ruleItemElement = ruleItemConfigElements
					.addElement(XmlPersistenceConstants.RULEITEM_CONFIG_ELEMENT);
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_EXECUTE_LEVEL, executeResult
					.getRuleExecuteContext().getRuleConfigContext().getRuleExecuteLevel().getDisplayName());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_SERIALNO,
					ruleDefinitionVO.getSimpleIdentifier());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_CATEGORY, ruleDefinitionVO.getCatalog()
					.getName());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_SUB_CATEGORY, ruleDefinitionVO
					.getSubCatalog().getName());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_DESCRIPTION,
					ruleDefinitionVO.getDescription());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_CHECKROLE, ruleDefinitionVO.getCheckRole()
					.getName());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_REPAIR_LEVEL, ruleDefinitionVO
					.getRepairLevel().getName());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_CODER, ruleDefinitionVO.getCoder());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_CLASSNAME,
					ruleDefinitionVO.getRuleDefinitionIdentifier());
			IRuleConfigContext ruleConfigContext = executeResult.getRuleExecuteContext().getRuleConfigContext();
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_SPECIAL_PARAM,
					ruleConfigContext.getSpecialParamString());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_SUCCESS, executeResult.getResult());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_RUN_RESULT, executeResult.getResult());
			ruleItemElement.addAttribute(XmlPersistenceConstants.RULEITEM_MEMO, executeResult.getNote());
		}
	}

	public static IRuleCheckConfiguration loadRuleCheckConfiguration(InputStream is) throws RuleBaseException {
		try {
			SAXReader saxReader = new SAXReader();
			saxReader.setEncoding(DEFAULT_ENCODING);
			Document document = saxReader.read(is);
			RuleCheckConfigurationImpl ruleCheckConfiguration = new RuleCheckConfigurationImpl();

			Node ruleConfigurationNode = document.selectSingleNode(XmlPersistenceConstants.RULE_CONFIGURATION);
			Node ruleItemConfigElement = ruleConfigurationNode
					.selectSingleNode(XmlPersistenceConstants.RULEITEM_CONFIG_ELEMENTS);
			List<? extends Node> ruleConfigElementList = ruleItemConfigElement
					.selectNodes(XmlPersistenceConstants.RULEITEM_CONFIG_ELEMENT);
			for (Node node : ruleConfigElementList) {
				Element ruleConfigurationElement = (Element) node;
				String ruleIdentifier = ruleConfigurationElement
						.attributeValue(XmlPersistenceConstants.RULEITEM_CLASSNAME);
				RuleDefinitionAnnotationVO ruleDefinitionVO = RuleDefinitionsReader.getInstance().getRuleDefinitionVO(
						ruleIdentifier);
				if (ruleDefinitionVO == null) {
					Logger.error(String.format("错误的identifier: %s", ruleIdentifier));
					continue;
				}
				RuleItemConfigVO ruleItemConfigVO = new RuleItemConfigVO(ruleDefinitionVO);
				String specialParameters = ruleConfigurationElement
						.attributeValue(XmlPersistenceConstants.RULEITEM_SPECIAL_PARAM);
				PrivateParamConfiguration privateParamConfiguration = PrivateParamConfiguration
						.fromString(specialParameters);
				ruleItemConfigVO.setPrivateParamConfiguration(privateParamConfiguration);

				String executeLevel = ruleConfigurationElement
						.attributeValue(XmlPersistenceConstants.RULEITEM_EXECUTE_LEVEL);
				RuleExecuteLevel ruleExecuteLevel = RuleExecuteLevel.getRuleExecuteLevel(executeLevel);
				ruleItemConfigVO.setRuleExecuteLevel(ruleExecuteLevel);

				ruleCheckConfiguration.addRuleItemConfigVo(ruleItemConfigVO);
			}

			Node commonParameterNode = ruleConfigurationNode
					.selectSingleNode(XmlPersistenceConstants.COMMON_PARAMETERS);
			CommonParamConfiguration commonParamConfiguration = ruleCheckConfiguration.getCommonParamConfiguration();
			List<? extends Node> paramsNodeList = commonParameterNode
					.selectNodes(XmlPersistenceConstants.COMMON_PARAMETER_LABEL);
			for (Node paramNode : paramsNodeList) {
				Element paramNodeElement = (Element) paramNode;
				String commonParamName = paramNodeElement.attributeValue(XmlPersistenceConstants.COMMON_PARAMETER_NAME);
				String commonParamValue = paramNodeElement
						.attributeValue(XmlPersistenceConstants.COMMON_PARAMETER_VALUE);
				commonParamConfiguration.setParamValue(commonParamName, commonParamValue);
			}
			return ruleCheckConfiguration;
		} catch (DocumentException e) {
			throw new RuleBaseException(e);
		}
	}

}
