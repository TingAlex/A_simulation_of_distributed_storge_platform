package beans;

/**
 * Created by Ting on 2017/7/17.
 */
public class WaitForUpload {
    private String fileName;
    private String UID;
    private Boolean ready;
    public WaitForUpload(){
        ready=false;
    }
    public WaitForUpload(String fileName, String UID, Boolean ready) {
        this.fileName = fileName;
        this.UID = UID;
        this.ready = ready;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }
}
