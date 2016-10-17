package team1793.data;

/**
 * Created by tyler on 10/8/16.
 */
public class Session {

    private int loginTime, logoutTime;
    private boolean needsBusPass;
    public Session(int loginTime, int logoutTime) {
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
    }


    public int getTimeLoggedIn() {
        assert getLogoutTime() > getLoginTime();
        int diff = getLogoutTime() - getLoginTime();
        if (diff <= 0)
            diff = 60;
        return diff;
    }

    public String getFormattedLoginTime() {
        return String.format("%02d:%02d", loginTime / 60, loginTime % 60);
    }

    public String getFormattedLogoutTime() {
        return String.format("%02d:%02d", logoutTime / 60, logoutTime % 60);
    }

    public int getLoginTime() {
        return loginTime;
    }

    public int getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(int logoutTime) {
        if (loginTime != 0)
            this.logoutTime = logoutTime;
    }

    public boolean needsBusPass() {
        return needsBusPass;
    }

    public void setNeedsBusPass(boolean needsBusPass) {
        this.needsBusPass = needsBusPass;
    }

    @Override
    public String toString() {
        return getFormattedLoginTime() + "," + getFormattedLogoutTime();
    }

    public boolean hasLoggedOut() { return getLoginTime() != getLogoutTime(); }
}
