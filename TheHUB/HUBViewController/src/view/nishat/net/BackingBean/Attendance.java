package view.nishat.net.BackingBean;

import com.oracle.xmlns.pcbpel.adapter.db.top.readempsalary.HrPayHistory;

import com.oracle.xmlns.pcbpel.adapter.db.top.readempsalary.HrPayHistoryCollection;

import getemppayrollrec.PayHistory;

import hub.nishat.net.model.AM.HubModuleImpl;


import java.io.Console;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.HashMap;
import java.util.Iterator;

import java.util.List;

import java.util.Map;

import javax.el.ELContext;

import javax.el.ExpressionFactory;

import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;

import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.binding.BindingContainer;

import oracle.binding.OperationBinding;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.domain.Date;
import oracle.jbo.server.PayloadDef;
import oracle.jbo.server.ViewObjectImpl;


import org.apache.myfaces.trinidad.event.AttributeChangeEvent;

import org.apache.myfaces.trinidad.event.DisclosureEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;


import utils.system;

import view.nishat.net.CustomDataControl.MonthlyDeductionsDetails;
import view.nishat.net.Helper.CommonUtil;
import view.nishat.net.Helper.Constants;
import view.nishat.net.Helper.PolicyHelper;
import view.nishat.net.PoJo.EmptyAttendance;
import view.nishat.net.PoJo.MonthlyDeductedLeave;

public class Attendance {

    //#{row.isLeave!=null and row.bindings.EffectiveWorkedHours.inputValue!= null and row.bindings.LeaveCancelled.inputValue==null?'true':'false'}

    //BINDING COMPONENT'S REFERENCES OF ATTENDANCE.JSPX PAGE
    private boolean showCasualDatesPicker;
    private boolean showAnnualDatesPicker;
    private RichSelectOneChoice leaveTypeLOV;
    private RichSelectBooleanCheckbox cbIsHalf;

    private RichInputDate casualLeaveDate;
    private RichInputText leaveTypeId;
    private RichInputDate annualLeaveFromDate;
    private RichInputDate annualLeaveToDate;
    private RichCommandButton logExceptionButton;


    private RichTable attTable;
    private RichInputText itPolicyException;
    private RichSelectOneChoice lovLeaveBalanceYear;

    //CLASS LEVEL VARIABLES
    private String dummy = "hello";
    private String leaveType;
    private RichInputDate casualLeaveEndDate;

    private static final int THRESHOLD_CASUAL_LEAVE_COUNT = 3;
    private static final int THRESHOLD_ANNUAL_LEAVE_COUNT = 3;
    private static final String ANNUAL_LEAVE = "ANNUAL";
    private static final String CASUAL_LEAVE = "CASUAL";
    private static final String TRAVEL_LEAVE = "TRAVEL_LEAVE";
    private static final String HALF_CASUAL_LEAVE = "HALF_CASUAL";
    private static final String HALF_ANNUAL_LEAVE = "HALF_ANNUAL";
    private static final String APPROVE_LEAVES = "approve_leave";
    private static final String INTIMATE_SUPERVISOR = "intimate";
    private RichSelectOneChoice monthLov;
    private RichSelectOneChoice yearLOV;
    private RichTable irgTable;
    private Map<String, Integer> leaves;
    private RichPanelBox attendancePanel;
    private HtmlInputText sal_month;
    private HtmlInputText sal_grossSal;
    private RichSelectOneChoice sal_monthLov;
    private RichSelectOneChoice sal_yearLov;
    private RichSelectOneChoice testLov;

    private String var_sal_month;
    private String var_sal_gross;
    private String var_sal_basic;
    private String var_sal_eobi;
    private String var_sal_hrent;
    private String var_sal_netsalary;
    private String var_sal_pfund;
    private String var_sal_util;
    private String var_sal_itax;
    private String var_sal_allowence;
    private String var_sal_total_deduction;
    private RichOutputText curr_month;
    private RichInputText postingMonth;
    private RichInputText curr_Year;
    private RichOutputText totalMissedTimeStr;
    /*#######################################################*/
    /*#####################THE CONSTRUCTOR###################*/
    /*#######################################################*/

    public Attendance() {
        showCasualDatesPicker = true;
        showAnnualDatesPicker = false;
        leaves = new HashMap<String, Integer>();
        leaves.put(CASUAL_LEAVE, 1);
        leaves.put(ANNUAL_LEAVE, 2);
        leaves.put(TRAVEL_LEAVE, 7);
    }

    /*##################################################################
     *                           LEAVE TYPE CHANGED
     *                           ------------------
     *THIS METHOD GETS CALLED WHEN USER CHANGED THE LEAVE TYPE LOV
     *                                                                 */

    public void leaveTypeChanged(ValueChangeEvent valueChangeEvent) {

        CommonUtil.setvalueToExpression("#{bindings.LeaveType1.inputValue}",
                                        valueChangeEvent.getNewValue()); //Updating Model Values
        String selectedCode =
            CommonUtil.getValueFrmExpression("#{bindings.LeaveType1.attributeValue}").toString();
        leaveType = selectedCode;

        //TOGGLING THE VIEW I.E. SHOWING TWO DATE PICKER IF IT'S ANNUAL
        //AND SHOWING JUST ONE DATE PICKER IF IT'S CASUAL
        if (selectedCode.toUpperCase().equals("ANNUAL")) {
            setShowCasualDatesPicker(false);
            setShowAnnualDatesPicker(true);
            CommonUtil.log("Show Annual Date picker");
        } else if (selectedCode.toUpperCase().equals("CASUAL")) {
            setShowCasualDatesPicker(true);
            setShowAnnualDatesPicker(false);
            CommonUtil.log("Show Casual Date Pciker");
        } else if (selectedCode.toUpperCase().equals("TRAVEL")) {
            setShowCasualDatesPicker(false);
            setShowAnnualDatesPicker(true);
            CommonUtil.log("Show Casual Date Pciker");
        }

    }

    /*######################################################################
     *                         TASK APPLY FOR LEAVES
     *                         ----------------
     *THIS METHOD GETS CALLED WHEN USER APPLIES FOR LEAVES.EITHER CASUAL LEAVES
     * OR ANNUAL LEAVES.
     * ######################################################################
     *                                                                      */

    public String applyForLeave() {
        //GETTING THE USER ID. FOR TESTING PURPOSES I AM HARDCOADING THE VALUE
        //String userid = "3721";
        String userid =
            CommonUtil.getSessionValue(Constants.SESSION_USERID).toString();

        //GET THE LEAVE TYPE THAT USE  HAS SELECTED FROM THE LOV
        String type =
            CommonUtil.getValueFrmExpression("#{bindings.LeaveType.attributeValue}").toString();

        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl voAttendance = am.getVO_Attendance2();

        //IF THE LEAVE SELECTED BY THE USER IS CASUAL
        if (type.equals("CASUAL")) {
            
            java.util.Date dStart = (java.util.Date)casualLeaveDate.getValue();
            java.util.Date dEnd =
                (java.util.Date)casualLeaveEndDate.getValue();

            List<java.util.Date> leaveDays =
                CommonUtil.getDatesBetween(dStart, dEnd);
            
            
    //            //CommonUtil.log("Casual Leaves Applied for "+daysBetween);
    //            //CommonUtil.log("IS half flag: " + isHalf);
    //
            if (leaveDays.size() > THRESHOLD_CASUAL_LEAVE_COUNT) {
                CommonUtil.log("Attendance.java: Leaves Sent for approval");
                int leavesRemaining = getRemainingLeaves(1,userid);
                if (leavesRemaining < THRESHOLD_CASUAL_LEAVE_COUNT) {
                    CommonUtil.showMessage("You do not have sufficient leaves in your accout to proceed", 112);
                }
                else{
                    //CommonUtil.showMessage("You have leave remaining in your account", 112);
                    processLeaves(dStart, dEnd, CASUAL_LEAVE, APPROVE_LEAVES);
                }
                //
            } else {
                //CommonUtil.log("Attendance.java: Deducting leaves");
                int leavesRemaining = getRemainingLeaves(1,userid);
                if (leavesRemaining < 1) {
                    CommonUtil.showMessage("You do not have sufficient leaves in your accout to proceed", 112);
                }
                else{
    //                    CommonUtil.showMessage("You have leave remaining in your account", 112);
                    processLeaves(dStart, dEnd, CASUAL_LEAVE, INTIMATE_SUPERVISOR);
                }
                
            }

        } else if (type.equals("ANNUAL")) {
            //GET THE TWO DATES (DATE-FROM AND DATE-TO)
            java.util.Date dStart =
                (java.util.Date)annualLeaveFromDate.getValue();
            java.util.Date dEnd = (java.util.Date)annualLeaveToDate.getValue();

            List<java.util.Date> leaveDays =
                CommonUtil.getDatesBetween(dStart, dEnd);
            //CommonUtil.log("Casual Leaves Applied for "+daysBetween);
            //CommonUtil.log("IS half flag: " + isHalf);

            if (leaveDays.size() > THRESHOLD_ANNUAL_LEAVE_COUNT) {
                int leavesRemaining = getRemainingLeaves(1,userid);
                if (leavesRemaining < THRESHOLD_ANNUAL_LEAVE_COUNT) {
                    CommonUtil.showMessage("You do not have sufficient leaves in your accout to proceed", 112);
                }
                else{
                //CommonUtil.showMessage("You have leave remaining in your account", 112);
                    if (CommonUtil.getSessionValue(Constants.SESSION_ACCESS_LEVEL).toString().equals("HOD")) {
                    CommonUtil.log("**************************************************************************");
                        processLeaves(dStart, dEnd, ANNUAL_LEAVE, INTIMATE_SUPERVISOR);   
                    }else{
                processLeaves(dStart, dEnd, ANNUAL_LEAVE, APPROVE_LEAVES);
                    }
                }
            } else {
                int leavesRemaining = getRemainingLeaves(1,userid);
                if (leavesRemaining < 1) {
                    CommonUtil.showMessage("You do not have sufficient leaves in your accout to proceed", 112);
                }
                else{
                //CommonUtil.showMessage("You have leave remaining in your account", 112);
                    processLeaves(dStart, dEnd, ANNUAL_LEAVE, INTIMATE_SUPERVISOR);
                }
                
                
            }
        }else if (type.equals("TRAVEL")) {
            java.util.Date startDate = (java.util.Date)annualLeaveFromDate.getValue();
            java.util.Date endDate = (java.util.Date)annualLeaveToDate.getValue();
            
            List<java.util.Date> leaveDays = CommonUtil.getDatesBetween(startDate, endDate);
            processLeaves(startDate, endDate, TRAVEL_LEAVE, APPROVE_LEAVES);
            
        }
        return null;
    }

    /*############# UNUSED METTHOD ##############################################*/

    public void attributeChangeListener(AttributeChangeEvent attributeChangeEvent) {
    }


    public void leaveBalanceYearChanged(ValueChangeEvent valueChangeEvent) {
        CommonUtil.setvalueToExpression("#{bindings.Year.inputValue}",
                                        valueChangeEvent.getNewValue()); //Updating Model Values
        String selectedCode =
            CommonUtil.getValueFrmExpression("#{bindings.Year.attributeValue}").toString();
    }

    /**********************************************************
     *                   TASK POST ATTENDANCE                      *
     *                   ---------------                      *
     * BELOW METHOD GETS CALLED WHEN EMPLOYEE PRESSED THE POST*
     * BUTTON ON ATTENDANCE.JSPX.                             *
     * ********************************************************
     *                                                        *
     *                                                        */
    public String postAttendance() {
        /***********************************************************
         *      EMPLOYEE MONTHLY ATTENDANCE POSTING STARTS         *
         *      ------------------------------------------         *
         * HERE IN THIS METHOD WE ARE POSTING THE ATTENDANCE OF EMP*
         * LOYEE. THE POSTING INCLUDES THE FOLLWOING STEPS:        *
         * 1:-UPDATE THE LEAVE BALANCE                             *
         * 2:-GENERATE NEXT MONTH'S ATTENDANCE                     *
         * 3:-UPDATE THE POSTING FLAG IN THE DATABASE              *
         * BUT IF HE HASN'T LOGGED ONE OR MORE OF HIS DAYS THEN HE *
         * WILL NOT BE ALLOWED TO POST HIS ATTENDANCE.             *
         * *********************************************************
         *                                                         */


        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        int shortLeaveCount = 0;
        int halfHourRelaxation = 0;
        int halfLeaveCount = 0;
        int casualLeaveCount = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        /** FIND THE DEFAULTED DAYS AND SHOW EMPLOYEE A MESSAGE IF HE TRIES
         * TO POST ATTENDANCE WITH THESE DEFAULTED DAYS*/
        ViewObjectImpl voDefaultedDays = am.getVO_Attendance2();
        voDefaultedDays.setWhereClause("((REGEXP_SUBSTR (effective_worked_hours,'[^:]+',1,1)* 60)+ (REGEXP_SUBSTR (effective_worked_hours,'[^:]+',1,2)) IS NULL OR (REGEXP_SUBSTR (effective_worked_hours,'[^:]+',1, 1)* 60)+ (REGEXP_SUBSTR (effective_worked_hours,'[^:]+',1,2)) < 390) and policy_exception_approved_flag is null and leave_approved_flag is null");
        voDefaultedDays.executeQuery();
        List<String> defaultedDays = new ArrayList<String>();
        RowSetIterator rsiDefaultedDays =
            voDefaultedDays.createRowSetIterator(null);
        Row[] defaultedRows = null;
        int pages = rsiDefaultedDays.getEstimatedRangePageCount();
        if (pages > 1) {
            defaultedRows = rsiDefaultedDays.getNextRangeSet();
        } else {
            defaultedRows = rsiDefaultedDays.getAllRowsInRange();
        }
        while (defaultedRows.length > 0) {
            for (int i = 0; i < defaultedRows.length; i++) {
                defaultedDays.add(defaultedRows[i].getAttribute("AttendanceDate").toString());
            }
            defaultedRows = rsiDefaultedDays.getNextRangeSet();
        }
        voDefaultedDays.setWhereClause(null);
        voDefaultedDays.executeQuery();

        /**CHECK FOR FAULTS IN THE ATTENDANCE*/
        if (defaultedDays.size() <= 0) {
            ViewObjectImpl voAttendance = am.getVO_Attendance2();
            voAttendance.executeQuery();
            RowSetIterator rsiAttendance =
                voAttendance.createRowSetIterator(null);

            Row[] rowsAttendance = rsiAttendance.getNextRangeSet();

            /*START OF THE LEAVE CALCULATION LOOP*/
            while (rowsAttendance.length > 0) {
                for (int i = 0; i < rowsAttendance.length; i++) {
                    /************************COUNT LEAVES ON THE BASIS OF EARLY OUT AND LATE IN.*******
                 *                       HERE GOES THE CONDITIONS:
                 *   0 > LATE IN AND EARLY OUT  <30  = RELAXATION
                 *   31 > LATE IN AND EARLY OUT <90  = SHORT LEAVE
                 *        LATE IN AND EARLY OUT >90  = HALF LEAVE**********************************/

                    int lateIn = 0;
                    int earlyOut = 0;
                    if (rowsAttendance[i].getAttribute("LateIn") != null) {
                        lateIn =
                                CommonUtil.convertTimeStringToMin(rowsAttendance[i].getAttribute("LateIn").toString());
                    }
                    if (rowsAttendance[i].getAttribute("EarlyOut") != null) {
                        earlyOut =
                                CommonUtil.convertTimeStringToMin(rowsAttendance[i].getAttribute("EarlyOut").toString());

                    }
                    Object leaveFlag =
                        rowsAttendance[i].getAttribute("LeaveApprovedFlag");
                    Object exceptionFlag =
                        rowsAttendance[i].getAttribute("PolicyExceptionApprovedFlag");
                    /**NOW I THINK ON A SINGLE DAY AN EMPLOYEE CAN UTILIZE EITHER
                 *LATE IN OR EARLY OUT. IF THAT'S THE CASE THEN I AM SUMMING UP
                 *THE TOTAL TIME OF LATE IN AND EARLY OUT AND COMPARE IT TO 30 MIN
                 */

                    int total = lateIn + earlyOut;
                    if (total > 0 && total <= 30) {
                        /**THESE ARE LATE INS. INCREMENT +1 */
                        halfHourRelaxation += 1;
                        System.out.println("incrementing latein earlyout: " +
                                           halfHourRelaxation);
                    } else if (total > 30 && total <= 90) {
                        shortLeaveCount += 1;
                        System.out.println("incrementing short leave: " +
                                           shortLeaveCount);
                    } else if (total > 90 && total <= 240) {
                        if (leaveFlag == null && exceptionFlag == null) {
                            halfLeaveCount += 1;
                            System.out.println("incrementing half leave: " +
                                               halfLeaveCount);
                        }
                    } else if (total > 240) {
                        if (leaveFlag == null && exceptionFlag == null) {
                            casualLeaveCount += 1;
                        }
                    }
                }
                rowsAttendance = rsiAttendance.getNextRangeSet();
            } //END OF THE LEAVE CALCULATION LOOP

            System.out.println("short leaves: " + shortLeaveCount +
                               " half leave: " + halfLeaveCount);
            /*************************HERE GOES THE LEAVE UPDATION TAKS.****************
         * HERE I AM GOING TO INCREASE/UPDATE SHORT LEAVES , HALF LEAVES AND LATE IN
         * AND EARLY OUTS.*/

            ViewObjectImpl voLeaveBalance = am.getVO_EmployeeLeaveBalance1();
            Row shortLeaveRow = null;
            Row relaxationTimeRow = null;
            Row halfLeaveRow = null;
            RowSetIterator rsiLeaves = null;

            /*IF PAGE COUNT IS 1 THEN USE rsiLeaves.getAllRowsInRange();*/
            //int pageCount = rsiLeaves.getEstimatedRangePageCount();

            /*UPDATE LATE INS*/
            voLeaveBalance.setWhereClause("LEAVE_TYPE_ID = 4 and MONTH = 'Apr'");
            voLeaveBalance.executeQuery();
            rsiLeaves = voLeaveBalance.createRowSetIterator(null);
            if (rsiLeaves.getAllRowsInRange().length > 0) {
                relaxationTimeRow = rsiLeaves.getAllRowsInRange()[0];

                if (relaxationTimeRow != null) {
                    relaxationTimeRow.setAttribute("AvailedLeaves",
                                                   Integer.parseInt(relaxationTimeRow.getAttribute("AvailedLeaves").toString()) +
                                                   halfHourRelaxation);
                    System.out.println("updating late in in if " +
                                       halfHourRelaxation);
                }
            } else {
                /*NO LATE INS ARE GIVEN TO EMPLOYEE. HE MAY BE IN HIS PROBATION OF GOD KNOWS
             * WHAT. . .*/
                Row newLeaveRow = voLeaveBalance.createRow();
                newLeaveRow.setAttribute("LeaveBalanceId",
                                         am.getLeaveSequence().intValue());
                newLeaveRow.setAttribute("UserId",
                                         "3562"); //HARD COADING FOR NOW OTHERWISE USE SESSION VALUE
                newLeaveRow.setAttribute("LeaveTypeId",
                                         4); //4 FOR LATE INS AND EARLY OUTS
                newLeaveRow.setAttribute("TotalLeaves", "0");
                newLeaveRow.setAttribute("AvailedLeaves", halfHourRelaxation);
                newLeaveRow.setAttribute("Month", "Apr");
                //CommonUtil.getCurrent(CommonUtil.CURR_MONTH_NAME));
                newLeaveRow.setAttribute("Year",
                                         Calendar.getInstance().get(Calendar.YEAR));
                voLeaveBalance.insertRow(newLeaveRow);
                voLeaveBalance.executeQuery();
                System.out.println("updating late in in else" +
                                   halfHourRelaxation);
            }


            voLeaveBalance.setWhereClause(null);
            voLeaveBalance.executeQuery();

            voLeaveBalance.setWhereClause("LEAVE_TYPE_ID = 3 and MONTH = 'Apr'");
            voLeaveBalance.executeQuery();
            System.out.println("All rows in range: " +
                               voLeaveBalance.getAllRowsInRange().length);

            rsiLeaves = voLeaveBalance.createRowSetIterator(null);

            if (rsiLeaves.getAllRowsInRange().length > 0) {
                shortLeaveRow = rsiLeaves.getAllRowsInRange()[0];
                if (shortLeaveRow != null) {
                    shortLeaveRow.setAttribute("AvailedLeaves",
                                               Integer.parseInt(shortLeaveRow.getAttribute("AvailedLeaves").toString()) +
                                               shortLeaveCount);
                    System.out.println("updating short leaves in if:" +
                                       shortLeaveCount);
                }
            } else {
                /*NO SHORT LEAVE IS GIVEN TO THE EMPLOYEE.
             * EITHER HE IS IN HIS PROBATION PERIOD OR GOD KNOWS WHAT
             * CREATE A ROW AND INCREASE AVAILED LEAVE*/
                Row newLeaveRow = voLeaveBalance.createRow();
                newLeaveRow.setAttribute("LeaveBalanceId",
                                         am.getLeaveSequence().intValue());
                newLeaveRow.setAttribute("UserId",
                                         "3562"); //HARD COADING FOR NOW OTHERWISE USE SESSION VALUE
                newLeaveRow.setAttribute("LeaveTypeId", 3); //3 FOR SHORT LEAVE
                newLeaveRow.setAttribute("TotalLeaves", "0");
                newLeaveRow.setAttribute("AvailedLeaves", shortLeaveCount);
                newLeaveRow.setAttribute("Month", "Apr");
                //CommonUtil.getCurrent(CommonUtil.CURR_MONTH_NAME));
                newLeaveRow.setAttribute("Year",
                                         Calendar.getInstance().get(Calendar.YEAR));
                voLeaveBalance.insertRow(newLeaveRow);
                voLeaveBalance.executeQuery();
                System.out.println("updating short leave in else" +
                                   shortLeaveCount);
            }
            voLeaveBalance.setWhereClause(null);
            voLeaveBalance.executeQuery();


            /*UPDATE HALF LEAVES*/
            voLeaveBalance.setWhereClause("LEAVE_TYPE_ID = 1 and MONTH = 'Apr'");
            voLeaveBalance.executeQuery();
            rsiLeaves = voLeaveBalance.createRowSetIterator(null);
            if (rsiLeaves.getAllRowsInRange().length > 0) {
                halfLeaveRow = rsiLeaves.getAllRowsInRange()[0];
                if (halfLeaveRow != null) {
                    halfLeaveRow.setAttribute("AvailedLeaves",
                                              Float.parseFloat(halfLeaveRow.getAttribute("AvailedLeaves").toString()) +
                                              halfLeaveCount / 2f +
                                              casualLeaveCount);
                    System.out.println("updating half leave in if" +
                                       halfLeaveCount);
                }
            } else {
                /*NO HALF LEAVE IS GIVEN TO USER. HE MAY BE IN PROBATION OR
             * GOD KNOWS WHAT. . .*/

                Row newLeaveRow = voLeaveBalance.createRow();
                newLeaveRow.setAttribute("LeaveBalanceId",
                                         am.getLeaveSequence().intValue());
                newLeaveRow.setAttribute("UserId",
                                         "3562"); //HARD COADING FOR NOW OTHERWISE USE SESSION VALUE
                newLeaveRow.setAttribute("LeaveTypeId", 1); //1 CASUAL LEAVES
                newLeaveRow.setAttribute("TotalLeaves", "0");
                newLeaveRow.setAttribute("AvailedLeaves",
                                         halfLeaveCount / 2f + casualLeaveCount);
                newLeaveRow.setAttribute("Month", "Apr");
                newLeaveRow.setAttribute("Year",
                                         Calendar.getInstance().get(Calendar.YEAR));
                voLeaveBalance.insertRow(newLeaveRow);
                voLeaveBalance.executeQuery();
                System.out.println("updating half leave in else" +
                                   halfLeaveCount);
            }
            voLeaveBalance.setWhereClause(null);
            voLeaveBalance.executeQuery();


            /****** UPDATION OF LEAVES DONE! ********/


            /**************HERE GOES THE GENERATION OF NEXT MONTH TASK*********
         *                                                              PISSED
         * SELECT ATTENDANCE OF 2 MONTHS FROM NOW.
         *                                                                */

            ViewObjectImpl voEmptyAtt = am.getVO_EmptyAttendance1();
            voEmptyAtt.setNamedWhereClauseParam("step_back_days",
                                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            voEmptyAtt.setNamedWhereClauseParam("from_month", "3");
            voEmptyAtt.setNamedWhereClauseParam("inlcude_previous_day", "0");
            voEmptyAtt.executeQuery();
            RowSetIterator rsiEmpty = voEmptyAtt.createRowSetIterator(null);
            Row[] rowsEmpty = rsiEmpty.getNextRangeSet();
            List<EmptyAttendance> emptyAttendance =
                new ArrayList<EmptyAttendance>();

            /*SAVE EMPTY ATTENDANCES IN AN ARRAYLIST*/
            while (rowsEmpty.length > 0) {
                for (int i = 0; i < rowsEmpty.length; i++) {
                    EmptyAttendance et = new EmptyAttendance();
                    try {
                        System.out.println(sdf.parse(rowsEmpty[i].getAttribute("AttendanceDate").toString()));
                        et.setAttendance_date(sdf.parse(rowsEmpty[i].getAttribute("AttendanceDate").toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    et.setEnd_time(rowsEmpty[i].getAttribute("EndTime").toString());
                    et.setExpected_work_hours(rowsEmpty[i].getAttribute("ExpectedWorkHours").toString());
                    et.setFull_name(rowsEmpty[i].getAttribute("FullName").toString());
                    et.setMax_end_time(rowsEmpty[i].getAttribute("MaxEndTime").toString());
                    et.setMax_start_time(rowsEmpty[i].getAttribute("MaxStartTime").toString());
                    et.setPerson_id(rowsEmpty[i].getAttribute("PersonId").toString());
                    et.setStart_time(rowsEmpty[i].getAttribute("StartTime").toString());
                    emptyAttendance.add(et);
                }
                rowsEmpty = rsiEmpty.getNextRangeSet();
            }

            /*NOW SAVE THE EMPTY ATTENDANCE LIST IN THE DATABASE ATTENDANCE TABLE*/
            for (EmptyAttendance et : emptyAttendance) {
                Row row = voAttendance.createRow();
                System.out.println(et.getAttendance_date().toString());
                row.setAttribute("EmpAtdId",
                                 am.getAttendanceSequence().intValue());
                row.setAttribute("AttendanceDate", et.getAttendance_date());
                row.setAttribute("EndTime", et.getEnd_time());
                row.setAttribute("ExpectedWorkHours",
                                 et.getExpected_work_hours());
                row.setAttribute("EmpName", et.getFull_name());
                row.setAttribute("MaxEndTime", et.getMax_end_time());
                row.setAttribute("MaxStartTime", et.getMax_start_time());
                row.setAttribute("EmpId", et.getPerson_id());
                row.setAttribute("StartTime", et.getStart_time());
                voAttendance.insertRow(row);
            }
            /**NEXT MONTHS EMPTY ATTENDANCE IS DONE***************
        /*

        /**************UPDATE THE POSTING FLAG IN THE DATABASE***************/
            ViewObjectImpl voPosting = am.getVO_AtdPosting1();
            Row rowpost = voPosting.createRow();
            rowpost.setAttribute("AtdPostId",
                                 am.getPostingSequence().intValue());
            rowpost.setAttribute("PostingMonth", "Apr");
            //CommonUtil.getCurrent(CommonUtil.CURR_MONTH_NAME).toString());
            rowpost.setAttribute("UserId",
                                 "3562"); //INSERT THE USER ID WHICH IS IN SESSION. FOR TESTING PURPOSES I AM HARD COADING THE VALUE
            rowpost.setAttribute("EmpPostedFlag", "Y");
            rowpost.setAttribute("HrPostedFlag", null);
            rowpost.setAttribute("PostingYear",
                                 Calendar.getInstance().get(Calendar.YEAR));
            voPosting.insertRow(rowpost);
            voPosting.executeQuery();

            /**COMMIT ALL THE CHANGES NOW**/
            am.getTransaction().commit();
            CommonUtil.refreshVO("VO_LeaveBalanceReport1Iterator");
            CommonUtil.refreshVO("VO_Attendance1Iterator");

        } else {
            StringBuilder dd = new StringBuilder();
            dd.append("<html></body><b><p>Following days need your attention:</p></b>");
            for (int i = 0; i < defaultedDays.size(); i++) {
                dd.append("<p color=red>" + defaultedDays.get(i) + " </p>");
            }
            dd.append("</body></html>");
            CommonUtil.showMessage(dd.toString(), 112);
        }

        /**********************************************************
         *    *     *     END OF EMPLOYEE POSTING            *    *
         *    *     *     ***********************            *    *
         *    *     *                                        *    *
         **********************************************************/

        return null;
    }


    /**#########################################################################################
     *############################## GETTERS AND SETTERS ######################################
     * #######################################################################################*/

    public void setLogExceptionButton(RichCommandButton logExceptionButton) {
        this.logExceptionButton = logExceptionButton;
    }

    public RichCommandButton getLogExceptionButton() {
        return logExceptionButton;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public String getDummy() {
        return dummy;
    }

    public void setAttTable(RichTable attTable) {
        this.attTable = attTable;
    }

    public RichTable getAttTable() {
        return attTable;
    }

    public void setItPolicyException(RichInputText itPolicyException) {
        this.itPolicyException = itPolicyException;
    }

    public RichInputText getItPolicyException() {
        return itPolicyException;
    }

    public void setLovLeaveBalanceYear(RichSelectOneChoice lovLeaveBalanceYear) {
        this.lovLeaveBalanceYear = lovLeaveBalanceYear;
    }

    public RichSelectOneChoice getLovLeaveBalanceYear() {
        return lovLeaveBalanceYear;
    }

    public void setLeaveTypeLOV(RichSelectOneChoice leaveTypeLOV) {
        this.leaveTypeLOV = leaveTypeLOV;
    }

    public RichSelectOneChoice getLeaveTypeLOV() {
        return leaveTypeLOV;
    }

    public void setShowCasualDatesPicker(boolean showCasualDatesPicker) {
        this.showCasualDatesPicker = showCasualDatesPicker;
    }

    public boolean isShowCasualDatesPicker() {
        return showCasualDatesPicker;
    }

    public void setShowAnnualDatesPicker(boolean showAnnualDatesPicker) {
        this.showAnnualDatesPicker = showAnnualDatesPicker;
    }

    public boolean isShowAnnualDatesPicker() {
        return showAnnualDatesPicker;
    }

    public void setCbIsHalf(RichSelectBooleanCheckbox cbIsHalf) {
        this.cbIsHalf = cbIsHalf;
    }

    public RichSelectBooleanCheckbox getCbIsHalf() {
        return cbIsHalf;
    }

    public void setCasualLeaveDate(RichInputDate casualLeaveDate) {
        this.casualLeaveDate = casualLeaveDate;
    }

    public RichInputDate getCasualLeaveDate() {
        return casualLeaveDate;
    }

    public void setLeaveTypeId(RichInputText leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public RichInputText getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setAnnualLeaveFromDate(RichInputDate annualLeaveFromDate) {
        this.annualLeaveFromDate = annualLeaveFromDate;
    }

    public RichInputDate getAnnualLeaveFromDate() {
        return annualLeaveFromDate;
    }


    public void setAnnualLeaveToDate(RichInputDate annualLeaveToDate) {
        this.annualLeaveToDate = annualLeaveToDate;
    }

    public RichInputDate getAnnualLeaveToDate() {
        return annualLeaveToDate;
    }


    public String testMethod() {
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl vo = am.getVO_Attendance2();
        vo.setNamedWhereClauseParam("bv_month", "APR");
        vo.executeQuery();
        vo.setNamedWhereClauseParam("bv_month", null);
        vo.reset();
        vo.executeQuery();

        return null;
    }

    public String testMethod2() {
        // Add event code here...
        System.out.println("in seconed method");
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl vo = am.getVO_Attendance2();
        vo.setNamedWhereClauseParam("bv_month", "JUN");
        vo.executeQuery();
        vo.setNamedWhereClauseParam("bv_month", null);
        vo.reset();
        vo.executeQuery();
        return null;
    }

    /***THIS METHOD WILL BE CALLED FROM APPLY FOR LEAVES METHOD. IF THE LEAVES THAT
     * USER APPLIED ARE 10 OR MORE THAN 10 THEN AN LFA REQUEST WILL AUTOMATICALLY BE
     * CREATED AND WILL BE SENT TO HOD FOR FURTHER PROCESSING */
    private void applyForLfa(int days) {
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl vo = am.getVO_EmpLFARequests1();
        Row lfaRequest = vo.createRow();
        lfaRequest.setAttribute("LfaId", am.getLfaSequence().intValue());
        lfaRequest.setAttribute("EmpId", "3562");
        lfaRequest.setAttribute("SubmissionDate", new java.util.Date());
        lfaRequest.setAttribute("Department",
                                "MIS Department Hierarchy"); //HARD CODING,CommonUtil.getSessionValue(Constants.SESSION_EMP_DPT) OTHERWISE
        lfaRequest.setAttribute("Designation",
                                "Management Trainee-HR Module-MIS Department-NCL-0-0"); //HARD CODING,GET FROM THE SESSION OTHERWISE
        PayHistory payHistory = new PayHistory();
        java.util.Date joiningDate = null;
        try {
            List<HrPayHistory> list =
                payHistory.getPayHistory("001167", "201504");
            if (list.size() == 1) {
                JAXBElement date = list.get(0).getJoining();
                XMLGregorianCalendar d = (XMLGregorianCalendar)date.getValue();
                Calendar cJoiningDate = Calendar.getInstance();
                cJoiningDate.set(Calendar.MONTH, d.getMonth());
                cJoiningDate.set(Calendar.YEAR, d.getYear());
                cJoiningDate.set(Calendar.DAY_OF_MONTH, d.getDay());
                joiningDate = cJoiningDate.getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        lfaRequest.setAttribute("DateOfJoining", joiningDate);
        lfaRequest.setAttribute("ApprovedAmmount", "121212");
        lfaRequest.setAttribute("HodApprovedDate", null);
        lfaRequest.setAttribute("HrApprovedDate", null);
        lfaRequest.setAttribute("NumberOfLeaves", days);
        vo.insertRow(lfaRequest);
        am.getTransaction().commit();
        vo.executeQuery();

    }

    public void setCasualLeaveEndDate(RichInputDate casualLeaveEndDate) {
        this.casualLeaveEndDate = casualLeaveEndDate;
    }

    public RichInputDate getCasualLeaveEndDate() {
        return casualLeaveEndDate;
    }

    /**
     *
     * @param leaveType
     * @param leaveDays
     * @param whatToDo
     */
    private void processLeaves(java.util.Date dStart, java.util.Date dend,
                               String leaveType, String whatToDo) {
        List<java.util.Date> leaveDays =
            CommonUtil.getDatesBetween(dStart, dend);

        if (whatToDo.equals(APPROVE_LEAVES)) {
            //CommonUtil.log("Send approval for the leaves " + leaveDays);
            //            int result =
            //                updateAttendanceLeaveFlag(leaveDays,leaveType);
            //if (result == 1) {
            insertLeave(leaveType, leaveDays, dStart, dend,
                        true); //pass true for approval
            //            } else {
            //                CommonUtil.doNothing();
            //            }
        } else if (whatToDo.equals(INTIMATE_SUPERVISOR)) {
            CommonUtil.log("Intimate User for leave :  " + leaveDays);
            int result = updateAttendanceLeaveFlag(leaveDays, leaveType);
            CommonUtil.log("Result of update attendance leave flag is: " +
                           result);
            if (result == 1) {
                insertLeave(leaveType, leaveDays, dStart, dend,
                            false); //pass false fo just intimatino
            } else {
                CommonUtil.doNothing();
            }
        }
    }

    private int updateAttendanceLeaveFlag(List<java.util.Date> leaveDays,
                                          String leaveType) {

        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl attendanceVO = am.getVO_Attendance2();

        /** Check If there is already a leave applied on one of the days selected by the user*/
        boolean alreadyHas = false;
        CommonUtil.resetWhereClause(attendanceVO);
        for (java.util.Date date : leaveDays) {
            String where =
                "emp_id = '" + CommonUtil.getSessionValue(Constants.SESSION_USERID) +
                "' and to_char(attendance_date,'MONDD') = '" +
                CommonUtil.getMonDD(date) + "' and leave_today = 'Y'";
            CommonUtil.resetWhereClause(attendanceVO);
            attendanceVO.setWhereClause(where);
            attendanceVO.executeQuery();
            int pageCount = attendanceVO.getEstimatedRangePageCount();
            if (pageCount > 0) {
                alreadyHas = true;
                break;
            }
        }
        CommonUtil.resetWhereClause(attendanceVO);


        /**If database don't have any leave applied on any of the date selected by the user then apply*/
        if (!alreadyHas) {
            StringBuilder sbLeaveDaysParam = new StringBuilder();
            for (java.util.Date date : leaveDays) {
                sbLeaveDaysParam.append("'" + CommonUtil.getMonDD(date) + "'" +
                                        ",");
            }
            //            CommonUtil.log(sbLeaveDaysParam.toString().substring(0,
            //                                                                 sbLeaveDaysParam.length() -
            //                                                                 1));
            CommonUtil.resetWhereClause(attendanceVO);
            attendanceVO.executeQuery();
            String where =
                "to_char(attendance_date,'MONDD')  in(" + sbLeaveDaysParam.toString().substring(0,
                                                                                                sbLeaveDaysParam.length() -
                                                                                                1) +
                ")";
            CommonUtil.log("Select Rows to update query:" + where);
            attendanceVO.setWhereClause("to_char(attendance_date,'MONDD')  in(" +
                                        sbLeaveDaysParam.toString().substring(0,
                                                                              sbLeaveDaysParam.length() -
                                                                              1) +
                                        ")");

            String query =
                "to_char(attendance_date,'MONDD')  in(" + sbLeaveDaysParam.toString().substring(0,
                                                                                                sbLeaveDaysParam.length() -
                                                                                                1) +
                ")";

            CommonUtil.log(query);

            attendanceVO.executeQuery();
            RowSetIterator rsi = attendanceVO.createRowSetIterator(null);
            int pageCount = rsi.getEstimatedRangePageCount();
            CommonUtil.log("Page Count: " + pageCount);
            Row[] rows = null;
            if (pageCount > 1) {
                rows =
rsi.getNextRangeSet(); /**Test this statement by applying for just 1eave*/
                CommonUtil.log("more then 1 row are there to update");
            } else {
                rows = rsi.getAllRowsInRange();
                CommonUtil.log("just 1 row to uipdate: " + rows.length);
            }

            while (rows.length > 0) {
                for (int i = 0; i < rows.length; i++) {
                    String effectiveWorkedHours =
                        rows[i].getAttribute("EffectiveWorkedHours") == null ?
                        "0:00" :
                        rows[i].getAttribute("EffectiveWorkedHours").toString();
                    Object leave_today = rows[i].getAttribute("LeaveToday");
                    String expectedWorkHours =
                        rows[i].getAttribute("ExpectedWorkHours").toString();
                    int minutesMissing =
                        (Integer.parseInt(expectedWorkHours.split(":")[0]) *
                         60 +
                         Integer.parseInt(expectedWorkHours.split(":")[1])) -
                        Integer.parseInt(effectiveWorkedHours.split(":")[0]) *
                        60 +
                        Integer.parseInt(effectiveWorkedHours.split(":")[1]);

                    int missingMinutes = 0;

                    if (minutesMissing > 240 && leave_today == null) {
                        rows[i].setAttribute("LeaveToday", "Y");
                        CommonUtil.log(leaves.get(leaveType) + " - " +
                                       leaveType);
                        rows[i].setAttribute("TypeOfLeave",
                                             leaves.get(leaveType));
                        CommonUtil.log("update flag for row # " + i);
                    } else {
                        CommonUtil.resetWhereClause(attendanceVO);
                        CommonUtil.showMessage("Wrong day to log a leave,you were present on this day.",
                                               111);
                        return 0;
                    }
                }
                rows = rsi.getNextRangeSet();
                //CommonUtil.log("got next range set of attendance");
            }
            attendanceVO.executeQuery();
            am.getTransaction().commit();
            CommonUtil.resetWhereClause(attendanceVO);
            return 1;
        } else {
            CommonUtil.showMessage("Leave Already Applied", 112);
            return 0;
        }

    }

    private void insertLeave(String leaveType, List<java.util.Date> leaveDays,
                             java.util.Date start, java.util.Date end,
                             boolean approvalRequired) {
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl leavesVO = am.getVO_EmpLeaves1();
        ViewObjectImpl consumedLeavesVO = am.getVO_ConsumedLeaves1();
        PolicyHelper ph = new PolicyHelper();

        /**
         * Insert leaves
         */

        Row leave = leavesVO.createRow();
        leave.setAttribute("LeaveId", am.getLeaveSequence());
        leave.setAttribute("LeaveType", ph.getLeaves().get(leaveType));
        leave.setAttribute("AprovedFlag", "");
        leave.setAttribute("ApprovalRequired", approvalRequired ? "Y" : "");
        leave.setAttribute("StartDate",
                           CommonUtil.convertFromJAVADateToSQLDate(start));
        leave.setAttribute("EndDate",
                           CommonUtil.convertFromJAVADateToSQLDate(end));
        leave.setAttribute("TotalLeaves", leaveDays.size());
        leave.setAttribute("UserId",
                           CommonUtil.getSessionValue(Constants.SESSION_USERID));
        leavesVO.insertRow(leave);
        leavesVO.executeQuery();
        am.getTransaction().commit();
        //CommonUtil.log("Everything Cool here. . . ");


        /**
         * If approval is not required then deduct the leave balance
         */
        if (!approvalRequired) {
            //CommonUtil.log("Attendance.java: Approval is not required. Deduct leaves");
            //deduction here
            try {
                ph.deductLeaves(PolicyHelper.NO_APPROVAL_REQUIRED, leaveType,
                                leaveDays,
                                CommonUtil.getSessionValue(Constants.SESSION_EMP_CODE).toString(),
                                CommonUtil.getSessionValue(Constants.SESSION_USERID).toString(),
                                am);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


    }

    public void monthChangedListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        CommonUtil.setvalueToExpression("#{bindings.Month.inputValue}",
                                        valueChangeEvent.getNewValue()); //Updating Model Values
        String selectedMonth =
            CommonUtil.getValueFrmExpression("#{bindings.Month.attributeValue}").toString();
        String selectedYear =
            CommonUtil.getValueFrmExpression("#{bindings.Year.attributeValue}").toString();
        //CommonUtil.log(selectedMonth + " - " + selectedYear);
        CommonUtil.createUserSession("yearmonth",
                                     CommonUtil.getMonthNumber(selectedMonth) +
                                     "-" + selectedYear);
        CommonUtil.log("yearmonth = "+CommonUtil.getSessionValue("yearmonth").toString());
        //MonthlyDeductionsDetails dc =  (MonthlyDeductionsDetails)CommonUtil.getCustomDataControl("MonthlyDeductionDetails")

        filterAttenedance(selectedMonth, selectedYear);
    }

    public void yearChangedListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        CommonUtil.setvalueToExpression("#{bindings.Year.inputValue}",
                                        valueChangeEvent.getNewValue()); //Updating Model Values
        String selectedYear =
            CommonUtil.getValueFrmExpression("#{bindings.Year.attributeValue}").toString();
        String selectedMonth =
            CommonUtil.getValueFrmExpression("#{bindings.Month.attributeValue}").toString();
        CommonUtil.log("yearmonth = "+selectedMonth+"-"+selectedYear);
        filterAttenedance(selectedMonth, selectedYear);
    }

    public void setMonthLov(RichSelectOneChoice monthLov) {
        this.monthLov = monthLov;
    }

    public RichSelectOneChoice getMonthLov() {
        return monthLov;
    }

    public void setYearLOV(RichSelectOneChoice yearLOV) {
        this.yearLOV = yearLOV;
    }

    public RichSelectOneChoice getYearLOV() {
        return yearLOV;
    }

    private void filterAttenedance(String selectedMonth, String selectedYear) {
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl voAttendance = am.getVO_Attendance2();
        //CommonUtil.resetWhereClause(voAttendance);
        CommonUtil.refreshVO("VO_Attendance2Iterator");
        voAttendance.setNamedWhereClauseParam("bv_month", null);
        voAttendance.setNamedWhereClauseParam("bv_year", null);
        voAttendance.executeQuery();

        //        String whereClause =
        //            "to_char(attendance_date,'MONYYYY') = '" + selectedMonth.toUpperCase() +
        //            selectedYear.toUpperCase() + "'";
        //        CommonUtil.log(whereClause);
        //voAttendance.setWhereClause(whereClause);

        CommonUtil.refreshVO("VO_Attendance2Iterator");

        voAttendance.setNamedWhereClauseParam("bv_month", selectedMonth);
        voAttendance.setNamedWhereClauseParam("bv_year", selectedYear);
        voAttendance.executeQuery();

        //        CommonUtil.refreshVO("VO_Attendance2Iterator");

    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public String saveException() {
        commitAction();
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl irregularities = am.getVO_AttendanceEvents2();
        Row row = getCurrentExceptionTableRow();
        if (row != null) {
            java.util.Date attendanceDate =
                CommonUtil.convertJBODateToJavaDate((Date)row.getAttribute("AttendanceDate"));
            Object exception = row.getAttribute("ExceptionRemarks");

            String exceptionStr =
                exception == null ? null : exception.toString();
            if (exceptionStr != null) {
                CommonUtil.log(exceptionStr);
                generateExceptionRequest(row);
                //                ApprovalNotifier hn = new ApprovalNotifier();
                //                hn.sendNotification(CommonUtil.getSessionValue(Constants.SESSION_PARENT_EMAIL).toString(), "<h1 style=color:RED>You have some excuses to approve!!</h1>");
            }


        } else {
            CommonUtil.doNothing();
        }


        //irregularities.setWhereClause("");
        return null;
    }

    private String commitAction() {
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        Object result = operationBinding.execute();
        if (!operationBinding.getErrors().isEmpty()) {
            return null;
        }
        return null;
    }

    public Row getCurrentExceptionTableRow() {
        HubModuleImpl am = ((HubModuleImpl)CommonUtil.getAppModule());
        /**GET THE SELECTED ROW KEY SET OF THE TABLE FROM THE PAGE
         * AND THEN GET THE KEY THAT WE WILL USE TO FIND THE ROW
         * FROM THE VIEW OBJECT*/
        RowKeySet selectedEmps = getIrgTable().getSelectedRowKeys();
        Iterator selectedEmpIter = selectedEmps.iterator();
        Key key = (Key)((List)selectedEmpIter.next()).get(0);

        /**GET THE SELECTED VIEW OBJECT ROW USING THE KEY*/
        ViewObjectImpl vo = am.getVO_AttendanceEvents2();
        vo.executeQuery();
        Row currentRow = vo.getRow(key);
        return currentRow;
    }


    public void setIrgTable(RichTable irgTable) {
        this.irgTable = irgTable;
    }

    public RichTable getIrgTable() {
        return irgTable;
    }

    private void generateExceptionRequest(Row row) {
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl exception = am.getVO_ExceptionReq1();
        Row r = exception.createRow();
        r.setAttribute("AttendanceDate", row.getAttribute("AttendanceDate"));
        r.setAttribute("ExceptionRequestDate", new java.util.Date());
        r.setAttribute("ExceptionRequestId", am.getExceptionReqSequence());
        r.setAttribute("IrregularityId", row.getAttribute("IrregularityId"));
        r.setAttribute("EmpAtdId", row.getAttribute("EmpAtdId"));
        r.setAttribute("EmpId", row.getAttribute("EmpId"));
        r.setAttribute("ExceptionCause", row.getAttribute("Type"));
        r.setAttribute("ExceptionRemarks",
                       row.getAttribute("ExceptionRemarks"));
        r.setAttribute("MissingMinutes", row.getAttribute("MinutesMissing"));
        if (CommonUtil.getSessionValue(Constants.SESSION_ACCESS_LEVEL).toString().equals("HOD")) {
            r.setAttribute("Approved", "Y");
        } else {
            r.setAttribute("Approved", "P");
        }

        exception.insertRow(r);

        String irrID = row.getAttribute("IrregularityId").toString();
        ViewObjectImpl voEvents = am.getVO_AttendanceEvents1();
        CommonUtil.resetWhereClause(voEvents);
        voEvents.setWhereClause("IRREGULARITY_ID = '" + irrID + "'");
        voEvents.executeQuery();

        Row irregularity =
            voEvents.createRowSetIterator(null).getAllRowsInRange()[0];
        if (CommonUtil.getSessionValue(Constants.SESSION_ACCESS_LEVEL).toString().equals("HOD")) {
            irregularity.setAttribute("ExceptionApproved", "Y");
        } else {
            irregularity.setAttribute("ExceptionApproved", "P");
        }


        am.getTransaction().commit();
    }

    /** TASK Monthly Posting */
    public String post() {

        System.out.println("Hello Selected yearmonth = "+ curr_month.getValue().toString()+"-"+curr_Year.getValue().toString());
        DateFormatSymbols d = new DateFormatSymbols();
        int monthNumber = Integer.parseInt(curr_month.getValue().toString());
        String postMonth = d.getShortMonths()[monthNumber - 1];
        String monthDate =
            ((Integer.parseInt(curr_month.getValue().toString()) <= 9) ?
             "0" + Integer.parseInt(curr_month.getValue().toString()) :
             "" + Integer.parseInt(curr_month.getValue().toString())) + "-" +
            curr_Year.getValue().toString();


        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();


        ViewObjectImpl voPosting = (ViewObjectImpl)am.getVO_AtdPosting1();

        CommonUtil.resetWhereClause(voPosting);

        voPosting.setWhereClause(" user_id = " +
                                 CommonUtil.getSessionValue(Constants.SESSION_USERID) +
                                 " and posting_year = "+Integer.parseInt(curr_Year.getValue().toString())+" and posting_month = '" +
                                 postMonth + "'");

        voPosting.executeQuery();

        int count = voPosting.getEstimatedRangePageCount();
        if (count > 0) {

            CommonUtil.showMessage("Already posted", 111);

            return null;
        }

        //GET THE VIEW OBJECTS REFERENCES
        ViewObjectImpl atdIRR = am.getVO_AttendanceIrregularityV1();
        ViewObjectImpl voConsumedLeaves = am.getVO_ConsumedLeaves1();
        ViewObjectImpl voAttendance = am.getVO_Attendance2();

        //RESET QUERIES
        CommonUtil.resetWhereClause(atdIRR);
        CommonUtil.resetWhereClause(voAttendance);

        //CHECK WEITHER EMPLOYEE HAS ANY PENDING REQUESTS FOR PREVIOUS MONTH
        atdIRR.setWhereClause("emp_id = '" +
                              CommonUtil.getSessionValue(Constants.SESSION_USERID).toString() +
                              "' and to_char(attendance_date,'YYYYMON') = '" +
                              curr_Year.getValue().toString() +
                              postMonth.toUpperCase() +
                              "' AND EXCEPTION_APPROVED = 'P'");
        atdIRR.executeQuery();
        RowSetIterator rsatdIRR = atdIRR.createRowSetIterator(null);
        Row irregularities[] =
            rsatdIRR.getEstimatedRangePageCount() == 1 ? rsatdIRR.getAllRowsInRange() :
            rsatdIRR.getNextRangeSet();
        int totalPending = irregularities.length;
        if (totalPending > 0) {
            CommonUtil.showMessage("Your have some unapproved excuses", 112);
        } else {

            CommonUtil.log("Deducting Leaves for the month: " + monthDate);
            PolicyHelper ph = new PolicyHelper();
            List<MonthlyDeductedLeave> leaves =
                ph.getMonthlyLeaves(monthDate, (HubModuleImpl)CommonUtil.getAppModule());
            CommonUtil.log("Total Leaves to be deducted for the month " +
                           monthDate + " are " + leaves.size());

            for (MonthlyDeductedLeave leave : leaves) {
                Row consumedLeave = voConsumedLeaves.createRow();
                consumedLeave.setAttribute("ConsumedLeavesId",
                                           leave.getLeaveID());
                consumedLeave.setAttribute("LeaveType", leave.getLeaveType());
                consumedLeave.setAttribute("Cause", leave.getLeaveCause());
                consumedLeave.setAttribute("UserId", leave.getUserID());
                consumedLeave.setAttribute("LeaveDate", leave.getLeaveDate());
                consumedLeave.setAttribute("JobStatus", leave.getJobStatus());
                voConsumedLeaves.insertRow(consumedLeave);

                CommonUtil.log("Leave Type:" + leave.getLeaveType() +
                               " Leave Date:" + leave.getLeaveCause());

                //                        /** Also log it in attendance table */
            Calendar c2 = Calendar.getInstance();
                c2.setTime(leave.getLeaveDate());
                int day = c2.get(Calendar.DAY_OF_MONTH);
                int month = c2.get(Calendar.MONTH) + 1;
                int year = c2.get(Calendar.YEAR);
                String param =
                    (day <= 9 ? ("0" + day) : day + "") + (month <= 9 ?
                                                           ("0" + month) :
                                                           month) + year + "";
                String where =
                    "to_char(attendance_date,'DDMMYYYY') = '" + param + "'";
                System.out.println("param = "+param);
                voAttendance.setWhereClause(where);
                voAttendance.executeQuery();
                Row row =
                    voAttendance.createRowSetIterator(null).getAllRowsInRange()[0];
                row.setAttribute("TypeOfLeave", leave.getLeaveType());
                row.setAttribute("LeaveToday", "Y");
            }
            //generateNextMonthsAttendance(); because i am doing it manually
            updatePostingFlag();

            CommonUtil.resetWhereClause(atdIRR);
            CommonUtil.resetWhereClause(voAttendance);

            am.getTransaction().commit();
            CommonUtil.resetWhereClause(voAttendance);
        }


        return null;
    }

    private void generateNextMonthsAttendance() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String postingMonthParam =
            "10-" + getPostingMonth().getValue().toString() + "-2015";
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl voAttendance = am.getVO_Attendance2();
        ViewObjectImpl voEmptyAtt = am.getVO_EmptyAttendance1();

        voEmptyAtt.setNamedWhereClauseParam("person_id", null);
        voEmptyAtt.setNamedWhereClauseParam("postingDate", null);
        voEmptyAtt.executeQuery();

        voEmptyAtt.setNamedWhereClauseParam("person_id",
                                            CommonUtil.getSessionValue(Constants.SESSION_USERID));
        voEmptyAtt.setNamedWhereClauseParam("postingDate", postingMonthParam);

        voEmptyAtt.executeQuery();
        RowSetIterator rsiEmpty = voEmptyAtt.createRowSetIterator(null);
        Row[] rowsEmpty = rsiEmpty.getNextRangeSet();
        List<EmptyAttendance> emptyAttendance =
            new ArrayList<EmptyAttendance>();

        /*SAVE EMPTY ATTENDANCES IN AN ARRAYLIST*/
        while (rowsEmpty.length > 0) {
            for (int i = 0; i < rowsEmpty.length; i++) {
                EmptyAttendance et = new EmptyAttendance();
                try {
                    System.out.println(sdf.parse(rowsEmpty[i].getAttribute("AttendanceDate").toString()));
                    et.setAttendance_date(sdf.parse(rowsEmpty[i].getAttribute("AttendanceDate").toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                et.setEnd_time(rowsEmpty[i].getAttribute("EndTime").toString());
                et.setExpected_work_hours(rowsEmpty[i].getAttribute("ExpectedWorkHours").toString());
                et.setFull_name(rowsEmpty[i].getAttribute("FullName").toString());
                et.setMax_end_time(rowsEmpty[i].getAttribute("MaxEndTime").toString());
                et.setMax_start_time(rowsEmpty[i].getAttribute("MaxStartTime").toString());
                et.setPerson_id(rowsEmpty[i].getAttribute("PersonId").toString());
                et.setStart_time(rowsEmpty[i].getAttribute("StartTime").toString());
                emptyAttendance.add(et);
            }
            rowsEmpty = rsiEmpty.getNextRangeSet();
        }

        /*NOW SAVE THE EMPTY ATTENDANCE LIST IN THE DATABASE ATTENDANCE TABLE*/
        for (EmptyAttendance et : emptyAttendance) {
            Row row = voAttendance.createRow();
            System.out.println(et.getAttendance_date().toString());
            row.setAttribute("EmpAtdId",
                             am.getAttendanceSequence().intValue());
            row.setAttribute("AttendanceDate", et.getAttendance_date());
            row.setAttribute("EndTime", et.getEnd_time());
            row.setAttribute("ExpectedWorkHours", et.getExpected_work_hours());
            row.setAttribute("EmpName", et.getFull_name());
            row.setAttribute("MaxEndTime", et.getMax_end_time());
            row.setAttribute("MaxStartTime", et.getMax_start_time());
            row.setAttribute("EmpId", et.getPerson_id());
            row.setAttribute("StartTime", et.getStart_time());
            voAttendance.insertRow(row);
        }
    }

    private void updatePostingFlag() {
        DateFormatSymbols d = new DateFormatSymbols();
        int monthNumber = Integer.parseInt(curr_month.getValue().toString());
        String postMonth = d.getShortMonths()[monthNumber - 1];
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl voPosting = am.getVO_AtdPosting1();
        String where =
            "user_id = '" + CommonUtil.getSessionValue(Constants.SESSION_USERID) +
            "' and posting_year = '" +
            curr_Year.getValue().toString() +
            "' and posting_month = '" + postMonth + "'";
        CommonUtil.log(where);
        voPosting.setWhereClause(where);
        voPosting.executeQuery();
        int pageCount = voPosting.getEstimatedRangePageCount();

        //  RowSetIterator rsiPosting = voPosting.createRowSetIterator(null);\\
        //   Row rowPosting = rsiPosting.getAllRowsInRange()[0];                \\
        //                                                                        \\
        //                                                                          \\
        //                                                                            \\
        //                                                                              \\
        if (pageCount == 0) {
            Row postingRecord = voPosting.createRow();
            postingRecord.setAttribute("AtdPostId",
                                       am.getPostingSequence().intValue());
            postingRecord.setAttribute("UserId",
                                       CommonUtil.getSessionValue(Constants.SESSION_USERID).toString());
            postingRecord.setAttribute("EmpPostedFlag", "Y");
            postingRecord.setAttribute("HrPostedFlag", null);
            System.out.println("Posting Year = "+curr_Year.getValue().toString() );
            postingRecord.setAttribute("PostingYear",
                                       Integer.parseInt(curr_Year.getValue().toString()));
            postingRecord.setAttribute("PostingMonth", postMonth);
            postingRecord.setAttribute("PostingDate", new java.util.Date());
        } else {
            CommonUtil.showMessage("You already posted attendance of this month",
                                   112);
        }


    }

    public void setAttendancePanel(RichPanelBox attendancePanel) {
        this.attendancePanel = attendancePanel;
    }

    public RichPanelBox getAttendancePanel() {
        return attendancePanel;
    }

    public String cancelLeave() {
        Row row = getCurrentAttendanceTableRow();
        String date = row.getAttribute("AttendanceDate").toString();

        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl voAtt = am.getVO_Attendance2();
        CommonUtil.resetWhereClause(voAtt);
        String where =
            " to_char(attendance_date,'YYYY-mm-dd') = '" + date + "'";

        CommonUtil.log("Attndance.java->cancelLeave: " + where);
        voAtt.setWhereClause(where);
        voAtt.executeQuery();
        Row attRow = voAtt.createRowSetIterator(null).getAllRowsInRange()[0];
        attRow.setAttribute("LeaveCancelled", "Y");
        CommonUtil.resetWhereClause(voAtt);
        voAtt.executeQuery();
        am.getTransaction().commit();
        return null;
    }

    public Row getCurrentAttendanceTableRow() {
        HubModuleImpl am = ((HubModuleImpl)CommonUtil.getAppModule());
        /**GET THE SELECTED ROW KEY SET OF THE TABLE FROM THE PAGE
         * AND THEN GET THE KEY THAT WE WILL USE TO FIND THE ROW
         * FROM THE VIEW OBJECT*/
        RowKeySet selectedEmps = getAttTable().getSelectedRowKeys();

        Iterator selectedEmpIter = selectedEmps.iterator();
        Key key = (Key)((List)selectedEmpIter.next()).get(0);

        /**GET THE SELECTED VIEW OBJECT ROW USING THE KEY*/
        ViewObjectImpl vo = am.getVO_Attendance2();
        vo.executeQuery();
        Row currentRow = vo.getRow(key);
        return currentRow;
    }

    public void setSal_month(HtmlInputText sal_month) {
        this.sal_month = sal_month;
    }

    public HtmlInputText getSal_month() {
        return sal_month;
    }

    public void setSal_grossSal(HtmlInputText sal_grossSal) {
        this.sal_grossSal = sal_grossSal;
    }

    public HtmlInputText getSal_grossSal() {
        return sal_grossSal;
    }

    public String getSal() {
        // Add event code here...
        PayHistory ph = new PayHistory();
        List<HrPayHistory> list;
        String monthCode =
            (sal_yearLov.getValue().toString() + sal_monthLov.getValue().toString())==null?"":sal_yearLov.getValue().toString() + sal_monthLov.getValue().toString();
        String empCode =
            CommonUtil.getSessionValue(Constants.SESSION_EMP_CODE).toString();

        try {
            list = ph.getPayHistory(empCode, monthCode);
            CommonUtil.log("list.Xize()" + list.size() + " empCode: " +
                           empCode + " monthCode: " + monthCode);
            if (list.size() > 0) {

                HrPayHistory hrp = list.get(0);
                CommonUtil.log("has salary " + hrp.getCurGrossSalary());
                //                sal_grossSal.setValue(hrp.getCurGrossSalary());
                //                sal_month.setValue(new DateFormatSymbols().getShortMonths()[(Integer.parseInt(sal_monthLov.getValue().toString())-1)]);
                //setVar_sal_month("What");
                setVar_sal_gross(hrp.getCurGrossSalary().toString());
                setVar_sal_month(new DateFormatSymbols().getShortMonths()[(Integer.parseInt(sal_monthLov.getValue().toString()) -
                                                                           1)]);
                setVar_sal_basic(hrp.getCurGbasic().toString());
                setVar_sal_eobi(hrp.getCurEobi().toString());
                setVar_sal_hrent(hrp.getCurHrent().toString());
                setVar_sal_itax(hrp.getCurHrent().toString());
                setVar_sal_itax(hrp.getCurItax().toString());
                setVar_sal_netsalary(hrp.getCurNetSalary().toString());
                setVar_sal_pfund(hrp.getCurPfund().toString());
                setVar_sal_util(hrp.getCurUtil().toString());
                setVar_sal_allowence(hrp.getCurVAllowances().toString());
                setVar_sal_total_deduction(hrp.getCurVDeductions().toString());
            } else {
                CommonUtil.showMessage("Salary Not Available!", 111);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void salaryBoxListener(DisclosureEvent disclosureEvent) {
        // Add event code here...

        PayHistory ph = new PayHistory();
        List<HrPayHistory> list;

        try {

            int month = (Calendar.getInstance().get(Calendar.MONTH));
            String mC = month <= 9 ? "0" + month : "" + month;
            String monthCode = null;
            if (mC.equals("00")) 
            {
                mC = "12";
                monthCode = (Calendar.getInstance().get(Calendar.YEAR))-1 + mC;
            }
            else monthCode = Calendar.getInstance().get(Calendar.YEAR) + mC;
            String empCode =
                "00"+CommonUtil.getSessionValue(Constants.SESSION_EMP_CODE).toString();
            list = ph.getPayHistory(empCode, monthCode);
            //list =ph.getPayHistory("001327","201512");

            CommonUtil.log("list.Xize()" + list.size() + " empCode: " +
                           empCode + " monthCode: " + monthCode);

            if (list.size() > 0) {
                HrPayHistory hrp = list.get(0);
                //                sal_grossSal.setValue(hrp.getCurGrossSalary());
                //                sal_month.setValue(new DateFormatSymbols().getShortMonths()[(Calendar.getInstance().get(Calendar.MONTH)-1)]);
                setVar_sal_gross(hrp.getCurGrossSalary().toString());
                month = Calendar.getInstance().get(Calendar.MONTH)==0?11:Calendar.getInstance().get(Calendar.MONTH)-1;
                System.out.println("month = "+month);
                setVar_sal_month(new DateFormatSymbols().getShortMonths()[month]);
                setVar_sal_basic(hrp.getCurGbasic().toString());
                setVar_sal_eobi(hrp.getCurEobi().toString());
                setVar_sal_hrent(hrp.getCurHrent().toString());
                setVar_sal_itax(hrp.getCurHrent().toString());
                setVar_sal_itax(hrp.getCurItax().toString());
                setVar_sal_netsalary(hrp.getCurNetSalary().toString());
                setVar_sal_pfund(hrp.getCurPfund().toString());
                setVar_sal_util(hrp.getCurUtil().toString());
                setVar_sal_allowence(hrp.getCurVAllowances().toString());
                setVar_sal_total_deduction(hrp.getCurVDeductions().toString());


            } else {
                CommonUtil.showMessage("Salary Not Available!", 111);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSal_monthLov(RichSelectOneChoice sal_monthLov) {
        this.sal_monthLov = sal_monthLov;
    }

    public RichSelectOneChoice getSal_monthLov() {
        return sal_monthLov;
    }

    public void setSal_yearLov(RichSelectOneChoice sal_yearLov) {
        this.sal_yearLov = sal_yearLov;
    }

    public RichSelectOneChoice getSal_yearLov() {
        return sal_yearLov;
    }

    public void salMonthLovChanged(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        CommonUtil.setvalueToExpression("#{bindings.Month2.inputValue}",
                                        valueChangeEvent.getNewValue()); //Updating Model Values


    }

    public void salYearLovChanged(ValueChangeEvent valueChangeEvent) {
        // Add event fcode here...
        CommonUtil.setvalueToExpression("#{bindings.Year1.inputValue}",
                                        valueChangeEvent.getNewValue()); //Updating Model Values

    }

    public void setTestLov(RichSelectOneChoice testLov) {
        this.testLov = testLov;
    }

    public RichSelectOneChoice getTestLov() {
        return testLov;
    }

    public void setVar_sal_month(String var_sal_month) {
        this.var_sal_month = var_sal_month;
    }

    public String getVar_sal_month() {
        return var_sal_month;
    }

    public void setVar_sal_gross(String var_sal_gross) {
        this.var_sal_gross = var_sal_gross;
    }

    public String getVar_sal_gross() {
        return var_sal_gross;
    }

    public void setVar_sal_basic(String var_sal_basic) {
        this.var_sal_basic = var_sal_basic;
    }

    public String getVar_sal_basic() {
        return var_sal_basic;
    }

    public void setVar_sal_eobi(String var_sal_eobi) {
        this.var_sal_eobi = var_sal_eobi;
    }

    public String getVar_sal_eobi() {
        return var_sal_eobi;
    }

    public void setVar_sal_hrent(String var_sal_hrent) {
        this.var_sal_hrent = var_sal_hrent;
    }

    public String getVar_sal_hrent() {
        return var_sal_hrent;
    }

    public void setVar_sal_netsalary(String var_sal_netsalary) {
        this.var_sal_netsalary = var_sal_netsalary;
    }

    public String getVar_sal_netsalary() {
        return var_sal_netsalary;
    }

    public void setVar_sal_pfund(String var_sal_pfund) {
        this.var_sal_pfund = var_sal_pfund;
    }

    public String getVar_sal_pfund() {
        return var_sal_pfund;
    }

    public void setVar_sal_util(String var_sal_util) {
        this.var_sal_util = var_sal_util;
    }

    public String getVar_sal_util() {
        return var_sal_util;
    }

    public void setVar_sal_itax(String var_sal_itax) {
        this.var_sal_itax = var_sal_itax;
    }

    public String getVar_sal_itax() {
        return var_sal_itax;
    }

    public void setVar_sal_allowence(String var_sal_allowence) {
        this.var_sal_allowence = var_sal_allowence;
    }

    public String getVar_sal_allowence() {
        return var_sal_allowence;
    }

    public void setVar_sal_total_deduction(String var_sal_total_deduction) {
        this.var_sal_total_deduction = var_sal_total_deduction;
    }

    public String getVar_sal_total_deduction() {
        return var_sal_total_deduction;
    }

    public void atdEventTypeChanged(ValueChangeEvent valueChangeEvent) {
        CommonUtil.log("Shit is being called.. .");
        // Add event code here...
        CommonUtil.setvalueToExpression("#{bindings.LeaveType2.inputValue}",
                                        valueChangeEvent.getNewValue()); //Updating Model Values
        String selectedType =
            CommonUtil.getValueFrmExpression("#{bindings.LeaveType2.attributeValue}").toString();


        DCBindingContainer dcBindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        // Get a the row iterator
        DCIteratorBinding iter =
            (DCIteratorBinding)dcBindings.get("VO_AttendanceEvents2Iterator");
        // Get all the rows of a iterator
        Row[] rows = iter.getAllRowsInRange();
        for (int i = 0; i < rows.length; i++) {
            rows[i].setAttribute("LeaveType", selectedType);
        }
        ((HubModuleImpl)CommonUtil.getAppModule()).getTransaction().commit();
    }

    public String openMyHierarchy() {
        // Add event code here...
        ExternalContext ec =
            FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect("http://fmw.nishat.net:7003/TheHUB/faces/SupervisorView/my_hierarchy.jspx");
            //ec.redirect("http://mis-12:7101/TheHUB/faces/SupervisorView/my_hierarchy.jspx");

        } catch (IOException e) {
        }
        return null;

    }

    public String mail() {
        // Add event code here...
        ExternalContext ec =
            FacesContext.getCurrentInstance().getExternalContext();
        try {
            //ec.redirect("http://fmw.nishat.net:7003/TheHUB/faces/SupervisorView/my_hierarchy.jspx");
            ec.redirect("mailto:usmanriaz@nishat.net");


        } catch (IOException e) {
        }
        return null;
    }

    public void setCurr_month(RichOutputText curr_month) {
        this.curr_month = curr_month;
    }

    public RichOutputText getCurr_month() {
        return curr_month;
    }

    public void setPostingMonth(RichInputText postingMonth) {
        this.postingMonth = postingMonth;
    }

    public RichInputText getPostingMonth() {
        return postingMonth;
    }

    private int getRemainingLeaves(int leave_type, String user_id) {
        HubModuleImpl appModule = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl voRemainingLeaves = appModule.getVO_RemainingLeaves1();
        voRemainingLeaves.setNamedWhereClauseParam("user_id", user_id);
        voRemainingLeaves.setNamedWhereClauseParam("leave_type", leave_type);
        voRemainingLeaves.executeQuery();
        RowSetIterator rsiRemainingLeaves =
            voRemainingLeaves.createRowSetIterator(null);
        return Integer.parseInt(rsiRemainingLeaves.getAllRowsInRange()[0].getAttribute("LeavesRemaining").toString());
    }

    public void setCurr_Year(RichInputText curr_Year) {
        this.curr_Year = curr_Year;
    }

    public RichInputText getCurr_Year() {
        return curr_Year;
    }

    public void setTotalMissedTimeStr(RichOutputText totalMissedTimeStr) {
        this.totalMissedTimeStr = totalMissedTimeStr;
    }

    public RichOutputText getTotalMissedTimeStr() {
        return totalMissedTimeStr;
    }
}
