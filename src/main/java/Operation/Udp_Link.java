package Operation;

import java.io.*;
import java.net.*;

import static servlet.StaticFile.nodeList;

public class Udp_Link extends Thread{
    private static int[]  time = {0, 0, 0, 0, 0, 0};
    public static int[] state = {0, 0, 0, 0, 0, 0};

    public void run(){
        try {
            DatagramSocket server = new DatagramSocket(5000);

            byte[] buf_receive;

            Thread t = new Thread(){
                public void run(){
                    while(true) {
                        for (int i = 0; i < 6; i++) {
                            time[i]++;
                            if (time[i] > 20)
                                nodeList.get(i).setOnline(false);
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            t.start();

            while(true) {
                buf_receive = new byte[100];
                DatagramPacket packet_receive = new DatagramPacket(buf_receive, buf_receive.length);

                server.receive(packet_receive);
                String recvStr = new String(packet_receive.getData(), 0, packet_receive.getLength());
                System.out.println(recvStr + " connecting");
                int num = Integer.parseInt(recvStr);
                time[num - 1] = 0;
                nodeList.get(num-1).setOnline(true);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
