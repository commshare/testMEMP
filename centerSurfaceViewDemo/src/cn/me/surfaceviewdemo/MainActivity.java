package cn.me.surfaceviewdemo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;






import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity implements PlayMediaItemListener {

	MediaItem mPlayItem=null;
	//    final String[] arrayFruit = new String[] { "苹果", "橘子", "草莓", "香蕉" };  
	private static final int ITEM_NUM=1;
//	private static final String LIST_ITEM_1="udp://@239.1.1.9:1234";
//	private static final String LIST_ITEM_2="/sdcard/Movies/kinogallery.com_0_300_Rise_of_an_Empire_trailer_int_o-720p.mp4";
//	private static final String LIST_ITEM_3="rtsp://192.168.1.120:8554/bipbop-gear1-all.ts";
//	private static final String LIST_ITEM_4="rtsp://192.168.1.120:8554/576.ts";
	private static String fileString = Environment
			.getExternalStorageDirectory() + "Movies;//zhangbinvideo.264";//"/h264/test.h264";
	private static final String LIST_ITEM_6="rtsp://192.168.1.120/The.Walking.Dead.S04E00.The.Oath.Part2.WEBrip.720x400-YYeTs.mp4";
	private static final String LIST_ITEM_7="/storage/emulated/0/Movies/300_Rise_of_an_Empire_trailer720p.mp4";
	private static final String LIST_ITEM_1080p="http://192.168.1.120/big_buck_bunny_1080p_H264_AAC_25fps_7200K.MP4";
	private static final String LIST_ITEM_720_400="http://192.168.1.120/The.Walking.Dead.S04E00.The.Oath.Part2.WEBrip.720x400-YYeTs.mp4";

	private final String[] arrayMediaItem=new String[]{
//			LIST_ITEM_1,			
//			LIST_ITEM_2,	
//			LIST_ITEM_3,
//			LIST_ITEM_4,
		//	fileString,
		//	LIST_ITEM_6,
			LIST_ITEM_7,
		//	LIST_ITEM_1080p,
		//	LIST_ITEM_720_400
	};
	
	private final String TAG = "ME_MP";

	private EditText et_path;
	private SurfaceView sv;
	private Button btn_play, btn_pause, btn_replay, btn_stop,btn_full_screen;
	private MediaPlayer mediaPlayer;
	private SeekBar seekBar;
	private int currentPosition = 0;
	private boolean isPlaying;
	private Button btn_default_list;
	private Button btn_save_path;
	

      //////////////use this 
    private ListView lv;
    private ContentAdapter adapter;
    List<MediaItem> mediaList;

	private   LayoutInflater mLI;// = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//2、根据资源里layout文件夹里的布局文件，膨胀出布局文件对象
	  private   LinearLayout mLL;// = (LinearLayout)mLI.inflate(R.layout.dialog_list,null);
	     
	     
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		seekBar = (SeekBar) findViewById(R.id.seekBar);
		sv = (SurfaceView) findViewById(R.id.sv);
		et_path = (EditText) findViewById(R.id.et_path);
		et_path.setText(LIST_ITEM_7);
		btn_play = (Button) findViewById(R.id.btn_play);
		btn_pause = (Button) findViewById(R.id.btn_pause);
		btn_replay = (Button) findViewById(R.id.btn_replay);
		btn_stop = (Button) findViewById(R.id.btn_stop);
		btn_full_screen=(Button)findViewById(R.id.btn_full_screen);

		//add list_button
		btn_save_path=(Button)findViewById(R.id.btn_save_path);
		 btn_default_list = (Button)findViewById(R.id.btn_default_list);
		btn_play.setOnClickListener(click);
		btn_pause.setOnClickListener(click);
		btn_replay.setOnClickListener(click);
		btn_stop.setOnClickListener(click);
		btn_default_list.setOnClickListener(click);
		btn_save_path.setOnClickListener(click);

		// 为SurfaceHolder添加回调
		sv.getHolder().addCallback(callback);
		
		// 4.0版本之下需要设置的属性
		// 设置Surface不维护自己的缓冲区，而是等待屏幕的渲染引擎将内容推送到界面
		// sv.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		// 为进度条添加进度更改事件
		seekBar.setOnSeekBarChangeListener(change);
		
		
		//
		mLI = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLL = (LinearLayout)mLI.inflate(R.layout.dialog_list,null);
		

		

		//3、获取布局文件中的控件
//	     RadioGroup mCreateRadioGroup = (RadioGroup)mLL.findViewById(R.id.radiogroup_create);
//	     final RadioButton mCreateFileButton = (RadioButton)mLL.findViewById(R.id.create_file);
//	     final RadioButton mCreateFolderButton = (RadioButton)mLL.findViewById(R.id.create_folder);
		

		//use this
		lv=(ListView)mLL.findViewById(R.id.listView);
		mediaList=new ArrayList<MediaItem>();
		
		createList(mediaList);
		//传入mediaList，希望不是空的
		//pass PlayMediaItemListener 
		//adapter=new ContentAdapter(this,mediaList,this);
		adapter=new ContentAdapter(this,mediaList);
		lv.setAdapter(adapter);
		
		Button btnPlaySelectedItem=(Button)mLL.findViewById(R.id.btn_set_item_to_play);
		btnPlaySelectedItem.setOnClickListener(click);
//		//下面的这个是可以的
//		btnPlaySelectedItem.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				//MediaItem item
//				mPlayItem=adapter.getPlayItem();
//				if(mPlayItem!=null)
//				{
//					et_path.setText(mPlayItem.getURL());
//					//直接播放如何？
//					play(0);
//		//use MainActivity.this by PanShangan
//					closeAlertDialog(MainActivity.this,mAlertDialog);
//					mAlertDialog.dismiss();
//				}
//				else
//					Log.e(TAG,"get play item fail !!!!");
//			}
//		});
//

	}
	
	 private void setPathToPlay(){
		mPlayItem=adapter.getPlayItem();
		if(mPlayItem!=null)
		{
			et_path.setText(mPlayItem.getURL());
			//直接播放如何？
			play(0);
			closeAlertDialog(this,mAlertDialog);
			//mAlertDialog.dismiss();
		}
		else
			Log.e(TAG,"get play item fail !!!!");
	}
	void createList(List<MediaItem> list)
	{
		if(list == null )
			Log.e(TAG,"ERROR ! default list is null !");
		else
		{
			for(int i=0;i<ITEM_NUM;i++)
			{
				MediaItem mItem=new MediaItem();
				mItem.setURL(arrayMediaItem[i]);
				list.add(mItem);
			}
		}
	}

	private AlertDialog mAlertDialog=null;

	//信息对话框
	protected void Messagedialog(String TempStr)
	{
	    AlertDialog.Builder builder = new Builder(MainActivity.this);
	  //  builder.setIcon(android.R.drawable.ic_menu_view);
	    builder.setMessage(TempStr);
	    //add by XiaoPan ,否则不能连续的关闭和弹出对话框
	    //java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
	    if(mLL.getParent() != null){
	    	((ViewGroup)mLL.getParent()).removeAllViews();
	    }
	    builder.setView(mLL);
	    builder.setTitle(R.string.choose_one_to_play);
	    builder.setPositiveButton(R.string.ok,
	                              new android.content.DialogInterface.OnClickListener()
	    {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//dialog.cancel();  //是不是都行啊
				dialog.dismiss();
			}
	    });
	    mAlertDialog=builder.create();
	    mAlertDialog.show();
	}
	
	private void closeAlertDialog(Context mContext,AlertDialog d)
	{
		if(d!=null)
			d.dismiss();
		else
			if(mContext!=null)
				Toast.makeText(mContext,"cannot close alert dialog！！！",0).show();
				
	}
	
	private Callback callback = new Callback() {
		// SurfaceHolder被修改的时候回调
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.i(TAG, "SurfaceHolder 被销毁");
			// 销毁SurfaceHolder的时候记录当前的播放位置并停止播放
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				currentPosition = mediaPlayer.getCurrentPosition();
				mediaPlayer.stop();
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Log.i(TAG, "SurfaceHolder 被创建");
			if (currentPosition > 0) {
				// 创建SurfaceHolder的时候，如果存在上次播放的位置，则按照上次播放位置进行播放
				play(currentPosition);
				currentPosition = 0;
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Log.i(TAG, "SurfaceHolder 大小被改变,format["+format+"]  width["+width+"] height["+height+"]");
		}

	};

	private OnSeekBarChangeListener change = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// 当进度条停止修改的时候触发
			// 取得当前进度条的刻度
			int progress = seekBar.getProgress();
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				// 设置当前播放的位置
				mediaPlayer.seekTo(progress);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

		}
	};

	private View.OnClickListener click = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.btn_play:
				play(0);
				break;
			case R.id.btn_pause:
				pause();
				break;
			case R.id.btn_replay:
				replay();
				break;
			case R.id.btn_stop:
				stop();
				break;
			case R.id.btn_default_list:
				show_default_list();
				break;
			case R.id.btn_save_path:
				save_customer_path();
				break;
			case R.id.btn_set_item_to_play:
				setPathToPlay();
				break;
				case R.id.btn_full_screen:
					full_screen();
					break;
			default:
				break;
			}
		}
	};
	private void full_screen(){

	}
	private void updateMediaItemList()
	{
		if(mediaList!=null)
			adapter.refreshData(mediaList);
	}
	protected void save_customer_path(){
		String path=et_path.getText().toString();
		if(path!=null)
		{
			MediaItem item=new MediaItem();
			item.setURL(path);
			mediaList.add(item);
			Toast.makeText(this, "添加客户路径 [ "+path+" ] 到列表",Toast.LENGTH_SHORT).show();
			updateMediaItemList();
		}
	}
	
	protected void show_default_list(){
		Messagedialog("android mediaplayer 测试列表");
	}
//	public  interface PlayMediaItemListener
//	{
//		void setPlayItem(MediaItem item);
//	}
	/*
	 * 停止播放
	 */
	protected void stop() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			btn_play.setEnabled(true);
			isPlaying = false;
		}
	}

	/**
	 * 开始播放
	 * 
	 * @param msec 播放初始位置    
	 */
	protected void play(final int msec) {
		// 获取视频文件地址
		//String path = et_path.getText().toString().trim();
		//add my url from item
		if(mPlayItem==null) {
			//Log.e(TAG, "no play item selected ! open play list first !!!");
			mPlayItem=new MediaItem();
			mPlayItem.setURL(LIST_ITEM_7);
		}
		else
		{
		String path=mPlayItem.getURL().trim();
//		File file = new File(path);
//		if (!file.exists()) {
//			Toast.makeText(this, "视频文件路径错误", 0).show();
//			return;
//		}
		if(path==null)
		{
			Toast.makeText(this,"path is null ！！！",0).show();
			return;
		}
			
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			// 设置播放的视频源
			//mediaPlayer.setDataSource(file.getAbsolutePath());
			mediaPlayer.setDataSource(path);
			Log.i(TAG, "--after setDataSource -----");
			// 设置显示视频的SurfaceHolder
			mediaPlayer.setDisplay(sv.getHolder());
			Log.i(TAG, "开始装载-------1---");
			mediaPlayer.prepareAsync();
			Log.i(TAG, "开始装载-------2-----");
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					Log.i(TAG, "装载完成");
					mediaPlayer.start();
					// 按照初始位置播放
					mediaPlayer.seekTo(msec);
					// 设置进度条的最大进度为视频流的最大播放时长
					seekBar.setMax(mediaPlayer.getDuration());
					// 开始线程，更新进度条的刻度
					new Thread() {

						@Override
						public void run() {
							try {
								isPlaying = true;
								while (isPlaying) {
									int current = mediaPlayer
											.getCurrentPosition();
									seekBar.setProgress(current);
									
									sleep(500);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();

					btn_play.setEnabled(false);
				}
			});
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// 在播放完毕被回调
					btn_play.setEnabled(true);
				}
			});

			//////////////暂时不要//////////////////////
//			mediaPlayer.setOnErrorListener(new OnErrorListener() {
//
//				@Override
//				public boolean onError(MediaPlayer mp, int what, int extra) {
//					Log.e(TAG,"发生错误重新播放");
//					// 发生错误重新播放
//					play(0);
//					isPlaying = false;
//					return false;
//				}
//			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		}//end else
	}

	/**
	 * 重新开始播放
	 */
	protected void replay() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(0);
			Toast.makeText(this, "重新播放", 0).show();
			btn_pause.setText("暂停");
			return;
		}
		isPlaying = false;
		play(0);
		

	}

	/**
	 * 暂停或继续
	 */
	protected void pause() {
		if (btn_pause.getText().toString().trim().equals("继续")) {
			btn_pause.setText("暂停");
			mediaPlayer.start();
			Toast.makeText(this, "继续播放", 0).show();
			return;
		}
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			btn_pause.setText("继续");
			Toast.makeText(this, "暂停播放", 0).show();
		}

	}
	@Override
	public void setPlayItem(MediaItem item) {
		// TODO Auto-generated method stub
		mPlayItem=item;
		
	}

}
