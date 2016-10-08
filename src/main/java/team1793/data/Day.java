package team1793.data;

/**
 * Created by tyler on 10/8/16.
 */
public class Day {
    private int loginTime, logoutTime;
    public Day(int loginTime) {
        this.loginTime = loginTime;
    }
    public int getTotalHours() {
        if(logoutTime < loginTime)
            return 0;
        return logoutTime-loginTime;
    }
    public int getLoginTime() {
        return loginTime;
    }

    public int getLogoutTime() {
        return logoutTime;
    }

    public void setLoginTime(int loginTime) {
        this.loginTime = loginTime;
    }

    public void setLogoutTime(int logoutTime) {
        if(loginTime != 0)
            this.logoutTime = logoutTime;
    }
}
