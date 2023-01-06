package me.minutz.dream.site;

import me.minutz.dream.DMaine;

public class SiteRun implements Runnable{
	public void run(){
		if(DMaine.conn != null){
			if(!DMaine.conn.stop){
				while(!DMaine.conn.serverSocket.isClosed()){
					DMaine.conn.run();
				}
			}
		}
	}

}
