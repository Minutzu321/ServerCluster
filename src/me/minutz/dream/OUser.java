package me.minutz.dream;

import java.util.ArrayList;
import java.util.List;

public class OUser {
	private String nume,uuid;
	private double coins,pcoins;
	private boolean simple;
	
	public OUser(String nume, String uuid, double coins, double pcoins, boolean simple) {
		this.nume = nume;
		this.uuid = uuid;
		this.coins = coins;
		this.pcoins = pcoins;
		this.simple = simple;
	}
	
	public String getNume() {
		return nume;
	}
	public void setNume(String nume) {
		this.nume = nume;
	}
	public String getUUID() {
		return uuid;
	}
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
	public double getCoins() {
		return coins;
	}
	public void setCoins(double coins) {
		this.coins = coins;
	}
	public double getPCoins() {
		return pcoins;
	}
	public void setPCoins(double pcoins) {
		this.pcoins = pcoins;
	}
	public boolean isSimple() {
		return simple;
	}
	public void setSimple(boolean simple) {
		this.simple = simple;
	}
	
	
	
	private String preg(String s){
		return s.replace("-", "@lin@");
	}
	private static String repreg(String s){
		return s.replace("@lin@", "-");
	}
	public String toString(){
		String s = "";
		List<String> sl = new ArrayList<String>();
		sl.add(nume);
		sl.add(uuid);
		sl.add(Double.toString(coins));
		sl.add(Double.toString(pcoins));
		for(int i = 0;i<sl.size();i++){
			if(i!=sl.size()-1){
				s=s+preg(sl.get(i))+"-";
			}else{
				s=s+preg(sl.get(i));
			}
		}
		return s;
	}
	public static OUser fromString(String s){
		try{
			String[] sp = s.split("-");
			String nume = repreg(sp[0]);
			String uuid = repreg(sp[1]);
			Double coins = Double.parseDouble(sp[2]);
			Double pcoins = Double.parseDouble(sp[3]);
			return new OUser(nume,uuid,coins,pcoins,true);
		}catch(Exception e){
		}
		return null;
	}
}
