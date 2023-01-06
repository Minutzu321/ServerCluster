package me.minutz.dream.config;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IpDat {
	
	private String ip;
	private Date data;
	private DateFormat df = new SimpleDateFormat("MM/dd/yyyy,HH:mm:ss");
	private int i;
	
	public IpDat(String ip, String d, int i){
		this.i = i;
		this.ip = ip;
		try {
			this.data = df.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public IpDat(String ip, Date d, int i){
		this.i = i;
		this.ip = ip;
		this.data = d;
	}
	
	public String getIp() {
		return ip;
	}
	public Date getData() {
		return data;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String toString(){
		return ip+"-"+df.format(data)+"-"+i;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	
	public static IpDat fromString(String s){
		if(s.contains("-")){
		String[] h = s.split("-");
		return new IpDat(h[0],h[1],Integer.parseInt(h[2]));
		}else{
			return new IpDat(s,(Date)null,0);
		}
	}
}
