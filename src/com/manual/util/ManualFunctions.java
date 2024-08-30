package com.manual.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

public class ManualFunctions 
{
	public static String Preview(String selected_sports,String scene, PrintWriter print_writer) throws InterruptedException
    {
		scene = scene.replace(".sum", "");
		switch(selected_sports.toUpperCase()) {
			case "BADMINTON":
				if(scene.equalsIgnoreCase("Bug_OneLine")||
						scene.equalsIgnoreCase("Bug_TwoLine")) {
					print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*TrumpLoop START;");
				} 
				break;
		}
		print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
		print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In STOP;");
		print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out STOP;");
		
		if (scene.contains("Bug")||scene.contains("LT")) {
			print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 110.0;");
		}else {
			print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 144.0;");
		}

		print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
		print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
		print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
		TimeUnit.SECONDS.sleep(1);
		print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
		print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
		print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");	
		
        return "";
    }
	public static String checkConnection(String ip, int port, int timeout) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port), timeout);
            return "Connected";
        } catch (SocketTimeoutException e) {
            return "Disconnected";
        } catch (IOException e) {
            return "Disconnected";
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
}