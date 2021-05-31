package org.techtown.mycalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddscheduleActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tv_location;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addschedule);

        tv_location = findViewById(R.id.tv_location);
        Intent intent = getIntent();

        String location = intent.getExtras().getString("location");
        tv_location.setText(location);

        // 뒤로가기
        ImageButton button = findViewById(R.id.btn_back);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
