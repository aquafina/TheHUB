package view.nishat.net.BackingBean;

import hub.nishat.net.model.AM.HubModuleImpl;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Map;

import oracle.adf.model.BindingContext;

import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.domain.Number;
import oracle.jbo.server.ViewObjectImpl;

import org.apache.myfaces.trinidad.model.RowKeySet;

import view.nishat.net.Helper.CommonUtil;
import view.nishat.net.Helper.Constants;
import view.nishat.net.Helper.PolicyHelper;

public class SupervisorResponsibility {
    private RichTable leaveRequestTable;
    private Map<Integer,String> leaves;
    private Map<String,Integer> leavesID;
    
    private RichTable exceptionReqTable;
    public SupervisorResponsibility() {
        leaves = new HashMap<Integer,String>();    
        leaves.put(1,"CASUAL");
        leaves.put(2,"ANNUAL");
        leaves.put(3,"HALF_CASUAL");
        leaves.put(4,"HALF_ANNUAL");
        
        leavesID = new HashMap<String,Integer>();    
        leavesID.put("CASUAL",1);
        leavesID.put("ANNUAL",2);
        leavesID.put("HALF_CASUAL",3);
        leavesID.put("HALF_ANNUAL",4);
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }
    private String commitAction(){
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding = bindings.getOperationBinding("Commit");
        Object result = operationBinding.execute();
        if (!operationBinding.getErrors().isEmpty()) {
            return null;
        }
        return null;
    }

    public void setLeaveRequestTable(RichTable leaveRequestTable) {
        this.leaveRequestTable = leaveRequestTable;
    }

    public RichTable getLeaveRequestTable() {
        return leaveRequestTable;
    }

    public String approve() {
        HubModuleImpl am = ((HubModuleImpl)CommonUtil.getAppModule());
        PolicyHelper ph = new PolicyHelper();
        Row row = getCurrentLeaveTableRow();
        String leaveType = row.getAttribute("LeaveType").toString();
        Date startDate = CommonUtil.convertJBODateToJavaDate(((oracle.jbo.domain.Date)row.getAttribute("StartDate")));
        Date endDate = CommonUtil.convertJBODateToJavaDate(((oracle.jbo.domain.Date)row.getAttribute("EndDate")));
        List<Date> leaveDays = CommonUtil.getDatesBetween(startDate,endDate);
        String empCode = row.getAttribute("EmpCode").toString();
        String userid = row.getAttribute("UserId").toString();
        
        updateLeaveFlaginAttTable(leaveDays,userid,leaveType);
        ph.deductLeaves(PolicyHelper.LEAVE_APPROVED,leaves.get(Integer.parseInt(leaveType)).toString(), leaveDays, empCode, userid, am);
        row.setAttribute("AprovedFlag", "Y");
        
        
        am.getTransaction().commit();
        
        return null;
    }
    public String disapprove() {
        HubModuleImpl am = ((HubModuleImpl)CommonUtil.getAppModule());
        ViewObjectImpl vo = am.getVO_LeaveRequests1();
        
        PolicyHelper ph = new PolicyHelper();
        Row row = getCurrentLeaveTableRow();
        String leaveType = row.getAttribute("LeaveType").toString();
        Date startDate = CommonUtil.convertJBODateToJavaDate(((oracle.jbo.domain.Date)row.getAttribute("StartDate")));
        Date endDate = CommonUtil.convertJBODateToJavaDate(((oracle.jbo.domain.Date)row.getAttribute("EndDate")));
        List<Date> leaveDays = CommonUtil.getDatesBetween(startDate,endDate);
        String empCode = row.getAttribute("EmpCode").toString();
        String userid = row.getAttribute("UserId").toString();
        CommonUtil.log("PolicyHelper.LEAVE_DISAPPROVED: "+PolicyHelper.LEAVE_DISAPPROVED);
        ph.deductLeaves(PolicyHelper.LEAVE_DISAPPROVED,leaves.get(Integer.parseInt(leaveType)).toString(), leaveDays, empCode, userid, am);
        row.setAttribute("AprovedFlag", "N");
        am.getTransaction().commit();
        
        CommonUtil.refreshVO("VO_LeaveRequests1Iterator");
        vo.executeQuery();
        
        return null;
    }
    public Row getCurrentLeaveTableRow(){
        HubModuleImpl am = ((HubModuleImpl)CommonUtil.getAppModule());
        /**GET THE SELECTED ROW KEY SET OF THE TABLE FROM THE PAGE
         * AND THEN GET THE KEY THAT WE WILL USE TO FIND THE ROW
         * FROM THE VIEW OBJECT*/
        RowKeySet selectedEmps = getLeaveRequestTable().getSelectedRowKeys();
        Iterator selectedEmpIter = selectedEmps.iterator();
        Key key = (Key)((List)selectedEmpIter.next()).get(0);

        /**GET THE SELECTED VIEW OBJECT ROW USING THE KEY*/
        ViewObjectImpl vo = am.getVO_LeaveRequests1();
        vo.executeQuery();
        Row currentRow = vo.getRow(key);
        return currentRow;
    }
    public Row getExceptionTableRow(){
        HubModuleImpl am = ((HubModuleImpl)CommonUtil.getAppModule());
        /**GET THE SELECTED ROW KEY SET OF THE TABLE FROM THE PAGE
         * AND THEN GET THE KEY THAT WE WILL USE TO FIND THE ROW
         * FROM THE VIEW OBJECT*/
        RowKeySet selectedEmps = getExceptionReqTable().getSelectedRowKeys();
        Iterator selectedEmpIter = selectedEmps.iterator();
        Key key = (Key)((List)selectedEmpIter.next()).get(0);

        /**GET THE SELECTED VIEW OBJECT ROW USING THE KEY*/
        ViewObjectImpl vo = am.getVO_ExceptionReq1();
        vo.executeQuery();
        Row currentRow = vo.getRow(key);
        return currentRow;
    }

    public String approveException() {
        Row row = getExceptionTableRow();
        String requestId = row.getAttribute("ExceptionRequestId").toString();
        exceptionAction(requestId, "Y");
        return null;
    }
    public void exceptionAction(String requestID,String flag){
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl voExceptions = am.getVO_ExceptionReq1();
        CommonUtil.resetWhereClause(voExceptions);
        CommonUtil.log("Asim: request id"+requestID);
        voExceptions.setWhereClause("EXCEPTION_REQUEST_ID = '"+requestID+"'");
        voExceptions.executeQuery();
        Row exception = voExceptions.createRowSetIterator(null).getAllRowsInRange()[0];
        exception.setAttribute("Approved", flag);
        String irrID = exception.getAttribute("IrregularityId").toString();
        CommonUtil.log("Asim: irr id: "+irrID);
        
        ViewObjectImpl voEvents = am.getVO_AttendanceEvents1();
        CommonUtil.resetWhereClause(voEvents);
        voEvents.setWhereClause("IRREGULARITY_ID = '"+irrID+"'");
        voEvents.executeQuery();
        int pageCount = voEvents.getEstimatedRangePageCount();
        Row irregularity = voEvents.createRowSetIterator(null).getAllRowsInRange()[0];
        irregularity.setAttribute("ExceptionApproved", flag);
        voEvents.executeQuery();
        
        
        
        am.getTransaction().commit();
        CommonUtil.resetWhereClause(voExceptions);
        CommonUtil.refreshVO("VO_ExceptionReq1Iterator");
        voExceptions.executeQuery();
    }

    public String disapproveException() {
        Row row = getExceptionTableRow();
        String requestId = row.getAttribute("ExceptionRequestId").toString();
        exceptionAction(requestId, "N");
        
//        String empCode= row.getAttribute("EmpCode").toString();
//        Date date = CommonUtil.convertJBODateToJavaDate((oracle.jbo.domain.Date)row.getAttribute("AttendanceDate"));
//        List<Date> leaveDays = new ArrayList<Date>();
//        leaveDays.add(date);
//        int  userId = ((Number)row.getAttribute("EmpId")).intValue();
//        
//        int missing_minutes = ((Number)row.getAttribute("MissingMinutes")).intValue();
//        PolicyHelper ph = new PolicyHelper();
//        ph.deductLeaves(4,ph.whatToDeduct(missing_minutes), leaveDays, empCode, String.valueOf(userId), (HubModuleImpl)CommonUtil.getAppModule());
        return null;
    }

    public void setExceptionReqTable(RichTable exceptionReqTable) {
        this.exceptionReqTable = exceptionReqTable;
    }

    public RichTable getExceptionReqTable() {
        return exceptionReqTable;
    }

    private void updateLeaveFlaginAttTable(List<Date> leaveDays,String userid,String leaveType) {
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl voAtt = am.getVO_XXEPortalAttendance1();
        for (Date d:leaveDays) {
            CommonUtil.resetWhereClause(voAtt);
            String where = " to_char(attendance_date,'YYYY-mm-dd') = '"+CommonUtil.convertFromJAVADateToSQLDate(d)+"' and emp_id = '"+userid+"'";
            //CommonUtil.log("Attndance.java->cancelLeave: "+where);
            voAtt.setWhereClause(where);
            voAtt.executeQuery();
            //System.out.println("Attendance.java -> cancelLeave: "+voAtt.getEstimatedRangePageCount());
            //System.out.println("Leave Type: "+leaveType+" and leave type id:"+leavesID.get(leaveType));
            Row row = voAtt.createRowSetIterator(null).getAllRowsInRange()[0];
            row.setAttribute("LeaveToday", "Y");
            row.setAttribute("TypeOfLeave", leaveType);
            
        } 
    }
}
