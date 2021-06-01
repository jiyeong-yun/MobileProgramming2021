package org.techtown.mycalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddscheduleActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tv_location, today_date;
    Button btn_date, btn_time;
    Calendar c = Calendar.getInstance();
    String date, time;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addschedule);

        today_date = findViewById(R.id.today_date);
        tv_location = findViewById(R.id.tv_location);
        btn_date = findViewById(R.id.btn_date);
        btn_time = findViewById(R.id.btn_time);
        ImageButton button = findViewById(R.id.btn_back);

        Intent intent = getIntent();
        String location = intent.getExtras().getString("location");
        tv_location.setText(location);

        btn_date.setOnClickListener(this);
        btn_time.setOnClickListener(this);
        button.setOnClickListener(this);

        date();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_date:
                setDate();
                break;
            case R.id.btn_time:
                setTime();
                break;
        }
    }

    // 오늘 날짜 표시
    private void date() {
        Date currentTime = Calendar.getInstance().getTime();
        //EE로 요일 표시 가능
        String date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(currentTime);
        today_date.setText(date);
    }

    private void setDate() {
        int Year = c.get(Calendar.YEAR);
        int Month = c.get(Calendar.MONTH);
        int Day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String stringMonth = String.valueOf(month+1);
                String stringDay = String.valueOf(dayOfMonth);
                if((month + 1) < 10){
                    stringMonth = "0" + String.valueOf(month+1);
                }
                if(dayOfMonth < 10){
                    stringDay = "0" + String.valueOf(dayOfMonth);
                }
                date = year + "/" + stringMonth + "/" + stringDay;
                btn_date.setText(date);
            }
        }, Year, Month, Day);
        if (btn_date.isClickable()) {
            datePickerDialog.show();
        }
    }

    private void setTime() {
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddscheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String state = "오전";
                String stringHour = String.valueOf(selectedHour);
                String stringMinute = String.valueOf(selectedMinute);
                // 선택한 시간이 12시가 넘으면 "오후"로 변경, -12시간해서 출력
                if(selectedHour == 12){
                    state = "오후";
                }else if (selectedHour > 12){
                    selectedHour -= 12;
                    state = "오후";
                }
                if(selectedHour < 10) {
                    stringHour = "0" + String.valueOf(selectedHour);
                }
                if(selectedMinute < 10) {
                    stringMinute = "0" + String.valueOf(selectedMinute);
                }
                time = state + " " + stringHour + " : " + stringMinute;
                btn_time.setText(time);
            }
        }, hour, minute, false); // true -> 24시간 형식
        timePickerDialog.show();
    }
}
