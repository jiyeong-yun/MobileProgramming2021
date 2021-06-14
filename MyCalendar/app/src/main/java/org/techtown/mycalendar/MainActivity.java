package org.techtown.mycalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button addBtn;
    RecyclerView recyclerView;
    SwipeController swipeController = null;

    Intent intent;
    private UserRepository userRepository;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.btn_add);
        recyclerView = findViewById(R.id.recyclerView);

        addBtn.setOnClickListener(this);

        UserDatabase db= Room.databaseBuilder(getApplicationContext(),UserDatabase.class,"db-mary")
                .fallbackToDestructiveMigration() //스키마 버전 변경 가능
                .allowMainThreadQueries() // 메인 스레드에서 DB에 IO를 가능하게 함
                .build();
        userRepository=db.userRepository();

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
                userRepository.delete(list.get(position).getUid());
                adapter.datas.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }
            @Override
            public void onLeftClicked(int position){
                //TODO:장소 변경방법 생각... 일자시간 변경...
                String location = list.get(position).getLocation();
                int uid = list.get(position).getUid();
                userRepository.delete(list.get(position).getUid());
                adapter.datas.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                intent.putExtra("location", location);
                //intent.putExtra("uid", uid);
                intent = new Intent(getApplicationContext(), AddscheduleActivity.class);
                startActivity(intent);
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

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;

    @Override
    public void onBackPressed() {
        //두번 눌러 종료
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }

}