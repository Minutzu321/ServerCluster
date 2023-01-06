package me.minutz.dream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.Bukkit;

public class Update
{
  private String url;
  private URL download;
  private boolean out = true,ready = false;

  public Update(String pluginurl)
  {
    this.url = pluginurl;
    try {
		download = new URL(url);
		ready=true;
	} catch (MalformedURLException e) {
		ready=false;
	}
  }

  public void externalUpdate()
  {
    try
    {
    	if(ready){

      BufferedInputStream in = null;
      FileOutputStream fout = null;
      try {
        if (this.out) {
          System.out.println("Updating...");
        }
        in = new BufferedInputStream(download.openStream());
    	String s = new java.io.File(DMaine.class.getProtectionDomain()
  			  .getCodeSource()
  			  .getLocation()
  			  .getPath()).getAbsolutePath();
        fout = new FileOutputStream(s);

        byte[] data = new byte[1024];
        int count;
        while ((count = in.read(data, 0, 1024)) != -1)
          fout.write(data, 0, count);
      }
      finally {
        if (in != null) {
          in.close();
        }
        if (fout != null) {
          fout.close();
        }
      }

      if (this.out) {
        System.out.println("Done!");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kickall");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
      }
    }
    }
    catch (IOException localIOException) {
    	localIOException.printStackTrace();
    }
  }
  public boolean isReady(){
	  return ready;
  }
  
}