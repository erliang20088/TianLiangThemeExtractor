package com.zel.vaolan.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 静态变量的定义
 * 
 * @author zel
 * 
 */
public class StaticValue4Keyword {
	public static String default_encoding = "utf-8";
	/**
	 * 分词后的标注中，一概过滤掉的词性
	 */
	public static Set<String> noneNatureSet = new HashSet<String>();
	static {
		// 初始化noneNatureSet集合,即这些词性不作为提取关键词的数据，直接过滤
		String[] nounArray = { "p", "c", "u", "uj", "e", "w", "unknown", "o",
				"uj", "y", "r", "f" };
		for (String temp : nounArray) {
			noneNatureSet.add(temp);
		}
	}

	// 提取文章中关键词不需要的词性
	public static Set<String> keyword_none_NatureSet = new HashSet<String>();
	static {
		// 初始化noneNatureSet集合,即这些词性不作为提取关键词的数据，直接过滤
		String[] nounArray = { "r", "c", "u", "e", "o", "y", "p", "uj", "w",
				"unknown", "m", "ng", "ag", "d", "ad", "vd" };
		for (String temp : nounArray) {
			keyword_none_NatureSet.add(temp);
		}
	}

	/**
	 * 否定词存放的词条set集合
	 */
	public static Set<String> noneSenseWordSet = new HashSet<String>();
	static {
		String none_sense_word_string = IOUtil.readDirOrFile(
				SystemParas.dic_filter_none_sense_path,
				StaticValue4Keyword.default_encoding);
		BufferedReader br = new BufferedReader(new StringReader(
				none_sense_word_string));
		String temp_line = null;
		try {
			while ((temp_line = br.readLine()) != null) {
				noneSenseWordSet.add(temp_line.trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 顺序有别，不能前后出现的词性结合顺序
	 * 
	 * @param args
	 */
	public static Map<String, Set<String>> natureJoinRelationMap = new HashMap<String, Set<String>>();
	static {
		Set<String> after_set = new HashSet<String>();
		after_set.add("d");
		after_set.add("ad");
		natureJoinRelationMap.put("n", after_set);
		natureJoinRelationMap.put("nr", after_set);
		natureJoinRelationMap.put("r", after_set);
		natureJoinRelationMap.put("z", after_set);
		natureJoinRelationMap.put("t", after_set);
		
		after_set = new HashSet<String>();
//		after_set.add("n");
		
		natureJoinRelationMap.put("j", after_set);
	}

	// 任何该词性的词语不进行合并
//	public static Set<String> noneJoinRelationSet = new HashSet<String>();
//	static {
//		noneJoinRelationSet.add("j");
//	}

	public static boolean isJoinAble(String front_nature, String after_nature) {
		if (front_nature == null) {
			return false;
		}
		Set set = null;
		if ((set = natureJoinRelationMap.get(front_nature)) != null
				&& set.contains(after_nature)) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		System.out.println(noneSenseWordSet.contains("是"));
	}
}
