package com.microdiary;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.microdiary.dao.Data;
import com.microdiary.dao.WeatherData;
import com.microdiary.util.MicroDiaryUtil;
import com.microdiary.util.NetWorkUtil;
import com.microdiary.util.ThreadUtil;
import com.microdiary.util.WebServiceUtil;

public class MainMicroDiary extends Activity {
	
	public static MainMicroDiary instance = null;
	 
	private ViewPager mTabPager;	
	private ImageView mTabImg;// 动画图片
	private ImageView mTab1,mTab2,mTab3,mTab4,mTab5;
	private int zero = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int one;//单个水平动画位移
	private int two;
	private int three;
	private int four;
	private LinearLayout mClose;
    private LinearLayout mCloseBtn;
    private View layout;	
	private boolean menu_display = false;
	private PopupWindow menuWindow;
	private LayoutInflater inflater;
	//private Button mRightBtn;
	
	
	MicroRiji microRiji;
	MicroNaozhong microNaozhong;
	MicroLicai microLicai;
	MicroJihua microJihua;
	MicroTianqi microTianqi;
	
	
	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == 0) {
//				microTianqi.initOnLineState();
				init();
			}
			else if(msg.what == 1) {
				microTianqi.fillCity();
			}
			else if(msg.what == 2) {
				microTianqi.showWeather();
			}
		}
    	
    };
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        
        setContentView(R.layout.main);
        
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        int[] bgIcons = new int[] {R.drawable.welcome, R.drawable.bg01, R.drawable.bg02, 
        		R.drawable.bg03, R.drawable.bg04};
        int index = (int) (Math.random() * 5);
        mainLayout.setBackgroundResource(bgIcons[index]);
      
        
        Data.data = new WeatherData(this);
        
//        ThreadUtil.showProgress(this);
		ThreadUtil.thread = new Thread(
				new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						if(!NetWorkUtil.isConnect(MainMicroDiary.this)) {
				        	Data.isOnline = false;
				        	try {
				        		Thread.sleep(2000);
				        	}catch (Exception e) {
				        		e.printStackTrace();
								// TODO: handle exception
							}
				        	
				        	String[] provinces = Data.data.getValue(WeatherData.PROVINCE_LIST).split(",");
				        	Data.provinces = new ArrayList<String>();
				        	for(int i = 0; i < provinces.length; i++) {
				        		Data.provinces.add(provinces[i]);
				        	}
				        	
				        	String[] cities = Data.data.getValue(WeatherData.CITY_LIST).split(",");
				        	Data.cities = new ArrayList<String>();
				        	for(int i = 0; i < cities.length; i++) {
				        		Data.cities.add(cities[i]);
				        	}
				        	
				        	handler.sendEmptyMessage(0);
				        	return;
				        }
						
						
				        Data.isOnline = true;
						
						try {
							Data.provinces = WebServiceUtil.getProvinceList();
							StringBuilder sb = new StringBuilder();
							for(int i = 0; i < Data.provinces.size(); i++) {
							    if(i == 0)
							    	sb.append(Data.provinces.get(i));
							    else
							    	sb.append("," + Data.provinces.get(i));
							}
							Data.data.setValue(WeatherData.PROVINCE_LIST, sb.toString());
							
							
							Data.cities = WebServiceUtil
									.getCityListByProvince(Data.provinces.get(Data.data.getProvinceId()));
							Data.detail = WebServiceUtil.getWeatherByCity(Data.cities.get(Data.data.getCityId()));
							sb = new StringBuilder();
							for(int i = 0; i < Data.cities.size(); i++) {
							    if(i == 0)
							    	sb.append(Data.cities.get(i));
							    else
							    	sb.append("," + Data.cities.get(i));
							}
							Data.data.setValue(WeatherData.CITY_LIST, sb.toString());
							
							
							ThreadUtil.closeProgress();
							handler.sendEmptyMessage(0);
						}catch (Exception e) {
							Data.isOnline = false;
							ThreadUtil.closeProgress();
							System.out.println("访问失败");
							
							String[] provinces = Data.data.getValue(WeatherData.PROVINCE_LIST).split(",");
				        	Data.provinces = new ArrayList<String>();
				        	for(int i = 0; i < provinces.length; i++) {
				        		Data.provinces.add(provinces[i]);
				        	}
				        	
				        	String[] cities = Data.data.getValue(WeatherData.CITY_LIST).split(",");
				        	Data.cities = new ArrayList<String>();
				        	for(int i = 0; i < cities.length; i++) {
				        		Data.cities.add(cities[i]);
				        	}
							
							handler.sendEmptyMessage(0);
							e.printStackTrace();
							// TODO: handle exception
						}
					}
				});
		ThreadUtil.thread.start();
        
        
        
    }
    
    public void init() {
    	setContentView(R.layout.main_microdiary);
        //启动后的set一个页面，，底层加viewpager。viewpager准备添加view。下面的view1--view5；
        //启动activity时不自动弹出软键盘
       getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
       instance = this;
       
       MicroDiaryUtil.createFile();
       /*
       mRightBtn = (Button) findViewById(R.id.right_btn);
       mRightBtn.setOnClickListener(new Button.OnClickListener()
		{	@Override
			public void onClick(View v)
			{	showPopupWindow (MainWeixin.this,mRightBtn);
			}
		  });*/
       
       mTabPager = (ViewPager)findViewById(R.id.tabpager);
       mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
       //主view主页面中最下各个imageview的引用。设置点击事件，zhuview上面就是viewpager
       // 点击切换view。
       mTab1 = (ImageView) findViewById(R.id.img_rj);
       mTab2 = (ImageView) findViewById(R.id.img_nz);
       mTab3 = (ImageView) findViewById(R.id.img_lc);
       mTab4 = (ImageView) findViewById(R.id.img_jh);
       mTab5 = (ImageView) findViewById(R.id.img_tq);
       mTabImg = (ImageView) findViewById(R.id.img_tab_now);
       mTab1.setOnClickListener(new MyOnClickListener(0));
       mTab2.setOnClickListener(new MyOnClickListener(1));
       mTab3.setOnClickListener(new MyOnClickListener(2));
       mTab4.setOnClickListener(new MyOnClickListener(3));
       mTab5.setOnClickListener(new MyOnClickListener(4));
       
       
       Display currDisplay = getWindowManager().getDefaultDisplay();//获取屏幕当前分辨率
       int displayWidth = currDisplay.getWidth();
       int displayHeight = currDisplay.getHeight();
       one = displayWidth/5; //设置水平动画平移大小
       two = one*2;
       three = one*3;
       four = one*4;
       //Log.i("info", "获取的屏幕分辨率为" + one + two + three + "X" + displayHeight);
       
       //InitImageView();//使用动画
     //将要分页显示的View装入数组中
       LayoutInflater mLi = LayoutInflater.from(this);
       View view1 = mLi.inflate(R.layout.main_tab_rj, null);
       View view2 = mLi.inflate(R.layout.main_tab_nz, null);
       View view3 = mLi.inflate(R.layout.main_tab_lc, null);
       View view4 = mLi.inflate(R.layout.main_tab_jh, null);
       View view5 = mLi.inflate(R.layout.main_tab_tq, null);
       
       
       
       microRiji = new MicroRiji(this, view1);
       microNaozhong = new MicroNaozhong(this, view2);
       microLicai = new MicroLicai(this, view3);
       microJihua = new MicroJihua(this, view4);
       microTianqi = new MicroTianqi(this, view5);
       
     //每个页面的view数据
       final ArrayList<View> views = new ArrayList<View>();
       views.add(view1);
       views.add(view2);
       views.add(view3);
       views.add(view4);
       views.add(view5);
     //填充ViewPager的数据适配器
       PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			
			//@Override
			//public CharSequence getPageTitle(int position) {
				//return titles.get(position);
			//}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		
		mTabPager.setAdapter(mPagerAdapter);
    }
    
    /**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	};
    
	 /* 页卡切换监听(原作者:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			//例如现在在中间currindex==2；那么，此时点击1，case = 0；这时第一个设置亮色，
			//通过动画，two到0；向右移动两个view，然后设置view3为暗色。
			case 0:
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.rj));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.nz_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.lc_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.jh_normal));
				}
				else if (currIndex == 4) {
					animation = new TranslateAnimation(four, 0, 0, 0);
					mTab5.setImageDrawable(getResources().getDrawable(R.drawable.tq_normal));
				}
				break;
			case 1:
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.nz));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.rj_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.lc_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.jh_normal));
				}
				else if (currIndex == 4) {
					animation = new TranslateAnimation(four, one, 0, 0);
					mTab5.setImageDrawable(getResources().getDrawable(R.drawable.tq_normal));
				}
				break;
			case 2:
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.lc));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.rj_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.nz_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.jh_normal));
				}
				else if (currIndex == 4) {
					animation = new TranslateAnimation(four, two, 0, 0);
					mTab5.setImageDrawable(getResources().getDrawable(R.drawable.tq_normal));
				}
				break;
			case 3:
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.jh));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, three, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.rj_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.nz_normal));
				}
				else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.lc_normal));
				}
				else if (currIndex == 4) {
					animation = new TranslateAnimation(four, three, 0, 0);
					mTab5.setImageDrawable(getResources().getDrawable(R.drawable.tq_normal));
				}
				break;
			case 4:
				mTab5.setImageDrawable(getResources().getDrawable(R.drawable.tq));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, four, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.rj_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, four, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.nz_normal));
				}
				else if (currIndex == 2) {
					animation = new TranslateAnimation(two, four, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.lc_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, four, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.jh_normal));
				}
				break;	
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			System.out.println("get data ok....");
			if(requestCode == 1) {
				if (!Environment.getExternalStorageState().equals(
						android.os.Environment.MEDIA_MOUNTED))
					{
						Toast.makeText(MainMicroDiary.this
							, "SD卡不存在，请插入SD卡！"
							, 3000)
							.show();
						return;
					}
				
			
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
				String name = MicroDiaryUtil.writeBitmap(bitmap);
				microRiji.currentDiary.setPicture(1);
				microRiji.currentDiary.setPhotoAddress(name);
				System.out.println("picname-->" + name);
				microRiji.picture.setImageResource(R.drawable.picture);
			}
			else if(requestCode == 2){
				if (!Environment.getExternalStorageState().equals(
						android.os.Environment.MEDIA_MOUNTED))
					{
					Toast.makeText(MainMicroDiary.this
							, "SD卡不存在，请插入SD卡！"
							, 3000)
							.show();
						return;
					}
				
				Uri uri = data.getData();
				String path = data.getDataString();
				microRiji.currentDiary.setAudio(1);
				microRiji.currentDiary.setAudioAddress(path);
				microRiji.audio.setImageResource(R.drawable.music_2);
				
			}
			else if(requestCode == 3){
				if (!Environment.getExternalStorageState().equals(
						android.os.Environment.MEDIA_MOUNTED))
					{
					Toast.makeText(MainMicroDiary.this
							, "SD卡不存在，请插入SD卡！"
							, 3000)
							.show();
						return;
					}
				
				Uri uri = data.getData();
				String path = data.getDataString();
				System.out.println("VIDEO-->" + path);
				microRiji.currentDiary.setVideo(1);
				microRiji.currentDiary.setVideoAddress(path);
				microRiji.video.setImageResource(R.drawable.movies);
				
			}
			else if(requestCode == 100) {
				System.out.println("weijihua on activity for result");
				microJihua.onActivityResultForJihua(requestCode, resultCode, data);
			}
		}
		else {
			System.out.println("get data error....");
		}
	}
	
	
	
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == 4)
			finish();
		/*
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  //获取 back键
    		
        	if(menu_display){         //如果 Menu已经打开 ，先关闭Menu
        		menuWindow.dismiss();
        		menu_display = false;
        		}
        	else {
        		Intent intent = new Intent();
            	intent.setClass(MicroDiary.this,Exit.class);
            	startActivity(intent);
        	}
    	}
    	
    	else if(keyCode == KeyEvent.KEYCODE_MENU){   //获取 Menu键			
			if(!menu_display){
				//获取LayoutInflater实例
				inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
				//这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
				//该方法返回的是一个View的对象，是布局中的根
				layout = inflater.inflate(R.layout.main_menu, null);
				
				//下面我们要考虑了，我怎样将我的layout加入到PopupWindow中呢？？？很简单
				menuWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); //后两个参数是width和height
				//menuWindow.showAsDropDown(layout); //设置弹出效果
				//menuWindow.showAsDropDown(null, 0, layout.getHeight());
				menuWindow.showAtLocation(this.findViewById(R.id.mainweixin), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
				//如何获取我们main中的控件呢？也很简单
				mClose = (LinearLayout)layout.findViewById(R.id.menu_close);
				mCloseBtn = (LinearLayout)layout.findViewById(R.id.menu_close_btn);
				
				
				//下面对每一个Layout进行单击事件的注册吧。。。
				//比如单击某个MenuItem的时候，他的背景色改变
				//事先准备好一些背景图片或者颜色
				mCloseBtn.setOnClickListener (new View.OnClickListener() {					
					@Override
					public void onClick(View arg0) {						
						//Toast.makeText(Main.this, "退出", Toast.LENGTH_LONG).show();
						Intent intent = new Intent();
			        	intent.setClass(MicroDiary.this,Exit.class);
			        	startActivity(intent);
			        	menuWindow.dismiss(); //响应点击事件之后关闭Menu
					}
				});				
				menu_display = true;				
			}else{
				//如果当前已经为显示状态，则隐藏起来
				menuWindow.dismiss();
				menu_display = false;
				}
			
			return false;
		}*/
    	return false;
    }
	/*
	//设置标题栏右侧按钮的作用
	public void btnmainright(View v) {  
		Intent intent = new Intent (MicroDiary.this,MainTopRightDialog.class);			
		startActivity(intent);	
		//Toast.makeText(getApplicationContext(), "点击了功能按钮", Toast.LENGTH_LONG).show();
      }  	
	public void startchat(View v) {      //小黑  对话界面
		Intent intent = new Intent (MicroDiary.this,ChatActivity.class);			
		startActivity(intent);	
		//Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
      }  
	public void exit_settings(View v) {                           //退出  伪“对话框”，其实是一个activity
		Intent intent = new Intent (MicroDiary.this,ExitFromSettings.class);			
		startActivity(intent);	
	 }
	public void btn_shake(View v) {                                   //手机摇一摇
		Intent intent = new Intent (MicroDiary.this,ShakeActivity.class);			
		startActivity(intent);	
	}*/
}
    
    

