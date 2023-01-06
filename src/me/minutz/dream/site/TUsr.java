package me.minutz.dream.site;

import me.minutz.dream.User;

public class TUsr{
	private User u;
	private String cod,ip,serverPort;
	
	public TUsr(User u, String cod, String ip, String serverPort) {
		this.u = u;
		this.cod = cod;
		this.ip = ip;
		this.serverPort = serverPort;
	}
	public User getU() {
		return u;
	}
	public void setU(User u) {
		this.u = u;
	}
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
	public String getIP() {
		return ip;
	}
	public void setIP(String ip) {
		this.ip = ip;
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	
}