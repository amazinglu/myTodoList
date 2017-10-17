package com.example.guojing.mytodolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.guojing.mytodolist.model.Todo;
import com.example.guojing.mytodolist.util.AlarmUtils;
import com.example.guojing.mytodolist.util.DateUtils;
import com.example.guojing.mytodolist.util.UIutils;

import java.util.Date;
import java.util.Calendar;

/**
 * Created by AmazingLu on 8/30/17.
 */

public class TodoEditActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    public static final String KEY_TODO = "todo_edit";
    public static final String KEY_TODO_ID = "todo_edit_id";
    public static final String KEY_NOTIFICATION_ID = "todo_notification_id";

    private Todo todo;
    private Date remainDate;

    private TextView todoEditText;
    private FloatingActionButton fab;
    private TextView deleteButton;
    private CheckBox completedCB;
    private TextView dateSet;
    private TextView timeSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todo = getIntent().getParcelableExtra(KEY_TODO);
        cancelNotificationIfNeeded();
        setUpUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpUI() {
        setContentView(R.layout.activity_edit_todo);
        setUpActionBar();
        todoEditText = (TextView) findViewById(R.id.todo_detail_edit_text);
        completedCB = (CheckBox) findViewById(R.id.todo_detail_completed_check_box);
        dateSet = (TextView) findViewById(R.id.todo_detail_edit_day_set);
        timeSet = (TextView) findViewById(R.id.todo_detail_edit_time_set);
        setUpSaveButton();
        setUpDeleteButton();
        setUpCompletedCheckBox();
        setUpDatePicker();

        if (todo == null) {
            todo = new Todo();
            dateSet.setText("set date");
            timeSet.setText("set time");
        } else {
            todoEditText.setText(todo.todoText);
            completedCB.setChecked(todo.completed);
            UIutils.setTextViewStrikeThrough(todoEditText, todo.completed);

            if (todo.remainDate != null) {
                /**
                 * the remain date is actually long long time
                 * change it to string date will precise to date
                 * chenge it to string time will precixse to time, like %
                 * for example, 0 day 6 hour is 6:00 and 1 day 6 hours is also 6:00
                 * same with date
                 * */
                dateSet.setText(DateUtils.dateToStringDate(todo.remainDate));
                timeSet.setText(DateUtils.dateToStringTime(todo.remainDate));
            } else {
                dateSet.setText("set date");
                timeSet.setText("set time");
            }
        }
        remainDate = todo != null ? todo.remainDate : null;
    }

    private void setUpActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        setTitle(null);
    }

    private void setUpSaveButton() {
        fab = (FloatingActionButton) findViewById(R.id.todo_detail_edit_add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndExit();
            }
        });
    }

    private void setUpDeleteButton() {
        deleteButton = (TextView) findViewById(R.id.todo_detail_edit_delete);
        if (todo == null) {
            deleteButton.setVisibility(View.GONE);
        } else {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra(KEY_TODO_ID, todo.id);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

    private void setUpCompletedCheckBox() {
        completedCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                UIutils.setTextViewStrikeThrough(todoEditText, isChecked);
                todoEditText.setTextColor(isChecked ? Color.GRAY : Color.WHITE);
            }
        });

        View checkBoxWarpper = findViewById(R.id.todo_detail_completed_layout);
        checkBoxWarpper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completedCB.setChecked(!completedCB.isChecked());
            }
        });

    }

    /**
     * use the DatePickerDialog to help the user to set the date and time
     * also use Java.util.Calender to get the year. month, day, hour and minute from the Data object
     * the listener do the following:
     * create a Calendar object, set the date and time based on remain date to the dialog
     * */
    private void setUpDatePicker() {
        dateSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = getCalendarFromRemindDate();
                Dialog dialog = new DatePickerDialog(
                        TodoEditActivity.this,
                        TodoEditActivity.this,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        timeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = getCalendarFromRemindDate();
                Dialog dialog = new TimePickerDialog(
                        TodoEditActivity.this,
                        TodoEditActivity.this,
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        true);
                dialog.show();
            }
        });
    }

    /**
     * when we open this activity via notification, we have to cancel this notification
     * when the activity is opened
     * */
    private void cancelNotificationIfNeeded() {
        // if nothing in the intent with id of KEY_NOTIFICATION_ID, notificationId = -1
        int notificationId = getIntent().getIntExtra(KEY_NOTIFICATION_ID, -1);
        if (notificationId != -1) {
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .cancel(notificationId);
        }
    }

    private void saveAndExit() {
        todo.todoText = todoEditText.getText().toString();
        todo.completed = completedCB.isChecked();
        todo.remainDate = remainDate;
        /**
         * set the alarm
         * */
        if (todo.remainDate != null) {
            AlarmUtils.setAlarm(TodoEditActivity.this, todo);
        }
        Intent intent = new Intent();
        intent.putExtra(KEY_TODO, todo);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * set the calendar to the remaindate
     * */
    private Calendar getCalendarFromRemindDate() {
        Calendar c = Calendar.getInstance();
        if (remainDate != null) {
            c.setTime(remainDate);
        }
        return c;
    }

    /**
     * after we set the date and time on the dialog
     * the function will give us the year, month, day, hour and minute
     * we reset the remain date
     * */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        // this method will be called after user has chosen date from the DatePickerDialog
        Calendar c = getCalendarFromRemindDate();
        c.set(year, monthOfYear, dayOfMonth);
        remainDate = c.getTime();
        dateSet.setText(DateUtils.dateToStringDate(remainDate));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
        // this method will be called after user has chosen time from the TimePickerDialog
        Calendar c = getCalendarFromRemindDate();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minutes);
        remainDate = c.getTime();
        timeSet.setText(DateUtils.dateToStringTime(remainDate));
    }
}
