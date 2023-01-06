package me.minutz.dream.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.minutz.dream.Alpha;
import me.minutz.dream.DMaine;
import me.minutz.dream.site.ServerPort;

public class ConfigUtil {
	  public static File playerssFile;
	  public static FileConfiguration playerss;
	  
	  public static File configFile;
	  public static FileConfiguration config;
	  public static void load()
			  {
		    try
		    {
				  configFile = new File(DMaine.getPlugin().getDataFolder(), "config.yml");
			      config = new YamlConfiguration();
			    if (!configFile.exists())
			    {
			      configFile.getParentFile().mkdirs();
			      copy(null, configFile);
			    }
			      setupConfig();
			      
			      if(config.get("lobby")!=null){
			    	  if(config.getBoolean("lobby")){
				  playerssFile = new File(DMaine.getPlugin().getDataFolder(), "playeri.yml");
			      playerss = new YamlConfiguration();
			    if (!playerssFile.exists())
			    {
			      playerssFile.getParentFile().mkdirs();
			      copy(null, playerssFile);
			    }
			    File f = new File(DMaine.getPlugin().getDataFolder().getAbsolutePath()+"\\players");
			    if(!f.exists()){
			    	f.mkdir();
			    }
			    	  }
			      }
			    loadYamls();
		    }
		    catch (Exception localException)
		    {
		    	localException.printStackTrace();
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
			  
			  public static PConfig getUUIDPlayerFile(String uuid){
				  File f = new File(DMaine.getPlugin().getDataFolder().getAbsolutePath()+"\\players", uuid+".yml");
			      YamlConfiguration yc = new YamlConfiguration();
			      return new PConfig(f,yc);
			  }

			  public static void saveYamls() {
			    try {
			    	if(Alpha.lobby){
			      playerss.save(playerssFile);
			    	}
			      config.save(configFile);
			    }
			    catch (IOException localIOException)
			    {
			    }
			  }

			  public static void loadYamls() {
			    try {
			    	if(Alpha.lobby){
			      playerss.load(playerssFile);
			    	}
			      config.load(configFile);
			    }
			    catch (Exception localException)
			    {
			    	localException.printStackTrace();
			    	load();
			    }
			  }
			  
			  private static void setupConfig(){
				  try {
				  config.load(configFile);
				  

				  if(config.get("serverName")==null){
				  config.set("serverName", "PUNE NUMELE AICI");
				  }
				  if(config.get("lobby")==null){
				  config.set("lobby",false);
				  
				  if(config.get("lobbyPort")==null){
				  config.set("lobbyPort", 25001);
				  }
				  if(config.get("serverPort")==null){
				  config.set("serverPort", 25002);
				  }
				  
				  }else{
					  if(config.get("lobbyPort")==null){
						  config.set("lobbyPort", 25001);
					  }
					  if(!config.getBoolean("lobby")){
					  if(config.get("serverPort")==null){
						  config.set("serverPort", 25002);
					  }
					}else{
						if(config.get("serverPort")!=null){
							config.set("serverPort", null);
						}
					}
				  }
				  if(config.getBoolean("lobby")){
					  if(config.get("mysql.host")==null){
				  config.set("mysql.host", "localhost");
					  }
					  if(config.get("mysql.dbname")==null){
				  config.set("mysql.dbname", "cubewars");
					  }
					  if(config.get("mysql.user")==null){
				  config.set("mysql.user", "root");
					  }
					  if(config.get("mysql.password")==null){
				  config.set("mysql.password", "exemplu");
					  }
					  if(config.get("siteServerPort")==null){
				  config.set("siteServerPort", 25000);
					  }
					  if(config.get("siteServers-PortsList")==null){
				  List<String> sl = new ArrayList<String>();
				  ServerPort sp = new ServerPort("exemplu",20346);
				  sl.add(sp.toString());
				  config.set("siteServers-PortsList", sl);
					  }
				  }
				  
				  config.save(configFile);
				} catch (Exception e) {

				}
			  }
			  
			  
//				public static void saveUsersList(String path,List<?> l){
//					playerss.set(path, l);
//					saveYamls();
//				}
			 
}