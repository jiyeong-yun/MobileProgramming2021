package org.techtown.mycalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button addBtn;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.btn_add);
        recyclerView = findViewById(R.id.recyclerView);

        addBtn.setOnClickListener(this);

        recycleView();
    }

    @Override
    public void onClick(View v) {
        //TODO: 일정 추가 버튼 기능
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(intent);
    }

    private void recycleView() {
        ListAdapter adapter;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        List<Data> list = new ArrayList<>();
        list.add(new Data("시험", "2021-05-23","공대7호관","10시"));

        adapter = new ListAdapter(this, (ArrayList<Data>) list);
        recyclerView.setAdapter(adapter);
    }
}