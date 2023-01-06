package me.minutz.dream.hive;

import me.minutz.dream.DMaine;

public class HServerRun implements Runnable{

	@Override
	public void run() {
		if(DMaine.hserver!=null){
			if(!DMaine.hserver.stop){
			DMaine.hserver.run();
			}
		}
	}

}
