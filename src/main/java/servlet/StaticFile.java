package servlet;

import beans.FileInfo;
import beans.StorageNode;
import beans.UserInfo;
import beans.WaitForUpload;

import java.util.LinkedList;

/**
 * Created by Ting on 2017/7/17.
 */
public class StaticFile {
    public static LinkedList<UserInfo> userInfoList;
    public static LinkedList<StorageNode> nodeList;
    public static LinkedList<FileInfo> fileInfoList;
    public static LinkedList<WaitForUpload> waitForUploadList;

}
