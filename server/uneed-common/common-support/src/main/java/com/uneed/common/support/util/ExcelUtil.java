package com.uneed.common.support.util;

import com.uneed.common.core.lang.StringUtil;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Excel相关工具类
 *
 * @author hcs
 * @date 2020-02-20
 */
public class ExcelUtil {
    /**
     * 处理excel表格中可能出现的控制字符或非法字符
     *
     * @param s
     * @return
     */
    public static String deleteIllegalCharacter(String s) {
        if (StringUtil.isEmpty(s)) {
            return s;
        }
        // 前后空格
        s = s.trim();

        //去除：空格\s,回车\n,水平制表符即tab \t,换行\r
        Pattern p = Pattern.compile("\\s|\n|\t|\r");
        Matcher m = p.matcher(s);
        s = m.replaceAll("");

        // Excel文档中非法字符
        if (s.contains("\u202C")) {
            s = s.replace("\u202C", "").trim();
        }
        if (s.contains("\u202D")) {
            s = s.replace("\u202D", "").trim();
        }
        if (s.contains("\u202E")) {
            s = s.replace("\u202E", "").trim();
        }

        s = HtmlUtils.htmlEscape(s, "UTF-8");
        s = JavaScriptUtils.javaScriptEscape(s);
        return s;
    }

}
