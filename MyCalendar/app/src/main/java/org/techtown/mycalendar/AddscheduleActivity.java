package org.techtown.mycalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddscheduleActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tv_location, tv_date;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addschedule);

        tv_date = findViewById(R.id.today_date);
        tv_location = findViewById(R.id.tv_location);
        Intent intent = getIntent();

        String location = intent.getExtras().getString("location");
        tv_location.setText(location);

        // 뒤로가기
        ImageButton button = findViewById(R.id.btn_back);
        button.setOnClickListener(this);

        date();
    }

    private void date() {
        Date currentTime = Calendar.getInstance().getTime();
        String date = new SimpleDateFormat("yyyy-MM-dd(EE요일)", Locale.getDefault()).format(currentTime);
        tv_date.setText(date);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
