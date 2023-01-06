package me.minutz.dream.hive;

public class HResp {
	private String msg;
	private Status status;
	
	public HResp(String msg, Status status) {
		this.msg = msg;
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}
	public Status getStatus() {
		return status;
	}

	public static HResp fromString(String s){
		if(s.startsWith("err")){
			return new HResp(s.substring(3),Status.ERROR);
		}
		return new HResp(s,Status.SUCCES);
	}


	public enum Status{
		ERROR,
		SUCCES;
	}
}
