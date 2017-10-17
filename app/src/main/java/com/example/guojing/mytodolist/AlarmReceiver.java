package com.example.guojing.mytodolist;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.guojing.mytodolist.model.Todo;

/**
 * Created by AmazingLu on 8/31/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "alarm!", Toast.LENGTH_LONG).show();
        final int notificationId = 100; // will be use to cancel the notification
        /**
         * get the object from intent, which is set in AlarmUntils
         * */
        Todo todo = intent.getParcelableExtra(TodoEditActivity.KEY_TODO);
        /**
         * build the notification
         * */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_event_available_white_24px)
                .setContentTitle(todo.todoText)
                .setContentText(todo.todoText);

        /**
         * intent which is the link of receiver and the TodoEditActivity
         * we have to send the to do object to TodoEditActivity so that TodoEditActivity can
         * build up the UI when we click the notification and jump to TodoEditActivity
         * also we have to send the notificationId to TodoEditActivity so that we can cancel this
         * notification in TodoEditActivity based on this id
         * */
        Intent resultIntent = new Intent(context, TodoEditActivity.class);
        resultIntent.putExtra(TodoEditActivity.KEY_TODO, todo);
        resultIntent.putExtra(TodoEditActivity.KEY_NOTIFICATION_ID, notificationId);
        /**
         * PendingIntent is a wrapper of intent and going to execute the intent sometime in the future
         * */
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(notificationId, builder.build());
    }
}
