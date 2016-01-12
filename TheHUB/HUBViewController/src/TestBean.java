import hub.nishat.net.model.AM.HubModuleImpl;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.List;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichTextEditor;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.adfinternal.view.faces.model.binding.FacesCtrlHierBinding;

import oracle.binding.BindingContainer;

import oracle.jbo.Row;

import oracle.jbo.RowSetIterator;

import oracle.jbo.ViewObject;

import oracle.jbo.domain.Number;
import oracle.jbo.server.JboAuditEventStrings;
import oracle.jbo.server.ViewObjectImpl;

import view.nishat.net.Helper.CommonUtil;
import view.nishat.net.Helper.PolicyHelper;
import view.nishat.net.LeaveDeductionRulesEngine;
import view.nishat.net.PoJo.MonthlyDeductedLeave;

public class TestBean {
    private RichCommandButton ss;
    private RichTable testTable;
    private RichTextEditor richTextEditor;
    private String richText;
    private oracle.jbo.domain.Date now;
    private Number month;
    private Long year;

    public TestBean() {
    }

    public String test() {
          
        DCBindingContainer dcBindings =  
                 (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();  
             // Get a the row iterator  
             DCIteratorBinding iter =  
                 (DCIteratorBinding)dcBindings.get("VO_AttendanceEvents2Iterator");  
             // Get all the rows of a iterator  
             Row[] rows = iter.getAllRowsInRange(); 
             CommonUtil.log(rows.length+" ");
             
        return null;
    }

    public String cb1_action() {
        CommonUtil.createUserSession("link", ss.getText().toString());
        System.out.println(ss.getText());
        return null;
    }

    public void setSs(RichCommandButton ss) {
        this.ss = ss;
    }

    public RichCommandButton getSs() {
        return ss;
    }

    public String cb1_action2() {
//        System.out.println("doing action");
//        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
//        ViewObjectImpl vo = am.getVO_Test1();
//        vo.executeQuery();
//        RowSetIterator rsItr = vo.createRowSetIterator(null);
//        rsItr.reset();
//        Row[] rows = rsItr.getAllRowsInRange();
//        int count =0;
//        while (rows.length > 0) {
//            for (int i = 0; i < rows.length; i++) {
//                count++;
//                if (rows[i].getAttribute("Id").equals("1")) {
//                    rows[i].setAttribute("Name", "Usman Riaz");
//                }
//            }
//            rows = rsItr.getNextRangeSet();
//
//        }
//        am.getTransaction().commit();
//        vo.executeQuery();
//
        return null;
    }

    public void setTestTable(RichTable testTable) {
        this.testTable = testTable;
    }

    public RichTable getTestTable() {
        return testTable;
    }

    public void setRichTextEditor(RichTextEditor richTextEditor) {
        this.richTextEditor = richTextEditor;
    }

    public RichTextEditor getRichTextEditor() {
        return richTextEditor;
    }

    public String cb1_action3() {
        // Add event code here...
        System.out.println(richTextEditor.getValue().toString());
        return null;
    }

    public void setRichText(String richText) {
        this.richText = richText;
    }

    public String getRichText() {
        return richText;
    }

    public String testdeduction() {
        PolicyHelper ph = new PolicyHelper();
        List<MonthlyDeductedLeave> leaves =
            ph.getMonthlyLeaves(CommonUtil.getMM_YYYY(new Date()),
                                (HubModuleImpl)CommonUtil.getAppModule());
        
        return null;
    }


    public void setNow(oracle.jbo.domain.Date now) {
        this.now = now;
    }

    public oracle.jbo.domain.Date getNow() {
        Date javaDate = Calendar.getInstance().getTime();
        java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
        oracle.jbo.domain.Date jboDate = new oracle.jbo.domain.Date(sqlDate);
        return jboDate;
        
    }

    public void setMonth(Number month) {
        this.month = month;
    }

    public Number getMonth() {
        return new Number(Calendar.getInstance().get(Calendar.MONTH)+1);
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getYear() {
        return new Long(Calendar.getInstance().get(Calendar.YEAR));
    }
    
    public String cb14_action() {
        // Add event code here...
       



        return null;
    }

    public String cb1_action4() {
        PolicyHelper ph = new PolicyHelper();
        List<MonthlyDeductedLeave> leaves =
            ph.getMonthlyLeaves(CommonUtil.getMM_YYYY(new Date()),
                                (HubModuleImpl)CommonUtil.getAppModule());
        for (MonthlyDeductedLeave mt : leaves) {
            CommonUtil.log(mt.getLeaveType()+" "+mt.getLeaveDate());
        }
                                                                                   
                                              
        return null;
    }

    public String getdate() {
        // Add event code here...
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        System.out.println(c.get(Calendar.DAY_OF_MONTH)+" "+CommonUtil.getMM_YYYY(c.getTime()));
        return null;
    }
}
