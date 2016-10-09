package team1793;

/**
 * Created by tyler on 10/8/16.
 */
public class Day {

    private int loginTime, logoutTime;
    public Day(int loginTime, int logoutTime) {
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
    }

    public Day(int loginTime) {
        this(loginTime,0);
    }

    public Day(String loginTime, String logoutTime) {

    }

    public int getTotalMinutes() {
        if(logoutTime < loginTime)
            return 0;
        return logoutTime-loginTime;
    }
    public int getTimeLoggedIn() {
        assert getLogoutTime() > getLoginTime();
        int diff = getLogoutTime() - getLoginTime();
        if(diff < 0)
            diff = 0;
        return diff;
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
