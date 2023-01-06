package me.minutz.dream.site;

public class ServerPort {
	private String servername;
	private int port;
	
	public ServerPort(String servername, int port) {
		this.servername = servername.toLowerCase();
		this.port = port;
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public String toString(){
		return servername+"-"+Integer.toString(port);
	}
	public static ServerPort fromString(String s){
		try{
			String sname = s.split("-")[0];
			int p = Integer.parseInt(s.split("-")[1]);
			return new ServerPort(sname,p);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
