package com.zel.vaolan.pojos;
 
import java.util.Set;

import com.zel.vaolan.manager.rule.RuleManager;

/** 
 * 对每个统计出来的词条集合的，频率和距离的合成类
 * 
 * @author zel
 *   
 */
public class TermFreqAndDistanceComp implements
		Comparable<TermFreqAndDistanceComp> {
	private String nature;

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	private int freq;
	// 为使用方便特别的冗余成员变量,即代表对象的key，即字符串的值
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public TermFreqAndDistanceComp(String key, String nature, int freq,
			int distance) {
		this.key = key;
		this.nature = nature;
		this.freq = freq;
		this.distance = distance;
	}

	/**
	 * 以某个词条的总词频数作为排序的词频
	 */
	@Override
	public int compareTo(TermFreqAndDistanceComp o) {
		int ret=this.freq * (mergeCount + 1) - o.freq * (mergeCount + 1);
		return ret!=0?ret:(this.key.length()-o.key.length());
	}

	private int distance;

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	// 定义某个词条一共merge了多少次,以单个词条的词频*mergeCount作为总词频参与排序
	private int mergeCount;

	public int getMergeCount() {
		return mergeCount;
	}

	public void setMergeCount(int mergeCount) {
		this.mergeCount = mergeCount;
	}

	// 判断本词条能否和参数词条合并,若有合并则返回true,没有则返回false
	public boolean merge(Set<String> hadAddSet,TermFreqAndDistanceComp termFreqAndDistanceComp) {
		if (termFreqAndDistanceComp == null) {
			return false;
		}
		String temp_term = null;

		// 提前计算各个变量
		int front_freq = this.getFreq();
		int after_freq = termFreqAndDistanceComp.getFreq();
		String front_key = this.getKey();
		String after_key = termFreqAndDistanceComp.getKey();

		if(hadAddSet.contains(front_key) || hadAddSet.contains(after_key)){
			return false;
		}
		
		if (front_freq == after_freq) {
			boolean is_valid_distance = false;
			int distance = this.getDistance()
					- termFreqAndDistanceComp.getDistance();

			if (distance > 0) {
				is_valid_distance = distance == front_freq * after_key.length();
			} else {
				is_valid_distance = Math.abs(distance) == front_freq
						* front_key.length();
			}
			if (is_valid_distance) {
				if (distance > 0) {
					if (RuleManager.isValid4NatureJoin(termFreqAndDistanceComp
							.getNature(), this.getNature())) {
						temp_term = after_key + front_key;
					}
				} else {
					if (RuleManager.isValid4NatureJoin(this.getNature(),
							termFreqAndDistanceComp.getNature())) {
						temp_term = front_key + after_key;
					}
				}
				if (temp_term != null) {
					// 将两个词条合并
					this.setMergeCount(this.getMergeCount() + 1);
					this.setKey(temp_term);
					// 合并后的词性统一暂定义为n,方便合并,避免因为p等的不方便
					this.setNature("n");
					// 暂取小的那一端
					this.setDistance(Math.min(this.getDistance(),
							termFreqAndDistanceComp.getDistance()));

					//将已合并的词条加入集合中
					hadAddSet.add(front_key);
					hadAddSet.add(after_key);
					
					return true;
				}
			}
		}
		return false;
	}

}
