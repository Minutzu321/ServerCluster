package me.minutz.dream;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.minutz.dream.Alpha.BY;
import me.minutz.dream.coinscrate.CoinsCrate;
import me.minutz.dream.config.ConfigUtil;
import me.minutz.dream.config.PConfig;
import me.minutz.dream.config.UseriConfig;
import me.minutz.dream.hive.HCmd;
import me.minutz.dream.hive.HResp;
import me.minutz.dream.hive.HServer;
import me.minutz.dream.hive.HServerRun;
import me.minutz.dream.mysql.MPlayeri;
import me.minutz.dream.mysql.MySQL;
import me.minutz.dream.papi.PH;
import me.minutz.dream.site.SCmd;
import me.minutz.dream.site.ServerPort;
import me.minutz.dream.site.SiteConn;
import me.minutz.dream.site.SiteRun;
import me.minutz.dream.site.TUsr;
import me.minutz.dream.util.JSONMessage;

public class DMaine extends JavaPlugin implements Listener{
	
	public static DMaine main;
	public static SiteConn conn;
	public static HServer hserver;
	public static MySQL mysql;
	public static boolean debug=true;
	
	public static String iv = "---------------",key="------------------------";
	
	public void onEnable(){
		main=this;
		
		ConfigUtil.load();
		
		hserver=new HServer();
		ScheduledExecutorService scheduler1 = Executors.newScheduledThreadPool(1);
		scheduler1.scheduleAtFixedRate(new HServerRun(), 1, 1, TimeUnit.MILLISECONDS);
		if(Alpha.lobby){		
		new Thread(){
			public void run(){
				
		mysql = new MySQL("-------------");
		mysql.Connect(ConfigUtil.config.getString("mysql.host"), ConfigUtil.config.getString("mysql.dbname"), ConfigUtil.config.getString("mysql.user"), ConfigUtil.config.getString("mysql.password"));
		MPlayeri.load();
		
		conn = new SiteConn();
		
		ScheduledExecutorService scheduler6 = Executors.newScheduledThreadPool(1);
		scheduler6.scheduleAtFixedRate(new SiteRun(), 1, 1, TimeUnit.MILLISECONDS);
			}
		}.run();
        new BukkitRunnable(){
        	String pref = "[ --------------- ] ";
			@Override
			public void run() {
		    	List<String> ar = ConfigUtil.config.getStringList("siteServers-PortsList");
		    	for(String sp : ar){
		    		ServerPort spl = ServerPort.fromString(sp);
		    		if(spl.getServername().toLowerCase().equals("exemplu")) {
		    			return;
		    		}
		    		try{
		    			HResp resp = SCmd.sendToServer("ping",spl.getPort());
		    			if(resp!=null){
		    				if(resp.getStatus()==HResp.Status.SUCCES){
		    					System.out.println(pref+"HServer "+spl.getServername()+" connected!");
		    				}else{
		    					System.out.println(pref+"HServer "+spl.getServername()+" couldn't connect! (Errcode:2)");
		    				}
		    			}
		    		}catch(Exception e){
		    			System.out.println(pref+"HServer "+spl.getServername()+" couldn't connect! (Errcode:1)");
		    			e.printStackTrace();
		    		}
		    	}
			}
        	
        }.runTaskLater(main, 20L * 10);
//        CoinsCrate cc = new CoinsCrate();
//        cc.enable();
		}
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            new PH(this).hook();
        }
        getServer().getPluginManager().registerEvents(this, this);
	}
	
	 @EventHandler(priority=EventPriority.HIGH)
	 public void onJoin(PlayerJoinEvent event)
	  {	
		 Player p = event.getPlayer();
		 if(Alpha.lobby){
		  User gu = (User) UseriConfig.getUserByGUUID(p.getUniqueId().toString());
		  if(gu==null){
		  OUser u = UseriConfig.getUserByNume(p.getName(), true);
		  if(u!=null){
			  PConfig pc = ConfigUtil.getUUIDPlayerFile(u.getUUID());
			  String inFileGUUID = pc.getPlayer().getString("guuid");
			  String GUUID = p.getUniqueId().toString();
			  if(!inFileGUUID.equals(GUUID)){
				  pc.getPlayer().set("guuid", GUUID);
				  pc.save();
			  }
		  }else{
					JSONMessage.create("You are not registered! To receive Coins you need to register: ")
					.color(ChatColor.RED)
					.then("------------")
						.color(ChatColor.GOLD)
						.style(ChatColor.BOLD)
						.tooltip("Click here to register")
						.openURL("---------------------")
					.then("!")
						.color(ChatColor.RED)
					.send(p);
			}
		 }else{
			 if(!gu.getNume().equalsIgnoreCase(p.getName())){
				 PConfig pc = gu.getPconf();
				  pc.getPlayer().set("nume", p.getName());
				  pc.save();
				  MPlayeri.updateContName(gu.getUUID(), p.getName());
				 p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bName changed from &c"+gu.getNume()+"&b to &c"+p.getName()));
			 }
		 }
		 }else{
			  OUser u = UseriConfig.getUserByNume(p.getName(), true);
			  if(u==null){
  				JSONMessage.create("You are not registered! To receive Coins you need to register: ")
					.color(ChatColor.RED)
					.then("-----------")
						.color(ChatColor.GOLD)
						.style(ChatColor.BOLD)
						.tooltip("Click here to register")
						.openURL("------------")
					.then("!")
						.color(ChatColor.RED)
					.send(p);;
			  }
		 }
	  }
	
	  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	  {
	    String cmd = command.getName();
	    //PLAYER
	    if ((cmd.equalsIgnoreCase("cw")) && ((sender instanceof Player)))
	    {
	        Player p = (Player)sender;
	        if(args.length==3){
	        	if(args[0].equals("oopsydoopsy")){
	        		String uuid = args[1];
	        		String cod = args[2];
	        		if(Alpha.lobby){
	        			for(TUsr usr : SCmd.t){
	        				if(usr.getCod().equals(cod)){
	        					if(usr.getU().getUUID().equals(uuid)){
	        					SCmd.t.remove(usr);
	        					return false;
	        					}
	        				}
	        			}
	        		}else{
	        			Alpha.oopsyDoopsy("cw oopsydoopsy "+uuid+" "+cod);
	        		}
	        	}
	        }
	        
    	    	if(args.length==0){
    	    		OUser u = UseriConfig.getUserByNume(p.getName(),true);
    	    		if(u!=null){
    	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Your coins: &e&l"+Double.toString(u.getCoins())));
    	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Your paid coins: &e&l"+Double.toString(u.getPCoins())));
    	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Your total coins: &e&l"+Double.toString((u.getCoins()+u.getPCoins()))));
    	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bAvailable commands:"));
    	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/cw stats <player>"));
    	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/cw changepass <newPassword> <newPasswordAgain>"));
    	    		if(p.isOp()){
        	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/cw coins set/add/rem <player> <amount>"));
    	    		}
    	    		}else{
        	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Your coins: &e&l0"));
        	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Your paid coins: &e&l0"));
        	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Your total coins: &e&l0"));
        	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bAvailable commands:"));
        	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/cw stats <player>"));
        	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/cw changepass <newPassword> <newPasswordAgain>"));
        	    		if(p.isOp()){
            	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/cw coins set/add/rem <player> <amount>"));
        	    		}
						JSONMessage.create("You are not registered on our site.")
						.color(ChatColor.GOLD)
						.then(" CLICK HERE ")
						.color(ChatColor.AQUA)
						.style(ChatColor.BOLD)
						.openURL("----------------")
						.then("to register and manage your account from web!")
						.color(ChatColor.GOLD)
						.send(p);;
    	    		}
    	    	}else{
    	    		if(args.length>=1){
    	    			if(args[0].equalsIgnoreCase("stats")){
    	    				if(args.length==2){
    	    					OUser ou = UseriConfig.getUserByNume(args[1], true);
    	    					if(ou!=null){
    	    	    	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6"+args[1]+"'s coins: &e&l"+Double.toString(ou.getCoins())));
    	    	    	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6"+args[1]+"'s paid coins: &e&l"+Double.toString(ou.getPCoins())));
    	    	    	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6"+args[1]+"'s total coins: &e&l"+Double.toString((ou.getCoins()+ou.getPCoins()))));
    	    					}else{
    	    						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlayer not found!"));
    	    					}
    	    				}else{
    	        	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cIncorrect format!"));
    	        	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bType /cw stats <player>"));
    	    				}
    	    			}
    	    			if(args[0].equalsIgnoreCase("changepass")||args[0].equalsIgnoreCase("changepassword")||args[0].equalsIgnoreCase("cp")){
    	    				if(args.length==3){
    	    					OUser u = UseriConfig.getUserByNume(p.getName(),true);
    	    					if(u!=null){
    	    						if(args[1].equals(args[2])){
    	    							Alpha.set("parola", u.getUUID(), args[1]);
    	    						}else{
    	    							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPasswords don't match. Try again."));
    	    						}
    	    					}else{
    	    						JSONMessage.create("You are not registered on our site.")
    	    									.color(ChatColor.GOLD)
    	    									.then(" CLICK HERE ")
    	    									.color(ChatColor.AQUA)
    	    									.style(ChatColor.BOLD)
    	    									.openURL("-----------------")
    	    									.then("to register and manage your account from web!")
    	    									.color(ChatColor.GOLD)
    	    									.send(p);;
    	    					}
    	    				}else{
    	        	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cIncorrect format!"));
    	        	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bType /cw changepass <newPassword> <newPasswordAgain>"));
    	    				}
    	    			}else{
    	    				if(!p.isOp()){
    	    				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCommand not found!"));
    	    	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bAvailable commands:"));
    	    	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/cw changepass <newPassword> <newPasswordAgain>"));
    	    				}
    	    			}
    	    		}
    	    	}
	        
	        if(p.isOp()){
	            	if(args.length>0){
	            		if(args[0].equalsIgnoreCase("update")){
	            			if(args.length==2){
	            				if(Alpha.lobby){
	            				HCmd.getResp("update:0:"+parseURL(args[1]));
	            				}else{
	            					Alpha.sendToLobby("update:"+Integer.toString(ConfigUtil.config.getInt("serverPort"))+":"+parseURL(args[1]));
	            				}
	            				update(args[1]);
	            			}
	            		}
	            		if(args.length==4){
	            		if(args[0].equalsIgnoreCase("coins")){
	            			if(args[1].equalsIgnoreCase("add")){
	            				OfflinePlayer t =Bukkit.getOfflinePlayer(args[2]);
	            				if(t!=null){
	            					OUser u = UseriConfig.getUserByNume(t.getName(),true);
	            					if(u!=null){
	            					try{
	            						double d = Double.parseDouble(args[3]);
	            						API.addCoins(BY.NAME,t.getName(), d);
	            						p.sendMessage("succes");
	            					}catch(Exception e){
	            						sender.sendMessage("Number not valid");	
	            					}
	            					}else{
	            						sender.sendMessage("Player not registered!");	
	            					}
	            				}else{
	            					sender.sendMessage("Player not found");
	            				}
	            			}else{
	            			if(args[1].equalsIgnoreCase("remove")||args[1].equalsIgnoreCase("rem")){
	            				OfflinePlayer t =Bukkit.getOfflinePlayer(args[2]);
	            				if(t!=null){
	            					OUser u = UseriConfig.getUserByNume(t.getName(),true);
	            					if(u!=null){
	            					try{
	            						double d = Double.parseDouble(args[3]);
	            						API.remCoins(BY.NAME,t.getName(), d);
	            						p.sendMessage("succes");
	            					}catch(Exception e){
	            						sender.sendMessage("Number not valid");	
	            					}
	            					}else{
	            						sender.sendMessage("Player not registered!");	
	            					}
	            				}else{
	            					sender.sendMessage("Player not found");
	            				}
	            			}else{
	            				if(args[1].equalsIgnoreCase("set")){
	                				OfflinePlayer t =Bukkit.getOfflinePlayer(args[2]);
	                				if(t!=null){
	                					OUser u = UseriConfig.getUserByNume(t.getName(),true);
	                					if(u!=null){
	                					try{
	                						double di = Double.parseDouble(args[3]);
	                						API.setCoins(BY.NAME,t.getName(), di);
	                						p.sendMessage("succes");
	                					}catch(Exception e){
	                						sender.sendMessage("Number not valid");	
	                					}
	                					}else{
	                						sender.sendMessage("Player not registered!");	
	                					}
	                				}else{
	                					sender.sendMessage("Player not found");
	                				}
	            				}else{
	            				sender.sendMessage("Incorect, scrie /cw coins add/set/rem [PLAYER] [NUMAR]");
	            				}
	            			}
	            			}
	            		}
	            	}
	    	    }
    
	    }
	    }
	    //CONSOLE
    	    if (cmd.equalsIgnoreCase("cw") && (sender instanceof ConsoleCommandSender))
    	    {
    	        if(args.length==3){
    	        	if(args[0].equals("oopsydoopsy")){
    	        		String uuid = args[1];
    	        		String cod = args[2];
    	        		if(Alpha.lobby){
    	        			for(TUsr usr : SCmd.t){
    	        				if(usr.getCod().equals(cod)){
    	        					if(usr.getU().getUUID().equals(uuid)){
    	        					SCmd.t.remove(usr);
    	        					break;
    	        					}
    	        				}
    	        			}
    	        		}else{
    	        			Alpha.oopsyDoopsy("cw oopsydoopsy "+uuid+" "+cod);
    	        		}
    	        	}
    	        }
            	if(args.length>0){
            		if(args[0].equalsIgnoreCase("update")){
            			if(args.length==2){
            				if(Alpha.lobby){
            				HCmd.getResp("update:0:"+parseURL(args[1]));
            				}else{
            					Alpha.sendToLobby("update:"+Integer.toString(ConfigUtil.config.getInt("serverPort"))+":"+parseURL(args[1]));
            				}
            				update(args[1]);
            			}
            		}
            		if(args.length==4){
            		if(args[0].equalsIgnoreCase("coins")){
            			if(args[1].equalsIgnoreCase("add")){
            				OfflinePlayer t =Bukkit.getOfflinePlayer(args[2]);
            				if(t!=null){
            					OUser u = UseriConfig.getUserByNume(t.getName(),true);
            					if(u!=null){
            					try{
            						double d = Double.parseDouble(args[3]);
            						API.addCoins(BY.NAME,t.getName(), d);
            					}catch(Exception e){
            						sender.sendMessage("Number not valid");	
            					}
            					}else{
            						sender.sendMessage("Player not registered!");	
            					}
            				}else{
            					sender.sendMessage("Player not found");
            				}
            			}else{
            			if(args[1].equalsIgnoreCase("remove")||args[1].equalsIgnoreCase("rem")){
            				OfflinePlayer t =Bukkit.getOfflinePlayer(args[2]);
            				if(t!=null){
            					OUser u = UseriConfig.getUserByNume(t.getName(),true);
            					if(u!=null){
            					try{
            						double d = Double.parseDouble(args[3]);
            						API.remCoins(BY.NAME,t.getName(), d);
            					}catch(Exception e){
            						sender.sendMessage("Number not valid");	
            					}
            					}else{
            						sender.sendMessage("Player not registered!");	
            					}
            				}else{
            					sender.sendMessage("Player not found");
            				}
            			}else{
            				if(args[1].equalsIgnoreCase("set")){
                				OfflinePlayer t =Bukkit.getOfflinePlayer(args[2]);
                				if(t!=null){
                					OUser u = UseriConfig.getUserByNume(t.getName(),true);
                					if(u!=null){
                					try{
                						double di = Double.parseDouble(args[3]);
                						API.setCoins(BY.NAME,t.getName(), di);
                					}catch(Exception e){
                						sender.sendMessage("Number not valid");	
                					}
                					}else{
                						sender.sendMessage("Player not registered!");	
                					}
                				}else{
                					sender.sendMessage("Player not found");
                				}
            				}else{
            				sender.sendMessage("Incorect, write /cw coins add/set/rem [PLAYER] [NUMBER]");
            				}
            			}
            			}
            		}
            		if(args[0].equalsIgnoreCase("pcoins")){
            			if(args[1].equalsIgnoreCase("add")){
            				OfflinePlayer t =Bukkit.getOfflinePlayer(args[2]);
            				if(t!=null){
            					OUser u = UseriConfig.getUserByNume(t.getName(),true);
            					if(u!=null){
            					try{
            						double d = Double.parseDouble(args[3]);
            						API.addPCoins(BY.NAME,t.getName(), d);
            					}catch(Exception e){
            						sender.sendMessage("Number not valid");	
            					}
            					}else{
            						sender.sendMessage("Player not registered!");	
            					}
            				}else{
            					sender.sendMessage("Player not found");
            				}
            			}else{
            			if(args[1].equalsIgnoreCase("remove")||args[1].equalsIgnoreCase("rem")){
            				OfflinePlayer t =Bukkit.getOfflinePlayer(args[2]);
            				if(t!=null){
            					OUser u = UseriConfig.getUserByNume(t.getName(),true);
            					if(u!=null){
            					try{
            						double d = Double.parseDouble(args[3]);
            						API.remPCoins(BY.NAME,t.getName(), d);
            					}catch(Exception e){
            						sender.sendMessage("Number not valid");	
            					}
            					}else{
            						sender.sendMessage("Player not registered!");	
            					}
            				}else{
            					sender.sendMessage("Player not found");
            				}
            			}else{
            				if(args[1].equalsIgnoreCase("set")){
                				OfflinePlayer t =Bukkit.getOfflinePlayer(args[2]);
                				if(t!=null){
                					OUser u = UseriConfig.getUserByNume(t.getName(),true);
                					if(u!=null){
                					try{
                						double di = Double.parseDouble(args[3]);
                						API.setPCoins(BY.NAME,t.getName(), di);
                					}catch(Exception e){
                						sender.sendMessage("Number not valid");	
                					}
                					}else{
                						sender.sendMessage("Player not registered!");	
                					}
                				}else{
                					sender.sendMessage("Player not found");
                				}
            				}else{
            				sender.sendMessage("Incorrect, wrie /cw pcoins add/set/rem [PLAYER] [NUMBER]");
            				}
            			}
            			}
            		}
            	}
            	}
    	    }
	    return false;
	  }
	  
	  private static String parseURL(String url){
		  return url.replace(":", "@dp@");
	  }
	  
	  public static void update(String url){
			Update u = new Update(url);
			if(u.isReady()){
			for(int i=0;i<20;i++){
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c&l!!! &r&lSERVER WILL RESTART IN 20 SECONDS &c&l!!!"));
			}
			new BukkitRunnable(){
				public void run(){
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
  				for(int i=0;i<20;i++){
      				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c&l!!! &r&lRESTARTING... &c&l!!!"));
      				}
				}
			}.runTaskLater(DMaine.getPlugin(), 20L*19);	            				
			new BukkitRunnable(){
				public void run(){
			u.externalUpdate();
				}
			}.runTaskLater(DMaine.getPlugin(), 20L*20);
			}
	  }
	
	public void onDisable(){
		try{
		hserver.stop();
		conn.stop();
		}catch(Exception e){
			
		}
	}
	
	public static DMaine getPlugin(){
		return main;
	}
}