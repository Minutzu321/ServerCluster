package me.minutz.dream.site;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import me.minutz.dream.DMaine;
import me.minutz.dream.config.ConfigUtil;
import upnp.UPnP;

public class SiteConn{
	ServerSocket serverSocket;
	boolean stop = false;
	private int pn = ConfigUtil.config.getInt("siteServerPort");
	private void strt(){
        try {
        	UPnP.openPortTCP(pn);
			serverSocket = new ServerSocket(pn);
			System.out.println("!Serverul "+ConfigUtil.config.getString("serverName")+" deschis pe "+pn);
		} catch (IOException e) {
			System.out.println("!Serverul nu a putut porni pe "+pn);
		}
	}
	public SiteConn(){
		strt();
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
	
	public void run() {
		if(!stop){
		try {
			if(serverSocket != null && !serverSocket.isClosed()){
				if(DMaine.debug) {
					System.out.println("Se citeste socketul...");
				}
			magie(serverSocket.accept());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	}
	
	public void magie(Socket sock){
		OutputStream os = null;
		try {
			os = sock.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(os, true);
        BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
        String inputLine;
  
        try {
			if ((inputLine = in.readLine()) != null) {
				if(inputLine.startsWith("-----------")){
			inputLine=inputLine.replace("----------------", "");
			if(DMaine.debug) {
				System.out.println(inputLine);
			}
			String jj = SCmd.getResp(inputLine);
			pw.println(jj);
				}else{
					pw.println(UUID.randomUUID().toString().replace("-", "")+"-"+UUID.randomUUID().toString().replace("-", ""));
				}
			pw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        try {
			in.close();
			pw.close();
			os.close();
			sock.close();
			if(DMaine.debug) {
				System.out.println("S-a inchis socketul");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        
	}

}
