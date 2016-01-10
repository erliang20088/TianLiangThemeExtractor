package com.zel.vaolan.impl.rule;

import com.zel.vaolan.ifaces.rule.IRule;
import com.zel.vaolan.utils.SystemParas;

/**
 * 主题词条的长度限制
 * 
 * @author zel
 * 
 */
public class LengthRuleImpl implements IRule {
	private String termValue;

	public LengthRuleImpl(String termValue) {
		this.termValue = termValue;
	}

	@Override
	public boolean isValid() {
		if (this.termValue.length() < SystemParas.rule_term_min_length) {
			return false;
		}
		return true;
	}

}
