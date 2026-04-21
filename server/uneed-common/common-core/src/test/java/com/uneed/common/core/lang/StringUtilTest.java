package com.uneed.common.core.lang;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * StringUtil Tester.
 *
 * @author huangad@coracle.com
 * @date 10/10/2019
 */
public class StringUtilTest {

    @Before
    public void before() {
        //TODO: Test before goes here...
    }

    @After
    public void after() {
        //TODO: Test after goes here...
    }


    /**
     * 测试字符串是空白字符
     */
    @Test
    public void testisEmpty() {
        // null
        String str = null;
        assertTrue(StringUtil.isEmpty(str));
        //空字符串
        str = "";
        assertTrue(StringUtil.isEmpty(str));
        //空格
        str = " ";
        assertTrue(StringUtil.isEmpty(str));
        //多个tab
        str = "         ";
        StringUtil.isEmpty(str);
        assertTrue(StringUtil.isEmpty(str));
        StringBuilder sb = null;
        assertTrue(StringUtil.isEmpty(sb));
        sb = new StringBuilder();
        assertTrue(StringUtil.isEmpty(sb));
        sb = new StringBuilder(" ");
        assertTrue(StringUtil.isEmpty(sb));
    }

    /**
     * Method: isNotEmpty(CharSequence cs)
     */
    @Test
    public void testisNotEmpty() {
        // null
        String str = null;
        assertFalse(StringUtil.isNotEmpty(str));
        //空字符串
        str = "";
        assertFalse(StringUtil.isNotEmpty(str));
        //空格
        str = " ";
        assertFalse(StringUtil.isNotEmpty(str));
        //多个tab
        str = "         ";
        assertFalse(StringUtil.isNotEmpty(str));
        StringBuilder sb = null;
        assertFalse(StringUtil.isNotEmpty(sb));
        sb = new StringBuilder();
        assertFalse(StringUtil.isNotEmpty(sb));
        sb = new StringBuilder(" ");
        assertFalse(StringUtil.isNotEmpty(sb));
    }


    /**
     * Method: nullToEmpty(CharSequence str)
     */
    @Test
    public void testNullToEmpty() {

        CharSequence str = "aaaa";
        String s1 = StringUtil.nullToEmpty(null);
        String s2 = StringUtil.nullToEmpty(str);

        assertEquals("", s1);
        assertEquals("aaaa", s2);
    }

    /**
     * Method: nullToDefault(CharSequence str, String defaultStr)
     */
    @Test
    public void testNullToDefault() {

        String defaultStr = "hahah";
        assertEquals(defaultStr, StringUtil.nullToDefault(null, defaultStr));

        CharSequence str = "aaaa";
        assertEquals("aaaa", StringUtil.nullToDefault(str, defaultStr));

    }

    /**
     * 测试驼峰字符转下划线
     */
    @Test
    public void testCamelToUnderlineParam() {
        String str = "camelCharacter";
        assertEquals("camel_character", StringUtil.camelToUnderline(str));
        str = "ABCDE";
        assertEquals("a_b_c_d_e", StringUtil.camelToUnderline(str));
    }


    /**
     * Method: camelToCharacter(final char ch, final String param)
     */
    @Test
    public void testCamelToCharacter() {
        //TODO: Test goes here...
          /*
        try {
           Method method = StringUtil.getClass().getMethod("camelToCharacter", char.class, String.class);
           method.setAccessible(true);
           method.invoke(<Object>, <Parameters>);
        } catch(NoSuchMethodException e) {
        } catch(IllegalAccessException e) {
        } catch(InvocationTargetException e) {
        }
        */
        char ch = '+';
        String str = "camelCharacter";

        assertEquals("camel+character", StringUtil.camelToCharacter(ch, str));

    }

    /**
     * 测试驼峰字符数组转下划线
     */
    @Test
    public void testCamelToUnderlineParams() {
        String[] str = new String[]{"camelCharacter", "ABCDE"};
        String[] result = StringUtil.camelArrayToUnderline(str);
        assertEquals("camel_character", result[0]);
        assertEquals("a_b_c_d_e", result[1]);
    }


    /**
     * Method: camelArrayToCharacter(final char ch, final String... params)
     */
    @Test
    public void testCamelArrayToCharacter() {
        char p = '+';
        String[] str = new String[]{"camelCharacter", "ABCDE"};

        String[] result = StringUtil.camelArrayToCharacter(p, str);
        assertEquals("camel+character", result[0]);
        assertEquals("a+b+c+d+e", result[1]);
    }

    /**
     * Method: underlineToCamel(final String param)
     */
    @Test
    public void testUnderlineToCamel() {
        String str = "camel_character";
        assertEquals("camelCharacter", StringUtil.underlineToCamel(str));
        str = "a_b_c_d_e";
        assertEquals("aBCDE", StringUtil.underlineToCamel(str));
    }

    /**
     * Method: underlineArrayToCamel(final String param)
     */
    @Test
    public void testUnderlineArrayToCamel() {
        String str = "a_b_c_d_e";
        String[] result = StringUtil.underlineArrayToCamel(str);
        assertEquals("aBCDE", result[0]);

    }

    /**
     * Method: characterToCamel(final char ch, final String param)
     */
    @Test
    public void testCharacterToCamel() {
           /*
        try {
           Method method = StringUtil.getClass().getMethod("characterToCamel", char.class, String.class);
           method.setAccessible(true);
           method.invoke(<Object>, <Parameters>);
        } catch(NoSuchMethodException e) {
        } catch(IllegalAccessException e) {
        } catch(InvocationTargetException e) {
        }
        */
        char ch = '+';
        String str = "camel+character";
        assertEquals("camelCharacter", StringUtil.characterToCamel(ch, str));


    }

    /**
     * Method: characterArrayToCamel(final char ch, final String... params)
     */
    @Test
    public void testCharacterArrayToCamel() {
        char p = '+';
        String[] str = new String[]{"camel+character", "a+b+c+d+e"};

        String[] result = StringUtil.characterArrayToCamel(p, str);
        assertEquals("camelCharacter", result[0]);
        assertEquals("aBCDE", result[1]);


    }

    /**
     * Method: upperFirst(final String param)
     */
    @Test
    public void testUpperFirst() {
        String param = "test";
        Assert.assertEquals("Test", StringUtil.upperFirst(param));
        param = "";
        Assert.assertEquals(param, StringUtil.upperFirst(param));
    }

    /**
     * Method: lowerFirst(final String param)
     */
    @Test
    public void testLowerFirst() {
        String param = "Test";
        Assert.assertEquals("test", StringUtil.lowerFirst(param));
        param = "";
        Assert.assertEquals(param, StringUtil.lowerFirst(param));

    }

    /**
     * Method: trim(final String param)
     */
    @Test
    public void testTrim() {
        assertEquals("", StringUtil.trim(null));
        assertEquals("", StringUtil.trim(" "));
        assertEquals("", StringUtil.trim(""));
        assertEquals("abc", StringUtil.trim("abc"));
        assertEquals("abc", StringUtil.trim("abc  "));
    }

    /**
     * Method: substring(final String param, int start)
     */
    @Test
    public void testSubstringForParamStart() {
        String param = "abc";
        Assert.assertEquals("", StringUtil.substring(param, 0, 0));
        Assert.assertEquals("a", StringUtil.substring(param, 0, 1));
        Assert.assertEquals("ab", StringUtil.substring(param, 0, 2));
        Assert.assertEquals("abc", StringUtil.substring(param, 0, 3));
        Assert.assertEquals("abc", StringUtil.substring(param, 0, 4));
        Assert.assertEquals("abc", StringUtil.substring(param, -2, 4));
        Assert.assertEquals("", StringUtil.substring(param, -2, -4));
        Assert.assertEquals("", StringUtil.substring(param, 4, 1));
        ////////////////////////////////////////////////////////
        Assert.assertEquals("abc", StringUtil.substring(param, -10));
        Assert.assertEquals("abc", StringUtil.substring(param, 0));
        Assert.assertEquals("bc", StringUtil.substring(param, 1));
        Assert.assertEquals("c", StringUtil.substring(param, 2));
        Assert.assertEquals("", StringUtil.substring(param, 3));
        Assert.assertEquals("", StringUtil.substring(param, 4));
        param = null;
        Assert.assertEquals("", StringUtil.substring(param, 4, 1));
        Assert.assertEquals("", StringUtil.substring(param, 4));
    }

    /**
     * Method: substringAfter(final String param, final String separator)
     */
    @Test
    public void testSubstringAfter() {
        String param = "abcd";
        Assert.assertEquals("", StringUtil.substringAfter(param, null));
        Assert.assertEquals("", StringUtil.substringAfter(param, ""));
        Assert.assertEquals("bcd", StringUtil.substringAfter(param, "a"));
        Assert.assertEquals("d", StringUtil.substringAfter(param, "c"));
        Assert.assertEquals("", StringUtil.substringAfter(param, "d"));
        Assert.assertEquals("", StringUtil.substringAfter(param, "cbc"));
        Assert.assertEquals("", StringUtil.substringAfter(param, "abcd"));
        Assert.assertEquals("", StringUtil.substringAfter(param, "e"));
        Assert.assertEquals("", StringUtil.substringAfter(null, "e"));
    }

    /**
     * Method: substringBefore(final String param, final String separator)
     */
    @Test
    public void testSubstringBefore() {
        String param = "abcd";
        Assert.assertEquals("abcd", StringUtil.substringBefore(param, null));
        Assert.assertEquals("abcd", StringUtil.substringBefore(param, ""));
        Assert.assertEquals("", StringUtil.substringBefore(param, "a"));
        Assert.assertEquals("ab", StringUtil.substringBefore(param, "c"));
        Assert.assertEquals("abc", StringUtil.substringBefore(param, "d"));
        Assert.assertEquals("abcd", StringUtil.substringBefore(param, "cbc"));
        Assert.assertEquals("", StringUtil.substringBefore(param, "abcd"));
        Assert.assertEquals("abcd", StringUtil.substringBefore(param, "e"));
        Assert.assertEquals("", StringUtil.substringBefore(null, "e"));
    }

    /**
     * Method: split(final String param, final char separator)
     */
    @Test
    public void testSplitForParamSeparator() {
        String s = "a,b,c, d";
        char p = ',';
        List<String> split = StringUtil.split(s, p);
        Object[] array = split.toArray();

        String[] arrayString = new String[]{"a", "b", "c", "d"};

        assertArrayEquals(arrayString, array);

        String p1 = ",";
        List<String> split1 = StringUtil.split(s, p1);
        Object[] array1 = split1.toArray();

        assertArrayEquals(arrayString, array1);
    }

    /**
     * Method: wrap(CharSequence str, CharSequence prefixAndSuffix)
     */
    @Test
    public void testWrapForStrPrefixAndSuffix() {
        CharSequence st = "hahahhaha";
        CharSequence prefix = "@@";

        assertEquals("@@hahahhaha@@", StringUtil.wrap(st, prefix));
    }

    /**
     * Method: wrap(CharSequence str, CharSequence prefix, CharSequence suffix)
     */
    @Test
    public void testWrapForStrPrefixSuffix() {
        CharSequence st = "hahahhaha";
        CharSequence pre = "@@";
        CharSequence suf = "~~";

        assertEquals("@@hahahhaha~~", StringUtil.wrap(st, pre, suf));
    }

    /**
     * Method: equals(CharSequence str1, CharSequence str2)
     */
    @Test
    public void testEqualsForStr1Str2() {
        assertTrue(StringUtil.equals(null, null));

        CharSequence a = "aaa";
        CharSequence b = "aaa";
        CharSequence c = "AAA";
        assertFalse(StringUtil.equals(null, a));

        assertTrue(StringUtil.equals(a, b));

        assertFalse(StringUtil.equals(a, c));


    }

    /**
     * Method: equalsIgnoreCase(CharSequence str1, CharSequence str2)
     */
    @Test
    public void testEqualsIgnoreCase() {
        assertTrue(StringUtil.equalsIgnoreCase(null, null));

        CharSequence a = "aaa";
        CharSequence b = "aaa";
        CharSequence c = "AAA";
        assertFalse(StringUtil.equalsIgnoreCase(null, a));

        assertTrue(StringUtil.equalsIgnoreCase(a, b));

        assertTrue(StringUtil.equalsIgnoreCase(a, c));
    }

    /**
     * Method: equals(CharSequence str1, CharSequence str2, boolean ignoreCase)
     */
    @Test
    public void testEqualsForStr1Str2IgnoreCase() {

        CharSequence a = "aaa";
        CharSequence b = "aaa";
        CharSequence c = "AAA";


        assertTrue(StringUtil.equals(null, null, true));
        assertTrue(StringUtil.equals(null, null, false));

        assertFalse(StringUtil.equals(null, a, true));
        assertFalse(StringUtil.equals(null, a, false));

        assertTrue(StringUtil.equals(a, b, true));
        assertTrue(StringUtil.equals(a, b, false));

        assertTrue(StringUtil.equals(a, c, true));
        assertFalse(StringUtil.equals(a, c, false));

    }

    /**
     * Method: propertyName(final String methodName)
     */
    @Test
    public void testPropertyName() {
        Assert.assertEquals("name", StringUtil.propertyName("getName"));
        Assert.assertEquals("name", StringUtil.propertyName("setName"));
        Assert.assertEquals("name", StringUtil.propertyName("isName"));
        Assert.assertEquals("name", StringUtil.propertyName("name"));
        Assert.assertEquals("", StringUtil.propertyName(""));
        Assert.assertEquals("a", StringUtil.propertyName("a"));
        Assert.assertEquals("", StringUtil.propertyName(null));
    }

    /**
     * Method: getterName(final String property)
     */
    @Test
    public void testGetterNameProperty() {
        Assert.assertEquals("getName", StringUtil.getterName("name"));
        Assert.assertEquals("isName", StringUtil.getterName("name", boolean.class));
        Assert.assertEquals("getName", StringUtil.getterName("name", Boolean.class));
        Assert.assertEquals("getName", StringUtil.getterName("name", Integer.class));
        Assert.assertEquals("getName", StringUtil.getterName("name", null));

        Assert.assertEquals("", StringUtil.getterName(""));
        Assert.assertEquals("", StringUtil.getterName("", boolean.class));
        Assert.assertEquals("", StringUtil.getterName("", Boolean.class));
        Assert.assertEquals("", StringUtil.getterName("", Integer.class));
        Assert.assertEquals("", StringUtil.getterName("", null));

        Assert.assertEquals("", StringUtil.getterName(null));
        Assert.assertEquals("", StringUtil.getterName(null, boolean.class));
        Assert.assertEquals("", StringUtil.getterName(null, Boolean.class));
        Assert.assertEquals("", StringUtil.getterName(null, Integer.class));
        Assert.assertEquals("", StringUtil.getterName(null, null));
    }


    /**
     * Method: setterName(final String property)
     */
    @Test
    public void testSetterName() {
        Assert.assertEquals("setName", StringUtil.setterName("name"));
        Assert.assertEquals("", StringUtil.setterName(""));
        Assert.assertEquals("", StringUtil.setterName(null));
    }

    /**
     * Method: isGetterOrSetter(String methodName)
     */
    @Test
    public void testIsGetterOrSetter() {
        String methodName1 = "getId";
        String methodName2 = "setId";
        String methodName3 = "SetId";

        assertTrue(StringUtil.isGetterOrSetter(methodName1));
        assertTrue(StringUtil.isGetterOrSetter(methodName2));
        assertFalse(StringUtil.isGetterOrSetter(methodName3));

    }

    /**
     * Method: isNumeric(CharSequence param)
     */
    @Test
    public void testIsNumeric() {
        Assert.assertTrue(StringUtil.isNumeric("123"));
        Assert.assertTrue(StringUtil.isNumeric("0000"));
        Assert.assertTrue(StringUtil.isNumeric("0"));
        Assert.assertFalse(StringUtil.isNumeric("-123"));
        Assert.assertFalse(StringUtil.isNumeric(""));
        Assert.assertFalse(StringUtil.isNumeric(" 1"));
        Assert.assertFalse(StringUtil.isNumeric(" 1 "));
        Assert.assertFalse(StringUtil.isNumeric(null));
    }

    /**
     * Method: reverse(String str)
     */
    @Test
    public void testReverse() {
        String str = "abcd";
        assertEquals("dcba", StringUtil.reverse(str));
    }

    /**
     * Method: format(final CharSequence template, final Object... params)
     */
    @Test
    public void testFormat() {
        String a = "this is {} for {}";
        String paramsA = "a";
        String paramsB = "b";
        String format = StringUtil.format(a, paramsA, paramsB);

        String reString = "this is a for b";
        assertEquals(reString, format);
    }

    /**
     * Method: toString(final Object obj)
     */
    @Test
    public void testToStringObj() {
        Object obj = 333;
        Object objChar = 'h';


        assertEquals("", StringUtil.toString(null));
        assertEquals("333", StringUtil.toString(obj));
        assertEquals("h", StringUtil.toString(objChar));
    }

    /**
     * Method: toString(final Object obj, final String charsetName)
     */
    @Test
    public void testToStringForObjCharsetName() {
        String objStr = "abc话";

        String charsetUTF16 = "utf-16";
        String charsetUTF8 = "utf-8";

        byte[] bytes = new byte[]{1, 5, 8};
        Byte[] byteBig = new Byte[]{22, 22, 127};
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);

        Object obj[] = new Object[3];
        Integer integer = 55;
        Double param = 59d;
        obj[1] = integer;
        obj[2] = param;


        assertEquals(StringUtil.toString(objStr, charsetUTF16), StringUtil.toString(objStr, charsetUTF8));
        assertNotEquals(StringUtil.toString(bytes, charsetUTF16), StringUtil.toString(bytes, charsetUTF8));
        assertNotEquals(StringUtil.toString(byteBig, charsetUTF16), StringUtil.toString(byteBig, charsetUTF8));
        assertNotEquals(StringUtil.toString(byteBuffer, charsetUTF16), StringUtil.toString(byteBuffer, charsetUTF8));
        assertEquals(StringUtil.toString(obj, charsetUTF16), StringUtil.toString(obj, charsetUTF8));


    }

    /**
     * Method: toString(Object obj, Charset charset)
     */
    @Test
    public void testToStringForObjCharset() {
        String objStr = "abc话";

        Charset charsetUTF16 = Charset.forName("utf-16");
        Charset charsetUTF8 = Charset.forName("utf-8");


        byte[] bytes = new byte[]{1, 5, 8};
        Byte[] byteBig = new Byte[]{22, 22, 127};
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);

        Object obj[] = new Object[3];
        Integer integer = 55;
        Double param = 59d;
        obj[1] = integer;
        obj[2] = param;


        assertEquals(StringUtil.toString(objStr, charsetUTF16), StringUtil.toString(objStr, charsetUTF8));
        assertNotEquals(StringUtil.toString(bytes, charsetUTF16), StringUtil.toString(bytes, charsetUTF8));
        assertNotEquals(StringUtil.toString(byteBig, charsetUTF16), StringUtil.toString(byteBig, charsetUTF8));
        assertNotEquals(StringUtil.toString(byteBuffer, charsetUTF16), StringUtil.toString(byteBuffer, charsetUTF8));
        assertEquals(StringUtil.toString(obj, charsetUTF16), StringUtil.toString(obj, charsetUTF8));

    }

    /**
     * Method: toUtf8String(Object obj)
     */
    @Test
    public void testToUtf8String() {
        Object object = 'h';
        Object objectInt = 56;

        assertEquals("h", StringUtil.toUtf8String(object));
        assertEquals("56", StringUtil.toUtf8String(objectInt));

    }

}
