package net.deepeshmakhijani.bpgclogin;

/**
 * Created by shubhamk on 30/10/16.
 */

public class SettingsItemFormat {
    String username;
    String password;
    public SettingsItemFormat()
    {   username="";
        password="";
    }
public SettingsItemFormat(String username,String password){
   this.username=username;
   this.password=password;
}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
