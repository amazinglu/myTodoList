package com.example.guojing.mytodolist.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.guojing.mytodolist.AlarmReceiver;
import com.example.guojing.mytodolist.TodoEditActivity;
import com.example.guojing.mytodolist.model.Todo;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by AmazingLu on 8/31/17.
 */

public class AlarmUtils {
    public static void setAlarm(@NonNull Context context, @NonNull Todo todo) {
        Calendar c = Calendar.getInstance();
        // if date is smaller that date now, which is the past day, do nothing and return
        // the alarm is only for the future time
        if (todo.remainDate.compareTo(c.getTime()) < 0) {
            return;
        }
        /**
         * get the alarm service
         * */
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        /**
         * the intent is used to link the Alarm receiver with the current context
         * also pass necessary information from the current context to receiver
         * */
        Intent intent = new Intent(context, AlarmReceiver.class);
        /**
         * past the object to Alarm receiver so that the receiver can receive the object and
         * display the content to the notification center
         * */
        intent.putExtra(TodoEditActivity.KEY_TODO, todo);
        PendingIntent alarmIntent = PendingIntent.getBroadcast
                (context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, todo.remainDate.getTime(), alarmIntent);
    }
}
