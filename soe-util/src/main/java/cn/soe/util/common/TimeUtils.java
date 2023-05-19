package cn.soe.util.common;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author xiezhenxiang 2019/5/14
 */
public class TimeUtils {

    /** 获取字符串日期 */
    public static String nowStr() {
        String date = LocalDate.now().toString();
        String time = LocalTime.now().withNano(0).toString();
        time = time.length() == 5 ? time + ":00" : time;
        return date + " " + time;
    }

    /** 获取当前时间 */
    public static Date now() {
        ZonedDateTime zdt = LocalDateTime.now().atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /** 获取时间戳 */
    public static Long nowStamp (){
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /** 格式化成字符串 */
    public static String format(LocalDateTime date, String pattern) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        return date.format(format);
    }

    /** 字符串转LocalDateTime */
    public static LocalDateTime parse(String dateStr, String pattern) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateStr, format);
    }

    /** 时间戳转LocalDateTime */
    public static LocalDateTime stampToDate(Long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
    }

    /** LocalDateTime转时间戳 */
    public static Long dateToStamp(LocalDateTime date) {
        return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
