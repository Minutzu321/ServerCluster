package me.minutz.dream.papi;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import me.minutz.dream.DMaine;
import me.minutz.dream.OUser;
import me.minutz.dream.config.UseriConfig;

public class PH extends EZPlaceholderHook {

    private DMaine ourPlugin;

    public PH(DMaine ourPlugin) {
        super(ourPlugin, "cw");
        this.ourPlugin = ourPlugin;
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        
    	if (p == null) {
            return "";
        }
    	// placeholder: %cw_coins%
        if (identifier.equals("coins")) {
        	OUser u = UseriConfig.getUserByNume(p.getName(), true);
        	if(u!=null){
            return Double.toString(u.getCoins());
        	}
        }
     // placeholder: %cw_pcoins%
        if (identifier.equals("pcoins")) {
        	OUser u = UseriConfig.getUserByNume(p.getName(), true);
        	if(u!=null){
        		return Double.toString(u.getPCoins());
        	}
        }
     // placeholder: %cw_totalcoins%
        if (identifier.equals("totalcoins")) {
        	OUser u = UseriConfig.getUserByNume(p.getName(), true);
        	if(u!=null){
        		double total = u.getPCoins()+u.getCoins();
        		return Double.toString(total);
        	}
        }

        return "0";
    }
}