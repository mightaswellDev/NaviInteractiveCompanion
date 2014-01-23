package com.mightaswell.navi.Service;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug.FlagToString;
import android.view.WindowManager;
import android.widget.ImageView;

import com.mightaswell.navi.R;

public class NaviFlight extends Service {
	
	private WindowManager window;
	private ImageView naviFly;
	private Display display;
	
	boolean mHasDoubleClicked = false;
	long lastPressTime;
	boolean _enable = true;
	
	int colorCount = 0;
	Color color;
	private boolean _xDockLeft;
	
	public NaviFlight() {
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		giftFromTheDekuTree();
		
	}
	public Point screenDimens(){
		display = window.getDefaultDisplay();
		Point p = new Point();
		display.getSize(p);
		return p;
	}
	
	public void giftFromTheDekuTree(){
		window = (WindowManager) getSystemService(WINDOW_SERVICE);
		naviFly = new ImageView(this);
		naviFly.setImageResource(R.drawable.navi_out_white);
		
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		
		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 100;
		
		window.addView(naviFly, params);
		
		heyListen(params);
		
			
	
	}
	
	

	
//	public void getNotified(){
//		noti = new NotificationListenerService() {
//			
//			@Override
//			public void onNotificationRemoved(StatusBarNotification sbn) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onNotificationPosted(StatusBarNotification sbn) {
//				// TODO Auto-generated method stub
//				
//				obj[notiCount] = sbn;
//				notiCount++;
//			}
//		};
//	}
//	public void initiatePopup(View anchor){
//		try{
//			Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//			ListPopupWindow popup = new ListPopupWindow(this);
//			popup.setAnchorView(anchor);
//			Point size = new Point();
//			display.getSize(size);
//			popup.setWidth((int)(size.y/(1.5)));
//			popup.setAdapter(new NotificationAdapter(getApplicationContext(), R.layout.popup_noti_layout, obj));
//			popup.setOnItemClickListener(new OnItemClickListener(){
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1,
//						int arg2, long arg3) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//			});
//			popup.show();
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(naviFly != null) window.removeView(naviFly);
	}
	
	
	private int checkDock(WindowManager.LayoutParams l, Point s){
		
		int returnedX = 0; 
		
		int naviY = l.y;
		int dockX = l.x;
		
		int naviSizeX = naviFly.getWidth();
		int naviSizeY = naviFly.getHeight();
		
		int widthHalf = s.x/2;
		int heightException = s.y/naviSizeY;
		
		if(naviY < heightException || naviY > s.y - heightException){
			returnedX = dockX;
		}else{
			if (dockX < widthHalf){
				returnedX = 0;
				_xDockLeft=true;
				
			}else if(dockX > widthHalf){
				returnedX = s.x - naviSizeX;
				_xDockLeft=false;
			}
		}
		return returnedX;
	}
	
	private void heyListen(final WindowManager.LayoutParams heyParams){
		try {
			naviFly.setOnTouchListener(new View.OnTouchListener() {
				private WindowManager.LayoutParams paramsF = heyParams;
				private int initialX;
				private int initialY;
				private float initialTouchX;
				private float initialTouchY;

				@Override 
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// Get current time in nano seconds.
						long pressTime = System.currentTimeMillis();


						// If double click...
						if (pressTime - lastPressTime <= 300) {
							mHasDoubleClicked = true;
						}
						else {     // If not double click....
							mHasDoubleClicked = false;
						}
						lastPressTime = pressTime; 
						initialX = paramsF.x;
						initialY = paramsF.y;
						initialTouchX = event.getRawX();
						initialTouchY = event.getRawY();
						break;
					case MotionEvent.ACTION_UP:
						
						int newDockedX = checkDock(paramsF, screenDimens());
						paramsF.x = newDockedX;
						if (!mHasDoubleClicked){
							if (initialY + (int) (event.getRawY() - initialTouchY) > initialY  + 250){
								navSwipe("DOWN");
							}else if (initialY + (int) (event.getRawY() - initialTouchY) < initialY - 250){
								navSwipe("UP");
							}else if (_xDockLeft && initialX + (int) (event.getRawX() - initialTouchX) > initialX  + 250){
								navSwipe("RIGHT");
							}else if (!_xDockLeft && initialX + (int) (event.getRawX() - initialTouchX) < initialX  - 250){
								navSwipe("LEFT");
							}
						}
						
						window.updateViewLayout(naviFly, paramsF);
						break;
					case MotionEvent.ACTION_MOVE:
						if (mHasDoubleClicked && (initialX + (int) (event.getRawX() - initialTouchX)) > screenDimens().x - naviFly.getWidth() && initialY + (int) (event.getRawY() - initialTouchY) > screenDimens().y - naviFly.getHeight()){
							NaviFlight.this.stopSelf();
						}else if(mHasDoubleClicked){
							paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
							paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
							window.updateViewLayout(naviFly, paramsF);
						}
						break;
					}
					return false;
				}

			
				
			}
			);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		naviFly.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	}

	
	
	
	public void navSwipe(String s){
		//Home-UP
		if(s.equals("UP")){
			
			naviFly.setColorFilter(Color.GREEN);
		}else if(s.equals("DOWN")){
		    
			naviFly.setColorFilter(Color.RED);
			 
		}else if(s.equals("RIGHT")){
			
			naviFly.setColorFilter(Color.CYAN);
			
		}else if(s.equals("LEFT")){
			
			naviFly.setColorFilter(Color.MAGENTA);
		}
	}
}
//	public void homeSwipe(){
////		Intent homeIntent = new Intent(Intent.CATEGORY_HOME);
//		
////		startActivity(homeIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
//		
//	}
//}
