package view.nishat.net.BackingBean;

import hub.nishat.net.model.AM.HubModuleImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.server.ViewObjectImpl;

import org.apache.myfaces.trinidad.model.RowKeySet;

import view.nishat.net.Helper.CommonUtil;

public class SupervisorApproval {
    private RichSelectOneChoice lovNotificationType;
    private RichTable approvalTable;
    private RichCommandButton bapproveDisapprove;


    public SupervisorApproval() {
    }


    public void setLovNotificationType(RichSelectOneChoice lovNotificationType) {
        this.lovNotificationType = lovNotificationType;
    }

    public RichSelectOneChoice getLovNotificationType() {
        return lovNotificationType;
    }


    public void searchNotificationLovChange(ValueChangeEvent valueChangeEvent) {
        String valueType = lovNotificationType.getValue().toString();
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl voLeavesAndException =
            am.getVO_LeavesAndPolicyExceptions1();
        if (!valueType.equals("A")) {
            voLeavesAndException.setNamedWhereClauseParam("n_type", valueType);
        } else {
            voLeavesAndException.setNamedWhereClauseParam("n_type", "%");
        }

        voLeavesAndException.executeQuery();
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }


    public String testButton() {
        // Add event code here...
        RowKeySet selectedEmps = getApprovalTable().getSelectedRowKeys();
        Iterator selectedEmpIter = selectedEmps.iterator();
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding empIter =
            bindings.findIteratorBinding("VO_LeavesAndPolicyExceptions1Iterator");
        RowSetIterator empRSIter = empIter.getRowSetIterator();
        String date = "";
        //GET THE ATTENDANCE FROM THE SELECTED ROW FROM TABLE
        while (selectedEmpIter.hasNext()) {
            Key key = (Key)((List)selectedEmpIter.next()).get(0);
            Row currentRow = empRSIter.getRow(key);
            date = currentRow.getAttribute("Type").toString();
            System.out.println(date);
        }
        return null;
    }

    public void setApprovalTable(RichTable approvalTable) {
        this.approvalTable = approvalTable;
    }

    public RichTable getApprovalTable() {
        return approvalTable;
    }

    public String approve() {
        //GET THE NOTIFICATION TYPE
        String selectedNotificationType =
            lovNotificationType.getValue() == null ? null :
            lovNotificationType.getValue().toString();

        /**GET THE SELECTED ROW KEY SET OF THE TABLE FROM THE PAGE
         * AND THEN GET THE KEY THAT WE WILL USE TO FIND THE ROW
         * FROM THE VIEW OBJECT*/
        RowKeySet selectedEmps = getApprovalTable().getSelectedRowKeys();
        Iterator selectedEmpIter = selectedEmps.iterator();
        Key key = (Key)((List)selectedEmpIter.next()).get(0);

        /**GET THE SELECTED VIEW OBJECT ROW USING THE KEY*/
        HubModuleImpl am = ((HubModuleImpl)CommonUtil.getAppModule());
        ViewObjectImpl vo = am.getVO_LeavesAndPolicyExceptions1();
        vo.setNamedWhereClauseParam("n_type", "%");
        vo.executeQuery();
        Row currentRow = vo.getRow(key);

        /**DETERMINE WHAT TO APPROVE, IS IT AN EXCEPTION OR IS IT A LEAVE*/
        String whatToApprove =
            currentRow.getAttribute("Type").equals("Exception") ? "Exception" :
            "Leave";


        /**********************************************************
         *                   EXCEPTION APPROVAL                   *
         *                   ******************
         *HERE I AM GOING TO APPROVE THE EXCEPTION. I'LL BE
         * PERFORMING FOLLOWING TASKS:
         * 1:-IF EXCEPTION WAS PREVIOUSLY DISAPPROVED THEN WE DON'T
         * SIMPLY APPROVE IT, WE DECREASE AVAILED LEAVES FROM HIS
         * LEAVE BALANCE THAT WAS PREVIOUSLY INCREASED DUE TO DIS-
         * APPROVAL.
         * 2:-IF EXCEPTION WAS NOT PREVIOUSLY DISAPPROVED THEN WE
         * SIMPLY APPROVE IT.*
         *
         *                                                        */

        if (whatToApprove.equals("Exception")) {

            /**TO DETERMINE IF IT WAS PREVIOUSLY DISAPPROVED.
             * IF IT WAS THEN FLAG WILL BE 'N' */
            String exceptionFlag =
                currentRow.getAttribute("PolicyExceptionApprovedFlag").toString();
            Object effectiveWorkedHours =
                currentRow.getAttribute("EffectiveWorkedHours");

            /**CALCULATE HOW MANY LEAVE I AM GOING TO DEDUCT*/

            /**APPROVE THE EXCEPTION AND DECREASE THE AVAILED LEAVES BALANCE
                 * BY THE AMOUNT WHICH WAS INCREASED WHEN THIS VERY EXCEPTION WAS
                 * BEING DISAPPROVES */

            float howManyWasDeduct =
                0.0f; //IT'S INITIAL VALUE, NOTHING'S GONNA BE BAD
            if (effectiveWorkedHours == null) {
                howManyWasDeduct = 1;
            } else {
                String effectiveHours = effectiveWorkedHours.toString();
                int min = CommonUtil.convertTimeStringToMin(effectiveHours);
                if (min < CommonUtil.convertTimeStringToMin("4:00")) {
                    howManyWasDeduct = 1f;
                } else {
                    howManyWasDeduct = .5f;
                }
            }

            currentRow.setAttribute("PolicyExceptionApprovedFlag", "Y");
            String date = currentRow.getAttribute("AttendanceDate").toString();

            /**UPDATE THE LEAVE BALANCE*/
            ViewObjectImpl voLeaveBalance = am.getVO_EmployeeLeaveBalance1();
            voLeaveBalance.setWhereClause("LEAVE_TYPE_ID = 1 and MONTH = 'Apr'"); //+ //CommonUtil.getCurrent(CommonUtil.CURR_MONTH_NAME));
            voLeaveBalance.executeQuery();
            RowSetIterator rs = voLeaveBalance.createRowSetIterator(null);
            rs.reset();
            Row[] rows = rs.getAllRowsInRange();
            if (rows.length > 0) {
                float newAvailedLeaves =
                    Float.parseFloat(rows[0].getAttribute("AvailedLeaves").toString()) +
                    howManyWasDeduct;
                rows[0].setAttribute("AvailedLeaves",
                                     String.valueOf(newAvailedLeaves));
                voLeaveBalance.executeQuery();
            }
            am.getTransaction().commit();
            voLeaveBalance.executeQuery();
            
            /******************************************************************
            *                     LEAVE APPROVAL
            *HERE CHECK IF IT'S CASUAL LEAVE OR ANNUAL LEAVE, EITHER HALF OR
            * FULL AND THEN UPDATE THE LEAVE BALANCE CORRESPONDENGLY*/

        } else if (whatToApprove.equals("Leave")) {
            /**DETERMINE IS IT GOING TO BE A HALF LEAVE OR NOT IF IT'S GOING TO BE A LEAVE*/
            String isHalf = currentRow.getAttribute("IsHalf").toString();

            /**UPDATE THE LEAVE FLAG TO Y*/
            currentRow.setAttribute("LeaveApprovedFlag", "Y");
            String leaveType =
                currentRow.getAttribute("LeaveTypeId").toString();
            String date = currentRow.getAttribute("AttendanceDate").toString();

            /**NOW, AN EMPLOYEE MAY NOT HAVE ANY LEAVE BALANCE. IN THAT CASE
             * I AM GOING TO CREATE A ROW FOR HIM AND UPDATE HIS LEAVE BALANCE
             * IN THAT ROW.*/
            ViewObjectImpl voLeaveBalance = am.getVO_EmployeeLeaveBalance1();
            //            voLeaveBalance.setNamedWhereClauseParam("at_date", date);
            //            voLeaveBalance.setNamedWhereClauseParam("leaveType", leaveType);
            voLeaveBalance.setWhereClause("LEAVE_TYPE_ID = " + leaveType +
                                          " and MONTH = 'Apr'"); //+CommonUtil.getCurrent(CommonUtil.CURR_MONTH_NAME));

            voLeaveBalance.executeQuery();
            RowSetIterator rs = voLeaveBalance.createRowSetIterator(null);
            rs.reset();
            Row[] rows = rs.getAllRowsInRange();
            int rowsCount = rows.length;
            /**HE DOES HAVE LEAVE BALANCE*/
            if (rowsCount > 0) {
                float newAvailedLeaves =
                    Float.parseFloat(rows[0].getAttribute("AvailedLeaves").toString()) +
                    (isHalf.equals("Y") ? .5f : 1f);
                rows[0].setAttribute("AvailedLeaves",
                                     String.valueOf(newAvailedLeaves));
                voLeaveBalance.executeQuery();
                /**HE DOES NOT HAVE ANY LEAVE BALANCE. CREATE A ROW*/
            } else {
                Row newLeaveRow = voLeaveBalance.createRow();
                newLeaveRow.setAttribute("LeaveBalanceId",
                                         am.getLeaveSequence().intValue());
                newLeaveRow.setAttribute("UserId",
                                         "3562"); //HARD COADING FOR NOW OTHERWISE USE SESSION VALUE
                newLeaveRow.setAttribute("LeaveTypeId", leaveType);
                newLeaveRow.setAttribute("TotalLeaves", "0");
                newLeaveRow.setAttribute("AvailedLeaves",
                                         isHalf.equals("Y") ? .5f : 1f);
                newLeaveRow.setAttribute("Month", "Apr");
                //CommonUtil.getCurrent(CommonUtil.CURR_MONTH_NAME));
                newLeaveRow.setAttribute("Year",
                                         Calendar.getInstance().get(Calendar.YEAR));
                voLeaveBalance.insertRow(newLeaveRow);
                voLeaveBalance.executeQuery();
            }
            am.getTransaction().commit();
            vo.executeQuery();
        }


        /**UPDATE THE LOV UI SELECTION */
        if (selectedNotificationType != null) {
            if (selectedNotificationType.equals("A")) {
                vo.setNamedWhereClauseParam("n_type", "%");
            } else {
                vo.setNamedWhereClauseParam("n_type",
                                            selectedNotificationType);
            }
        } else if (selectedNotificationType == null) {
            vo.setNamedWhereClauseParam("n_type", "U");
        }
        vo.executeQuery();

        return null;

    }

    public String disapprove() {
        /**GET THE SELECTED NORIFICATION TYPE FROM THE LOV FROM UI PAGE
         */
        String selectedNotificationType =
            lovNotificationType.getValue() == null ? null :
            lovNotificationType.getValue().toString();


        /**GET THE SELECTED ROW KEY SET OF THE TABLE FROM THE PAGE
         * AND THEN GET THE KEY THAT WE WILL USE TO FIND THE ROW
         * FROM THE VIEW OBJECT*/
        RowKeySet selectedEmps = getApprovalTable().getSelectedRowKeys();
        Iterator selectedEmpIter = selectedEmps.iterator();
        Key key = (Key)((List)selectedEmpIter.next()).get(0);


        /**GET THE SELECTED VIEW OBJECT ROW USING THE KEY*/
        HubModuleImpl am = ((HubModuleImpl)CommonUtil.getAppModule());
        ViewObjectImpl vo = am.getVO_LeavesAndPolicyExceptions1();
        vo.setNamedWhereClauseParam("n_type", "%");
        vo.executeQuery();
        Row currentRow = vo.getRow(key);


        /**VARIABLE TO DETERMINE WHAT TO APPROVE, IS IT GOING TO BE AN EXCEPTINO
         * OR A LEAVE*/
        String whatToDisapprove =
            currentRow.getAttribute("Type").equals("Exception") ? "Exception" :
            "Leave";

        /**IT'S AN EXCEPTION*/
        if (whatToDisapprove.equals("Exception")) {
            /**TO DETERMINE IF IT WAS PREVIOUSLY APPROVED.
             * IF IT WAS THEN FLAG WILL BE 'N' */
            String exceptionFlag =
                currentRow.getAttribute("PolicyExceptionApprovedFlag").toString();
            Object effectiveWorkedHours =
                currentRow.getAttribute("EffectiveWorkedHours");
            /**CALCULATE HOW MANY LEAVE I AM GOING TO DEDUCT*/
            if (exceptionFlag.equals("Y")) {
                /**APPROVE THE EXCEPTION AND DECREASE THE AVAILED LEAVES BALANCE
                 * BY THE AMOUNT WHICH WAS INCREASED WHEN THIS VERY EXCEPTION WAS
                 * BEING DISAPPROVES */

                float howManyWasDeduct =
                    0.2f; //IT'S INITIAL VALUE, NOTHING'S GONNA BE BAD
                if (effectiveWorkedHours == null) {
                    howManyWasDeduct = 1;
                } else {
                    String effectiveHours = effectiveWorkedHours.toString();
                    int min =
                        CommonUtil.convertTimeStringToMin(effectiveHours);
                    if (min < CommonUtil.convertTimeStringToMin("4:00")) {
                        howManyWasDeduct = 1f;
                    } else {
                        howManyWasDeduct = .5f;
                    }
                }

                currentRow.setAttribute("PolicyExceptionApprovedFlag", "N");
                String date =
                    currentRow.getAttribute("AttendanceDate").toString();

                /**UPDATE THE LEAVE BALANCE*/
                ViewObjectImpl voLeaveBalance =
                    am.getVO_EmployeeLeaveBalance1();
                voLeaveBalance.setWhereClause("LEAVE_TYPE_ID = 1 and MONTH = 'Apr'"); //+ //CommonUtil.getCurrent(CommonUtil.CURR_MONTH_NAME));
                voLeaveBalance.executeQuery();
                RowSetIterator rs = voLeaveBalance.createRowSetIterator(null);
                rs.reset();
                Row[] rows = rs.getAllRowsInRange();
                if (rows.length > 0) {
                    float newAvailedLeaves =
                        Float.parseFloat(rows[0].getAttribute("AvailedLeaves").toString()) -
                        howManyWasDeduct;
                    rows[0].setAttribute("AvailedLeaves",
                                         String.valueOf(newAvailedLeaves));
                    voLeaveBalance.executeQuery();

                }
                am.getTransaction().commit();
                voLeaveBalance.executeQuery();
            } else if (exceptionFlag.equals("U")) {
                currentRow.setAttribute("PolicyExceptionApprovedFlag", "N");
                am.getTransaction().commit();
                vo.executeQuery();
            }
            //            /**UPDATE THE LEAVES
        } else if (whatToDisapprove.equals("Leave")) {
            String leaveApprovedFlag =
                currentRow.getAttribute("LeaveApprovedFlag").toString();
            if (leaveApprovedFlag.equals("Y")) {
                currentRow.setAttribute("LeaveApprovedFlag", "N");
                String leaveType =
                    currentRow.getAttribute("LeaveTypeId").toString();
                String date =
                    currentRow.getAttribute("AttendanceDate").toString();
                String isHalf =
                    currentRow.getAttribute("IsHalf") != null ? currentRow.getAttribute("IsHalf").toString() :
                    null;
                /**UPDATE THE VOLEAVE_BALANCE
                             */
                ViewObjectImpl voLeaveBalance =
                    am.getVO_EmployeeLeaveBalance1();
                //                voLeaveBalance.setNamedWhereClauseParam("at_date", date);
                //                voLeaveBalance.setNamedWhereClauseParam("leaveType",
                //                                                        leaveType);
                voLeaveBalance.setWhereClause("LEAVE_TYPE_ID = " + leaveType +
                                              " and MONTH = 'Apr'"); //+CommonUtil.getCurrent(CommonUtil.CURR_MONTH_NAME));

                voLeaveBalance.executeQuery();
                RowSetIterator rs = voLeaveBalance.createRowSetIterator(null);
                rs.reset();
                Row[] rows = rs.getAllRowsInRange();
                if (rows.length > 0) {
                    float newAvailedLeaves =
                        Float.parseFloat(rows[0].getAttribute("AvailedLeaves").toString()) -
                        (isHalf.equals("Y") ? .5f : 1f);
                    rows[0].setAttribute("AvailedLeaves",
                                         String.valueOf(newAvailedLeaves));
                    voLeaveBalance.executeQuery();
                    vo.executeQuery();
                    am.getTransaction().commit();
                }
            } else {
                currentRow.setAttribute("LeaveApprovedFlag", "N");
                am.getTransaction().commit();
                vo.executeQuery();
            }
        }
        if (selectedNotificationType != null) {
            if (selectedNotificationType.equals("A")) {
                System.out.println("select all");
                vo.setNamedWhereClauseParam("n_type", "%");
            } else {
                vo.setNamedWhereClauseParam("n_type",
                                            selectedNotificationType);
                System.out.println("select " + selectedNotificationType);
            }
        } else if (selectedNotificationType == null) {
            vo.setNamedWhereClauseParam("n_type", "U");
            System.out.println("select unprocessed");
        }
        vo.executeQuery();
        return null;
    }

    public void setBapproveDisapprove(RichCommandButton bapproveDisapprove) {
        this.bapproveDisapprove = bapproveDisapprove;
    }

    public RichCommandButton getBapproveDisapprove() {
        return bapproveDisapprove;
    }
}
