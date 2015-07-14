package net.cybisoft.looplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Playlist extends ArrayList<String> {
	
	private static final long serialVersionUID = 1L;
	private int currentItem = -1;
	
	/**
	 * Creates a Playlist from a directory.
	 * @param directoryPath Path of the directory.
	 */
	public Playlist(String directoryPath) 
	{
		// loads all media items from the directory.
		this.addAll(getMediaList(new File(directoryPath)));
	}
	
	private List<String> getMediaList(File parentDir) 
	{
		ArrayList<String> inFiles = new ArrayList<String>();
		
	    File[] files = parentDir.listFiles();
	    
	    for (File file : files) {
	        if (file.isDirectory()) {
	            inFiles.addAll(getMediaList(file));
	        } else {
	        	String fileName = file.getName();
	            if(isImage(fileName) || isVideo(fileName)){
	                inFiles.add(file.getAbsolutePath());
	            }
	        }
	    }
	    return inFiles;
	}
	
	public String getCurrent()
	{
		if (currentItem >= 0 && currentItem < size())
		{
			return get(currentItem);
		} 
		else 
		{
			return null;
		}
	}
	
	public String goToNext() {
		// Nothing to do if there's no media!
		if (size() < 1) 
		{
			return null;
		}

		// Gets the next item to play.
		currentItem++;
		if (currentItem >= size()) 
		{
			// No more media in the playlist, restart from the first.
			currentItem = 0;
		}
		
		return get(currentItem);
	}
	
	public static boolean isImage(String filename) {
		return filename.toLowerCase(Locale.ROOT).endsWith(".jpg");
	}

	public static boolean isVideo(String filename) {
		String lowerFilename = filename.toLowerCase(Locale.ROOT);
		return lowerFilename.endsWith(".mp4") || lowerFilename.endsWith(".mov");
	}
}
