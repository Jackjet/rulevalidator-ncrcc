package com.yonyou.nc.codevalidator.rule.executor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.yonyou.nc.codevalidator.rule.IRuleDefinition;

public class RuleExecutorFactoryImpl implements IRuleExecutorFactory {

	private Map<String, IRuleDefinition> idToDefinitionMap = new HashMap<String, IRuleDefinition>();

	public void addRuleDefinition(IRuleDefinition ruleDefinition) {
		idToDefinitionMap.put(ruleDefinition.getIdentifier(), ruleDefinition);
	}

	public void removeRuleDefinition(IRuleDefinition ruleDefinition) {
		String identifier = ruleDefinition.getIdentifier();
		if (idToDefinitionMap.containsKey(identifier)) {
			idToDefinitionMap.remove(identifier);
		}
	}

	@Override
	public Map<String, IRuleDefinition> getIdentifierToDefinitionMap() {
		return Collections.unmodifiableMap(idToDefinitionMap);
	}

}
