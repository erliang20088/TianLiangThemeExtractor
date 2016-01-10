import java.util.LinkedList;
import java.util.List;

import com.vaolan.parser.JsoupHtmlParser;
import com.zel.util.io.IOUtil;
import com.zel.vaolan.manager.extractor.KeywordExtManager;

public class KeywordExtTest {
	/**
	 * 简单文本测试
	 */
	public static void simpleTxtTest() {
		// 主题词提取核心类
		KeywordExtManager keywordExtManager = new KeywordExtManager();
		String source = null;

		List<String> strList = new LinkedList<String>();

		// strList.add("诏兰科技公司的发展很光明，因为有波哥带领的团队必将赢得成功");
		// strList.add("同志们干活都很带劲");
		// strList.add("马英九人品挺不错的");
		// strList.add("马英九的人品挺不错的");
		// strList.add("李大的哥真的挺不错，做饭也好,你看看这个效果如何？");
		// strList.add("一休哥哥真的很好");
		// strList.add("结婚的和尚和尚未结婚的和尚!");
		// strList
		// .add("创新工场CEO，媒体联系presschuangxin.com,风险投资#微博控#创新工场#教育#科技#电子商务#移动互联网#创业#IT互联网#世界因你不同");
		strList.add("一颗很逊的卤蛋。演员，代表作电视剧《武林外传》《潜伏》电影《爱出色》等");

		// String filePath = "d:/Noname5.txt";

		// source = IOUtil
		// .readFile(filePath, StaticValue4Keyword.default_encoding);
		// strList.add(source);

		// List<String> keywordList =
		// keywordExtManager.getNormalKeyword(source);
		// System.out.println("关键词： "+keywordList);

		for (String str : strList) {
			List<String> themeKeywordList = keywordExtManager
					.getThemeKeyword(str);
			System.out.println("主题词：" + themeKeywordList);
		}
	}

	public static void main(String[] args) {
		// 主题词提取核心类
		KeywordExtManager keywordExtManager = new KeywordExtManager();
		String source = null;

		List<String> strList = new LinkedList<String>();

		String article = IOUtil.readFile("d://article.txt", "UTF-8");
		// String article =
		// "《私人订制》是华谊兄弟传媒股份有限公司2013年出品的喜剧电影，冯小刚导演，葛优、白百合、李小璐、郑恺等联袂主演，范伟、宋丹丹、李诚儒、王宝强等诸多喜剧大腕儿鼎力助阵。2013年12月19日正式上映。";

		article = JsoupHtmlParser.getCleanTxt(article);

		// for (String str : strList) {
		List<String> themeKeywordList = keywordExtManager
				.getThemeKeyword(article);
		System.out.println("主题词：" + themeKeywordList);
	}
	// simpleTxtTest();
	// }
}
