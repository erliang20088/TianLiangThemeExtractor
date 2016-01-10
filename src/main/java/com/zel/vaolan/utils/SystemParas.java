package com.zel.vaolan.utils;

/**
 * 系统参数配置
 * 
 * @author zel
 * 
 */
public class SystemParas {
	// 日志
	public static MyLogger logger = new MyLogger(SystemParas.class);

	public static ReadConfigUtil readConfigUtil = new ReadConfigUtil(
			"config_theme_extractor.properties", true);

	// 关键词提取的top-n的数量
	public static int keyword_top_number = Integer.parseInt(readConfigUtil
			.getValue("keyword_top_number"));
	// 主题词提取的top-n的数量
	public static int theme_top_number = Integer.parseInt(readConfigUtil
			.getValue("theme_top_number"));
	// 主题词抽取时候的最小主题词条的字数长度
	public static int rule_term_min_length = Integer.parseInt(readConfigUtil
			.getValue("rule_term_min_length"));

	// 两个认为是共现的词的最大词之间的距离，在距离之内算共现，距离之外不算共现
	public static int terms_occur_max_distance = Integer
			.parseInt(readConfigUtil.getValue("terms_occur_max_distance"));

	// 读取无意义词典配置
	public static String dic_filter_none_sense_path = readConfigUtil
			.getValue("dic_filter_none_sense_path");
}
