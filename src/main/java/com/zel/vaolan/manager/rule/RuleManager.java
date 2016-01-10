package com.zel.vaolan.manager.rule;
 
import com.zel.vaolan.ifaces.rule.IRule;
import com.zel.vaolan.impl.rule.LengthRuleImpl;
import com.zel.vaolan.impl.rule.NatureJoinRelationRuleImpl;

/**
 * 规则管理器
 * 
 * @author zel
 *  
 */
public class RuleManager {
	// private List<IRule> ruleList = new LinkedList<IRule>();

	public void init() {
		// ruleList.add(arg0)
	}

	public static boolean isValidLength(String value) {
		IRule rule = new LengthRuleImpl(value);
		if (rule.isValid()) {
			return true;
		}
		return false;
	}

	public static boolean isValid4NatureJoin(String front_nature, String after_nature) {
//		if(StaticValue4Keyword.noneJoinRelationSet.contains(front_nature) || StaticValue4Keyword.noneJoinRelationSet.contains(after_nature)){
//			return false;
//		}
		IRule rule = new NatureJoinRelationRuleImpl(front_nature, after_nature);
		return rule.isValid();
	}
}
