package servlet;

import Operation.Udp_Link;
import beans.FileInfo;
import beans.StorageNode;
import beans.UserInfo;
import beans.WaitForUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.LinkedList;

import static beans.Constants.SN_IP;
import static servlet.StaticFile.*;
//import static servlet.StaticFile.nodeList;

/**
 * Created by Ting on 2017/7/18.
 */

public class Ini extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        userInfoList=new LinkedList<UserInfo>();
        nodeList=new LinkedList<StorageNode>();
        fileInfoList=new LinkedList<FileInfo>();
        waitForUploadList=new LinkedList<WaitForUpload>();
        String IP=SN_IP;
        Long capacity=new Long(1024*1024);
        Long used=new Long(0);
        Long remain=new Long(1024*1024);
        Boolean online=false;
        System.out.println("Init ");
        int [] Port=new int[6];
        for(int i=0;i<6;i++){
            StorageNode node=new StorageNode("Node",IP,0,capacity,used,remain,online);
            Port[i]=4001+i;
            node.setPort(Port[i]);
            nodeList.add(node);
            System.out.println("Init "+IP+" : "+Port[i]);
        }
        new Udp_Link().start();
        //测试使用，先开两个节点测试上传
//        nodeList.get(0).setOnline(true);
//        nodeList.get(1).setOnline(true);
    }
}
