package me.minutz.dream.hive;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.minutz.dream.Alpha;
import me.minutz.dream.Alpha.BY;
import me.minutz.dream.DMaine;
import me.minutz.dream.Update;
import me.minutz.dream.ciphers.CCipher;
import me.minutz.dream.config.ConfigUtil;
import me.minutz.dream.config.PConfig;
import me.minutz.dream.config.UseriConfig;
import me.minutz.dream.mysql.MPlayeri;
import me.minutz.dream.site.SCmd;
import me.minutz.dream.site.ServerPort;
import me.minutz.dream.util.JSONMessage;

public class HCmd {
	public static String getResp(String s){
	    String cmd = s.split(":")[0];
	    String[] args = new String[s.split(":").length - 1];
	    for (int i = 1; i < s.split(":").length; i++) {
	      args[(i - 1)] = s.split(":")[i];
	    }
	    if(cmd.equals("ping")){
	    	return "pong";
	    }
	    
	    if(cmd.equals("update")){
	    	if(Alpha.lobby){
	    	int port = Integer.parseInt(args[0]);
	    	if(port!=0){
	    		String url = args[1].replaceAll("@dp@", ":");
		    	List<String> ar = ConfigUtil.config.getStringList("siteServers-PortsList");
		    	for(String sp : ar){
		    		ServerPort spl = ServerPort.fromString(sp);
		    		if(spl.getPort()!=port){
		    			SCmd.sendToServer("update:"+args[1], spl.getPort());
		    		}
		    	}
	    		DMaine.update(url);
	    	}else{
		    	List<String> ar = ConfigUtil.config.getStringList("siteServers-PortsList");
		    	for(String sp : ar){
		    		ServerPort spl = ServerPort.fromString(sp);
		    		SCmd.sendToServer("update:"+args[1], spl.getPort());
		    	}
	    	}
	    	}else{
	    		String url = args[0].replaceAll("@dp@", ":");
	    		DMaine.update(url);
	    	}
	    	return "succes";
	    }
	    
	    if(cmd.equals("isonline")){
	    	String nume = args[0];
	    	Player p = Bukkit.getPlayer(nume);
	    	if(p!=null){
	    		if(p.isOnline()){
	    			return "da:"+p.getUniqueId().toString();
	    		}
	    	}else{
	    		return "nu";
	    	}
	    }
	    
	    if(cmd.equals("sendcode")){
	    	boolean again = Boolean.getBoolean(args[0]);
	    	String cod = args[1];
	    	String uuid = args[2];
	    	String nume = args[3];
	    	Player p = Bukkit.getPlayer(nume);
	    	if(p!=null){
	    		if(p.isOnline()){
	    	if(again){
	    		JSONMessage.create("Here it's another code: ")
				.color(ChatColor.AQUA)
			.then(cod)
				.color(ChatColor.GOLD)
				.style(ChatColor.BOLD)
			.then(".")
				.color(ChatColor.AQUA)
			.then(" If you didn't retryed, ")
				.color(ChatColor.RED)
			.then("click here")
				.color(ChatColor.GOLD)
				.style(ChatColor.BOLD)
				.tooltip("ATTENTION! You should not play with this. Read carefully")
				.runCommand("/cw oopsydoopsy "+uuid+" "+cod)
				.send(p);
	    			}else{
	    				JSONMessage.create("Congratulations! You registered on our site. Now, you have to insert the code: ")
						.color(ChatColor.AQUA)
						.then(cod)
							.color(ChatColor.GOLD)
							.style(ChatColor.BOLD)
						.then(".")
							.color(ChatColor.AQUA)
						.then(" If you didn't registered, ")
							.color(ChatColor.RED)
						.then("click here")
							.color(ChatColor.GOLD)
							.style(ChatColor.BOLD)
							.tooltip("ATTENTION! You should not play with this. Read carefully")
							.runCommand("/cw oopsydoopsy "+uuid+" "+cod)
							.send(p);
	    			}
	    		}
	    	}
	    }
	    
    	if(cmd.equals("execcmd")){
	    	String ecmd = args[0];
	    	if(!ecmd.contains("@nxt@")){
	    	ecmd=ecmd.replace("@3m@", " ");
	    	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ecmd);
	    	return "succes";
	    	}else{
	    		ecmd=ecmd.replace("@3m@", " ");
	    		ecmd = ecmd.replace("@nxt@", "@");
	    		for(String cm : ecmd.split("@")){
	    	    	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cm);
	    		}
	    	}
    	}
	    
	    if(Alpha.lobby){
	    if(cmd.startsWith("getOUser")){
	    	String by = args[0];
	    	String serverName = args[1];
	    	String obj = args[2];
	    	if(BY.UUID.toString().equalsIgnoreCase(by)){
	    		return UseriConfig.getUserByUUID(obj, true).toString();
	    	}
	    	if(BY.NAME.toString().equalsIgnoreCase(by)){
	    		return UseriConfig.getUserByNume(obj, true).toString();
	    	}
	    	if(BY.GUUID.toString().equalsIgnoreCase(by)){
	    		return UseriConfig.getUserByGUUID(obj).toString();
	    	}
	    }
	    if(cmd.equals("set")){
	    	if(args.length==4){
	    	args[3]=args[3].replace("@dp@", ":");
	    	if(args[0].equals("nume")){
	    		String serverName = args[1];
	    		String uuid = args[2];
	    		String newPlayerName = args[3];
	    		
	    		if(UseriConfig.exists(uuid)){
	    		PConfig pconf = ConfigUtil.getUUIDPlayerFile(uuid);
	    		FileConfiguration user = pconf.getPlayer();
	    		user.set("nume", newPlayerName);
	    		pconf.save();
	    		MPlayeri.updateContName(uuid, newPlayerName);
	    		return "succes";
	    		}else{
	    			return "errPlayer not found";
	    		}
	    	}
	    	if(args[0].equals("parola")){
	    		String serverName = args[1];
	    		String uuid = args[2];
	    		String newPlayerPass = args[3];
	    		
	    		if(UseriConfig.exists(uuid)){
	    		PConfig pconf = ConfigUtil.getUUIDPlayerFile(uuid);
	    		FileConfiguration user = pconf.getPlayer();
	    		user.set("par", CCipher.encrypt(DMaine.key, DMaine.iv, newPlayerPass));
	    		pconf.save();
	    		MPlayeri.updateContPar(uuid, newPlayerPass);
	    		return "succes";
	    		}else{
	    			return "errPlayer not found";
	    		}
	    	}
	    	if(args[0].equals("guuid")){
	    		String serverName = args[1];
	    		String uuid = args[2];
	    		String newPlayerGUUID = args[3];
	    		
	    		if(UseriConfig.exists(uuid)){
	    		PConfig pconf = ConfigUtil.getUUIDPlayerFile(uuid);
	    		FileConfiguration user = pconf.getPlayer();
	    		user.set("guuid", newPlayerGUUID);
	    		pconf.save();
	    		return "succes";
	    		}else{
	    			return "errPlayer not found";
	    		}
	    	}
	    	if(args[0].equals("coins")){
	    		String serverName = args[1];
	    		String uuid = args[2];
	    		String newPlayerCoins = args[3];
	    		
	    		if(UseriConfig.exists(uuid)){
	    		PConfig pconf = ConfigUtil.getUUIDPlayerFile(uuid);
	    		FileConfiguration user = pconf.getPlayer();
	    		user.set("coins", newPlayerCoins);
	    		pconf.save();
	    		MPlayeri.updateCont(uuid, newPlayerCoins,user.getString("pcoins"));
	    		return "succes";
	    		}else{
	    			return "errPlayer not found";
	    		}
	    	}
	    	if(args[0].equals("pcoins")){
	    		String serverName = args[1];
	    		String uuid = args[2];
	    		String newPlayerPCoins = args[3];
	    		
	    		if(UseriConfig.exists(uuid)){
	    		PConfig pconf = ConfigUtil.getUUIDPlayerFile(uuid);
	    		FileConfiguration user = pconf.getPlayer();
	    		user.set("pcoins", newPlayerPCoins);
	    		pconf.save();
	    		MPlayeri.updateCont(uuid, user.getString("coins"),newPlayerPCoins);
	    		return "succes";
	    		}else{
	    			return "errPlayer not found";
	    		}
	    	}
	    	if(args[0].equals("coins&pcoins")){
	    		String serverName = args[1];
	    		String uuid = args[2];
	    		String newPlayerCoins = args[3];
	    		String newPlayerPCoins = args[4];
	    		
	    		if(UseriConfig.exists(uuid)){
	    		PConfig pconf = ConfigUtil.getUUIDPlayerFile(uuid);
	    		FileConfiguration user = pconf.getPlayer();
	    		user.set("coins", newPlayerCoins);
	    		user.set("pcoins", newPlayerPCoins);
	    		pconf.save();
	    		MPlayeri.updateCont(uuid, newPlayerCoins,newPlayerPCoins);
	    		return "succes";
	    		}else{
	    			return "errPlayer not found";
	    		}
	    	}
	    	if(args[0].equals("ban")){
	    		String serverName = args[1];
	    		String uuid = args[2];
	    		try{
	    		boolean ban = Boolean.getBoolean(args[3]);
	    		if(UseriConfig.exists(uuid)){
	    		PConfig pconf = ConfigUtil.getUUIDPlayerFile(uuid);
	    		FileConfiguration user = pconf.getPlayer();
	    		user.set("ban", ban);
	    		pconf.save();
	    		int iban = 0;
	    		if(ban){
	    			iban=1;
	    		}
	    		MPlayeri.updateCont(uuid, user.getString("coins"),user.getString("pcoins"),iban);
	    		return "succes";
	    		}else{
	    			return "errPlayer not found";
	    		}
	    		}catch(Exception e){
	    			return "err"+e.getLocalizedMessage();
	    		}
	    	}
	    }
	    }
	    }
	    return "errUnexpected error";
	}
}
