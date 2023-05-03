package cn.soe.utl.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiezhenxiang 2023/4/27
 */
public class RegexUtils {

    /**
     * 提取以headStr开始且以tailStr结尾的字符串
     * @param includeHeadTail 是否返回startStr和endStr
     */
    public static List<String> extractFromHeadTail(String input, String headStr, String tailStr, boolean includeHeadTail) {
        List<String> matches = new ArrayList<String>();
        String patternString = Pattern.quote(headStr) + "(.*?)" + Pattern.quote(tailStr);
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String group = matcher.group(1);
            matches.add(includeHeadTail ? headStr + group + tailStr : group);
        }
        return matches;
    }
}
