package org.techtown.mycalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button addBtn;
    RecyclerView recyclerView;

    private UserRepository userRepository;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.btn_add);
        recyclerView = findViewById(R.id.recyclerView);

        UserDatabase db= Room.databaseBuilder(getApplicationContext(),UserDatabase.class,"db-mary")
                .fallbackToDestructiveMigration() //스키마 버전 변경 가능
                .allowMainThreadQueries() // 메인 스레드에서 DB에 IO를 가능하게 함
                .build();
        userRepository=db.userRepository();

        addBtn.setOnClickListener(this);

        recycleView();
    }

    @Override
    public void onClick(View v) {
        //TODO: 일정 추가 버튼 기능
        Intent intent = new Intent(getApplicationContext(), Map_AddscheduleActivity.class);
        startActivity(intent);
    }

    private void recycleView() {
        ListAdapter adapter;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        /*List<Data> list = new ArrayList<>();
        list.add(new Data("시험", "2021-05-23","오전 10:00", "공대7호관","10시"));*/

        List<Data> list = userRepository.findAll();

        for( i = 0; i < list.size(); i++) {
            Log.d("TAG", "onCreate: findAll() : " + list.get(i).getUid());
            Log.d("TAG", "onCreate: findAll() : " + list.get(i).getTodo());
            Log.d("TAG", "onCreate: findAll() : " + list.get(i).getDate());
            Log.d("TAG", "onCreate: findAll(): "+list.get(i).getLocation());
            Log.d("TAG", "onCreate: findAll(): "+list.get(i).getMemo());
        }

        adapter = new ListAdapter(this, (ArrayList<Data>) list);
        recyclerView.setAdapter(adapter);
    }
}