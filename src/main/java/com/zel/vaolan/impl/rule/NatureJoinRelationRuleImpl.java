package com.zel.vaolan.impl.rule;

import com.zel.vaolan.ifaces.rule.IRule;
import com.zel.vaolan.utils.StaticValue4Keyword;

/**
 * 词性之间结合的关系限制，如名词后不跟副词
 * 
 * @author zel
 * 
 */
public class NatureJoinRelationRuleImpl implements IRule {
	private String front_nature;
	private String after_natrue;

	public NatureJoinRelationRuleImpl(String front_nature, String after_nature) {
		this.front_nature = front_nature;
		this.after_natrue = after_nature;
	}

	@Override
	public boolean isValid() {
		return StaticValue4Keyword.isJoinAble(front_nature, after_natrue);
	}
}
