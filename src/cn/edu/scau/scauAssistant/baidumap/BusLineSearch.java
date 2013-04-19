package cn.edu.scau.scauAssistant.baidumap;
import java.util.ArrayList;
import java.util.List;

import org.scauhci.studentAssistant.R;
import org.scauhci.studentAssistant.main.AssistantApplication;

import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKRoute;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKStep;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.RouteOverlay;


public class BusLineSearch extends MapActivity {
	Button mBtnSearch = null;	// ������ť
	
	MapView mMapView = null;	// ��ͼView
	MKSearch mSearch = null;	// ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	String  mCityName = null;
	MKBusLineResult busLineResult=null;
	LocationListener mLocationListener = null;//createʱע���listener��Destroyʱ��ҪRemove
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.buslinesearch);
        
        AssistantApplication app = (AssistantApplication)this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new AssistantApplication.MyGeneralListener());
		}
		app.mBMapMan.start();
        // ���ʹ�õ�ͼSDK�����ʼ����ͼActivity
        super.initMapActivity(app.mBMapMan);
        
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.setBuiltInZoomControls(true);
        //���������Ŷ���������Ҳ��ʾoverlay,Ĭ��Ϊ������
        mMapView.setDrawOverlayWhenZooming(true);
        
        // ��ʼ������ģ�飬ע���¼�����
        mSearch = new MKSearch();
        mSearch.init(app.mBMapMan, new MKSearchListener(){

			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// ����ſɲο�MKEvent�еĶ���
				if (error != 0 || res == null) {
					Toast.makeText(BusLineSearch.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_LONG).show();
					return;
		        }
				
				// �ҵ�����·��poi node
                MKPoiInfo curPoi = null;
                int totalPoiNum  = res.getNumPois();
				for( int idx = 0; idx < totalPoiNum; idx++ ) {
				    curPoi = res.getPoi(idx);
                    if ( 2 == curPoi.ePoiType ) {
                        // poi���ͣ�0����ͨ�㣬1������վ��2��������·��3������վ��4��������·
                        mSearch.busLineSearch(mCityName, curPoi.uid);
                    	break;
                    }
				}
				
				// û���ҵ�������Ϣ
                if (curPoi == null) {
                    Toast.makeText(BusLineSearch.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_LONG).show();
                    return;
                }
				
			}
			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
			}
			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
			}
			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
			}
			public void onGetAddrResult(MKAddrInfo res, int error) {
			}
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
				if (iError != 0 || result == null) {
					Toast.makeText(BusLineSearch.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_LONG).show();
					return;
		        }

				RouteOverlay routeOverlay = new RouteOverlay(BusLineSearch.this, mMapView);
			    // �˴���չʾһ��������Ϊʾ��
			    routeOverlay.setData(result.getBusRoute());
			    mMapView.getOverlays().clear();
			    mMapView.getOverlays().add(routeOverlay);
			    mMapView.invalidate();
			    
			    mMapView.getController().animateTo(result.getBusRoute().getStart());
			    
			    busLineResult=result;
			    MKRoute route=busLineResult.getBusRoute();
			    /*
			    List<ArrayList<GeoPoint>> geoPoints=route.getArrayPoints();
			    if(geoPoints!=null&&geoPoints.size()>0){
		    		ArrayList<GeoPoint> geoPoint=geoPoints.get(0);
		    		for(int i=0;i<geoPoint.size();i++){
		    			GeoPoint point=geoPoint.get(i);
		    			System.out.println("geoPoint "+i+" :"+point.getLatitudeE6()+" "+point.getLongitudeE6());
		    		}
		    	}*/
			    for(int i=0;i<route.getNumSteps();i++){
			    	MKStep step=route.getStep(i);
			    	System.out.println("step "+i+" :"+step.getContent()+" "+
			    	     step.getPoint().getLatitudeE6()+" "+step.getPoint().getLongitudeE6());
			    }
			}
			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
				// TODO Auto-generated method stub
				
			}
            @Override
            public void onGetRGCShareUrlResult(String arg0, int arg1) {
                // TODO Auto-generated method stub
                
            }
			

        });
        
        // �趨������ť����Ӧ
        mBtnSearch = (Button)findViewById(R.id.search);
        
        OnClickListener clickListener = new OnClickListener(){
			public void onClick(View v) {
					SearchButtonProcess(v);
			}
        };
        
        mBtnSearch.setOnClickListener(clickListener); 
        
        /** ���ذ�ť  **/
		Button back = (Button)findViewById(R.id.backButton);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			}
		});
	}
	void SearchButtonProcess(View v) {
		if (mBtnSearch.equals(v)) {
			EditText editCity = (EditText)findViewById(R.id.city);
			EditText editSearchKey = (EditText)findViewById(R.id.searchkey);
			mCityName = editCity.getText().toString(); 
			mSearch.poiSearchInCity(mCityName, editSearchKey.getText().toString());
		}
	}
	@Override
	protected void onResume() {
		// ע�ᶨλ�¼�
        mLocationListener = new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				if(location != null){
					String strLog = String.format("����ǰ��λ��:\r\n" +
							"γ��:%f\r\n" +
							"����:%f",
							location.getLongitude(), location.getLatitude());
					System.out.println(strLog);
					//TextView mainText = (TextView)findViewById(R.id.textview);
			       // mainText.setText(strLog);
				}
			}
        };
		AssistantApplication app = (AssistantApplication)this.getApplication();
		app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
		app.mBMapMan.start();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
