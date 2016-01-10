package com.zel.vaolan.manager.analyzer;

import java.util.LinkedList;
import java.util.List;

import com.zel.core.analyzer.StandardAnalyzer;
import com.zel.entity.TermUnit;
import com.zel.interfaces.analyzer.Analyzer;

/**
 * 分词管理器
 * 
 * @author zel
 * 
 */
public class SkyLightAnalyzerManager {
	public static Analyzer analyzer = new StandardAnalyzer();

	public static List<TermUnit> getSplitResult(String src) {
		return analyzer.getSplitResult(src);
	}

	public static List<TermUnit> getSplitPOSResult(String src) {
		// return analyzer.getSplitPOSResult(src);
		return analyzer.getSplitMergePOSResult(src);
	}

	public static void main(String[] args) {
		List<String> strlist = new LinkedList<String>();

		// strlist.add("基于以上考虑，我们在标注过程中尽量避免那些容易出错的词性标记，而采用那些不容易出错、而对提高汉语词法句法分析正确率有明显作用的标记");

		// strlist.add("马英九这人挺不错的");
		// strlist.add("水瓶座的女人");
		strlist.add("一般很晚才回来");
		strlist.add("12月19");
		// strlist.add("大波哥人挺不错，饭做的也好,你看看这个效果如何？");

		// List<TermUnit> list = getSplitResult(src);
		for (String str : strlist) {
			List<TermUnit> list = getSplitPOSResult(str);
			if (list != null) {
				for (TermUnit term : list) {
					// System.out.print(term.getValue() + ",");
					System.out.print(term.getValue()
							+ "/"
							+ term.getNatureTermUnit().getTermNatureItem()
									.getNatureItem().getName() + ",");
				}
				System.out.println();
			} else {
				System.out.println("分词结果为null");
			}
		}
	}
}
