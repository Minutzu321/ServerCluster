package me.minutz.dream;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.minutz.dream.Alpha.BY;
import me.minutz.dream.util.JSONMessage;

public class API {
	
	public static boolean isRegistered(BY by, String obj){
		if(Alpha.getOUser(by, obj)!=null){
			return true;
		}
		return false;
	}

	//COINS
	public static void addCoins(String name,double n){
		addCoins(BY.NAME,name,n);
	}
	public static void remCoins(String name,double n){
		remCoins(BY.NAME,name,n);
	}
	public static void setCoins(String name,double n){
		setCoins(BY.NAME,name,n);
	}
	public static double getCoins(String name){
		return getCoins(BY.NAME,name);
	}
	
	public static void addCoins(BY by, String obj,double n){
		OUser u = Alpha.getOUser(by, obj);
		if(u!=null){
		double f = u.getCoins()+n;
		if(f<0){
			f=0;
		}
		Alpha.set("coins", u.getUUID(), Double.toString(f));
		}else{
			Player p = null;
			if(by == BY.NAME){
				p = Bukkit.getPlayer(obj);
			}else{
				if(by == BY.GUUID){
				p = Bukkit.getPlayer(UUID.fromString(obj));
				}
			}
			
			if(p!=null){
				JSONMessage.create("You are not registered! To receive Coins you need to register: ")
				.color(ChatColor.RED)
				.then("==========")
					.color(ChatColor.GOLD)
					.style(ChatColor.BOLD)
					.tooltip("Click here to register")
					.openURL("============")
				.then("!")
					.color(ChatColor.RED)
				.send(p);
			}
		}
	}
	
	public static void remCoins(BY by, String obj,double n){
		OUser u = Alpha.getOUser(by, obj);
		if(u!=null){
		double f = u.getCoins()-n;
		if(f<0){
			f=0;
		}
		Alpha.set("coins", u.getUUID(), Double.toString(f));
		}else{
			Player p = null;
			if(by == BY.NAME){
				p = Bukkit.getPlayer(obj);
			}else{
				if(by == BY.GUUID){
				p = Bukkit.getPlayer(UUID.fromString(obj));
				}
			}
			if(p!=null){
				JSONMessage.create("You are not registered! To receive Coins you need to register: ")
				.color(ChatColor.RED)
				.then("===================")
					.color(ChatColor.GOLD)
					.style(ChatColor.BOLD)
					.tooltip("Click here to register")
					.openURL("===========")
				.then("!")
					.color(ChatColor.RED)
				.send(p);
			}
		}
	}
	
	public static void setCoins(BY by, String obj,double f){
		OUser u = Alpha.getOUser(by, obj);
		if(u!=null){
			if(f<0){
				f=0;
			}
			Alpha.set("coins", u.getUUID(), Double.toString(f));
		}else{
			Player p = null;
			if(by == BY.NAME){
				p = Bukkit.getPlayer(obj);
			}else{
				if(by == BY.GUUID){
				p = Bukkit.getPlayer(UUID.fromString(obj));
				}
			}
			if(p!=null){
				JSONMessage.create("You are not registered! To receive Coins you need to register: ")
				.color(ChatColor.RED)
				.then("================")
					.color(ChatColor.GOLD)
					.style(ChatColor.BOLD)
					.tooltip("Click here to register")
					.openURL("================")
				.then("!")
					.color(ChatColor.RED)
				.send(p);
			}
		}
	}
	
	public static double getCoins(BY by, String obj){
		OUser u = Alpha.getOUser(by, obj);
		if(u!=null){
		return u.getCoins();
		}
		return 0;
	}
	
	
	//PCOINS
	public static void addPCoins(String name,double n){
		addPCoins(BY.NAME,name,n);
	}
	public static void remPCoins(String name,double n){
		remPCoins(BY.NAME,name,n);
	}
	public static void setPCoins(String name,double n){
		setPCoins(BY.NAME,name,n);
	}
	public static double getPCoins(String name){
		return getPCoins(BY.NAME,name);
	}
	
	public static void addPCoins(BY by, String obj,double n){
		OUser u = Alpha.getOUser(by, obj);
		if(u!=null){
		double f = u.getPCoins()+n;
		if(f<0){
			f=0;
		}
		Alpha.set("pcoins", u.getUUID(), Double.toString(f));
		}else{
			Player p = null;
			if(by == BY.NAME){
				p = Bukkit.getPlayer(obj);
			}else{
				if(by == BY.GUUID){
				p = Bukkit.getPlayer(UUID.fromString(obj));
				}
			}
			if(p!=null){
				JSONMessage.create("You are not registered! To receive Coins you need to register: ")
				.color(ChatColor.RED)
				.then("=================")
					.color(ChatColor.GOLD)
					.style(ChatColor.BOLD)
					.tooltip("Click here to register")
					.openURL("=====================")
				.then("!")
					.color(ChatColor.RED)
				.send(p);
			}
		}
	}
	
	public static void remPCoins(BY by, String obj,double n){
		OUser u = Alpha.getOUser(by, obj);
		if(u!=null){
		double f = u.getPCoins()-n;
		if(f<0){
			f=0;
		}
		Alpha.set("pcoins", u.getUUID(), Double.toString(f));
		}else{
			Player p = null;
			if(by == BY.NAME){
				p = Bukkit.getPlayer(obj);
			}else{
				if(by == BY.GUUID){
				p = Bukkit.getPlayer(UUID.fromString(obj));
				}
			}
			if(p!=null){
				JSONMessage.create("You are not registered! To receive Coins you need to register: ")
				.color(ChatColor.RED)
				.then("============")
					.color(ChatColor.GOLD)
					.style(ChatColor.BOLD)
					.tooltip("Click here to register")
					.openURL("=======================")
				.then("!")
					.color(ChatColor.RED)
				.send(p);
			}
		}
	}
	
	public static void setPCoins(BY by, String obj,double f){
		OUser u = Alpha.getOUser(by, obj);
		if(u!=null){
			if(f<0){
				f=0;
			}
			Alpha.set("pcoins", u.getUUID(), Double.toString(f));
		}else{
			Player p = null;
			if(by == BY.NAME){
				p = Bukkit.getPlayer(obj);
			}else{
				if(by == BY.GUUID){
				p = Bukkit.getPlayer(UUID.fromString(obj));
				}
			}
			if(p!=null){
				JSONMessage.create("You are not registered! To receive Coins you need to register: ")
				.color(ChatColor.RED)
				.then("===================")
					.color(ChatColor.GOLD)
					.style(ChatColor.BOLD)
					.tooltip("Click here to register")
					.openURL("====================")
				.then("!")
					.color(ChatColor.RED)
				.send(p);
			}
		}
	}
	
	public static double getPCoins(BY by, String obj){
		OUser u = Alpha.getOUser(by, obj);
		if(u!=null){
		return u.getPCoins();
		}
		return 0;
	}
	
}
