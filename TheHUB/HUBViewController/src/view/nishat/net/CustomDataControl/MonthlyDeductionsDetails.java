package view.nishat.net.CustomDataControl;

import hub.nishat.net.model.AM.HubModuleImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import view.nishat.net.Helper.CommonUtil;
import view.nishat.net.Helper.Constants;
import view.nishat.net.Helper.PolicyHelper;
import view.nishat.net.PoJo.MonthlyDeductedLeave;

public class MonthlyDeductionsDetails {
    
    private List<AnnualDeductedLeaveRptBean> list =
        new ArrayList<AnnualDeductedLeaveRptBean>();
    Map<Integer, String> map = new HashMap<Integer, String>();
    

    public MonthlyDeductionsDetails() {
        PolicyHelper ph = new PolicyHelper();
        map.put(1, "CASUAL");
        map.put(2, "ANNUAL");
        map.put(3, "HALF_CASUAL");
        map.put(4, "HALF_ANNUAL");
        map.put(5, "SHORT");
        map.put(6, "30_MINS_LATE");
        
        CommonUtil.log(CommonUtil.getSessionValue("yearmonth").toString()+" heheheh");
        List<MonthlyDeductedLeave> leaves =
            ph.getMonthlyLeaves("07-2015",
                                (HubModuleImpl)CommonUtil.getAppModule());
        for (MonthlyDeductedLeave m : leaves) {
            AnnualDeductedLeaveRptBean mb = new AnnualDeductedLeaveRptBean();
            mb.setJobStatus(m.getJobStatus());
            mb.setLeaveCause(m.getLeaveCause());
            mb.setLeaveDate(m.getLeaveDate());
            mb.setLeaveID(m.getLeaveID());
            mb.setLeaveType(m.getLeaveType());
            mb.setUserID(m.getUserID());
            mb.setLeaveTypeStr(map.get(m.getLeaveType()));
            list.add(mb);
        }
        
    }

    public void setList(List<AnnualDeductedLeaveRptBean> list) {
        this.list = list;
    }

    public List<AnnualDeductedLeaveRptBean> getList() {
        CommonUtil.log(CommonUtil.getSessionValue("yearmonth").toString()+" heeheheh");
        return list;
    }

   
}
