package net.cybisoft.looplayer;

import java.io.Console;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

public class PlaybackController implements MediaPlayer.OnCompletionListener {

	private VideoView wVideoView;
	private ImageView wImageView;
	private MainActivity mainActivity;
	
	private Playlist currentPlaylist;
	
	private String currentFilename;

	private ScheduledExecutorService executor;
	
	public PlaybackController(MainActivity activity, Playlist playlist) {
		mainActivity = activity;
		
		wVideoView = activity.wVideoView;
		wVideoView.setOnCompletionListener(this);
		
		wImageView = activity.wImageView;
		
		currentPlaylist = playlist;
		
		executor = Executors.newSingleThreadScheduledExecutor();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		playNextMedia();
	}

	public void pause() {
		if(currentFilename != null){
	        wVideoView.pause();
	    }
	}
	
	public void playNextMedia()
	{
		// A video has finished playing.
		if (currentPlaylist == null)
		{
			Toast.makeText(mainActivity.getBaseContext(), "No more videos to play.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Plays next video.
		currentFilename = currentPlaylist.goToNext();
		if (currentFilename != null) {
			//Log.d("PLOP", "next: " + currentFilename);
			
			if (Playlist.isVideo(currentFilename)) {
				
				// Plays video.
				wVideoView.setVisibility(View.VISIBLE);
				wVideoView.setVideoPath(currentFilename);
				wVideoView.start();
				
				// Hide image.
				wImageView.setVisibility(View.INVISIBLE);
				
			} else if (Playlist.isImage(currentFilename)) {
				
				// Plays image.
				wImageView.setImageURI(Uri.parse(currentFilename));
				wImageView.setVisibility(View.VISIBLE);
				int playbackTimeSecs = getPlaybackTimeFromFilename(currentFilename);
				//Log.d("PLOP", playbackTimeSecs + "");
				if (playbackTimeSecs > 0) {
					startPlaybackTimer(playbackTimeSecs);
				}
				
				// Hides video.
				wVideoView.setVisibility(View.INVISIBLE);
				
			}
		}
		
		// Toast
		//Toast.makeText(getBaseContext(),"Now playing " + currentFilename, Toast.LENGTH_SHORT).show();
	}

	private void startPlaybackTimer(int playbackTimeSecs) {
		final Runnable r = new Runnable() {
			
			@Override
			public void run() {
				playNextMedia();
			}
		};
		
		executor.schedule(r, (long)playbackTimeSecs, TimeUnit.SECONDS);
	}

	private int getPlaybackTimeFromFilename(String filename) {
		int ret = 15; // default playback time is 15 seconds.
		
		String[] elements = filename.split(" ");
		
		if (elements != null) {
			for (String s : elements) {
				if (s.endsWith("s")) {
					try {
						ret = Integer.parseInt(s.substring(0, s.length() - 1));
						
						if (ret < 1) {
							ret = -1;
						}
					} catch (Exception ex) {
						// nothing to do - this was not a valid time string.
					}
				}
			}
		}
		
		return ret;
	}
	
}
