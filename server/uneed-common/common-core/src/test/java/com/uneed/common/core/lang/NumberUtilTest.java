package com.uneed.common.core.lang;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

import static org.junit.Assert.*;


/**
 * NumberUtil Tester.
 *
 * @author huangad@coracle.com
 * @date 09/27/2019
 */
public class NumberUtilTest {

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    /**
     * Method: add(float v1, float v2)
     */
    @Test
    public void testAddForV1V2() {
        //float+float
        float a = 1.2f;
        float b = 2.2f;
        assertEquals(3.4d, NumberUtil.add(a, b), 0.001);

        //float+double
        double c = 3.3d;
        assertEquals(4.5d, NumberUtil.add(a, c), 0.001);

        //double+float
        assertEquals(4.5d, NumberUtil.add(c, a), 0.001);

        //double+double
        double d = 4.4d;
        assertEquals(7.7d, NumberUtil.add(c, d), 0.001);

        //Double+Double
        Double e = 5.5d;
        Double f = 5.5d;
        Double g = null;
        assertEquals(5.5d, NumberUtil.add(e, null), 0.01);
        assertEquals(0.0d, NumberUtil.add(g, null), 0.01);
        assertEquals(11.0d, NumberUtil.add(e, f), 0.001);

        //Number+Number
        Number number1 = 1.21;
        Number number2 = 2.21;
        Number number3 = null;
        BigDecimal bigDecimal1 = new BigDecimal("3.42");
        BigDecimal bigDecimal2 = new BigDecimal("1.21");
        BigDecimal bigDecimal3 = new BigDecimal("0");

        assertEquals(bigDecimal1, NumberUtil.add(number1, number2));
        assertEquals(bigDecimal2, NumberUtil.add(number1, null));
        assertEquals(bigDecimal3, NumberUtil.add(number3, null));


    }

    /**
     * Method: add(Number... values)
     */
    @Test
    public void testAddValues() {
        //Number...Values
        Number number1 = 1.21;
        Number number2 = 2.21;
        Number number3 = null;
        Number number4 = null;
        Number number5 = 3.14;
        BigDecimal bigDecimal2 = new BigDecimal("1.21");
        BigDecimal bigDecimal3 = new BigDecimal("0");
        BigDecimal bigDecimal4 = new BigDecimal("6.56");
        assertEquals(bigDecimal4, NumberUtil.add(number1, number2, number5));

        Number number6 = null;
        assertEquals(bigDecimal3, NumberUtil.add(number3, number4, number6));
        assertEquals(bigDecimal2, NumberUtil.add(number1, number3, number4));

        //String...Values
        String s1 = null;
        String s2 = null;
        String s3 = null;
        assertEquals(bigDecimal3, NumberUtil.add(s1, s2, s3));

        String s4 = "3.14";
        String s5 = "3.14";
        String s6 = "3.14";
        BigDecimal bigDecimal7 = new BigDecimal("9.42");
        assertEquals(bigDecimal7, NumberUtil.add(s4, s5, s6));

        BigDecimal bigDecimal8 = new BigDecimal("3.14");
        assertEquals(bigDecimal8, NumberUtil.add(s1, s2, s4));

        //BigDecimal...Values
        BigDecimal bigDecimal9 = new BigDecimal("1");
        BigDecimal bigDecimal10 = new BigDecimal("13.56");
        assertEquals(bigDecimal10, NumberUtil.add(bigDecimal7, bigDecimal8, bigDecimal9));

        BigDecimal bigDecimal11 = null;
        BigDecimal bigDecimal12 = null;
        BigDecimal bigDecimal13 = null;
        BigDecimal bigDecimal14 = new BigDecimal("0");
        BigDecimal bigDecimal15 = new BigDecimal("1");

        assertEquals(bigDecimal14, NumberUtil.add(bigDecimal11, bigDecimal12, bigDecimal13));
        assertEquals(bigDecimal15, NumberUtil.add(bigDecimal11, bigDecimal12, bigDecimal9));

    }

    /**
     * Method: sub(float v1, float v2)
     */
    @Test
    public void testSubForV1V2() {
        //float-float
        float a = 1.2f;
        float b = 2.2f;
        assertEquals(1.0d, NumberUtil.sub(b, a), 0.001);

        //float-double
        double c = 3.3d;
        assertEquals(-2.1d, NumberUtil.sub(a, c), 0.001);

        //double-float
        assertEquals(2.1d, NumberUtil.sub(c, a), 0.001);

        //double-double
        double d = 4.4d;
        assertEquals(-1.1d, NumberUtil.sub(c, d), 0.001);

        //Double-Double
        Double e = 5.5d;
        Double f = 5.5d;
        Double g = null;
        Double h = null;
        assertEquals(5.5d, NumberUtil.sub(e, g), 0.001);
        assertEquals(0.0d, NumberUtil.sub(g, h), 0.001);
        assertEquals(0.0d, NumberUtil.sub(e, f), 0.001);

        //Number-Number
        Number number1 = 1.21;
        Number number2 = 2.21;
        Number number3 = null;
        Number number4 = null;
        BigDecimal bigDecimal1 = new BigDecimal("1.00");
        BigDecimal multiply = bigDecimal1.multiply(new BigDecimal("-1"));
        BigDecimal bigDecimal2 = new BigDecimal("1.21");
        BigDecimal bigDecimal3 = new BigDecimal("0");

        assertEquals(multiply, NumberUtil.sub(number1, number2));
        assertEquals(bigDecimal2, NumberUtil.sub(number1, number3));
        assertEquals(bigDecimal3, NumberUtil.sub(number3, number4));


    }

    /**
     * Method: sub(Number... values)
     */
    @Test
    public void testSubValues() {
        //Number...Values
        Number number1 = 1.00;
        Number number2 = 2.00;
        Number number3 = null;
        Number number4 = null;
        Number number5 = 3.14;
        BigDecimal bigDecimal2 = new BigDecimal("1.0");
        BigDecimal bigDecimal3 = new BigDecimal("0");
        BigDecimal bigDecimal4 = new BigDecimal("0.14");
        assertEquals(bigDecimal4, NumberUtil.sub(number5, number2, number1));

        Number number6 = null;
        assertEquals(bigDecimal3, NumberUtil.sub(number3, number4, number6));
        assertEquals(bigDecimal2, NumberUtil.sub(number1, number3, number4));

        //String...Values
        String s1 = null;
        String s2 = null;
        String s3 = null;
        assertEquals(bigDecimal3, NumberUtil.sub(s1, s2, s3));

        String s4 = "5.20";
        String s5 = "2.10";
        String s6 = "1.10";
        BigDecimal bigDecimal7 = new BigDecimal("2.00");
        assertEquals(bigDecimal7, NumberUtil.sub(s4, s5, s6));

        BigDecimal bigDecimal8 = new BigDecimal("0.00");
        assertEquals(bigDecimal8, NumberUtil.sub(s4, s2, s4));

        //BigDecimal...Values
        BigDecimal bigDecimal9 = new BigDecimal("1.00");
        assertEquals(bigDecimal9, NumberUtil.sub(bigDecimal7, bigDecimal9, bigDecimal8));

        BigDecimal bigDecimal11 = null;
        BigDecimal bigDecimal12 = null;
        BigDecimal bigDecimal13 = null;
        BigDecimal bigDecimal14 = new BigDecimal("0");
        BigDecimal bigDecimal15 = new BigDecimal("1.00");

        assertEquals(bigDecimal14, NumberUtil.sub(bigDecimal11, bigDecimal12, bigDecimal13));
        assertEquals(bigDecimal15, NumberUtil.sub(bigDecimal9, bigDecimal11, bigDecimal12));

    }

    /**
     * Method: mul(float v1, float v2)
     */
    @Test
    public void testMulForV1V2() {
        //float float
        float a = 1.2f;
        float b = 2.2f;
        assertEquals(2.64d, NumberUtil.mul(a, b), 0.001);

        //float+double
        double c = 3.3d;
        assertEquals(3.96d, NumberUtil.mul(a, c), 0.001);

        //double+float
        assertEquals(3.96d, NumberUtil.mul(c, a), 0.001);

        //double+double
        double d = 4.4d;
        assertEquals(14.52, NumberUtil.mul(c, d), 0.001);

        //Double+Double
        Double e = 5.5d;
        Double f = 5.5d;
        Double g = null;

        assertEquals(5.5d, NumberUtil.mul(e, null), 0.01);
        assertEquals(0.0d, NumberUtil.mul(g, null), 0.01);
        assertEquals(30.25, NumberUtil.mul(e, f), 0.001);

        //Number+Number
        Number number1 = 1.21;
        Number number2 = 2.21;
        Number number3 = null;
        Number number4 = null;
        BigDecimal bigDecimal1 = new BigDecimal("2.6741");
        BigDecimal bigDecimal2 = new BigDecimal("1.21");
        BigDecimal bigDecimal3 = new BigDecimal("0");

        assertEquals(bigDecimal1, NumberUtil.mul(number1, number2));
        assertEquals(bigDecimal2, NumberUtil.mul(number1, number3));
        assertEquals(bigDecimal3, NumberUtil.mul(number3, number4));


    }

    /**
     * Method: mul(Number... values)
     */
    @Test
    public void testMulValues() {
        //Number...Values
        Number number1 = 1.21;
        Number number2 = 2.21;
        Number number3 = null;
        Number number4 = null;
        Number number5 = 3.14;
        BigDecimal bigDecimal2 = new BigDecimal("1.21");
        BigDecimal bigDecimal3 = new BigDecimal("0");
        BigDecimal bigDecimal4 = new BigDecimal("8.396674");
        assertEquals(bigDecimal4, NumberUtil.mul(number1, number2, number5));

        Number number6 = null;
        assertEquals(bigDecimal3, NumberUtil.mul(number3, number4, number6));
        assertEquals(bigDecimal2, NumberUtil.mul(number1, number3, number4));

        //String...Values
        String s1 = null;
        String s2 = null;
        String s3 = null;
        assertEquals(bigDecimal3, NumberUtil.mul(s1, null, null));

        String s4 = "3.14";
        String s5 = "3.14";
        String s6 = "3.14";
        BigDecimal bigDecimal7 = new BigDecimal("30.959144");
        assertEquals(bigDecimal7, NumberUtil.mul(s4, s5, s6));

        BigDecimal bigDecimal8 = new BigDecimal("0.00");
        assertEquals(bigDecimal8, NumberUtil.mul(s1, s2, s4));

        //BigDecimal...Values
        BigDecimal bigDecimal9 = new BigDecimal("1");
        BigDecimal bigDecimal10 = new BigDecimal("0.00");
        BigDecimal bigDecimala = new BigDecimal("2");
        assertEquals(bigDecimal10, NumberUtil.mul(bigDecimala, bigDecimal8, bigDecimal9));

        BigDecimal bigDecimal11 = null;
        BigDecimal bigDecimal12 = null;
        BigDecimal bigDecimal13 = null;
        BigDecimal bigDecimal14 = new BigDecimal("0");

        assertEquals(bigDecimal14, NumberUtil.mul(bigDecimal11, bigDecimal12, bigDecimal13));
        assertEquals(bigDecimal14, NumberUtil.mul(bigDecimal11, bigDecimal12, bigDecimal9));

    }

    /**
     * Method: div(float v1, float v2)
     */
    @Test
    public void testDivForV1V2() {
        //float+float
        float a = 1.2f;
        float b = 2.2f;
        assertEquals(0.5454545455d, NumberUtil.div(a, b), 0.001);

        //float+double
        double c = 3.3d;
        assertEquals(0.3636363636d, NumberUtil.div(a, c), 0.001);

        //double+float
        assertEquals(2.75d, NumberUtil.div(c, a), 0.001);

        //double+double
        double d = 4.4d;
        assertEquals(0.75d, NumberUtil.div(c, d), 0.001);


        //Double+Double
        Double e = 5.5d;
        Double f = 5.5d;
        Double g = 0d;

        assertEquals(1.00d, NumberUtil.div(e, f), 0.01);
        assertEquals(0.0d, NumberUtil.div(g, e), 0.01);


        //Number+Number
        Number number1 = 1.21;
        Number number2 = 2.21;


        BigDecimal bigDecimal1 = new BigDecimal("0.5475113122");


        assertEquals(bigDecimal1, NumberUtil.div(number1, number2));

        //String+String

        String s4 = "3.14";
        String s5 = "3.14";
        BigDecimal bigDecimal7 = new BigDecimal("1.0000000000");
        assertEquals(bigDecimal7, NumberUtil.div(s4, s5));


    }

    /**
     * Method: div(float v1, float v2, int scale)
     */
    @Test
    public void testDivForV1V2Scale() {
        //float+float
        float a = 1.21f;
        float b = 2.2f;
        assertEquals(0.55d, NumberUtil.div(a, b, 2), 0.001);

        //float+double
        double c = 3.3d;
        assertEquals(0.37d, NumberUtil.div(a, c, 2), 0.001);

        //double+float
        assertEquals(2.7d, NumberUtil.div(c, a, 1), 0.001);

        //double+double
        double d = 4.4d;
        assertEquals(0.75d, NumberUtil.div(c, d, 3), 0.001);


        //Double+Double
        Double e = 5.5d;
        Double f = 5.5d;
        Double g = 0d;

        assertEquals(1.00d, NumberUtil.div(e, f, 2), 0.01);
        assertEquals(0.0d, NumberUtil.div(g, e, 2), 0.01);


        //Number+Number
        Number number1 = 1.21;
        Number number2 = 2.21;
        Number number3 = 0;
        Number number4 = 1;

        BigDecimal bigDecimal1 = new BigDecimal("0.55");

        BigDecimal bigDecimal3 = new BigDecimal("0.00");

        assertEquals(bigDecimal1, NumberUtil.div(number1, number2, 2));
        assertEquals(bigDecimal3, NumberUtil.div(number3, number4, 2));

        //String+String

        String s4 = "3.14";
        String s5 = "3.19";
        BigDecimal bigDecimal7 = new BigDecimal("0.98");
        assertEquals(bigDecimal7, NumberUtil.div(s4, s5, 2));


    }

    /**
     * Method: div(float v1, float v2, int scale, RoundingMode roundingMode)
     */
    @Test
    public void testDivForV1V2ScaleRoundingMode() {
        //float+float
        float a = 1.20f;
        float b = 2.2f;
        assertEquals(0.54d, NumberUtil.div(a, b, 2, RoundingMode.FLOOR), 0.001);

        //float+double
        double c = 3.3d;
        assertEquals(0.36d, NumberUtil.div(a, c, 2, RoundingMode.FLOOR), 0.001);

        //double+float
        assertEquals(2.7d, NumberUtil.div(c, a, 1, RoundingMode.FLOOR), 0.001);

        //double+double
        double d = 4.4d;
        assertEquals(0.75d, NumberUtil.div(c, d, 3, RoundingMode.FLOOR), 0.001);


        //Double+Double
        Double e = 5.5d;
        Double f = 5.5d;
        Double g = 0d;

        assertEquals(1.00d, NumberUtil.div(e, f, 2, RoundingMode.FLOOR), 0.01);
        assertEquals(0.0d, NumberUtil.div(g, e, 2, RoundingMode.FLOOR), 0.01);


        //Number+Number
        Number number1 = 1.21;
        Number number2 = 2.21;
        Number number3 = 0;
        Number number4 = 1;

        BigDecimal bigDecimal1 = new BigDecimal("0.54");

        BigDecimal bigDecimal3 = new BigDecimal("0.00");

        assertEquals(bigDecimal1, NumberUtil.div(number1, number2, 2, RoundingMode.FLOOR));
        assertEquals(bigDecimal3, NumberUtil.div(number3, number4, 2, RoundingMode.FLOOR));

        //String+String

        String s4 = "3.14";
        String s5 = "3.19";
        BigDecimal bigDecimal7 = new BigDecimal("0.98");
        assertEquals(bigDecimal7, NumberUtil.div(s4, s5, 2, RoundingMode.FLOOR));

    }

    /**
     * Method: round(double v, int scale)
     */
    @Test
    public void testRoundForVScale() {
        double a = 1.555d;
        BigDecimal bigDecimal = new BigDecimal("1.56");
        assertEquals(bigDecimal, NumberUtil.round(a, 2));


    }

    /**
     * Method: roundStr(double v, int scale)
     */
    @Test
    public void testRoundStrForVScale() {
        double a = 1.14d;
        String b = "1.1";
        assertEquals(b, NumberUtil.roundStr(a, 1));


    }

    /**
     * Method: round(String numberStr, int scale)
     */
    @Test
    public void testRoundForNumberStrScale() {

        String b = "1.234";
        BigDecimal bigDecimal1 = new BigDecimal("1.23");
        assertEquals(bigDecimal1, NumberUtil.round(b, 2));
    }

    /**
     * Method: round(BigDecimal number, int scale)
     */
    @Test
    public void testRoundForNumberScale() {
        BigDecimal bigDecimal2 = new BigDecimal("1.565");
        BigDecimal bigDecimal3 = new BigDecimal("1.57");
        assertEquals(bigDecimal3, NumberUtil.round(bigDecimal2, 2));


    }

    /**
     * Method: roundStr(String numberStr, int scale)
     */
    @Test
    public void testRoundStrForNumberStrScale() {
        String a = "1.268";
        String b = "1.27";
        assertEquals(b, NumberUtil.roundStr(a, 2));

    }

    /**
     * Method: round(double v, int scale, RoundingMode roundingMode)
     */
    @Test
    public void testRoundForVScaleRoundingMode() {
        double a = 1.15d;
        String b = "1.1";
        assertEquals(b, NumberUtil.roundStr(a, 1, RoundingMode.FLOOR));
    }

    /**
     * Method: roundStr(double v, int scale, RoundingMode roundingMode)
     */
    @Test
    public void testRoundStrForVScaleRoundingMode() {

        double v = 1.599;
        assertEquals("1.59", NumberUtil.roundStr(v, 2, RoundingMode.DOWN));
        //TODO: Test goes here...
    }

    /**
     * Method: round(String numberStr, int scale, RoundingMode roundingMode)
     */
    @Test
    public void testRoundForNumberStrScaleRoundingMode() {
        String numberStr = "2.8898";
        BigDecimal bigDecimal = new BigDecimal("2.889");
        assertEquals(bigDecimal, NumberUtil.round(numberStr, 3, RoundingMode.DOWN));
    }

    /**
     * Method: round(BigDecimal number, int scale, RoundingMode roundingMode)
     */
    @Test
    public void testRoundForNumberScaleRoundingMode() {
        BigDecimal bigDecimal = new BigDecimal("1.255");
        BigDecimal bigDecimal1 = new BigDecimal("1.26");
        assertEquals(bigDecimal1, NumberUtil.round(bigDecimal, 2, RoundingMode.UP));
    }

    /**
     * Method: roundStr(String numberStr, int scale, RoundingMode roundingMode)
     */
    @Test
    public void testRoundStrForNumberStrScaleRoundingMode() {
        String a = "1.872";
        assertEquals("1.88", NumberUtil.roundStr(a, 2, RoundingMode.UP));
    }

    /**
     * Method: roundHalfEven(Number number, int scale)
     */
    @Test
    public void testRoundHalfEvenForNumberScale() {
        Number number = 1.245;
        BigDecimal bigDecimal = new BigDecimal("1.24");
        assertEquals(bigDecimal, NumberUtil.roundHalfEven(number, 2));


    }

    /**
     * Method: roundHalfEven(BigDecimal value, int scale)
     */
    @Test
    public void testRoundHalfEvenForValueScale() {
        Number number = 1.245;
        BigDecimal bigDecimal = new BigDecimal("1.24");
        assertEquals(bigDecimal, NumberUtil.roundHalfEven(number, 2));

    }

    /**
     * Method: roundDown(Number number, int scale)
     */
    @Test
    public void testRoundDownForNumberScale() {
        Number number = 2.456;
        BigDecimal bigDecimal = new BigDecimal("2.45");
        assertEquals(bigDecimal, NumberUtil.roundDown(number, 2));
    }

    /**
     * Method: roundDown(BigDecimal value, int scale)
     */
    @Test
    public void testRoundDownForValueScale() {
        BigDecimal bigDecimal = new BigDecimal("1.223");
        BigDecimal bigDecimal1 = new BigDecimal("1.22");
        assertEquals(bigDecimal1, NumberUtil.roundDown(bigDecimal, 2));
    }

    /**
     * Method: decimalFormat(String pattern, double value)
     */
    @Test
    public void testDecimalFormatForPatternValue() {
        double a = 12.121d;

        assertEquals("12", NumberUtil.decimalFormat("0", a));

        long b = (long) 5.433;
        assertEquals("5.00", NumberUtil.decimalFormat("0.00", b));
    }

    /**
     * Method: decimalFormatMoney(double value)
     */
    @Test
    public void testDecimalFormatMoney() {
        double a = 12321111111d;

        assertEquals("12,321,111,111.00", NumberUtil.decimalFormatMoney(a));
    }

    /**
     * Method: formatPercent(double number, int scale)
     */
    @Test
    public void testFormatPercent() {
        double a = 0.55559d;
        assertEquals("55.56%", NumberUtil.formatPercent(a, 2));
    }

    /**
     * Method: isNumber(String str)
     */
    @Test
    public void testIsNumber() {
        String a = "12E-3";
        String b = "-12.22d";
        String c = "-4";

        assertTrue(NumberUtil.isNumber(a));
        assertTrue(NumberUtil.isNumber(b));
        assertTrue(NumberUtil.isNumber(c));
    }

    /**
     * Method: isValidIfNumber(Object obj)
     */
    @Test
    public void testIsValidIfNumber() {
        Double a = 112d;
        BigDecimal c = new BigDecimal("22");
        Number d = 44;
        assertTrue(NumberUtil.isValidIfNumber(a));
        assertTrue(NumberUtil.isValidIfNumber(null));
        assertTrue(NumberUtil.isValidIfNumber(c));
        assertTrue(NumberUtil.isValidIfNumber(d));


    }

    /**
     * Method: isInteger(String s)
     */
    @Test
    public void testIsInteger() {
        String a = "5.1";
        String b = "2";

        assertFalse(NumberUtil.isInteger(a));
        assertTrue(NumberUtil.isInteger(b));


    }

    /**
     * Method: isLong(String s)
     */
    @Test
    public void testIsLong() {
        String a = "5.1";
        long b = 123;
        String c = String.valueOf(b);

        assertFalse(NumberUtil.isLong(a));
        assertTrue(NumberUtil.isLong(c));

    }

    /**
     * Method: isDouble(String s)
     */
    @Test
    public void testIsDouble() {
        String a = "5";
        double b = 123.2d;
        String c = String.valueOf(b);

        assertFalse(NumberUtil.isDouble(a));
        assertTrue(NumberUtil.isDouble(c));
    }

    /**
     * Method: isPrimes(int n)
     */
    @Test
    public void testIsPrimes() {
        int a = 2;
        int b = 4;

        assertTrue(NumberUtil.isPrimes(a));
        assertFalse(NumberUtil.isPrimes(b));
    }

    /**
     * Method: generateRandomNumber(int begin, int end, int size)
     */
    @Test
    public void testGenerateRandomNumber() {
        int[] arr = NumberUtil.generateRandomNumber(0, 9, 9);
        int x = arr[0];
        for (int i = 1; i < 9; i++) {
            assertTrue(x != arr[i]);

        }

    }

    /**
     * Method: generateBySet(int begin, int end, int size)
     */
    @Test
    public void testGenerateBySet() {
        Integer[] arr = NumberUtil.generateBySet(0, 9, 9);
        int x = arr[0];
        for (int i = 1; i < 9; i++) {
            assertTrue(x != arr[i]);

        }
    }

    /**
     * Method: range(int stop)
     */
    @Test
    public void testRangeStop() {
        int[] arr = NumberUtil.range(8);
        int x = arr[0];

        for (int i = 1; i <= 8; i++) {
            assertEquals(x, arr[i] - 1);
            x = arr[i];

        }
    }

    /**
     * Method: range(int start, int stop)
     */
    @Test
    public void testRangeForStartStop() {
        int[] arr = NumberUtil.range(2, 9);
        int x = arr[0];

        for (int i = 1; i <= 7; i++) {
            assertEquals(x, arr[i] - 1);
            x = arr[i];

        }
    }

    /**
     * Method: range(int start, int stop, int step)
     */
    @Test
    public void testRangeForStartStopStep() {
        int[] arr = NumberUtil.range(2, 6, 2);
        int x = arr[0];

        for (int i = 1; i <= 2; i++) {
            assertEquals(x, arr[i] - 2);
            x = arr[i];

        }
    }

    /**
     * Method: appendRange(int start, int stop, Collection<Integer> values)
     */
    @Test
    public void testAppendRangeForStartStopValues() {
        List<Integer> collection = new ArrayList<>();
        collection.add(2);
        collection.add(5);
        List<Integer> testCollection = (List<Integer>) NumberUtil.appendRange(3, 9, collection);

        Integer x = testCollection.get(2);
        for (int i = 2; i < testCollection.size(); i++) {
            assertEquals(x, testCollection.get(i));
            x++;


        }

    }

    /**
     * Method: appendRange(int start, int stop, int step, Collection<Integer> values)
     */
    @Test
    public void testAppendRangeForStartStopStepValues() {
        List<Integer> collection = new ArrayList<>();
        collection.add(2);
        collection.add(5);
        List<Integer> testCollection = (List<Integer>) NumberUtil.appendRange(3, 9, 2, collection);

        Integer x = testCollection.get(2);
        for (int i = 2; i < testCollection.size(); i++) {
            assertEquals(x, testCollection.get(i));
            x = x + 2;


        }

    }

    /**
     * Method: factorial(long start, long end)
     */
    @Test
    public void testFactorialForStartEnd() {
        assertEquals(60, NumberUtil.factorial(5, 2));
    }

    /**
     * Method: factorial(long n)
     */
    @Test
    public void testFactorialN() {
        assertEquals(120, NumberUtil.factorial(5));
    }

    /**
     * Method: sqrt(long x)
     */
    @Test
    public void testSqrt() {
        assertEquals(2, NumberUtil.sqrt(4));
        assertEquals(0, NumberUtil.sqrt(-1));
    }

    /**
     * Method: processMultiple(int selectNum, int minNum)
     */
    @Test
    public void testProcessMultiple() {
        int a = 7;
        int b = 5;
        assertEquals(21, NumberUtil.processMultiple(a, b));
    }

    /**
     * Method: divisor(int m, int n)
     */
    @Test
    public void testDivisor() {
        int a = 18;
        int b = 81;
        assertEquals(9, NumberUtil.divisor(a, b));
    }

    /**
     * Method: multiple(int m, int n)
     */
    @Test
    public void testMultiple() {
        int a = 7;
        int b = 5;
        assertEquals(35, NumberUtil.multiple(a, b));
    }

    /**
     * Method: getBinaryStr(Number number)
     */
    @Test
    public void testGetBinaryStr() {
        Number a = 53;
        assertEquals("110101", NumberUtil.getBinaryStr(a));
    }

    /**
     * Method: binaryToInt(String binaryStr)
     */
    @Test
    public void testBinaryToInt() {
        String a = "11010";
        assertEquals(26, NumberUtil.binaryToInt(a));
    }

    /**
     * Method: binaryToLong(String binaryStr)
     */
    @Test
    public void testBinaryToLong() {
        String a = "11011";
        assertEquals(27, NumberUtil.binaryToLong(a));
    }

    /**
     * Method: compare(char x, char y)
     */
    @Test
    public void testCompareForXY() {
        char a = '9';
        char b = '8';
        assertEquals(1, NumberUtil.compare(a, b));

        double c = 1.1d;
        double d = 3.2d;
        assertEquals(-1, NumberUtil.compare(c, d));

        int e = 1;
        int f = 1;
        assertEquals(0, NumberUtil.compare(e, f));

        long g = 18;
        long h = 19;
        assertEquals(-1, NumberUtil.compare(g, h));

        short j = 55;
        short k = 43;
        assertEquals(1, NumberUtil.compare(j, k));

        byte l = 127;
        byte m = 126;
        assertEquals(1, NumberUtil.compare(l, m));


    }

    /**
     * Method: isGreater(BigDecimal bigNum1, BigDecimal bigNum2)
     */
    @Test
    public void testIsGreater() {
        BigDecimal bigDecimal1 = new BigDecimal("212.212");
        BigDecimal bigDecimal2 = new BigDecimal("123.11");

        assertTrue(NumberUtil.isGreater(bigDecimal1, bigDecimal2));
    }


    /**
     * Method: isGreaterOrEqual(BigDecimal bigNum1, BigDecimal bigNum2)
     */
    @Test
    public void testIsGreaterOrEqual() {
        BigDecimal bigDecimal1 = new BigDecimal("212.212");
        BigDecimal bigDecimal2 = new BigDecimal("212.212");

        assertTrue(NumberUtil.isGreaterOrEqual(bigDecimal1, bigDecimal2));
    }

    /**
     * Method: isLess(BigDecimal bigNum1, BigDecimal bigNum2)
     */
    @Test
    public void testIsLess() {
        BigDecimal bigDecimal1 = new BigDecimal("2121.212");
        BigDecimal bigDecimal2 = new BigDecimal("212.212");

        assertFalse(NumberUtil.isLess(bigDecimal1, bigDecimal2));
    }

    /**
     * Method: isLessOrEqual(BigDecimal bigNum1, BigDecimal bigNum2)
     */
    @Test
    public void testIsLessOrEqual() {
        BigDecimal bigDecimal1 = new BigDecimal("212.212");
        BigDecimal bigDecimal2 = new BigDecimal("212.212");

        assertTrue(NumberUtil.isLessOrEqual(bigDecimal1, bigDecimal2));
    }

    /**
     * Method: equals(BigDecimal bigNum1, BigDecimal bigNum2)
     */
    @Test
    public void testEquals() {
        BigDecimal bigDecimal1 = new BigDecimal("2121.00");
        BigDecimal bigDecimal2 = new BigDecimal("2121");

        assertTrue(NumberUtil.equals(bigDecimal1, bigDecimal2));
    }

    /**
     * Method: min(T... numberArray)
     */
    @Test
    public void testMinNumberArray() {

        long[] arr2 = {1, 3, 5, 7, 9};
        assertEquals(9, NumberUtil.max(arr2));

        int[] arr3 = {3, 5, 7, 9};
        assertEquals(3, NumberUtil.min(arr3));

        short[] arr4 = {9, 4, 2};
        assertEquals(2, NumberUtil.min(arr4));

        double[] arr5 = {11.2d, 23d, 45d};
        assertEquals(11.2d, NumberUtil.min(arr5), 0.01);

        float[] arr6 = {11.2f, 23f, 45f};
        assertEquals(11.2f, NumberUtil.min(arr6), 0.01);

    }

    /**
     * Method: max(T... numberArray)
     */
    @Test
    public void testMaxNumberArray() {

        long[] arr2 = {1, 3, 5, 7, 9};
        assertEquals(9, NumberUtil.max(arr2));

        int[] arr3 = {3, 5, 7, 9};
        assertEquals(9, NumberUtil.max(arr3));

        short[] arr4 = {9, 4, 2};
        assertEquals(9, NumberUtil.max(arr4));

        double[] arr5 = {11.2d, 23d, 45d};
        assertEquals(45d, NumberUtil.max(arr5), 0.01);

        float[] arr6 = {11.2f, 23f, 45f};
        assertEquals(45f, NumberUtil.max(arr6), 0.01);
    }

    /**
     * Method: toString(Number number, String defaultValue)
     */
    @Test
    public void testToStringForNumberDefaultValue() {
        Number number = 55.00;
        String defaultValue = "0";
        String s = "55";
        assertEquals(s, NumberUtil.toString(number, defaultValue));
    }

    /**
     * Method: toString(Number number)
     */
    @Test
    public void testToStringNumber() {
        Number number = 55.00;
        String s = "55";
        assertEquals(s, NumberUtil.toString(number));
    }

    /**
     * Method: toBigDecimal(Number number)
     */
    @Test
    public void testToBigDecimalNumber() {
        Number a = 23.10;
        assertEquals(new BigDecimal("23.1"), NumberUtil.toBigDecimal(a));

        String b = "33.20";
        assertEquals(new BigDecimal("33.20"), NumberUtil.toBigDecimal(b));
    }

    /**
     * Method: count(int total, int part)
     */
    @Test
    public void testCount() {
        int total = 100, part = 25;
        assertEquals(4, NumberUtil.count(total, part));
    }

    /**
     * Method: null2Zero(BigDecimal decimal)
     */
    @Test
    public void testNull2Zero() {
        assertEquals(new BigDecimal("0"), NumberUtil.null2Zero(null));
    }

    /**
     * Method: zero2One(int value)
     */
    @Test
    public void testZero2One() {
        int value = 0;
        int value1 = 1;
        int value2 = -1;

        assertEquals(1, NumberUtil.zero2One(value));
        assertEquals(1, NumberUtil.zero2One(value1));
        assertEquals(-1, NumberUtil.zero2One(value2));
    }

    /**
     * Method: newBigInteger(String str)
     */
    @Test
    public void testNewBigInteger() {
        String s1 = "5";
        String s2 = "";

        assertEquals(new BigInteger("5"), NumberUtil.newBigInteger(s1));
        assertNull(NumberUtil.newBigInteger(s2));
    }

    /**
     * Method: isBeside(long number1, long number2)
     */
    @Test
    public void testIsBesideForNumber1Number2() {
        long a = 1, b = 2, c = 3;
        assertTrue(NumberUtil.isBeside(a, b));
        assertFalse(NumberUtil.isBeside(a, c));

        int d = 1, e = 2, f = 3;
        assertTrue(NumberUtil.isBeside(d, e));
        assertFalse(NumberUtil.isBeside(d, f));
    }

    /**
     * Method: partValue(int total, int partCount)
     */
    @Test
    public void testPartValueForTotalPartCount() {
        int total = 80;
        int part = 40;

        assertEquals(2, NumberUtil.partValue(total, part));

        int part2 = 30;
        assertEquals(3, NumberUtil.partValue(total, part2));
    }

    /**
     * Method: partValue(int total, int partCount, boolean isPlusOneWhenHasRem)
     */
    @Test
    public void testPartValueForTotalPartCountIsPlusOneWhenHasRem() {
        int total = 80;
        int part = 40;
        boolean isPlusOneWhenHasRem = true;

        assertEquals(2, NumberUtil.partValue(total, part, isPlusOneWhenHasRem));

        int part2 = 30;
        assertEquals(3, NumberUtil.partValue(total, part2, isPlusOneWhenHasRem));
        isPlusOneWhenHasRem = false;
        assertEquals(2, NumberUtil.partValue(total, part2, isPlusOneWhenHasRem));

    }

    /**
     * Method: pow(Number number, int n)
     */
    @Test
    public void testPowForNumberN() {
        Number number = 6;
        int n = 3;
        assertEquals(new BigDecimal("216"), NumberUtil.pow(number, n));

        BigDecimal bigDecimal = new BigDecimal("6");
        assertEquals(new BigDecimal("216"), NumberUtil.pow(bigDecimal, n));
    }

    /**
     * Method: parseInt(String number)
     */
    @Test
    public void testParseInt() {
        String s1 = "0x16";
        String s2 = "21";
        String s3 = "1011";
        String s4 = "";

        assertEquals(22, NumberUtil.parseInt(s1));
        assertEquals(21, NumberUtil.parseInt(s2));
        assertEquals(1011, NumberUtil.parseInt(s3));
        assertEquals(0, NumberUtil.parseInt(s4));


    }

    /**
     * Method: parseLong(String number)
     */
    @Test
    public void testParseLong() {
        String s1 = "0x16";
        String s2 = "21";
        String s3 = "1011";
        String s4 = "";

        long a = 22L;
        long b = 21L;
        long c = 1011L;
        long d = 0L;
        assertEquals(a, NumberUtil.parseLong(s1));
        assertEquals(b, NumberUtil.parseLong(s2));
        assertEquals(c, NumberUtil.parseLong(s3));
        assertEquals(d, NumberUtil.parseLong(s4));
    }

    /**
     * Method: parseNumber(String numberStr)
     */
    @Test
    public void testParseNumber() {
        String s = "22l";
        Number number = 22L;
        assertEquals(number, NumberUtil.parseNumber(s));
    }

    /**
     * Method: toBytes(int value)
     */
    @Test
    public void testToBytes() {
        int a = 1000;

        byte[] arr = new byte[]{0, 0, 3, -24};

        assertTrue(Arrays.equals(arr, NumberUtil.toBytes(a)));
    }

    /**
     * Method: toInt(byte[] bytes)
     */
    @Test
    public void testToInt() {
        int a = 1000;

        byte[] arr = new byte[]{0, 0, 3, -24};

        assertEquals(a, NumberUtil.toInt(arr));
    }


    /**
     * Method: toUnsignedByteArray(BigInteger value)
     */
    @Test
    public void testToUnsignedByteArrayValue() {
        BigInteger a = new BigInteger("12345678");

        byte[] arr = new byte[]{-68, 97, 78};

        assertTrue(Arrays.equals(arr, NumberUtil.toUnsignedByteArray(a)));

    }

    /**
     * Method: toUnsignedByteArray(int length, BigInteger value)
     */
    @Test
    public void testToUnsignedByteArrayForLengthValue() {
        BigInteger a = new BigInteger("12345678");
        int length = 6;

        byte[] arr = new byte[]{0, 0, 0, -68, 97, 78};
        byte[] bytes = NumberUtil.toUnsignedByteArray(length, a);

        assertTrue(Arrays.equals(arr, bytes));
    }

    /**
     * Method: fromUnsignedByteArray(byte[] buf)
     */
    @Test
    public void testFromUnsignedByteArrayBuf() {
        byte[] bytes = new byte[]{0, 0, 0, -68, 97, 78};
        BigInteger a = new BigInteger("12345678");
        assertEquals(a, NumberUtil.fromUnsignedByteArray(bytes));
    }

    /**
     * Method: fromUnsignedByteArray(byte[] buf, int off, int length)
     */
    @Test
    public void testFromUnsignedByteArrayForBufOffLength() {
        byte[] arr = new byte[]{-68, 97, 78};
        int off = 1;
        int length = 2;

        BigInteger a = new BigInteger("24910");
        assertEquals(a, NumberUtil.fromUnsignedByteArray(arr, off, length));

    }

}
