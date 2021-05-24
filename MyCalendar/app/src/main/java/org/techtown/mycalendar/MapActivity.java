package org.techtown.mycalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MapActivity extends AppCompatActivity {
    Intent intent;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map);
        // TODO: 위치 넘겨서 각 카드뷰에 지도 띄우기 switch문 말고~

        intent = getIntent();
        position = intent.getIntExtra("position", -1);

        switch (position){
            case 0:
//                setContentView(R.layout.activity_one);
                break;

            case 1:
//                setContentView(R.layout.activity_two);
                break;

            case 2:
                //추가 하면됨
                break;
        }
    }
}