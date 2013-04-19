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
	Button mBtnSearch = null;	// 搜索按钮
	
	MapView mMapView = null;	// 地图View
	MKSearch mSearch = null;	// 搜索模块，也可去掉地图模块独立使用
	String  mCityName = null;
	MKBusLineResult busLineResult=null;
	LocationListener mLocationListener = null;//create时注册此listener，Destroy时需要Remove
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
        // 如果使用地图SDK，请初始化地图Activity
        super.initMapActivity(app.mBMapMan);
        
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.setBuiltInZoomControls(true);
        //设置在缩放动画过程中也显示overlay,默认为不绘制
        mMapView.setDrawOverlayWhenZooming(true);
        
        // 初始化搜索模块，注册事件监听
        mSearch = new MKSearch();
        mSearch.init(app.mBMapMan, new MKSearchListener(){

			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// 错误号可参考MKEvent中的定义
				if (error != 0 || res == null) {
					Toast.makeText(BusLineSearch.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
					return;
		        }
				
				// 找到公交路线poi node
                MKPoiInfo curPoi = null;
                int totalPoiNum  = res.getNumPois();
				for( int idx = 0; idx < totalPoiNum; idx++ ) {
				    curPoi = res.getPoi(idx);
                    if ( 2 == curPoi.ePoiType ) {
                        // poi类型，0：普通点，1：公交站，2：公交线路，3：地铁站，4：地铁线路
                        mSearch.busLineSearch(mCityName, curPoi.uid);
                    	break;
                    }
				}
				
				// 没有找到公交信息
                if (curPoi == null) {
                    Toast.makeText(BusLineSearch.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
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
					Toast.makeText(BusLineSearch.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
					return;
		        }

				RouteOverlay routeOverlay = new RouteOverlay(BusLineSearch.this, mMapView);
			    // 此处仅展示一个方案作为示例
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
        
        // 设定搜索按钮的响应
        mBtnSearch = (Button)findViewById(R.id.search);
        
        OnClickListener clickListener = new OnClickListener(){
			public void onClick(View v) {
					SearchButtonProcess(v);
			}
        };
        
        mBtnSearch.setOnClickListener(clickListener); 
        
        /** 返回按钮  **/
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
		// 注册定位事件
        mLocationListener = new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				if(location != null){
					String strLog = String.format("您当前的位置:\r\n" +
							"纬度:%f\r\n" +
							"经度:%f",
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
