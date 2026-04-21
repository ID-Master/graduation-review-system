package com.uneed.common.core.lang;

import com.uneed.common.core.collection.ArrayUtil;
import com.uneed.common.core.collection.CollectionUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.text.StringFormatter;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

import static com.uneed.common.core.lang.ObjectUtil.isNotNull;
import static com.uneed.common.core.lang.ObjectUtil.isNull;

/**
 * 字符串处理的工具类。
 *
 * @author diablo
 * @date 2019/9/7
 * @since 1.0.0
 */
public final class StringUtil {

    /**
     * 私有化构造函数，禁止实例化该类
     */
    private StringUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 字符串常量系列
     */
    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String IS = "is";
    public static final String GET = "get";
    public static final String SET = "set";
    public static final String DOT = "\\.";
    public static final String COMMA = ",";
    public static final String EMPTY_JSON = "{}";
    public static final String UNDERLINE = "_";

    /**
     * 判断字符串是否为null或空白字符
     * <pre>
     * StringUtil.isEmpty(null)      = true
     * StringUtil.isEmpty("")        = true
     * StringUtil.isEmpty(" ")       = true
     * StringUtil.isEmpty("bob")     = false
     * StringUtil.isEmpty("  bob  ") = false
     * </pre>
     *
     * @param param 字符串
     * @return 是否为null或空白字符
     */
    public static boolean isEmpty(final CharSequence param) {
        if (isNull(param)) {
            return true;
        }
        for (int i = 0; i < param.length(); i++) {
            if (!Character.isWhitespace(param.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否非null或非空白字符，实际为{@link #isEmpty(CharSequence)}取反。
     *
     * @param param 需要判断的字符串
     * @return 是否非null或非空白字符
     */
    public static boolean isNotEmpty(final CharSequence param) {
        return !isEmpty(param);
    }

    /**
     * 当给定字符串为null时，转换为Empty
     *
     * @param str 被转换的字符串
     * @return 转换后的字符串
     */
    public static String nullToEmpty(CharSequence str) {
        return nullToDefault(str, EMPTY);
    }

    /**
     * 如果字符串是<code>null</code>，则返回指定默认字符串，否则返回字符串本身。
     *
     * <pre>
     * nullToDefault(null, &quot;default&quot;)  = &quot;default&quot;
     * nullToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;&quot;
     * nullToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
     * nullToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
     * </pre>
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     */
    public static String nullToDefault(CharSequence str, String defaultStr) {
        return (str == null) ? defaultStr : str.toString();
    }

    /**
     * 如果值为null返回空字符串
     * @param obj
     * @return
     */
    public static String valueOfEmpty(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }

    //----------------------------------------------------------------------------

    /**
     * 驼峰格式字符串转下划线格式字符串。
     *
     * @param param 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String camelToUnderline(final String param) {
        return camelToCharacter(CharUtil.UNDERLINE, param);
    }

    /**
     * 驼峰格式字符串转下划线格式字符串。
     *
     * @param params 需要转换的字符串
     * @return 转换后的字符串
     */
    public static List<String> camelToUnderline(final List<String> params) {
        List<String> list = Lists.newArrayList();
        if (ObjectUtil.isNotEmpty(params)) {
            params.stream().map(StringUtil::camelToUnderline).filter(StringUtil::isNotEmpty).forEach(list::add);
        }
        return list;
    }

    /**
     * 驼峰格式字符串转自定义格式的字符串
     *
     * @param ch    自定义格式字符
     * @param param 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String camelToCharacter(final char ch, final String param) {
        if (isEmpty(param)) {
            return EMPTY;
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append(ch);
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * 驼峰格式字符串转下划线格式字符串。该方法接收一个字符串数组，并将数组内的元素逐个转换后，添加到新数组中再返回。
     *
     * @param params 需要转换的字符串数组
     * @return 转换后的字符串数组
     */
    public static String[] camelArrayToUnderline(final String... params) {
        return camelArrayToCharacter(CharUtil.UNDERLINE, params);
    }

    /**
     * 驼峰格式字符串转自定义格式的字符串。该方法接收一个字符串数组，并将数组内的元素逐个转换后，添加到新数组中再返回。
     *
     * @param ch     自定义格式字符
     * @param params 需要转换的字符串数组
     * @return 转换后的字符串
     */
    public static String[] camelArrayToCharacter(final char ch, final String... params) {
        int len = isNull(params) ? 0 : params.length;
        String[] temp = new String[len];
        for (int i = 0; i < len; i++) {
            temp[i] = camelToCharacter(ch, params[i]);
        }
        return temp;
    }

    /**
     * 下划线格式字符串转驼峰格式字符串
     *
     * @param param 需要转换的字符串
     * @return 驼峰格式字符串
     */
    public static String underlineToCamel(final String param) {
        return characterToCamel(CharUtil.UNDERLINE, param);
    }

    /**
     * 下划线格式字符串转驼峰格式字符串.该方法接收一个字符串数组，并将数组内的元素逐个转换后，添加到新数组中再返回。
     *
     * @param param 需要转换的字符串
     * @return 驼峰格式字符串数组
     */
    public static String[] underlineArrayToCamel(final String param) {
        return characterArrayToCamel(CharUtil.UNDERLINE, param);
    }

    /**
     * 自定义格式字符串转驼峰格式字符串
     *
     * @param ch    自定义格式字符
     * @param param 需要转换的字符串
     * @return 驼峰格式的字符串
     */
    public static String characterToCamel(final char ch, final String param) {
        if (isEmpty(param)) {
            return EMPTY;
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c != ch) {
                sb.append(Character.toLowerCase(c));
                continue;
            }
            if (i > 0 && ++i < len) {
                sb.append(Character.toUpperCase(param.charAt(i)));
            }
        }
        return sb.toString();
    }

    /**
     * 自定义格式字符串转驼峰格式字符串。该方法接收一个字符串数组，并将数组内的元素逐个转换后，添加到新数组中再返回。
     *
     * @param ch     自定义格式字符
     * @param params 需要转换的字符串
     * @return 驼峰格式的字符串数组
     */
    public static String[] characterArrayToCamel(final char ch, final String... params) {
        int len = isNull(params) ? 0 : params.length;
        String[] temp = new String[len];
        for (int i = 0; i < len; i++) {
            temp[i] = characterToCamel(ch, params[i]);
        }
        return temp;
    }

    //----------------------------------------------------------------------------

    /**
     * 首字母大写
     *
     * @param param 字符串参数
     * @return 首字母转大写后的字符串
     */
    public static String upperFirst(final String param) {
        return isEmpty(param) ? EMPTY : Character.toUpperCase(param.charAt(0)) + param.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param param 字符串参数
     * @return 首字母转小写后的字符串
     */
    public static String lowerFirst(final String param) {
        return isEmpty(param) ? EMPTY : Character.toLowerCase(param.charAt(0)) + param.substring(1);
    }

    /**
     * 字符串去空格
     *
     * <pre>
     * StringUtil.trim(null)          = ""
     * StringUtil.trim("")            = ""
     * StringUtil.trim("     ")       = ""
     * StringUtil.trim("abc")         = "abc"
     * StringUtil.trim("    abc    ") = "abc"
     * </pre>
     *
     * @param param 字符串参数
     * @return 去空格以后的字符串
     */
    public static String trim(final String param) {
        return isEmpty(param) ? EMPTY : param.trim();
    }

    /**
     * 截取字符串
     *
     * @param param 需要截取的字符串参数
     * @param start 开始索引
     * @return 截取后的字符串
     */
    public static String substring(final String param, int start) {
        return (isEmpty(param) || start >= param.length()) ? EMPTY : param.substring(Math.max(start, 0));
    }

    /**
     * 截取字符串
     *
     * @param param 需要截取的字符串参数
     * @param start 开始索引
     * @param end   结束索引
     * @return 截取后的字符串
     */
    public static String substring(final String param, int start, int end) {
        return (isEmpty(param) || start >= end) ? EMPTY : param.substring(Math.max(start, 0), Math.min(end, param.length()));
    }

    /**
     * 截取字符串中分割标记前面的值
     *
     * @param param     需要截取的字符串参数
     * @param separator 分割标记
     * @return 截取后的字符串
     */
    public static String substringAfter(final String param, final String separator) {
        if (isEmpty(param) || isEmpty(separator)) {
            return EMPTY;
        }
        int ind = param.indexOf(separator);
        return (ind < 0) ? EMPTY : substring(param, (ind + separator.length()));
    }

    /**
     * 截取字符串中分割标记后面的值
     *
     * @param param     需要截取的字符串参数
     * @param separator 分割标记
     * @return 截取后的字符串
     */
    public static String substringBefore(final String param, final String separator) {
        if (isEmpty(param)) {
            return EMPTY;
        }
        if (isEmpty(separator)) {
            return param;
        }
        int ind = param.indexOf(separator);
        return (ind < 0) ? param : substring(param, 0, ind);
    }

    /**
     * 安全的分割字符串，忽略空白字符
     *
     * @param param     需要分割的字符串
     * @param separator 分割符
     * @return 分割后的字符串数组
     */
    public static List<String> split(final String param, final char separator) {
        return split(param, toString(separator));
    }

    /**
     * 安全的分割字符串分割，忽略空白字符
     *
     * @param param     需要分割的字符串
     * @param separator 分割符
     * @return 分割后的字符串数组
     */
    public static List<String> split(final String param, final String separator) {
        String[] temp = isEmpty(param) ? new String[0] : param.split(isEmpty(separator) ? SPACE : separator);
        List<String> list = CollectionUtil.newArrayList();
        for (String s : temp) {
            if (isEmpty(s)) {
                continue;
            }
            list.add(s.trim());
        }
        return list;
    }

    /**
     * 遍历字符串数组的字符，判断数组中的字符是否contains指定参数，只有有一个包含，就返回true
     *
     * @param arg    指定参数
     * @param params 字符串数组
     * @return boolean 是否包含
     */
    public static boolean contains(String arg, String... params) {
        if (isNull(arg)) {
            return false;
        }
        for (String param : params) {
            if (isNull(param)) {
                continue;
            }
            if (param.contains(arg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 包装指定字符串<br>
     * 当前缀和后缀一致时使用此方法
     *
     * @param str             被包装的字符串
     * @param prefixAndSuffix 前缀和后缀
     * @return 包装后的字符串
     */
    public static String wrap(CharSequence str, CharSequence prefixAndSuffix) {
        return wrap(str, prefixAndSuffix, prefixAndSuffix);
    }

    /**
     * 包装指定字符串
     *
     * @param str    被包装的字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 包装后的字符串
     */
    public static String wrap(CharSequence str, CharSequence prefix, CharSequence suffix) {
        return nullToEmpty(prefix).concat(nullToEmpty(str)).concat(nullToEmpty(suffix));
    }

    /**
     * 比较两个字符串（大小写敏感）。
     *
     * <pre>
     * equals(null, null)   = true
     * equals(null, &quot;abc&quot;)  = false
     * equals(&quot;abc&quot;, null)  = false
     * equals(&quot;abc&quot;, &quot;abc&quot;) = true
     * equals(&quot;abc&quot;, &quot;ABC&quot;) = false
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
     */
    public static boolean equals(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, false);
    }

    /**
     * 比较两个字符串（大小写不敏感）。
     *
     * <pre>
     * equalsIgnoreCase(null, null)   = true
     * equalsIgnoreCase(null, &quot;abc&quot;)  = false
     * equalsIgnoreCase(&quot;abc&quot;, null)  = false
     * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
     * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
     */
    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, true);
    }

    /**
     * 比较两个字符串是否相等。
     *
     * @param str1       要比较的字符串1
     * @param str2       要比较的字符串2
     * @param ignoreCase 是否忽略大小写
     * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
     */
    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (null == str1) {
            // 只有两个都为null才判断相等
            return str2 == null;
        }
        if (null == str2) {
            // 字符串2空，字符串1非空，直接false
            return false;
        }

        if (ignoreCase) {
            return str1.toString().equalsIgnoreCase(str2.toString());
        } else {
            return str1.equals(str2);
        }
    }

    /**
     * 将标准的getter或setter方法名称转换为对应的属性名称
     *
     * @param methodName java标准getter或setter方法名称
     * @return 生成对应的property名称
     */
    public static String propertyName(final String methodName) {
        return isEmpty(methodName) ? EMPTY : isGetterOrSetter(methodName) ? lowerFirst(methodName
                .substring(
                        methodName.startsWith(IS)
                                ? 2 : 3))
                : methodName;
    }

    /**
     * 根据属性名称获取对应的getter方法名
     *
     * @param property 属性名称
     * @return 生成property对应的getter名称
     */
    public static String getterName(final String property) {
        return isEmpty(property) ? EMPTY : GET + upperFirst(property);
    }

    /**
     * 根据属性名称和java类型，获取对应的getter方法名
     * boolean类型 生成以is开头，Boolean类型 生成以get开头
     *
     * @param property     属性名称
     * @param propertyType 属性类型
     * @return 生成property对应的getter名称
     */
    public static String getterName(final String property, final Class<?> propertyType) {
        return isEmpty(property) ? EMPTY : ((isNotNull(propertyType) && boolean.class
                .isAssignableFrom(propertyType)) ? IS : GET) + upperFirst(property);
    }

    /**
     * 根据属性名称获取对应的setter方法名称
     *
     * @param property 属性名称
     * @return 生成property对应的getter名称
     */
    public static String setterName(final String property) {
        return isEmpty(property) ? EMPTY : SET + upperFirst(property);
    }

    /**
     * 判断是否标准getter或setter方法
     *
     * @param methodName 方法名称
     * @return 是否标准属性方法
     */
    public static boolean isGetterOrSetter(String methodName) {
        return isNotEmpty(methodName) && (methodName.startsWith(GET) || methodName.startsWith(SET) || methodName.startsWith(IS));
    }

    /**
     * 判断是否数字，负数返回false
     *
     * @param param 需要判断的字符串参数
     * @return 是否数字
     */
    public static boolean isNumeric(CharSequence param) {
        if (isEmpty(param)) {
            return false;
        }
        for (int i = 0; i < param.length(); ++i) {
            if (!Character.isDigit(param.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 反转字符串<br>
     * 例如：abcd =》dcba
     *
     * @param str 被反转的字符串
     * @return 反转后的字符串
     */
    public static String reverse(String str) {
        return new String(ArrayUtil.reverse(str.toCharArray()));
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params   参数值
     * @return 格式化后的文本
     */
    public static String format(final CharSequence template, final Object... params) {
        return isEmpty(template) ? EMPTY : StringFormatter.format(template.toString(), params);
    }

    /**
     * 将Object对象转换为字符串，为空的情况下输出为""
     *
     * @param obj 需要转换的对象
     * @return 转换后的字符串
     */
    public static String toString(final Object obj) {
        return isNull(obj) ? EMPTY : obj.toString();
    }

    /**
     * 将对象转为字符串<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj         对象
     * @param charsetName 字符集
     * @return 字符串
     */
    public static String toString(final Object obj, final String charsetName) {
        return toString(obj, Charset.forName(charsetName));
    }

    /**
     * 将对象转为字符串<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj     对象
     * @param charset 字符集
     * @return 字符串
     */
    public static String toString(Object obj, Charset charset) {
        if (null == obj) {
            return EMPTY;
        }

        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof byte[]) {
            return toString((byte[]) obj, charset);
        } else if (obj instanceof Byte[]) {
            return toString((Byte[]) obj, charset);
        } else if (obj instanceof ByteBuffer) {
            return toString((ByteBuffer) obj, charset);
        } else if (ArrayUtil.isArray(obj)) {
            return ArrayUtil.toString(obj);
        }
        return obj.toString();
    }

    /**
     * 将byte数组转为字符串
     *
     * @param bytes   byte数组
     * @param charset 字符集
     * @return 字符串
     */
    public static String toString(byte[] bytes, String charset) {
        return toString(bytes, CharsetUtil.charset(charset));
    }

    /**
     * 解码字节码
     *
     * @param bytes   字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public static String toString(byte[] bytes, Charset charset) {
        return isNull(bytes) ? EMPTY : isNull(charset) ? new String(bytes) : new String(bytes, charset);
    }

    /**
     * 将Byte数组转为字符串
     *
     * @param bytes   byte数组
     * @param charset 字符集
     * @return 字符串
     */
    public static String toString(Byte[] bytes, String charset) {
        return toString(bytes, CharsetUtil.charset(charset));
    }

    /**
     * 解码字节码
     *
     * @param bytes   字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public static String toString(Byte[] bytes, Charset charset) {
        if (isNull(bytes)) {
            return EMPTY;
        }

        byte[] data = new byte[bytes.length];
        Byte dataByte;
        for (int i = 0; i < bytes.length; i++) {
            dataByte = bytes[i];
            data[i] = (null == dataByte) ? -1 : dataByte;
        }

        return toString(data, charset);
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data    数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String toString(ByteBuffer data, String charset) {
        return isNull(data) ? EMPTY : toString(data, CharsetUtil.charset(charset));
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data    数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String toString(ByteBuffer data, Charset charset) {
        if (isNull(charset)) {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }

    /**
     * 将对象转为字符串<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj 对象
     * @return 字符串
     */
    public static String toUtf8String(Object obj) {
        return toString(obj, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 字符串转大写
     *
     * @param param 字符串参数
     * @return 转大写以后的字符串
     */
    public static String toUpperCase(final String param) {
        return isEmpty(param) ? EMPTY : param.toUpperCase();
    }

    /**
     * 字符串转小写
     *
     * @param param 字符串参数
     * @return 转小写以后的字符串
     */
    public static String toLowerCase(final String param) {
        return isEmpty(param) ? EMPTY : param.toLowerCase();
    }
}
