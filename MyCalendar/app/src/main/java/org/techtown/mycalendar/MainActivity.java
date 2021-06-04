package org.techtown.mycalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button addBtn;
    RecyclerView recyclerView;
    SwipeController swipeController = null;

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
        // 일정 추가
        Intent intent = new Intent(getApplicationContext(), Map_AddscheduleActivity.class);
        startActivity(intent);
    }

    private void recycleView() {
        ListAdapter adapter;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        List<Data> list = userRepository.findAll();

        for( i = 0; i < list.size(); i++) {
            Log.d("###", "findAll - getUid(): " + list.get(i).getUid());
            Log.d("###", "findAll - getTodo(): " + list.get(i).getTodo());
            Log.d("###", "findAll - getDate(): " + list.get(i).getDate());
            Log.d("###", "findAll - getTime(): " + list.get(i).getTime());
            Log.d("###", "findAll - getLocation(): "+list.get(i).getLocation());
            Log.d("###", "findAll - getMemo(): "+list.get(i).getMemo());
        }

        adapter = new ListAdapter(this, (ArrayList<Data>) list);

        recyclerView.setAdapter(adapter);

        swipeController = new SwipeController(new SwipeControllerAction() {
            @Override
            public void onRightClicked(int position) {
                adapter.datas.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                list.remove(position);
            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }
}