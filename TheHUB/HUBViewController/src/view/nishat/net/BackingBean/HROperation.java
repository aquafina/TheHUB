package view.nishat.net.BackingBean;

import hub.nishat.net.model.AM.HubModuleImpl;

import java.text.SimpleDateFormat;

import java.util.Calendar;

import java.util.Date;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.VariableValueManager;
import oracle.jbo.server.ViewObjectImpl;

import view.nishat.net.Helper.CommonUtil;
import view.nishat.net.Helper.Constants;

public class HROperation {
    private RichSelectOneChoice scDepartmentName;
    private RichInputText month;
    private RichInputText year;
    private RichSelectOneChoice scMonth;
    private RichSelectOneChoice scYear;
    private RichOutputText personId;
    private RichOutputText att_month;
    private RichOutputText att_year;
    private RichInputText itPersonId;
    private RichInputText itMins;
    private RichInputText itPersonTypeId;
    private RichInputText itWorkingTime;

    public HROperation() {
    }

    public void setScDepartmentName(RichSelectOneChoice scDepartmentName) {
        this.scDepartmentName = scDepartmentName;
    }

    public RichSelectOneChoice getScDepartmentName() {
        return scDepartmentName;
    }

    /***************************DEPARTMENT SELECTED*****************************
     * THIS METHOD IS GETTING CALLED FROM THE SELECT ONE CHOICE ON THE HR PAGE.
     * WHEN A NEW DEPARTMENT IS SELECTED, I AM LOADING ALL THE EMPLOYEE OF THAT
     * DEPARTMENT                                                              *
     *                                                                         *
     *                                                                         */
    public void departmentSelected(ValueChangeEvent valueChangeEvent) {
        /**GET THE SELECTED DEPARTMENT FROM THE LOV*/
        CommonUtil.setvalueToExpression("#{bindings.HierarchyName.inputValue}",
                                        valueChangeEvent.getNewValue());
        String selectedDpt =
            CommonUtil.getValueFrmExpression("#{bindings.HierarchyName.attributeValue}").toString();
        System.out.println("Select All the employee of: " + selectedDpt);
        /**LOAD ALL THE EMPLOYEE OF THAT DEPARTMENT*/
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl vo = am.getVO_ActiveEmployees2();

        /**RESET WHERE CLAUSE*/
        vo.setWhereClause(null);
        vo.executeQuery();

        /**CREATE NEW WHERE CLAUSE AND APPLY IT ON VO*/
        String whereClause = "HIERARCHY_NAME = '" + selectedDpt + "'";
        vo.setWhereClause(whereClause);
        vo.executeQuery();
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public String applyAttendanceSearch() {
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl vo = am.getVO_AttendanceHR3();
        oracle.jbo.ViewCriteria vc =
            vo.getViewCriteria("VO_AttendanceHRCriteria");
        vc.resetCriteria();
        VariableValueManager vvm = vc.ensureVariableManager();
        System.out.println("Month: " +
                           CommonUtil.getValueFrmExpression("#{bindings.Year.attributeValue}") +
                           " Year: " +
                           CommonUtil.getValueFrmExpression("#{bindings.Year.attributeValue}"));
        
        vvm.clearValue("bv_year");
        vvm.clearValue("bv_month");
        
        vvm.setVariableValue("bv_year",
                             CommonUtil.getValueFrmExpression("#{bindings.Year.attributeValue}"));
        vvm.setVariableValue("bv_month",
                             CommonUtil.getValueFrmExpression("#{bindings.Month.attributeValue}"));
        
        vo.applyViewCriteria(vc, true);
        vo.executeQuery();
        return null;
    }

    public void setMonth(RichInputText month) {
        this.month = month;
    }

    public RichInputText getMonth() {
        return month;
    }

    public void setYear(RichInputText year) {
        this.year = year;
    }

    public RichInputText getYear() {
        return year;
    }

    public void setScMonth(RichSelectOneChoice scMonth) {
        this.scMonth = scMonth;
    }

    public RichSelectOneChoice getScMonth() {
        return scMonth;
    }

    public void setScYear(RichSelectOneChoice scYear) {
        this.scYear = scYear;
    }

    public RichSelectOneChoice getScYear() {
        return scYear;
    }

    public void monthSelected(ValueChangeEvent valueChangeEvent) {
        CommonUtil.setvalueToExpression("#{bindings.Month.inputValue}",
                                        valueChangeEvent.getNewValue());
    }

    public void yearSelected(ValueChangeEvent valueChangeEvent) {
        CommonUtil.setvalueToExpression("#{bindings.Year.inputValue}",
                                        valueChangeEvent.getNewValue());
    }

    public void setPersonId(RichOutputText personId) {
        this.personId = personId;
    }

    public RichOutputText getPersonId() {
        return personId;
    }

    public String post() {
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl voPosting = am.getVO_HRPosting1();
        voPosting.setWhereClause("USER_ID='" + personId.getValue().toString() +
                                 "' and upper(Posting_Month) = '" +
                                 att_month.getValue().toString().toUpperCase() +
                                 "' and posting_year = '" +
                                 att_year.getValue().toString() + "'");
        voPosting.executeQuery();
        RowSetIterator rsi = voPosting.createRowSetIterator(null);
        Row[] row = rsi.getAllRowsInRange();
        row[0].setAttribute("HrPostedFlag", "Y");
        voPosting.setWhereClause(null);
        voPosting.executeQuery();
        am.getTransaction().commit();
        voPosting.executeQuery();
        return null;
    }

    public void setAtt_month(RichOutputText att_month) {
        this.att_month = att_month;
    }

    public RichOutputText getAtt_month() {
        return att_month;
    }

    public void setAtt_year(RichOutputText att_year) {
        this.att_year = att_year;
    }

    public RichOutputText getAtt_year() {
        return att_year;
    }

    public void setItPersonId(RichInputText itPersonId) {
        this.itPersonId = itPersonId;
    }

    public RichInputText getItPersonId() {
        return itPersonId;
    }

    public void setItMins(RichInputText itMins) {
        this.itMins = itMins;
    }

    public RichInputText getItMins() {
        return itMins;
    }

    public void setItPersonTypeId(RichInputText itPersonTypeId) {
        this.itPersonTypeId = itPersonTypeId;
    }

    public RichInputText getItPersonTypeId() {
        return itPersonTypeId;
    }

    public void setItWorkingTime(RichInputText itWorkingTime) {
        this.itWorkingTime = itWorkingTime;
    }

    public RichInputText getItWorkingTime() {
        return itWorkingTime;
    }

    /** METHOD TO SAVE USER SETTINGS E.G. FLEXI HOURS AND EMPLOYEE TYPE
     *  THIS METHOD GETS CALLED FROM EMP_SETTINGS.JSPX PAGE ON SAVE BUTTON
     *  CLICK. . .
     */
    public String saveSettings() {
        /**GET THE VALUES FROM PAGE*/
        String flexiMin = itMins.getValue().toString();
        String employeeType = itPersonTypeId.getValue().toString();
        String employeeId = itPersonId.getValue().toString();

        String newFlexi =
            CommonUtil.getValueFrmExpression("#{bindings.HoursFlexible.attributeValue}").toString();
        if (flexiMin == null || employeeType == null || employeeId == null) {
            return null;
        }
                else{
                    CommonUtil.log(flexiMin+" - "+employeeType+" - "+employeeId);
                }

        //        /**GET VIEW OBJECT FROM APPLICATION MODULE AND QUERY THE REOCORD OF A SPECIFIC
        //         * EMPLOYEE */
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl voEmpFlexi = am.getVO_EmpFlexi1();
        ViewObjectImpl voUsers = am.getVO_XxEPortalUsers1();

        CommonUtil.resetWhereClause(voEmpFlexi);
        voEmpFlexi.setWhereClause("emp_id = '" + employeeId + "'");
        voEmpFlexi.executeQuery();
        RowSetIterator rsiEmpFlexi = voEmpFlexi.createRowSetIterator(null);
        Row[] rowEmpFlexi = rsiEmpFlexi.getAllRowsInRange();
        //        /**GET THE CURRENT START TIME,END TIME AND CURRENT FLEXI HOURS OF EMPLOYEE*/
        String startTime =
            rowEmpFlexi[0].getAttribute("MaxStartTime").toString();
        //String startTime = "9:00 AM";
        String endTime = rowEmpFlexi[0].getAttribute("MaxEndTime").toString();
        //String endTime = "5:00 PM";
        String currFlexi =
            rowEmpFlexi[0].getAttribute("HoursFlexible").toString();
        System.out.println("Current Flexi: " + currFlexi + " New Flexi:" +
                           newFlexi);
        //        /**ADD FLEXI TIME TO START TIME AND GET THE NEW START TIME*/
        Calendar calStartTime = Calendar.getInstance();
        calStartTime.set(Calendar.HOUR,
                         Integer.parseInt(startTime.split(" ")[0].split(":")[0]));
        calStartTime.set(Calendar.MINUTE,
                         Integer.parseInt(startTime.split(" ")[0].split(":")[1]));
        calStartTime.add(Calendar.MINUTE,
                         Integer.parseInt(flexiMin.split(" ")[0]));

        String newStartTime =
            calStartTime.get(Calendar.HOUR) + ":" + (calStartTime.get(Calendar.MINUTE) <
                                                     10 ?
                                                     "0" + calStartTime.get(Calendar.MINUTE) :
                                                     calStartTime.get(Calendar.MINUTE)) +
            " AM";
        //CommonUtil.log(newStartTime);
        /**ADD FLEXI TIME TO END TIME AND GET THE NEW END TIME*/
        Calendar calEndTime = Calendar.getInstance();
        calEndTime.set(Calendar.HOUR,
                       Integer.parseInt(endTime.split(" ")[0].split(":")[0]));
        calEndTime.set(Calendar.MINUTE,
                       Integer.parseInt(endTime.split(" ")[0].split(":")[1]));
        calEndTime.add(Calendar.MINUTE,
                       Integer.parseInt(flexiMin.split(" ")[0]));
        String newEndTime =
            calEndTime.get(Calendar.HOUR) + ":" + (calEndTime.get(Calendar.MINUTE) <
                                                   10 ?
                                                   "0" + calEndTime.get(Calendar.MINUTE) :
                                                   calEndTime.get(Calendar.MINUTE)) +
            " PM";
        CommonUtil.log(newStartTime + " - " + newEndTime);

        /**SET NEW START AND END TIMEINGS*/
        if (Integer.parseInt(flexiMin) == 0) {
            rowEmpFlexi[0].setAttribute("MaxStartTime", "9:00 AM");
            rowEmpFlexi[0].setAttribute("MaxEndTime", "5:00 PM");
            rowEmpFlexi[0].setAttribute("StartTime", "9:00 AM");
            rowEmpFlexi[0].setAttribute("EndTime", "5:00 PM");

        } else if (Integer.parseInt(flexiMin) == 30) {
            CommonUtil.log("Flexi timings are 9:30 ");
            rowEmpFlexi[0].setAttribute("MaxStartTime", "9:30 AM");
            rowEmpFlexi[0].setAttribute("MaxEndTime", "5:30 PM");
            rowEmpFlexi[0].setAttribute("StartTime", "8:30 AM");
            rowEmpFlexi[0].setAttribute("EndTime", "4:30 PM");
        } else if (Integer.parseInt(flexiMin) == 60) {
            rowEmpFlexi[0].setAttribute("MaxStartTime", "10:00 AM");
            rowEmpFlexi[0].setAttribute("MaxEndTime", "6:00 PM");
            rowEmpFlexi[0].setAttribute("StartTime", "8:30 AM");
            rowEmpFlexi[0].setAttribute("EndTime", "4:30 PM");
        }
        CommonUtil.resetWhereClause(voUsers);
        voUsers.setWhereClause("person_id = '" + employeeId + "'");
        voUsers.executeQuery();
        RowSetIterator rsiUsers = voUsers.createRowSetIterator(null);
        Row[] user = null;
        int pageCount = voUsers.getEstimatedRangePageCount();
        CommonUtil.log("User: " + pageCount);
        if (pageCount > 1) {
            user = rsiUsers.getNextRangeSet();
        } else {
            user = rsiUsers.getAllRowsInRange();
        }
        while (user.length > 0) {
            for (int i = 0; i < user.length; i++) {
                //System.out.println(user[i].getAttribute("EmailAddress").toString()+" - "+employeeType);
                user[i].setAttribute("EmpType", employeeType);
            }
            user = rsiUsers.getNextRangeSet();
        }

        CommonUtil.resetWhereClause(voEmpFlexi);

        ViewObjectImpl voAttendance = am.getVO_AttendanceHR1();

        java.sql.Date date =
            CommonUtil.convertFromJAVADateToSQLDate(new Date());
        String where =
            "emp_id = '" + employeeId + "' and to_char(attendance_Date,'yyyy-mm-dd') >= '" +
            date + "'";
        CommonUtil.log(where);
        CommonUtil.resetWhereClause(voAttendance);
        voAttendance.setWhereClause(where);
        voAttendance.executeQuery();
        CommonUtil.log(voAttendance.getEstimatedRangePageCount()+" <<============");
        RowSetIterator rsiAttendance = voAttendance.createRowSetIterator(null);
        Row[] rows = rsiAttendance.getNextRangeSet();
        while (rows.length > 0) {
            for (int i = 0; i < rows.length; i++) {
                if (Integer.parseInt(flexiMin) == 0) {
                    rows[i].setAttribute("MaxStartTime", "9:00 AM");
                    rows[i].setAttribute("MaxEndTime", "5:00 PM");
                    rows[i].setAttribute("StartTime", "9:00 AM");
                    rows[i].setAttribute("EndTime", "5:00 PM");

                } else if (Integer.parseInt(flexiMin) == 30) {
                    CommonUtil.log("what the fuck");
                    rows[i].setAttribute("MaxStartTime", "9:30 AM");
                    rows[i].setAttribute("MaxEndTime", "5:30 PM");
                    rows[i].setAttribute("StartTime", "8:30 AM");
                    rows[i].setAttribute("EndTime", "4:30 PM");
                } else if (Integer.parseInt(flexiMin) == 60) {
                    rows[i].setAttribute("MaxStartTime", "10:00 AM");
                    rows[i].setAttribute("MaxEndTime", "6:00 PM");
                    rows[i].setAttribute("StartTime", "8:30 AM");
                    rows[i].setAttribute("EndTime", "4:30 PM");
                }
            }
            rows = rsiAttendance.getNextRangeSet();
            
        }
        
        am.getTransaction().commit();
        CommonUtil.resetWhereClause(voAttendance);
        voAttendance.executeQuery();
        return null;
    }

    public void flexiHourchanged(ValueChangeEvent valueChangeEvent) {
        CommonUtil.setvalueToExpression("#{bindings.HoursFlexible.inputValue}",
                                        valueChangeEvent.getNewValue());
        String selectedDpt =
            CommonUtil.getValueFrmExpression("#{bindings.HoursFlexible.attributeValue}").toString();
    }
}
