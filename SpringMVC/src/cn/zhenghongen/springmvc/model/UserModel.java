package cn.zhenghongen.springmvc.model;

/**
 * Created with IntelliJ IDEA.
 * PROJECT: myCodes
 * File Description:
 * Author: ZhengHongEn
 * Revision History:
 * 2013/06/04             ZhengHongEn              Create
 */
public class UserModel {
    private String username;
    private String password;
    private String realname;
    private WorkInfoModal workInfo;
    private SchoolInfoModal schoolInfo;

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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public WorkInfoModal getWorkInfo() {
        return workInfo;
    }

    public void setWorkInfo(WorkInfoModal workInfo) {
        this.workInfo = workInfo;
    }

    public SchoolInfoModal getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfoModal schoolInfo) {
        this.schoolInfo = schoolInfo;
    }
}
