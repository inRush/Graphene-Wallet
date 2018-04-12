package com.sxds.common.helper;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author inrush
 * @date 2017/9/14.
 * @package com.sxds.oaplat.helper
 */

public class TimeHelper {
    // ///s 代表Simple日期格式：yyyy-MM-dd
    // ///f 代表Full日期格式：yyyy-MM-dd hh:mm:ss

    public static final SimpleDateFormat SS_FORMAT = new SimpleDateFormat("MM-dd", Locale.ENGLISH);
    public static final SimpleDateFormat S_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public static final SimpleDateFormat F_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    public static final SimpleDateFormat A_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    public static final SimpleDateFormat CHINA_S_FORMAT = new SimpleDateFormat("yyyy年MM月dd", Locale.CHINA);
    public static final SimpleDateFormat CHINA_SMALL_FORMAT = new SimpleDateFormat("MM月dd日", Locale.CHINA);
    public static final SimpleDateFormat SSS_FORMAT = new SimpleDateFormat("yyyyMMdd E", Locale.ENGLISH);

    public static long s_str_2_long(String dateString) {
        try {
            Date d = S_FORMAT.parse(dateString);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long f_str_2_long(String dateString) {
        try {
            Date d = F_FORMAT.parse(dateString);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String ss_long_2_str(long timestamp) {
        return SS_FORMAT.format(new Date(timestamp));
    }

    public static String sss_long_2_str(long timestamp) {
        return SSS_FORMAT.format(new Date(timestamp));
    }

    public static String s_long_2_str(long timestamp) {
        return S_FORMAT.format(new Date(timestamp));
    }

    public static String f_long_2_str(long timestamp) {
        return F_FORMAT.format(new Date(timestamp));
    }

    public static String china_format_str(Date date) {
        return CHINA_S_FORMAT.format(date);
    }

    public static String china_format_small_str(Date date) {
        return CHINA_SMALL_FORMAT.format(date);
    }

    public static String format_date_str(Date date) {
        return S_FORMAT.format(date);
    }

    /**
     * 获取字符串时间的年份
     *
     * @param dateString 格式为yyyy-MM-ss，或者yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static int getYear(String dateString) {
        try {
            Date d = S_FORMAT.parse(dateString);
            return d.getYear() + 1900;// 年份是基于格林威治时间，所以加上1900
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取字符串时间的月份
     *
     * @param dateString 格式为yyyy-MM-ss，或者yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static int getMonth(String dateString) {
        try {
            Date d = S_FORMAT.parse(dateString);
            return d.getMonth();// 月份从0-11
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取字符串时间的天
     *
     * @param dateString 格式为yyyy-MM-ss，或者yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static int getDayOfMonth(String dateString) {
        try {
            Date d = S_FORMAT.parse(dateString);
            return d.getDate();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static int getHours(String timeString) {
        SimpleDateFormat formart = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = formart.parse(timeString);
            return date.getHours();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static int getMinutes(String timeString) {
        SimpleDateFormat formart = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = formart.parse(timeString);
            return date.getMinutes();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static int getSeconds(String timeString) {
        SimpleDateFormat formart = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = formart.parse(timeString);
            return date.getSeconds();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static String getCurrentTime() {
        return F_FORMAT.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 在当前时间上加上多少毫秒，返回这个时间
     *
     * @param mask
     * @return
     */
    public static String getCurrentTimeMask(long mask) {
        return F_FORMAT.format(new Date(System.currentTimeMillis() + mask));
    }

    // /////////////////////以上是通用的，下面为特殊需求的////////////////////////
    // /**
    // * 时间戳转换日期格式
    // *
    // * @param timestamp
    // * 单位秒
    // * @return
    // */
    // public static String getCurrentTime(long timestamp) {
    // SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // return f.format(new Date(timestamp * 1000));
    // }

    /**
     * 获取精简的日期
     *
     * @param time
     * @return
     */
    public static String getSimpleDate(String time) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = F_FORMAT.parse(time);
            return formater.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @param time
     * @return
     */
    public static String getSimpleDateTime(String time) {
        SimpleDateFormat formater = new SimpleDateFormat("yy-MM-dd HH:mm");
        Date date = null;
        try {
            date = F_FORMAT.parse(time);
            return formater.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getSimpleTime(String time) {
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = F_FORMAT.parse(time);
            return formater.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getChatSimpleDate(String time) {
        SimpleDateFormat formater = new SimpleDateFormat("yy-MM-dd");
        Date date = null;
        try {
            date = F_FORMAT.parse(time);
            return formater.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getTimeHM(String time) {
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = F_FORMAT.parse(time);
            return formater.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static SimpleDateFormat friendly_format1 = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat friendly_format2 = new SimpleDateFormat("MM-dd HH:mm");


    public static String sk_time_friendly_format2(long time) {
        return friendly_format2.format(new Date(time * 1000));
    }

    public static String sk_time_s_long_2_str(long time) {
        return s_long_2_str(time * 1000);
    }

    public static String sk_time_ss_long_2_str(long time) {
        return ss_long_2_str(time * 1000);
    }

    public static long sk_time_s_str_2_long(String dateString) {
        return s_str_2_long(dateString) / 1000;
    }

    public static int sk_time_current_time() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    private static SimpleDateFormat hm_formater = new SimpleDateFormat("HH:mm");

    public static String sk_time_long_to_hm_str(long time) {
        try {
            return hm_formater.format(new Date(time * 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String sk_time_long_to_chat_time_str(long time) {
        String date1 = sk_time_s_long_2_str(time);
        String date2 = sk_time_s_long_2_str(System.currentTimeMillis() / 1000);
        if (date1.compareToIgnoreCase(date2) == 0) {// 是同一天
            return sk_time_long_to_hm_str(time);
        } else {
            return long_to_yMdHm_str(time * 1000);
        }
    }

    public static final SimpleDateFormat SK_FORMAT_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);

    // 日期加小时的字符串
    public static String long_to_yMdHm_str(long time) {
        return SK_FORMAT_1.format(new Date(time));
    }

    public static long sk_time_yMdHm_str_to_long(String time) {
        try {
            return SK_FORMAT_1.parse(time).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int yMdHm_getYear(String dateString) {
        try {
            Date d = SK_FORMAT_1.parse(dateString);
            return d.getYear() + 1900;// 年份是基于格林威治时间，所以加上1900
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int yMdHm_getMonth(String dateString) {
        try {
            Date d = SK_FORMAT_1.parse(dateString);
            return d.getMonth();// 月份从0-11
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int yMdHm_getDayOfMonth(String dateString) {
        try {
            Date d = SK_FORMAT_1.parse(dateString);
            return d.getDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int yMdHm_getHours(String timeString) {
        try {
            Date date = SK_FORMAT_1.parse(timeString);
            return date.getHours();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int yMdHm_getMinutes(String timeString) {
        try {
            Date date = SK_FORMAT_1.parse(timeString);
            return date.getMinutes();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @param textView
     * @param time     时间戳/1000
     * @return
     */
    public static long getSpecialBeginTime(TextView textView, long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        if (time > currentTime) {
            time = currentTime;
        }
        textView.setText(sk_time_s_long_2_str(time));
        return time;
    }

    public static int sk_time_age(long birthday) {
        int age = (new Date().getYear()) - (new Date(birthday * 1000).getYear());
        if (age < 0 || age > 100) {
            return 25;
        }
        return age;
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static long getTimeStampStr(String dateStr) {
        long timeStemp = 0;
        Date date;
        try {
            date = S_FORMAT.parse(dateStr);
            timeStemp = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStemp;
    }

    /**
     * 根据 timestamp 生成各类时间状态串
     *
     * @param timestamp 距1970 00:00:00 GMT的秒数
     * @param format    格式
     * @return 时间状态串(如：刚刚5分钟前)
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeState(String timestamp, String format) {

        if (timestamp == null || "".equals(timestamp)) {
            return "";
        }

        try {
            timestamp = formatTimestamp(timestamp);
            long _timestamp = Long.parseLong(timestamp);

            if (System.currentTimeMillis() - _timestamp < 1 * 60 * 1000) {
                return "刚刚";
            } else if (System.currentTimeMillis() - _timestamp < 30 * 60 * 1000) {
                return ((System.currentTimeMillis() - _timestamp) / 1000 / 60) + "分钟前";
            } else {

                Calendar now = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(_timestamp);

                if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                        && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                        && c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
                    return sdf.format(c.getTime());
                }

                if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                        && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                        && c.get(Calendar.DATE) - 0 == now.get(Calendar.DATE) - 1) {
                    SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
                    return sdf.format(c.getTime());
                } else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                    SimpleDateFormat sdf;
                    if (format != null && !"".equalsIgnoreCase(format)) {
                        sdf = new SimpleDateFormat(format);
                    } else {
                        sdf = new SimpleDateFormat("MM-dd HH:mm");
                    }
                    return sdf.format(c.getTime());
                } else {
                    SimpleDateFormat sdf;
                    if (format != null && !"".equalsIgnoreCase(format)) {
                        sdf = new SimpleDateFormat(format);
                    } else {
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    }
                    return sdf.format(c.getTime());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getTimeState(long time) {

        if (time <= 0) {
            return "";
        }

        time = time / 1000;
        String timestamp = String.valueOf(time);
        if (TextUtils.isEmpty(timestamp)) {
            return "";
        }

        try {
            timestamp = formatTimestamp(timestamp);
            long _timestamp = Long.parseLong(timestamp);

            Calendar now = Calendar.getInstance();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(_timestamp);

            if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                    && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                    && c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                return sdf.format(c.getTime());
            }
            if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                    && c.get(Calendar.DATE) - 0 == now.get(Calendar.DATE) - 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
                return "昨天";
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
                return sdf.format(c.getTime());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 对时间戳格式进行格式化，保证时间戳长度为13位
     *
     * @param timestamp 时间戳
     * @return 返回为13位的时间戳
     */
    public static String formatTimestamp(String timestamp) {
        if (timestamp == null || "".equals(timestamp)) {
            return "";
        }

        String tempTimeStamp = timestamp + "00000000000000";
        StringBuffer stringBuffer = new StringBuffer(tempTimeStamp);
        return tempTimeStamp = stringBuffer.substring(0, 13);
    }

    /**
     * 获取精简的日期
     *
     * @param time
     * @return
     */
    public static String getFormaterSimpleDate(String time) {
        SimpleDateFormat formater = new SimpleDateFormat("MM-dd HH:mm");
        Date date = null;
        try {
            date = F_FORMAT.parse(time);
            return formater.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @return
     * @para
     */
    public static List<String> getWeekListStr() {//从第二天开始
        List<String> str = new ArrayList<String>();
        String dateStr = "";
        //取时间
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);

        for (int i = 0; i < 8; i++) {
            Date date = new Date();
            calendar.setTime(date);
            if (hours > 21) {
                //把日期往后增加两天.整数往后推,负数往前移动
                calendar.add(Calendar.DATE, i + 2);
            } else {
                //把日期往后增加一天.整数往后推,负数往前移动
                calendar.add(Calendar.DATE, i + 1);
            }
            //这个时间就是日期往后推一天的结果
            date = calendar.getTime();
            dateStr = SS_FORMAT.format(date) + "," + getWeekDayString2(date);
            str.add(dateStr);
        }
        return str;
    }

    public static String getWeekDayString2(Date date) {
        String weekString = "";
        final String[] dayNames = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        weekString = dayNames[dayOfWeek - 1];
        return weekString;
    }

    public static String getWeekDayString(Date date) {
        String weekString = "";
        final String[] dayNames = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        weekString = dayNames[dayOfWeek - 1];
        return weekString;
    }

    public static String getTimeStamp(String timeStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date;
        long timeStemp = 0;
        try {
            date = simpleDateFormat.parse(timeStr);
            timeStemp = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(timeStemp);
    }

    public static long f_str_3_long(String dateString) {
        try {
            Date d = A_FORMAT.parse(dateString);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String f_long_3_str(long timestamp) {
        return A_FORMAT.format(new Date(timestamp));
    }
}
