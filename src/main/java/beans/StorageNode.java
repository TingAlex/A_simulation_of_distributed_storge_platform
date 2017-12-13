package beans;

/**
 * Created by Ting on 2017/7/17.
 */
public class StorageNode {
    private String name;
    private String IP;
    private int port;
    private Long capacity;
    private Long used;
    private Long remain;
    private Boolean online;
    public StorageNode(){
        capacity = 1024 * 1024L;
    };
    public StorageNode(String name, String IP, int port, Long capacity, Long used, Long remain, Boolean online) {
        this.name = name;
        this.IP = IP;
        this.port = port;
        this.capacity = capacity;
        this.used = used;
        this.remain = remain;
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Long getUsed() {
        return used;
    }

    public void setUsed(Long used) {
        this.used = used;
        remain=remain-used;
    }

    public Boolean getOnline() {
        return online;
    }
    public Long getRemain() {
        return remain;
    }
    public void setRemain(Long remain) {
        this.remain=remain;
    }
    public void setOnline(Boolean online) {
        this.online = online;
    }
}
