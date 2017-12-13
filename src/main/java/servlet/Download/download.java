package servlet.Download;

import EncodeAndCompress.CopyOfMyzipDecompressing;
import EncodeAndCompress.FileEncryptAndDecrypt;
import beans.FileInfo;
import beans.StorageNode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.LinkedList;

import static beans.Constants.SN_IP;
import static beans.Constants.UPLOAD_DIRECTORY;
import static servlet.StaticFile.fileInfoList;
import static servlet.StaticFile.nodeList;

/**
 * Created by Ting on 2017/7/18.
 */
@WebServlet(urlPatterns = {"/download"})
public class download extends HttpServlet{
    public LinkedList<StorageNode> judgeOnline(LinkedList<String> node){
        LinkedList<StorageNode> storageNodes=new LinkedList<StorageNode>();

        for(int j=0;j<node.size();j++){

        String IP=node.get(j).split(":")[0];
        int Port=Integer.parseInt(node.get(j).split(":")[1]);
            for(int i=0;i<nodeList.size();i++){
                if(nodeList.get(i).getOnline()&&nodeList.get(i).getIP().equals(IP)&&nodeList.get(i).getPort()==Port){
                    storageNodes.add(nodeList.get(i));
                }
            }
        }
        return storageNodes;
    }

    public boolean Download(String userName,String UID,String path) {
        for (int i = 0; i < fileInfoList.size(); i++) {
//            FileInfo fileInfo = fileInfoList.get(i);
//            System.out.println(fileInfoList.get(i).getUID());
//            System.out.println(fileInfoList.get(i).getUser());
//            System.out.println(fileInfoList.get(i).getGoPublic());
            if ((fileInfoList.get(i).getGoPublic() || fileInfoList.get(i).getUser().equals(userName)) && fileInfoList.get(i).getUID().equals(UID)) {
                if (fileInfoList.get(i).getDelete()) {
                    return false;
                } else {
                    String trueName=fileInfoList.get(i).getFileName();
                    LinkedList<String> stringLinkedList = fileInfoList.get(i).getStroageNodes();
                    LinkedList<StorageNode> nodes = judgeOnline(stringLinkedList);
                    for(int j=0;j<nodes.size();j++){
                        try {
                            StorageNode node = nodes.get(j);
                            int PORT = node.getPort();
                            Socket socket = new Socket(SN_IP, PORT);
                            //2、获取输出流，向服务器端发送信息
                            DataInputStream dis = new DataInputStream(socket.getInputStream());
                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                            dos.writeInt(3);

                            dos.writeUTF(UID);
                            dos.flush();
                            String filePath=path+File.separator+trueName;
                            FileOutputStream fos = new FileOutputStream(new File(filePath));
                            BufferedOutputStream bos = new BufferedOutputStream(fos);
                            int length;
                            byte[] buffer = new byte[4096];
                            while ((length = dis.read(buffer, 0, buffer.length))>0){
                                fos.write(buffer, 0, length);
                                fos.flush();
                            }
                            bos.close();
                            fos.close();
                            dis.close();
                            dos.close();
                            socket.close();
                            break;
                        }catch (Exception e){
                            System.out.println("transfer from SN false");
                            if(nodes.size()==2){
                                try {
                                    StorageNode node = nodes.get(1);
                                    int PORT = node.getPort();
                                    Socket socket = new Socket(SN_IP, PORT);

                                    //2、获取输出流，向服务器端发送信息
                                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                                    dos.writeInt(3);

                                    dos.writeUTF(UID);
                                    dos.flush();
                                    String filePath=path+File.separator+UID;
                                    FileOutputStream fos = new FileOutputStream(new File(filePath));
                                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                                    int length;
                                    byte[] buffer = new byte[4096];
                                    while ((length = dis.read(buffer, 0, buffer.length))>0){
                                        fos.write(buffer, 0, length);
                                        fos.flush();
                                    }
                                    bos.close();
                                    fos.close();
                                    dis.close();
                                    dos.close();
                                    socket.close();
                                    break;
                                }catch (Exception ee){
                                    System.out.println("transfer from another SN false too");
                                }
                            }
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    public String findNameByUID(String UID){
        String name=null;
        for(int i=0;i<fileInfoList.size();i++){
            if(fileInfoList.get(i).getUID().equals(UID)){
                name=fileInfoList.get(i).getFileName();
                break;
            }
        }
        return name;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String UID=(String)req.getParameter("downloadFile");

        if(!UID.equals("")){
            HttpSession session=req.getSession();
            String userName=(String)session.getAttribute("name");
            String path = req.getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;
            if(Download(userName,UID,path)){
                //根据UID找到它的fileInfo表中原名
                String fileName=findNameByUID(UID);
                //在我的路径下找到这个文件
                String filePath=path+File.separator+fileName;
                File file = new File(filePath);
//                try {
//                    FileEncryptAndDecrypt.decrypt(filePath, filePath+".zip", UID.length());// 解密
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                file.delete();
//                CopyOfMyzipDecompressing cmd1 = new CopyOfMyzipDecompressing();//解压
//                cmd1.free(filePath+".zip", filePath);


                System.out.println(file.getAbsolutePath());
                //deleteFile.delete();
                if (file.exists() && file.isFile()) {
                    resp.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                // 读取要下载的文件，保存到文件输入流
                FileInputStream in = new FileInputStream(path+File.separator+fileName);
                // 创建输出流
                OutputStream out = resp.getOutputStream();
                // 创建缓冲区
                byte buffer[] = new byte[1024];
                int len = 0;
                // 循环将输入流中的内容读取到缓冲区当中
                while ((len = in.read(buffer)) > 0) {
                    // 输出缓冲区的内容到浏览器，实现文件下载
                    out.write(buffer, 0, len);
                }
                // 关闭文件输入流
                in.close();
                // 关闭输出流
                out.close();
                req.setAttribute("downloadMessage","download success!");
                req.getRequestDispatcher("/console").forward(req,resp);


            } else {
                    System.out.println(UID + "find but can't download");
                    req.setAttribute("downloadMessage","download success!");
                    req.getRequestDispatcher("/console").forward(req,resp);
                }
            } else {
                req.setAttribute("downloadMessage","download false!");
                req.getRequestDispatcher("/console").forward(req,resp);
            }
        }else {
            resp.sendRedirect("/console");
            }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}