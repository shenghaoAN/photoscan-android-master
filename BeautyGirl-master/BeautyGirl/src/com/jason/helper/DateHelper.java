package com.jason.helper;

import android.content.ContentResolver;
import android.provider.Settings;


import com.jason.Debug;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kelvin on 5/21/13.
 */
public class DateHelper {

    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    public static Date getUTCByLocal(Date localDate) {
        java.util.Calendar localCalendar = java.util.Calendar.getInstance();
        localCalendar.setTime(localDate);
        int zoneOffset = localCalendar.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = localCalendar.get(java.util.Calendar.DST_OFFSET);
        localCalendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        Date utcDate = new Date(localCalendar.getTimeInMillis());
        return utcDate;
    }

    public static Date getLocalByUTC(Date utcDate) {
        java.util.Calendar localCalendar = java.util.Calendar.getInstance();
        localCalendar.setTime(utcDate);
        int zoneOffset = localCalendar.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = localCalendar.get(java.util.Calendar.DST_OFFSET);
        localCalendar.add(java.util.Calendar.MILLISECOND, (zoneOffset + dstOffset));
        Date localDate = new Date(localCalendar.getTimeInMillis());
        return localDate;
    }

    public static Date parseDate(String dateChar, String format) {
        simpleDateFormat.applyPattern(format);
        try {
            return simpleDateFormat.parse(dateChar);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }


    public static Date parseDate(String dateChar) {
        return parseDate(dateChar, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDate(Date date, String format) {
        simpleDateFormat.applyPattern(format);
        return simpleDateFormat.format(date);
    }

    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @param UTCDate 开始时间  UTCDate
     * @return
     */
    public static String formatDate1(Date UTCDate) {
        try {
            UTCDate = getLocalByUTC(UTCDate);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String localDate = format1.format(UTCDate);
            Date utcDate = getUTCByLocal(format1.parse(localDate));
            return formatDate(utcDate);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "";
    }

    /**
     * @param UTCDate 结束时间  UTCDate
     * @return
     */
    public static String formatDate2(Date UTCDate) {
        try {
            UTCDate = getLocalByUTC(UTCDate);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            long time = UTCDate.getTime() + 24 * 3600 * 1000;
            String localDate = format.format(time);
            Date utcDate = getUTCByLocal(format.parse(localDate));
            return formatDate(utcDate);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "";
    }

    public static Date String2UTCDate(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date utcDate = getUTCByLocal(format.parse(time));
            return utcDate;
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static Date UTCDate2LocalDate(Date date, String format) {
        String time = formatDateUTC2Local(date, format);
        Date localDate = parseDate(time, format);
        return localDate;
    }

    public static String formatDateUTC2Local(Date date, String format) {
        return formatDate(getLocalByUTC(date), format);
    }

    public static String formatDateUTC2Local(Date date) {
        return formatDate(getLocalByUTC(date), "yyyy-MM-dd HH:mm:ss");
    }

    //时间格式化显示星期几 1991-09-11 sun
    public static String formatDateUTC2LocalWeek(Date date) {
        return formatDate(getLocalByUTC(date), "yyyy-MM-dd EEEE");
    }

    /**
     * 獲取系統時間格式 （日期）yyyy-MM-dd
     *
     * @param contentResolver
     * @return
     */
    public static String getSystemFormat(ContentResolver contentResolver) {
        // 获取当前系统设置
        String strTimeFormat = android.provider.Settings.System.getString(contentResolver,
                Settings.System.DATE_FORMAT);
//      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strTimeFormat);
        Debug.Log("SystemDateFormat", strTimeFormat);
        return strTimeFormat;
    }

}
