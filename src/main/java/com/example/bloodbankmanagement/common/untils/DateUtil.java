package com.example.bloodbankmanagement.common.untils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String DATETIME_FORMAT_ICT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATETIME_FORMAT_NORMAL = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_FORMAT_NORMAL_SSS = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyyMMdd HHmmss";
    public static final String DATE_FORMAT_YYMMDDHHMMSS_SSS = "yyMMddHHmmssSSS";
    public static final String DATE_FORMAT_YYYYMMDDHHMMSS_SSS = "yyyyMMddHHmmssSSS";
    public static final String DATE_FORMAT_DDMMYYYY = "ddMMyyyy";
    public static final String DATE_FORMAT_NORMAL = "yyyyMMdd";
    public static final String TIME_FORMAT_HHMMSS = "HHmmss";
    public static final String TIME_FORMAT_HHMM = "HHmm";
    public static final String DATE_FORMAT_DD_DOT_MM_DOT_YYYY = "dd.MM.yyyy";
    public static final String DATE_FORMAT_DD_MM_YYYY = "dd-MM-yyyy";
    public static final String DATE_FORMAT_DD_SOURCE_MM_SOURCE_YYYY = "dd/MM/yyyy";
    public static final String DATETIME_FORMAT_DDMMYYYYHHMM = "dd/MM/yyyy HH:mm";
    public static final String FORMAT_YYMMDD = "yyMMdd";
    public static final String DATETIME_FORMAT_DDMMYYYYHHMMSS = "dd/MM/yyyy HH:mm:ss";
    public static final String MONTH_ONLY_FORMAT_RAW = "yyyyMM";
    public static final String TIME_FORMAT_VN = "HH:mm:ss";
    public static final String MONTH_ONLY_FORMAT_VN = "MM-yyyy";
    public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String DATETIME_FORMAT_MIN_YYYYMMDD = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT_SLASH_YYYYMMDD = "yyyy/MM/dd";
    public static final String DATETIME_FORMAT_DOT_YYYYMMDD = "yyyy.MM.dd";
    public static final String TIME_FORMAT_DDHHMMSS = "ddHHmmss";
    public static final String DATE_FORMAT_DOT_DDMMYYYY = "dd.MM.yyyy";
    public static final String DATETIME_FORMAT_YYYY_MM_DD_HH_MM_SS_SPACE_SSS = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String TIME_FORMAT_NORMAL = "HHmmss";
    public static final String DATETIME_FORMAT_YYYY_MM_DD_HH_MM_SS_DOT_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    private DateUtil() {
    }

    public static String strNowDate() {
        LocalDate date = LocalDate.now();
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
    public static String strNowTime() {
        LocalDateTime date = LocalDateTime.now();
        return date.format(DateTimeFormatter.ofPattern("HHmmss"));
    }
    public static String getLastDayOfTheMonth(String date) {
        String lastDayOfTheMonth = "";

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
        try{
            Date dt= formatter.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);

            Date lastDay = calendar.getTime();

            lastDayOfTheMonth = formatter.format(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lastDayOfTheMonth;
    }

    public static String getFirstDayOfTheMonth() {
        String lastDayOfTheMonth = "";

        LocalDate todaydate = LocalDate.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDD);

        return todaydate.withDayOfMonth(1).format(myFormatObj);
    }
    public static String getDateFromDateString(String date, String formatIn, String formatOut) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(formatOut);
            Date dateParse = (new SimpleDateFormat(formatIn)).parse(date);
            return dateFormat.format(dateParse);
        } catch (ParseException var5) {
            return null;
        }
    }

    public static String getTimeFromDateString(String date, String formatIn, String formatOut) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(formatOut);
            Date dateParse = (new SimpleDateFormat(formatIn)).parse(date);
            return dateFormat.format(dateParse);
        } catch (ParseException var5) {
            return null;
        }
    }

    public static boolean isDatePattern(String date, String format) {

        String pattern = "";
        switch (format) {
            case "yyyy-MM-dd":
                pattern = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
                break;
            case "yyyy/MM/dd":
                pattern = "^\\d{4}\\/(0[1-9]|1[012])\\/(0[1-9]|[12][0-9]|3[01])$";
                break;
            case "yyyy.MM.dd":
                pattern = "^\\d{4}\\.(0[1-9]|1[012])\\.(0[1-9]|[12][0-9]|3[01])$";
                break;
            case DATE_FORMAT_YYYYMMDD:
                pattern = "^\\d{4}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])$";
                break;
            case DATE_FORMAT_DD_SOURCE_MM_SOURCE_YYYY:
                pattern = "^(0?[1-9]|[12][0-9]|3[01])[\\/](0?[1-9]|1[012])[\\/]\\d{4}$";
                break;
            case DATE_FORMAT_DD_MM_YYYY:
                pattern = "^(0?[1-9]|[12][0-9]|3[01])[\\-](0?[1-9]|1[012])[\\-]\\d{4}$";
                break;
            default:
                break;
        }
        return date.matches(pattern);
    }

    public static String nowToTimestampStr() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return timestampFormat.format(calendar.getTime());
    }

}
