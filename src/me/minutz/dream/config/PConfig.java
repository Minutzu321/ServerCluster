package me.minutz.dream.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.FileConfiguration;

public class PConfig {
	
	  private File playerFile;
	  private FileConfiguration player;
	  
	public PConfig(File playerFile, FileConfiguration player) {
		this.playerFile = playerFile;
		this.player = player;
	    if (!playerFile.exists())
	    {
	      playerFile.getParentFile().mkdirs();
	      copy(null, playerFile);
	    }
	    try {
		      player.load(playerFile);
		    }
		    catch (Exception e)
		    {
		    }
	}
	
	  private static void copy(InputStream in, File file)
	  {
	    try
	    {
	      OutputStream out = new FileOutputStream(file);
	      byte[] buffer = new byte[63];
	      int len;
	      while ((len = in.read(buffer)) > 0)
	      {
	        out.write(buffer, 0, len);
	      }
	      out.close();
	      in.close();
	    }
	    catch (Exception localException)
	    {
	    }
	  }

	public File getPlayerFile() {
		return playerFile;
	}

	public void setPlayerFile(File playerFile) {
		this.playerFile = playerFile;
	}

	public FileConfiguration getPlayer() {
		return player;
	}

	public void setPlayer(FileConfiguration player) {
		this.player = player;
	}
	  
	public void save(){
		try {
			player.save(playerFile);
		} catch (IOException e) {
		}
	}
	

}
