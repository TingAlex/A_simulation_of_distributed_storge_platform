package StorageNodes;

import StorageNodes.Udp_Link;

import java.io.*;
import java.net.*;
import java.util.Properties;

public class StorageNode_Thread {
    public static void main(String[] args)throws Exception{
        System.out.println("The number of StorageNode:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String num = br.readLine();
        Properties p = new Properties();
        InputStream in = new BufferedInputStream(new FileInputStream("src/main/java/StorageNodes/"+num+".properties"));
        p.load(in);
        String path = p.getProperty("Path");
        String port = p.getProperty("Port");
        String fileServer_IP = p.getProperty("FileServer_IP");
        String fileServer_Port = p.getProperty("FileServer_Port");
        String ip = p.getProperty("IP");
        System.out.println(port);

        new Udp_Link(num, fileServer_IP, fileServer_Port, ip, port).start();
        ServerSocket ss = new ServerSocket(Integer.parseInt(port));
        Socket s;

        while(true){
            s = ss.accept();
            new ServerSocketThread(s, path).start();
        }
    }
}

