package com.uneed.common.core.text;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.uneed.common.core.collection.ArrayUtil;
import com.uneed.common.core.lang.ObjectUtil;
import com.uneed.common.core.lang.StringUtil;
import com.uneed.common.core.text.pattern.PinyinPattern;

import java.util.ArrayList;
import java.util.List;

import static com.uneed.common.core.lang.ObjectUtil.nullToDefault;


/**
 * Class description goes here.
 *
 * @author diablo
 * @date 17/12/28
 */
public final class PinyinUtil {

    private PinyinUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    private static final String REPLACE = "の";

    public static String toPinyin(String str) {
        return toPinyin(str, null, null);
    }

    public static String toPinyin(String str, String delimiter) {
        return toPinyin(str, delimiter, null);
    }

    public static String toPinyin(String str, PinyinPattern pattern) {
        return toPinyin(str, null, pattern);
    }

    public static String toPinyin(String str, String delimiter, PinyinPattern pattern) {
        try {
            boolean toUpper = pattern != null && pattern.getFlag() % 2 == 0;
            if (!toUpper) {
                return PinyinHelper.convertToPinyinString(str, nullToDefault(delimiter, ""), getFormat(pattern));
            }
            String[] temp = PinyinHelper.convertToPinyinString(str, REPLACE, getFormat(pattern)).split(REPLACE);
            for (int i = 0; i < temp.length; i++) {
                temp[i] = StringUtil.upperFirst(temp[i]);
            }
            return ArrayUtil.join(temp, ObjectUtil.nullToDefault(delimiter, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String toShortPinyin(String str) {
        try {
            return PinyinHelper.getShortPinyin(str);
        } catch (PinyinException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 是否多音字
     */
    public static boolean isPolyphone(char c) {
        return PinyinHelper.hasMultiPinyin(c);
    }

    public static List<String> findPolyphone(String str) {
        List<String> list = new ArrayList<>();
        for (char ch : str.toCharArray()) {
            if (isPolyphone(ch)) {
                String s = Character.toString(ch);
                if (list.contains(s)) {
                    continue;
                }
                list.add(Character.toString(ch));
            }
        }
        return list;
    }

    private static PinyinFormat getFormat(PinyinPattern pattern) {
        return pattern == null ? PinyinFormat.WITHOUT_TONE : PinyinFormat.valueOf(pattern.getFormat());
    }
}
