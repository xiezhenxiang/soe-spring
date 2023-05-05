package cn.soe.boot.autoconfigure.web.filter.xss;

import cn.soe.boot.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Safelist;

import java.util.function.Function;

/**
 * @author xiezhenxiang 2023/5/5
 * <a href="https://gitee.com/596392912/mica/tree/master/mica-xss">...</a>
 */
@Slf4j
public class XssParamConvertor implements Function<String, String> {

    private final XssFilterProperties xssFilterProperties;

    public XssParamConvertor(XssFilterProperties xssFilterProperties) {
        this.xssFilterProperties = xssFilterProperties;
    }

    private final Document.OutputSettings settings = new Document.OutputSettings()
            .escapeMode(Entities.EscapeMode.xhtml)
            .prettyPrint(false);

    @Override
    public String apply(String str) {
        // 注意会被转义
        String escapedStr = Jsoup.clean(str, "", soeSafeList(), settings);
        // 反转义
        String unescapeStr = Entities.unescape(escapedStr);
        if(!str.equals(unescapeStr) && xssFilterProperties.isFastFail()){
            throw new BizException(401, "此次请求存在xss攻击风险");
        }
        log.warn("xss convert: {}{} -> {}", System.lineSeparator(),
                str.replaceAll("\n|\r", ""),
                unescapeStr.replaceAll("\n|\r", ""));
        return unescapeStr;
    }

    /**
     * @see org.jsoup.safety.Safelist#basicWithImages()
     */
    public static Safelist soeSafeList() {
        return new Safelist()
                /*.addTags(
                        "a", "b", "blockquote", "br", "cite", "code", "dd", "dl", "dt", "em",
                        "i", "li", "ol", "p", "pre", "q", "small", "span", "strike", "strong", "sub",
                        "sup", "u", "ul")
                .addAttributes("a", "href")
                .addAttributes("blockquote", "cite")
                .addAttributes("q", "cite")

                .addProtocols("a", "href", "ftp", "http", "https", "mailto")
                .addProtocols("blockquote", "cite", "http", "https")
                .addProtocols("cite", "cite", "http", "https")*/

                .addTags("img")
                .addAttributes("img", "align", "alt", "height", "src", "title", "width")
                .addProtocols("img", "src", "http", "https");
    };
}