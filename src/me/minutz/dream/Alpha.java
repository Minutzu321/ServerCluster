package me.minutz.dream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import me.minutz.dream.Alpha.BY;
import me.minutz.dream.config.ConfigUtil;
import me.minutz.dream.config.UseriConfig;
import me.minutz.dream.hive.HCmd;
import me.minutz.dream.hive.HResp;
import me.minutz.dream.hive.HResp.Status;
import me.minutz.dream.site.SCmd;
import me.minutz.dream.site.ServerPort;

public class Alpha {
	public static String Snume = ConfigUtil.config.getString("serverName");
	public static boolean lobby = ConfigUtil.config.getBoolean("lobby");
	public static int lobbyport = ConfigUtil.config.getInt("lobbyPort");
	
	public enum BY{
		UUID,
		NAME,
		GUUID
	}
	public static void oopsyDoopsy(String cmd){
		sendToLobby("execcmd:"+cmd);
	}
	
	public static String online(String nume){
		String lr = HCmd.getResp("isonline:"+nume);
		if(lr.startsWith("da")){
			return "t:"+lr.split(":")[1];
		}else{
	    	List<String> ar = ConfigUtil.config.getStringList("siteServers-PortsList");
	    	for(String sp : ar){
	    		ServerPort spl = ServerPort.fromString(sp);
	    			String sr = SCmd.sendToServer("isonline:"+nume,spl.getPort()).getMsg();
	    			if(sr.startsWith("da")){
	    				return Integer.toString(spl.getPort())+":"+sr.split(":")[1];
	    			}
	    	}
		}
		return "nu";
	}
	
	public static OUser getOUser(BY by,String obj){
		if(lobby){
	    	if(by == BY.UUID){
	    		return UseriConfig.getUserByUUID(obj, true);
	    	}
	    	if(by == BY.NAME){
	    		return UseriConfig.getUserByNume(obj, true);
	    	}
	    	if(by == BY.GUUID){
	    		return UseriConfig.getUserByGUUID(obj);
	    	}
	    	return null;
		}else{
			HResp hr = sendToLobby("getOUser:"+by.toString().toLowerCase()+":"+Snume.toLowerCase()+":"+obj);
			if(hr.getStatus()==Status.SUCCES){
			return OUser.fromString(hr.getMsg());
			}else{
				return null;
			}
		}
	}
	
	public static HResp set(String cmd,String uuid,String obj){
		HResp raspuns =HResp.fromString("errUnexpected error");
		obj=obj.replace(":", "@dp@");
		if(lobby){
			raspuns = HResp.fromString(HCmd.getResp("set:"+cmd+":"+Snume.toLowerCase()+":"+uuid+":"+obj));
		}else{
			raspuns = sendToLobby("set:"+cmd+":"+Snume.toLowerCase()+":"+uuid+":"+obj);
		}
		return raspuns;
	}
	
	public static HResp sendToLobby(String cmd){
		String raspuns = "";
		
		Socket socket = null;
        try
        {
            String host = "localhost";
            int port = lobbyport;
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
 
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
 
            String sendMessage = "password"+cmd + "\n";
            sendMessage=sendMessage.replace(" ", "@3m@");
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
