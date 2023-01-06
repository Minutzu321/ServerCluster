package me.minutz.dream.hive;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import me.minutz.dream.Alpha;
import me.minutz.dream.config.ConfigUtil;

public class HServer {
	ServerSocket serverSocket;
	boolean stop=false;
	public HServer(){
        int port = 25000;
        if(Alpha.lobby){
        	port=Alpha.lobbyport;
        }else{
        	port=ConfigUtil.config.getInt("serverPort");
        }
        try {
			serverSocket = new ServerSocket(port);
			System.out.println("!HServer pornit pe portul "+port);
		} catch (IOException e) {
			System.out.println("!HServer-ul nu a putut porni pe portul "+port+". Incearca altul.");
			e.printStackTrace();
		}
	}
	
	public void stop(){
		stop=true;
	    if (serverSocket != null && !serverSocket.isClosed()) {
	        try {
	            serverSocket.close();
	        } catch (IOException e)
	        {
	            e.printStackTrace(System.err);
	        }
	    }
	}
	
	public void run(){
		if(!stop){
		Socket socket = null;
	    if (serverSocket != null && !serverSocket.isClosed()) {
		try{
        socket = serverSocket.accept();
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String cmd = br.readLine();
        
        String returnMessage = null;
        if(cmd.startsWith("----------")){
        returnMessage = HCmd.getResp(cmd.substring(12));
        }else{
        	returnMessage="--------------";
        }

        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(returnMessage);
        bw.flush();
    }catch (Exception e){}
finally
{
    try
    {
        socket.close();
    }
    catch(Exception e){}
}
}
		}
}
}

