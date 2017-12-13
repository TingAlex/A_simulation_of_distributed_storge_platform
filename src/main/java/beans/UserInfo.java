package beans;

/**
 * Created by Ting on 2017/7/16.
 */
public class UserInfo {
    private String userName;
    private String password;

    public UserInfo(){}
    public UserInfo(String userName, String password)  {
        this.userName= userName;
        this.password=password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
