package kr.co.easterbunny.wonderple.library.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by scona on 2017-01-13.
 */

public class TimeUtils {


    public static String unixTimeToDate(long unixSeconds) {
        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

    public static String unixTimeToRead(Long unixSeconds) {
        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

    public static long unixTimeLeft(Long unixSeconds) {
        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
        Date dat = new Date();
        long difference = date.getTime() - dat.getTime();

        return difference;
    }


    public static int dDay(String mday)    {
        if (mday == null)
            return 0;

        mday = mday.trim();

        int first = mday.indexOf("-");
        int last = mday.lastIndexOf("-");
        int year = Integer.parseInt(mday.substring(0, first));
        int month = Integer.parseInt(mday.substring(first + 1, last));
        int day = Integer.parseInt(mday.substring(last + 1, mday.length()));

        GregorianCalendar cal = new GregorianCalendar();
        long currentTime = cal.getTimeInMillis() / (1000 * 60 * 60 * 24);
        cal.set(year, month - 1, day);
        long birthTime = cal.getTimeInMillis() / (1000 * 60 * 60 * 24);
        int interval = (int) (birthTime - currentTime) * -1;

        return interval;
    }
}
