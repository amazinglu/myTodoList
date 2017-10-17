package com.example.guojing.mytodolist.util;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by AmazingLu on 8/30/17.
 */

public class DateUtils {
    private static DateFormat dateformat =
            new SimpleDateFormat("yyyy MM dd HH:mm", Locale.getDefault());
    private static DateFormat dateFormatDate =
            new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
    private static  DateFormat dateFormatTime =
            new SimpleDateFormat("HH:mm",Locale.getDefault());

    @NonNull
    public static Date stringToDate(@NonNull String string) {
        try {
            // exception because the format of the string may not be right
            return dateformat.parse(string);
        } catch (ParseException e) {
            return Calendar.getInstance().getTime();
        }
    }

    @NonNull
    public static String dateToString(@NonNull Date date) {
        return dateformat.format(date);
    }

    @NonNull
    public static String dateToStringDate(@NonNull Date date) {
        return dateFormatDate.format(date);
    }

    @NonNull
    public static String dateToStringTime(@NonNull Date date) {
        return dateFormatTime.format(date);
    }


}
