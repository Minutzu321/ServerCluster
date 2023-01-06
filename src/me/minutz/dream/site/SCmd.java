package me.minutz.dream.site;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import me.minutz.dream.Alpha;
import me.minutz.dream.User;
import me.minutz.dream.config.ConfigUtil;
import me.minutz.dream.config.IpDat;
import me.minutz.dream.config.PConfig;
import me.minutz.dream.config.UseriConfig;
import me.minutz.dream.hive.HCmd;
import me.minutz.dream.hive.HResp;
import me.minutz.dream.mysql.MPlayeri;

public class SCmd {
	
	public static List<TUsr> t= new ArrayList<TUsr>();
	
	public static String getResp(String s){
	    String cmd = s.split(":")[0];
	    String[] args = new String[s.split(":").length - 1];
	    for (int i = 1; i < s.split(":").length; i++) {
	      args[(i - 1)] = s.split(":")[i];
	    }
	    
//	    System.out.println(s);
	    if(cmd.equals("ping")){
	    	String ip = args[0];
	    	//TODO stiu eu
	    	return "pong";
	    }
	    
	    if(cmd.equals("reg")){
	    	String nume = args[0];
	    	String par = args[1];
	    	String ip = args[2];
	    	
	    	if(UseriConfig.getUserByNume(nume,true)!=null){
	    		return "Player already registered!";
	    	}else{
	    			String sr = Alpha.online(nume);
	    			if(!sr.equals("nu")){
	    				Random r = new Random();
	    				String cod = "";
	    				for(int i=0;i<8;i++){
	    					cod=cod+r.nextInt(10);
	    				}
	    				List<IpDat> ipd = new ArrayList<IpDat>();
	    				ipd.add(new IpDat(ip,Calendar.getInstance().getTime(),1));
	    	    		User u = new User(nume, UseriConfig.getRUUID(), par, ipd,false,0,0,sr.split(":")[1],null);
	    	    		for(TUsr tu:t){
	    	    			if(tu.getU().getNume().equals(u.getNume())){
	    	    				t.remove(tu);
	    	    				break;
	    	    			}
	    	    		}
	    	    		TUsr ll = new TUsr(u,cod,ip,sr.split(":")[0]);
	    	    		t.add(ll);
	    	    		if(ll.getServerPort().equals("t")){
	    	    			HCmd.getResp("sendcode:false:"+cod+":"+u.getUUID()+":"+nume);
	    	    		}else{
	    	    			int portf = Integer.parseInt(ll.getServerPort());
	    	    			sendToServer("sendcode:false:"+cod+":"+u.getUUID()+":"+nume, portf);
	    	    		}
	    				return "da:";
	    			}else{
	    				return "Player not found(You need to be online).";
	    			}

	    	}
	    }
	    
	    if(cmd.equals("checkcod")){
	    	String nume = args[0];
	    	String tcod = args[1];
	    	for(TUsr tu : t){
	    		if(tu.getU().getNume().equals(nume)){
	    			if(tu.getCod().equals(tcod)){
	    				UseriConfig.addUser(tu.getU());
	    				MPlayeri.addCont(tu.getU());
	    				t.remove(tu);
	    				return "da:"+tu.getU().getUUID();
	    			}else{
		    			String sr = Alpha.online(nume);
		    			if(!sr.equals("nu")){
    				Random r = new Random();
    				String cod = "";
    				for(int i=0;i<8;i++){
    					cod=cod+r.nextInt(10);
    				}
    				tu.setServerPort(sr.split(":")[0]);
    				tu.setCod(cod);
    	    		if(tu.getServerPort().equals("t")){
    	    			HCmd.getResp("sendcode:false:"+cod+":"+tu.getU().getUUID()+":"+nume);
    	    		}else{
    	    			int portf = Integer.parseInt(tu.getServerPort());
    	    			sendToServer("sendcode:false:"+cod+":"+tu.getU().getUUID()+":"+nume, portf);
    	    		}
	    			return "Incorrect. Another code was sent";
	    		}else{
	    			return "Player not found(You need to be online).";
	    		}
	    			}
	    		}
	    	}
	    	return "Error";
	    }
	    
	    if(cmd.equals("log")){
	    	String nume = args[0];
	    	String par = args[1];
	    	String ip = args[2];
	    	
	    	User f = (User) UseriConfig.getUserByNume(nume, false);
	    	if(f!=null){
	    		if(!f.isBanat()){
	    		if(f.getParola().equals(par)){
	    			IpDat i = null;
	    			for(IpDat idt:f.getIplist()){
	    				if(idt.getIp().equals(ip)){
	    					i=idt;
	    				}
	    			}
	    			IpDat nidt = null;
	    			if(i!=null){
	    			nidt = new IpDat(ip,Calendar.getInstance().getTime(),(i.getI()+1));
	    			}else{
	    				nidt = new IpDat(ip,Calendar.getInstance().getTime(),1);	
	    			}
	    			UseriConfig.addIpToUUID(f.getUUID(), nidt);
	    			return "da:"+f.getUUID();
	    		}else{
	    			return "Wrong password.";
	    		}
	    	}else{
	    		return f.getNume()+" is banned!";
	    	}
	    	}else{
	    		return "Player not found.";
	    	}
	    }
	    
	    if(cmd.equals("checkuuid")){
	    	String nume = args[0];
	    	String uuid = args[1];
	    	
	    	if(UseriConfig.exists(uuid)){
			PConfig pconf = ConfigUtil.getUUIDPlayerFile(uuid);
			FileConfiguration user = pconf.getPlayer();
	    	String f = (String) user.getString("nume");
	    	if(f!=null){
	    		if(f.equals(nume)){
	    			boolean b = user.getBoolean("ban"); 
	    			if(!b){
	    				Double c = Double.parseDouble(user.getString("coins"));
	    				Double pc = Double.parseDouble(user.getString("pcoins"));
	    		return "da:"+c+":"+pc;
	    			}else{
	    				return "ban";
	    			}
	    		}
	    	}
	    }
	    	return "nu";
	    }
	    if(cmd.equals("execcmd")){
//	    	if(args.length==1){
	    	String ecmd = args[0];
	    	if(!ecmd.contains("@nxt@")){
	    	ecmd=ecmd.replace("@3m@", " ");
	    	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ecmd);
	    	return "succes";
	    	}else{
	    		ecmd=ecmd.replace("@3m@", " ");
	    		ecmd = ecmd.replace("@nxt@", "@");
	    		for(String cm : ecmd.split("@")){
	    	    	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cm);
	    		}
	    	}
//	    	}else{
//		    	String ecmd = args[0];
//		    	String serverName = args[1];
//		    	List<String> ar = ConfigUtil.config.getStringList("siteServers-PortsList");
//		    	for(String sp : ar){
//		    		ServerPort spl = ServerPort.fromString(sp);
//		    		if(spl.getServername().equalsIgnoreCase(serverName)){
//		    			sendToServer("execcmd:"+ecmd,spl.getPort());
//		    			return "succes";
//		    		}
//		    	}
//	    	}
	    }
	    if(cmd.equals("uvisitors")){
			Document doc = null;
			try {
				doc = Jsoup.parse(new URL("---------------"), 10000);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Elements tds = doc.select("b");
			return tds.toArray()[11].toString();
	    }
		return "errhopa";
	}
	
	public static HResp sendToServer(String cmd,int port){
		String raspuns = "";
		
		Socket socket = null;
        try
        {
            String host = "localhost";
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
 
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
 
            String sendMessage = "pass"+cmd + "\n";
            bw.write(sendMessage);
            bw.flush();
 
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String message = br.readLine();
            return HResp.fromString(message);
        }
        catch (Exception e)
        {
            raspuns="err"+e.getLocalizedMessage();
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return HResp.fromString(raspuns);
	}
	

}