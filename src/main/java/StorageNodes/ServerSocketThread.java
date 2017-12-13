package StorageNodes;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class ServerSocketThread extends Thread{
    private Socket socket = null;
    private BufferedReader br = null;
    private PrintWriter pw = null;
    private String path;
    public ServerSocketThread(Socket s, String path){
        this.socket = s;
        this.path = path;
    }
    public static void clean(final Object buffer) throws Exception {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner",new Class[0]);
                    getCleanerMethod.setAccessible(true);
                    sun.misc.Cleaner cleaner =(sun.misc.Cleaner)getCleanerMethod.invoke(buffer,new Object[0]);
                    cleaner.clean();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                return null;}});

    }
    public void run(){
        try{
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            FileInputStream fis;
            BufferedInputStream bis;
            FileOutputStream fos;
            BufferedOutputStream bos;

            String filename;
            int length = 0;

            byte[] buffer = new byte[4096];

            int command = dis.readInt();

            switch(command){
                case 1:
                    String ip_F = dis.readUTF();
                    int port_F = dis.readInt();
                    filename = dis.readUTF();
                    int r = 0;

                    File file = new File(path,filename);
                    fos = new FileOutputStream(file);
                    bos = new BufferedOutputStream(fos);

                    while ((length = dis.read(buffer, 0, buffer.length))>0){
                        fos.write(buffer, 0, length);
                        fos.flush();
                    }

                    Socket socket_F = new Socket(ip_F, port_F);
                    DataInputStream dis_F = new DataInputStream(socket_F.getInputStream());
                    DataOutputStream dos_F = new DataOutputStream(socket_F.getOutputStream());
                    dos_F.writeInt(2);
                    dos_F.writeUTF(filename);
                    dos_F.flush();
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    while ((r = bis.read(buffer)) != -1) {
                        dos_F.write(buffer, 0, r);
                        dos_F.flush();
                    }

                    bis.close();
                    fis.close();
                    dis_F.close();
                    dos_F.close();
                    bos.close();
                    fos.close();

                    //dos.writeInt(1);

                    dos.close();
                    dis.close();
                    socket.close();




//                    clean();
                    break;

                case 2:
                    filename = dis.readUTF();

                    fos = new FileOutputStream(new File(path, filename));
                    bos = new BufferedOutputStream(fos);

                    while ((length = dis.read(buffer, 0, buffer.length))>0){
                        fos.write(buffer, 0, length);
                        fos.flush();
                    }

                    bos.close();
                    fos.close();
                    dos.close();
                    dis.close();
                    socket.close();
                    break;

                case 3:
                    filename = dis.readUTF();
//                    dos.writeUTF(filename);
//                    dos.flush();

                    File t = new File(path, filename);

                    fis = new FileInputStream(t);
                    bis = new BufferedInputStream(fis);

                    while ((r = bis.read(buffer)) != -1) {
                        dos.write(buffer, 0, r);
                        dos.flush();
                    }

                    bis.close();
                    fis.close();
                    dos.close();
                    dis.close();
                    socket.close();
                    break;

                case 4:
                    filename = dis.readUTF();
                    File deleteFile = new File(path, filename);
                    System.out.println(deleteFile.getAbsolutePath());
                    //deleteFile.delete();
                    if (deleteFile.exists() && deleteFile.isFile()) {
                        if (deleteFile.delete()) {
                            System.out.println("delete" + filename + "success");

                        } else {
                            System.out.println("delete" + filename + "false");

                        }
                    } else {
                        System.out.println(filename + "don't exist");

                    }
                    dos.writeInt(1);
                    dis.close();
                    dos.close();
                    socket.close();
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
