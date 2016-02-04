package view.nishat.net.CustomDataControl;

import hub.nishat.net.model.AM.HubModuleImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import view.nishat.net.Helper.CommonUtil;
import view.nishat.net.Helper.Constants;
import view.nishat.net.Helper.PolicyHelper;
import view.nishat.net.PoJo.MonthlyDeductedLeave;

public class LeaveReport {
    private List<LeaveBalanceRec> recordList;
    float annualLeave = 0;
    float casualLeave = 0;
    float shortLeave = 0;
    float lates = 0;

    public LeaveReport() {
        recordList = new ArrayList<LeaveBalanceRec>();
        //getMonthlyLeaves();
        CommonUtil.log(annualLeave+" - "+casualLeave+"  - "+shortLeave+" - "+lates);
        
        LeaveBalanceRec annualRec = new LeaveBalanceRec();
        annualRec.setLeaveType("Annual");
        float availableAnnual = Float.parseFloat(CommonUtil.getSessionValue(Constants.SESSION_ANNUAL_AVAILABLE).toString());
        float availedAnnualDB = Float.parseFloat(CommonUtil.getSessionValue(Constants.SESSION_ANNUAL_AVAILED).toString());
        annualRec.setLeaveAvailable(availableAnnual);
        annualRec.setLeaveAvailed(availedAnnualDB+annualLeave);
        CommonUtil.log(availableAnnual+" - "+availableAnnual+" = "+(availableAnnual-availedAnnualDB));
        //annualRec.setUnpaid((availableAnnual-availedAnnualDB)>0?0:(availableAnnual-availedAnnualDB));
        annualRec.setUnpaid(0);
        
        LeaveBalanceRec casualRec = new LeaveBalanceRec();
        casualRec.setLeaveType("Casual");
        float availableCasual = Float.parseFloat(CommonUtil.getSessionValue(Constants.SESSION_CASUAL_AVAILABLE).toString());
        float availedCasualDB = Float.parseFloat(CommonUtil.getSessionValue(Constants.SESSION_CASUAL_AVAILED).toString());
        casualRec.setLeaveAvailable(availableCasual);
        casualRec.setLeaveAvailed(availedCasualDB+casualLeave);
        casualRec.setUnpaid((availedCasualDB+casualLeave)-availedCasualDB <0?(availedCasualDB+casualLeave)-availedCasualDB:0);
        
        
        LeaveBalanceRec shortRec = new LeaveBalanceRec();
        shortRec.setLeaveType("Short");
        float availableShort = Float.parseFloat(CommonUtil.getSessionValue(Constants.SESSION_SHORT_AVAILABLE).toString());
        float availedShortDB = Float.parseFloat(CommonUtil.getSessionValue(Constants.SESSION_SHORT_AVAILED).toString());
        shortRec.setLeaveAvailable(availableShort);
        shortRec.setLeaveAvailed(availedShortDB+shortLeave);
        shortRec.setUnpaid((availedShortDB+shortLeave)-availedShortDB <0?(availedShortDB+shortLeave)-availedShortDB:0);
        
        LeaveBalanceRec lateRec = new LeaveBalanceRec();
        lateRec.setLeaveType("30 Min Lates");
        float availableLates = Float.parseFloat(CommonUtil.getSessionValue(Constants.SESSION_LATES_AVAILABLE).toString());
        float availedLatesDB = Float.parseFloat(CommonUtil.getSessionValue(Constants.SESSION_LATES_AVAILED).toString());
        lateRec.setLeaveAvailable(availableLates);
        lateRec.setLeaveAvailed(availedLatesDB+lates);
        lateRec.setUnpaid((availedLatesDB+lates)-availableLates <0?(availedLatesDB+lates)-availableLates:0);
        recordList.add(casualRec);
        recordList.add(annualRec);
        recordList.add(shortRec);
        recordList.add(lateRec);
    }

    private void getMonthlyLeaves() {
        PolicyHelper ph = new PolicyHelper();
        List<MonthlyDeductedLeave> leaves =
            ph.getMonthlyLeaves(CommonUtil.getMM_YYYY(new Date()),
                                (HubModuleImpl)CommonUtil.getAppModule());
        
        for (MonthlyDeductedLeave leave : leaves) {
            switch (leave.getLeaveType()) {
            case 1:
                casualLeave += 1;
                break;
            case 2:
                annualLeave += 1;
                break;
            case 3:
                casualLeave += .5;
                break;
            case 4:
                annualLeave += .5;
                break;
            case 5:
                shortLeave += 1;
                break;
            case 6:
                lates += 1;
                break;
            }
        }
        

    }

    public void setRecordList(List<LeaveBalanceRec> recordList) {
        this.recordList = recordList;
    }

    public List<LeaveBalanceRec> getRecordList() {
        return recordList;
    }
}
