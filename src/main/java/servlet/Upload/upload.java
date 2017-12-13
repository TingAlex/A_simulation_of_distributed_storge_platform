package servlet.Upload;

import EncodeAndCompress.FileEncryptAndDecrypt;
import EncodeAndCompress.MyZipCompressing;
import Operation.Oper;
import beans.FileInfo;
import beans.StorageNode;
import beans.WaitForUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static beans.Constants.CLIENT_IP;
import static beans.Constants.SN_IP;
import static beans.Constants.UPLOAD_DIRECTORY;
import static servlet.StaticFile.fileInfoList;
import static servlet.StaticFile.nodeList;
import static servlet.StaticFile.waitForUploadList;

/**
 * Created by Ting on 2017/7/17.
 */
@WebServlet(urlPatterns = {"/upload"})
//@MultipartConfig(location = "C:\\Users\\Ting\\IdeaProjects\\TempStorage")
public class upload extends HttpServlet {
    // 上传文件存储目录

    // 上传配置
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;  // 3MB

    //    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
//    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    public void UseNode(StorageNode node, Long length) {
        for (int i = 0; i < nodeList.size(); i++) {
            if (node.getPort() == nodeList.get(i).getPort()) {
                nodeList.get(i).setUsed(nodeList.get(i).getUsed() + length);
                break;
            }
        }
    }

    public void AddNodeToFileInfo(String UID, StorageNode node) {
        for (int i = 0; i < fileInfoList.size(); i++) {
            if (UID.equals(fileInfoList.get(i).getUID())) {
                fileInfoList.get(i).addStroageNode(node.getIP() + ":" + node.getPort());
                break;
            }
        }
    }

    public boolean judgeEnough(LinkedList<StorageNode> list, int index, File file) {
        if (list.get(index).getRemain() < file.length()) {
            return false;
        } else return true;
    }

    public boolean UploadToSN(String UID, File file) {
        try {
            int PORT;
            Socket socket;
            LinkedList<StorageNode> list = Oper.chooseNode(nodeList, 2);
            if (list.size() == 2) {
                //判断主节点容量是否足够
                //看看副节点
                boolean anotherEnough = false;
                if (judgeEnough(list, 0, file)) {
                    //把主节点地址写入表中
                    UseNode(list.get(0), file.length());
                    AddNodeToFileInfo(UID, list.get(0));
                    if (judgeEnough(list, 1, file)) {
                        //把副节点地址写入表中
                        UseNode(list.get(1), file.length());
                        AddNodeToFileInfo(UID, list.get(1));
                        anotherEnough = true;
                    } else {
                        anotherEnough = false;
                    }
                    PORT = list.get(0).getPort();
                    socket = new Socket(SN_IP, PORT);

                    //2、获取输出流，向服务器端发送信息
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    byte[] buffer = new byte[4096];
                    int rr;
                    if (anotherEnough == false) {
                        dos.writeInt(2);
                    } else {
                        dos.writeInt(1);
                        dos.writeUTF(SN_IP);
                        dos.writeInt(list.get(1).getPort());

                    }
                    dos.writeUTF(file.getName());
                    //dos.writeLong(file.length());
                    dos.flush();
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    while ((rr = bis.read(buffer)) != -1) {
                        dos.write(buffer, 0, rr);
                        dos.flush();
                    }

                    bis.close();
                    fis.close();
//                    int result=dis.readInt();
//                    if(result==1){
//                        System.out.println("Transfer success");
//                    }
                    //上面的代码是成功传给了一个节点
                    dis.close();
                    dos.close();
                    socket.close();
                    return true;

                } else {
                    System.out.println("Not Enough ");
                    return false;
                }
            } else {
                System.out.println("No node exists ");
                return false;
            }
        } catch (Exception e) {

            System.out.println("服务器异常: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //req.getRequestDispatcher("/console").forward(req,resp);
        resp.sendRedirect("/console");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        HttpSession session=req.getSession();
//        String goPub=(String)session.getAttribute("Access");
//        System.out.println(goPub);


        if (!ServletFileUpload.isMultipartContent(req)) {
            PrintWriter writer = resp.getWriter();
            writer.println("Error: form needs contain enctype=multipart/form data");
            writer.flush();
            return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);

        upload.setHeaderEncoding("UTF-8");
        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录
        String uploadPath = req.getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;
        //如果目录不存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        try {
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(req);

            if (formItems != null && formItems.size() > 0) {
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        //将节点信息保存到总表
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setFileName(fileName);
                        //计算UID和key
                        String UID = UUID.randomUUID().toString();
                        fileInfo.setUID(UID);
                        fileInfo.setKey(UID);
                        fileInfo.setSize(new File(item.getName()).length());
                        HttpSession session = req.getSession();
                        String userName = (String) session.getAttribute("name");
                        fileInfo.setUser(userName);

                        //将节点信息保存到waitList
                        WaitForUpload waitInfo = new WaitForUpload();
                        waitInfo.setFileName(fileName);
                        waitInfo.setUID(UID);
                        waitForUploadList.add(waitInfo);


                        String filePath = uploadPath + File.separator + UID;
                        File storeFile = new File(filePath);
                        System.out.println(filePath);
                        item.write(storeFile);
                        //在这里进行压缩和加密


//                        String filepath1 = filePath + ".zip";
//                        MyZipCompressing mz = new MyZipCompressing();//压缩
//                        System.out.println(filePath);
//                        mz.zipfile(filepath1, filePath);
//                        FileEncryptAndDecrypt.encrypt(filepath1, UID);//加密
//                        File defile=new File(filePath);
//                        defile.delete();
//                        File file=new File(filepath1);
//                        file.renameTo(new File(filePath));


//                        try {
//                            BufferedReader reader = null;
//                            BufferedWriter writer = null;
//                            reader = new BufferedReader(new FileReader(filePath));
//                            writer = new BufferedWriter(new FileWriter(filePath+"E"));
//                            String tempString = null;
//                            while ((tempString = reader.readLine()) != null) {
//                                //tempString=DESUtil.encrypt(tempString,"A1B2C3D4E5F60708");
//                                writer.write(tempString);
//                            }
//                            writer.flush();
//                            writer.close();
//                            reader.close();
//                        }catch (Exception ee){
//                            ee.printStackTrace();
//                            req.setAttribute("uploadMessage","upload false");
//                        }
//                        File delFile=new File(filePath);
//                        delFile.delete();
//                        File file=new File(filePath+"E");
//                        file.renameTo(new File(filePath));
                        //把文件大小信息写进fileInfo
                        fileInfo.setSize(new File(filePath).length());
                        fileInfoList.add(fileInfo);
                        //在这里开启线程执行上传文件到SN操作
                        if (UploadToSN(UID, new File(filePath))) {

                            req.setAttribute("uploadMessage", "upload success!");
                            req.setAttribute("UID", UID);
//                            file=new File(filePath);
                            storeFile.delete();
                        } else
                            req.setAttribute("uploadMessage", "upload false");

                    }
                }
            }
        } catch (Exception e) {
            req.setAttribute("uploadMessage", "error: " + e.getMessage());
        }
        req.getRequestDispatcher("/console").forward(req, resp);
    }

}
