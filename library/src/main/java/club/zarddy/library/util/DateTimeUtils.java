package club.zarddy.library.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期时间功能
 */
public class DateTimeUtils {

    // 以毫秒为基础的时间单位
    public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;

    /**
     * 时间戳转换成日期
     * @param millis 时间戳
     * @param justMonthDay 如果为 true，则转换成 月-日
     */
    public static String convertMillisToDate(long millis, boolean justMonthDay) {
        try {
            String format = justMonthDay ? "MM-dd" : "yyyy-MM-dd"; // 是否只显示 月日
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            return sdf.format(millis);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 时间戳转换成时间
     * @param millis java格式时间戳
     * @return 如：2017-09-04 20:18:32
     */
    public static String convertMillisToFullTime(long millis) {
        try {
            String format = "yyyy-MM-dd HH:mm:ss"; // 年月日 时分
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            return sdf.format(millis);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 时间戳转换成时分秒
     * @param millis java格式时间戳
     * @return 如：20:18:32
     */
    public static String convertMillisToTime(long millis) {
        try {
            String format = "HH:mm:ss"; // 年月日 时分
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            return sdf.format(millis);
        } catch (Exception e) {
            return "00:00:00";
        }
    }

    /**
     * 将毫秒计算并格式化成时分秒
     * @param millis 总毫秒数
     * @return 格式化后的时间，如 00:12:30
     */
    public static String formatMillisToTime(long millis) {
        long day = millis / DAY;
        long hour = day > 0 ? millis % DAY / HOUR : millis / HOUR;
        long minute = millis % HOUR / MINUTE;
        long second = millis % MINUTE / SECOND;

        if (day > 0) {
            return String.format(Locale.getDefault(), "%02d:%02d:%02d:%02d", day, hour, minute, second);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second);
        }
    }

    /**
     * 日期字符串转换为时间戳
     * @param source 日期字符串，格式：2019-07-27
     * @return 十三位数的时间戳
     */
    public static long parseDateStringToMillis(String source) {
        String format = "yyyy-MM-dd"; // 年月日 时分
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        try {
            Date date = sdf.parse(source);
            return date.getTime();

        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 获取今天 00:00:00 的时间戳
     */
    public static long getTodayTimeStamp() {
        return getDayTimeStamp(System.currentTimeMillis());
    }

    /**
     * 获取指定时间戳的当天时间的 00:00:00 的时间戳
     */
    public static long getDayTimeStamp(long millis) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis() / 1000 * 1000;
    }

    /**
     * 判断某个时间戳是否为今天时间
     * @param millis 时间戳
     */
    public static boolean isTodayTime(long millis) {
        return getTodayTimeStamp() == getDayTimeStamp(millis);
    }

    public static String getScreenshotTimeFormat(long millis) {
        try {
            String format = "yyyyMMddHHmmss"; // 年月日时分秒
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            return sdf.format(millis);
        } catch (Exception e) {
            return "";
        }
    }
}
