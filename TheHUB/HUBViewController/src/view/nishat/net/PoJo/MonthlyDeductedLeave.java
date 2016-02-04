package view.nishat.net.PoJo;

import java.util.Date;

public class MonthlyDeductedLeave {
    private String leaveID;
    private int leaveType;
    private int leaveCause;
    private int userID;
    private Date leaveDate;
    private String jobStatus;

    public void setLeaveID(String leaveID) {
        this.leaveID = leaveID;
    }

    public String getLeaveID() {
        return leaveID;
    }

    public void setLeaveType(int leaveType) {
        this.leaveType = leaveType;
    }

    public int getLeaveType() {
        return leaveType;
    }

    public void setLeaveCause(int leaveCause) {
        this.leaveCause = leaveCause;
    }

    public int getLeaveCause() {
        return leaveCause;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getJobStatus() {
        return jobStatus;
    }
    public String toString(){
        return getJobStatus()+" - "+getLeaveCause()+" - "+getLeaveDate()+" - "+getLeaveID()+" - "+getLeaveType()+" - "+getUserID();
    }
}
