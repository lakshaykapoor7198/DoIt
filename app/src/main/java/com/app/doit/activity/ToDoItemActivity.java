package com.app.doit.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.doit.alarm.AlarmReceiver;
import com.app.doit.dao.TodoItemDao;
import com.app.doit.model.TodoItemDto;
import com.app.doit.R;
import com.app.doit.datetime.DatePickerFragment;
import com.app.doit.datetime.TimePickerFragment;

import java.util.Calendar;

public class ToDoItemActivity extends AppCompatActivity {
    private final TodoItemDto todoItem = new TodoItemDto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editText = findViewById(R.id.todoItemTitle);

        Button timePickerButton = findViewById(R.id.todoItemTime);
        timePickerButton.setOnClickListener((v) -> {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.show(getSupportFragmentManager(), "timePicker");
            timePickerFragment.setActivity(this);
        });

        Button datePickerButton = findViewById(R.id.todoItemDate);
        datePickerButton.setOnClickListener((v) -> {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            datePickerFragment.setActivity(this);
        });

        Button todoItemAddButton = findViewById(R.id.todoItemAdd);
        todoItemAddButton.setOnClickListener((v) -> {
            todoItem.setTitle(editText.getText().toString());
            if (todoItem.validate()){
                createAlarm();
                TodoItemDao.saveTodoItem(this, todoItem);
                Intent intent = new Intent(ToDoItemActivity.this, MainActivity.class);
                startActivity(intent);
                Log.i(getString(R.string.doitapptag), todoItem.toString());
                Toast.makeText(ToDoItemActivity.this, "Added todo item" + todoItem.getTitle(), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ToDoItemActivity.this, "Invalid todoItem" + todoItem.getTitle(), Toast.LENGTH_SHORT).show();
                Log.e(getString(R.string.doitapptag), "Invalid todoItem" + todoItem.getTitle());
            }
        });
    }

    @SuppressLint("ScheduleExactAlarm")
    private void createAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra(getString(R.string.todotitle),todoItem.getTitle());
        int requestId = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestId, intent, PendingIntent.FLAG_IMMUTABLE);
        todoItem.setAlarmRequestId(requestId);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, todoItem.getYear());
        calendar.set(Calendar.MONTH, todoItem.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, todoItem.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, todoItem.getHour());
        calendar.set(Calendar.MINUTE, todoItem.getMinute());
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() ,pendingIntent);
    }

    public void setTime(int hour, int minute){
        todoItem.setHour(hour);
        todoItem.setMinute(minute);
    }

    public void setDate(int day, int month, int year){
        todoItem.setDay(day);
        todoItem.setMonth(month);
        todoItem.setYear(year);
    }
}