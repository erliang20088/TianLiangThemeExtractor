package com.zel.vaolan.iface;

import java.util.List;

/**
 * 实现关键词提取的接口类
 * 
 * @author zel
 * 
 */
public interface IKeywordExtractor {
	// 得到关键词,而非主题词
	public List<String> getNormalKeyword(String source);

	// 得到主题词
	public List<String> getThemeKeyword(String source);
}
