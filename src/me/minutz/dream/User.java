package me.minutz.dream;

import java.util.List;

import me.minutz.dream.config.IpDat;
import me.minutz.dream.config.PConfig;

public class User extends OUser{
	private String parola,guuid;
	private List<IpDat> iplist;
	private boolean banat = false;
	private PConfig pconf;
	
	public User(String nume, String uuid, String parola, List<IpDat> iplist, boolean banat, double coins,double pcoins, String guuid, PConfig pconf) {
		super(nume,uuid,coins,pcoins,false);
		this.parola = parola;
		this.iplist = iplist;
		this.banat = banat;
		this.guuid = guuid;
		this.pconf = pconf;
	}
	
	public String getParola() {
		return parola;
	}
	public void setParola(String parola) {
		this.parola = parola;
	}
	public List<IpDat> getIplist() {
		return iplist;
	}
	public void setIplist(List<IpDat> iplist) {
		this.iplist = iplist;
	}
	public boolean isBanat() {
		return banat;
	}
	public void setBanat(boolean banat) {
		this.banat = banat;
	}
	public String getGUUID() {
		return guuid;
	}
	public void setGUUID(String guuid) {
		this.guuid = guuid;
	}
	public PConfig getPconf() {
		return pconf;
	}
	public void setPconf(PConfig pconf) {
		this.pconf = pconf;
	}
}
