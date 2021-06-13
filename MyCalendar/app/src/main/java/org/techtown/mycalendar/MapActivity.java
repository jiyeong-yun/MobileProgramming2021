package org.techtown.mycalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class MapActivity extends AppCompatActivity {
    Intent intent;
    TMapView tmapview;
    TMapPoint tMapPointStart;
    TMapPoint tMapPointEnd;
    double lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 지도 띄우기
        tmapview = new TMapView(this);
        tmapview.setSKTMapApiKey("l7xxf07bcc5a789e4d678d5622e927b5e84a");

        initialize(tmapview);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,1,locationListener);
        }catch (SecurityException e){
            Toast.makeText(getApplicationContext(),"위치가 파악 불가!", Toast.LENGTH_SHORT).show();
            locationManager.removeUpdates(locationListener);
        }

        tMapPointStart = new TMapPoint(lat, lon);
        tMapPointEnd = new TMapPoint(35.84854, 127.12937);

        TMapPolyLine polyLine = new TMapPolyLine();
        PathAsync pathAsync = new PathAsync();
        pathAsync.execute(polyLine);
    }

    class PathAsync extends AsyncTask<TMapPolyLine, Void, TMapPolyLine> {
        @Override
        protected TMapPolyLine doInBackground(TMapPolyLine... tMapPolyLines) {
            TMapPolyLine tMapPolyLine = tMapPolyLines[0];
            try {
                tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd);
                tMapPolyLine.setOutLineColor(Color.BLACK);
                tMapPolyLine.setLineWidth(4);

            }catch(Exception e) {
                e.printStackTrace();
                Log.e("error",e.getMessage());
            }
            return tMapPolyLine;
        }

        @Override
        protected void onPostExecute(TMapPolyLine tMapPolyLine) {
            super.onPostExecute(tMapPolyLine);
            tmapview.addTMapPolyLine("Line", tMapPolyLine);
        }
    }

    private void initialize(TMapView tmapview) {
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        linearLayoutTmap.addView(tmapview);

        // 전북대로 설정
        tmapview.setOnClickListenerCallBack(mOnClickListenerCallback);
        tmapview.setZoomLevel(15);

        intent = getIntent();
        String location = intent.getExtras().getString("location");

        // 마커표시
        ArrayList<String> arrBuilding = new ArrayList<>();
        arrBuilding.add(location);

        searchPOI(arrBuilding);
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lat = location.getLatitude(); // 위도 읽어오기
            lon = location.getLongitude(); // 경도 읽어오기
            tmapview.setCenterPoint(lon, lat);
            tmapview.setLocationPoint(lon, lat);
            tmapview.setIconVisibility(true);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) { }

        @Override
        public void onProviderEnabled(String s) { }

        @Override
        public void onProviderDisabled(String s) { }
    };

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
                    for(int j = 0; j < arrayList.size(); j++)
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

}