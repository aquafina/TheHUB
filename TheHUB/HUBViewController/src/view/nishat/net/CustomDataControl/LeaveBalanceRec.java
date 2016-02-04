package view.nishat.net.CustomDataControl;

import getemppayrollrec.PayHistory;

public class LeaveBalanceRec {
    private String leaveType;
    private float leaveAvailed;
    private float leaveAvailable;
    private float unpaid;
    PayHistory ph;

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveAvailed(float leaveAvailed) {
        this.leaveAvailed = leaveAvailed;
    }

    public float getLeaveAvailed() {
        return leaveAvailed;
    }

    public void setLeaveAvailable(float leaveAvailable) {
        this.leaveAvailable = leaveAvailable;
    }

    public float getLeaveAvailable() {
        return leaveAvailable;
    }

    public void setUnpaid(float unpaid) {
        this.unpaid = unpaid;
    }

    public float getUnpaid() {
        return unpaid;
    }
}
