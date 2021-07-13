package TalkBasic;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;
    private int id;
    private String userId;
    private String reuser;
    private String repwd;

    public String getReuser() {
        return reuser;
    }

    public void setReuser(String reuser) {
        this.reuser = reuser;
    }

    public String getRepwd() {
        return repwd;
    }

    public void setRepwd(String repwd) {
        this.repwd = repwd;
    }

    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String userId, String password)
    {
        this.userId=userId;
        this.password=password;
    }
}
