package com.uneed.common.core.date;

import com.uneed.common.core.date.pattern.DateFormatPattern;
import com.uneed.common.core.date.pattern.DatePattern;
import com.uneed.common.core.exception.unchecked.ParameterException;
import com.uneed.common.core.lang.ObjectUtil;
import com.uneed.common.core.lang.StringUtil;
import com.uneed.common.core.lang.Validate;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author diablo
 * @date 2017/6/13
 */
public final class DateUtil {


    /**
     * 私有化构造函数，禁止实例化该类
     */
    private DateUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 接收一个Long类型的参数返回日期的字符串
     *
     * @param timestamp 从 1970-01-01 00:00:00.000 到当前时间的毫秒数
     * @return "yyyy-MM-dd" 字符串
     */
    public static String toString(Long timestamp) {
        return toString(timestamp, "yyyy-MM-dd");
    }

    /**
     * 接收一个Long类型的参数,并按照pattern匹配的格式进行转换返回日期的字符串
     *
     * @param timestamp yyyy-MM-dd HH:mm:ss:SSS时间格式
     * @param pattern   匹配的格式
     * @return 对应格式的时间字符串
     */
    public static String toString(Long timestamp, String pattern) {
        return ObjectUtil.isNotEmpty(timestamp) ? new DateTime(timestamp).toString(pattern) : "";
    }

    /**
     * 将Date类型按yyyy-MM-dd格式转为字符串类型
     *
     * @param date 被转化的date类型参数
     * @return "yyyy-MM-dd" 字符串
     */
    public static String toString(Date date) {
        return toString(date, "yyyy-MM-dd");
    }

    /**
     * 将Date类型按pattern格式转为字符串类型
     *
     * @param date    Date类型对象
     * @param pattern 匹配的时间格式
     * @return 对应g格式字符串
     */
    public static String toString(Date date, String pattern) {
        return ObjectUtil.isNotEmpty(date) ? new DateTime(date).toString(pattern) : "";
    }

    /**
     * 传入timestamp转成Date类型
     *
     * @param timestamp 从 1970-01-01 00:00:00.000 到当前时间的毫秒数
     * @return Date类型
     */
    public static Date toDate(Long timestamp) {
        return ObjectUtil.isNotEmpty(timestamp) ? new DateTime(timestamp).toDate() : null;
    }

    /**
     * 传入str转成Date类型
     *
     * @param str 待转化为时间格式字符串
     * @return Date类型
     */
    public static Date toDate(String str) {
        DateFormatPattern pattern = getFormatPattern(str);
        if (ObjectUtil.isEmpty(pattern)) {
            pattern = DateFormatPattern.COMMON_DATE;
        }
        return toDate(str, pattern.getPattern());
    }

    /**
     * 传入str转成pattern格式的Date类型对象
     *
     * @param str     时间字符串
     * @param pattern 时间格式
     * @return Date类型
     */
    public static Date toDate(String str, String pattern) {
        pattern = ObjectUtil.isEmpty(pattern) ? "yyyy-MM-dd" : pattern;
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return ObjectUtil.isNotEmpty(str) ? DateTime.parse(str, formatter).toDate() : null;
    }

    /**
     * 传入timestamp转为Date并且为这一天的开始时间（最小值）
     *
     * @param timestamp 从 1970-01-01 00:00:00.000 到当前时间的毫秒数
     * @return Date类型
     */
    public static Date toDateWithMinimum(Long timestamp) {
        return toDateWithMinimum(toDate(timestamp));
    }

    /**
     * 传入字符串为Date并且为这一天的开始时间（最小值）
     *
     * @param str 时间字符串
     * @return Date类型
     */
    public static Date toDateWithMinimum(String str) {
        return toDateWithMinimum(toDate(str));
    }

    /**
     * 传入字符串和格式转为Date并且为这一天的开始时间（最小值）
     *
     * @param str     时间字符串
     * @param pattern 匹配的时间格式
     * @return Date类型
     */
    public static Date toDateWithMinimum(String str, String pattern) {
        return toDateWithMinimum(toDate(str, pattern));
    }

    /**
     * 传入date转成这一天的开始时间（最小值）并返回
     *
     * @param date 日期
     * @return Date类型
     */
    public static Date toDateWithMinimum(Date date) {
        return ObjectUtil.isNotEmpty(date) ? new DateTime(date).millisOfDay().withMinimumValue().toDate() : null;
    }

    /**
     * 将timestamp时间类型转为Date并且为这一天的结束时间（最大值）
     *
     * @param timestamp 从 1970-01-01 00:00:00.000 到当前时间的毫秒数
     * @return Date类型
     */
    public static Date toDateWithMaximum(Long timestamp) {
        return toDateWithMaximum(toDate(timestamp));
    }

    /**
     * 传入字符串为Date并且为这一天的结束时间（最大值）
     *
     * @param str 时间字符串
     * @return Date类型
     */
    public static Date toDateWithMaximum(String str) {
        return toDateWithMaximum(toDate(str));
    }

    /**
     * 传入字符串和格式为Date并且为这一天的结束时间（最大值）
     *
     * @param str     时间字符串
     * @param pattern 匹配的时间格式
     * @return Date类型
     */
    public static Date toDateWithMaximum(String str, String pattern) {
        return toDateWithMaximum(toDate(str, pattern));
    }

    /**
     * 传入date转成这一天的结束时间（最大值）
     *
     * @param date date对象
     * @return Date类型
     */
    public static Date toDateWithMaximum(Date date) {
        return ObjectUtil.isNotEmpty(date) ? new DateTime(date).millisOfDay().withMaximumValue().toDate() : null;
    }

    /**
     * 判断date的时间是否在现在之前
     *
     * @param date 日期对象
     * @return boolean
     */
    public static boolean beforeNow(Date date) {
        return new DateTime(date).isBeforeNow();
    }

    /**
     * 判断date时间是否在现在之后
     *
     * @param date 日期对象
     * @return boolean
     */
    public static boolean afterNow(Date date) {
        return new DateTime(date).isAfterNow();
    }

    /**
     * 判断时间one是否在时间two之前
     *
     * @return boolean
     */
    public static boolean before(Date one, Date two) {
        Validate.notNull(one, "one can't be null!");
        return !ObjectUtil.isEmpty(two) && new DateTime(one).isBefore(new DateTime(two));
    }

    /**
     * 判断时间one是否在时间two之后
     *
     * @return boolean
     */
    public static boolean after(Date one, Date two) {
        Validate.notNull(one, "one can't be null!");
        return !ObjectUtil.isEmpty(two) && new DateTime(one).isAfter(new DateTime(two));
    }

    /**
     * 比较两个时间某个单位上的差值（年、月、日、时、分、秒）
     *
     * @param one     时间参数1
     * @param two     时间参数2
     * @param pattern 枚举常量
     * @return 某个时间段
     */
    public static int difference(Date one, Date two, DatePattern pattern) {
        Validate.notNull(one, "one can't be null!");
        Validate.notNull(two, "one can't be null!");
        if (ObjectUtil.isEmpty(pattern)) {
            pattern = DatePattern.DAY;
        }
        DateTime d1 = new DateTime(one);
        DateTime d2 = new DateTime(two);
        if (d1.isAfter(d2)) {
            DateTime temp = d1;
            d1 = d2;
            d2 = temp;
        }
        int diff;
        switch (pattern) {
            case YEAR:
                diff = Years.yearsBetween(d1, d2).getYears();
                break;
            case MONTH:
                diff = Months.monthsBetween(d1, d2).getMonths();
                break;
            case HOUR:
                diff = Hours.hoursBetween(d1, d2).getHours();
                break;
            case MINUTE:
                diff = Minutes.minutesBetween(d1, d2).getMinutes();
                break;
            case SECOND:
                diff = Seconds.secondsBetween(d1, d2).getSeconds();
                break;
            default:
                diff = Days.daysBetween(d1, d2).getDays();
        }
        return diff;
    }

    /**
     * 根据传入的时间字符串返回对应的时间格式对象
     *
     * @param value 传入的时间字符串
     * @return DateFormatPattern
     */
    public static DateFormatPattern getFormatPattern(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (DateFormatPattern pattern : DateFormatPattern.values()) {
            Pattern compile = Pattern.compile(pattern.getMatched());
            if (compile.matcher(value).matches()) {
                return pattern;
            }
        }
        return null;
    }

    /**
     * 验证时间段是否合规
     *
     * @param times 时间段集
     */
    public static void validateTime(String... times) {
        boolean b = true;
        String t = null;
        for (String time : times) {
            if (StringUtil.isEmpty(time)) {
                continue;
            }
            String[] temp = time.split(":");
            if (temp.length != 2) {
                b = false;
                t = time;
                break;
            }
            String v = temp[0];
            if (!StringUtil.isNumeric(v)) {
                b = false;
                t = time;
                break;
            }
            int h = Integer.parseInt(v);
            if (h < 0 || h > 23) {
                b = false;
                t = time;
                break;
            }
            v = temp[1];
            if (!StringUtil.isNumeric(v)) {
                b = false;
                t = time;
                break;
            }
            h = Integer.parseInt(v);
            if (h < 0 || h > 59) {
                b = false;
                t = time;
                break;
            }
        }

        if (!b) {
            throw new ParameterException("时间值[" + t + "]不合规，正确格式为[0~23:0~59]");
        }
    }

    /**
     * 判断是否在时间区间之内
     *
     * @param date  要判断的时间
     * @param start 开始时间
     * @param end   结束时间
     * @return boolean 是否在时间区间
     */
    public static boolean datePeriod(Date date, Date start, Date end) {
        Validate.notNull(date, "one can't be null!");
        if (ObjectUtil.isNull(start) || ObjectUtil.isNull(end)) {
            return false;
        }

        return !date.before(start) && !date.after(end);
    }

    /**
     * 判断一个时间段和另一个时间段是否有交集
     *
     * @param oneStart 第一个的开始时间
     * @param oneEnd   第一个的结束时间
     * @param twoStart 第二个的开始时间
     * @param towEnd   第二个的结束时间
     * @return boolean 是否有交集
     */
    public static boolean datePeriod(Date oneStart, Date oneEnd, Date twoStart, Date towEnd) {
        Validate.notNull(oneStart, "oneStart can't be null!");
        Validate.notNull(oneEnd, "oneEnd can't be null!");
        if (ObjectUtil.isNull(twoStart) || ObjectUtil.isNull(towEnd)) {
            return false;
        }
        //用第一个时间来做判断，如果第一个时间的开始时间在第二个时间的开始时间之前，有俩种情况
        if (oneStart.before(twoStart)) {
            return oneEnd.after(twoStart);
        }

        return oneStart.before(towEnd);
    }

    /**
     * 日期和时间字符串连接，返回一个日期类型
     *
     * @param date 日期字符串
     * @param time 时间字符串，支持HH:mm/HH:mm:ss俩种类型的时间
     * @return Date
     */
    public static Date joinTime(String date, String time) {
        Validate.notEmpty(date, "date can't be empty!");
        Validate.notEmpty(time, "time can't be empty!");
        DateFormatPattern pattern = time.length() > 5 ? DateFormatPattern.COMMON_DATE_TIME : DateFormatPattern.COMMON_DATE_SHORT_TIME;
        return toDate(date + StringUtil.SPACE + time, pattern.getPattern());
    }

    /**
     * 日期和时间字符串连接，返回一个日期类型
     *
     * @param date 日期
     * @param time 时间字符串，支持HH:mm/HH:mm:ss俩种类型的时间
     * @return Date
     */
    public static Date joinTime(Date date, String time) {
        Validate.notNull(date, "date can't be null!");
        return joinTime(toString(date, DateFormatPattern.COMMON_DATE.getPattern()), time);
    }

    /**
     * 获取星期描述
     *
     * @param date 日期
     * @return String
     */
    public static String getWeek(Date date) {
        return getWeekDesc(new DateTime(date).getDayOfWeek());
    }

    /**
     * 获取星期描述
     *
     * @param date 日期
     * @return String
     */
    public static String getWeek(String date) {
        return getWeek(toDate(date));
    }

    /**
     * 将数字周转换为中文周名称
     *
     * @param week 数字周
     * @return 中文周描述
     */
    public static String getWeekDesc(int week) {
        String weekDay = "";
        switch (week) {
            case 1:
                weekDay = "周一";
                break;
            case 2:
                weekDay = "周二";
                break;
            case 3:
                weekDay = "周三";
                break;
            case 4:
                weekDay = "周四";
                break;
            case 5:
                weekDay = "周五";
                break;
            case 6:
                weekDay = "周六";
                break;
            case 7:
                weekDay = "周日";
                break;
            default:
        }
        return weekDay;
    }
}
