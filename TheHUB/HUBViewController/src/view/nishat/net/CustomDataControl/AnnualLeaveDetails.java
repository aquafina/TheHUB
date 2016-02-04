package view.nishat.net.CustomDataControl;

import hub.nishat.net.model.AM.HubModuleImpl;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.server.ViewObjectImpl;

import view.nishat.net.Helper.CommonUtil;
import view.nishat.net.Helper.Constants;
import view.nishat.net.Helper.PolicyHelper;
import view.nishat.net.PoJo.MonthlyDeductedLeave;

public class AnnualLeaveDetails {
    final int irregFromMonth = 6;
    final int irregFromYear = 2015;
    final int currentYear;
    private String connectionString =
        "jdbc:oracle:thin:@192.168.0.31:1522:prod";
    private List<AnnualDeductedLeaveRptBean> list =
        new ArrayList<AnnualDeductedLeaveRptBean>();
    Map<Integer, String> map = new HashMap<Integer, String>();


    public AnnualLeaveDetails() {
        PolicyHelper ph = new PolicyHelper();
        map.put(1, "Casual");
        map.put(2, "Annual");
        map.put(3, "Half Casual");
        map.put(4, "Half Annual");
        map.put(5, "Short");
        map.put(6, "30 Mins Late");
        map.put(7, "TRAVEL");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        currentYear = c.get(Calendar.YEAR);
        
//        List<MonthlyDeductedLeave> leavesPreviousMonth =
//            ph.getMonthlyLeaves("08-2015",
//                                (HubModuleImpl)CommonUtil.getAppModule());
//        CommonUtil.log("Annual Leave Details.java: "+"08-2015");
//        
//        for (MonthlyDeductedLeave m : leavesPreviousMonth) {
//            AnnualDeductedLeaveRptBean mb = new AnnualDeductedLeaveRptBean();
//            mb.setJobStatus(m.getJobStatus());
//            mb.setLeaveCause(m.getLeaveCause());
//            mb.setLeaveDate(m.getLeaveDate());
//            mb.setLeaveID(m.getLeaveID());
//            mb.setLeaveType(m.getLeaveType());
//            mb.setUserID(m.getUserID());
//            mb.setLeaveTypeStr(map.get(m.getLeaveType()));
//            mb.setStatus("In Process");
//            list.add(mb);
//        }
        
       //http://fmw.nishat.net:7003/TheHUB/faces/login_page.jspx
        for (int year = irregFromYear;year <= currentYear; year++) 
        {
            Date currentDate = new Date();
            int totalMonths = 0;
            if (year < currentYear)
            {
                totalMonths = 11;
            }
            else 
            {
                totalMonths = currentDate.getMonth();
            }
            int month=1;
            if (year == 2015) 
            {
                month = 7;
            }
        for (;month<=totalMonths+1;month++)
        {
            String monthStr = "";
            if (month<=9) monthStr="0"+month;
            else monthStr=month+"";
            String leaveDate;
            leaveDate= monthStr+"-"+year;
            List<MonthlyDeductedLeave> leavesPreviousMonth2 =
                    ph.getMonthlyLeaves(leaveDate,
                                        (HubModuleImpl)CommonUtil.getAppModule());
            CommonUtil.log("leaveDate = "+ leaveDate);
                
                for (MonthlyDeductedLeave m : leavesPreviousMonth2) {
                    AnnualDeductedLeaveRptBean mb = new AnnualDeductedLeaveRptBean();
                    mb.setJobStatus(m.getJobStatus());
                    mb.setLeaveCause(m.getLeaveCause());
                    mb.setLeaveDate(m.getLeaveDate());
                    mb.setLeaveID(m.getLeaveID());
                    mb.setLeaveType(m.getLeaveType());
                    mb.setUserID(m.getUserID());
                    mb.setLeaveTypeStr(map.get(m.getLeaveType()));
                    mb.setStatus("In Process");
                    list.add(mb);
                }
        }
           
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =
DriverManager.getConnection(connectionString, "xx_e_portal", "mskiz145");
            ps =
 conn.prepareStatement("select * from XX_E_PORTAL_CONSUMED_LEAVES where user_id = '" +
                       CommonUtil.getSessionValue(Constants.SESSION_USERID).toString() +
                       "' and to_char(leave_date,'YYYY') = '"+year+"' ORDER BY LEAVE_DATE ASC"  );
            rs = ps.executeQuery();
            while (rs.next()) {
                AnnualDeductedLeaveRptBean mb = new AnnualDeductedLeaveRptBean();
                mb.setJobStatus(rs.getString("JOB_STATUS"));
                mb.setLeaveCause(rs.getInt("CAUSE"));
                mb.setLeaveDate(rs.getDate("LEAVE_DATE"));
                mb.setLeaveID(rs.getInt("CONSUMED_LEAVES_ID")+"");
                mb.setLeaveType(rs.getInt("LEAVE_TYPE"));
                mb.setUserID(rs.getInt("USER_ID"));
                mb.setStatus("Processed");
                mb.setLeaveTypeStr(map.get(rs.getInt("LEAVE_TYPE")));
                list.add(mb);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                rs.close();
                ps.close();
                conn.close();
            } catch (Exception ex) {

            }
        }
        }
    }

    public void setList(List<AnnualDeductedLeaveRptBean> list) {
        this.list = list;
    }

    public List<AnnualDeductedLeaveRptBean> getList() {
        return list;
    }
}
