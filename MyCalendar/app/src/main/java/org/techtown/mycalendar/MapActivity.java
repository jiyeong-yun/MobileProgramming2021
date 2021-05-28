package org.techtown.mycalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {
    Intent intent;
    int position;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        TMapView tmapview = new TMapView(this);

        tmapview.setSKTMapApiKey( "l7xxf07bcc5a789e4d678d5622e927b5e84a" );
        linearLayoutTmap.addView( tmapview );

/*        tmapdata.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList poiItem) {
                for(int i = 0; i < poiItem.size(); i++) {
                    TMapPOIItem  item = (TMapPOIItem) poiItem.get(i);
                    Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                            "Address: " + item.getPOIAddress().replace("null", "")  + ", " +
                            "Point: " + item.getPOIPoint().toString());
                }
            }
        });*/

        // 리버스 지오코딩
        try {
            final String address = new TMapData().convertGpsToAddress(tmapview.getLatitude(), tmapview.getLongitude());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MapActivity.this, address, Toast.LENGTH_SHORT).show();
                }
            });
        }catch(Exception e) {
            e.printStackTrace();
        }

        //지도 이벤트 설정하기
        tmapview.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
//                Toast.makeText(MapActivity.this, "onPress~!", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
//                Toast.makeText(MapActivity.this, "onPressUp~!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


/*        // TODO: 위치 넘겨서 각 카드뷰에 지도 띄우기 switch문 말고~

        intent = getIntent();
        position = intent.getIntExtra("position", -1);

        switch (position){
            case 0:
                setContentView(R.layout.activity_map);
                break;

            case 1:
//                setContentView(R.layout.activity_two);
                break;

            case 2:
                //추가 하면됨
                break;
        }*/
    }
}