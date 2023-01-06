package me.minutz.dream.config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;

import me.minutz.dream.Alpha;
import me.minutz.dream.Alpha.BY;
import me.minutz.dream.DMaine;
import me.minutz.dream.OUser;
import me.minutz.dream.User;
import me.minutz.dream.ciphers.CCipher;

public class UseriConfig {
	
	public static String uuidList = "ulist";
	public static String getRUUID(){
		String uuid = UUID.randomUUID().toString();
		if(getUserByUUID(uuid,true)!=null){
			return getRUUID();
		}
		return uuid;
	}
	public static boolean exists(String uuid){
		if(Alpha.lobby){
		@SuppressWarnings("unchecked")
		List<String> l = (List<String>) ConfigUtil.playerss.getList(uuidList);
		if(l!=null){
			if(l.contains(uuid)){
				return true;
			}
		}
		return false;
		}else{
			if(Alpha.getOUser(BY.UUID, uuid)!=null){
				return true;
			}else{
				return false;
			}
		}
		
	}
	
	public static void addIpToUUID(String uuid, IpDat ip){
		PConfig pconf = ConfigUtil.getUUIDPlayerFile(uuid);
		FileConfiguration user = pconf.getPlayer();

		List<String> l = (List<String>) user.getList("iplist");
		if(l!=null){
			for(String ipds : l){
				IpDat ipd = IpDat.fromString(ipds);
				if(ipd.getIp().equals(ip.getIp())){
					l.remove(ipds);
					break;
				}
			}
		l.add(ip.toString());
		user.set("iplist", l);
		pconf.save();
		}else{
		l = new ArrayList<String>();
		l.add(ip.toString());
		user.set("iplist", l);
		pconf.save();
		}
	}
	
	public static void addUser(User u){
		addUser(u.getUUID(),u.getNume(),u.getParola(),u.getIplist().get(0),u.getGUUID());
	}
	
	public static User addUser(String uuid,String nuser,String par,IpDat ip,String guuid){
		PConfig pconf = ConfigUtil.getUUIDPlayerFile(uuid);
		FileConfiguration user = pconf.getPlayer();
		List<IpDat> iplist = new ArrayList<IpDat>();
		iplist.add(ip);
		User u = null;
		if(uuid!=null){
			u=new User(nuser,uuid,par,iplist,false,0,0,guuid,pconf);
		}else{
			u=new User(nuser,getRUUID(),par,iplist,false,0,0,guuid,pconf);
		}
		user.set("nume", nuser);
		user.set("guuid", guuid);
		user.set("par", CCipher.encrypt(DMaine.key, DMaine.iv, par));
		user.set("ban", false);
		user.set("coins", Double.toString(u.getCoins()));
		user.set("pcoins", Double.toString(u.getPCoins()));
		List<String> dl = new ArrayList<String>();
		for(IpDat hh:iplist){
			dl.add(hh.toString());
		}
		user.set("iplist",dl);
		pconf.save();
		@SuppressWarnings("unchecked")
		List<String> l = (List<String>) ConfigUtil.playerss.getList(uuidList);
		if(l!=null){
		l.add(u.getUUID());
		ConfigUtil.playerss.set(uuidList, l);
		ConfigUtil.saveYamls();
		}else{
		l = new ArrayList<String>();
		l.add(u.getUUID());
		ConfigUtil.playerss.set(uuidList, l);
		ConfigUtil.saveYamls();
		}
		return u;
	}
	
	public static OUser getUserByUUID(String uuid,boolean simple){
		if(Alpha.lobby){
		if(exists(uuid)){
			PConfig pconf = ConfigUtil.getUUIDPlayerFile(uuid);
			FileConfiguration user = pconf.getPlayer();
			String nume = user.getString("nume");
			double coins = Double.parseDouble(user.getString("coins"));
			double pcoins = Double.parseDouble(user.getString("pcoins"));
			if(!simple){
			String guuid = user.getString("guuid");
			String par = CCipher.decrypt(DMaine.key, user.getString("par"));
			boolean b = user.getBoolean("ban");
			List<String> ipl = user.getStringList("iplist");
			List<IpDat> dl = new ArrayList<IpDat>();
			for(String hh:ipl){
				dl.add(IpDat.fromString(hh));
			}
			return new User(nume,uuid,par,dl,b,coins,pcoins,guuid,pconf);
			}
			return new OUser(nume,uuid,coins,pcoins,true);
		}
	}else{
		return Alpha.getOUser(BY.UUID, uuid);
	}
		return null;
	}
	
	public static OUser getUserByNume(String nume,boolean simple){
		if(Alpha.lobby){
		@SuppressWarnings("unchecked")
		List<String> l = (List<String>) ConfigUtil.playerss.getList(uuidList);
		if(l==null){
			return null;
		}
		for(String uuid:l){
			OUser usr = getUserByUUID(uuid,simple);
			if(usr.getNume().toLowerCase().equals(nume.toLowerCase())){
				return usr;
			}
		}
		}else{
			return Alpha.getOUser(BY.NAME, nume);
		}
		return null;
	}
	
	public static OUser getUserByGUUID(String guuid){
		if(Alpha.lobby){
		@SuppressWarnings("unchecked")
		List<String> l = (List<String>) ConfigUtil.playerss.getList(uuidList);
		if(l==null){
			return null;
		}
		for(String uuid:l){
			User usr = (User) getUserByUUID(uuid,false);
			if(usr.getGUUID().equals(guuid)){
				return usr;
			}
		}
		}else{
			return Alpha.getOUser(BY.GUUID, guuid);
		}
		return null;
	}
	
	public static List<User> getUseriByIP(String ip){
		@SuppressWarnings("unchecked")
		List<String> l = (List<String>) ConfigUtil.playerss.getList(uuidList);
		if(l==null){
			return null;
		}
		List<User> ul = new ArrayList<User>();
		for(String uuid:l){
			User usr = (User) getUserByUUID(uuid,false);
			for(IpDat id : usr.getIplist()){
				if(id.getIp().equals(ip)){
					ul.add(usr);
				}
			}
		}
		if(ul.size()>0){
		return ul;	
		}
		return null;
	}
	
}
