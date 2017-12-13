package beans;

import java.util.LinkedList;

/**
 * Created by Ting on 2017/7/17.
 */
public class FileInfo {
    private Boolean goPublic;
    private String user;
    private String UID;
    private String fileName;
    private LinkedList<String> stroageNodes;
    private String key;
    private Boolean Delete;
    private Long size;
    public FileInfo(){
        goPublic=false;
        Delete=false;
        stroageNodes=new LinkedList<String>();
    }
    public FileInfo(Boolean goPublic, String user, String UID, String fileName, LinkedList<String> stroageNodes, String key, Boolean delete,Long size) {
        this.goPublic = goPublic;
        this.user = user;
        this.UID = UID;
        this.fileName = fileName;
        this.stroageNodes = stroageNodes;
        this.key = key;
        this.Delete = delete;
        this.size=size;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Boolean getGoPublic() {
        return goPublic;
    }

    public void setGoPublic(Boolean goPublic) {
        this.goPublic = goPublic;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LinkedList<String> getStroageNodes() {
        return stroageNodes;
    }

    public void setStroageNodes(LinkedList<String> stroageNodes) {
        this.stroageNodes = stroageNodes;
    }

    public String getStroageNode(int index) {
        return stroageNodes.get(index);
    }

    public void addStroageNode(String stroageNode) {
        stroageNodes.add(stroageNode);
    }

    public void removeStroageNode(int index){stroageNodes.remove(index);}

    public void removeStroageNode(String stroageNode){
        for(int i=0;i<stroageNodes.size();i++){
            if(stroageNodes.get(i).equals(stroageNode)){
                removeStroageNode(i);
                break;
            }
        }
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getDelete() {
        return Delete;
    }

    public void setDelete(Boolean delete) {
        Delete = delete;
    }
}
