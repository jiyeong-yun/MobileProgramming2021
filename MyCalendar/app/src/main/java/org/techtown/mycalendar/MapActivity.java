package org.techtown.mycalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.Map;

public class MapActivity extends AppCompatActivity {
    Intent intent;
    int position;
    Handler handler;

    TMapView tmapview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // 지도 띄우기
        tmapview = new TMapView(this);
        tmapview.setSKTMapApiKey("l7xxf07bcc5a789e4d678d5622e927b5e84a");

        initialize(tmapview);

    }

    private void initialize(TMapView tmapview) {
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        linearLayoutTmap.addView(tmapview);

        // 전북대로 설정
        tmapview.setOnClickListenerCallBack(mOnClickListenerCallback);
        tmapview.setZoomLevel(15);
        tmapview.setCenterPoint(127.129436, 35.846964);

        // 마커표시
        ArrayList<String> arrBuilding = new ArrayList<>();
        arrBuilding.add("공과대학 7호관");

        searchPOI(arrBuilding);

    }

    // 주변 명칭 검색
    private void searchPOI(ArrayList<String> arrPOI)
    {
        final TMapData tMapData = new TMapData();
        final ArrayList<TMapPoint> arrTMapPoint = new ArrayList<>();
        final ArrayList<String> arrTitle = new ArrayList<>();
        final ArrayList<String> arrAddress = new ArrayList<>();

        for(int i = 0; i < arrPOI.size(); i++ )
        {
            tMapData.findTitlePOI(arrPOI.get(i), new TMapData.FindTitlePOIListenerCallback()
            {
                @Override
                public void onFindTitlePOI(ArrayList<TMapPOIItem> arrayList)
                {
                    for(int j = 0; j < arrayList.size(); j++ )
                    {
                        TMapPOIItem tMapPOIItem = arrayList.get(j);
                        arrTMapPoint.add(tMapPOIItem.getPOIPoint());
                        arrTitle.add(tMapPOIItem.getPOIName());
                        arrAddress.add(tMapPOIItem.upperAddrName + " " +
                                tMapPOIItem.middleAddrName + " " + tMapPOIItem.lowerAddrName);
                    }
                    setMultiMarkers(arrTMapPoint, arrTitle, arrAddress);
                }
            });
        }
    }

    // 마커 설정
    private void setMultiMarkers(ArrayList<TMapPoint> arrTPoint, ArrayList<String> arrTitle,
                                 ArrayList<String> arrAddress)
    {
        for( int i = 0; i < arrTPoint.size(); i++ )
        {
            Bitmap bitmapIcon = createMarkerIcon(R.drawable.poi_dot);

            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            tMapMarkerItem.setIcon(bitmapIcon);

            tMapMarkerItem.setTMapPoint(arrTPoint.get(i));

            tmapview.addMarkerItem("markerItem" + i, tMapMarkerItem);

            setBalloonView(tMapMarkerItem, arrTitle.get(i), arrAddress.get(i));
        }
    }

    // 풍선뷰
    private void setBalloonView(TMapMarkerItem marker, String title, String address)
    {
        marker.setCanShowCallout(true);

        if( marker.getCanShowCallout() )
        {
            marker.setCalloutTitle(title);
            marker.setCalloutSubTitle(address);

//            Bitmap bitmap = createMarkerIcon(R.drawable.right_arrow);
//            marker.setCalloutRightButtonImage(bitmap);
        }
    }

    private Bitmap createMarkerIcon(int image)
    {
        Log.e("MapViewActivity", "(F)   createMarkerIcon()");

        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), image);
        bitmap = Bitmap.createScaledBitmap(bitmap, 50, 50,false);

        return bitmap;
    }

    // TODO: 지도 이벤트 설정하기
    TMapView.OnClickListenerCallback mOnClickListenerCallback = new TMapView.OnClickListenerCallback() {
        @Override
        public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
            double latitude = tMapPoint.getLatitude();
            double longitude = tMapPoint.getLongitude();

//                Toast.makeText(MapActivity.this, "onPressed~!", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
//                Toast.makeText(MapActivity.this, "onPressUp~!", Toast.LENGTH_SHORT).show();
            return false;
        }
    };





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