package view.nishat.net.BackingBean;

import hub.nishat.net.model.AM.HubModuleImpl;

import java.io.IOException;

import java.sql.SQLException;

import java.text.DateFormatSymbols;

import java.util.Calendar;
import java.util.Date;

import java.util.List;

import javax.faces.application.FacesMessage;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.dms.http.HttpRequest;

import oracle.javatools.parser.java.v2.common.CommonUtilities;

import oracle.jbo.Row;
import oracle.jbo.RowSet;
import oracle.jbo.RowSetIterator;
import oracle.jbo.server.ViewObjectImpl;

import view.nishat.net.Helper.AuthHandler;
import view.nishat.net.Helper.CommonUtil;
import view.nishat.net.Helper.Constants;
import view.nishat.net.Helper.PolicyHelper;
import view.nishat.net.PoJo.MonthlyDeductedLeave;

public class LoginPage {
    private RichInputText username;
    private RichInputText password;

    public LoginPage() {
        System.out.println("Login Bean Created");
    }

    public void setUsername(RichInputText username) {
        this.username = username;
    }

    public RichInputText getUsername() {
        return username;
    }

    public void setPassword(RichInputText password) {
        this.password = password;
    }

    public RichInputText getPassword() {
        return password;
    }

    public String proceedLogin() {
        //get the username and password that user entered
        String _username = username.getValue().toString();
        String _password = password.getValue().toString();

        AuthHandler authHandler = new AuthHandler();
        int result_code = -1;
        try {
            result_code = authHandler.authenticate(_username, _password);
        } catch (ClassNotFoundException e) {
            CommonUtil.showMessage("Take Screenshot and email it to I.C.T - " +
                                   e.getMessage() + " LoginPage.java line 52",
                                   112);
        }

        switch (result_code) {
        case 0:
            CommonUtil.showMessage("Invalid Username or password", 112);
            break;
        case 1:

            HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
            ViewObjectImpl voConsumedLeaves = am.getVO_ConsumedLeaves1();
            CommonUtil.resetWhereClause(voConsumedLeaves);

            String where =
                "user_id = '" + CommonUtil.getSessionValue(Constants.SESSION_USERID).toString() +
                "' and to_char(leave_date,'YYYY') = '2015'";
//                        String where =
//                "user_id = '" + "1379" +
//                "' and to_char(leave_date,'MON') = '" +
//                new DateFormatSymbols().getShortMonths()[Calendar.getInstance().get(Calendar.MONTH)].toUpperCase() +
//                "'";
            
            CommonUtil.log(where);
            voConsumedLeaves.setWhereClause(where);
            voConsumedLeaves.executeQuery();

            int pageCount = voConsumedLeaves.getEstimatedRangePageCount();
            Row[] rows =
                pageCount == 1 ? voConsumedLeaves.getAllRowsInRange() :
                voConsumedLeaves.getNextRangeSet();
            float casualLeave = 0;
            float annualLeave = 0;
            float shortLeave = 0;
            float lates = 0;
            float casualAvailable = 0;
            float annualAvailable = 0;
            float shortAvailable = 0;
            float latesAvailable = 0;

            while (rows.length > 0) {
                CommonUtil.log("in the loop");
                for (int i = 0; i < rows.length; i++) {
                    //if (rows[i].getAttribute("EffectiveWorkedHours") == null) {
                        int leaveType =
                            Integer.parseInt(rows[i].getAttribute("LeaveType").toString());
                        switch (leaveType) {
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
                //}
                rows = voConsumedLeaves.getNextRangeSet();
            }
            CommonUtil.log("Login Page: "+casualLeave+ " - "+annualLeave);
            CommonUtil.resetWhereClause(voConsumedLeaves);


            ViewObjectImpl voLeaveBalance = am.getVO_LeaveBalance1();
            CommonUtil.resetWhereClause(voLeaveBalance);

            String whereClause =
                "user_id = '" + CommonUtil.getSessionValue(Constants.SESSION_USERID).toString() +
                "' and year = '" + Calendar.getInstance().get(Calendar.YEAR) +
                "'";
            
            CommonUtil.log(whereClause);
            voLeaveBalance.setWhereClause(whereClause);
            voLeaveBalance.executeQuery();
            int pageCount1 = voLeaveBalance.getEstimatedRangePageCount();
            RowSetIterator rsi = voLeaveBalance.createRowSetIterator(null);
            Row[] rowsLeaveBalance =
                pageCount1 == 1 ? rsi.getAllRowsInRange() :
                rsi.getNextRangeSet();
            CommonUtil.log("Leave balance rec: " + pageCount1);
            while (rowsLeaveBalance.length > 0) {
                for (int i = 0; i < rowsLeaveBalance.length; i++) {
                    int leaveTypeID =
                        Integer.parseInt(rowsLeaveBalance[i].getAttribute("LeaveTypeId").toString());
                    switch (leaveTypeID) {
                    case 1:
                        casualAvailable =
                                Float.parseFloat(rowsLeaveBalance[i].getAttribute("TotalLeaves").toString());
                        break;
                    case 2:
                        annualAvailable =
                                Float.parseFloat(rowsLeaveBalance[i].getAttribute("TotalLeaves").toString());
                        break;
                    case 3:
                        shortAvailable =
                                Float.parseFloat(rowsLeaveBalance[i].getAttribute("TotalLeaves").toString());
                        break;
                    case 4:
                        latesAvailable =
                                Float.parseFloat(rowsLeaveBalance[i].getAttribute("TotalLeaves").toString());
                        break;
                    }
                }
                rowsLeaveBalance = rsi.getNextRangeSet();
            }
            CommonUtil.resetWhereClause(voLeaveBalance);
            CommonUtil.createUserSession(Constants.SESSION_CASUAL_AVAILED,
                                         String.valueOf(casualLeave));
            CommonUtil.createUserSession(Constants.SESSION_ANNUAL_AVAILED,
                                         String.valueOf(annualLeave));
            CommonUtil.createUserSession(Constants.SESSION_SHORT_AVAILED,
                                         String.valueOf(shortLeave));
            CommonUtil.createUserSession(Constants.SESSION_LATES_AVAILED,
                                         String.valueOf(lates));

            CommonUtil.createUserSession(Constants.SESSION_CASUAL_AVAILABLE,
                                         String.valueOf(casualAvailable));
            CommonUtil.createUserSession(Constants.SESSION_ANNUAL_AVAILABLE,
                                         String.valueOf(annualAvailable));
            CommonUtil.createUserSession(Constants.SESSION_SHORT_AVAILABLE,
                                         String.valueOf(shortAvailable));
            CommonUtil.createUserSession(Constants.SESSION_LATES_AVAILABLE,
                                         String.valueOf(latesAvailable));
            DateFormatSymbols d = new DateFormatSymbols();
            String month = d.getShortMonths()[Calendar.getInstance().get(Calendar.MONTH)];
            CommonUtil.createUserSession("yearmonth", CommonUtil.getMonthNumber(month)+"-"+Calendar.getInstance().get(Calendar.YEAR));
            

//            CommonUtil.showMessage("Login Page: "+CommonUtil.getSessionValue(Constants.SESSION_CASUAL_AVAILED) +
//                           " - " +
//                           CommonUtil.getSessionValue(Constants.SESSION_ANNUAL_AVAILED) +
//                           " - " +
//                           CommonUtil.getSessionValue(Constants.SESSION_SHORT_AVAILED) +
//                           " - " +
//                           CommonUtil.getSessionValue(Constants.SESSION_LATES_AVAILED) +
//                           " - " +
//                           CommonUtil.getSessionValue(Constants.SESSION_CASUAL_AVAILABLE) +
//                           " - " +
//                           CommonUtil.getSessionValue(Constants.SESSION_ANNUAL_AVAILABLE) +
//                           " - " +
//                           CommonUtil.getSessionValue(Constants.SESSION_SHORT_AVAILABLE) +
//                           " - " +
//                           CommonUtil.getSessionValue(Constants.SESSION_LATES_AVAILABLE) +
//                           "",111);

            CommonUtil.log(CommonUtil.getSessionValue(Constants.SESSION_USERID).toString());
            if (CommonUtil.getSessionValue(Constants.SESSION_USERID).toString().equals("84")) {
                CommonUtil.redirect("CEOReports/employee_report.jspx");
            }else{
                CommonUtil.redirect("employee_attendance.jspx");
            }
            break;
        case 2:
            CommonUtil.redirect("confirm_password_page.jspx");
            break;
        }
        return null;
    }

    public String proceedLogout() {
        CommonUtil.destroySession();

        //Object object = (Object)FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        object.getParameter("contextPath");
        //CommonUtil.log(object.toString());
        CommonUtil.redirect("http://fmw.nishat.net:7003/TheHUB/faces/login_page.jspx");
        return null;
    }
}
