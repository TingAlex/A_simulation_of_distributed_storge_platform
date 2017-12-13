package StorageNodes;
import java.io.*;
import java.net.*;
import java.lang.Thread;

public class Udp_Link extends Thread {
    private String s;
    private String fileServer_IP;
    private String fileServer_Port;
    private String ip;
    private String port;
    public Udp_Link(String s, String f_IP, String f_Port, String ip, String port){
        this.s = s;
        this.fileServer_IP = f_IP;
        this.fileServer_Port = f_Port;
        this.ip = ip;
        this.port = port;
    }
    public void run(){
        try{
            DatagramSocket client = new DatagramSocket();

            InetAddress addr = InetAddress.getByName(fileServer_IP);

            byte[] b;

//            b = ip.getBytes();
//            DatagramPacket ip_Send = new DatagramPacket(b, b.length, addr, Integer.parseInt(fileServer_Port));
//            client.send(ip_Send);
//            System.out.println(ip);
//            b = port.getBytes();
//            DatagramPacket port_Send = new DatagramPacket(b, b.length, addr, Integer.parseInt(fileServer_Port));
//            client.send(port_Send);
//            System.out.println(port);

            b = s.getBytes();
            DatagramPacket packet_Send = new DatagramPacket(b, b.length, addr, Integer.parseInt(fileServer_Port));

            while(true) {
                client.send(packet_Send);
                try
                {
                    Thread.sleep(5000);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
