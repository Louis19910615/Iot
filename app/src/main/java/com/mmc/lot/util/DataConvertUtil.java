package com.mmc.lot.util;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by louis on 2017/7/6.
 */

public class DataConvertUtil {


    public static String ToStr(int year, int month, int day) {
        String str_month;
        String str_day;
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
                Locale.CHINA);
        Date date;
        if (month < 10) {
            str_month = "0" + month;
        } else {
            str_month = String.valueOf(month);
        }

        if (day < 10) {
            str_day = "0" + day;
        } else {
            str_day = String.valueOf(day);
        }
        String time = String.valueOf(year) + "年" + str_month +"月" + str_day + "日" + "12时00分00秒";
        return time;
    }
//    //掉此方法输入所要转换的时间输入例如（"2014年06月14日16时09分00秒"）返回date
//    public static Date StrToDate(String time) {
//        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
//                Locale.CHINA);
//        Date date = null;
//        try {
//            date = sdr.parse(time);
//            return date;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return date;
//    }

//    //调用此方法输入所要转换的时间戳输入例如（1402733340）输出Date
//    public static Date longToDate(long time) {
//        Date date = new Date();
//        date.setTime(time * 1000L);
//        return date;
//
//    }

    public static int getYear(String str) {
        return Integer.valueOf(str.substring(0, 4));
    }

    public static int getMonth(String str) {
        return Integer.valueOf(str.substring(5, 7));
    }

    public static int getDay(String str) {
        return Integer.valueOf(str.substring(8, 10));
    }

    //掉此方法输入所要转换的时间输入例如（"2014年06月14日16时09分00秒"）返回时间戳
    public static long strTolong(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
                Locale.CHINA);
        Date date;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            return l/1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
    public static String longToStr(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        String times = sdr.format(new Date(time * 1000L));
        return times;

    }

    public static Calendar LongToCalendar(long timeInMills) {
        Logger.e("DataConvertUtil LongToCalendar:" + timeInMills);
        Calendar dateAndTime = Calendar.getInstance();
//        dateAndTime.setTime(new Date(timeInMills * 1000));
        dateAndTime.setTimeInMillis(timeInMills * 1000);

        return dateAndTime;
    }

    public static Calendar DateFormatToCalendar(String strFormat) {
        Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(strFormat.substring(0, strFormat.length() - 6));
            dateAndTime.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateAndTime;
    }

    public static String getFormatFromDateFormat(String strFormat) {
        Calendar dateAndTime = DateFormatToCalendar(strFormat);

        Calendar today = Calendar.getInstance(Locale.CHINA);

        if (today.get(Calendar.YEAR) == dateAndTime.get(Calendar.YEAR)
                && today.get(Calendar.MONTH) == dateAndTime.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) == dateAndTime.get(Calendar.DAY_OF_MONTH)) {
            return "今天";
        } else {
            int month = dateAndTime.get(Calendar.MONTH) + 1;
            int day = dateAndTime.get(Calendar.DAY_OF_MONTH);
            String monthStr = String.valueOf(month);
            String dayStr = String.valueOf(day);
            if (month < 10) {
                monthStr = "0" + monthStr;
            }

            if (day < 10) {
                dayStr = "0" + dayStr;
            }

            return monthStr + "/" + dayStr;
        }


    }
    public static String getFormatFromDateFormat(Calendar dateAndTime) {
        Calendar today = Calendar.getInstance(Locale.CHINA);

        if (today.get(Calendar.YEAR) == dateAndTime.get(Calendar.YEAR)
                && today.get(Calendar.MONTH) == dateAndTime.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) == dateAndTime.get(Calendar.DAY_OF_MONTH)) {
            return "今天";
        } else {
            int month = dateAndTime.get(Calendar.MONTH) + 1;
            int day = dateAndTime.get(Calendar.DAY_OF_MONTH);
            String monthStr = String.valueOf(month);
            String dayStr = String.valueOf(day);
            if (month < 10) {
                monthStr = "0" + monthStr;
            }

            if (day < 10) {
                dayStr = "0" + dayStr;
            }

            return monthStr + "/" + dayStr;
        }

    }
    public static int getYearFromDateFormat(String strFormat) {
        return DateFormatToCalendar(strFormat).get(Calendar.YEAR);
    }

    public static int getMonthFromDateFormat(String strFormat) {
        return DateFormatToCalendar(strFormat).get(Calendar.MONTH);
    }

    public static int getDayFromDateFormat(String strFormat) {
        return DateFormatToCalendar(strFormat).get(Calendar.DAY_OF_MONTH);
    }

    public static String CalendarToStrFormat(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日",Locale.CHINA);
        Date date = new Date(calendar.getTimeInMillis());
        Logger.e("CalendarToStrFormat", format.format(date));
        return format.format(date);
    }

    private static String decode(char[] in) throws Exception {
        int off = 0;
        char c;
        char[] out = new char[in.length];
        int outLen = 0;
        while (off < in.length) {
            c = in[off++];
            if (c == '\\') {
                if (in.length > off) { // 是否有下一个字符
                    c = in[off++]; // 取出下一个字符
                } else {
                    out[outLen++] = '\\'; // 末字符为'\'，返回
                    break;
                }
                if (c == 'u') { // 如果是"\\u"
                    int value = 0;
                    if (in.length > off + 4) { // 判断"\\u"后边是否有四个字符
                        boolean isUnicode = true;
                        for (int i = 0; i < 4; i++) { // 遍历四个字符
                            c = in[off++];
                            switch (c) {
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    value = (value << 4) + c - '0';
                                    break;
                                case 'a':
                                case 'b':
                                case 'c':
                                case 'd':
                                case 'e':
                                case 'f':
                                    value = (value << 4) + 10 + c - 'a';
                                    break;
                                case 'A':
                                case 'B':
                                case 'C':
                                case 'D':
                                case 'E':
                                case 'F':
                                    value = (value << 4) + 10 + c - 'A';
                                    break;
                                default:
                                    isUnicode = false; // 判断是否为unicode码
                            }
                        }
                        if (isUnicode) { // 是unicode码转换为字符
                            out[outLen++] = (char) value;
                        } else { // 不是unicode码把"\\uXXXX"填入返回值
                            off = off - 4;
                            out[outLen++] = '\\';
                            out[outLen++] = 'u';
                            out[outLen++] = in[off++];
                        }
                    } else { // 不够四个字符则把"\\u"放入返回结果并继续
                        out[outLen++] = '\\';
                        out[outLen++] = 'u';
                        continue;
                    }
                } else {
                    switch (c) { // 判断"\\"后边是否接特殊字符，回车，tab一类的
                        case 't':
                            c = '\t';
                            out[outLen++] = c;
                            break;
                        case 'r':
                            c = '\r';
                            out[outLen++] = c;
                            break;
                        case 'n':
                            c = '\n';
                            out[outLen++] = c;
                            break;
                        case 'f':
                            c = '\f';
                            out[outLen++] = c;
                            break;
                        default:
                            out[outLen++] = '\\';
                            out[outLen++] = c;
                            break;
                    }
                }
            } else {
                out[outLen++] = (char) c;
            }
        }
        Logger.e(new String(out, 0, outLen));
        return new String(out, 0, outLen);
    }

}
