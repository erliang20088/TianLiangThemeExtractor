package com.zel.vaolan.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zel.entity.TermUnit;
import com.zel.util.MapToSortListUtil;
import com.zel.vaolan.iface.IKeywordExtractor;
import com.zel.vaolan.manager.analyzer.SkyLightAnalyzerManager;
import com.zel.vaolan.manager.rule.RuleManager;
import com.zel.vaolan.pojos.TermFreqAndDistanceComp;
import com.zel.vaolan.pojos.TermUnitStatMapPojo;
import com.zel.vaolan.utils.StaticValue4Keyword;
import com.zel.vaolan.utils.SystemParas;

/**
 * 关键词提取的实现类
 * 
 * @author zel
 * 
 */
public class KeywordExtractorImpl implements IKeywordExtractor {
	private SkyLightAnalyzerManager skyLightAnalyzerManager;

	public SkyLightAnalyzerManager getSkyLightAnalyzerManager() {
		return skyLightAnalyzerManager;
	}

	public void setSkyLightAnalyzerManager(
			SkyLightAnalyzerManager skyLightAnalyzerManager) {
		this.skyLightAnalyzerManager = skyLightAnalyzerManager;
	}

	public KeywordExtractorImpl(SkyLightAnalyzerManager skyLightAnalyzerManager) {
		this.skyLightAnalyzerManager = skyLightAnalyzerManager;
	}

	// 得到一般性的关键词
	@Override
	public List<String> getNormalKeyword(String source) {
		if (source == null || source.isEmpty()) {
			return null;
		}
		List<TermUnit> temp_termList = null;
		String natureValue = null;
		// 要存储的结果集合
		TermUnitStatMapPojo termUnitStatMapPojo = new TermUnitStatMapPojo();

		temp_termList = this.skyLightAnalyzerManager.getSplitPOSResult(source);
		if (temp_termList != null) {
			for (TermUnit termUnit : temp_termList) {
				natureValue = termUnit.getNatureTermUnit().getTermNatureItem()
						.getNatureItem().getName();
				// 如果在过滤的词性集合中，则直接过滤掉
				if (!(StaticValue4Keyword.keyword_none_NatureSet
						.contains(natureValue)
						|| StaticValue4Keyword.noneSenseWordSet
								.contains(termUnit.getValue()) || termUnit
						.getLength() < 2)) {
					termUnitStatMapPojo.addTermUnit(termUnit);
				}
			}
			if (termUnitStatMapPojo.getTermUnitListMap().isEmpty()) {
				return null;
			}
			// 对统计出来的map按value中集合的大小进行排序，即词频做逆序，取top n作为推荐
			MapToSortListUtil<String, TermUnit> mapToSortListUtil = new MapToSortListUtil<String, TermUnit>();
			List<Map.Entry<String, List<TermUnit>>> mappingWordGroupList = mapToSortListUtil
					.mapToSortList(termUnitStatMapPojo.getTermUnitListMap(),
							mapToSortListUtil.getDefaultComparator());

			List<Map.Entry<String, List<TermUnit>>> newList = mappingWordGroupList
					.subList(0, Math.min(mappingWordGroupList.size(),
							SystemParas.keyword_top_number));
			List<String> keyword_topN = new LinkedList<String>();

			for (Map.Entry<String, List<TermUnit>> entry : newList) {
				keyword_topN.add(entry.getKey());
			}
			return keyword_topN;
		}

		return null;
	}

	// 得到主题词
	@Override
	public List<String> getThemeKeyword(String source) {
		if (source == null || source.isEmpty()) {
			return null;
		}
		List<TermUnit> temp_termList = null;
		String natureValue = null;
		String termValue = null;
		// 要存储的结果集合
		TermUnitStatMapPojo termUnitStatMapPojo = new TermUnitStatMapPojo();

		temp_termList = this.skyLightAnalyzerManager.getSplitPOSResult(source);
		if (temp_termList != null) {
			for (TermUnit termUnit : temp_termList) {
				natureValue = termUnit.getNatureTermUnit().getTermNatureItem()
						.getNatureItem().getName();
				// 如果在指定的无意义词性集合中，则直接过滤掉
				if (!StaticValue4Keyword.noneNatureSet.contains(natureValue)
						&& (!StaticValue4Keyword.noneSenseWordSet
								.contains(termUnit.getValue()))) {
					termUnitStatMapPojo.addTermUnit(termUnit);
				}
			}
			if (termUnitStatMapPojo.getTermUnitListMap().isEmpty()) {
				return null;
			}

			Map<String, TermFreqAndDistanceComp> newStatsMap = termUnitStatMapPojo
					.getFreqAndDistance();

			// 对统计出来的map按value中集合的大小进行排序，即词频做逆序，取top n作为推荐
			MapToSortListUtil<String, TermFreqAndDistanceComp> mapToSortListUtil = new MapToSortListUtil<String, TermFreqAndDistanceComp>();
			List<Map.Entry<String, TermFreqAndDistanceComp>> mappingTermList = mapToSortListUtil
					.mapToSortList4NotListValue(newStatsMap,
							mapToSortListUtil.defaultComparator4NotListValue);

			List<Map.Entry<String, TermFreqAndDistanceComp>> newList = mappingTermList
					.subList(0, Math.min(mappingTermList.size(),
							SystemParas.keyword_top_number));

			Map<String, TermFreqAndDistanceComp> merge_map = mergeSortList(newList);
			// 对统计出来的map按value中集合的大小进行排序，即词频做逆序，取top n作为推荐
			mappingTermList = mapToSortListUtil
					.mapToSortList4NotListValue(merge_map,
							mapToSortListUtil.defaultComparator4NotListValue);

			newList = mappingTermList.subList(0, Math.min(mappingTermList
					.size(), SystemParas.theme_top_number));

			List<String> keyword_topN = new LinkedList<String>();
			for (Map.Entry<String, TermFreqAndDistanceComp> entry : newList) {
				keyword_topN.add(entry.getKey());
			}

			return keyword_topN;
		}
		return null;
	}

	// 对排序后的结果集合并成最后的主题词,因为是排序而来，肯定不为空，故不做为空判断
	public Map<String, TermFreqAndDistanceComp> mergeSortList(
			List<Map.Entry<String, TermFreqAndDistanceComp>> mappingTermList) {
		// 依然要map结构，merge完后再统一排序取top-n
		Map<String, TermFreqAndDistanceComp> themeResultMap = new HashMap<String, TermFreqAndDistanceComp>();

		int term_len = mappingTermList.size();
		// 局部变量声明
		// Map.Entry<String, TermFreqAndDistanceComp> entry_front_entry = null;
		TermFreqAndDistanceComp entry_front_value = null;
		// Map.Entry<String, TermFreqAndDistanceComp> entry_after_entry = null;
		TermFreqAndDistanceComp entry_after_value = null;

		StringBuilder all_sb = new StringBuilder();
		String front_key_old = null;

		//加过的词条的过滤集合，防止一个词条被合并多次
		Set<String> hadAddSet = new HashSet<String>();

		boolean isMerge = false;
		for (int i = 0; i < term_len; i++) {
			entry_front_value = mappingTermList.get(i).getValue();
			front_key_old = entry_front_value.getKey();
			for (int j = i + 1; j < term_len; j++) {
				entry_after_value = mappingTermList.get(j).getValue();
				// 进行词条合并
				isMerge = entry_front_value.merge(hadAddSet,entry_after_value);

				if (isMerge) {
					// 如果有合并重新再比较一次，主要在于key之前是无序的
					j = i;
				}
			}
			if (all_sb.indexOf(entry_front_value.getKey()) < 0
					&& RuleManager.isValidLength(entry_front_value.getKey())) {
				themeResultMap.put(entry_front_value.getKey(),
						entry_front_value);
			}
			all_sb.append(entry_front_value.getKey() + " ");
		}
		return themeResultMap;
	}
}
