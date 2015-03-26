package com.echo.onlineshoppingsys.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.onlineshoppingsys.R;
import com.echo.onlineshoppingsys.fragment.GuideMainTabFragment;
import com.echo.onlineshoppingsys.fragment.MessageMainTabFragment;
import com.echo.onlineshoppingsys.fragment.SetMainTabFragment;
import com.echo.onlineshoppingsys.model.GuideSite;
import com.echo.onlineshoppingsys.model.Threads;
import com.echo.onlineshoppingsys.services.SmsService;
import com.echo.onlineshoppingsys.util.DialogUtils;
import com.echo.onlineshoppingsys.util.SharedPreferencesUtils;

public class MainActivity extends FragmentActivity implements OnClickListener {
	
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments;
	private Dialog mDialog;
	
	//接收短信广播
	private BroadcastReceiver receiver;
	
	private int backClickTime;
	
	//短信会话和短信实体的集合
	public static List<Threads> list_threads;
	public static List<GuideSite> list_sites;
	
	//当前购物金额与上限金额
	public static float currentMoney = 0;
	public static int totalMoney = 0;
	
	public static int year;
	public static int month;
	
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			((MessageMainTabFragment)mAdapter.getItem(0)).adapter.notifyDataSetChanged();
			mDialog.dismiss();
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
		initData();
		initFragment();
		initListener();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		IntentFilter filter = new IntentFilter(SMS_RECEIVED);
	    receiver = new IncomingSMSReceiver();
	    registerReceiver(receiver, filter);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(receiver != null) {
			unregisterReceiver(receiver);
		}
	}
	
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.vp_main);
		list_threads = new ArrayList<Threads>();
		Time time = new Time("GMT+8");    
        time.setToNow();   
        year = time.year;   
        month = time.month + 1; //0-11
		mDialog = DialogUtils.getLoadingDialog(this, "正在加载银行短信…", false, null);
		mDialog.show();
	}
	
	private void initData() {
		list_sites = new ArrayList<GuideSite>();
		list_sites.add(new GuideSite("淘宝", "http://m.taobao.com/", BitmapFactory.decodeResource(getResources(), R.drawable.taobao_icon)));
		list_sites.add(new GuideSite("天猫", "http://m.tmall.com/", BitmapFactory.decodeResource(getResources(), R.drawable.tianmao_icon)));
		list_sites.add(new GuideSite("京东", "http://m.jd.com/", BitmapFactory.decodeResource(getResources(), R.drawable.jd_icon)));
		list_sites.add(new GuideSite("苏宁易购", "http://m.suning.com/", BitmapFactory.decodeResource(getResources(), R.drawable.suning_icon)));
		list_sites.add(new GuideSite("当当", "http://m.dangdang.com/", BitmapFactory.decodeResource(getResources(), R.drawable.dangdang_icon)));
		list_sites.add(new GuideSite("亚马逊", "http://m.amazon.cn/", BitmapFactory.decodeResource(getResources(), R.drawable.amzn_icon)));
		list_sites.add(new GuideSite("1号店", "http://m.yhd.com/", BitmapFactory.decodeResource(getResources(), R.drawable.yihaodian_icon)));
		list_sites.add(new GuideSite("凡客", "http://m.vancl.com/", BitmapFactory.decodeResource(getResources(), R.drawable.fanke_icon)));
		list_sites.add(new GuideSite("拉手网", "http://m.lashou.com/", BitmapFactory.decodeResource(getResources(), R.drawable.lashou_icon)));
		list_sites.add(new GuideSite("美团", "http://m.meituan.com/", BitmapFactory.decodeResource(getResources(), R.drawable.meituan_icon)));
		list_sites.add(new GuideSite("蘑菇街", "http://m.mogujie.com/", BitmapFactory.decodeResource(getResources(), R.drawable.mogujie_icon)));
		list_sites.add(new GuideSite("唯品会", "http://m.vip.com/", BitmapFactory.decodeResource(getResources(), R.drawable.vip_icon)));
		list_sites.add(new GuideSite("聚美优品", "http://m.jumei.com/", BitmapFactory.decodeResource(getResources(), R.drawable.jumei_icon)));
	
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getDataAsync();
			}
		}).start();
		SharedPreferencesUtils.getTotalMoney(this);
	}
	
	private void initFragment() {
		mFragments = new ArrayList<Fragment>();
		
		MessageMainTabFragment messageMainTabFragment = new MessageMainTabFragment();
		GuideMainTabFragment guideMainTabFragment = new GuideMainTabFragment();
		SetMainTabFragment setMainTabFragment = new SetMainTabFragment();
		
		mFragments.add(messageMainTabFragment);
		mFragments.add(guideMainTabFragment);
		mFragments.add(setMainTabFragment);
		
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				return mFragments.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				return mFragments.get(arg0);
			}
		};
		
		mViewPager.setAdapter(mAdapter);
	}
	
	private void initListener() {
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch(arg0) {
					case 0:
						((TextView) findViewById(R.id.tv_tab_name)).setText("短信");
						((ImageView) findViewById(R.id.iv_msg)).setImageResource(R.drawable.public_icon_tabbar_msg_pre);
						((ImageView) findViewById(R.id.iv_guide)).setImageResource(R.drawable.public_icon_tabbar_app_nm);
						((ImageView) findViewById(R.id.iv_set)).setImageResource(R.drawable.public_icon_tabbar_more_nm);
						break;
					case 1:
						((TextView) findViewById(R.id.tv_tab_name)).setText("导购");
						((ImageView) findViewById(R.id.iv_msg)).setImageResource(R.drawable.public_icon_tabbar_msg_nm);
						((ImageView) findViewById(R.id.iv_guide)).setImageResource(R.drawable.public_icon_tabbar_app_pre);
						((ImageView) findViewById(R.id.iv_set)).setImageResource(R.drawable.public_icon_tabbar_more_nm);
						break;
					case 2:
						((TextView) findViewById(R.id.tv_tab_name)).setText("设置");
						((ImageView) findViewById(R.id.iv_msg)).setImageResource(R.drawable.public_icon_tabbar_msg_nm);
						((ImageView) findViewById(R.id.iv_guide)).setImageResource(R.drawable.public_icon_tabbar_app_nm);
						((ImageView) findViewById(R.id.iv_set)).setImageResource(R.drawable.public_icon_tabbar_more_pre);
						break;
						
					default:
						break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		findViewById(R.id.msg_tab_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(0);
			}
		});
		
		findViewById(R.id.guide_tab_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(1);
			}
		});
		
		findViewById(R.id.set_tab_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(2);
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	//异步获取本地短信信息
	public void getDataAsync() {
		list_threads = SmsService.getSession(getContentResolver());
		SmsService.getCurrentMoney(getContentResolver(), list_threads);
		mHandler.sendEmptyMessage(0);
		if(currentMoney >= totalMoney)
			createNotify();
	}
	
	//超额提醒
	public void createNotify() {
		long[] vv = new long[] {0, 150, 100, 150};
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setVibrate(vv)
		        .setDefaults(Notification.DEFAULT_SOUND)
		        .setContentTitle("网购防沉迷系统")
		        .setTicker("您的购物金额已经超过上限金额，请注意！")
		        .setContentText("您的购物金额已经超过上限金额，请注意！");
		        
		Intent resultIntent = new Intent(this, MainActivity.class);
		 
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0, mBuilder.build());
	}
	
	//短信接收器
	private class IncomingSMSReceiver extends BroadcastReceiver {
		private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
		
		public void onReceive(Context _context, Intent _intent) {
			if (_intent.getAction().equals(SMS_RECEIVED)) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						getDataAsync();
					}
				}).start();
			}
		}
	}
	
	/** 连续按back键2次(间隔小于3秒 ) */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (backClickTime == 1) {
				finish();
				backClickTime = 0;
			} else {
				Toast.makeText(this, "再次点击返回退出", Toast.LENGTH_SHORT)
				.show();
				backClickTime++;
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						backClickTime = 0;
					}
				}, 3000);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
