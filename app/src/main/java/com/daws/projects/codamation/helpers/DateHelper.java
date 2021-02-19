package com.daws.projects.codamation.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {

    public static DateHelper getInstance() {
        return ourInstance;
    }

    private static final DateHelper ourInstance = new DateHelper();

    private SimpleDateFormat longDateTimeWithMonthNameFormat;
    private SimpleDateFormat longDateWithMonthNameFormat;
    private SimpleDateFormat requestDateFormat;
    private SimpleDateFormat shortDateWithDash;
    private SimpleDateFormat utcFormat;

    private DateHelper(){
        longDateTimeWithMonthNameFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        longDateWithMonthNameFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        requestDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        shortDateWithDash = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
    }

    public SimpleDateFormat getLongDateTimeWithMonthNameFormat() {
        return longDateTimeWithMonthNameFormat;
    }

    public SimpleDateFormat getLongDateWithMonthNameFormat() {
        return longDateWithMonthNameFormat;
    }

    public SimpleDateFormat getRequestDateFormat() {
        return requestDateFormat;
    }

    public SimpleDateFormat getShortDateWithDash() {
        return shortDateWithDash;
    }

    private Date convertMilisToDate(long datemilis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datemilis);
        return calendar.getTime();
    }

    public Date getDateDaysAgo(int daysAgo){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -daysAgo);
        return c.getTime();
    }

    public String convertMilisToLongDateTimeWithMonthName(long datemilis){
        return longDateTimeWithMonthNameFormat.format(convertMilisToDate(datemilis));
    }

    public String convertMilisToLongDateWithMonthName(long datemilis){
        return longDateWithMonthNameFormat.format(convertMilisToDate(datemilis));
    }

    public String convertMilisToRequestDate(long datemilis){
        return requestDateFormat.format(convertMilisToDate(datemilis));
    }

    public String convertDateToRequestDate(Date date){
        return requestDateFormat.format(date);
    }

    public String convertDateToFormattedDate(Date date, SimpleDateFormat dateFormat){
        return dateFormat.format(date);
    }

    public Date convertStringToDate(String date, SimpleDateFormat dateFormat){
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public String convertUTCToLongDateTimeWithMonthName(String UTCFormat) throws ParseException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        utcFormat.setTimeZone(utc);
        Date convertedDate = utcFormat.parse(UTCFormat);
        return longDateTimeWithMonthNameFormat.format(convertedDate);
    }

    public List<Date> getAllDateBetweenTwoDate(Date startDate, Date endDate){
        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate))
        {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public String getCurrentDate(){
        Date date = new Date();
        return longDateWithMonthNameFormat.format(date);
    }
}
