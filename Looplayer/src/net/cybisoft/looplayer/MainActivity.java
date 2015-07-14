package net.cybisoft.looplayer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends Activity {

	private PlaybackController playbackController;
	
	public VideoView wVideoView;
	public ImageView wImageView;
	
	private WakeLock wakeLock;
	
	private String dummyVersion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		
		// Register for crashes.
		Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
		
		// Gets the version.
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			dummyVersion = pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			dummyVersion = "r1-c";
		}
		
		// Removes the windows decorations and goes fullscreen.
		//getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Gets the wake lock.
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
	    wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyMediaPlayer");

	    // Shows the content.
		setContentView(R.layout.activity_main);
		
		// Inits the widgets.
		wVideoView = (VideoView) findViewById(R.id.videoView);
		wImageView = (ImageView) findViewById(R.id.imageView);

		// Loads the playlist to play.
		Playlist currentPlaylist = new Playlist(Environment.getExternalStorageDirectory().toString()+"/Movies");
		playbackController = new PlaybackController(this, currentPlaylist);
		Toast.makeText(getBaseContext(), getString(R.string.app_name) + " " + dummyVersion + " - " + currentPlaylist.size() + " videos will now be played in loop.", Toast.LENGTH_LONG).show();
		playbackController.playNextMedia();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		
		wakeLock.release();
		
		playbackController.pause();
	}

	@Override
	protected void onResume() {

		super.onResume();
		
		wakeLock.acquire();
	}

}
