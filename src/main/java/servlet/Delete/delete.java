package servlet.Delete;

import Operation.Oper;
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
import java.util.LinkedList;

import static beans.Constants.CLIENT_IP;
import static beans.Constants.SN_IP;
import static servlet.StaticFile.*;

/**
 * Created by Ting on 2017/7/18.
 */
@WebServlet(urlPatterns = {"/delete"})
public class delete extends HttpServlet {
    public boolean ReliseNode(StorageNode node,String UID){
        Long size=0L;
        for(int i=0;i<fileInfoList.size();i++){
            if(fileInfoList.get(i).getUID().equals(UID)){
                fileInfoList.get(i).removeStroageNode(node.getIP()+":"+node.getPort());
                size=fileInfoList.get(i).getSize();
                break;
            }

        }
        for(int i=0;i<nodeList.size();i++){
            if(nodeList.get(i).getPort()==node.getPort()){
                nodeList.get(i).setUsed( nodeList.get(i).getUsed()-size);
                return true;
            }
        }
        return false;
    }
    public StorageNode judgeOnline(String node){
        String IP=node.split(":")[0];
        int Port=Integer.parseInt(node.split(":")[1]);
        for(int i=0;i<nodeList.size();i++){
            if(nodeList.get(i).getOnline()&&nodeList.get(i).getIP().equals(IP)&&nodeList.get(i).getPort()==Port){
                return nodeList.get(i);
            }
        }
        return null;
    }
    public int DeleteFromSN(String UID,LinkedList<String> nodes) {
        int num=0;
        try {
            int PORT;
            Socket socket;
            while(nodes.size()!=0){
                //判断节点是否在线
                StorageNode node;
                if((node=judgeOnline(nodes.getFirst()))!=null){
                    PORT=node.getPort();
                    socket = new Socket(SN_IP, PORT);

                    //2、获取输出流，向服务器端发送信息
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                    dos.writeInt(4);

                    dos.writeUTF(UID);
                    dos.flush();
                    int result=dis.readInt();
                    if(result==1){
                        System.out.println(node.getName()+"Delete success");
                        num++;
                    }
                    //上面的代码是成功传给了一个节点
                    dis.close();
                    dos.close();
                    socket.close();
                    ReliseNode(node,UID);
                }else{
                    System.out.println(nodes.getFirst()+"Delete false ");
                }
            }
        } catch (Exception e) {
            System.out.println("服务器异常: " + e.getMessage());
            return -1;
        }
        return num;
    }
    public boolean Delete(String userName,String UID){
        for(int i=0;i<fileInfoList.size();i++){
//            FileInfo fileInfo=fileInfoList.get(i);
//            System.out.println(fileInfoList.get(i).getUID());
//            System.out.println(fileInfoList.get(i).getUser());
//            System.out.println(fileInfoList.get(i).getGoPublic());
            if((fileInfoList.get(i).getUser().equals(userName))&&fileInfoList.get(i).getUID().equals(UID)){
                if(fileInfoList.get(i).getDelete()){
                    return false;
                }else {
                    fileInfoList.get(i).setDelete(true);
                    //远程删除，成功就return true
                    if(DeleteFromSN(UID,fileInfoList.get(i).getStroageNodes())>0){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String UID=(String)req.getParameter("deleteFile");
        if(!UID.equals("")){
            HttpSession session=req.getSession();
            String userName=(String)session.getAttribute("name");
            if(Delete(userName,UID)){
                req.setAttribute("deleteMessage","delete success!");
                req.getRequestDispatcher("/console").forward(req,resp);
            }else{
                req.setAttribute("deleteMessage","delete false!");
                req.getRequestDispatcher("/console").forward(req,resp);
            }
        }else {
            resp.sendRedirect("/console");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
