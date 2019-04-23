package com.hyk.shoppingstreet.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by songben on 15/12/10.
 */
public class TimeUtil {


    public static Date convertToDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    public static Date getStartOfDay(Date date) {
        assert date != null;
        return null != date ? new DateTime(date).withTimeAtStartOfDay().toDate() : null;
    }

    public static Date getStartOfDay(Long timestamp) {
        assert timestamp != null;
        return null != timestamp ? new DateTime(timestamp).withTimeAtStartOfDay().toDate() : null;
    }

    public static Date getBeginOfDay(Date date) {
        return getStartOfDay(date);
    }

    public static Date getEndOfDay(Long timestamp) {
        assert timestamp != null;
        return null != timestamp ? new DateTime(timestamp).withTime(23, 59, 59, 0).toDate() : null;
    }

    public static Date getEndOfDay(Date date) {
        assert date != null;
        return null != date ? new DateTime(date).withTime(23, 59, 59, 0).toDate() : null;
    }

    public static Date getFormattedDate(String dateStr, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dateStr);
    }

    public static Date getFormatTimeDate(Date date, String format) {
        Date result = null;
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            result = df.parse(df.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getFormatTimeStr(Date date, String format) {
        assert date != null;
        try {
            return new SimpleDateFormat(format).format(date);
        } catch (Exception ignored) {
            return "";
        }
    }

    public static Date addMinute(Date date, Integer minutes) {
        return new DateTime(date).plusMinutes(minutes).toDate();
    }

    public static Date addSeconds(Date date, int seconds) {
        return new DateTime(date).plusSeconds(seconds).toDate();
    }

    public static Date addDay(int days) {
        return new LocalDate().plusDays(days).toDate();
    }

    public static Date addDay(Date date, int days) {
        return new DateTime(date).plusDays(days).toDate();
    }

    public static Date addWeek(Date date, int weeks) {
        return LocalDate.fromDateFields(date).plusWeeks(weeks).toDate();
    }

    public static Date addMonth(Date date, int months) {
        return LocalDate.fromDateFields(date).plusMonths(months).toDate();
    }

    public static Date addYear(Date date, Integer years) {
        return LocalDate.fromDateFields(date).plusYears(years).toDate();
    }

    public static String getCurrentMonthStr() {
        LocalDate dayOfPreMonth = new LocalDate().withDayOfMonth(1);
        return getFormatTimeStr(dayOfPreMonth.toDate(), "yyyyMM");
    }

    public static String getPreMonthStr() {
        LocalDate dayOfPreMonth = new LocalDate().plusMonths(-1).withDayOfMonth(1);
        return getFormatTimeStr(dayOfPreMonth.toDate(), "yyyyMM");
    }

    public static Date getCurrentMonthStartDay() {
        return new LocalDate().withDayOfMonth(1).toDate();
    }

    public static Date getLastMonthStarDay() {
        return new LocalDate().plusMonths(-1).withDayOfMonth(1).toDate();
    }


    public static Date getMonthStartDay(Date date) {
        assert date != null;
        return new LocalDate(date).withDayOfMonth(1).toDate();
    }

    public static Date getMonthEndDay(Date date) {
        assert date != null;
        return new DateTime(date).plusMonths(1).withDayOfMonth(1).plusDays(-1)
                .withTime(23, 59, 59, 0).toDate();
    }

    public static Date getStartOfDateWeek(Date date) {
        return new DateTime(date).withDayOfWeek(1).withTimeAtStartOfDay().toDate();
    }

    public static Date getEndOfDateWeek(Date date) {
        return new DateTime(date).withDayOfWeek(7).withTime(23, 59, 59, 0).toDate();
    }

    public static Date getStartOfCurrentWeek() {
        return getStartOfDateWeek(new Date());
    }

    public static Date getEndOfCurrentWeek() {
        return getEndOfDateWeek(new Date());
    }

    public static Date getStartOfPreviousWeek() {
        return getStartOfDateWeek(addWeek(new Date(), -1));
    }

    public static Date getEndOfPreviousWeek() {
        return getEndOfDateWeek(addWeek(new Date(), -1));
    }

    public static String getMonth(Date date) {
        return getMonth(date, "yyyyMM");
    }

    public static String getMonth(Date date, String format) {
        return getFormatTimeStr(new LocalDate(date).withDayOfMonth(1).toDate(), format);
    }

    public static Integer getOnlyMonth(Date date) {
        assert date != null;
        return new LocalDate(date).getMonthOfYear();
    }

    public static Integer getOnlyYear(Date date) {
        assert date != null;
        return new LocalDate(date).getYear();
    }

    public static int getWeekDayOfTomorrow() {
        int weekDay = new LocalDate().plusDays(1).getDayOfWeek() + 1;
        return weekDay / 8 == 0 ? weekDay % 8 : weekDay / 8;
    }


    public static int getWeekDayOfToday() {
        return new LocalDate().getDayOfWeek();
    }

    public static String formatTimeStr(Integer minute) {
        if (minute == null || minute >= 1440) {
            return null;
        }
        int hour = minute / 60;
        int min = minute % 60;
        return (hour < 10 ? "0" + hour : hour) + ":" + (min < 10 ? "0" + min : min);
    }

    public static Date formatDate(Long timestamp, Integer minute) {
        int hour = minute / 60;
        int min = minute % 60;
        return new DateTime(timestamp).withHourOfDay(hour).withMinuteOfHour(min)
                .withSecondOfMinute(0).toDate();
    }

    public static Date getCurrentOfYesterday() {
        return new DateTime().minusDays(1).toDate();
    }


    //获取年月日单值
    public static Calendar DateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static int getMinuteOfDay(Date date) {
        return new DateTime(date).getMinuteOfDay();
    }

    public static int getDayBetweenDate(Date date1, Date date2) {
        if (null == date1 || null == date2) {
            return 0;
        }
        return (int) ((getStartOfDay(date1).getTime() - getStartOfDay(date2).getTime()) / (24 * 60
                * 60 * 1000));
    }

    public static Pair<Date, Date> getDateRangeByTimeFlag(Date standardDate, ChronoUnit timeUnit) {
        Date beginDate = null;
        Date endDate = null;
        if (null != timeUnit) {
            if (timeUnit == ChronoUnit.DAYS) {
                beginDate = TimeUtil.getStartOfDay(standardDate);
                endDate = TimeUtil.getEndOfDay(standardDate);
            } else if (timeUnit == ChronoUnit.WEEKS) {
                beginDate = TimeUtil.getStartOfDateWeek(standardDate);
                endDate = TimeUtil.getEndOfDateWeek(standardDate);
            } else if (timeUnit == ChronoUnit.MONTHS) {
                beginDate = TimeUtil.getMonthStartDay(standardDate);
                endDate = TimeUtil.getMonthEndDay(standardDate);
            }
        }
        return Pair.of(beginDate, endDate);
    }

    /**
     * 按日期和分钟数构建时间，date会被处理为当天开始，即00:00:00，在加上minure分钟数
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date compose(Date date, Integer minute) {
        return TimeUtil.addMinute(TimeUtil.getStartOfDay(date), minute);
    }

    /**
     * 计算日期相差天数
     * 报名计算日期专用
     *
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @return 相差天数
     */
    public static int getStudentCourseDayBetweenDate(Date endDate, Date startDate) {
        if (null == startDate || null == endDate) {
            return 0;
        }
        if (getStartOfDay(startDate).getTime() == startDate.getTime()) {
            return (int) (
                    (getEndOfDay(endDate).getTime() - getStartOfDay(startDate).getTime() + 1000) / (24
                            * 60 * 60 * 1000));
        } else {
            return (int) (
                    (getEndOfDay(endDate).getTime() - getStartOfDay(startDate).getTime() + 1000) / (24
                            * 60 * 60 * 1000) - 1);
        }
    }

    /**
     * 判断日期是否相等
     *
     * @param dt1
     * @param dt2
     * @return
     */
    public static boolean
    sameDate(Date dt1, Date dt2) {
        LocalDate ld1 = new LocalDate(new DateTime(getStartOfDay(dt1)));
        LocalDate ld2 = new LocalDate(new DateTime(getStartOfDay(dt2)));
        return ld1.equals(ld2);
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(new LocalDate(new Date()).toDate());
    }

}
