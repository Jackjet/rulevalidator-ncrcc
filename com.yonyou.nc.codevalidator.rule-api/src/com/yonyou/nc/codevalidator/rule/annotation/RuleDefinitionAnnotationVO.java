package com.yonyou.nc.codevalidator.rule.annotation;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.vo.CommonParamConfiguration;
import com.yonyou.nc.codevalidator.rule.vo.ParamConfiguration;
import com.yonyou.nc.codevalidator.rule.vo.PrivateParamConfiguration;

public class RuleDefinitionAnnotationVO implements Comparable<RuleDefinitionAnnotationVO> {

	private static final String SYSTEM_PREFIX = "ncrcc.yonyou.com/browse/NCRCC-";

	private String ruleDefinitionName;
	private ScopeEnum scope;
	private CatalogEnum catalog;
	private SubCatalogEnum subCatalog;
	private String description;
	private CheckRoleEnum checkRole;
	private String coder;
	private String memo;
	private String solution;
	private RepairLevel repairLevel;
	private String relatedIssueId;
	private ExecutePeriod executePeriod;
	private ExecuteLayer executeLayer;

	private CommonParamConfiguration commonParamConfiguration = new CommonParamConfiguration();
	private PrivateParamConfiguration privateParamConfiguration = new PrivateParamConfiguration();
	
	public RuleDefinitionAnnotationVO(String ruleDefinitionName) {
		this.ruleDefinitionName = ruleDefinitionName;
	}

	/**
	 * identifier去掉包名称前缀
	 * @return
	 */
	public String getSimpleIdentifier() {
		return ruleDefinitionName.substring(ruleDefinitionName.lastIndexOf(".") + 1, ruleDefinitionName.length());
	}

	public String getRuleDefinitionIdentifier() {
		return ruleDefinitionName;
	}

	public ScopeEnum getScope() {
		return scope;
	}

	void setScope(ScopeEnum scope) {
		this.scope = scope;
	}

	public CatalogEnum getCatalog() {
		return catalog;
	}

	void setCatalog(CatalogEnum catalog) {
		this.catalog = catalog;
	}

	public SubCatalogEnum getSubCatalog() {
		return subCatalog;
	}

	void setSubCatalog(SubCatalogEnum subCatalog) {
		this.subCatalog = subCatalog;
	}

	public String getDescription() {
		return description;
	}

	void setDescription(String description) {
		this.description = description;
	}

	public CheckRoleEnum getCheckRole() {
		return checkRole;
	}

	void setCheckRole(CheckRoleEnum checkRole) {
		this.checkRole = checkRole;
	}

	public String getCoder() {
		return coder;
	}

	void setCoder(String coder) {
		this.coder = coder;
	}

	public String getMemo() {
		return memo;
	}

	void setMemo(String memo) {
		this.memo = memo;
	}

	public String getSolution() {
		return solution;
	}

	void setSolution(String solution) {
		this.solution = solution;
	}

	public String getRelatedIssueId() {
		return relatedIssueId;
	}

	void setRelatedIssueId(String relatedIssueId) {
		this.relatedIssueId = relatedIssueId;
	}

	public String getRelatedSystemIssueLink() {
		return relatedIssueId == null || relatedIssueId.trim().length() == 0 ? "" : SYSTEM_PREFIX + relatedIssueId;
	}

	public RepairLevel getRepairLevel() {
		return repairLevel;
	}

	void setRepairLevel(RepairLevel repairLevel) {
		this.repairLevel = repairLevel;
	}

	void setRuleDefinitionName(String ruleDefinitionName) {
		this.ruleDefinitionName = ruleDefinitionName;
	}

	@Override
	public int compareTo(RuleDefinitionAnnotationVO o) {
		return 0;
	}

	public List<String> getSpecialParamList() {
		List<String> result = new ArrayList<String>();
		List<ParamConfiguration> paramConfigurationList = privateParamConfiguration.getParamConfigurationList();
		for (ParamConfiguration paramConfiguration : paramConfigurationList) {
			result.add(paramConfiguration.getParamName());
		}
		return result;
	}

	public String getSpecialParamStr() {
		List<ParamConfiguration> paramConfigurationList = privateParamConfiguration.getParamConfigurationList();
		StringBuilder result = new StringBuilder();
		for (ParamConfiguration paramConfiguration : paramConfigurationList) {
			result.append(paramConfiguration.getParamName()).append(",");
		}
		return result.toString();
	}

	public CommonParamConfiguration getCommonParamConfiguration() {
		return commonParamConfiguration;
	}

	public void setCommonParamConfiguration(CommonParamConfiguration commonParamConfiguration) {
		this.commonParamConfiguration = commonParamConfiguration;
	}

	public PrivateParamConfiguration getPrivateParamConfiguration() {
		return privateParamConfiguration;
	}

	public void setPrivateParamConfiguration(PrivateParamConfiguration privateParamConfiguration) {
		this.privateParamConfiguration = privateParamConfiguration;
	}

	public ExecutePeriod getExecutePeriod() {
		return executePeriod;
	}

	void setExecutePeriod(ExecutePeriod executePeriod) {
		this.executePeriod = executePeriod;
	}

	public ExecuteLayer getExecuteLayer() {
		return executeLayer;
	}

	void setExecuteLayer(ExecuteLayer executeLayer) {
		this.executeLayer = executeLayer;
	}
	
}
