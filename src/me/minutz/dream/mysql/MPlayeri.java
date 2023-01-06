package me.minutz.dream.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import me.minutz.dream.DMaine;
import me.minutz.dream.User;
import me.minutz.dream.ciphers.CCipher;
import me.minutz.dream.config.ConfigUtil;
import me.minutz.dream.config.PConfig;
import me.minutz.dream.config.UseriConfig;

public class MPlayeri {
	
	  private static String tname="playeri",lname="deexecutat";
	  
	  public static void load(){
			  createT();
			  loadExecutant();
			  loadUseri();
	  }
	  
	  public static void updateContName(String uuid,String nume){
		  String q = "UPDATE "+tname+" SET "
				  	+"nume='%num'"
				  	+" WHERE `uuid`='%uuid'";
		  q=q.replaceAll("%uuid", uuid);
		  q=q.replaceAll("%num", nume);
		  DMaine.mysql.execute(q);
	  }
	  public static void updateContPar(String uuid,String parola){
		  String q = "UPDATE "+tname+" SET "
				  	+"parola='%par'"
				  	+" WHERE `uuid`='%uuid'";
		  q=q.replaceAll("%uuid", uuid);
		  q=q.replaceAll("%par", CCipher.encrypt(DMaine.key, DMaine.iv, parola));
		  DMaine.mysql.execute(q);
	  }
	  
	  public static void updateCont(String uuid,String coins,String pcoins){
		  String q = "UPDATE "+tname+" SET "
				  	+"pcoins='%psct',"
				  	+"coins='%sct'"
				  	+" WHERE `uuid`='%uuid'";
		  q=q.replaceAll("%uuid", uuid);
		  q=q.replaceAll("%sct", ""+coins);
		  q=q.replaceAll("%psct", ""+pcoins);
		  DMaine.mysql.execute(q);
	  }
	
	  public static void updateCont(String uuid,String coins,String pcoins,int j){
		  String q = "UPDATE "+tname+" SET "
				  	+"coins='%sct',"
				  	+"pcoins='%psct',"
				  	+"ban='%res'"
				  	+" WHERE `uuid`='%uuid'";
		  q=q.replaceAll("%uuid", uuid);
		  q=q.replaceAll("%sct", ""+coins);
		  q=q.replaceAll("%psct", ""+pcoins);
		  q=q.replaceAll("%res", ""+j);
		  DMaine.mysql.execute(q);
	  }
	  
	  public static void addCont(User u){
		  int ban = 0;
		  if(u.isBanat()){
			  ban=1;
		  }
		  addCont(u.getUUID(),u.getNume(),u.getParola(),u.getCoins(),u.getPCoins(),ban,134);
	  }

	  public static void addCont(String uuid,String nume,String parola,double coins,double pcoins,int ban,int grad){
		  String q = "INSERT INTO "+tname+" (uuid, nume, parola, coins, pcoins, ban) VALUES ('$u', '$n', '$p', '$c', '$a', '$b');";
		  q = q.replace("$u", uuid);
		  q = q.replace("$n", nume);
		  q = q.replace("$p", CCipher.encrypt(DMaine.key, DMaine.iv, parola));
		  q = q.replace("$c", Double.toString(coins));
		  q = q.replace("$a", Double.toString(pcoins));
		  q = q.replace("$b", ""+ban);
		  DMaine.mysql.execute(q);
	  }  
	  
	  public static void delCont(String uuid){
		  String q = "DELETE FROM "+tname+" WHERE `uuid`="+'"'+uuid+'"';
		  DMaine.mysql.execute(q);
	  }
	  
	  private static String getStr(List<String> l){
		  String s="";
		  for(int i=0;i<l.size();i++){
			  if(l.size()-1!=i){
				  s=s+"`cmd`="+'"'+l.get(i)+'"';
				  s=s+" OR ";
			  }else{
				  s=s+"`cmd`="+'"'+l.get(i)+'"';
			  }
		  }
		  return s;
	  }
	  
	  public static void loadExecutant(){
			String query = "select * from "+lname;
			ResultSet rs = DMaine.mysql.query(query);
			try{
				String texec = "DELETE FROM "+lname+" WHERE ";
				List<String> ctdl = new ArrayList<String>();
			while(rs.next()){
				String ecmd = rs.getString("cmd");
		    	ctdl.add(ecmd);
			}
			new BukkitRunnable(){
				List<String> lola = ctdl;
				public void run(){
					for(String c:lola){
				    	String ecmd=c.replace("@3m@", " ");
				    	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ecmd);
					}
				}
			}.runTaskLater(DMaine.getPlugin(), 20*2);
			if(ctdl.size()>0){
			texec=texec+getStr(ctdl);
			DMaine.mysql.execute(texec);
			}
			}catch(SQLException e){
	  	    	System.out.println("[" +  DMaine.mysql.conName + "] Error loading cmds: " + e.getMessage());
			}
	  }
	  
	  public static void loadUseri(){
			String query = "select * from "+tname;
			ResultSet rs = DMaine.mysql.query(query);
			List<String> uuids = new ArrayList<String>();
			try{
			while(rs.next()){
				String uuid = rs.getString("uuid");
				String coins = rs.getString("coins");
				String pcoins = rs.getString("pcoins");
				PConfig pconf = ConfigUtil.getUUIDPlayerFile(uuid);
				FileConfiguration user = pconf.getPlayer();
				if(user.contains("coins")){
					uuids.add(uuid);
				if(!user.getString("coins").equals(coins)){
					user.set("coins", coins);
					pconf.save();
				}
				}
				
				if(user.contains("pcoins")){
				if(!user.getString("pcoins").equals(pcoins)){
					user.set("pcoins", pcoins);
					pconf.save();
				}
				}else{
					delCont(uuid);
				}
			}
			}catch(SQLException e){
	  	    	System.out.println("[" +  DMaine.mysql.conName + "] Error loading useri: " + e.getMessage());
			}
			@SuppressWarnings("unchecked")
			List<String> l = (List<String>) ConfigUtil.playerss.getList(UseriConfig.uuidList);
			if(l!=null){
			  for(String uuid : l){
				  if(!uuids.contains(uuid)){
					  addCont((User) UseriConfig.getUserByUUID(uuid,false));
				  }
			  }
			}
	  }
	  
	  public static void createT(){
		  String q = "CREATE TABLE IF NOT EXISTS "+tname+"("
				  + "uuid varchar(100) NOT NULL,"
				  + "nume varchar(20) NOT NULL,"
				  + "parola varchar(255) NOT NULL,"
				  + "coins varchar(100) NOT NULL,"
				  + "pcoins varchar(100) NOT NULL,"
				  + "ban int(1) NOT NULL"
				  + ");";
		  DMaine.mysql.execute(q);
		  String q2 = "CREATE TABLE IF NOT EXISTS "+lname+"("
				  + "cmd varchar(400) NOT NULL"
				  + ");";
		  DMaine.mysql.execute(q2);
	  }
}
