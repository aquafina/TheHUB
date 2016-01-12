package view.nishat.net.Helper;

import hub.nishat.net.model.AM.HubModuleImpl;

import java.sql.Connection;

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

import view.nishat.net.LeaveDeductionRulesEngine;
import view.nishat.net.PoJo.MonthlyDeductedLeave;

public class PolicyHelper {
    public static int NO_APPROVAL_REQUIRED = 1;
    public static int LEAVE_APPROVED = 2;
    public static int LEAVE_DISAPPROVED = 6;
    public static String EMP_TYPE_7 = "3";
    public static String EMP_TYPE_4 = "2";
    private Map<String, Integer> leaves;

    public PolicyHelper() {
        leaves = new HashMap<String, Integer>();
        leaves.put("CASUAL", 1);
        leaves.put("ANNUAL", 2);
        leaves.put("HALF_CASUAL", 3);
        leaves.put("HALF_ANNUAL", 4);
        leaves.put("SHORT", 5);
        leaves.put("30_MINS_LATE", 6);
        leaves.put("TRAVEL_LEAVE", 7);
    }

    public void deductLeaves(int cause, String leaveType, List<Date> leaveDays,
                             String emp_code, String userid,
                             HubModuleImpl am) {
        try {
            CommonUtil.log(cause + " - " + leaveType + " - " + leaveDays +
                           " - " + emp_code + " - " + userid);
            ViewObjectImpl consumed = am.getVO_ConsumedLeaves1();
            String jobStatus = getJobStatus(emp_code);
            for (Date leaveDate : leaveDays) {
                //CommonUtil.log("job Status: "+jobStatus);
                if (jobStatus != null) {
                    Row row = consumed.createRow();
                    row.setAttribute("ConsumedLeavesId",
                                     am.getConsumedLeaveSequence());
                    row.setAttribute("LeaveType",
                                     Integer.parseInt(leaves.get(leaveType).toString()));
                    row.setAttribute("Cause", cause);
                    row.setAttribute("UserId", userid);
                    row.setAttribute("LeaveDate",
                                     CommonUtil.convertFromJAVADateToSQLDate(leaveDate));
                    row.setAttribute("JobStatus", jobStatus);
                    //CommonUtil.log("");
                }
            }
            //consumed.executeQuery();
            am.getTransaction().commit();
            //CommonUtil.log("Everything cool here too");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setLeaves(Map<String, Integer> leaves) {
        this.leaves = leaves;
    }

    public Map<String, Integer> getLeaves() {
        return leaves;
    }

    public String whatToDeduct(int missing_minutes) {
        String what = null;
        if (missing_minutes > 5 && missing_minutes <= 30) {
            what = "30_MINS_LATE";
        } else if (missing_minutes > 30 && missing_minutes <= 90) {
            what = "SHORT";
        } else if (missing_minutes > 91 && missing_minutes <= 240) {
            what = "HALF_CASUAL";
        } else if (missing_minutes > 240) {
            what = "CASUAL";
        }
        return what;
    }
    public List<MonthlyDeductedLeave> getMonthlyLeaves(String monthYear,
                                                       HubModuleImpl am) {
        
        List<MonthlyDeductedLeave> monthlyDeductedLeaves =
            new ArrayList<MonthlyDeductedLeave>();
        String userID =
            CommonUtil.getSessionValue(Constants.SESSION_USERID).toString();
        //String userID = "3099";
        
        ViewObjectImpl vo = am.getVO_AttendanceIrregularityV1();
        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.MONTH, (Integer.parseInt(monthYear.split("-")[0])-1));
        int currentMonthDays = c2.getActualMaximum(Calendar.DAY_OF_MONTH);
                        
        //GET THE JOB STATUS
        String jobStatus = null;
        try {
            jobStatus =
                    getJobStatus(CommonUtil.getSessionValue(Constants.SESSION_EMP_CODE).toString());
            CommonUtil.log("Job Status: "+jobStatus);
            //jobStatus = getJobStatus("001167");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        Calendar cal = Calendar.getInstance();
//        int dateToday = Calendar.DATE;
        //loop through the current month
        for (int i = 1; i <= currentMonthDays; i++) { 
            String date = (i <= 9 ? "0" + i : i) + "-" + monthYear;
            
            /**Reset where parameters that were set in the previous loop iteration*/
            CommonUtil.resetWhereClause(vo);
            String where =
                "emp_id = '" + userID + "' and to_char(attendance_date,'DD-MM-YYYY') = '" +
                date + "'";
            //CommonUtil.log("PolicyHelper.java"+where);
            vo.setWhereClause(where);
//            String orderBy = "order by ATTENDANCXE_DATE ";
//            vo.setOrderByClause(orderBy);
            vo.executeQuery();
            int pageCount = vo.getEstimatedRangePageCount();
            //CommonUtil.log("Page Count: "+pageCount);
            RowSetIterator rsi = vo.createRowSetIterator(null);
            Row[] rows =
                pageCount == 1 ? rsi.getAllRowsInRange() : rsi.getNextRangeSet();
         
            int lateIn = 0;
            int earlyOut = 0;
            int missingMinutes7 = 0;
            String flag = null;
            String empType = rows.length>0?rows[0].getAttribute("EmpType").toString():"";
//            rows!=null?rows[0].getAttribute("EmpType").toString():""
            int expectedWorkHours = 0;
            while (rows.length > 0) {
                for (int j = 0; j < rows.length; j++) {
                    String type = rows[j].getAttribute("Type").toString();
                    flag =
rows[j].getAttribute("LeaveType") == null ? null : rows[j].getAttribute("LeaveType").toString();

                    String expectedWH =
                        rows[j].getAttribute("ExpectedWorkHours").toString();
                    int expectedWM =
                        Integer.parseInt(expectedWH.split(":")[0]) * 60 +
                        Integer.parseInt(expectedWH.split(":")[1]);
                    expectedWorkHours = expectedWM;
                    if (type.equals("LATE_IN")) {
                        lateIn =
                                Integer.parseInt(rows[j].getAttribute("MinutesMissing").toString());
                        //CommonUtil.log(""+lateIn);
                    } else if (type.equals("EARLY_OUT")) {
                        earlyOut =
                                Integer.parseInt(rows[j].getAttribute("MinutesMissing").toString());
                        //CommonUtil.log(""+earlyOut);
                    } else if (type.equals("ABSENT")) {
                        lateIn =
                                Integer.parseInt(rows[j].getAttribute("MinutesMissing").toString());
                        //CommonUtil.log(""+lateIn);
                    }
                    if (type.equals("MISSED_MINUTES_7") || type.equals("ABSENT")) {
                        if (rows[j].getAttribute("MinutesMissing")==null)
                        {
                            missingMinutes7 = 420;
                        }
                        else missingMinutes7 = Integer.parseInt((rows[j].getAttribute("MinutesMissing")).toString());
                    }
                }
                rows = rsi.getNextRangeSet();
            }
            

            LeaveDeductionRulesEngine engine = new LeaveDeductionRulesEngine();
            ArrayList<String> list = null;
            if (empType.equals(EMP_TYPE_7)) 
            {
                list = engine.getLeaveDeducted7(missingMinutes7, flag, 7);// hardcoded expected worked hours, replace with user's
            }
            //CommonUtil.log(lateIn + " and " + earlyOut + " on " + date);
            else 
            {
                list =
                    engine.getLeaveDeducted(lateIn, earlyOut, flag, 8);// hardcoded expected worked hours, replace with user's 
            }
            for (String s : list) {
             CommonUtil.log("Just after calculations: "+s);
            }

            for (String leaveType : list) {
                MonthlyDeductedLeave leave = new MonthlyDeductedLeave();
                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH,
                      Integer.parseInt(date.split("-")[0]));
                c.set(Calendar.MONTH,
                      Integer.parseInt(date.split("-")[1]) - 1);
                c.set(Calendar.YEAR, Integer.parseInt(date.split("-")[2]));
                System.out.println("Time Stamp = "+c.getTime());
                leave.setJobStatus(jobStatus);
                leave.setLeaveCause(5);
                leave.setLeaveDate(c.getTime());
                leave.setLeaveID(am.getConsumedLeaveSequence().intValue() +
                                 "");
                leave.setLeaveType(leaves.get(leaveType));
                //CommonUtil.log(" Middle of calculations: "+leaveType+" id: "+leaves.get(leaveType));
                leave.setUserID(Integer.parseInt(userID));

                //CommonUtil.log("before adding leave"+leave.getLeaveType()+" and  "+leave.getLeaveDate());
                monthlyDeductedLeaves.add(leave);
            }

        }
        for (MonthlyDeductedLeave l : monthlyDeductedLeaves) {
            //CommonUtil.log("helloooo :"+l.getLeaveType()+" "+l.getLeaveDate());
        }
        CommonUtil.resetWhereClause(vo);


        return monthlyDeductedLeaves;

    }

    private String getJobStatus(String emp_code) {


        Connection connection = null;
        String jobStatus = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            String unitCode = getUnitCode(emp_code);
            if (unitCode!=null){
                connection = CommonUtil.connectHRDB();
                String query =
                    "select emp_code,job_status from hr_employees where emp_code = '" +
                    emp_code + "' and UNIT_CODE = '"+unitCode+"'";
                //CommonUtil.log(query);
                pst = connection.prepareStatement(query);
                rs = pst.executeQuery();
                if (rs.next()) {
                    jobStatus = rs.getString("JOB_STATUS");
                    //CommonUtil.log("Job Status: "+jobStatus);
                } else {
                    CommonUtil.doNothing();
                }   
            }else{
                jobStatus = "U";
            }
            

        } catch (Exception ex) {
            ex.printStackTrace();


        } finally {
            if (connection != null) {
                try {
                    CommonUtil.log("Connection Closed PolicyHelper.java");
                    rs.close();
                    pst.close();
                    connection.close();
                    connection = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return jobStatus;
    }

    private String getUnitCode(String empCode) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String unitCode = null;
        try {
            conn = CommonUtil.connectEBSDB();
            String query =
                "select attribute3 from per_all_people_f where ATTRIBUTE2 = '"+empCode+"'";
            //CommonUtil.log("Unit Code Query: "+query);
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) {
                unitCode =  rs.getString("attribute3");
              //  CommonUtil.log("Unit Code : "+unitCode);
            }
        } catch (Exception ex) {
            
        } finally {
            try {
                rs.close();
                pst.close();
                conn.close();
            } catch (Exception e) {

            }
        }
        return unitCode;
    }
}
