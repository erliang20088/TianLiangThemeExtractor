package com.zel.vaolan.pojos;
 
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zel.entity.TermUnit;

/**
 * 提取词条后所集成以<Key,WordItemPojoList>来表示,并对其数据做合并得到主题词
 * 
 * @author zel
 *   
 */
public class TermUnitStatMapPojo {
	@Override
	public String toString() {
		return "TermUnitStatMapPojo [wordToListMap=" + termUnitListMap + "]";
	}

	private Map<String, List<TermUnit>> termUnitListMap;

	public Map<String, List<TermUnit>> getTermUnitListMap() {
		return termUnitListMap;
	}

	public void setTermUnitListMap(Map<String, List<TermUnit>> termUnitListMap) {
		this.termUnitListMap = termUnitListMap;
	}

	public TermUnitStatMapPojo() {
		termUnitListMap = new HashMap<String, List<TermUnit>>();
	}

	public boolean contains(String key) {
		return termUnitListMap.containsKey(key);
	}

	public List<TermUnit> get(String key) {
		return termUnitListMap.get(key);
	}

	public void put(String key, List<TermUnit> termUnitList) {
		termUnitListMap.put(key, termUnitList);
	}

	public boolean isEmpty() {
		return termUnitListMap.isEmpty();
	}

	public Map<String, TermFreqAndDistanceComp> getFreqAndDistance() {
		if (termUnitListMap.isEmpty()) {
			return null;
		}
		Set<String> keySet = termUnitListMap.keySet();
		List<TermUnit> termUnitList = null;

		Map<String, TermFreqAndDistanceComp> newMap = new HashMap<String, TermFreqAndDistanceComp>();

		TermFreqAndDistanceComp termFreqAndDistanceComp = null;
		int freq = 0;
		int distance = 0;
		/**
		 * 记录该词条的词性 将每个集合最后该词条出现的位置的词性作为集合的词性，会有问题，暂时这样处理
		 */
		String nature = null;
		for (String key : keySet) {
			termUnitList = termUnitListMap.get(key);
			for (TermUnit termUnit : termUnitList) {
				freq++;
				distance += termUnit.getOffset();
				nature = termUnit.getNatureTermUnit().getTermNatureItem()
						.getNatureItem().getName();
			}
			termFreqAndDistanceComp = new TermFreqAndDistanceComp(key,nature, freq,
					distance);
			newMap.put(key, termFreqAndDistanceComp);
			freq = 0;
			distance = 0;
		}
		return newMap;
	}

	// 向map集合中，添加一个词条对象
	public void addTermUnit(TermUnit termUnit) {
		String key = termUnit.getValue();
		if (contains(key)) {
			get(key).add(termUnit);
		} else {
			List<TermUnit> termUnitList = new LinkedList<TermUnit>();
			termUnitList.add(termUnit);
			put(key, termUnitList);
		}
	}

}
